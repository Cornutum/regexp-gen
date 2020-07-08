//////////////////////////////////////////////////////////////////////////////
// 
//                    Copyright 2020, Cornutum Project
//                             www.cornutum.org
//
//////////////////////////////////////////////////////////////////////////////

package org.cornutum.regexpgen.js;

/**
 * Implements the Visitor pattern for {@link org.cornutum.regexpgen.RegExpGen} implementations.
 */
public interface RegExpGenVisitor
  {
  public void visit( AlternativeGen regExpGen);
  public void visit( SeqGen regExpGen);
  public void visit( AnyOfGen regExpGen);
  public void visit( NoneOfGen regExpGen);
  public void visit( AnyPrintableGen regExpGen);
  }
