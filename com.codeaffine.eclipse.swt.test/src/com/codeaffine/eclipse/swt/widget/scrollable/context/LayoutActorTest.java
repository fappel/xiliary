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

import static com.codeaffine.eclipse.swt.test.util.graphics.PointAssert.assertThat;
import static org.mockito.Mockito.mock;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.eclipse.swt.layout.LayoutWrapper;
import com.codeaffine.eclipse.swt.test.util.DisplayHelper;
import com.codeaffine.eclipse.swt.util.OperationWithRedrawSuspension;

public class LayoutActorTest {

  private static final int MARGIN = 40;
  private static final int X_LOCATION = 10;
  private static final int Y_LOCATION = 20;

  @Rule
  public final DisplayHelper displayHelper = new DisplayHelper();

  private ScrollableControl<Composite> scrollable;
  private Composite adapter;
  private Shell shell;

  @Before
  public void setUp() {
    shell = displayHelper.createShell();
    scrollable = new ScrollableControl<>( new Composite( shell, SWT.NONE ) );
    adapter = new Composite( shell, SWT.NONE );
  }

  @Test
  public void layout() {
    LayoutActor actor = new LayoutActor( newLayoutWithMargin(), scrollable, adapter );
    scrollable.setLocation( new Point( X_LOCATION, Y_LOCATION ) );

    actor.layout( shell, true );

    assertThat( scrollable.getLocation() ).isEqualToPointOf( MARGIN, MARGIN );
    assertThat( adapter.getLocation() ).isEqualToPointOf( MARGIN - X_LOCATION, MARGIN - Y_LOCATION );
  }

  @Test
  public void ensureRedrawSuspension() {
    OperationWithRedrawSuspension operation = mock( OperationWithRedrawSuspension.class );
    LayoutActor actor = new LayoutActor( newLayoutWithMargin(), scrollable, adapter, operation );
    scrollable.setLocation( new Point( X_LOCATION, Y_LOCATION ) );

    actor.layout( shell, true );

    assertThat( scrollable.getLocation() ).isEqualToPointOf( X_LOCATION, Y_LOCATION );
    assertThat( adapter.getLocation() ).isEqualToPointOf( 0, 0 );
  }

  @Test
  public void computeSize() {
    LayoutActor actor = new LayoutActor( newLayoutWithMargin(), scrollable, adapter );
    Point expected = new LayoutWrapper( newLayoutWithMargin() ).computeSize( shell, SWT.DEFAULT, SWT.DEFAULT, true );

    Point actual = actor.computeSize( shell, SWT.DEFAULT, SWT.DEFAULT, true );

    assertThat( actual ).isEqualTo( expected );
  }

  private static FillLayout newLayoutWithMargin() {
    FillLayout result = new FillLayout();
    result.marginHeight = MARGIN;
    result.marginWidth = MARGIN;
    return result;
  }
}