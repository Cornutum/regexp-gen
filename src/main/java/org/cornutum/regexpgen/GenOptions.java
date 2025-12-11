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
import java.util.HashSet;
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
    setSpaceChars( ECMA_SPACE);
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

  /**
   * Changes the set of characters used to generate matches for the "\s" expression.
   */
  public void setSpaceChars( Set<Character> chars)
    {
    Set<Character> spaceChars = Optional.ofNullable( chars).orElse( ECMA_SPACE);
    if( spaceChars.isEmpty())
      {
      throw new IllegalArgumentException( "Space character set is empty");
      }
    spaceChars_ = spaceChars;
    }

  /**
   * Changes the set of characters used to generate matches for the "\s" expression.
   */
  public void setSpaceChars( String chars)
    {
    setSpaceChars(
      IntStream.range( 0, chars.length())
      .mapToObj( i -> chars.charAt( i))
      .collect( toSet()));
    }

  /**
   * Returns the set of characters used to generate matches for the "\s" expression.
   */
  public Set<Character> getSpaceChars()
    {
    return spaceChars_;
    }

  public String toString()
    {
    return
      ToString.getBuilder( this)
      .toString();
    }

  private Set<Character> anyPrintable_;
  private Set<Character> spaceChars_;

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

  /**
   * Standard ECMA-262 whitespace characters for the "\s" character class
   */
  public static final Set<Character> ECMA_SPACE;
  static
    {
    Set<Character> ecmaSpace = new HashSet<>();
    ecmaSpace.add( ' ');
    ecmaSpace.add( '\f');
    ecmaSpace.add( '\n');
    ecmaSpace.add( '\r');
    ecmaSpace.add( '\t');
    ecmaSpace.add( (char) 0x000b);  // Vertical tab
    ecmaSpace.add( (char) 0x00a0);  // Non-breaking space
    ecmaSpace.add( (char) 0x1680);  // Ogham space mark
    ecmaSpace.add( (char) 0x2028);  // Line separator
    ecmaSpace.add( (char) 0x2029);  // Paragraph separator
    ecmaSpace.add( (char) 0x202f);  // Narrow no-break space
    ecmaSpace.add( (char) 0x205f);  // Medium mathematical space
    ecmaSpace.add( (char) 0x3000);  // Ideographic space
    ecmaSpace.add( (char) 0xfeff);  // Byte order mark
    // En quad through hair space
    for( char c = (char) 0x2000; c <= (char) 0x200a; c++)
      {
      ecmaSpace.add( c);
      }
    ECMA_SPACE = Collections.unmodifiableSet( ecmaSpace);
    }
  }
