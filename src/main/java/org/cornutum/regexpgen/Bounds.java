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
   * Returns a new {@link Bounds} instance for the intersection of this bounds with the given range.
   * Throws an exception if this bounds lies outside the range.
   */
  public Bounds clippedTo( String rangeName, int rangeMin, int rangeMax) throws IllegalArgumentException
    {
    if( getMinValue() > rangeMax)
      {
      throw new IllegalArgumentException( String.format( "%s cannot be greater than %s", rangeName, rangeMax));
      }

    if( getMaxValue() < rangeMin)
      {
      throw new IllegalArgumentException( String.format( "%s cannot be less than %s", rangeName, rangeMin));
      }

    return new Bounds( Math.max( getMinValue(), rangeMin), Math.min( getMaxValue(), rangeMax));
    }

  /**
   * Returns true if this bounds intersects with the given range.
   */
  public boolean intersects( int rangeMin, int rangeMax)
    {
    boolean intersects;
    try
      {
      intersects = clippedTo( "", rangeMin, rangeMax) != null;
      }
    catch( IllegalArgumentException e)
      {
      intersects = false;
      }
    return intersects;
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
    catch( ArithmeticException e)
      {
      return Integer.MAX_VALUE;
      }
    }

  /**
   * Returns the product the given values, avoiding overflow.
   */
  public static int productOf( int a, int b)
    {
    try
      {
      return Math.multiplyExact( a, b);
      }
    catch( ArithmeticException e)
      {
      return Integer.MAX_VALUE;
      }
    }

  public String toString()
    {
    return
      new StringBuilder()
      .append( '[')
      .append( getMinValue())
      .append( ',')
      .append( Optional.of( getMaxValue()).filter( max -> max < Integer.MAX_VALUE).orElse( null))
      .append( ']')
      .toString();
    }

  public boolean equals( Object object)
    {
    Bounds other =
      object != null && object.getClass().equals( getClass())
      ? (Bounds) object
      : null;

    return
      other != null
      && other.getMinValue() == getMinValue()
      && other.getMaxValue() == getMaxValue();
    }

  public int hashCode()
    {
    return
      getClass().hashCode()
      ^ getMinValue()
      ^ getMaxValue();
    }
  
  private final int minValue_;
  private final int maxValue_;
  }
