//////////////////////////////////////////////////////////////////////////////
// 
//                    Copyright 2025, Cornutum Project
//                             www.cornutum.org
//
//////////////////////////////////////////////////////////////////////////////

package org.cornutum.regexpgen;

import java.util.Optional;
import java.util.Set;

/**
 * Builds {@link RegExpGen} instances using a specified {@link Provider}.
 */
public class RegExpGenBuilder
  {
  /**
   * Creates a new RegExpGenBuilder instance.
   */
  public static RegExpGenBuilder generateRegExp( Provider provider)
    {
    return new RegExpGenBuilder( provider);
    }
  
  /**
   * Creates a new RegExpGenBuilder instance.
   */
  public RegExpGenBuilder( Provider provider)
    {
    provider_ = provider;
    options_ = MatchOptionsBuilder.options();
    }

  /**
   * Changes the set of characters used to generate matches for the "." expression.
   */
  public RegExpGenBuilder withAny( String chars)
    {
    options_.withAny( chars);
    return this;
    }

  /**
   * Changes the set of characters used to generate matches for the "." expression.
   */
  public RegExpGenBuilder withAny( Set<Character> chars)
    {
    options_.withAny( chars);
    return this;
    }

  /**
   * Changes the set of characters used to generate matches for the "\s" expression.
   */
  public RegExpGenBuilder withSpace( String chars)
    {
    options_.withAny( chars);
    return this;
    }

  /**
   * Changes the set of characters used to generate matches for the "\s" expression.
   */
  public RegExpGenBuilder withSpace( Set<Character> chars)
    {
    options_.withSpace( chars);
    return this;
    }

  /**
   * Changes if generating strings containing only matching characters.
   */
  public RegExpGenBuilder exactly( boolean exactMatch)
    {
    options_.exactly( exactMatch);
    return this;
    }

  /**
   * Generate strings containing only matching characters.
   */
  public RegExpGenBuilder exactly()
    {
    return exactly( true);
    }
  
  /**
   * Returns a {@link RegExpGen} that generates strings containing characters that match the given
   * regular expression, using the specified options.
   */
  public RegExpGen matching( String regexp)
    {
    return provider_.matching( regexp, options_.build());
    }
  
  /**
   * Returns a {@link RegExpGen} that generates strings that do NOT match the given regular expression,
   * using the specified options.
   */
  public Optional<RegExpGen> notMatching( String regexp)
    {
    return provider_.notMatching( regexp, options_.build());
    }

  private Provider provider_;
  private MatchOptionsBuilder options_;
  }
