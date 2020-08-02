//////////////////////////////////////////////////////////////////////////////
// 
//                    Copyright 2020, Cornutum Project
//                             www.cornutum.org
//
//////////////////////////////////////////////////////////////////////////////

package org.cornutum.regexpgen.js;

import java.util.Set;

import org.cornutum.regexpgen.GenOptions;
import org.cornutum.regexpgen.util.ToString;

/**
 * Generates a sequence of any printable characters.
 */
public class AnyPrintableGen extends AnyOfGen
  {
  /**
   * Creates a new AnyPrintableGen instance.
   */
  public AnyPrintableGen( GenOptions options)
    {
    super( options);
    }
  
  /**
   * Creates a new AnyPrintableGen instance.
   */
  public AnyPrintableGen( GenOptions options, int length)
    {
    this( options);
    setOccurrences( length, length);
    }
  
  /**
   * Creates a new AnyPrintableGen instance.
   */
  public AnyPrintableGen( GenOptions options, Integer minOccur, Integer maxOccur)
    {
    this( options);
    setOccurrences( minOccur, maxOccur);
    }

  /**
   * Returns the set of characters that define this class.
   */
  protected Set<Character> getCharSet()
    {
    return getOptions().getAnyPrintableChars();
    }

  /**
   * Returns the characters in this class.
   */
  public Character[] getChars()
    {
    return makeChars();
    }

  /**
   * Implements the Visitor pattern for {@link org.cornutum.regexpgen.RegExpGen} implementations.
   */
  public void accept( RegExpGenVisitor visitor)
    {
    visitor.visit( this);
    }

  public String toString()
    {
    return
      ToString.getBuilder( this)
      .append( "occurs", getOccurrences())
      .toString();
    }
  }
