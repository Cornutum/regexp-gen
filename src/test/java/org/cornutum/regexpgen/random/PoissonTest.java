//////////////////////////////////////////////////////////////////////////////
// 
//                    Copyright 2020, Cornutum Project
//                             www.cornutum.org
//
//////////////////////////////////////////////////////////////////////////////

package org.cornutum.regexpgen.random;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.IntStream;
import static java.util.stream.Collectors.averagingInt;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

/**
 * Runs tests for {@link Poission}.
 */
public class PoissonTest
  {
  @Test
  public void expectedLambda()
    {
    // Given...
    int lambda = lambda();
    Poisson values = new Poisson( new Random( randomSeed_), lambda);
    int count = getSampleCount();
    
    // When...
    List<Integer> generated =
      IntStream.range( 0, count)
      .mapToObj( i -> values.next())
      .collect( toList());
      
    // Then...
    double average = generated.stream().collect( averagingInt( Integer::intValue));
    assertThat( "Average", (int) Math.round( average), is( lambda));

    if( printResults())
      {
      System.out.println( String.format( "Lambda=%s", lambda));
      int max = generated.stream().max( Integer::compareTo).orElse( 0);
      Map<Integer,List<Integer>> counts = generated .stream().collect( groupingBy( Function.identity()));
      IntStream.range( 0, max)
        .forEach( i ->    {
          System.out.println(
            String.format(
              "%4s: %4s",
              i,
              Optional.ofNullable( counts.get(i)).map( List::size).orElse( 0)));
          });
      }
    }

  /**
   * Returns true if printing results.
   */
  private boolean printResults()
    {
    return Optional.ofNullable( System.getProperty( "printResults")).map( Boolean::valueOf).orElse( false);
    }

  /**
   * Returns the lambda parameter for this test.
   */
  private int lambda()
    {
    return
      Optional.ofNullable( StringUtils.trimToNull( System.getProperty( "lambda")))
      .map( Integer::valueOf)
      .orElse( 16);
    }

  /**
   * Returns the number of samples for this test.
   */
  private int getSampleCount()
    {
    return Optional.ofNullable( System.getProperty( "count")).map( Integer::valueOf).orElse( 1000);
    }

  private static long randomSeed_;
  static
    {
    randomSeed_ =
      Optional.ofNullable( StringUtils.trimToNull( System.getProperty( "seed")))
      .map( Long::valueOf)
      .orElse( new Random().nextLong());
    System.out.println( String.format( "PoissonTest: randomSeed=%s", randomSeed_));
    }
  }
