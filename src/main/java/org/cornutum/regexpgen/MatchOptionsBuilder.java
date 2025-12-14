//////////////////////////////////////////////////////////////////////////////
// 
//                    Copyright 2025, Cornutum Project
//                             www.cornutum.org
//
//////////////////////////////////////////////////////////////////////////////

package org.cornutum.regexpgen;

import org.cornutum.regexpgen.util.CharUtils;
import org.cornutum.regexpgen.util.ToString;

import java.util.Optional;
import java.util.Set;
import java.util.stream.IntStream;
import static java.util.stream.Collectors.toSet;

/**
 * Builds a {@link MatchOptions} instance.
 */
public class MatchOptionsBuilder
  {
  /**
   * Returns a new {@link MatchOptionsBuilder}.
   */
  public static MatchOptionsBuilder options()
    {
    return options( null);
    }

  /**
   * Returns a new {@link MatchOptionsBuilder}.
   */
  public static MatchOptionsBuilder options( MatchOptions other)
    {
    return new MatchOptionsBuilder( other);
    }

  /**
   * Creates a new {@link MatchOptionsBuilder}.
   */
  private MatchOptionsBuilder()
    {
    this( null);
    }
  
  /**
   * Creates a new {@link MatchOptionsBuilder}.
   */
  private MatchOptionsBuilder( MatchOptions other)
    {
    options_ = new Options();
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
  public MatchOptionsBuilder withAny( String chars)
    {
    options_.setAnyPrintableChars( chars);
    return this;
    }

  /**
   * Changes the set of characters used to generate matches for the "." expression.
   */
  public MatchOptionsBuilder withAny( Set<Character> chars)
    {
    options_.setAnyPrintableChars( chars);
    return this;
    }

  /**
   * Changes the set of characters used to generate matches for the "\s" expression.
   */
  public MatchOptionsBuilder withSpace( String chars)
    {
    options_.setSpaceChars( chars);
    return this;
    }

  /**
   * Changes the set of characters used to generate matches for the "\s" expression.
   */
  public MatchOptionsBuilder withSpace( Set<Character> chars)
    {
    options_.setSpaceChars( chars);
    return this;
    }

  /**
   * Changes if generating strings containing only matching characters.
   */
  public MatchOptionsBuilder exactly( boolean exactMatch)
    {
    options_.setExactMatch( exactMatch);
    return this;
    }

  /**
   * Generate strings containing only matching characters.
   */
  public MatchOptionsBuilder exactly()
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

  public String toString()
    {
    return
      ToString.getBuilder( this)
      .toString();
    }

  private Options options_;
  
  /**
   * The standard {@link MatchOptions} implementation.
   */
  static class Options implements MatchOptions
    {
    /**
     * Creates a new Options instance.
     */
    public Options()
      {
      setAnyPrintableChars( ANY_LATIN_1);
      setSpaceChars( ECMA_SPACE);
      setExactMatch( false);
      }

    /**
     * Changes the set of characters used to generate matches for the "." expression.
     */
    void setAnyPrintableChars( Set<Character> chars)
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
    void setAnyPrintableChars( String chars)
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
    void setSpaceChars( Set<Character> chars)
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
    void setSpaceChars( String chars)
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
    void setExactMatch( boolean exactMatch)
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
      return genOptions_;
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
    private GenOptions genOptions_ = new GenOptions( this);
    }
  }
