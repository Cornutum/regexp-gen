/////////////////////////////////////////////////////////////////////////////
// 
//                    Copyright 2020, Cornutum Project
//                             www.cornutum.org
//
//////////////////////////////////////////////////////////////////////////////

package org.cornutum.regexpgen.js;

import org.cornutum.regexpgen.Bounds;
import org.cornutum.regexpgen.RegExpGen;
import org.cornutum.regexpgen.util.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Returns the {@link RegExpGen} represented by a Javascript regular expression.
 */
public class Parser
  {
  /**
   * Returns the {@link RegExpGen} represented by this Javascript regular expression.
   */
  public static RegExpGen parseRegExp( String regexp)
    {
    return new Parser( regexp).parse();
    }

  /**
   * Creates a new Parser instance.
   */
  private Parser( String regexp)
    {
    chars_ = regexp;
    }

  /**
   * Returns the {@link RegExpGen} represented by this Javascript regular expression.
   */
  private RegExpGen parse()
    {
    List<RegExpGen> seq = new ArrayList<RegExpGen>();

    char c = peekc();
    if( c != '^' && c != EOS)
      {
      seq.add( new AnyPrintableGen( 0, null));
      }
    else
      {
      pop(1);
      }

    seq.add(
      Optional.ofNullable( getNext())
      .orElseThrow( () -> error( "No regular expression found")));

    c = peekc();
    if( c == EOS)
      {
      seq.add( new AnyPrintableGen( 0, null));
      }
    else if( c != '$')
      {
      unexpectedChar( c);
      }

    return
      seq.size() > 1
      ? new SeqGen( seq)
      : seq.get(0);
    }

  /**
   * Returns the {@link RegExpGen} represented by next element of this Javascript regular expression.
   */
  private AbstractRegExpGen getNext()
    {
    List<AbstractRegExpGen> alternatives = new ArrayList<AbstractRegExpGen>();
    AbstractRegExpGen alternative = getAlternative();
    if( alternative != null)
      {
      alternatives.add( alternative);
      while( peekc() == '|')
        {
        pop(1);
        alternatives.add(
          Optional.ofNullable( getAlternative())
          .orElseThrow( () -> error( "Alternative missing")));
        }
      }

    return
      alternatives.isEmpty()?
      null :

      alternatives.size() > 1?
      new AlternativeGen( alternatives) :

      alternatives.get(0);
    }

  /**
   * Returns the {@link RegExpGen} represented by next alternative of this Javascript regular expression.
   */
  private AbstractRegExpGen getAlternative()
    {
    AbstractRegExpGen alternative = null;
    AbstractRegExpGen term = getTerm();
    if( term != null)
      {
      SeqGen seq;
      for( seq = new SeqGen( term); (term = getTerm()) != null; seq.add( term));
      alternative = seq;
      }

    return alternative;
    }

  /**
   * Returns the {@link RegExpGen} represented by next term of this Javascript regular expression.
   */
  private AbstractRegExpGen getTerm()
    {
    if( "\\b".equalsIgnoreCase( peek(2)))
      {
      throw error( "Unsupported word boundary expression");
      }
    if( "(?<!".equals( peek(4)))
      {
      throw error( "Unsupported negative look-behind expression");
      }

    RegExpGen prefix = null;
    if( "(?<=".equals( peek(4)))
      {
      pop(4);

      prefix =
        Optional.ofNullable( getNext())
        .orElseThrow( () -> error( "Missing look-behind prefix"));

      if( peekc() != ')')
        {
        throw error( "Missing ')'");
        }
      pop(1);
      }

    AbstractRegExpGen quantified = getQuantified();
    if( prefix != null)
      {
      if( quantified != null)
        {
        quantified = new SeqGen( prefix, quantified);
        }
      else
        {
        throw error( "Missing regular expression");
        }
      }

    if( "(?!".equals( peek(3)))
      {
      throw error( "Unsupported negative look-ahead expression");
      }
    
    if( "(?=".equals( peek(3)))
      {
      if( quantified == null)
        {
        throw error( "Unexpected look-ahead expression");
        }
      pop(3);

      RegExpGen suffix =
        Optional.ofNullable( getNext())
        .orElseThrow( () -> error( "Missing look-ahead suffix"));

      if( peekc() != ')')
        {
        throw error( "Missing ')'");
        }
      pop(1);

      quantified = new SeqGen( quantified, suffix);
      }

    return quantified;
    }

  /**
   * Returns the {@link RegExpGen} represented by next quantified atom of this Javascript regular expression.
   */
  private AbstractRegExpGen getQuantified()
    {
    AbstractRegExpGen atom = getAtom();
    if( atom != null)
      {
      Bounds quantifier = getQuantifier();
      if( quantifier != null)
        {
        atom.setOccurrences( quantifier);
        }
      }

    return atom;
    }

  /**
   * Returns the {@link Bounds} represented by the next quantifier in this Javascript regular expression.
   */
  private Bounds getQuantifier()
    {
    Integer minOccur;
    Integer maxOccur;
    
    switch( peekc())
      {
      case '?':
        {
        pop(1);
        minOccur = 0;
        maxOccur = 1;
        break;
        }
      case '*':
        {
        pop(1);
        minOccur = 0;
        maxOccur = null;
        break;
        }
      case '+':
        {
        pop(1);
        minOccur = 1;
        maxOccur = null;
        break;
        }
      case '{':
        {
        pop(1);

        minOccur =
          Optional.ofNullable( getDecimal())
          .orElseThrow( () -> error( "Missing number"));

        if( peekc() == ',')
          {
          pop(1);
          maxOccur = getDecimal();
          }
        else
          {
          maxOccur = minOccur;
          }

        if( peekc() != '}')
          {
          throw error( "Missing '}'");
          }
        
        break;
        }
      default:
        {
        minOccur = null;
        maxOccur = null;
        break;
        }
      }

    Bounds quantifier = null;
    if( minOccur != null)
      {
      if( peekc() == '?')
        {
        pop(1);
        maxOccur = minOccur;
        }

      quantifier = new Bounds( minOccur, maxOccur);
      }
    
    return quantifier;
    }

  /**
   * Returns the next decimal integer from this Javascript regular expression.
   */
  private Integer getDecimal()
    {
    String decimalChars = "0123456789";

    StringBuilder decimal = new StringBuilder();
    for( char c = 0;
         decimalChars.indexOf( (c = peekc())) >= 0;
         pop(1), decimal.append( c));

    try
      {
      return
        decimal.length() == 0
        ? null
        : Integer.valueOf( decimal.toString());
      }
    catch( Exception e)
      {
      throw error( String.format( "Invalid decimal string=%s", decimal.toString()));
      }
    }

  /**
   * Returns the {@link RegExpGen} represented by next atom of this Javascript regular expression.
   */
  private AbstractRegExpGen getAtom()
    {
    AbstractRegExpGen atom;
      
    if( (atom = getGroup()) == null
        &&
        (atom = getCharClass()) == null
        &&
        (atom = getAtomEscape()) == null
        &&
        (atom = getAnyOne()) == null)
      {
      atom = getPatternChar();
      }

    return atom;
    }

  /**
   * Returns the {@link RegExpGen} represented by a group.
   */
  private AbstractRegExpGen getGroup()
    {
    AbstractRegExpGen group = null;
    if( peekc() == '(')
      {
      pop(1);

      if( "?:".equals( peek(2)))
        {
        // Generation doesn't depend on capturing.
        pop(2);
        }
      else if( "?<".equals( peek(2)))
        {
        // Generation doesn't depend on capturing -- ignore group name.
        for( pop(2); peekc() != '>'; pop(1));
        pop(1);
        }

      group =
        Optional.ofNullable( getNext())
        .orElseThrow( () -> error( "Incomplete group expression"));

      if( peekc() != ')')
        {
        throw error( "Missing ')'");
        }
      pop(1);
      }

    return group;
    }

  /**
   * Returns the {@link RegExpGen} represented by a single character.
   */
  private AbstractRegExpGen getAnyOne()
    {
    AbstractRegExpGen anyOne = null;

    if( peekc() == '.')
      {
      pop(1);
      anyOne = new AnyPrintableGen(1);
      }

    return anyOne;
    }

  /**
   * Returns the {@link RegExpGen} represented by a single character.
   */
  private AbstractRegExpGen getAtomEscape()
    {
    CharClassGen escapeClass = null;
    if( peekc() == '\\')
      {
      pop(1);

      escapeClass =
        Optional.ofNullable( getCharClassEscape())
        .orElse( getCharEscape());
      }

    return escapeClass;
    }

  /**
   * Returns the {@link RegExpGen} represented by a character class.
   */
  private AbstractRegExpGen getCharClass()
    {
    CharClassGen charClass = null;
    if( peekc() == '[')
      {
      pop(1);

      if( peekc() == '^')
        {
        pop(1);
        charClass = new NoneOfGen();
        }
      else
        {
        charClass = new AnyOfGen();
        }

      char c;
      CharClassGen prevClass;
      for( prevClass = null,
             c = peekc();

           c != ']'
             && c != EOS;

           c = peekc())
        {
        Matcher rangeMatcher;
        charClass.addAll( prevClass);

        if( c == '-')
          {
          if( !charClass.isEmpty())
            {
            unexpectedChar( c);
            }

          // Include '-' in this class
          prevClass = new AnyOfGen( c);
          pop(1);
          }
        
        else if( (prevClass = getClassEscape()) != null)
          {
          // Include escaped char(s) in this class
          }
        
        else if( (rangeMatcher = rangePattern_.matcher( peek( 3))).matches())
          {
          char first = rangeMatcher.group(1).charAt(0);
          char last = rangeMatcher.group(2).charAt(0);
          if( last == '\\' || last == '-')
            {
            pop(2);
            unexpectedChar( last);
            }

          // Include character range in this class
          prevClass = new AnyOfGen( first, last);
          pop(3);
          }
        
        else
          {
          // Include single char in this class
          prevClass = new AnyOfGen( c);
          pop(1);
          }
        }
      charClass.addAll( prevClass);
      
      if( c != ']')
        {
        throw error( "Missing ']'");
        }
      if( charClass.isEmpty())
        {
        throw error( "Empty character class");
        }
      pop(1);
      }

    return charClass;
    }

  /**
   * Returns the {@link RegExpGen} represented by an escaped character class
   */
  private CharClassGen getClassEscape()
    {
    CharClassGen escapeClass = null;
    if( peekc() == '\\')
      {
      pop(1);

      if( (escapeClass = getBackspaceEscape()) == null
          &&
          (escapeClass = getCharClassEscape()) == null)
        {
        escapeClass = getCharEscape();
        }
      }

    return escapeClass;
    }

  /**
   * Returns the {@link RegExpGen} represented by an escaped backspace character.
   */
  private CharClassGen getBackspaceEscape()
    {
    CharClassGen escapeClass = null; 

    if( peekc() == 'b')
      {
      pop(1);
      escapeClass = new AnyOfGen( '\b');
      }
    
    return escapeClass;
    }

  /**
   * Returns the {@link RegExpGen} represented by an escaped character class
   */
  private CharClassGen getCharClassEscape()
    {
    CharClassGen escapeClass; 
    char id = peekc();

    switch( id)
      {
      case 'd':
      case 'D':
        {
        escapeClass = new AnyOfGen( '0', '9');
        if( id == 'D')
          {
          escapeClass = new NoneOfGen( escapeClass);
          }
        break;
        }
      case 'w':
      case 'W':
        {
        escapeClass = new AnyOfGen( '0', '9');
        escapeClass.addAll( 'A', 'Z');
        escapeClass.addAll( 'a', 'z');
        escapeClass.add( '_');
        if( id == 'W')
          {
          escapeClass = new NoneOfGen( escapeClass);
          }
        break;
        }
      case 's':
      case 'S':
        {
        escapeClass = new AnyOfGen();
        escapeClass.add( '\f');
        escapeClass.add( '\n');
        escapeClass.add( '\r');
        escapeClass.add( '\t');
        escapeClass.add( (char) 0x000b);
        escapeClass.add( (char) 0x00a0);
        escapeClass.add( (char) 0x1680);
        escapeClass.add( (char) 0x2028);
        escapeClass.add( (char) 0x2029);
        escapeClass.add( (char) 0x202f);
        escapeClass.add( (char) 0x205f);
        escapeClass.add( (char) 0x3000);
        escapeClass.add( (char) 0xfeff);
        escapeClass.addAll( (char) 0x2000, (char) 0x200a);
        if( id == 'S')
          {
          escapeClass = new NoneOfGen( escapeClass);
          }
        break;
        }
      default:
        {
        escapeClass = null;
        break;
        }
      }

    if( escapeClass != null)
      {
      pop(1);
      }

    return escapeClass;
    }

  /**
   * Returns the {@link RegExpGen} represented by an escaped character.
   */
  private CharClassGen getCharEscape()
    {
    CharClassGen charClass;

    if( (charClass = getNamedCharEscape()) == null
        &&
        (charClass = getControlEscape()) == null
        &&
        (charClass = getHexCharClass()) == null
        &&
        (charClass = getUnicodeCharClass()) == null)
      {
      charClass = getLiteralChar();
      }

    return charClass;
    }

  /**
   * Returns the {@link RegExpGen} represented by a named escaped character.
   */
  private CharClassGen getNamedCharEscape()
    {
    CharClassGen escapeClass; 
    char id = peekc();

    switch( id)
      {
      case 't':
        {
        escapeClass = new AnyOfGen( '\t');
        break;
        }
      case 'r':
        {
        escapeClass = new AnyOfGen( '\r');
        break;
        }
      case 'n':
        {
        escapeClass = new AnyOfGen( '\n');
        break;
        }
      case 'f':
        {
        escapeClass = new AnyOfGen( '\f');
        break;
        }
      case 'v':
        {
        escapeClass = new AnyOfGen( (char) 0x000b);
        break;
        }
      case '0':
        {
        escapeClass = new AnyOfGen( (char) 0);
        break;
        }
      default:
        {
        escapeClass = null;
        break;
        }
      }

    if( escapeClass != null)
      {
      pop(1);
      }

    return escapeClass;
    }

  /**
   * Returns the {@link RegExpGen} represented by a control character
   */
  private CharClassGen getControlEscape()
    {
    CharClassGen escapeClass = null;

    if( peekc() == 'c')
      {
      pop(1);

      String controlChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
      int controlChar = controlChars.indexOf( Character.toUpperCase( peekc()));
      if( controlChar < 0)
        {
        throw error( String.format( "Invalid control escape character='%c'", peekc()));
        }
      pop(1);

      escapeClass = new AnyOfGen( (char) (0x0001 + controlChar));
      }
    
    return escapeClass;
    }

  /**
   * Returns the {@link RegExpGen} represented by a hexadecimal character
   */
  private CharClassGen getHexCharClass()
    {
    CharClassGen escapeClass = null;

    if( peekc() == 'x')
      {
      pop(1);

      String digits = peek(2);
      Matcher hexCharMatcher = hexCharPattern_.matcher( digits);
      if( !hexCharMatcher.matches())
        {
        throw error( String.format( "Invalid hex character='%s'", digits));
        }
      pop(2);

      escapeClass = new AnyOfGen( (char) Integer.parseInt( digits, 16));
      }
    
    return escapeClass;
    }

  /**
   * Returns the {@link RegExpGen} represented by a Unicode character
   */
  private CharClassGen getUnicodeCharClass()
    {
    CharClassGen escapeClass = null;

    if( peekc() == 'u')
      {
      pop(1);

      String digits = peek(4);
      Matcher unicodeCharMatcher = unicodeCharPattern_.matcher( digits);
      if( !unicodeCharMatcher.matches())
        {
        throw error( String.format( "Invalid Unicode character='%s'", digits));
        }
      pop(4);

      escapeClass = new AnyOfGen( (char) Integer.parseInt( digits, 16));
      }
    
    return escapeClass;
    }

  /**
   * Returns the {@link RegExpGen} represented by a literal character.
   */
  private CharClassGen getLiteralChar()
    {
    return
      Optional.of( peekc())
      .filter( c -> c != EOS)
      .map( c -> {
        CharClassGen literal = new AnyOfGen( c);
        pop(1);
        return literal;
        })
      .orElse( null);
    }

  /**
   * Returns the {@link RegExpGen} represented by a non-delimiter character.
   */
  private CharClassGen getPatternChar()
    {
    String syntaxChars = "^$\\.*+?()[]{}|";
    
    return
      Optional.of( peekc())
      .filter( c -> c != EOS && syntaxChars.indexOf( c) < 0)
      .map( c -> {
        CharClassGen patternChar = new AnyOfGen( c);
        pop(1);
        return patternChar;
        })
      .orElse( null);
    }

  /**
   * Reports an unexpected character error.
   */
  private void unexpectedChar( char c)
    {
    throw
      error(
        c == EOS
        ? "Unexpected end of string"
        : String.format( "Unexpected character=%s", c));
    }

  /**
   * Returns a parse error exception.
   */
  private RuntimeException error( String reason)
    {
    return new IllegalStateException( String.format( "%s at position=%s", reason, cursor_));
    }

  /**
   * Returns the next character.
   */
  private char peekc()
    {
    return
      cursor_ < chars_.length()
      ? chars_.charAt( cursor_)
      : EOS;
    }

  /**
   * Returns the next <CODE>N</CODE> characters. Returns all remaining characters if
   * fewer than <CODE>N</CODE> characters remain.
   */
  private String peek( int n)
    {
    return chars_.substring( cursor_, Math.min( cursor_ + n, chars_.length()));
    }

  /**
   * Removes the next <CODE>N</CODE> characters. Removes all remaining characters if
   * fewer than <CODE>N</CODE> characters remain.
   */
  private void pop( int n)
    {
    cursor_ = Math.min( cursor_ + n, chars_.length());
    }

  public String toString()
    {
    return
      ToString.getBuilder( this)
      .append(
        String.format(
          "%s%s%s",
          chars_.substring( 0, cursor_),
          new Character( (char) 0x00BB),
          chars_.substring( cursor_)))
      .toString();
    }
  
  private final String chars_;
  private int cursor_ = 0;

  private static final char EOS = (char) -1;
  private static final Pattern rangePattern_ = Pattern.compile( "(.)-([^\\]])");
  private static final Pattern hexCharPattern_ = Pattern.compile( "\\p{XDigit}{2}");
  private static final Pattern unicodeCharPattern_ = Pattern.compile( "\\p{XDigit}{4}");
  }
