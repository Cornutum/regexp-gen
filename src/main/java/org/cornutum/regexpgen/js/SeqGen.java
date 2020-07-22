//////////////////////////////////////////////////////////////////////////////
// 
//                    Copyright 2020, Cornutum Project
//                             www.cornutum.org
//
//////////////////////////////////////////////////////////////////////////////

package org.cornutum.regexpgen.js;

import org.cornutum.regexpgen.Bounds;
import org.cornutum.regexpgen.RandomGen;
import org.cornutum.regexpgen.RegExpGen;
import org.cornutum.regexpgen.util.ToString;
import static org.cornutum.regexpgen.Bounds.UNBOUNDED;
import static org.cornutum.regexpgen.Bounds.bounded;
import static org.cornutum.regexpgen.Bounds.dividedBy;
import static org.cornutum.regexpgen.Bounds.productOf;
import static org.cornutum.regexpgen.Bounds.reduceBy;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Generates strings matching a sequence of regular expressions.
 */
public class SeqGen extends AbstractRegExpGen
  {
  /**
   * Creates a new SeqGen instance.
   */
  public SeqGen()
    {
    super();
    }
  
  /**
   * Creates a new SeqGen instance.
   */
  public SeqGen( RegExpGen... members)
    {
    for( RegExpGen member : members)
      {
      add( member);
      }
    }
  
  /**
   * Creates a new SeqGen instance.
   */
  public <T extends RegExpGen> SeqGen( Iterable<T> members)
    {
    for( RegExpGen member : members)
      {
      add( member);
      }
    }

  /**
   * Adds a regular expression to this sequence.
   */
  public void add( RegExpGen member)
    {
    if( member != null)
      {
      members_.add( member);
      }
    }

  /**
   * Add a sequence of characters to this sequence.
   */
  public void add( String chars)
    {
    Stream.of( Optional.ofNullable( chars).orElse( ""))
      .flatMap( s -> IntStream.range( 0, s.length()).mapToObj( i -> new Character( s.charAt(i))))
      .forEach( c -> add( new AnyOfGen( c)));
    }

  /**
   * Returns the members of this sequence.
   */
  public Iterable<RegExpGen> getMembers()
    {
    return members_;
    }

  /**
   * Returns the minimum length for any matching string.
   */
  public int getMinLength()
    {
    return productOf( getMinOccur(), getMembersMinLength());
    }

  /**
   * Returns the maximum length for any matching string.
   */
  public int getMaxLength()
    {
    return productOf( getMaxOccur(), getMembersMaxLength());
    }

  /**
   * Returns the minimum length for any matching sequence.
   */
  protected int getMembersMinLength()
    {
    return getRemainingMinLength( 0);
    }

  /**
   * Returns the minimum length for any matching subsequence starting with the i-th member
   */
  private int getRemainingMinLength( int start)
    {
    return
      IntStream.range( start, members_.size())
        .map( i -> members_.get(i).getMinLength())
        .reduce( Bounds::sumOf)
        .orElse( 0);
    }

  /**
   * Returns the maximum length for any matching subsequence starting with the i-th member
   */
  private int getRemainingMaxLength( int start)
    {
    return
      IntStream.range( start, members_.size())
        .map( i -> members_.get(i).getMaxLength())
        .reduce( Bounds::sumOf)
        .orElse( 0);
    }

  /**
   * Returns the maximum length for any matching sequence.
   */
  protected int getMembersMaxLength()
    {
    return
      IntStream.range( 0, members_.size())
        .map( i -> members_.get(i).getMaxLength())
        .reduce( Bounds::sumOf)
        .orElse( 0);
    }

  /**
   * Returns a random string within the given bounds that matches this regular expression.
   */
  protected String generateLength( RandomGen random, Bounds length)
    {
    StringBuilder matching = new StringBuilder();

    if( getMaxLength() > 0)
      {
      // Given a range of lengths...
      int lengthMin = length.getMinValue();
      int lengthMax = length.getMaxValue();
      
      // ...allowing for a range of occurrences...
      int memberMin = getMembersMinLength();
      int memberMax = getMembersMaxLength();
      int mayOccurMin = Math.max( 1, lengthMin / memberMax);
      int mayOccurMax = bounded( lengthMax).map( max -> Math.max( 1, dividedBy( max, memberMin))).orElse( UNBOUNDED);
      Bounds mayOccur =
        new Bounds( mayOccurMin, mayOccurMax)
        .clippedTo( "Occurrences", getMinOccur(), getMaxOccur());

      // ...for a random number of occurrences...
      int targetOccur = random.within( mayOccur);
      int targetLength = bounded( lengthMax).orElse( targetOccur * random.within( memberMin, memberMax));

      // ...generate a random match for each occurrence
      int remaining;
      for( remaining = targetLength;
           targetOccur > 0 && remaining > 0;
           targetOccur--, remaining = targetLength - matching.length())
        {
        // Next occurrence match complete?
        int nextMin = targetOccur == 1? Math.max( 0, length.getMinValue() - matching.length()) : 0;
        int nextMax = remaining / targetOccur;
        String seqMatch = completeSeq( random, 0, nextMin, nextMax);
        if( seqMatch != null)
          {
          // Yes, continue to next occurrence
          matching.append( seqMatch);
          }
        else
          {
          // No, no more occurrences are possible
          targetOccur = 0;
          }
        }
      }

    return matching.toString();
    }

  /**
   * Completes a random string that matches this sequence starting with the i'th member.
   */
  private String completeSeq( RandomGen random, int i, int needed, int remaining)
    {
    RegExpGen member = members_.get(i);
    int memberMin = member.getMinLength();
    int memberMax = member.getMaxLength();

    String matching;
    int memberMatchMin; 
    int memberMatchMax;
    for( matching = null,
           memberMatchMax = remaining - getRemainingMinLength( i+1),
           memberMatchMin = Math.min( memberMax, Math.max( memberMin, reduceBy( needed, getRemainingMaxLength( i+1))));
         
         matching == null
           && memberMatchMax >= memberMatchMin
           && memberMatchMin >= memberMin;

         memberMatchMin--)
      {
      String memberMatch = members_.get(i).generate( random, new Bounds( memberMatchMin, memberMatchMax));

      matching =
        memberMatch != null && i+1 < members_.size()?
        
        Optional.ofNullable( completeSeq( random, i+1, needed - memberMatch.length(), remaining - memberMatch.length()))
        .map( remainingMatch -> memberMatch + remainingMatch)
        .orElse( null) :

        memberMatch;
      }

    return matching;
    }

  /**
   * Returns if any part of this regular expression must match the start of a string.
   */
  public boolean isAnchoredStart()
    {
    return
      isAnchoredStartAll()
      || getStartAlternatives().anyMatch( r -> r.isAnchoredStart());
    }

  /**
   * Returns if any part of this regular expression must match the end of a string.
   */
  public boolean isAnchoredEnd()
    {
    return
      isAnchoredEndAll()
      || getEndAlternatives().anyMatch( r -> r.isAnchoredEnd());
    }

  /**
   * Returns the possible starting subexpressions for this regular expression.
   */
  public Stream<AbstractRegExpGen> getStartAlternatives()
    {
    return
      members_.isEmpty()?
      Stream.empty() :
      ((AbstractRegExpGen) members_.get(0)).getStartAlternatives();
    }

  /**
   * Returns the possible ending subexpressions for this regular expression.
   */
  public Stream<AbstractRegExpGen> getEndAlternatives()
    {
    return
      members_.isEmpty()?
      Stream.empty() :
      ((AbstractRegExpGen) members_.get( members_.size() - 1)).getEndAlternatives();
    }

  /**
   * Implements the Visitor pattern for {@link RegExpGen} implementations.
   */
  public void accept( RegExpGenVisitor visitor)
    {
    visitor.visit( this);
    }

  /**
   * Returns an {@link SeqGen} builder.
   */
  public static Builder builder()
    {
    return new Builder();
    }

  public String toString()
    {
    return
      ToString.getBuilder( this)
      .append( "members", members_)
      .appendSuper( super.toString())
      .toString();
    }

  public boolean equals( Object object)
    {
    SeqGen other =
      object != null && object.getClass().equals( getClass())
      ? (SeqGen) object
      : null;

    return
      other != null
      && super.equals( other)
      && other.members_.equals( members_);
    }

  public int hashCode()
    {
    return
      super.hashCode()
      ^ members_.hashCode();
    }

  private List<RegExpGen> members_ = new ArrayList<RegExpGen>();

  /**
   * Builds a {@link SeqGen} instance.
   */
  public static class Builder extends BaseBuilder<Builder>
    {
    /**
     * Returns the {@link AbstractRegExpGen} instance for this builder.
     */
    protected AbstractRegExpGen getAbstractRegExpGen()
      {
      return seq_;
      }

	public Builder add( RegExpGen... members)
      {
      for( RegExpGen member : members)
        {
        seq_.add( member);
        }
      return this;
      }

	public Builder addAll( Iterable<RegExpGen> members)
      {
      for( RegExpGen member : members)
        {
        seq_.add( member);
        }
      return this;
      }

	public Builder add( String chars)
      {
      seq_.add( chars);
      return this;
      }

    public SeqGen build()
      {
      return seq_;
      }
      
    private SeqGen seq_ = new SeqGen();
    }  
  }
