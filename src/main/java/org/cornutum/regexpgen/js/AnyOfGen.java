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
 * Generates a sequence containing any of a set of characters.
 */
public class AnyOfGen extends CharClassGen
  {
  /**
   * Creates a new AnyOfGen instance.
   */
  public AnyOfGen()
    {
    super();
    }
  
  /**
   * Creates a new AnyOfGen instance.
   */
  protected AnyOfGen( char c)
    {
    super( c);
    }
  
  /**
   * Creates a new AnyOfGen instance.
   */
  protected AnyOfGen( char first, char last)
    {
    super( first, last);
    }

  /**
   * Returns a random string within the given bounds that matches this regular expression.
   */
  public String generate( Random random, Bounds bounds)
    {
    return null;
    }

  /**
   * Returns an {@link AnyOfGen} builder.
   */
  public static Builder<AnyOfGen> builder()
    {
    return new Builder<AnyOfGen>( new AnyOfGen());
    }

  /**
   * Builds an {@link AnyOfGen} instance.
   */
  public static class Builder<T extends AnyOfGen>  extends CharClassGenBuilder<Builder<T>>
    {
    public Builder( T anyOf)
      {
      anyOf_ = anyOf;
      }
    
    /**
     * Returns the {@link CharClassGen} instance for this builder.
     */
    protected CharClassGen getCharClassGen()
      {
      return anyOf_;
      }

    public T build()
      {
      return anyOf_;
      }
      
    private T anyOf_;
    }
  }
