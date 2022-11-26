//////////////////////////////////////////////////////////////////////////////
// 
//                    Copyright 2020, Cornutum Project
//                             www.cornutum.org
//
//////////////////////////////////////////////////////////////////////////////

package org.cornutum.regexpgen;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.IntStream;

import org.cornutum.regexpgen.util.ToString;

import static java.util.stream.Collectors.toSet;

/**
 * Defines options for generating regular expression matches.
 */
public class GenOptions
  {
  /**
   * Creates a new GenOptions instance for generating matches for the given
   * regular expression.
   */
  public GenOptions()
    {
    setAnyPrintableChars( ANY_LATIN_1);
    }

  /**
   * Changes the set of characters used to generate matches for the "." expression.
   */
  public void setAnyPrintableChars( Set<Character> chars)
    {
    Set<Character> anyPrintable = Optional.ofNullable( chars).orElse( ANY_LATIN_1);
    if( anyPrintable.isEmpty())
      {
      throw new IllegalArgumentException( "Printable character set is empty");
      }
    anyPrintable.stream()
      .filter( GenOptions::isLineTerminator)
      .findFirst()
      .ifPresent( lt -> {
        throw
          new IllegalArgumentException(
            String.format(
              "Printable character set cannot include line terminator=\\u%s",
              Integer.toHexString( lt.charValue())));
        });
    
    anyPrintable_ = anyPrintable;
    }

  /**
   * Changes the set of characters used to generate matches for the "." expression.
   */
  public void setAnyPrintableChars( String chars)
    {
    setAnyPrintableChars(
      IntStream.range( 0, chars.length())
      .mapToObj( i -> chars.charAt( i))
      .collect( toSet()));
    }

  /**
   * Returns the set of characters used to generate matches for the "." expression.
   */
  public Set<Character> getAnyPrintableChars()
    {
    return anyPrintable_;
    }

  public String toString()
    {
    return
      ToString.getBuilder( this)
      .toString();
    }

  private Set<Character> anyPrintable_;

  /**
   * Return true if the character with the given code point is printable.
   */
  private static boolean isPrintable( int codePoint)
    {
    return
      Character.toChars( codePoint)[0] == ' '
      || !(Character.isSpaceChar( codePoint) || notVisible_.contains( Character.getType( codePoint))) ;
    }

  /**
   * Return true if the character is a line terminator
   */
  private static boolean isLineTerminator( Character character)
    {
    char c = character.charValue();
    return c == '\n' || c == '\r' || c == '\u0085' || c == '\u2028' || c == '\u2029';
    }

  private static final List<Integer> notVisible_ =
    Arrays.asList(
      (int) Character.CONTROL,
      (int) Character.SURROGATE,
      (int) Character.UNASSIGNED);

  private static Set<Character> printableChars( int startPoint, int endPoint)
    {
    return
      IntStream.range( startPoint, endPoint)
      .filter( GenOptions::isPrintable)
      .mapToObj( i -> Character.valueOf( (char) i))
      .collect( toSet());
    }

  /**
   * All printable characters in the basic and supplemental Latin-1 code blocks
   */
  public static final Set<Character> ANY_LATIN_1 = Collections.unmodifiableSet( printableChars( 0, 256));

  /**
   * All printable characters in the ASCII code block
   */
  public static final Set<Character> ANY_ASCII = Collections.unmodifiableSet( printableChars( 0, 128));
  }
