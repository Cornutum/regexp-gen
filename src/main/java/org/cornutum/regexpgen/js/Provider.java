//////////////////////////////////////////////////////////////////////////////
// 
//                    Copyright 2022, Cornutum Project
//                             www.cornutum.org
//
//////////////////////////////////////////////////////////////////////////////

package org.cornutum.regexpgen.js;

import org.cornutum.regexpgen.MatchOptions;
import org.cornutum.regexpgen.RegExpGen;
import static org.cornutum.regexpgen.MatchOptionsBuilder.options;

import java.util.Optional;

/**
 * Provides instances of a {@link RegExpGen} that generates strings matching a JavaScript <CODE>RegExp</CODE>
 * (the <A href="https://www.ecma-international.org/publications-and-standards/standards/ecma-262/#sec-patterns">ECMAScript standard</A>).
 */
public class Provider implements org.cornutum.regexpgen.Provider
  {
  /**
   * Returns a new {@link Provider}.
   */
  public static org.cornutum.regexpgen.Provider forEcmaScript()
    {
    return new Provider();
    }

  /**
   * Returns a {@link RegExpGen} that generates strings containing characters that match the given
   * regular expression, using the given options.
   */
  public RegExpGen matching( String regexp, MatchOptions options)
    {
    return new Parser( regexp, options).parse();
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
  public Optional<RegExpGen> notMatching( String regexp, MatchOptions options) throws UnsupportedOperationException
    {
    return
      NotMatchingFactory.makeFrom(
        new Parser( regexp, options( options).exactly().build())
        .parse());
    }
  }
