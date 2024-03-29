//////////////////////////////////////////////////////////////////////////////
// 
//                    Copyright 2020, Cornutum Project
//                             www.cornutum.org
//
//////////////////////////////////////////////////////////////////////////////

package org.cornutum.regexpgen.js;

import org.cornutum.regexpgen.Bounds;
import org.cornutum.regexpgen.GenOptions;
import org.cornutum.regexpgen.RandomGen;
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
  public AlternativeGen( GenOptions options)
    {
    super( options);
    }
  
  /**
   * Creates a new AlternativeGen instance.
   */
  public AlternativeGen( GenOptions options, AbstractRegExpGen... members)
    {
    this( options);
    for( AbstractRegExpGen member : members)
      {
      add( member);
      }
    }
  
  /**
   * Creates a new AlternativeGen instance.
   */
  public <T extends AbstractRegExpGen> AlternativeGen( GenOptions options, Iterable<T> members)
    {
    this( options);
    for( AbstractRegExpGen member : members)
      {
      add( member);
      }
    }

  /**
   * Adds an alternative regular expression.
   */
  public void add( AbstractRegExpGen member)
    {
    members_.add( member);
    }

  /**
   * Returns the alternative regular expressions.
   */
  public Iterable<AbstractRegExpGen> getMembers()
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
      int mayOccurMin = lengthMin / memberMax;
      int mayOccurMax = bounded( lengthMax).map( max -> dividedBy( max, memberMin)).orElse( UNBOUNDED);
      Bounds mayOccur =
        new Bounds( mayOccurMin, mayOccurMax)
        .clippedTo( "Occurrences", getMinOccur(), getMaxOccur());

      // ...for a random number of occurrences...
      int targetOccur = random.within( mayOccur);
      int targetLength = bounded( lengthMax).orElse( targetOccur * random.within( new Bounds( memberMin, memberMax)));

      // ...generate a random match for each occurrence
      int remaining;
      int needed;
      for( remaining = targetLength,
             needed = lengthMin;
           
           targetOccur > 0
             && remaining > 0;
           
           targetOccur--,
             remaining = targetLength - matching.length(),
             needed = lengthMin - matching.length())
        {
        // Can some random member generate the next occurrence?
        int nextMin = needed / targetOccur;
        int nextMax = remaining / targetOccur;
        String alternativeMatch = completeAlternative( random, nextMin, nextMax);
        if( alternativeMatch != null)
          {
          // Yes, append the next occurrence
          matching.append( alternativeMatch);
          }
        else
          {
          // No, no more occurrences are possible now
          targetOccur = 0;
          }
        }
      }

    return matching.toString();
    }

  /**
   * Completes a random string with the given range that matches some alternative.
   */
  private String completeAlternative( RandomGen random, int needed, int remaining)
    {
    String memberMatch;
    int nextMin;
    for( memberMatch = null, 
           nextMin = needed;

         memberMatch == null
           && nextMin >= 0;

         nextMin--)
      {
      Bounds nextBounds= new Bounds( nextMin, remaining);

      memberMatch =
        memberFeasibleFor( random, nextBounds)
        .map( member -> member.generate( random, nextBounds))
        .orElse( null);
      }

    return memberMatch;
    }

  /**
   * Returns a member that can generate a string within the given bounds.
   */
  private Optional<AbstractRegExpGen> memberFeasibleFor( RandomGen random, Bounds length)
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
    return members_.stream().flatMap( r -> r.getStartAlternatives());
    }

  /**
   * Returns the possible ending subexpressions for this regular expression.
   */
  public Stream<AbstractRegExpGen> getEndAlternatives()
    {
    return members_.stream().flatMap( r -> r.getEndAlternatives());
    }

  /**
   * Implements the Visitor pattern for {@link AbstractRegExpGen} implementations.
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

  /**
   * Returns an {@link AlternativeGen} builder.
   */
  public static Builder builder( GenOptions options)
    {
    return new Builder( options);
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

  private List<AbstractRegExpGen> members_ = new ArrayList<AbstractRegExpGen>();

  /**
   * Builds an {@link AlternativeGen} instance.
   */
  public static class Builder extends BaseBuilder<Builder>
    {
    public Builder()
      {
      this( BUILDER_OPTIONS);
      }

    public Builder( GenOptions options)
      {
      alternative_ = new AlternativeGen( options);
      }
    
    /**
     * Returns the {@link AbstractRegExpGen} instance for this builder.
     */
    protected AbstractRegExpGen getAbstractRegExpGen()
      {
      return alternative_;
      }

	public Builder add( AbstractRegExpGen... members)
      {
      for( AbstractRegExpGen member : members)
        {
        alternative_.add( member);
        }
      return this;
      }

	public Builder addAll( Iterable<AbstractRegExpGen> members)
      {
      for( AbstractRegExpGen member : members)
        {
        alternative_.add( member);
        }
      return this;
      }

    public AlternativeGen build()
      {
      return alternative_;
      }
      
    private AlternativeGen alternative_; 
    }
  }
