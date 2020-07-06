//////////////////////////////////////////////////////////////////////////////
// 
//                    Copyright 2020, Cornutum Project
//                             www.cornutum.org
//
//////////////////////////////////////////////////////////////////////////////

package org.cornutum.regexpgen.js;

import java.util.Random;

import org.cornutum.regexpgen.Bounds;

/**
 * Generates a sequence containing any <EM>except</EM> a given set of characters.
 */
public class NoneOfGen extends CharClassGen
  {
  /**
   * Creates a new NoneOfGen instance.
   */
  public NoneOfGen()
    {
    super();
    }
  
  /**
   * Creates a new NoneOfGen instance.
   */
  protected NoneOfGen( char c)
    {
    super( c);
    }
  
  /**
   * Creates a new NoneOfGen instance.
   */
  protected NoneOfGen( char first, char last)
    {
    super( first, last);
    }
  
  /**
   * Creates a new NoneOfGen instance.
   */
  public NoneOfGen( CharClassGen charClass)
    {
    super();
    addAll( charClass);
    }

  /**
   * Returns a random string within the given bounds that matches this regular expression.
   */
  public String generate( Random random, Bounds bounds)
    {
    return null;
    }

  /**
   * Returns an {@link AlternativeGen} builder.
   */
  public static Builder builder()
    {
    return new Builder();
    }

  /**
   * Builds a {@link NoneOfGen} instance.
   */
  public static class Builder extends CharClassGenBuilder<Builder>
    {
    /**
     * Returns the {@link CharClassGen} instance for this builder.
     */
    protected CharClassGen getCharClassGen()
      {
      return noneOf_;
      }

    public NoneOfGen build()
      {
      return noneOf_;
      }
      
    private NoneOfGen noneOf_ = new NoneOfGen();
    }
  }
