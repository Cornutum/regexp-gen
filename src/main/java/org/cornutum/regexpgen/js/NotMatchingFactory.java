//////////////////////////////////////////////////////////////////////////////
// 
//                    Copyright 2022, Cornutum Project
//                             www.cornutum.org
//
//////////////////////////////////////////////////////////////////////////////

package org.cornutum.regexpgen.js;

import org.cornutum.regexpgen.RegExpGen;
import org.cornutum.regexpgen.util.CharUtils;
import static org.cornutum.regexpgen.Bounds.bounded;

import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.collections4.SetUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
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
    AbstractRegExpGen notMatching;

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
      notMatching = withSource( new AnyPrintableGen( regExpGen.getMatchOptions(), 1, null), ".+");
      }

    // Unanchored empty string a possible match?
    else if( sequences.stream().anyMatch( List::isEmpty))
      {
      // Yes, then any string could be a match.
      notMatching = null;
      }

    else
      {
      // Given all possible matching initial character class sequences...
      List<AbstractRegExpGen> notAlternatives = new ArrayList<AbstractRegExpGen>();

      Set<CharClassGen> initial = getInitialRequired( sequences);
      Set<CharClassGen> initialPrefix = getInitialPrefix( sequences);
      Set<Character> initialAny = getAnyOf( initial);
      Set<Character> initialNone = getNoneOf( initial);

      Set<Character> mismatching =
        // Are initial chars defined and consistent?
        initialAny.equals( initialNone)?

        // No, can't determine a mismatching generator
        null :

        // Yes, find a set of mismatching chars that are...
        (initialNone.isEmpty()
         // ... not among those required to be allowed
         ? regExpGen.getMatchOptions().getAnyPrintableChars().stream().filter( c -> !initialAny.contains( c))

         // ... or required to be excluded
         : SetUtils.difference( initialNone, initialAny).stream())

        // ... and are excluded from any optional prefix
        .filter( c -> isExcluded( initialPrefix, c))
        .limit( 32)
        .collect( toSet());

      // Mismatching chars found?
      if( Optional.ofNullable( mismatching).map( m -> !m.isEmpty()).orElse( false))
        {
        // Yes, include a mismatching generator.
        notAlternatives.add(
          withLength(
            withSource( new AnyOfGen( regExpGen.getMatchOptions(), mismatching), mismatching),
            bounded( regExpGen.getMaxLength()).map( max -> max + 1).orElse( 1),
            null));
        }

      // Minimum length required?
      if( regExpGen.getMinLength() > 0)
        {
        // Yes, include a too-short generator
        Set<Character> allowed =
          initial.stream()
          .flatMap( chars -> Arrays.stream( chars.getChars()))
          .limit( 32)
          .collect( toSet());
          
        notAlternatives.add(
          withLength(
            withSource( new AnyOfGen( regExpGen.getMatchOptions(), allowed), allowed),
            0,
            regExpGen.getMinLength() - 1));
        }

      notMatching =
        notAlternatives.isEmpty()?
        null :

        notAlternatives.size() == 1?
        notAlternatives.get(0) :

        withSource( new AlternativeGen( regExpGen.getMatchOptions(), notAlternatives), notAlternatives);
      }

    return Optional.ofNullable( notMatching).map( NotMatchingFactory::withAnchors);
    }

  private NotMatchingFactory()
    {
    }

  /**
   * Returns the generators for the initial required char of any matching sequence.
   */
  private static Set<CharClassGen> getInitialRequired( List<List<CharClassGen>> sequences)
    {
    return initialChars( sequences).filter( chars -> chars.getMinOccur() > 0).collect( toSet());
    }

  /**
   * Returns the generators for any optional prefix of any matching sequence.
   */
  private static Set<CharClassGen> getInitialPrefix( List<List<CharClassGen>> sequences)
    {
    return initialChars( sequences).filter( chars -> chars.getMinOccur() == 0).collect( toSet());
    }

  /**
   * Returns true if the given character does not match any of the given the generators.
   */
  private static boolean isExcluded( Set<CharClassGen> prefix, Character c)
    {
    return prefix.stream().allMatch( chars -> isExcluded( chars, c));
    }

  /**
   * Returns true if the given character does not match the given generator.
   */
  private static boolean isExcluded( CharClassGen chars, Character c)
    {
    return
      chars instanceof NoneOfGen?
      chars.getCharSet().contains( c) :

      chars instanceof AnyPrintableGen?
      CharUtils.isLineTerminator( c) :

      !chars.getCharSet().contains( c);
    }

  /**
   * Returns the generators of the given type for the initial char of any matching sequence.
   */
  private static Set<Character> getAnyOf( Set<CharClassGen> candidates)
    {
    return
      candidates.stream()
      .filter( chars -> chars instanceof AnyOfGen)
      .flatMap( chars -> chars.getCharSet().stream())
      .collect( toSet());
    }

  /**
   * Returns the generators of the given type for the initial char of any matching sequence.
   */
  private static Set<Character> getNoneOf( Set<CharClassGen> candidates)
    {
    return
      candidates.stream()
      .filter( chars -> chars instanceof NoneOfGen)
      .flatMap( chars -> chars.getCharSet().stream())
      .collect( toSet());
    }

  /**
   * Returns the generators for the initial char of any matching sequence.
   */
  private static Stream<CharClassGen> initialChars( List<List<CharClassGen>> sequences)
    {
    return sequences.stream().filter( seq -> seq.size() > 0).map( seq -> seq.get( 0));
    }

  private static <T extends AbstractRegExpGen> T withSource( T regExpGen, Set<Character> source)
    {
    return withSource( regExpGen, source.stream().map( CharUtils::charClassLiteral).collect( joining( "", "[", "]")));
    }

  private static AlternativeGen withSource( AlternativeGen alternativeGen, List<AbstractRegExpGen> members)
    {
    return withSource( alternativeGen, members.stream().map( RegExpGen::getSource).collect( joining( "|", "(", ")")));
    }

  private static <T extends AbstractRegExpGen> T withSource( T regExpGen, String source)
    {
    regExpGen.setSource( source);
    return regExpGen;
    }

  private static <T extends AbstractRegExpGen> T withLength( T regExpGen, int minLength, Integer maxLength)
    {
    regExpGen.setOccurrences( minLength, maxLength);
    return withSource( regExpGen, String.format( "%s{%s,%s}", regExpGen.getSource(), minLength, Objects.toString( maxLength, "")));
    }

  private static <T extends AbstractRegExpGen> T withAnchors( T regExpGen)
    {
    return
      Optional.ofNullable( regExpGen)
      .map( r -> {
        r.setAnchoredStart( true);
        r.setAnchoredEnd( true);
        return withSource( r, String.format( "^%s$", r.getSource()));
        })
      .orElse( null);
    }
  
  /**
   * Returns all matching generator sequences.
   */
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

  /**
   * Returns the cross product of all matching generator sequences for each member of a SeqGen.
   */
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
