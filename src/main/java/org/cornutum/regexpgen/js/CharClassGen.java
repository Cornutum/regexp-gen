//////////////////////////////////////////////////////////////////////////////
// 
//                    Copyright 2020, Cornutum Project
//                             www.cornutum.org
//
//////////////////////////////////////////////////////////////////////////////

package org.cornutum.regexpgen.js;

import java.util.HashSet;
import java.util.Set;

/**
 * Generates a sequence based on a set of characters.
 */
public abstract class CharClassGen extends AbstractRegExpGen
  {
  /**
   * Creates a new CharClassGen instance.
   */
  protected CharClassGen()
    {
    super();
    }
  
  /**
   * Creates a new CharClassGen instance.
   */
  protected CharClassGen( char c)
    {
    super();
    add( c);
    }
  
  /**
   * Creates a new CharClassGen instance.
   */
  protected CharClassGen( char first, char last)
    {
    super();
    addAll( first, last);
    }

  /**
   * Adds a character to this class
   */
  public void add( char c) 
    {
    chars_.add( c);
    }

  /**
   * Adds all characters in the given range to this class
   */
  public void addAll( char first, char last) 
    {
    int min = Character.codePointAt( new char[]{ (char) Math.min( first, last)}, 0);
    int max = Character.codePointAt( new char[]{ (char) Math.max( first, last)}, 0);

    StringBuilder range = new StringBuilder();
    for( int c = min; c <= max; c++)
      {
      range.appendCodePoint( c);
      }

    addAll( range.toString());
    }

  /**
   * Adds all of the given characters to this class
   */
  public void addAll( String chars) 
    {
    for( int i = 0; i < chars.length(); i++)
      {
      add( chars.charAt( i));
      }
    }

  /**
   * Adds all of the given characters to this class
   */
  public void addAll( CharClassGen charClass) 
    {
    if( charClass != null)
      {
      for( Character c : charClass.chars_)
        {
        add( c);
        }
      }
    }

  /**
   * Returns true if and only if this class contains no characters.
   */
  public boolean isEmpty()
    {
    return chars_.isEmpty();
    }

  /**
   * Returns the minimum length for any matching string.
   */
  public int getMinLength()
    {
    return getMinOccur();
    }

  /**
   * Returns the maximum length for any matching string.
   */
  public int getMaxLength()
    {
    return getMaxOccur();
    }

  private Set<Character> chars_ = new HashSet<Character>();
  }
