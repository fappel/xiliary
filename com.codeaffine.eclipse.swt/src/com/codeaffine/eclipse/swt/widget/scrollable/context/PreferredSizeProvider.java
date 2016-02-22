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
package com.codeaffine.eclipse.swt.widget.scrollable.context;

import static java.util.Arrays.stream;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Scrollable;

class PreferredSizeProvider {

  private final ScrollableControl<? extends Scrollable> scrollable;

  private boolean useLastCalculation;
  private Point preferredSize;

  PreferredSizeProvider( ScrollableControl<? extends Scrollable> scrollable ) {
    this.scrollable = scrollable;
    this.useLastCalculation = false;
    this.preferredSize = new Point( 0, 0 );
    registerPotentialPreferrredSizeChangeObserver( scrollable, evt -> useLastCalculation = false );
  }

  Point getSize() {
    if( !useLastCalculation ) {
      preferredSize = scrollable.computePreferredSize();
      useLastCalculation = !isOwnerDrawnAndVirtual();
    }
    return new Point( preferredSize.x, preferredSize.y );
  }

  boolean useLastCalculation() {
    return useLastCalculation;
  }

  private boolean isOwnerDrawnAndVirtual() {
    return scrollable.isOwnerDrawn() && ( scrollable.getControl().getStyle() & SWT.VIRTUAL ) != 0;
  }

  private static void registerPotentialPreferrredSizeChangeObserver(
    ScrollableControl<? extends Scrollable> scrollable, Listener observer )
  {
    stream( new int[] { SWT.Resize, SWT.Move, SWT.Modify, SWT.Expand, SWT.Collapse } )
      .forEach( eventType -> scrollable.addListener( eventType, observer ) );
  }
}