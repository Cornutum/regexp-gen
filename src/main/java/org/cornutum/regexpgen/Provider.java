//////////////////////////////////////////////////////////////////////////////
// 
//                    Copyright 2022, Cornutum Project
//                             www.cornutum.org
//
//////////////////////////////////////////////////////////////////////////////

package org.cornutum.regexpgen;

/**
 * Provides instances of a {@link RegExpGen} implementation.
 */
public interface Provider
  {
  /**
   * Returns a {@link RegExpGen} that generates strings containing characters that match the given
   * regular expression.
   */
  public RegExpGen matching( String regexp);
  
  /**
   * Returns a {@link RegExpGen} that generates strings containing only characters that match the
   * given regular expression.
   */
  public RegExpGen matchingExact( String regexp);

  /**
   * Returns a {@link RegExpGen} that generates strings that do NOT match the given regular
   * expression.
   * <P/>
   * This is an optional service. Throws an {@link UnsupportedOperationException} if not implemented.
   */
  public RegExpGen notMatching( String regexp) throws UnsupportedOperationException;
  }
