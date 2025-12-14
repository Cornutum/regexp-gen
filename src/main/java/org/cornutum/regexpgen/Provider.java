//////////////////////////////////////////////////////////////////////////////
// 
//                    Copyright 2022, Cornutum Project
//                             www.cornutum.org
//
//////////////////////////////////////////////////////////////////////////////

package org.cornutum.regexpgen;

import static org.cornutum.regexpgen.MatchOptionsBuilder.options;

import java.util.Optional;

/**
 * Provides instances of a {@link RegExpGen} implementation.
 */
public interface Provider
  {
  /**
   * Returns a {@link RegExpGen} that generates strings containing characters that match the given
   * regular expression.
   *
   * @deprecated Replace using {@link RegExpGenBuilder} with default options
   */
  @Deprecated
  public default RegExpGen matching( String regexp)
    {
    return matching( regexp, options().build());
    }

  /**
   * Returns a {@link RegExpGen} that generates strings containing characters that match the given
   * regular expression, using the given options.
   */
  public RegExpGen matching( String regexp, MatchOptions options);

  /**
   * Returns a {@link RegExpGen} that generates strings containing only characters that match the
   * given regular expression.
   *
   * @deprecated Replace using {@link RegExpGenBuilder} with <CODE>exactly()</CODE>
   */
  @Deprecated
  public default RegExpGen matchingExact( String regexp)
    {
    return matching( regexp, options().exactly().build());
    }

  /**
   * Returns a {@link RegExpGen} that generates strings that do NOT match the given regular
   * expression.
   * <P/>
   * For some regular expressions, no result is possible. For example, there is no string that
   * does not match ".*". For such expressions, this method should return {@link Optional#empty}.
   * <P/>
   * This is an optional service. Throws an {@link UnsupportedOperationException} if not implemented.
   *
   * @deprecated Replace using {@link RegExpGenBuilder#notMatching}
   */
  @Deprecated
  public default Optional<RegExpGen> notMatching( String regexp) throws UnsupportedOperationException
    {
    return notMatching( regexp, options().build());
    }

  /**
   * Returns a {@link RegExpGen} that generates strings that do NOT match the given regular
   * expression, using the given options.
   * <P/>
   * For some regular expressions, no result is possible. For example, there is no string that
   * does not match ".*". For such expressions, this method should return {@link Optional#empty}.
   * <P/>
   * This is an optional service. Throws an {@link UnsupportedOperationException} if not implemented.
   */
  public Optional<RegExpGen> notMatching( String regexp, MatchOptions options) throws UnsupportedOperationException;
  }
