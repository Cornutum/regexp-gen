//////////////////////////////////////////////////////////////////////////////
// 
//                    Copyright 2020, Cornutum Project
//                             www.cornutum.org
//
//////////////////////////////////////////////////////////////////////////////

package org.cornutum.regexpgen.js;

import java.util.HashSet;
import java.util.Set;

import org.cornutum.regexpgen.util.ToString;

import static java.util.stream.Collectors.joining;

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

  public String toString()
    {
    return
      ToString.getBuilder( this)
      .append( "chars", charsString())
      .appendSuper( super.toString())
      .toString();
    }

  public boolean equals( Object object)
    {
    CharClassGen other =
      object instanceof CharClassGen
      ? (CharClassGen) object
      : null;

    return
      other != null
      && super.equals( other)
      && other.chars_.equals( chars_);
    }

  public int hashCode()
    {
    return
      super.hashCode()
      ^ chars_.hashCode();
    }

  /**
   * Returns a string representation of the characters in this class.
   */
  private String charsString()
    {
    StringBuilder chars = new StringBuilder();
    chars.append( '[');
    chars.append( chars_.stream().sorted().map( Object::toString).collect( joining( "")));
    chars.append( ']');
    return chars.toString();
    }

  /**
   * Returns the character class represented by "/d"
   */
  public static CharClassGen digit()
    {
    return new AnyOfGen( '0', '9');
    }

  /**
   * Returns the character class represented by "/D"
   */
  public static CharClassGen nonDigit()
    {
    return new NoneOfGen( digit());
    }

  /**
   * Returns the character class represented by "/w"
   */
  public static CharClassGen word()
    {
    AnyOfGen word = new AnyOfGen( '0', '9');
    word.addAll( 'A', 'Z');
    word.addAll( 'a', 'z');
    word.add( '_');
    return word;
    }

  /**
   * Returns the character class represented by "/W"
   */
  public static CharClassGen nonWord()
    {
    return new NoneOfGen( word());
    }

  /**
   * Returns the character class represented by "/s"
   */
  public static CharClassGen space()
    {
    AnyOfGen space = new AnyOfGen();
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
  public static CharClassGen nonSpace()
    {
    return new NoneOfGen( space());
    }

  private Set<Character> chars_ = new HashSet<Character>();

  /**
   * Builds a {@link CharClassGen} instance.
   */
  @SuppressWarnings("unchecked")
  public static abstract class CharClassGenBuilder<T extends CharClassGenBuilder<T>> extends BaseBuilder<T>
    {
    /**
     * Returns the {@link CharClassGen} instance for this builder.
     */
    protected abstract CharClassGen getCharClassGen();

    /**
     * Returns the {@link AbstractRegExpGen} instance for this builder.
     */
    protected AbstractRegExpGen getAbstractRegExpGen()
      {
      return getCharClassGen();
      }
    
    public T add( char c) 
      {   
      getCharClassGen().add( c);
      return (T) this;
      }

    public T addAll( char first, char last) 
      {
      getCharClassGen().addAll( first, last);
      return (T) this;
      }

    public T addAll( String chars) 
      {
      getCharClassGen().addAll( chars);
      return (T) this;
      }

    public T addAll( CharClassGen charClass) 
      {
      getCharClassGen().addAll( charClass);
      return (T) this;
      }

    public T digit() 
      {
      getCharClassGen().addAll( CharClassGen.digit());
      return (T) this;
      }

    public T nonDigit() 
      {
      getCharClassGen().addAll( CharClassGen.nonDigit());
      return (T) this;
      }

    public T word() 
      {
      getCharClassGen().addAll( CharClassGen.word());
      return (T) this;
      }

    public T nonWord() 
      {
      getCharClassGen().addAll( CharClassGen.nonWord());
      return (T) this;
      }

    public T space() 
      {
      getCharClassGen().addAll( CharClassGen.space());
      return (T) this;
      }

    public T nonSpace() 
      {
      getCharClassGen().addAll( CharClassGen.nonSpace());
      return (T) this;
      }
    }
  }
