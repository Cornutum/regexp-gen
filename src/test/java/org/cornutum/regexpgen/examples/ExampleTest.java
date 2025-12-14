//////////////////////////////////////////////////////////////////////////////
// 
//                    Copyright 2020, Cornutum Project
//                             www.cornutum.org
//
//////////////////////////////////////////////////////////////////////////////

package org.cornutum.regexpgen.examples;

import org.cornutum.regexpgen.RandomGen;
import org.cornutum.regexpgen.RegExpGen;
import org.cornutum.regexpgen.js.Provider;
import org.cornutum.regexpgen.random.RandomBoundsGen;
import static org.cornutum.regexpgen.RegExpGenBuilder.generateRegExp;

import org.junit.Test;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;
import java.util.Random;

public class ExampleTest
  {
  @Test
  public void simple()
    {
    // Given a JavaScript regular expression...
    String regexp = regexp( "^Regular expressions are ((odd|hard|stupid), )+but cool!$");

    // ...and a random number generator...
    RandomGen random = getRandomGen();

    // ...create a RegExpGen instance...
    RegExpGen generator = generateRegExp( Provider.forEcmaScript()).matching( regexp);

    System.out.println( String.format( "\n%s [ %s ]:", "simple", regexp));
    for( int i = 0; i < getGeneratorCount(); i++)
      {
      // ...and generate matching strings.
      System.out.println( generator.generate( random));
      }
    }

  @Test
  public void longMatches()
    {
    // Given a JavaScript regular expression...
    String regexp = regexp( "^Regular expressions are ((odd|hard|stupid), )+but cool!$");

    // ...and a random number generator that limits the length of unbounded matches to an average of minimum + 64 ..
    RandomGen random = getRandomGen( 64);

    // ...create a RegExpGen instance...
    RegExpGen generator = generateRegExp( Provider.forEcmaScript()).matching( regexp);

    System.out.println( String.format( "\n%s [ %s ]:", "longMatches", regexp));
    for( int i = 0; i < getGeneratorCount(); i++)
      {
      // ...and generate matching strings.
      System.out.println( generator.generate( random));
      }
    }

  @Test
  public void bounded()
    {
    // Given a JavaScript regular expression...
    String regexp = regexp( "^Regular expressions are ((odd|hard|stupid), )+but cool!$");

    // ...and a random number generator...
    RandomGen random = getRandomGen();

    // ...create a RegExpGen instance...
    RegExpGen generator = generateRegExp( Provider.forEcmaScript()).matching( regexp);

    System.out.println( String.format( "\n%s [ %s ]:", "bounded", regexp));
    for( int i = 0; i < getGeneratorCount(); i++)
      {
      // ...and generate matching strings.
      System.out.println( generator.generate( random, getMinLength(), getMaxLength()));
      }
    }

  @Test
  public void substringMatches()
    {
    // Given a JavaScript regular expression...
    String regexp = regexp( "(Hello|Howdy|Allô), world!");

    // ...and a random number generator...
    RandomGen random = getRandomGen();

    // ...create a RegExpGen instance...
    RegExpGen generator = generateRegExp( Provider.forEcmaScript()).matching( regexp);

    System.out.println( String.format( "\n%s [ %s ]:", "substringMatches", regexp));
    for( int i = 0; i < getGeneratorCount(); i++)
      {
      // ...and generate matching strings.
      System.out.println( generator.generate( random));
      }
    }

  @Test
  public void withAnyPrintable()
    {
    // Given a JavaScript regular expression...
    String regexp = regexp( "<< My secret is [^\\d\\s]{8,32} >>");

    // ...and a random number generator...
    RandomGen random = getRandomGen();

    // ...create a RegExpGen instance...
    RegExpGen generator =
      generateRegExp( Provider.forEcmaScript())

      // ...matching "." with specific characters...
      .withAny( "1001 Anagrams!")
      .matching( regexp);

    System.out.println( String.format( "\n%s [ %s ]:", "withAnyPrintable", regexp));
    for( int i = 0; i < getGeneratorCount(); i++)
      {
      // ...and generate matching strings.
      System.out.println( generator.generate( random));
      }
    }

  @Test
  public void exactMatches()
    {
    // Given a JavaScript regular expression...
    String regexp = regexp( "(Hello|Howdy|Allô), world!");

    // ...and a random number generator...
    RandomGen random = getRandomGen();

    // ...create a RegExpGen instance...
    RegExpGen generator =
      generateRegExp( Provider.forEcmaScript())
      .exactly()
      .matching( regexp);

    System.out.println( String.format( "\n%s [ %s ]:", "exactMatches", regexp));
    for( int i = 0; i < getGeneratorCount(); i++)
      {
      // ...and generate matching strings.
      System.out.println( generator.generate( random));
      }
    }

  @Test
  public void notMatching()
    {
    // Given a JavaScript regular expression...
    String regexp = regexp( "(Hello|Howdy|Allô), world!");

    // ...and a random number generator...
    RandomGen random = getRandomGen();

    // ...create a RegExpGen instance...
    RegExpGen generator =
      generateRegExp( Provider.forEcmaScript()).notMatching( regexp)
      .orElseThrow( () -> new IllegalStateException( String.format( "Unable to generate string not matching '%s'", regexp)));

    System.out.println( String.format( "\n%s [ %s ]:", "notMatching", regexp));
    for( int i = 0; i < getGeneratorCount(); i++)
      {
      // ...and generate matching strings.
      System.out.println( generator.generate( random));
      }
    }

  @Test
  public void withSpaceChars()
    {
    // Given a JavaScript regular expression...
    String regexp = regexp( "^Name:\\s+Value$");

    // ...and a random number generator...
    RandomGen random = getRandomGen();

    // ...create a RegExpGen instance...
    RegExpGen generator =
      generateRegExp( Provider.forEcmaScript())

      // ...matching "\s" with specific characters...
      .withSpace( " \t")

      .exactly()
      .matching( regexp);

    System.out.println( String.format( "\n%s [ %s ]:", "withSpaceChars", regexp));
    for( int i = 0; i < getGeneratorCount(); i++)
      {
      // ...and generate matching strings.
      System.out.println( generator.generate( random));
      }
    }

  /**
   * Returns the number of matches to generate for each regular expression.
   */
  private int getGeneratorCount()
    {
    return Optional.ofNullable( StringUtils.trimToNull( System.getProperty( "count"))).map( Integer::valueOf).orElse( 3);
    }

  /**
   * Returns the minimum length for a bounded match.
   */
  private int getMinLength()
    {
    return Optional.ofNullable( StringUtils.trimToNull( System.getProperty( "min"))).map( Integer::valueOf).orElse( 40);
    }

  /**
   * Returns the maximum length for a bounded match.
   */
  private int getMaxLength()
    {
    return Optional.ofNullable( StringUtils.trimToNull( System.getProperty( "max"))).map( Integer::valueOf).orElse( 50);
    }

  /**
   * Returns the random number generator for this test.
   */
  private RandomGen getRandomGen( int lambda)
    {
    return new RandomBoundsGen( new Random( randomSeed_), lambda);
    }

  /**
   * Returns the random number generator for this test.
   */
  private RandomGen getRandomGen()
    {
    return new RandomBoundsGen( new Random( randomSeed_));
    }

  /**
   * If system property="regexp" is defined, returns its value; otherwise, returns the given string.
   */
  private String regexp( String regexp)
    {
    return Optional.ofNullable( StringUtils.trimToNull( System.getProperty( "regexp"))).orElse( regexp);
    }

  private static long randomSeed_;
  static
    {
    randomSeed_ =
      Optional.ofNullable( StringUtils.trimToNull( System.getProperty( "seed")))
      .map( Long::valueOf)
      .orElse( new Random().nextLong());
    System.out.println( String.format( "ExampleTest: randomSeed=%s", randomSeed_));
    }
  }
