//////////////////////////////////////////////////////////////////////////////
// 
//                    Copyright 2020, Cornutum Project
//                             www.cornutum.org
//
//////////////////////////////////////////////////////////////////////////////

package org.cornutum.regexpgen.js;

import org.cornutum.regexpgen.RegExpGen;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.core.IsNull;

/**
 * A composite matcher for {@link AbstractRegExpGen} instances
 */
public class RegExpGenMatcher extends BaseMatcher<RegExpGen>
  {
  /**
   * Creates a new RegExpGenMatcher instance.
   */
  public RegExpGenMatcher( RegExpGen expected)
    {
    delegate_ = MatcherFactory.matcherFor( (AbstractRegExpGen) expected);
    }

  public boolean matches( Object item)
    {
    return delegate_.matches( item);
    }

  public void describeMismatch( Object item, Description mismatchDescription)
    {
    delegate_.describeMismatch( item, mismatchDescription);
    }

  public void describeTo( Description mismatchDescription)
    {
    delegate_.describeTo( mismatchDescription);
    }

  public String toString()
    {
    return delegate_.toString();
    }

  private final Matcher<?> delegate_;

  /**
   * Creates a type-specific Matcher for an {@link AbstractRegExpGen}.
   */
  private static class MatcherFactory implements RegExpGenVisitor
    {
    /**
     * Creates a new MatcherFactory instance.
     */
    public static Matcher<?> matcherFor( AbstractRegExpGen expected)
      {
      Matcher<?> matcher;
      if( expected == null)
        {
        matcher = new IsNull<AbstractRegExpGen>();
        }
      else
        {
        MatcherFactory factory = new MatcherFactory();
        expected.accept( factory);
        matcher = factory.matcher_;
        }

      return matcher;
      }
    
    public void visit( AlternativeGen regExpGen)
      {
      matcher_ = new AlternativeGenMatcher( regExpGen);
      }

    public void visit( SeqGen regExpGen)
      {
      matcher_ = new SeqGenMatcher( regExpGen);
      }

    public void visit( AnyOfGen regExpGen)
      {
      matcher_ = new CharClassGenMatcher( AnyOfGen.class, regExpGen);
      }

    public void visit( NoneOfGen regExpGen)
      {
      matcher_ = new CharClassGenMatcher( NoneOfGen.class, regExpGen);
      }

    public void visit( AnyPrintableGen regExpGen)
      {
      matcher_ = new CharClassGenMatcher( AnyPrintableGen.class, regExpGen);
      }

    private Matcher<?> matcher_;
    }

  }
