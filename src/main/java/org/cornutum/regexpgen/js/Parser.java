/////////////////////////////////////////////////////////////////////////////
// 
//                    Copyright 2020, Cornutum Project
//                             www.cornutum.org
//
//////////////////////////////////////////////////////////////////////////////

package org.cornutum.regexpgen.js;

import org.cornutum.regexpgen.Bounds;
import org.cornutum.regexpgen.GenOptions;
import org.cornutum.regexpgen.RegExpGen;
import org.cornutum.regexpgen.util.ToString;

import org.apache.commons.collections4.IterableUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Returns the {@link AbstractRegExpGen} represented by a JavaScript regular expression.
 */
public class Parser
  {
  /**
   * Returns an {@link AbstractRegExpGen} that generates strings containing characters that match the given
   * JavaScript regular expression.
   *
   * @deprecated Replaced by {@link Provider#matching}
   */
  public static AbstractRegExpGen parseRegExp( String regexp) throws IllegalArgumentException
    {
    return new Parser( regexp).parse( false);
    }
  
  /**
   * Returns an {@link AbstractRegExpGen} that generates strings containing only characters that match the
   * given JavaScript regular expression.
   *
   * @deprecated Replaced by {@link Provider#matchingExact}
   */
  public static AbstractRegExpGen parseRegExpExact( String regexp) throws IllegalArgumentException
    {
    return new Parser( regexp).parse( true);
    }

  /**
   * Creates a new Parser instance.
   */
  Parser( String regexp)
    {
    this( regexp, null);
    }

  /**
   * Creates a new Parser instance with the given options.
   */
  Parser( String regexp, GenOptions options)
    {
    chars_ = regexp;
    options_ = options != null ? options : new GenOptions();
    charClasses_ = new CharClasses( options_);
    }

  /**
   * Returns the {@link AbstractRegExpGen} represented by this JavaScript regular expression.
   * If <CODE>exact</CODE> is true, the result generates strings containing only
   * characters matching this regular expression. Otherwise, the result generates strings
   * that may contain other characters surrounding the matching characters.
   */
  AbstractRegExpGen parse( boolean exact) throws IllegalArgumentException
    {
    AbstractRegExpGen regExpGen = getNext();

    char c = peekc();
    if( c != EOS)
      {
      unexpectedChar( c);
      }
    
    return
      Optional.ofNullable( regExpGen)
      .map( r -> exact? r : withStartGen( withEndGen( r)))
      .orElse( null);
    }

  /**
   * Returns the {@link RegExpGen} represented by next element of this JavaScript regular expression.
   */
  private AbstractRegExpGen getNext()
    {
    int cursorStart = cursor();
    List<AbstractRegExpGen> alternatives = new ArrayList<AbstractRegExpGen>();
    AbstractRegExpGen alternative = getAlternative();
    if( alternative != null)
      {
      alternatives.add( alternative);
      while( peekc() == '|')
        {
        advance(1);
        alternatives.add(
          Optional.ofNullable( getAlternative())
          .orElseThrow( () -> error( "Alternative missing")));
        }
      }

    return
      alternatives.isEmpty()?
      null :

      alternatives.size() > 1?
      startingAt( cursorStart, new AlternativeGen( options(), alternatives)) :

      alternatives.get(0);
    }

  /**
   * Returns the {@link RegExpGen} represented by next alternative of this JavaScript regular expression.
   */
  private AbstractRegExpGen getAlternative()
    {
    int cursorStart = cursor();
    List<AbstractRegExpGen> terms = new ArrayList<AbstractRegExpGen>();

    List<AbstractRegExpGen> termExpr;
    while( (termExpr = getTerm()) != null)
      {
      for( AbstractRegExpGen term : termExpr)
        {
        if( term.isAnchoredStart() && isAnchoredStart( terms))
          {
          throw error( "Start-anchored expression can be matched at most once");
          }
        if( term.isAnchoredStart() && !terms.isEmpty())
          {
          throw error( "Extra expressions not allowed preceding ^ anchor");
          }
        if( isAnchoredEnd( terms))
          {
          throw error( "Extra expressions not allowed after $ anchor");
          } 

        terms.add( term);
        }
      }

    return
      terms.isEmpty()?
      null :

      terms.size() > 1?
      startingAt( cursorStart, new SeqGen( options(), terms)) :
      
      terms.get(0);      
    }

  /**
   * Returns true if the given sequence of terms is start-anchored.
   */
  private boolean isAnchoredStart( List<AbstractRegExpGen> terms)
    {
    return !terms.isEmpty() && terms.get(0).isAnchoredStart();
    }

  /**
   * Returns true if the given sequence of terms is end-anchored.
   */
  private boolean isAnchoredEnd( List<AbstractRegExpGen> terms)
    {
    return !terms.isEmpty() && terms.get( terms.size() - 1).isAnchoredEnd();
    }

  /**
   * Returns the sequence of {@link RegExpGen} instances represented by next term of this JavaScript regular expression.
   */
  private List<AbstractRegExpGen> getTerm()
    {
    int cursorStart = cursor();
    List<AbstractRegExpGen> termExpr = new ArrayList<AbstractRegExpGen>();
    
    // Get any start assertion
    AbstractRegExpGen prefix = null;
    boolean anchoredStart = false;
    for( boolean assertionFound = true; assertionFound; cursorStart = cursor())
      {
      if( "\\b".equalsIgnoreCase( peek(2)))
        {
        throw error( "Unsupported word boundary assertion");
        }

      if( "(?<!".equals( peek(4)))
        {
        throw error( "Unsupported negative look-behind assertion");
        }

      if( (assertionFound = "(?<=".equals( peek(4))))
        {
        advance(4);

        prefix =
          Optional.ofNullable( getNext())
          .orElseThrow( () -> error( "Missing look-behind expression"));

        if( peekc() != ')')
          {
          throw error( "Missing ')'");
          }
        advance(1);
        prefix = startingAt( cursorStart, prefix);
        }
      else if( (assertionFound = peekc() == '^'))
        {
        anchoredStart = true;
        advance(1);
        }        
      }
    if( prefix != null && anchoredStart)
      {
      throw error( "Start assertion is inconsistent with look-behind assertion");
      }

    // Get atomic expression
    int cursorStartQuantified = cursor();
    AbstractRegExpGen quantified = getQuantified();
    if( quantified == null && prefix != null)
      {
      throw error( "Missing regular expression for look-behind assertion");
      }
    quantified = startingAt( cursorStartQuantified, quantified);

    // Get any end assertion
    cursorStart = cursor();
    AbstractRegExpGen suffix = null;
    boolean anchoredEnd = false;
    for( boolean assertionFound = true; assertionFound; cursorStart = cursor())
      {
      if( "\\b".equalsIgnoreCase( peek(2)))
        {
        throw error( "Unsupported word boundary assertion");
        }
      if( "(?!".equals( peek(3)))
        {
        throw error( "Unsupported negative look-ahead assertion");
        }
      if( (assertionFound = "(?=".equals( peek(3))))
        {
        if( quantified == null)
          {
          throw error( "Missing regular expression for look-ahead assertion");
          }
        advance(3);

        suffix =
          Optional.ofNullable( getNext())
          .orElseThrow( () -> error( "Missing look-ahead expression"));

        if( peekc() != ')')
          {
          throw error( "Missing ')'");
          }
        advance(1);

        suffix = startingAt( cursorStart, suffix);
        }
      else if( (assertionFound = peekc() == '$'))
        {
        anchoredEnd = true;
        advance(1);
        } 
      }
    if( suffix != null && anchoredEnd)
      {
      throw error( "End assertion is inconsistent with look-ahead assertion");
      }

    // Accumulate all expressions for this term
    if( prefix != null)
      {
      termExpr.add( prefix);
      }

    if( quantified != null)
      {
      termExpr.add( quantified);
      if( anchoredStart)
        {
        quantified.setAnchoredStart( true);
        }
      if( anchoredEnd)
        {
        quantified.setAnchoredEnd( true);
        startingAt( cursorStartQuantified, quantified);
        }
      }
    else if( anchoredStart || anchoredEnd)
      {
      termExpr.add(
        startingAt(
          cursorStart,

          AnyOfGen.builder( options())
          .anyPrintable()
          .anchoredStart( anchoredStart)
          .anchoredEnd( anchoredEnd)
          .occurs(0)
          .build()));
      }
    
    if( suffix != null)
      {
      termExpr.add( suffix);
      }

    return
      termExpr.isEmpty()
      ? null
      : termExpr;
    }

  /**
   * Returns the {@link RegExpGen} represented by next quantified atom of this JavaScript regular expression.
   */
  private AbstractRegExpGen getQuantified()
    {
    int cursorStart = cursor();
    AbstractRegExpGen atom = getAtom();
    if( atom != null)
      {
      Bounds quantifier = getQuantifier();
      if( quantifier != null)
        {
        if( atom.isAnchoredEnd() && quantifier.getMaxValue() > 1 && !atom.isAnchoredEndAll())
          {
          throw error( "End-anchored expression can be matched at most once");
          }
        if( atom.isAnchoredStart() && quantifier.getMaxValue() > 1 && !atom.isAnchoredStartAll())
          {
          throw error( "Start-anchored expression can be matched at most once");
          }
        atom.setOccurrences( quantifier);
        }
      }

    return startingAt( cursorStart, atom);
    }

  /**
   * Returns the {@link Bounds} represented by the next quantifier in this JavaScript regular expression.
   */
  private Bounds getQuantifier()
    {
    Integer minOccur;
    Integer maxOccur;
    
    switch( peekc())
      {
      case '?':
        {
        advance(1);
        minOccur = 0;
        maxOccur = 1;
        break;
        }
      case '*':
        {
        advance(1);
        minOccur = 0;
        maxOccur = null;
        break;
        }
      case '+':
        {
        advance(1);
        minOccur = 1;
        maxOccur = null;
        break;
        }
      case '{':
        {
        advance(1);

        minOccur =
          Optional.ofNullable( getDecimal())
          .orElseThrow( () -> error( "Missing number"));

        if( peekc() == ',')
          {
          advance(1);
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
        advance(1);
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
        advance(1);
        maxOccur = minOccur;
        }

      quantifier = new Bounds( minOccur, maxOccur);
      }
    
    return quantifier;
    }

  /**
   * Returns the next decimal integer from this JavaScript regular expression.
   */
  private Integer getDecimal()
    {
    String decimalChars = "0123456789";

    StringBuilder decimal = new StringBuilder();
    for( char c = 0;
         decimalChars.indexOf( (c = peekc())) >= 0;
         advance(1), decimal.append( c));

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
   * Returns the {@link RegExpGen} represented by next atom of this JavaScript regular expression.
   */
  private AbstractRegExpGen getAtom()
    {
    int cursorStart = cursor();
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

    return startingAt( cursorStart, atom);
    }

  /**
   * Returns the {@link RegExpGen} represented by a group.
   */
  private AbstractRegExpGen getGroup()
    {
    int cursorStart = cursor();
    AbstractRegExpGen group = null;
    if( peekc() == '(')
      {
      advance(1);

      if( "?:".equals( peek(2)))
        {
        // Generation doesn't depend on capturing.
        advance(2);
        }
      else if( "?<".equals( peek(2)))
        {
        // Generation doesn't depend on capturing -- ignore group name.
        for( advance(2); peekc() != '>'; advance(1));
        advance(1);
        }

      group =
        Optional.ofNullable( getNext())
        .orElseThrow( () -> error( "Incomplete group expression"));

      if( peekc() != ')')
        {
        throw error( "Missing ')'");
        }
      advance(1);
      }

    return startingAt( cursorStart, group);
    }

  /**
   * Returns the {@link RegExpGen} represented by a single character.
   */
  private AbstractRegExpGen getAnyOne()
    {
    int cursorStart = cursor();
    AbstractRegExpGen anyOne = null;

    if( peekc() == '.')
      {
      advance(1);
      anyOne = startingAt( cursorStart, new AnyPrintableGen( options(),1));
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
      advance(1);

      if( (escapeClass = getCharClassEscape()) == null)
        {
        escapeClass = getCharEscape();
        }
      }

    return escapeClass;
    }

  /**
   * Returns the {@link RegExpGen} represented by a character class.
   */
  private AbstractRegExpGen getCharClass()
    {
    int cursorStart = cursor();
    CharClassGen charClass = null;
    if( peekc() == '[')
      {
      advance(1);

      if( peekc() == '^')
        {
        advance(1);
        charClass = new NoneOfGen( options());
        }
      else
        {
        charClass = new AnyOfGen( options());
        }

      char c;
      CharClassGen prevClass;
      for( prevClass = null,
             c = peekc();

           c != ']'
             && c != EOS;

           c = peekc())
        {
        // Is this a non-initial/final '-' char?
        CharClassGen rangeStart = null;
        if( c == '-' && !(prevClass == null || peek(2).endsWith( "]")))
          {
          // Yes, continue to look for end of character range
          rangeStart = prevClass;
          advance(1);
          c = peekc();
          }
        else
          {
          // No, look for new class member
          charClass.addAll( prevClass);
          }
        
        if( (prevClass = getClassEscape()) != null)
          {
          // Include escaped char(s) in this class
          }
        else
          {
          // Include single char in this class
          prevClass = new AnyOfGen( options(), c);
          advance(1);
          }

        if( rangeStart != null)
          {
          // Add char range to this class
          char first =
            Optional.of( rangeStart.getChars())
            .filter( start -> start.length == 1)
            .map( start -> start[0])
            .orElseThrow( () -> error( "Character range must begin with a specific character"));

          char last =
            Optional.of( prevClass.getChars())
            .filter( end -> end.length == 1)
            .map( end -> end[0])
            .orElseThrow( () -> error( "Character range must end with a specific character"));

          prevClass = new AnyOfGen( options(), first, last);
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
      advance(1);
      }

    return startingAt( cursorStart, charClass);
    }

  /**
   * Returns the {@link RegExpGen} represented by an escaped character class
   */
  private CharClassGen getClassEscape()
    {
    CharClassGen escapeClass = null;
    if( peekc() == '\\')
      {
      advance(1);

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
    int cursorStart = cursor();
    CharClassGen escapeClass = null; 

    if( peekc() == 'b')
      {
      advance(1);
      escapeClass = startingAt( cursorStart, new AnyOfGen( options(), '\b'));
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
        {
        escapeClass = charClasses().digit();
        break;
        }
      case 'D':
        {
        escapeClass = charClasses().nonDigit();
        break;
        }
      case 'w':
        {
        escapeClass = charClasses().word();
        break;
        }
      case 'W':
        {
        escapeClass = charClasses().nonWord();
        break;
        }
      case 's':
        {
        escapeClass = charClasses().space();
        break;
        }
      case 'S':
        {
        escapeClass = charClasses().nonSpace();
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
      advance(1);
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
    int cursorStart = cursor();
    CharClassGen escapeClass; 
    char id = peekc();

    switch( id)
      {
      case 't':
        {
        escapeClass = new AnyOfGen( options(), '\t');
        break;
        }
      case 'r':
        {
        escapeClass = new AnyOfGen( options(), '\r');
        break;
        }
      case 'n':
        {
        escapeClass = new AnyOfGen( options(), '\n');
        break;
        }
      case 'f':
        {
        escapeClass = new AnyOfGen( options(), '\f');
        break;
        }
      case 'v':
        {
        escapeClass = new AnyOfGen( options(), (char) 0x000b);
        break;
        }
      case '0':
        {
        escapeClass = new AnyOfGen( options(), (char) 0);
        break;
        }
      default:
        {
        if( Character.isDigit( id))
          {
          throw error( "Unsupported back reference to capturing group");
          }
        if( id == 'k')
          {
          throw error( "Unsupported back reference to named group");
          }
        escapeClass = null;
        break;
        }
      }

    if( escapeClass != null)
      {
      advance(1);
      }

    return startingAt( cursorStart, escapeClass);
    }

  /**
   * Returns the {@link RegExpGen} represented by a control character
   */
  private CharClassGen getControlEscape()
    {
    int cursorStart = cursor();
    CharClassGen escapeClass = null;

    if( peekc() == 'c')
      {
      advance(1);

      String controlChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
      int controlChar = controlChars.indexOf( Character.toUpperCase( peekc()));
      if( controlChar < 0)
        {
        throw error( String.format( "Invalid control escape character='%c'", peekc()));
        }
      advance(1);

      escapeClass = startingAt( cursorStart, new AnyOfGen( options(), (char) (0x0001 + controlChar)));
      }
    
    return escapeClass;
    }

  /**
   * Returns the {@link RegExpGen} represented by a hexadecimal character
   */
  private CharClassGen getHexCharClass()
    {
    int cursorStart = cursor();
    CharClassGen escapeClass = null;

    if( peekc() == 'x')
      {
      advance(1);

      String digits = peek(2);
      Matcher hexCharMatcher = hexCharPattern_.matcher( digits);
      if( !hexCharMatcher.matches())
        {
        throw error( String.format( "Invalid hex character='%s'", digits));
        }
      advance(2);

      escapeClass = startingAt( cursorStart, new AnyOfGen( options(), (char) Integer.parseInt( digits, 16)));
      }
    
    return escapeClass;
    }

  /**
   * Returns the {@link RegExpGen} represented by a Unicode character
   */
  private CharClassGen getUnicodeCharClass()
    {
    int cursorStart = cursor();
    CharClassGen escapeClass = null;

    if( peekc() == 'u')
      {
      advance(1);

      String digits = peek(4);
      Matcher unicodeCharMatcher = unicodeCharPattern_.matcher( digits);
      if( !unicodeCharMatcher.matches())
        {
        throw error( String.format( "Invalid Unicode character='%s'", digits));
        }
      advance(4);

      escapeClass = startingAt( cursorStart, new AnyOfGen( options(), (char) Integer.parseInt( digits, 16)));
      }
    
    return escapeClass;
    }

  /**
   * Returns the {@link RegExpGen} represented by a literal character.
   */
  private CharClassGen getLiteralChar()
    {
    int cursorStart = cursor();
    return
      Optional.of( peekc())
      .filter( c -> c != EOS)
      .map( c -> {
        CharClassGen literal = new AnyOfGen( options(), c);
        advance(1);
        return startingAt( cursorStart, literal);
        })
      .orElse( null);
    }

  /**
   * Returns the {@link RegExpGen} represented by a non-delimiter character.
   */
  private CharClassGen getPatternChar()
    {
    int cursorStart = cursor();
    String syntaxChars = "^$\\.*+?()[]{}|";
    
    return
      Optional.of( peekc())
      .filter( c -> c != EOS && syntaxChars.indexOf( c) < 0)
      .map( c -> {
        CharClassGen patternChar = new AnyOfGen( options(), c);
        advance(1);
        return startingAt( cursorStart, patternChar);
        })
      .orElse( null);
    }

  /**
   * Returns the given {@link AbstractRegExpGen} after prefacing any unanchored initial subexpressions
   * with an implicit ".*" expression.
   */
  private AbstractRegExpGen withStartGen( AbstractRegExpGen regExpGen)
    {
    AlternativeGen alternative;
    SeqGen seq;

    AbstractRegExpGen initiated = regExpGen;
    if( regExpGen instanceof AlternativeGen && (alternative = uninitiated( (AlternativeGen) regExpGen)) != null)
      {
      initiated =
        AlternativeGen.builder( options())
        .addAll(
          IterableUtils.toList( alternative.getMembers())
          .stream()
          .map( this::withStartGen)
          .collect( Collectors.toList()))
        .occurs( alternative.getOccurrences())
        .build();
      }
    else if( regExpGen instanceof SeqGen && (seq = uninitiated( (SeqGen) regExpGen)) != null)
      {
      List<AbstractRegExpGen> members = IterableUtils.toList( seq.getMembers());
      initiated =
        SeqGen.builder( options())
        .addAll(
          IntStream.range( 0, members.size())
          .mapToObj( i -> i == 0?  withStartGen( members.get(i)) : members.get(i))
          .collect( Collectors.toList()))
        .occurs( seq.getOccurrences())
        .build();
      }
    else if( !regExpGen.isAnchoredStart())
      {
      initiated = new SeqGen( options(), new AnyPrintableGen( options(), 0, null), regExpGen);
      }

    initiated.setSource( regExpGen.getSource());
    return initiated;
    }

  /**
   * Returns the given {@link AbstractRegExpGen} after appending an implicit ".*" expression to
   * any unanchored final subexpressions.
   */
  private AbstractRegExpGen withEndGen( AbstractRegExpGen regExpGen)
    {
    AlternativeGen alternative;
    SeqGen seq;

    AbstractRegExpGen terminated = regExpGen;
    if( regExpGen instanceof AlternativeGen && (alternative = unterminated( (AlternativeGen) regExpGen)) != null)
      {
      terminated =
        AlternativeGen.builder( options())
        .addAll(
          IterableUtils.toList( alternative.getMembers())
          .stream()
          .map( this::withEndGen)
          .collect( Collectors.toList()))
        .occurs( alternative.getOccurrences())
        .build();
      }
    else if( regExpGen instanceof SeqGen && (seq = unterminated( (SeqGen) regExpGen)) != null)
      {
      List<AbstractRegExpGen> members = IterableUtils.toList( seq.getMembers());
      int last = members.size() - 1;
      terminated =
        SeqGen.builder( options())
        .addAll(
          IntStream.range( 0, members.size())
          .mapToObj( i -> i == last?  withEndGen( members.get(i)) : members.get(i))
          .collect( Collectors.toList()))
        .occurs( seq.getOccurrences())
        .build();
      }
    else if( !regExpGen.isAnchoredEnd())
      {
      terminated = new SeqGen( options(), regExpGen, new AnyPrintableGen( options(), 0, null));
      }
    
    terminated.setSource( regExpGen.getSource());
    return terminated;
    }

  /**
   * Returns the given regular expression if it may have uninitiated subexpressions.
   * Otherwise, returns null.
   */
  private <T extends AbstractRegExpGen> T uninitiated( T regExpGen)
    {
    return
      regExpGen.isAnchoredStartAll() || regExpGen.getMaxOccur() > 1
      ? null
      : regExpGen;
    }

  /**
   * Returns the given regular expression if it may have unterminated subexpressions.
   * Otherwise, returns null.
   */
  private <T extends AbstractRegExpGen> T unterminated( T regExpGen)
    {
    return
      regExpGen.isAnchoredEndAll() || regExpGen.getMaxOccur() > 1
      ? null
      : regExpGen;
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
    return new IllegalArgumentException( String.format( "%s at position=%s", reason, cursor_));
    }

  /**
   * Returns the {@link GenOptions options} for {@link RegExpGen} instances created by this parser.
   */
  private GenOptions options()
    {
    return options_;
    }

  /**
   * Returns the {@link CharClasses} for this parser.
   */
  private CharClasses charClasses()
    {
    return charClasses_;
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
   * Advances past the next <CODE>N</CODE> characters. Advances to the end if
   * fewer than <CODE>N</CODE> characters remain.
   */
  private void advance( int n)
    {
    cursor_ = Math.min( cursor_ + n, chars_.length());
    }

  /**
   * Returns the current cursor position.
   */
  private int cursor()
    {
    return cursor_;
    }

  /**
   * Updates the {@link AbstractRegExpGen#getSource source} for the given generator.
   */
  private <T extends AbstractRegExpGen> T startingAt( int start, T regExpGen)
    {
    if( regExpGen != null)
      {
      regExpGen.setSource( chars_.substring( start, cursor_));
      }
    return regExpGen;
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
  private final GenOptions options_;
  private final CharClasses charClasses_;
  private int cursor_ = 0;

  private static final char EOS = (char) -1;
  private static final Pattern hexCharPattern_ = Pattern.compile( "\\p{XDigit}{2}");
  private static final Pattern unicodeCharPattern_ = Pattern.compile( "\\p{XDigit}{4}");
  }
