//////////////////////////////////////////////////////////////////////////////
// 
//                    Copyright 2022, Cornutum Project
//                             www.cornutum.org
//
//////////////////////////////////////////////////////////////////////////////

package org.cornutum.regexpgen.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import static java.util.stream.Collectors.toSet;

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
    return lineTerminators().contains( character);
    }

  /**
   * Return the set of line terminator characters.
   */
  public static Set<Character> lineTerminators()
    {
    return lineTerminators_;
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

  /**
   * Returns a literal string for the given character.
   */
  public static String literal( Character c)
    {
    return
      c == '\\'? "\\\\" :
      c == '\''? "\\'" :
      c == '\f'? "\\f" :
      c == '\n'? "\\n" :
      c == '\r'? "\\r" :
      c == '\t'? "\\t" :
      c == '\13'? "\\v" :
      c == '\0'? "\\x00" :
      c >= 0x2000? String.format( "\\u%s", Integer.toHexString( c)) :
      String.valueOf( c);
    }

  /**
   * Returns a literal string for the given character in a JavaScript string.
   */
  public static String stringLiteral( Character c)
    {
    return
      c == '\''? "\\'" :
      c == '"'? "\\\"" :
      literal( c);
    }

  /**
   * Returns a literal string for the given character in a character class expression.
   */
  public static String charClassLiteral( Character c)
    {
    return
      c == '-'? "\\-" :
      c == ']'? "\\]" :
      literal( c);
    }

  private static final List<Integer> notVisible_ =
    Arrays.asList(
      (int) Character.CONTROL,
      (int) Character.SURROGATE,
      (int) Character.UNASSIGNED);

  private static Set<Character> lineTerminators_ =
    Collections.unmodifiableSet(
      Arrays.asList(
        Character.valueOf( '\n'),
        Character.valueOf( '\r'),
        Character.valueOf( '\u0085'),
        Character.valueOf( '\u2028'),
        Character.valueOf( '\u2029'))
      .stream()
      .collect( toSet()));
  }
