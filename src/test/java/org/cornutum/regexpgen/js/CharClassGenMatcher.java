//////////////////////////////////////////////////////////////////////////////
// 
//                    Copyright 2020, Cornutum Project
//                             www.cornutum.org
//
//////////////////////////////////////////////////////////////////////////////

package org.cornutum.regexpgen.js;

import org.cornutum.hamcrest.ClassCompositeMatcher;
import org.cornutum.hamcrest.Composites;

/**
 * A composite matcher for {@link CharClassGen} objects.
 */
public class CharClassGenMatcher extends ClassCompositeMatcher<CharClassGen>
  {
  /**
   * Creates a new CharClassGenMatcher instance.
   */
  public CharClassGenMatcher( Class<? extends CharClassGen> expectedType, CharClassGen expected)
    {
    super( expectedType, expected);
    expectThat( valueOf( "chars", CharClassGen::getChars).matches( Composites::containsElements));
    expectThat( matches( AbstractRegExpGenMatcher::new));
    }
  }
