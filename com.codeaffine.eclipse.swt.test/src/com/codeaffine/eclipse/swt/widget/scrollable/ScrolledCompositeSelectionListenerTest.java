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
import static com.codeaffine.eclipse.swt.test.util.graphics.PointAssert.assertThat;
import static com.codeaffine.eclipse.swt.widget.scrollable.ScrolledCompositeHelper.createScrolledComposite;
import static java.util.Arrays.asList;

import java.util.Collection;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import com.codeaffine.eclipse.swt.test.util.DisplayHelper;
import com.codeaffine.eclipse.swt.widget.scrollbar.FlatScrollBar;

@RunWith( Parameterized.class )
public class ScrolledCompositeSelectionListenerTest {

  private static final int SELECTION = 10;

  @Rule
  public final DisplayHelper displayHelper = new DisplayHelper();

  @Parameter ( 0 )
  public int orientation;

  @Parameter ( 1 )
  public Point expectedOrigin;

  private ScrolledCompositeSelectionListener selectionListener;
  private ScrolledComposite scrolledComposite;
  private FlatScrollBar flatScrollBar;

  @Parameters
  public static Collection<Object[]> data() {
    return asList( new Object[] { SWT.HORIZONTAL, new Point( SELECTION, 0 ) },
                   new Object[] { SWT.VERTICAL, new Point( 0, SELECTION ) } );
  }

  @Before
  public void setUp() {
    Shell shell = createShell( displayHelper );
    scrolledComposite = createScrolledComposite( shell );
    flatScrollBar = new FlatScrollBar( shell, orientation );
    selectionListener = new ScrolledCompositeSelectionListener( scrolledComposite, orientation );
  }

  @Test
  public void widgetSelected() {
    flatScrollBar.setSelection( SELECTION );
    SelectionEvent event = createSelectionEvent( flatScrollBar );

    selectionListener.widgetSelected( event );
    Point actual = scrolledComposite.getOrigin();

    assertThat( actual ).isEqualTo( expectedOrigin );
  }

  private static SelectionEvent createSelectionEvent( FlatScrollBar flatScrollBar ) {
    Event event = new Event();
    event.widget = flatScrollBar;
    return new SelectionEvent( event );
  }
}