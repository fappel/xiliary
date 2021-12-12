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

import static com.codeaffine.eclipse.swt.widget.scrollable.ScrollBarUpdater.SELECTION_RASTER_SMOOTH_FACTOR;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Table;

import com.codeaffine.eclipse.swt.widget.scrollbar.FlatScrollBar;

class TableVerticalSelectionListener extends SelectionAdapter {

  private final Table table;

  TableVerticalSelectionListener( Table table ) {
    this.table = table;
  }

  @Override
  public void widgetSelected( SelectionEvent event ) {
    int selection = ( ( FlatScrollBar )event.widget ).getSelection();
    table.setTopIndex( selection / SELECTION_RASTER_SMOOTH_FACTOR );
  }
}