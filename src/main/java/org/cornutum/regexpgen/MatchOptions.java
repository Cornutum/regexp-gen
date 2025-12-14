//////////////////////////////////////////////////////////////////////////////
// 
//                    Copyright 2025, Cornutum Project
//                             www.cornutum.org
//
//////////////////////////////////////////////////////////////////////////////

package org.cornutum.regexpgen;

import org.cornutum.regexpgen.util.CharUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import static java.util.stream.Collectors.toSet;

/**
 * Defines options for generating regular expression matches.
 */
public interface MatchOptions
  {
  /**
   * Returns the set of characters used to generate matches for the "." expression.
   */
  public Set<Character> getAnyPrintableChars();

  /**
   * Returns the set of characters used to generate matches for the "\s" expression.
   */
  public Set<Character> getSpaceChars();

  /**
   * Returns if generating strings containing only matching characters.
   */
  public boolean isExactMatch();

  /**
   * @deprecated Provides {@link GenOptions} for backward-compatibility only
   */
  public GenOptions getGenOptions();

  /**
   * All printable characters in the basic and supplemental Latin-1 code blocks.
   */
  public static final Set<Character> ANY_LATIN_1 =
    Collections.unmodifiableSet(
      CharUtils.printableLatin1()
      .collect( toSet()));

  /**
   * All printable characters in the ASCII code block.
   */
  public static final Set<Character> ANY_ASCII = 
    Collections.unmodifiableSet(
      CharUtils.printableAscii()
      .collect( toSet()));

  /**
   * Standard ECMA-262 whitespace characters for the "\s" character class.
   */
  public static final Set<Character> ECMA_SPACE =
    Arrays.stream(
      new Character[]{
        ' ',
        '\f',
        '\n',
        '\r',
        '\t',
        (char) 0x000b,  // Vertical tab
        (char) 0x00a0,  // Non-breaking space
        (char) 0x1680,  // Ogham space mark
        (char) 0x2028,  // Line separator
        (char) 0x2029,  // Paragraph separator
        (char) 0x202f,  // Narrow no-break space
        (char) 0x205f,  // Medium mathematical space
        (char) 0x3000,  // Ideographic space
        (char) 0xfeff,  // Byte order mark
        (char) 0x2000,  // En quad through...
        (char) 0x2001,
        (char) 0x2002,
        (char) 0x2003,
        (char) 0x2004,
        (char) 0x2005,
        (char) 0x2006,
        (char) 0x2007,
        (char) 0x2008,
        (char) 0x2009,
        (char) 0x200a   // ... hair space
      })
    .collect( toSet());

  /**
   * Standard ASCII whitespace characters for the "\s" character class. This is the same definition used
   * for regular expressions matched by the Java <CODE>Pattern</CODE> class.
   */
  public static final Set<Character> ASCII_SPACE =
    Arrays.stream(
      new Character[]{
        ' ',
        '\f',
        '\n',
        '\r',
        '\t',
        (char) 0x000b,  // Vertical tab
      })
    .collect( toSet());  
  }
