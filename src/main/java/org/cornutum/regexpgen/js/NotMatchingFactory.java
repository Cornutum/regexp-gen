//////////////////////////////////////////////////////////////////////////////
// 
//                    Copyright 2022, Cornutum Project
//                             www.cornutum.org
//
//////////////////////////////////////////////////////////////////////////////

package org.cornutum.regexpgen.js;

import org.cornutum.regexpgen.RegExpGen;
import org.cornutum.regexpgen.util.CharUtils;

import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.collections4.SetUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.*;

/**
 * Creates a RegExpGen that produces strings that do NOT match a specified RegExpGen.
 */
public class NotMatchingFactory implements RegExpGenVisitor
  {
  /**
   * Returns RegExpGen that produces strings that do NOT match the specified RegExpGen.
   * Returns {@link Optional#empty} if unable to generate not-matching strings.
   */
  public static Optional<RegExpGen> makeFrom( AbstractRegExpGen regExpGen)
    {
    RegExpGen notMatching;

    // Get all matching sequences, ignoring those that match only an empty string.
    boolean anchored = regExpGen.isAnchoredStart() && regExpGen.isAnchoredEnd();
    List<List<CharClassGen>> sequences =
      NotMatchingFactory.sequencesFor( regExpGen)
      .stream()
      .filter( chars -> !(anchored && chars.isEmpty()))
      .collect( toList());
    
    // Empty string is the only possible match?
    if( sequences.isEmpty())
      {
      // Yes, then any non-empty string is "not matching"
      notMatching = withSource( new AnyPrintableGen( regExpGen.getOptions(), 1, null), ".+");
      }

    // Unanchored empty string a possible match?
    else if( sequences.stream().anyMatch( List::isEmpty))
      {
      // Yes, then any string could be a match.
      notMatching = null;
      }

    else
      {
      // No, try to build a "not matching" generator.
      List<CharClassGen> notSeq = new ArrayList<CharClassGen>();

      // Given all possible matching character class sequences...
      int nextPos;
      for( nextPos = 0,
             notMatching = null;

           isValidPos( sequences, nextPos)
             && notMatching == null;

           nextPos++)
        {
        Set<Character> anyOfBefore;
        Set<Character> noneOfBefore;
        for( anyOfBefore = new HashSet<Character>(),
               noneOfBefore = new HashSet<Character>();

             // While all char classes at this position can match an empty char...
             isValidPos( sequences, nextPos)
               && !isCharRequired( sequences, nextPos);

             nextPos++)
          {
          // ...accumulate all chars allowed or excluded up to this position
          anyOfBefore.addAll( isAnyOfAt( sequences, nextPos));
          noneOfBefore.addAll( isNoneOfAt( sequences, nextPos));
          }

        // No chars required at any position?
        if( !isValidPos( sequences, nextPos))
          {
          Set<Character> conflicted = SetUtils.intersection( anyOfBefore, noneOfBefore);
          if( conflicted.isEmpty())
            {
            // Are there any characters that are not allowed at any previous position?
            Optional.of(
              CharUtils.printableChars()
              .filter( c -> !anyOfBefore.contains( c) || noneOfBefore.contains( c))
              .limit( 16)
              .collect( toSet()))
              .filter( mismatched -> !mismatched.isEmpty())

              // If so, we have a mismatch and the "not matching" generator is complete
              .map( mismatched -> withSource( new AnyOfGen( regExpGen.getOptions(), mismatched), mismatched.stream().map( String::valueOf).collect( joining( ""))))
              .ifPresent( mismatch -> notSeq.add( mismatch));
            }
          }

        else
          {
          Set<Character> conflicted =
            SetUtils.intersection(
              SetUtils.union( anyOfBefore, isAnyOfAt( sequences, nextPos)),
              SetUtils.union( noneOfBefore, isNoneOfAt( sequences, nextPos)));

          if( !conflicted.isEmpty())
            {
            // Yes, allow a match in this position and look for a mismatch at the next position.
            notSeq.add(
              withSource(
                new AnyOfGen( regExpGen.getOptions(), conflicted),
                conflicted.stream().map( String::valueOf).collect( joining( ""))));
            }
          else
            {
            // Yes, are there chars that are both allowed and excluded at previous positions?
            

            // Are there any characters that are not allowed at this position?
            Optional.of( isAllowedAt( sequences, nextPos))
              .map( allowed -> CharUtils.printableChars().filter( c -> !(anyOfBefore.contains( c) || allowed.contains( c))).limit( 16).collect( toSet()))
              .filter( mismatched -> !mismatched.isEmpty())

              // If so, we have a mismatch at this position and the "not matching" generator is complete
              .map( mismatched -> withSource( new AnyOfGen( regExpGen.getOptions(), mismatched), mismatched.stream().map( String::valueOf).collect( joining( ""))))
              .ifPresent( mismatch -> notSeq.add( mismatch));
              
            notMatching =
              notSeq.isEmpty()?
              null :
        
              notSeq.size() == 1?
              notSeq.get(0) :
        
              withSource(
                new SeqGen( regExpGen.getOptions(), notSeq),
                notSeq.stream().map( String::valueOf).collect( joining( ",")));
            }
          }
        }
      }
    
    return Optional.ofNullable( notMatching);
    }

  private NotMatchingFactory()
    {
    }

  private static boolean isValidPos( List<List<CharClassGen>> sequences, int pos)
    {
    return sequences.stream().anyMatch( seq -> seq.size() > pos);
    }

  private static boolean isCharRequired( List<List<CharClassGen>> sequences, int pos)
    {
    return charsAt( sequences, pos).anyMatch( chars -> chars.getMinOccur() > 0);
    }

  private static Set<Character> isAllowedAt( List<List<CharClassGen>> sequences, int pos)
    {
    return
      charsAt( sequences, pos)
      .flatMap( chars -> Arrays.stream( chars.getChars()))
      .collect( toSet());
    }

  private static Set<Character> isAnyOfAt( List<List<CharClassGen>> sequences, int pos)
    {
    return
      charsAt( sequences, pos)
      .filter( chars -> chars instanceof AnyOfGen)
      .flatMap( chars -> chars.getCharSet().stream())
      .collect( toSet());
    }

  private static Set<Character> isNoneOfAt( List<List<CharClassGen>> sequences, int pos)
    {
    return
      charsAt( sequences, pos)
      .filter( chars -> chars instanceof NoneOfGen)
      .flatMap( chars -> chars.getCharSet().stream())
      .collect( toSet());
    }

  private static Stream<CharClassGen> charsAt( List<List<CharClassGen>> sequences, int pos)
    {
    return sequences.stream().filter( seq -> seq.size() > pos).map( seq -> seq.get( pos));
    }

  private static <T extends AbstractRegExpGen> T withSource( T regExpGen, String source)
    {
    regExpGen.setSource( source);
    return regExpGen;
    }
  
  protected static List<List<CharClassGen>> sequencesFor( AbstractRegExpGen regExpGen)
    {
    NotMatchingFactory factory = new NotMatchingFactory();
    regExpGen.accept( factory);

    List<CharClassGen> emptySeq = emptyList();
    return
      Stream.concat(
        factory.getSequences().stream(),

        Optional.of( emptySeq)
        .filter( empty -> regExpGen.getMinOccur() == 0)
        .map( Stream::of)
        .orElse( Stream.empty()))
      
      .collect( toList());
    }
  
  public void visit( AlternativeGen regExpGen)
    {
    sequences_ =
      IterableUtils.toList( regExpGen.getMembers())
      .stream()
      .flatMap( gen -> sequencesFor( gen).stream())
      .collect( toList());
    }
  
  public void visit( SeqGen regExpGen)
    {
    sequences_ =
      allSequences(
        IterableUtils.toList( regExpGen.getMembers())
        .stream()
        .map( NotMatchingFactory::sequencesFor)
        .collect( toList()));
    }
  
  public void visit( AnyOfGen regExpGen)
    {
    visitCharClass( regExpGen);
    }
  
  public void visit( NoneOfGen regExpGen)
    {
    visitCharClass( regExpGen);
    }
  
  public void visit( AnyPrintableGen regExpGen)
    {
    visitCharClass( regExpGen);
    }
  
  public void visitCharClass( CharClassGen regExpGen)
    {
    sequences_ =
      Optional.of( regExpGen)
      .filter( chars -> chars.getMaxOccur() > 0)
      .map( chars -> singletonList( singletonList( chars)))
      .orElse( emptyList());
    }

  private List<List<CharClassGen>> allSequences( List<List<List<CharClassGen>>> memberSequences)
    {
    List<List<CharClassGen>> all = new ArrayList<List<CharClassGen>>();
    int members = memberSequences.size();
    if( members == 1)
      {
      all.addAll( memberSequences.get(0));
      }
    else
      {
      allSequences( memberSequences.subList( 0, members - 1))
        .forEach( prevSeq -> {
          memberSequences.get( members - 1)
            .forEach( nextSeq -> all.add( Stream.concat( prevSeq.stream(), nextSeq.stream()).collect( toList())));
          });
      }
    
    return all;
    }

  private List<List<CharClassGen>> getSequences()
    {
    return sequences_;
    }

  private List<List<CharClassGen>> sequences_;
  }
