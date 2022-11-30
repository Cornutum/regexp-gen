//////////////////////////////////////////////////////////////////////////////
// 
//                    Copyright 2020, Cornutum Project
//                             www.cornutum.org
//
//////////////////////////////////////////////////////////////////////////////

package org.cornutum.regexpgen.js;

import org.cornutum.regexpgen.GenOptions;

import java.util.Set;

/**
 * Generates a sequence containing any <EM>except</EM> a given set of characters.
 */
public class NoneOfGen extends CharClassGen
  {
  /**
   * Creates a new NoneOfGen instance.
   */
  public NoneOfGen( GenOptions options)
    {
    super( options);
    }
  
  /**
   * Creates a new NoneOfGen instance.
   */
  protected NoneOfGen( GenOptions options, char c)
    {
    super( options, c);
    }
  
  /**
   * Creates a new NoneOfGen instance.
   */
  protected NoneOfGen( GenOptions options, char first, char last)
    {
    super( options, first, last);
    }
  
  /**
   * Creates a new NoneOfGen instance.
   */
  protected NoneOfGen( GenOptions options, Set<Character> chars)
    {
    super( options, chars);
    }
  
  /**
   * Creates a new NoneOfGen instance.
   */
  public NoneOfGen( CharClassGen charClass)
    {
    super( charClass.getOptions());
    addAll( charClass);
    }

  /**
   * Returns the characters in this class.
   */
  public Character[] getChars()
    {
    return makeChars();
    }

  /**
   * Creates an array containing the characters in this class
   */
  protected Character[] makeChars()
    {
    return
      getOptions().getAnyPrintableChars().stream()
      .filter( c -> !getCharSet().contains( c))
      .toArray( Character[]::new);
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
    NoneOfGen other =
      object != null && object.getClass().equals( getClass())
      ? (NoneOfGen) object
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
   * Returns an {@link NoneOfGen} builder.
   */
  public static Builder builder()
    {
    return new Builder();
    }

  /**
   * Returns an {@link NoneOfGen} builder.
   */
  public static Builder builder( GenOptions options)
    {
    return new Builder( options);
    }

  /**
   * Builds a {@link NoneOfGen} instance.
   */
  public static class Builder extends CharClassGenBuilder<Builder>
    {
    public Builder()
      {
      this( BUILDER_OPTIONS);
      }

    public Builder( GenOptions options)
      {
      noneOf_ = new NoneOfGen( options);
      }
    
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
      
    private NoneOfGen noneOf_ ;
    }
  }
