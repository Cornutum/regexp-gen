//////////////////////////////////////////////////////////////////////////////
// 
//                    Copyright 2020, Cornutum Project
//                             www.cornutum.org
//
//////////////////////////////////////////////////////////////////////////////

package org.cornutum.regexpgen.js;

import org.cornutum.hamcrest.ClassCompositeMatcher;

/**
 * A composite matcher for {@link AlternativeGen} objects.
 */
public class AlternativeGenMatcher extends ClassCompositeMatcher<AlternativeGen>
  {
  /**
   * Creates a new AlternativeGenMatcher instance.
   */
  public AlternativeGenMatcher( AlternativeGen expected)
    {
    super( AlternativeGen.class, expected);
    expectThat( valueOf( "alternatives", AlternativeGen::getMembers).matches( listsMatching( RegExpGenMatcher::new)));
    expectThat( matches( AbstractRegExpGenMatcher::new));
    }
  }
