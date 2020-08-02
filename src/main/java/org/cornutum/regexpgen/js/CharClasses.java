//////////////////////////////////////////////////////////////////////////////
// 
//                    Copyright 2020, Cornutum Project
//                             www.cornutum.org
//
//////////////////////////////////////////////////////////////////////////////

package org.cornutum.regexpgen.js;

import org.cornutum.regexpgen.GenOptions;

/**
 * Creates {@link CharClassGen} instances for standard character classes.
 */
public class CharClasses
  {
  /**
   * Creates a new CharClasses instance.
   */
  public CharClasses( GenOptions options)
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
    AnyOfGen space = new AnyOfGen( options_);
    space.add( ' ');
    space.add( '\f');
    space.add( '\n');
    space.add( '\r');
    space.add( '\t');
    space.add( (char) 0x000b);
    space.add( (char) 0x00a0);
    space.add( (char) 0x1680);
    space.add( (char) 0x2028);
    space.add( (char) 0x2029);
    space.add( (char) 0x202f);
    space.add( (char) 0x205f);
    space.add( (char) 0x3000);
    space.add( (char) 0xfeff);
    space.addAll( (char) 0x2000, (char) 0x200a);

    return space;
    }

  /**
   * Returns the character class represented by "/S"
   */
  public CharClassGen nonSpace()
    {
    return new NoneOfGen( space());
    }

  private final GenOptions options_;
  }
