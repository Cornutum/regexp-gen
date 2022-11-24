//////////////////////////////////////////////////////////////////////////////
// 
//                    Copyright 2022, Cornutum Project
//                             www.cornutum.org
//
//////////////////////////////////////////////////////////////////////////////

package org.cornutum.regexpgen.js;

import org.cornutum.regexpgen.RegExpGen;

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
   * regular expression.
   */
  public RegExpGen matching( String regexp)
    {
    return new Parser( regexp).parse( false); 
    }
  
  /**
   * Returns a {@link RegExpGen} that generates strings containing only characters that match the
   * given regular expression.
   */
  public RegExpGen matchingExact( String regexp)
    {
    return new Parser( regexp).parse( true); 
    }

  /**
   * Returns a {@link RegExpGen} that generates strings that do NOT match the given regular
   * expression.
   * <P/>
   * This is an optional service. Throws an {@link UnsupportedOperationException} if not implemented.
   */
  public RegExpGen notMatching( String regexp) throws UnsupportedOperationException
    {
    throw new UnsupportedOperationException();
    }
  }
