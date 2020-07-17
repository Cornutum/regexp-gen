//////////////////////////////////////////////////////////////////////////////
// 
//                    Copyright 2020, Cornutum Project
//                             www.cornutum.org
//
//////////////////////////////////////////////////////////////////////////////

package org.cornutum.regexpgen.js;

import org.apache.commons.lang3.StringUtils;
import org.cornutum.regexpgen.RandomGen;
import org.cornutum.regexpgen.RegExpGen;
import org.cornutum.regexpgen.random.RandomBoundsGen;

import org.junit.Test;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
import static java.util.stream.Collectors.toList;

/**
 * Runs tests for {@link Generate}.
 */
public class GenerateTest
  {
  @Test
  public void whenAlternative()
    {
    // Given...
    String regexp = "(cat|dog|bird)+";
    RegExpGen generator = Parser.parseRegExp( regexp);
    RandomGen random = getRandomGen();
    
    // When...
    List<String> matches =
      IntStream.range( 0, 100)
      .mapToObj( i -> {
          try
            {
            return generator.generate( random);
            }
          catch( Exception e)
            {
            throw new RuntimeException( String.format( "Can't generate match[%s]", i), e);
            }
        })
      .collect( toList());
    
    // Then...
    matches.stream()
      .forEach( text -> assertThat( String.format( "%s -> %s", regexp, text), matches( text, regexp), is( true)));
    }

  /**
   * Returns true if the given text matches the given regular expression.
   */
  private Boolean matches( String text, String regexp)
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
   * Returns a string containing a JavaScript string literal representing the given value.
   */
  private String stringLiteral( String value)
    {
    Matcher escapeMatcher = literalEscaped_.matcher( value);
    StringBuffer escaped = new StringBuffer();
    while( escapeMatcher.find())
      {
      escapeMatcher.appendReplacement( escaped, String.format( "\\\\%s", Matcher.quoteReplacement( escapeMatcher.group())));
      }
    escapeMatcher.appendTail( escaped);

    return escaped.toString();
    }

  /**
   * Returns the random number generator for this test.
   */
  private RandomGen getRandomGen()
    {
    return new RandomBoundsGen( new Random( randomSeed_));
    }

  private static long randomSeed_;
  private static final Pattern literalEscaped_ = Pattern.compile( "[\\\\']");
  
  static
    {
    randomSeed_ =
      Optional.ofNullable( StringUtils.trimToNull( System.getProperty( "seed")))
      .map( Long::valueOf)
      .orElse( new Random().nextLong());
    System.out.println( String.format( "GenerateTest: randomSeed=%s", randomSeed_));
    }
  }
