//////////////////////////////////////////////////////////////////////////////
// 
//                    Copyright 2020, Cornutum Project
//                             www.cornutum.org
//
//////////////////////////////////////////////////////////////////////////////

package org.cornutum.regexpgen.js;

import org.cornutum.regexpgen.Bounds;
import org.cornutum.regexpgen.RandomGen;
import org.cornutum.regexpgen.RegExpGen;
import org.cornutum.regexpgen.random.RandomBoundsGen;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import static org.cornutum.hamcrest.ExpectedFailure.expectFailure;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
import static java.util.Collections.singleton;
import static java.util.stream.Collectors.toList;

/**
 * Runs tests for {@link RegExpGen#generate(RandomGen)}.
 */
public class GenerateTest
  {
  @Test
  public void whenAlternativeOccurrences()
    {
    verifyMatchesFor( "(cat|dog|turtle)+");
    verifyMatchesFor( "^(cat|dog|turtle)+");
    verifyMatchesFor( "(cat|dog|turtle)+$");

    verifyNotMatchesFor( "(cat|dog|turtle)+");
    verifyNotMatchesFor( "^(cat|dog|turtle)+");
    verifyNotMatchesFor( "(cat|dog|turtle)+$");
    }

  @Test
  public void whenSeqOccurrences()
    {
    verifyMatchesFor( "(Digit=[\\d],){2}");
    verifyMatchesFor( "^(Digit=[\\d],){2}");
    verifyMatchesFor( "(Digit=[\\d],){2}$");

    verifyNotMatchesFor( "(Digit=[\\d],){2}");
    verifyNotMatchesFor( "^(Digit=[\\d],){2}");
    verifyNotMatchesFor( "(Digit=[\\d],){2}$");
    }

  @Test
  public void whenLookAhead()
    {
    verifyMatchesFor( "^\\((Copyright[-: ]+2020)?[\\\\\\d\\t]? K(?=ornutum\\))|@Copyright|@Trademark");

    verifyNotMatchesFor( "^\\((Copyright[-: ]+2020)?[\\\\\\d\\t]? K(?=ornutum\\))|@Copyright|@Trademark");
    }

  @Test
  public void whenLookBehind()
    {
    verifyJavaMatchesFor( "(?<=(^Gronk| cat| dog))( is an animal)$");

    verifyJavaNotMatchesFor( "(?<=(^Gronk| cat| dog))( is an animal)$");
    }

  @Test
  public void whenAlternativesMultiple()
    {
    verifyMatchesFor( "-+|\\([^\\0-]*?\\0\\)|-");

    verifyNotMatchesFor( "-+|\\([^\\0-]*?\\0\\)|-");
    }

  @Test
  public void whenEscapeCharUnicode()
    {
    verifyMatchesFor( "([^\\sA-Z\\u00D1@]\\n)+|X\\\\=.{0,2}(?=Suffix)");

    verifyNotMatchesFor( "([^ A-Z\\u00D1@]\\n)+|X\\\\=.{0,2}(?=Suffix)");
    }

  @Test
  public void whenAnchorStartEnd()
    {
    verifyMatchesFor( "^[\\b]\\\\+?\\f$");

    verifyNotMatchesFor( "^[\\b]\\\\+?\\f$");
    }

  @Test
  public void whenOptionalLazy()
    {
    verifyMatchesFor( "\\rX|^\\r??$|[^\\r]");

    verifyNotMatchesFor( "\\rX|^\\r??$|[^\\r]");
    }

  @Test
  public void whenBoundedLazy()
    {
    verifyMatchesFor( "([\\S@X-Z\\]\\v]\\(\\x6A.z\\))*?$");

    verifyNotMatchesNone( "([\\S@X-Z\\]\\v]\\(\\x6A.z\\))*?$");
    }

  @Test
  public void whenUnbounded()
    {
    verifyMatchesFor( "(^(A|B))([^\\cC]*\\\\\\t)+(?=Z$|D)|(Z|D$)");

    verifyNotMatchesFor( "(^(A|B))([^\\cC]*\\\\\\t)+(?=Z$|D)|(Z|D$)");
    }

  @Test
  public void whenBoundedMinMax()
    {
    verifyMatchesFor( "\\u028f{2,3}$");

    verifyNotMatchesFor( "\\u028f{2,3}$");
    }

  @Test
  public void whenBoundedMinMaxSame()
    {
    verifyMatchesFor( "\\?|^(?:[^\\\\-z\\W\\f-]\\)\\v+.){2,2}?(?=Suffix)");

    verifyNotMatchesFor( "\\?|^(?:[^\\\\-z\\W\\f-]\\)\\v+.){2,2}?(?=Suffix)");
    }

  @Test
  public void whenBoundedMinOnly()
    {
    verifyMatchesFor( "\\\\{3}[\\n]\\??\\0{2,}$");

    verifyNotMatchesFor( "\\\\{3}[\\n]\\??\\0{2,}$");
    }

  @Test
  public void whenBoundedMinZero()
    {
    verifyMatchesFor( "^\\{|\\cb{0,3}?(?=C|D|E$)|\\{{1,2}?");

    verifyNotMatchesFor( "^\\{|\\cb{0,3}?(?=C|D|E$)|\\{{1,2}?");
    }

  @Test
  public void whenEscapeChar()
    {
    verifyMatchesFor( "^[-\\[\\r\\--\\-\\]\\D].\\\\(?:x+yz$)?");

    verifyNotMatchesFor( "^[-\\[\\r\\--\\-\\]\\D].\\\\(?:x+yz$)?");
    }

  @Test
  public void whenEscapeCharHex()
    {
    verifyMatchesFor( "^[^\\xA2]\\xc5+\\n*?(?=\\f)|(Z$)");

    verifyNotMatchesFor( "^[^\\xA2]\\xc5+\\n*?(?=\\f)|(Z$)");
    }

  @Test
  public void whenEscapeCharWord()
    {
    verifyMatchesFor( "\\*[\\]A-Z.\\w\\t]+$");

    verifyNotMatchesFor( "\\*[\\]A-Z.\\w\\t]+$");
    }

  @Test
  public void whenDetachedAnchorStart()
    {
    verifyMatchesFor( "( fat | bad |^)cat\\.$");

    verifyNotMatchesFor( "( fat | bad |^)cat\\.$");
    }

  @Test
  public void whenDetachedAnchorEnd()
    {
    verifyMatchesFor( "^Cats are bad( company |$| pets )");

    verifyNotMatchesFor( "^Cats are bad( company |$| pets )");
    }

  @Test
  public void whenDetachedAnchorsEmpty()
    {
    verifyMatchesFor( "^$");

    verifyNotMatchesFor( "^$");
    }

  @Test
  public void whenNamedCharClass()
    {
    verifyMatchesFor( "\\w+@\\w+(\\.\\w+)*\\.(com|net|org)");

    verifyNotMatchesFor( "\\w+@\\w+(\\.\\w+)*\\.(com|net|org)");
    }

  @Test
  public void whenOptionalPrefixes()
    {
    verifyMatchesFor( ".*\\d*[\\W\\D]|\\D*\\W*@|\\S*[XYZ]*\\d*!");

    verifyNotMatchesFor( ".*\\d*[\\W\\D]|\\D*\\W*@|\\S*[XYZ]*\\d*!");
    }

  @Test
  public void whenAnyPrintables()
    {
    verifyMatchesFor( "-- Name\\[\\d\\]=\\w+ --");
    verifyMatchesFor( "-- Name\\[\\d\\]=\\w+ --", singleton( Character.valueOf( '?')));

    verifyNotMatchesFor( "-- Name\\[\\d\\]=\\w+ --");
    verifyNotMatchesFor( "-- Name\\[\\d\\]=\\w+ --", singleton( Character.valueOf( '?')));
    }

  @Test
  public void whenLengthValid()
    {
    verifyMatchesFor( "^They say( No[?!]+,)+ but I say( (Yes[?!]|What?),)+ OK\\?$", 35, 40);
    }

  @Test
  public void whenLengthInvalid()
    {
    // Given...
    String regexp = "^They say( No[?!],){1,3} but I say( (Yes[?!]|What?),){1,3} OK\\?$";
    RegExpGen generator = Provider.forEcmaScript().matching( regexp);
    RandomGen random = getRandomGen();

    expectFailure( IllegalArgumentException.class)
      .when( () -> generator.generate( random, new Bounds( 0, generator.getMinLength() - 1)))
      .then( failure -> {
        assertThat( "Failure", failure.getMessage(), is( "Length cannot be less than 32"));
        });

    expectFailure( IllegalArgumentException.class)
      .when( () -> generator.generate( random, new Bounds( generator.getMaxLength() + 1, null)))
      .then( failure -> {
        assertThat( "Failure", failure.getMessage(), is( "Length cannot be greater than 55"));
        });
    }

  @Test
  public void whenNoCharsAvailable()
    {
    // Given...
    String regexp = "\\D+";
    RegExpGen generator = Provider.forEcmaScript().matching( regexp);
    RandomGen random = getRandomGen();

    generator.getOptions().setAnyPrintableChars( singleton( Character.valueOf( '0')));

    expectFailure( IllegalStateException.class)
      .when( () -> generator.generate( random))
      .then( failure -> {
        assertThat(
          "Failure",
          failure.getMessage(),
          is( "NoneOfGen[\\D+]: "
              + "Can't generate string of valid length=5 -- no matching characters available"));
        });
    }

  private void verifyMatchesFor( String regexp)
    {
    verifyMatchesFor( regexp, (Integer) null);
    }

  private void verifyMatchesFor( String regexp, Integer lengthMax)
    {
    verifyMatchesFor( regexp, 0, lengthMax);
    }
  
  private void verifyMatchesFor( String regexp, Integer lengthMin, Integer lengthMax)
    {
    verifyMatchesFor( regexp, lengthMin, lengthMax, this::matchesJavaScript);
    }

  private void verifyJavaMatchesFor( String regexp)
    {
    verifyJavaMatchesFor( regexp, null);
    }

  private void verifyJavaMatchesFor( String regexp, Integer lengthMax)
    {
    verifyJavaMatchesFor( regexp, 0, lengthMax);
    }
  
  private void verifyJavaMatchesFor( String regexp, Integer lengthMin, Integer lengthMax)
    {
    verifyMatchesFor( regexp, lengthMin, lengthMax, this::matchesJava);
    }
  
  private void verifyMatchesFor( String regexp, Integer lengthMin, Integer lengthMax, BiFunction<String,String,Boolean> matchCheck)
    {
    verifyMatchesFor( false, regexp, null, lengthMin, lengthMax, matchCheck);
    verifyMatchesFor( true, regexp, null, lengthMin, lengthMax, matchCheck);
    }

  private void verifyMatchesFor( String regexp, Set<Character> printable)
    {
    verifyMatchesFor( false, regexp, printable, 0, null, this::matchesJavaScript);
    }

  private void verifyMatchesFor( boolean exact, String regexp, Set<Character> printable, Integer lengthMin, Integer lengthMax, BiFunction<String,String,Boolean> matchCheck)
    {
    // Given...
    RegExpGen generator =
      exact
      ? Provider.forEcmaScript().matchingExact( regexp)
      : Provider.forEcmaScript().matching( regexp);

    Optional.ofNullable( printable)
      .ifPresent( chars -> generator.getOptions().setAnyPrintableChars( chars));
    
    RandomGen random = getRandomGen();
    Bounds length = new Bounds( lengthMin, lengthMax);
    
    // When...
    List<String> matches =
      IntStream.range( 0, getGeneratorCount())
      .mapToObj( i -> {
          try
            {
              return generator.generate( random, length);
            }
          catch( Exception e)
            {
            throw new RuntimeException( String.format( "Can't generate match[%s] for length=%s, regexp=%s", i, length, regexp), e);
            }
        })
      .collect( toList());
    
    // Then...
    if( printResults())
      {
      System.out.println( String.format( "\n%s %s", exact? "Exact" : "Any", regexp));
      }
    IntStream.range( 0, matches.size())
      .forEach( i -> {
        String text = matches.get(i);

        boolean matched;
        try
          {
          matched = matchCheck.apply( text, regexp);
          }
        catch( Exception e)
          {
          throw new RuntimeException( String.format( "[%s] %s -> %s", i, regexp, text), e);
          }
        

        if( printResults())
          {
          System.out.println( String.format( "  [%s] %s %s", i, matched? "T" : "F", text));
          }
        else
          {
          assertThat( String.format( "[%s] %s -> %s", i, regexp, text), matched, is( true));
          assertThat( String.format( "[%s] %s -> %s, length", i, regexp, text), text.length(), greaterThanOrEqualTo( generator.getMinLength()));
          assertThat( String.format( "[%s] %s -> %s, length", i, regexp, text), text.length(), lessThanOrEqualTo( generator.getMaxLength()));
          assertThat( String.format( "[%s] %s -> %s, length", i, regexp, text), text.length(), lessThanOrEqualTo( length.getMaxValue()));
          }
        });
    if( printResults())
      {
      long distinct = matches.stream().distinct().count();
      int minLength = matches.stream().mapToInt( String::length).min().orElse(0);
      int maxLength = matches.stream().mapToInt( String::length).max().orElse(0);
      long avgLength = Math.round( matches.stream().mapToInt( String::length).average().orElse(0.0));
      System.out.println(
        String.format(
          "  Bounds=%s, Distinct=%s, MinLength=%s, AvgLength=%s, MaxLength=%s",
          length,
          distinct,
          minLength,
          avgLength,
          maxLength));
      }
    }

  private void verifyNotMatchesFor( String regexp)
    {
    verifyNotMatchesFor( regexp, null, this::matchesJavaScript);
    }

  private void verifyJavaNotMatchesFor( String regexp)
    {
    verifyNotMatchesFor( regexp, null, this::matchesJava);
    }

  private void verifyNotMatchesFor( String regexp, Set<Character> printable)
    {
    verifyNotMatchesFor( regexp, printable, this::matchesJavaScript);
    }

  private void verifyNotMatchesFor( String regexp, Set<Character> printable, BiFunction<String,String,Boolean> matchCheck)
    {
    // Given...
    RegExpGen generator =
      Provider.forEcmaScript().notMatching( regexp)
      .orElseThrow( () -> new RuntimeException( String.format( "Can't create not-matching generator for regexp=%s", regexp)));
    
    RandomGen random = getRandomGen();

    Optional.ofNullable( printable)
      .ifPresent( chars -> generator.getOptions().setAnyPrintableChars( chars));

    // When...
    List<String> matches =
      IntStream.range( 0, getGeneratorCount())
      .mapToObj( i -> {
          try
            {
              return generator.generate( random);
            }
          catch( Exception e)
            {
            throw new RuntimeException( String.format( "Can't generate match[%s] for regexp=%s", i, regexp), e);
            }
        })
      .collect( toList());
    
    // Then...
    if( printResults())
      {
      System.out.println( String.format( "\nNot %s", regexp));
      }
    IntStream.range( 0, matches.size())
      .forEach( i -> {
        String text = matches.get(i);

        boolean matched;
        try
          {
          matched = matchCheck.apply( text, regexp);
          }
        catch( Exception e)
          {
          throw new RuntimeException( String.format( "[%s] %s -> %s", i, regexp, text), e);
          }

        if( printResults())
          {
          System.out.println( String.format( "  [%s] %s %s", i, matched? "T" : "F", text));
          }
        else
          {
          assertThat( String.format( "[%s] %s -> %s", i, regexp, text), matched, is( false));
          }
        });
    }

  private void verifyNotMatchesNone( String regexp)
    {
    assertThat(
      String.format( "Not matching '%s'", regexp),
      Provider.forEcmaScript().notMatching( regexp),
      is( Optional.empty()));
    }

  /**
   * Returns true if the given text matches the given JavaScript regular expression.
   */
  private Boolean matchesJavaScript( String text, String regexp)
    {
    Context cx = Context.enter();
    String script = null;
    try
      {
      script = String.format( "new RegExp( '%s').test( '%s')", stringLiteral( regexp), stringLiteral( text));
      Scriptable scope = cx.initStandardObjects();
      return (Boolean) cx.evaluateString( scope, script, "GenerateTest", 1, null);
      }
    catch( Exception e)
      {
      throw new IllegalStateException( script, e);
      }
    finally
      {
      Context.exit();
      }
    }

  /**
   * Returns true if the given text matches the given Java regular expression.
   */
  private Boolean matchesJava( String text, String regexp)
    {
    return Pattern.compile( regexp).matcher( text).find();
    }

  /**
   * Returns a string containing a JavaScript string literal representing the given value.
   */
  private String stringLiteral( String value)
    {
    return
      value
      .replace( "\\", "\\\\")
      .replace( "'", "\\'")
      .replace( "\f", "\\f")
      .replace( "\n", "\\n")
      .replace( "\r", "\\r")
      .replace( "\t", "\\t")
      .replace( "\13", "\\v")
      .replace( "\0", "\\x00")
      ;
    }

  /**
   * Returns the random number generator for this test.
   */
  private RandomGen getRandomGen()
    {
    return new RandomBoundsGen( new Random( randomSeed_));
    }

  /**
   * Returns the number of matches to generate for each regular expression.
   */
  private int getGeneratorCount()
    {
    return Optional.ofNullable( System.getProperty( "count")).map( Integer::valueOf).orElse( 100);
    }

  /**
   * Returns true if printing results instead of asserting expectations.
   */
  private boolean printResults()
    {
    return Optional.ofNullable( System.getProperty( "printResults")).map( Boolean::valueOf).orElse( false);
    }

  private static long randomSeed_;
  static
    {
    randomSeed_ =
      Optional.ofNullable( StringUtils.trimToNull( System.getProperty( "seed")))
      .map( Long::valueOf)
      .orElse( new Random().nextLong());
    System.out.println( String.format( "GenerateTest: randomSeed=%s", randomSeed_));
    }
  }
