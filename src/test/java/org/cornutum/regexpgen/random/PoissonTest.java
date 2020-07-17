//////////////////////////////////////////////////////////////////////////////
// 
//                    Copyright 2020, Cornutum Project
//                             www.cornutum.org
//
//////////////////////////////////////////////////////////////////////////////

package org.cornutum.regexpgen.random;

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
    int lambda = 4;
    Poisson values = new Poisson( new Random(), lambda);
    int count = 1000;
    
    // When...
    List<Integer> generated =
      IntStream.range( 0, count)
      .mapToObj( i -> values.next())
      .collect( toList());
      
    // Then...
    double average = generated.stream().collect( averagingInt( Integer::intValue));
    assertThat( "Average", (int) Math.round( average), is( lambda));

    int max = generated.stream().max( Integer::compareTo).orElse( 0);
    Map<Integer,List<Integer>> counts = generated .stream().collect( groupingBy( Function.identity()));
    IntStream.range( 0, max)
      .forEach( i ->    {
        System.out.println(
          String.format( "%4s: %4s", i, Optional.ofNullable( counts.get(i)).map( List::size).orElse( 0))
          );
        });
    }

  }
