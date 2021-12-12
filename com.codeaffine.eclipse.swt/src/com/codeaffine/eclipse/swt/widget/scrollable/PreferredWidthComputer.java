/**
 * Copyright (c) 2014 - 2022 Frank Appel
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Frank Appel - initial API and implementation
 */
package com.codeaffine.eclipse.swt.widget.scrollable;

import com.codeaffine.eclipse.swt.widget.scrollable.context.AdaptionContext;

class PreferredWidthComputer {

  int compute( AdaptionContext<?> context ) {
    int result = context.getPreferredSize().x;
    if( context.isVerticalBarVisible() ) {
      result += context.getVerticalBarOffset();
    }
    return result + context.getOffset() * 2;
  }
}