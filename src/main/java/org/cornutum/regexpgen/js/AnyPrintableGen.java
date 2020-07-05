//////////////////////////////////////////////////////////////////////////////
// 
//                    Copyright 2020, Cornutum Project
//                             www.cornutum.org
//
//////////////////////////////////////////////////////////////////////////////

package org.cornutum.regexpgen.js;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

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
    addAllPrintable();
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
   * Adds all printable Latin-1 characters to this class.
   */
  private void addAllPrintable()
    {
    StringBuilder allChars = new StringBuilder();
    IntStream.range( 0, 256).filter( this::isPrintable).forEach( codePoint -> allChars.appendCodePoint( codePoint));
    addAll( allChars.toString());
    }

  /**
   * Return true if the character with the given code point is printable.
   */
  private boolean isPrintable( int codePoint)
    {
    return
      Character.toChars( codePoint)[0] == ' '
      || !(Character.isSpaceChar( codePoint) || notVisible_.contains( Character.getType( codePoint))) ;
    }

  private static final List<Integer> notVisible_ =
    Arrays.asList(
      (int) Character.CONTROL,
      (int) Character.SURROGATE,
      (int) Character.UNASSIGNED);
  }
