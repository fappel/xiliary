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
package com.codeaffine.eclipse.swt.widget.scrollable;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Scrollable;
import org.eclipse.swt.widgets.Text;

class TestScrollableFactory implements ScrollableFactory<Scrollable> {

  private Scrollable scrollable;

  @Override
  public Scrollable create( Composite parent ) {
    scrollable = new Text( parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL );
    return scrollable;
  }

  Scrollable getScrollable() {
    return scrollable;
  }
}