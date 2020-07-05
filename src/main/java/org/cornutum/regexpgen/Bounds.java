//////////////////////////////////////////////////////////////////////////////
// 
//                    Copyright 2020, Cornutum Project
//                             www.cornutum.org
//
//////////////////////////////////////////////////////////////////////////////

package org.cornutum.regexpgen;

import java.util.Optional;

/**
 * Defines length constraints for strings matching a regular expression.
 */
public class Bounds
  {
  /**
   * Creates a new Bounds instance.
   */
  public Bounds()
    {
    this( 0, Integer.MAX_VALUE);
    }

  /**
   * Creates a new Bounds instance.
   */
  public Bounds( int maxValue)
    {
    this( 0, maxValue);
    }

  /**
   * Creates a new Bounds instance.
   */
  public Bounds( Integer minValue, Integer maxValue)
    {
    int min = Optional.ofNullable( minValue).orElse( 0);
    int max = Optional.ofNullable( maxValue).orElse( Integer.MAX_VALUE);
    
    if( min < 0)
      {
      throw new IllegalArgumentException( "Minimum value must be non-negative");
      }
    if( min > max)
      {
      throw new IllegalArgumentException( String.format( "Minimum value=%s is greater than maximum value=%s", min, max));
      }
    
    minValue_ = min;
    maxValue_ = max;
    }

  /**
   * Returns the minimum value for any matching string.
   */
  public int getMinValue()
    {
    return minValue_;
    }

  /**
   * Returns the maximum value for any matching string.
   */
  public int getMaxValue()
    {
    return maxValue_;
    }

  /**
   * Returns the sum the given values, avoiding overflow.
   */
  public static int sumOf( int a, int b)
    {
    try
      {
      return Math.addExact( a, b);
      }
    catch( Exception e)
      {
      return Integer.MAX_VALUE;
      }
    }

  private final int minValue_;
  private final int maxValue_;
  }
