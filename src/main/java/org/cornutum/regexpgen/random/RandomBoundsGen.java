//////////////////////////////////////////////////////////////////////////////
// 
//                    Copyright 2020, Cornutum Project
//                             www.cornutum.org
//
//////////////////////////////////////////////////////////////////////////////

package org.cornutum.regexpgen.random;

import org.cornutum.regexpgen.Bounds;
import org.cornutum.regexpgen.RandomGen;

import java.util.Random;

/**
 * Generates random integer values within given {@link Bounds}.
 */
public class RandomBoundsGen implements RandomGen
  {
  /**
   * Creates a new RandomBoundsGen instance. 
   */
  public RandomBoundsGen()
    {
    this( new Random());
    }

  /**
   * Creates a new RandomBoundsGen instance. 
   */
  public RandomBoundsGen( Random random)
    {
    this( random, 16);
    }

  /**
   * Creates a new RandomBoundsGen instance. 
   */
  public RandomBoundsGen( int lambda)
    {
    this( new Random(), lambda);
    }
  
  /**
   * Creates a new RandomBoundsGen instance. When no upper bound is defined, uses a Poisson distribution with
   * the given lambda parameter.
   */
  public RandomBoundsGen( Random random, int lambda)
    {
    random_ = random;
    extra_ = new Poisson( random, lambda);
    }
  
  /**
   * Returns a random integer between <CODE>min</CODE> (inclusive) and <CODE>max</CODE> (exclusive).
   */
  public int within( int min, int max)
    {
    return
      max - min <= 0?
      min :

      max < Integer.MAX_VALUE?
      min + random_.nextInt( max - min) :

      min + extra_.next();
    }

  private final Random random_;
  private final Poisson extra_;
  }
