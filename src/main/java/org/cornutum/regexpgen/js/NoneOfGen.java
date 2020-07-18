//////////////////////////////////////////////////////////////////////////////
// 
//                    Copyright 2020, Cornutum Project
//                             www.cornutum.org
//
//////////////////////////////////////////////////////////////////////////////

package org.cornutum.regexpgen.js;

import java.util.stream.IntStream;

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
   * Creates an array containing the characters in this class
   */
  protected Character[] makeChars()
    {
    String anyPrintable = anyPrintable();
    return
      IntStream.range( 0, anyPrintable.length())
      .mapToObj( i -> anyPrintable.charAt(i))
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
