//////////////////////////////////////////////////////////////////////////////
// 
//                    Copyright 2020, Cornutum Project
//                             www.cornutum.org
//
//////////////////////////////////////////////////////////////////////////////

package org.cornutum.regexpgen;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Generates random integer values.
 */
public interface RandomGen
  {
  /**
   * Returns a random integer between <CODE>min</CODE> (inclusive) and <CODE>max</CODE> (exclusive).
   */
  public int within( int min, int max);
  
  /**
   * Returns a random integer between <CODE>0</CODE> (inclusive) and <CODE>max</CODE> (exclusive).
   */
  default int below( int max)
    {
    return within( 0, max);
    }
  
  /**
   * Returns a random integer within the given bounds.
   */
  default int within( Bounds bounds)
    {
    return within( bounds.getMinValue(), Bounds.sumOf( bounds.getMaxValue(), 1));
    }

  /**
   * Returns the given elements shuffled into a random sequence.
   */
  default <T> List<T> shuffle( List<T> elements)
    {
    List<T> shuffled = new ArrayList<T>( elements);
    for( int to = elements.size(); to > 1; to--)
      {
      Collections.swap( shuffled, below( to), to-1);
      }
    
    return shuffled;
    }

  }
