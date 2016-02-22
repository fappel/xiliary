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

import static com.codeaffine.eclipse.swt.test.util.DisplayHelper.flushPendingEvents;
import static com.codeaffine.eclipse.swt.test.util.ShellHelper.createShell;
import static com.codeaffine.eclipse.swt.widget.scrollable.ScrollBarUpdater.SELECTION_RASTER_SMOOTH_FACTOR;
import static com.codeaffine.eclipse.swt.widget.scrollbar.FlatScrollBarAssert.assertThat;
import static org.assertj.core.api.Assertions.assertThat;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Shell;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.eclipse.swt.test.util.DisplayHelper;
import com.codeaffine.eclipse.swt.widget.scrollbar.FlatScrollBar;

public class StyledTextVerticalScrollBarUpdaterTest {

  private static final int LINE_INDEX = 2;

  @Rule public final DisplayHelper displayHelper = new DisplayHelper();

  private StyledTextVerticalScrollBarUpdater updater;
  private FlatScrollBar scrollbar;
  private StyledText styledText;
  private Shell shell;

  @Before
  public void setUp() {
    shell = createShell( displayHelper, SWT.RESIZE );
    styledText = new TestStyledTextFactory().create( shell );
    scrollbar = new FlatScrollBar( shell, SWT.VERTICAL );
    updater = new StyledTextVerticalScrollBarUpdater( styledText, scrollbar );
    shell.open();
    flushPendingEvents();
  }

  @Test
  public void update() {
    updater.update();

    assertThat( scrollbar )
      .hasIncrement( SELECTION_RASTER_SMOOTH_FACTOR )
      .hasPageIncrement( updater.calculateThumb() )
      .hasThumb( updater.calculateThumb() )
      .hasMaximum( expectedMaximum() )
      .hasMinimum( 0 )
      .hasSelection( 0 );
  }

  @Test
  public void updateWithSelection() {
    int expectedSelection = LINE_INDEX * SELECTION_RASTER_SMOOTH_FACTOR;
    styledText.setTopIndex( 2 );

    updater.update();

    assertThat( scrollbar )
      .hasIncrement( SELECTION_RASTER_SMOOTH_FACTOR )
      .hasPageIncrement( updater.calculateThumb() )
      .hasThumb( updater.calculateThumb() )
      .hasMaximum( expectedMaximum() )
      .hasMinimum( 0 )
      .hasSelection( expectedSelection );
  }

  @Test
  public void updateWithoutLines() {
    styledText.setText( "" );

    updater.update();

    assertThat( scrollbar )
      .hasIncrement( SELECTION_RASTER_SMOOTH_FACTOR )
      .hasPageIncrement( updater.calculateThumb() )
      .hasThumb( SELECTION_RASTER_SMOOTH_FACTOR )
      .hasMaximum( SELECTION_RASTER_SMOOTH_FACTOR )
      .hasMinimum( 0 )
      .hasSelection( 0 );
  }

  @Test
  public void ensureCorrectRounding() {
    int height = updater.calculateHeight();

    int actual = updater.calculateThumb();

    assertThat( actual ).isLessThan( SELECTION_RASTER_SMOOTH_FACTOR * height / styledText.getLineHeight() );
  }

  private int expectedMaximum() {
    return SELECTION_RASTER_SMOOTH_FACTOR * styledText.getLineCount();
  }
}