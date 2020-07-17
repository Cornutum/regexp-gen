//////////////////////////////////////////////////////////////////////////////
// 
//                    Copyright 2020, Cornutum Project
//                             www.cornutum.org
//
//////////////////////////////////////////////////////////////////////////////

package org.cornutum.regexpgen.random;

import java.util.Random;

/**
 * Generates random numbers following a Poisson distribution
 */
public class Poisson
  {
  /**
   * Creates a new Poisson distribution with the given lambda parameter.
   */
  public Poisson( Random random, int lambda)
    {
    if( lambda <= 0)
      {
      throw new IllegalArgumentException( "Lambda must be > 0");
      }

    L = Math.exp( -lambda);
    this.random = random;
    }

  /**
   * Returns the next random number from this Poisson distribution.
   */
  public int next()
    {
    int k = 0;
    for( double p = 1.0; (p *= random.nextDouble()) > L; k++);
    return k;      
    }

  private final Random random;
  private final double L;
  }
