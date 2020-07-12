//////////////////////////////////////////////////////////////////////////////
// 
//                    Copyright 2020, Cornutum Project
//                             www.cornutum.org
//
//////////////////////////////////////////////////////////////////////////////

package org.cornutum.regexpgen.js;

import org.cornutum.hamcrest.BaseCompositeMatcher;
import org.hamcrest.Matchers;

/**
 * A composite matcher for {@link AbstractRegExpGen} objects.
 */
public class AbstractRegExpGenMatcher extends BaseCompositeMatcher<AbstractRegExpGen>
  {
  /**
   * Creates a new AbstractRegExpGenMatcher instance.
   */
  public AbstractRegExpGenMatcher( AbstractRegExpGen expected)
    {
    super( expected);
    expectThat( valueOf( "minOccur", AbstractRegExpGen::getMinOccur).matches( Matchers::equalTo));
    expectThat( valueOf( "maxOccur", AbstractRegExpGen::getMaxOccur).matches( Matchers::equalTo));
    }
  }
