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
    return Bounds.productOf( getMinOccur(), getMembersMinLength());
    }

  /**
   * Returns the maximum length for any matching string.
   */
  public int getMaxLength()
    {
    return Bounds.productOf( getMaxOccur(), getMembersMaxLength());
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

    // Given a random target length...
    int targetLength = random.within( length);
    if( targetLength > 0 && getMaxLength() > 0)
      {
      // ...allowing for a range of occurrences...
      int memberMin = getMembersMinLength();
      int memberMax = getMembersMaxLength();
      int mayOccurMin = Math.max( 1, targetLength / memberMax);
      int mayOccurMax = memberMin==0? targetLength : Math.max( 1, targetLength / memberMin);
      Bounds mayOccur =
        new Bounds( mayOccurMin, mayOccurMax)
        .clippedTo( "Occurrences", getMinOccur(), getMaxOccur());

      // ...for a random number of occurrences...
      int targetOccur = random.within( mayOccur);
      if( targetOccur > 0)
        {
        // ...generate a random match for each occurrence
        int remaining;
        for( remaining = targetLength;
             targetOccur > 0 && remaining > 0;
             targetOccur--, remaining = targetLength - matching.length())
          {
          matching.append( generateSeq( random, new Bounds( remaining / targetOccur)));
          }
        }
      }

    return matching.toString();
    }

  /**
   * Returns a random string within the given bounds that matches this regular expression.
   */
  private String generateSeq( RandomGen random, Bounds length)
    {
    StringBuilder matching = new StringBuilder();

    int i;
    int remaining;
    Bounds nextBounds;
    int max = length.getMaxValue();
    for( i = 0, 
           remaining = max,
           nextBounds = getNextBounds( remaining, i);
         
         i < members_.size()
           && nextBounds != null
           && members_.get(i).isFeasibleLength( nextBounds);
         
         i++,
           remaining = max - matching.length(),
           nextBounds = getNextBounds( remaining, i))
      {
      matching.append( members_.get(i).generate( random, nextBounds));
      }

    return
      // Match generated for full sequence?
      i < members_.size()

      // No, empty string is the only possible match
      ? ""

      // Yes, return complete match
      : matching.toString();
    }

  /**
   * Returns the bounds for a match for the i-th member of this sequence, given the specified number
   * of chars remaining for a complete match. Return null if a match for this member is no longer possible.
   */
  private Bounds getNextBounds( int remaining, int i)
    {
    int next = remaining - getRemainingMinLength( i+1);
    return
      next >= 0
      ? new Bounds( next)
      : null;
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
