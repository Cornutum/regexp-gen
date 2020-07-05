//////////////////////////////////////////////////////////////////////////////
// 
//                    Copyright 2020, Cornutum Project
//                             www.cornutum.org
//
//////////////////////////////////////////////////////////////////////////////

package org.cornutum.regexpgen;

import java.util.Optional;
import java.util.Random;

/**
 * Generates strings that match a regular expression.
 */
public interface RegExpGen
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
   * Returns a random string within the given bounds that matches this regular expression.
   */
  public String generate( Random random, Bounds length);

  /**
   * Returns a random string that matches this regular expression.
   */
  default String generate( Random random)
    {
    return generate( random, new Bounds());
    }

  /**
   * Returns the effective limits of the given length bounds for this regular expression.
   * Throws an exception if no string matching this regular expression can satisfy the given bounds.
   */
  default Bounds effectiveLength( Bounds bounds) throws IllegalArgumentException
    {
    int minLength = Optional.ofNullable( bounds).map( Bounds::getMinValue).orElse( 0);
    if( minLength > getMaxLength())
      {
      throw new IllegalArgumentException( String.format( "No matching string can be longer than %s", getMaxLength()));
      }

    int maxLength = Optional.ofNullable( bounds).map( Bounds::getMaxValue).orElse( Integer.MAX_VALUE);
    if( maxLength < getMinLength())
      {
      throw new IllegalArgumentException( String.format( "No matching string can be shorter than %s", getMinLength()));
      }

    return new Bounds( Math.max( minLength, getMinLength()), Math.min( maxLength, getMaxLength()));
    }
  }
