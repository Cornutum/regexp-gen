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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

/**
 * Generates strings matching a sequence of regular expressions.
 */
public class SeqGen extends AbstractRegExpGen
  {
  /**
   * Creates a new SeqGen instance.
   */
  public SeqGen()
    {
    super();
    }
  
  /**
   * Creates a new SeqGen instance.
   */
  public SeqGen( RegExpGen... members)
    {
    for( RegExpGen member : members)
      {
      add( member);
      }
    }
  
  /**
   * Creates a new SeqGen instance.
   */
  public SeqGen( Iterable<RegExpGen> members)
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
    if( member != null)
      {
      members_.add( member);
      }
    }

  /**
   * Returns the minimum length for any matching string.
   */
  public int getMinLength()
    {
    return
      IntStream.range( 0, members_.size())
      .map( i -> members_.get(i).getMinLength())
      .reduce( Bounds::sumOf)
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
      .reduce( Bounds::sumOf)
      .orElse( 0);
    }

  /**
   * Returns a random string within the given bounds that matches this regular expression.
   */
  public String generate( Random random, Bounds bounds)
    {
    return null;
    }

  public String toString()
    {
    return
      ToString.getBuilder( this)
      .append( "members", members_)
      .appendSuper( super.toString())
      .toString();
    }

  private List<RegExpGen> members_ = new ArrayList<RegExpGen>();
  }
