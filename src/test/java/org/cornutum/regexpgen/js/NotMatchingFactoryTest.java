//////////////////////////////////////////////////////////////////////////////
// 
//                    Copyright 2022, Cornutum Project
//                             www.cornutum.org
//
//////////////////////////////////////////////////////////////////////////////

package org.cornutum.regexpgen.js;

import org.junit.Test;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import java.util.List;

/**
 * Runs tests for {@link NotMatchingFactoryTest}.
 */
public class NotMatchingFactoryTest
  {
  @Test
  public void whenAlternativeOccurrences()
    {
    // Given...
    String regexp = "(cat|dog|turtle)+";
    AbstractRegExpGen generator = (AbstractRegExpGen) Provider.forEcmaScript().matchingExact( regexp);

    // When...
    List<List<CharClassGen>> sequences = NotMatchingFactory.sequencesFor( generator);

    // Then...
    assertThat( sequences, is( notNullValue()));
    }

  @Test
  public void whenLookAhead()
    {
    // Given...
    String regexp = "^\\((Copyright[-: ]+2020)?[\\\\\\d\\t]? K(?=ornutum\\))|@Copyright|@Trademark";
    AbstractRegExpGen generator = (AbstractRegExpGen) Provider.forEcmaScript().matchingExact( regexp);

    // When...
    List<List<CharClassGen>> sequences = NotMatchingFactory.sequencesFor( generator);

    // Then...
    assertThat( sequences, is( notNullValue()));
    }

  @Test
  public void whenLookBehind()
    {
    // Given...
    String regexp = "(?<=(^Gronk| cat| dog))( is an animal)$";
    AbstractRegExpGen generator = (AbstractRegExpGen) Provider.forEcmaScript().matchingExact( regexp);

    // When...
    List<List<CharClassGen>> sequences = NotMatchingFactory.sequencesFor( generator);

    // Then...
    assertThat( sequences, is( notNullValue()));
    }

  @Test
  public void whenOptionalLazy()
    {
    // Given...
    String regexp = "\\rX|^\\r??$|[^\\r]";
    AbstractRegExpGen generator = (AbstractRegExpGen) Provider.forEcmaScript().matchingExact( regexp);

    // When...
    List<List<CharClassGen>> sequences = NotMatchingFactory.sequencesFor( generator);

    // Then...
    assertThat( sequences, is( notNullValue()));
    }
  }
