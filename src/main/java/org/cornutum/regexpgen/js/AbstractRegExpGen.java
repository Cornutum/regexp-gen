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
import org.cornutum.regexpgen.RegExpGen;
import org.cornutum.regexpgen.util.ToString;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * Base class for {@link RegExpGen} implementations.
 */
public abstract class AbstractRegExpGen implements RegExpGen
  {
  /**
   * Creates a new AbstractRegExpGen instance.
   */
  protected AbstractRegExpGen( GenOptions options)
    {
    this( options, 1);
    }
  
  /**
   * Creates a new AbstractRegExpGen instance.
   */
  protected AbstractRegExpGen( GenOptions options, int length)
    {
    this( options, length, length);
    }
  
  /**
   * Creates a new AbstractRegExpGen instance.
   */
  protected AbstractRegExpGen( GenOptions options, Integer minOccur, Integer maxOccur)
    {
    options_ = options;
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
   * Returns the {@link GenOptions options} for this generator.
   */
  public GenOptions getOptions()
    {
    return options_;
    }

  /**
   * Returns a random string within the given bounds that matches this regular expression.
   */
  public String generate( RandomGen random, Bounds bounds)
    {
    return generateLength( random, effectiveLength( bounds));
    }

  /**
   * Returns a random string within the given bounds that matches this regular expression.
   */
  protected abstract String generateLength( RandomGen random, Bounds length);
  
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
   * Changes the regular expression string from which this generator was derived.
   */
  void setSource( String source)
    {
    source_ = source;
    }

  /**
   * Returns the regular expression string from which this generator was derived.
   */
  @Override
  public String getSource()
    {
    return source_;
    }

  /**
   * Implements the Visitor pattern for {@link RegExpGen} implementations.
   */
  public abstract void accept( RegExpGenVisitor visitor);

  public String toString()
    {
    return
      ToString.getBuilder( this)
      .append( Objects.toString( getSource(), ""))
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
      && other.getOccurrences().equals( getOccurrences())
      && other.isAnchoredStart() == isAnchoredStart()
      && other.isAnchoredEnd() == isAnchoredEnd()
      ;
    }

  public int hashCode()
    {
    return
      getClass().hashCode()
      ^ getOccurrences().hashCode()
      ^ Boolean.hashCode( isAnchoredStart())
      ^ Boolean.hashCode( isAnchoredEnd())
      ;
    }

  private String source_;
  private Bounds occurrences_;
  private boolean anchoredStart_ = false;
  private boolean anchoredEnd_ = false;
  private final GenOptions options_;

  public static final GenOptions BUILDER_OPTIONS = new GenOptions();
  
  /**
   * Builds an {@link AbstractRegExpGen} instance.
   */
  @SuppressWarnings("unchecked")
  protected static abstract class BaseBuilder<T extends BaseBuilder<T>>
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

	public T anchoredStart( boolean anchored)
      {
      getAbstractRegExpGen().setAnchoredStart( anchored);
      return (T) this;
      }

	public T anchoredStart()
      {
      return anchoredStart( true);
      }

	public T anchoredEnd( boolean anchored)
      {
      getAbstractRegExpGen().setAnchoredEnd( anchored);
      return (T) this;
      }

	public T anchoredEnd()
      {
      return anchoredEnd( true);
      }
    }
  }
