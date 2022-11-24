//////////////////////////////////////////////////////////////////////////////
// 
//                    Copyright 2020, Cornutum Project
//                             www.cornutum.org
//
//////////////////////////////////////////////////////////////////////////////

package org.cornutum.regexpgen.js;

import org.junit.Test;
import static org.cornutum.hamcrest.Composites.*;
import static org.hamcrest.MatcherAssert.*;

import java.util.Arrays;
import java.util.List;
import static java.util.stream.Collectors.toList;

/**
 * Runs tests for RegExpGen bounds.
 */
public class BoundsTest
  {
  @Test
  public void ordering()
    {
    // Given...
    List<String> regexps =
      Arrays.asList(
        "A*BC*",
        "ABC+",
        "(A|B|C)*",
        "ABCD",
        "[\\d]{10}");
    
    // When...
    List<String> ordered =
      regexps.stream()
      .map( regexp -> Provider.forEcmaScript().matchingExact( regexp))
      .sorted()
      .map( generator -> generator.getOptions().getRegExp())
      .collect( toList());
    
    // Then...
    assertThat(
      "Ordered",
      ordered,
      listsMembers(
        "ABCD",
        "[\\d]{10}", 
        "ABC+", 
        "A*BC*",
        "(A|B|C)*"));
    }
  }
