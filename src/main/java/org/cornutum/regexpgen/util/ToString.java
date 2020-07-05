//////////////////////////////////////////////////////////////////////////////
// 
//                    Copyright 2020, Cornutum Project
//                             www.cornutum.org
//
//////////////////////////////////////////////////////////////////////////////

package org.cornutum.regexpgen.util;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Defines methods to construct standard string representations.
 */
public final class ToString
  {
  private ToString()
    {
    // Static methods only
    }
  
  /**
   * Returns a standard string builder for the given object.
   */
  public static ToStringBuilder getBuilder( Object object)
    {
    return new ToStringBuilder( object, ToStringStyle.SHORT_PREFIX_STYLE);
    }
  }

