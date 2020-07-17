//////////////////////////////////////////////////////////////////////////////
// 
//                    Copyright 2020, Cornutum Project
//                             www.cornutum.org
//
//////////////////////////////////////////////////////////////////////////////

package org.cornutum.regexpgen.js;

import org.cornutum.regexpgen.Bounds;
import org.cornutum.regexpgen.RandomGen;

import org.apache.commons.lang3.ArrayUtils;

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
   * Returns a random string within the given bounds that matches this regular expression.
   */
  protected String generateLength( RandomGen random, Bounds length)
    {
    StringBuilder matching = new StringBuilder();

    IntStream.range( 0, random.within( length))
        .forEach( i -> matching.append( notExcluded( random)));
    
    return matching.toString();
    }

  /**
   * Returns a random character not excluded by this regular expression. 
   */
  private Character notExcluded( RandomGen random)
    {
    Character[] excluded = getChars();
    String anyPrintable = anyPrintable();

    Character c;
    while( ArrayUtils.indexOf( excluded, (c = anyPrintable.charAt( random.below( anyPrintable.length())))) >= 0);

    return c;
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
