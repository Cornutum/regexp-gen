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
import java.util.Optional;
import java.util.Random;
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
    return
      IntStream.range( 0, members_.size())
      .map( i -> members_.get(i).getMinLength())
      .reduce( Bounds::sumOf)
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
      .reduce( Bounds::sumOf)
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
