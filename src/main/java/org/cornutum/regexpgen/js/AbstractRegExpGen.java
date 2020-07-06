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

  public String toString()
    {
    return
      ToString.getBuilder( this)
      .append( "occurs", getOccurrences())
      .toString();
    }

  private Bounds occurrences_;

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
    }
  }
