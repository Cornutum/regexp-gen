//////////////////////////////////////////////////////////////////////////////
// 
//                    Copyright 2020, Cornutum Project
//                             www.cornutum.org
//
//////////////////////////////////////////////////////////////////////////////

package org.cornutum.regexpgen.js;

import org.cornutum.regexpgen.MatchOptions;

/**
 * Creates {@link CharClassGen} instances for standard character classes.
 */
public class CharClasses
  {
  /**
   * Creates a new CharClasses instance.
   */
  public CharClasses( MatchOptions options)
    {
    options_ = options;
    }

  /**
   * Returns the character class represented by "/d"
   */
  public CharClassGen digit()
    {
    return new AnyOfGen( options_, '0', '9');
    }

  /**
   * Returns the character class represented by "/D"
   */
  public CharClassGen nonDigit()
    {
    return new NoneOfGen( digit());
    }

  /**
   * Returns the character class represented by "/w"
   */
  public CharClassGen word()
    {
    AnyOfGen word = new AnyOfGen( options_, '0', '9');
    word.addAll( 'A', 'Z');
    word.addAll( 'a', 'z');
    word.add( '_');
    return word;
    }

  /**
   * Returns the character class represented by "/W"
   */
  public CharClassGen nonWord()
    {
    return new NoneOfGen( word());
    }

  /**
   * Returns the character class represented by "/s"
   */
  public CharClassGen space()
    {
    return new AnyOfGen( options_, options_.getSpaceChars());
    }

  /**
   * Returns the character class represented by "/S"
   */
  public CharClassGen nonSpace()
    {
    return new NoneOfGen( space());
    }

  private final MatchOptions options_;
  }
