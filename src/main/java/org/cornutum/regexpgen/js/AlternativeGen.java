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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Generates strings matching one of a set of alternative regular expressions.
 */
public class AlternativeGen extends AbstractRegExpGen
  {
  /**
   * Creates a new AlternativeGen instance.
   */
  public AlternativeGen()
    {
    super();
    }
  
  /**
   * Creates a new AlternativeGen instance.
   */
  public AlternativeGen( RegExpGen... members)
    {
    for( RegExpGen member : members)
      {
      add( member);
      }
    }
  
  /**
   * Creates a new AlternativeGen instance.
   */
  public <T extends RegExpGen> AlternativeGen( Iterable<T> members)
    {
    for( RegExpGen member : members)
      {
      add( member);
      }
    }

  /**
   * Adds an alternative regular expression.
   */
  public void add( RegExpGen member)
    {
    members_.add( member);
    }

  /**
   * Returns the alternative regular expressions.
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
    return productOf( getMinOccur(), getMemberMinLength());
    }

  /**
   * Returns the maximum length for any matching string.
   */
  public int getMaxLength()
    {
    return productOf( getMaxOccur(), getMemberMaxLength());
    }

  /**
   * Returns the minimum length of a string that can match some member.
   */
  protected int getMemberMinLength()
    {
    return
      IntStream.range( 0, members_.size())
      .map( i -> members_.get(i).getMinLength())
      .min()
      .orElse( 0);
    }

  /**
   * Returns the maximum length of a string that can match some member.
   */
  protected int getMemberMaxLength()
    {
    return
      IntStream.range( 0, members_.size())
      .map( i -> members_.get(i).getMaxLength())
      .max()
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
      int memberMin = getMemberMinLength();
      int memberMax = getMemberMaxLength();
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
        // Can some random member generate the next occurrence?
        int nextMin = targetOccur == 1? Math.max( 0, length.getMinValue() - matching.length()) : 0;
        int nextMax = remaining / targetOccur;
        Bounds next = new Bounds( nextMin, nextMax);
        Optional<RegExpGen> nextMember = memberFeasibleFor( random, next);
        if( nextMember.isPresent())
          {
          // Yes, append the next occurrence
          matching.append( nextMember.get().generate( random, next));
          }
        else
          {
          // No, no more occurrences are possible now
          targetLength = matching.length();
          }
        }
      }

    return matching.toString();
    }

  /**
   * Returns a member that can generate a string within the given bounds.
   */
  private Optional<RegExpGen> memberFeasibleFor( RandomGen random, Bounds length)
    {
    return
      random.shuffle( members_)
      .stream()
      .filter( member -> member.isFeasibleLength( length))
      .findFirst();
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
    return members_.stream().flatMap( r -> ((AbstractRegExpGen) r).getStartAlternatives());
    }

  /**
   * Returns the possible ending subexpressions for this regular expression.
   */
  public Stream<AbstractRegExpGen> getEndAlternatives()
    {
    return members_.stream().flatMap( r -> ((AbstractRegExpGen) r).getEndAlternatives());
    }

  /**
   * Implements the Visitor pattern for {@link RegExpGen} implementations.
   */
  public void accept( RegExpGenVisitor visitor)
    {
    visitor.visit( this);
    }

  /**
   * Returns an {@link AlternativeGen} builder.
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
    AlternativeGen other =
      object != null && object.getClass().equals( getClass())
      ? (AlternativeGen) object
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
   * Builds an {@link AlternativeGen} instance.
   */
  public static class Builder extends BaseBuilder<Builder>
    {
    /**
     * Returns the {@link AbstractRegExpGen} instance for this builder.
     */
    protected AbstractRegExpGen getAbstractRegExpGen()
      {
      return alternative_;
      }

	public Builder add( RegExpGen... members)
      {
      for( RegExpGen member : members)
        {
        alternative_.add( member);
        }
      return this;
      }

	public Builder addAll( Iterable<RegExpGen> members)
      {
      for( RegExpGen member : members)
        {
        alternative_.add( member);
        }
      return this;
      }

    public AlternativeGen build()
      {
      return alternative_;
      }
      
    private AlternativeGen alternative_ = new AlternativeGen();
    }
  }
