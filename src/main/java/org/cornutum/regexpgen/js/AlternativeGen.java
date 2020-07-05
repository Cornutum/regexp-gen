//////////////////////////////////////////////////////////////////////////////
// 
//                    Copyright 2020, Cornutum Project
//                             www.cornutum.org
//
//////////////////////////////////////////////////////////////////////////////

package org.cornutum.regexpgen.js;

import org.cornutum.regexpgen.Bounds;
import org.cornutum.regexpgen.RegExpGen;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

/**
 * Generates strings matching one of a set of alternative regular expressions.
 */
public class AlternativeGen extends AbstractRegExpGen
  {
  /**
   * Creates a new AlternativeGen instance.
   */
  public AlternativeGen()
    {
    super();
    }
  
  /**
   * Creates a new AlternativeGen instance.
   */
  public AlternativeGen( RegExpGen... members)
    {
    for( RegExpGen member : members)
      {
      add( member);
      }
    }
  
  /**
   * Creates a new AlternativeGen instance.
   */
  public <T extends RegExpGen> AlternativeGen( Iterable<T> members)
    {
    for( RegExpGen member : members)
      {
      add( member);
      }
    }

  /**
   * Adds a regular expression to this sequence.
   */
  public void add( RegExpGen member)
    {
    members_.add( member);
    }

  /**
   * Returns the minimum length for any matching string.
   */
  public int getMinLength()
    {
    return
      IntStream.range( 0, members_.size())
      .map( i -> members_.get(i).getMinLength())
      .min()
      .orElse( 0);
    }

  /**
   * Returns the maximum length for any matching string.
   */
  public int getMaxLength()
    {
    return
      IntStream.range( 0, members_.size())
      .map( i -> members_.get(i).getMaxLength())
      .max()
      .orElse( 0);
    }

  /**
   * Returns a random string within the given bounds that matches this regular expression.
   */
  public String generate( Random random, Bounds bounds)
    {
    return null;
    }

  private List<RegExpGen> members_ = new ArrayList<RegExpGen>();
  }
