//////////////////////////////////////////////////////////////////////////////
// 
//                    Copyright 2020, Cornutum Project
//                             www.cornutum.org
//
//////////////////////////////////////////////////////////////////////////////

package org.cornutum.regexpgen.js;

import org.cornutum.regexpgen.util.ToString;

/**
 * Generates a sequence of any printable characters.
 */
public class AnyPrintableGen extends AnyOfGen
  {
  /**
   * Creates a new AnyGen instance.
   */
  public AnyPrintableGen()
    {
    super();
    addAll( CharClassGen.anyPrintable());
    }
  
  /**
   * Creates a new AnyGen instance.
   */
  public AnyPrintableGen( int length)
    {
    this();
    setOccurrences( length, length);
    }
  
  /**
   * Creates a new AnyGen instance.
   */
  public AnyPrintableGen( Integer minOccur, Integer maxOccur)
    {
    this();
    setOccurrences( minOccur, maxOccur);
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
