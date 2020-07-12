//////////////////////////////////////////////////////////////////////////////
// 
//                    Copyright 2020, Cornutum Project
//                             www.cornutum.org
//
//////////////////////////////////////////////////////////////////////////////

package org.cornutum.regexpgen.js;

import org.cornutum.hamcrest.ClassCompositeMatcher;

/**
 * A composite matcher for {@link SeqGen} objects.
 */
public class SeqGenMatcher extends ClassCompositeMatcher<SeqGen>
  {
  /**
   * Creates a new SeqGenMatcher instance.
   */
  public SeqGenMatcher( SeqGen expected)
    {
    super( SeqGen.class, expected);
    expectThat( valueOf( "members", SeqGen::getMembers).matches( listsMatching( RegExpGenMatcher::new)));
    expectThat( matches( AbstractRegExpGenMatcher::new));
    }
  }
