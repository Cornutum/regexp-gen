//////////////////////////////////////////////////////////////////////////////
// 
//                    Copyright 2025, Cornutum Project
//                             www.cornutum.org
//
//////////////////////////////////////////////////////////////////////////////

package org.cornutum.regexpgen;

import org.cornutum.regexpgen.util.CharUtils;
import org.cornutum.regexpgen.util.ToString;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.IntStream;
import static java.util.stream.Collectors.toSet;

/**
 * Defines options for generating regular expression matches.
 */
public class MatchOptions
  {
  /**
   * Creates a new MatchOptions instance for generating matches for the given
   * regular expression.
   */
  public MatchOptions()
    {
    setAnyPrintableChars( ANY_LATIN_1);
    setSpaceChars( ECMA_SPACE);
    setExactMatch( false);
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

  /**
   * Changes if generating strings containing only matching characters.
   */
  public void setExactMatch( boolean exactMatch)
    {
    exactMatch_ = exactMatch;
    }

  /**
   * Returns if generating strings containing only matching characters.
   */
  public boolean isExactMatch()
    {
    return exactMatch_;
    }

  /**
   * @deprecated Provides {@link GenOptions} for backward-compatibility only
   */
  public GenOptions getGenOptions()
    {
    return new GenOptions( this);
    }

  /**
   * Returns a new {@link Builder}.
   */
  public static Builder builder()
    {
    return builder( null);
    }

  /**
   * Returns a new {@link Builder}.
   */
  public static Builder builder( MatchOptions other)
    {
    return new Builder( other);
    }

  public String toString()
    {
    return
      ToString.getBuilder( this)
      .toString();
    }

  private Set<Character> anyPrintable_;
  private Set<Character> spaceChars_;
  private boolean exactMatch_;

  /**
   * Builds a {@link MatchOptions} instance.
   */
  public static class Builder
    {
    /**
     * Creates a new Builder instance.
     */
    public Builder()
      {
      this( null);
      }
    
    public Builder( MatchOptions other)
      {
      options_ = new MatchOptions();
      if( other != null)
        {
        options_.setAnyPrintableChars( other.getAnyPrintableChars());
        options_.setSpaceChars( other.getSpaceChars());
        options_.setExactMatch( other.isExactMatch());
        }
      }

    /**
     * Changes the set of characters used to generate matches for the "." expression.
     */
    public Builder withAny( String chars)
      {
      options_.setAnyPrintableChars( chars);
      return this;
      }

    /**
     * Changes the set of characters used to generate matches for the "." expression.
     */
    public Builder withAny( Set<Character> chars)
      {
      options_.setAnyPrintableChars( chars);
      return this;
      }

    /**
     * Changes the set of characters used to generate matches for the "\s" expression.
     */
    public Builder withSpace( String chars)
      {
      options_.setSpaceChars( chars);
      return this;
      }

    /**
     * Changes the set of characters used to generate matches for the "\s" expression.
     */
    public Builder withSpace( Set<Character> chars)
      {
      options_.setSpaceChars( chars);
      return this;
      }

    /**
     * Changes if generating strings containing only matching characters.
     */
    public Builder exactly( boolean exactMatch)
      {
      options_.setExactMatch( exactMatch);
      return this;
      }

    /**
     * Generate strings containing only matching characters.
     */
    public Builder exactly()
      {
      return exactly( true);
      }

    /**
     * Returns the {@link MatchOptions} for this builder.
     */
    public MatchOptions build()
      {
      return options_;
      }

    private MatchOptions options_;
    }

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
