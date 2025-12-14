//////////////////////////////////////////////////////////////////////////////
// 
//                    Copyright 2020, Cornutum Project
//                             www.cornutum.org
//
//////////////////////////////////////////////////////////////////////////////

package org.cornutum.regexpgen;

import org.cornutum.regexpgen.util.ToString;

import java.util.Set;

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
    this( new MatchOptions());
    }

  /**
   * Creates a new GenOptions instance for generating matches for the given
   * regular expression.
   */
  public GenOptions( MatchOptions matchOptions)
    {
    matchOptions_ = matchOptions;
    }

  /**
   * Changes the set of characters used to generate matches for the "." expression.
   *
   * @deprecated Use {@link RegExpGenBuilder} to configure options
   */
  @Deprecated
  public void setAnyPrintableChars( Set<Character> chars)
    {
    matchOptions_.setAnyPrintableChars( chars);
    }

  /**
   * Changes the set of characters used to generate matches for the "." expression.
   * @deprecated Use {@link RegExpGenBuilder} to configure options
   */
  @Deprecated
  public void setAnyPrintableChars( String chars)
    {
    matchOptions_.setAnyPrintableChars( chars);
    }

  /**
   * Returns the set of characters used to generate matches for the "." expression.
   */
  public Set<Character> getAnyPrintableChars()
    {
    return matchOptions_.getAnyPrintableChars();
    }

  public String toString()
    {
    return
      ToString.getBuilder( this)
      .toString();
    }

  private final MatchOptions matchOptions_;

  /**
   * All printable characters in the basic and supplemental Latin-1 code blocks
   *
   * @deprecated Use {@link MatchOptions#ANY_LATIN_1}
   */
  @Deprecated
  public static final Set<Character> ANY_LATIN_1 = MatchOptions.ANY_LATIN_1;

  /**
   * All printable characters in the ASCII code block
   *
   * @deprecated Use {@link MatchOptions#ANY_ASCII}
   */
  @Deprecated
  public static final Set<Character> ANY_ASCII = MatchOptions.ANY_ASCII;
  }
