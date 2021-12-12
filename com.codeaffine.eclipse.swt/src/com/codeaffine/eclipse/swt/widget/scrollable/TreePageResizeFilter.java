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

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Tree;

/* fappel: Workaround for https://github.com/fappel/xiliary/issues/45 */
class TreePageResizeFilter {

  static final Rectangle IRRELEVANT_BOUNDS = new Rectangle( 0, 0, 0, 0 );
  static final String CLASS_NAME_PAGE_BOOK = "org.eclipse.ui.part.PageBook";

  void register( TreeAdapter treeAdapter, Tree tree ) {
    if( hasPageBookParent( treeAdapter ) ) {
      registerEventFilter( treeAdapter, tree );
    }
  }

  private static void registerEventFilter( TreeAdapter treeAdapter, Tree tree ) {
    Listener eventFilter = evt -> filterIrrelevantResizeEvents( treeAdapter, tree, evt );
    Display display = treeAdapter.getDisplay();
    display.addFilter( SWT.Resize, eventFilter );
    treeAdapter.addListener( SWT.Dispose, evt -> display.removeFilter( SWT.Resize, eventFilter ) );
  }

  private static void filterIrrelevantResizeEvents( TreeAdapter treeAdapter, Tree tree, Event evt ) {
    if( evt.widget == tree && !hasRelevantBounds( treeAdapter ) ) {
      evt.type = SWT.None;
    }
  }

  private static boolean hasPageBookParent( TreeAdapter treeAdapter ) {
    return treeAdapter.getParent().getClass().getName().equals( CLASS_NAME_PAGE_BOOK );
  }

  private static boolean hasRelevantBounds( TreeAdapter treeAdapter ) {
    return !treeAdapter.getParent().getBounds().equals( IRRELEVANT_BOUNDS );
  }
}