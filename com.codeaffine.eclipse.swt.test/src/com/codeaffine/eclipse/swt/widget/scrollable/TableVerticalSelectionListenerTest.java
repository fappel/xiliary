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

import static com.codeaffine.eclipse.swt.test.util.ShellHelper.createShell;
import static com.codeaffine.eclipse.swt.widget.scrollable.ScrollBarUpdater.SELECTION_RASTER_SMOOTH_FACTOR;
import static com.codeaffine.eclipse.swt.widget.scrollable.SelectionEventHelper.createEvent;
import static com.codeaffine.eclipse.swt.widget.scrollable.TableHelper.createTable;
import static org.assertj.core.api.Assertions.assertThat;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.eclipse.swt.test.util.DisplayHelper;
import com.codeaffine.eclipse.swt.widget.scrollbar.FlatScrollBar;

public class TableVerticalSelectionListenerTest {

  private static final int ITEM_INDEX = 2;
  @Rule
  public final DisplayHelper displayHelper = new DisplayHelper();

  @Test
  public void widgetSelected() {
    Shell shell = createShell( displayHelper );
    Table table = createTable( shell, 20 );
    FlatScrollBar scrollBar = prepareScrollBar( shell, table );
    SelectionListener listener = new TableVerticalSelectionListener( table );

    listener.widgetSelected( createEvent( scrollBar, ITEM_INDEX * SELECTION_RASTER_SMOOTH_FACTOR ) );

    assertThat( table.getTopIndex() ).isEqualTo( ITEM_INDEX );
  }

  private static FlatScrollBar prepareScrollBar( Shell shell, Table table ) {
    FlatScrollBar result = new FlatScrollBar( shell, SWT.VERTICAL );
    new TableVerticalScrollBarUpdater( table, result ).update();
    return result;
  }
}