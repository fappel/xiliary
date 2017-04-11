/**
 * Copyright (c) 2014 - 2017 Frank Appel
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
import static com.codeaffine.eclipse.swt.widget.scrollable.StyledTextHelper.createStyledText;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Shell;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import com.codeaffine.eclipse.swt.test.util.DisplayHelper;
import com.codeaffine.eclipse.swt.widget.scrollbar.FlatScrollBar;

public class StyledTextVerticalSelectionListenerTest {

  private static final int LINE_INDEX = 2;

  @Rule
  public final DisplayHelper displayHelper = new DisplayHelper();

  @Test
  public void widgetSelected() {
    Shell shell = createShell( displayHelper );
    StyledText styledText = createStyledText( shell );
    SelectionListener selectionListener = mock( SelectionListener.class );
    styledText.getVerticalBar().addSelectionListener( selectionListener );
    FlatScrollBar scrollBar = prepareScrollBar( shell, styledText );
    SelectionListener listener = new StyledTextVerticalSelectionListener( styledText );

    listener.widgetSelected( createEvent( scrollBar, LINE_INDEX * SELECTION_RASTER_SMOOTH_FACTOR ) );

    ArgumentCaptor<SelectionEvent> captor = forClass( SelectionEvent.class );
    assertThat( styledText.getTopIndex() ).isEqualTo( LINE_INDEX );
    verify( selectionListener ).widgetSelected( captor.capture() );
    assertThat( captor.getValue().detail ).isEqualTo( LINE_INDEX * SELECTION_RASTER_SMOOTH_FACTOR );
  }

  private static FlatScrollBar prepareScrollBar( Shell shell, StyledText styledText ) {
    FlatScrollBar result = new FlatScrollBar( shell, SWT.VERTICAL );
    new StyledTextVerticalScrollBarUpdater( styledText, result ).update();
    return result;
  }
}