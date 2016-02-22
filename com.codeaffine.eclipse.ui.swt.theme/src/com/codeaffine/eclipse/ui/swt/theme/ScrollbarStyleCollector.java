/**
 * Copyright (c) 2014 - 2016 Frank Appel
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Frank Appel - initial API and implementation
 */
package com.codeaffine.eclipse.ui.swt.theme;

import java.util.Collection;
import java.util.HashSet;
import java.util.stream.Stream;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;

import com.codeaffine.eclipse.swt.widget.scrollable.ScrollbarStyle;

class ScrollbarStyleCollector {

  Collection<ScrollbarStyle> collect() {
    return collect( Display.getCurrent().getShells() );
  }

  private static Collection<ScrollbarStyle> collect( Control[] children ) {
    Collection<ScrollbarStyle> result = new HashSet<>();
    Stream.of( children ).forEach( child -> result.addAll( collect( child ) ) );
    return result;
  }

  private static Collection<ScrollbarStyle> collect( Control child ) {
    Collection<ScrollbarStyle> result = new HashSet<>();
    if( child instanceof ScrollbarStyle ) {
      result.add( ( ScrollbarStyle )child );
    }
    if( child instanceof Composite ){
      result.addAll( collect( ( ( Composite )child ).getChildren() ) );
    }
    return result;
  }
}