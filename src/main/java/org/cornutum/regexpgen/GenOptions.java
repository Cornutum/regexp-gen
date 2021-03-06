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
  public GenOptions( String regexp)
    {
    regexp_ = regexp;
    setAnyPrintableChars( ANY_LATIN_1);
    }

  /**
   * Returns the regular expression for which matches are generated.
   */
  public String getRegExp()
    {
    return regexp_;
    }

  /**
   * Changes the set of characters used to generate matches for the "." expression.
   */
  public void setAnyPrintableChars( Set<Character> chars)
    {
    anyPrintable_ = chars;
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

  private final String regexp_;
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
