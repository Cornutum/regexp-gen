//////////////////////////////////////////////////////////////////////////////
// 
//                    Copyright 2020, Cornutum Project
//                             www.cornutum.org
//
//////////////////////////////////////////////////////////////////////////////

package org.cornutum.regexpgen.js;

import org.cornutum.regexpgen.Bounds;
import org.cornutum.regexpgen.RegExpGen;
import org.cornutum.regexpgen.util.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
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
    return
      IntStream.range( 0, members_.size())
      .map( i -> members_.get(i).getMinLength())
      .min()
      .orElse( 0);
    }

  /**
   * Returns the maximum length for any matching string.
   */
  public int getMaxLength()
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
  public String generate( Random random, Bounds bounds)
    {
    return null;
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
