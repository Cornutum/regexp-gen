//////////////////////////////////////////////////////////////////////////////
// 
//                    Copyright 2020, Cornutum Project
//                             www.cornutum.org
//
//////////////////////////////////////////////////////////////////////////////

package org.cornutum.regexpgen.js;

import org.cornutum.regexpgen.GenOptions;

/**
 * Generates a sequence containing any of a set of characters.
 */
public class AnyOfGen extends CharClassGen
  {
  /**
   * Creates a new AnyOfGen instance.
   */
  public AnyOfGen( GenOptions options)
    {
    super( options);
    }
  
  /**
   * Creates a new AnyOfGen instance.
   */
  protected AnyOfGen( GenOptions options, char c)
    {
    super( options, c);
    }
  
  /**
   * Creates a new AnyOfGen instance.
   */
  protected AnyOfGen( GenOptions options, char first, char last)
    {
    super( options, first, last);
    }

  /**
   * Implements the Visitor pattern for {@link org.cornutum.regexpgen.RegExpGen} implementations.
   */
  public void accept( RegExpGenVisitor visitor)
    {
    visitor.visit( this);
    }

  public boolean equals( Object object)
    {
    AnyOfGen other =
      object != null && object.getClass().equals( getClass())
      ? (AnyOfGen) object
      : null;

    return
      other != null
      && super.equals( other);
    }

  public int hashCode()
    {
    return super.hashCode();
    }

  /**
   * Returns an {@link AnyOfGen} builder.
   */
  public static Builder builder()
    {
    return new Builder();
    }

  /**
   * Returns an {@link AnyOfGen} builder.
   */
  public static Builder builder( GenOptions options)
    {
    return new Builder( options);
    }

  /**
   * Builds an {@link AnyOfGen} instance.
   */
  public static class Builder extends CharClassGenBuilder<Builder>
    {
    public Builder()
      {
      this( BUILDER_OPTIONS);
      }

    public Builder( GenOptions options)
      {
      anyOf_ = new AnyOfGen( options);
      }
    
    /**
     * Returns the {@link CharClassGen} instance for this builder.
     */
    protected CharClassGen getCharClassGen()
      {
      return anyOf_;
      }

    public Builder anyPrintable()
      {
      anyOf_ = new AnyPrintableGen( anyOf_.getOptions());
      return this;
      }

    public AnyOfGen build()
      {
      return anyOf_;
      }
      
    private AnyOfGen anyOf_;
    }
  }
