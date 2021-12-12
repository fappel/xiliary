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

import java.util.Optional;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Scrollable;

import com.codeaffine.eclipse.swt.widget.scrollable.ScrollableAdapterFactory.Adapter;

class ScrollableAdapterFactoryHelper {

  public static <S extends Scrollable, A extends Scrollable & Adapter<S>> Optional<A> adapt(
    S adaptable, Class<A> type )
  {
    Optional<A> result = new ScrollableAdapterFactory().create( adaptable, type );
    if( result.isPresent() ) {
      ScrollbarStyle style = ( ScrollbarStyle )result.get();
      style.setThumbColor( Display.getCurrent().getSystemColor( SWT.COLOR_RED ) );
    }
    return result;
  }
}