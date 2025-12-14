//////////////////////////////////////////////////////////////////////////////
// 
//                    Copyright 2020, Cornutum Project
//                             www.cornutum.org
//
//////////////////////////////////////////////////////////////////////////////

package org.cornutum.regexpgen.js;

import org.cornutum.regexpgen.Bounds;
import org.cornutum.regexpgen.MatchOptions;
import org.cornutum.regexpgen.RandomGen;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.IntStream;

/**
 * Generates a sequence based on a set of characters.
 */
public abstract class CharClassGen extends AbstractRegExpGen
  {
  /**
   * Creates a new CharClassGen instance.
   */
  protected CharClassGen( MatchOptions options)
    {
    super( options);
    }
  
  /**
   * Creates a new CharClassGen instance.
   */
  protected CharClassGen( MatchOptions options, char c)
    {
    super( options);
    add( c);
    }
  
  /**
   * Creates a new CharClassGen instance.
   */
  protected CharClassGen( MatchOptions options, char first, char last)
    {
    super( options);
    addAll( first, last);
    }
  
  /**
   * Creates a new CharClassGen instance.
   */
  protected CharClassGen( MatchOptions options, Set<Character> chars)
    {
    super( options);
    addAll( chars);
    }

  /**
   * Adds a character to this class
   */
  public void add( char c) 
    {
    getCharSet().add( c);
    charArray_ = null;
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
   * Adds all of the given characters to this class.
   */
  public void addAll( String chars) 
    {
    for( int i = 0; i < chars.length(); i++)
      {
      add( chars.charAt( i));
      }
    }

  /**
   * Adds all of the given characters to this class.
   */
  public void addAll( CharClassGen charClass) 
    {
    if( charClass != null)
      {
      for( Character c : charClass.getChars())
        {
        add( c);
        }
      }
    }

  /**
   * Adds all of the given characters to this class.
   */
  public void addAll( Set<Character> chars) 
    {
    if( chars != null)
      {
      chars.forEach( c -> add( c.charValue()));
      }
    }

  /**
   * Returns the characters in this class.
   */
  public Character[] getChars()
    {
    if( charArray_ == null)
      {
      charArray_ = makeChars();
      }
    
    return charArray_;
    }

  /**
   * Returns true if the given character belongs to this class.
   */
  public boolean contains( Character c)
    {
    return Arrays.stream( getChars()).anyMatch( classChar -> c.equals( classChar));
    }

  /**
   * Returns the set of characters that define this class.
   */
  protected Set<Character> getCharSet()
    {
    return chars_;
    }

  /**
   * Creates an array containing the characters in this class
   */
  protected Character[] makeChars()
    {
    return getCharSet().stream().toArray( Character[]::new);
    }

  /**
   * Returns true if and only if this class contains no characters.
   */
  public boolean isEmpty()
    {
    return getCharSet().isEmpty();
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

  /**
   * Returns a random string within the given bounds that matches this regular expression.
   */
  protected String generateLength( RandomGen random, Bounds length)
    {
    StringBuilder matching = new StringBuilder();

    Character[] chars = getChars();
    int generated = random.within( length);
    if( generated > 0 && chars.length == 0)
      {
      throw
        new IllegalStateException(
          String.format(
            "%s: Can't generate string of valid length=%s -- no matching characters available",
            this,
            generated));
      }
    
    IntStream.range( 0, generated)
      .forEach( i -> matching.append( chars[ random.below( chars.length)]));
    
    return matching.toString();
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
      && other.getCharSet().equals( getCharSet());
    }

  public int hashCode()
    {
    return
      super.hashCode()
      ^ getCharSet().hashCode();
    }

  private Set<Character> chars_ = new HashSet<Character>();
  private Character[] charArray_ = null;

  /**
   * Builds a {@link CharClassGen} instance.
   */
  @SuppressWarnings("unchecked")
  protected static abstract class CharClassGenBuilder<T extends CharClassGenBuilder<T>> extends BaseBuilder<T>
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

    public T addAll( Set<Character> chars) 
      {
      getCharClassGen().addAll( chars);
      return (T) this;
      }

    public T digit() 
      {
      getCharClassGen().addAll( charClasses_.digit());
      return (T) this;
      }

    public T nonDigit() 
      {
      getCharClassGen().addAll( charClasses_.nonDigit());
      return (T) this;
      }

    public T word() 
      {
      getCharClassGen().addAll( charClasses_.word());
      return (T) this;
      }

    public T nonWord() 
      {
      getCharClassGen().addAll( charClasses_.nonWord());
      return (T) this;
      }

    public T space() 
      {
      getCharClassGen().addAll( charClasses_.space());
      return (T) this;
      }

    public T nonSpace() 
      {
      getCharClassGen().addAll( charClasses_.nonSpace());
      return (T) this;
      }

    private CharClasses charClasses_ = new CharClasses( BUILDER_OPTIONS);
    }
  }
