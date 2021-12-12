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

import java.util.stream.Stream;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.ScrollBar;

import com.codeaffine.eclipse.swt.widget.scrollbar.FlatScrollBar;

public class StyledTextVerticalSelectionListener extends SelectionAdapter {

  private final StyledText styledText;

  StyledTextVerticalSelectionListener( StyledText styledText ) {
    this.styledText = styledText;
  }

  @Override
  public void widgetSelected( SelectionEvent event ) {
    int selection = ( ( FlatScrollBar )event.widget ).getSelection();
    styledText.setTopIndex( selection / SELECTION_RASTER_SMOOTH_FACTOR );
    dispatchSelectionEventsToScrollbarListeners( selection );
  }

  private void dispatchSelectionEventsToScrollbarListeners( int selection ) {
    ScrollBar verticalBar = styledText.getVerticalBar();
    if( verticalBar != null && selection != verticalBar.getSelection() ) {
      Stream.of( verticalBar.getListeners( SWT.Selection ) )
        .forEach( listener -> listener.handleEvent( createEvent( verticalBar, selection ) ) );
    }
  }

  private Event createEvent( ScrollBar source, int detail ) {
    Event result = new Event();
    result.widget = source;
    result.type = SWT.Selection;
    result.detail = detail;
    result.display = styledText.getDisplay();
    return result;
  }
}