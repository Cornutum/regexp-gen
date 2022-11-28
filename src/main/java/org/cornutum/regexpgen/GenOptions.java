//////////////////////////////////////////////////////////////////////////////
// 
//                    Copyright 2020, Cornutum Project
//                             www.cornutum.org
//
//////////////////////////////////////////////////////////////////////////////

package org.cornutum.regexpgen;

import org.cornutum.regexpgen.util.CharUtils;
import org.cornutum.regexpgen.util.ToString;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.IntStream;
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
      .filter( CharUtils::isLineTerminator)
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
   * All printable characters in the basic and supplemental Latin-1 code blocks
   */
  public static final Set<Character> ANY_LATIN_1 =
    Collections.unmodifiableSet(
      CharUtils.printableLatin1()
      .collect( toSet()));

  /**
   * All printable characters in the ASCII code block
   */
  public static final Set<Character> ANY_ASCII = 
    Collections.unmodifiableSet(
      CharUtils.printableAscii()
      .collect( toSet()));
  }
