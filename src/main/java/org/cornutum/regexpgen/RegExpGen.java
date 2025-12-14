//////////////////////////////////////////////////////////////////////////////
// 
//                    Copyright 2020, Cornutum Project
//                             www.cornutum.org
//
//////////////////////////////////////////////////////////////////////////////

package org.cornutum.regexpgen;

/**
 * Generates strings that match a regular expression.
 */
public interface RegExpGen extends Comparable<RegExpGen>
  {
  /**
   * Returns the minimum length for any matching string.
   */
  public int getMinLength();

  /**
   * Returns the maximum length for any matching string.
   */
  public int getMaxLength();

  /**
   * Returns the length bounds for any matching string.
   */
  default Bounds getLength()
    {
    return new Bounds( getMinLength(), getMaxLength());
    }

  /**
   * Returns the {@link GenOptions options} for this generator.
   *
   * @deprecated Use {@link RegExpGenBuilder} to configure options.
   */
  @Deprecated
  public GenOptions getOptions();

  /**
   * Returns the regular expression string from which this generator was derived.
   */
  public String getSource();

  /**
   * Returns a random string within the given bounds that matches this regular expression.
   */
  public String generate( RandomGen random, Bounds length);

  /**
   * Returns a random string that matches this regular expression.
   */
  default String generate( RandomGen random)
    {
    return generate( random, new Bounds());
    }

  /**
   * Returns a random string within the given bounds that matches this regular expression.
   */
  default String generate( RandomGen random, Integer minLength, Integer maxLength)
    {
    return generate( random, new Bounds( minLength, maxLength));
    }

  /**
   * Returns false if no string matching this regular expression can satisfy the given bounds.
   */
  default boolean isFeasibleLength( Bounds bounds)
    {
    boolean feasible;
    try
      {
      effectiveLength( bounds);
      feasible = true;
      }
    catch( IllegalArgumentException e)
      {
      feasible = false;
      }

    return feasible;
    }

  /**
   * Returns the effective limits of the given length bounds for this regular expression.
   * Throws an exception if no string matching this regular expression can satisfy the given bounds.
   */
  default Bounds effectiveLength( Bounds bounds) throws IllegalArgumentException
    {
    return bounds.clippedTo( "Length", getMinLength(), getMaxLength());
    }

  /**
   * Compares {@link RegExpGen} instances in order of increasing range of {@link #getLength matching lengths}.
   */
  default int compareTo( RegExpGen other)
    {
    return getLength().compareTo( other.getLength());
    }
  }
