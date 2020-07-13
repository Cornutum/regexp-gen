//////////////////////////////////////////////////////////////////////////////
// 
//                    Copyright 2020, Cornutum Project
//                             www.cornutum.org
//
//////////////////////////////////////////////////////////////////////////////

package org.cornutum.regexpgen.js;

import java.util.stream.Stream;

import org.cornutum.regexpgen.Bounds;
import org.cornutum.regexpgen.RegExpGen;
import org.cornutum.regexpgen.util.ToString;

/**
 * Base class for {@link RegExpGen} implementations.
 */
public abstract class AbstractRegExpGen implements RegExpGen
  {
  /**
   * Creates a new AbstractRegExpGen instance.
   */
  protected AbstractRegExpGen()
    {
    this( 1);
    }
  
  /**
   * Creates a new AbstractRegExpGen instance.
   */
  protected AbstractRegExpGen( int length)
    {
    this( length, length);
    }
  
  /**
   * Creates a new AbstractRegExpGen instance.
   */
  protected AbstractRegExpGen( Integer minOccur, Integer maxOccur)
    {
    setOccurrences( minOccur, maxOccur);
    }
  
  /**
   * Changes the number of occurrences allowed for this regular expression.
   */
  public void setOccurrences( Integer minOccur, Integer maxOccur)
    {
    setOccurrences( new Bounds( minOccur, maxOccur));
    }
  
  /**
   * Changes the number of occurrences allowed for this regular expression.
   */
  public void setOccurrences( Bounds occurrences)
    {
    occurrences_ = occurrences;
    }
  
  /**
   * Returns the number of occurrences allowed for this regular expression.
   */
  public Bounds getOccurrences()
    {
    return occurrences_;
    }

  /**
   * Returns the minimum number occurrences allowed for this regular expression.
   */
  public int getMinOccur()
    {
    return occurrences_.getMinValue();
    }

  /**
   * Returns the maximum number occurrences allowed for this regular expression.
   */
  public int getMaxOccur()
    {
    return occurrences_.getMaxValue();
    }
  
  /**
   * Returns if any part of this regular expression must match the start of a string.
   */
  public boolean isAnchoredStart()
    {
    return isAnchoredStartAll();
    }
  
  /**
   * Returns if this regular expression must match the start of a string.
   */
  public boolean isAnchoredStartAll()
    {
    return anchoredStart_;
    }
  
  /**
   * Changes if this regular expression must match the start of a string.
   */
  public void setAnchoredStart( boolean anchored)
    {
    anchoredStart_ = anchored;
    }
  
  /**
   * Returns any part of if this regular expression must match the end of a string.
   */
  public boolean isAnchoredEnd()
    {
    return isAnchoredEndAll();
    }
  
  /**
   * Returns if this regular expression must match the end of a string.
   */
  public boolean isAnchoredEndAll()
    {
    return anchoredEnd_;
    }

  /**
   * Changes if this regular expression must match the end of a string.
   */
  public void setAnchoredEnd( boolean anchored)
    {
    anchoredEnd_ = anchored;
    }

  /**
   * Returns the possible starting subexpressions for this regular expression.
   */
  public Stream<AbstractRegExpGen> getStartAlternatives()
    {
    return Stream.of( this);
    }

  /**
   * Returns the possible ending subexpressions for this regular expression.
   */
  public Stream<AbstractRegExpGen> getEndAlternatives()
    {
    return Stream.of( this);
    }

  /**
   * Implements the Visitor pattern for {@link RegExpGen} implementations.
   */
  public abstract void accept( RegExpGenVisitor visitor);

  public String toString()
    {
    return
      ToString.getBuilder( this)
      .append( "occurs", getOccurrences())
      .toString();
    }

  public boolean equals( Object object)
    {
    AbstractRegExpGen other =
      object instanceof AbstractRegExpGen
      ? (AbstractRegExpGen) object
      : null;

    return
      other != null
      && other.getOccurrences().equals( getOccurrences());
    }

  public int hashCode()
    {
    return
      getClass().hashCode()
      ^ getOccurrences().hashCode();
    }
  
  private Bounds occurrences_;
  private boolean anchoredStart_ = false;
  private boolean anchoredEnd_ = false;

  /**
   * Builds an {@link AbstractRegExpGen} instance.
   */
  @SuppressWarnings("unchecked")
  public static abstract class BaseBuilder<T extends BaseBuilder<T>>
    {
    /**
     * Returns the {@link AbstractRegExpGen} instance for this builder.
     */
    protected abstract AbstractRegExpGen getAbstractRegExpGen();

	public T occurs( Integer minOccur, Integer maxOccur)
      {
      getAbstractRegExpGen().setOccurrences( minOccur, maxOccur);
      return (T) this;
      }

	public T occurs( int occurs)
      {
      getAbstractRegExpGen().setOccurrences( occurs, occurs);
      return (T) this;
      }

	public T occurs( Bounds bounds)
      {
      getAbstractRegExpGen().setOccurrences( bounds);
      return (T) this;
      }
    }
  }
