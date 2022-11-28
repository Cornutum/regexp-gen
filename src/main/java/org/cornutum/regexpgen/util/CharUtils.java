//////////////////////////////////////////////////////////////////////////////
// 
//                    Copyright 2022, Cornutum Project
//                             www.cornutum.org
//
//////////////////////////////////////////////////////////////////////////////

package org.cornutum.regexpgen.util;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Defines methods for processing characters.
 */
public final class CharUtils
  {
  /**
   * Creates a new CharUtils instance.
   */
  private CharUtils()
    {
    // Static methods only
    }
  /**
   * Return true if the character with the given code point is printable.
   */
  public static boolean isPrintable( int codePoint)
    {
    return
      Character.toChars( codePoint)[0] == ' '
      || !(Character.isSpaceChar( codePoint) || notVisible_.contains( Character.getType( codePoint))) ;
    }

  /**
   * Return true if the character is a line terminator
   */
  public static boolean isLineTerminator( Character character)
    {
    char c = character.charValue();
    return c == '\n' || c == '\r' || c == '\u0085' || c == '\u2028' || c == '\u2029';
    }

  /**
   * Returns printable characters from the given code point range.
   */
  public static Stream<Character> printableChars( int startPoint, int endPoint)
    {
    return
      IntStream.range( startPoint, endPoint)
      .filter( CharUtils::isPrintable)
      .mapToObj( i -> Character.valueOf( (char) i));
    }

  /**
   * Returns all printable ASCII characters.
   */
  public static Stream<Character> printableAscii()
    {
    return printableChars( 0, 128);
    }

  /**
   * Returns all printable Latin-1 characters.
   */
  public static Stream<Character> printableLatin1()
    {
    return printableChars( 0, 256);
    }

  /**
   * Returns all printable Unicode characters.
   */
  public static Stream<Character> printableChars()
    {
    return printableChars( 0, 0xFFFF);
    }

  public static Character REPLACEMENT = Character.valueOf( (char) 0xFFFD);
  
  private static final List<Integer> notVisible_ =
    Arrays.asList(
      (int) Character.CONTROL,
      (int) Character.SURROGATE,
      (int) Character.UNASSIGNED);
  }
