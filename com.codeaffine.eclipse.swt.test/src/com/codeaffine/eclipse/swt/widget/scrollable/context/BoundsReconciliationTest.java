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
package com.codeaffine.eclipse.swt.widget.scrollable.context;

import static com.codeaffine.eclipse.swt.test.util.SWTEventHelper.trigger;
import static com.codeaffine.eclipse.swt.test.util.ShellHelper.createShell;
import static com.codeaffine.eclipse.swt.test.util.graphics.RectangleAssert.assertThat;
import static com.codeaffine.eclipse.swt.widget.scrollable.TreeHelper.createTree;
import static com.codeaffine.test.util.lang.ThrowableCaptor.thrownBy;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.eclipse.swt.test.util.DisplayHelper;
import com.codeaffine.eclipse.swt.test.util.SWTIgnoreConditions.GtkPlatform;
import com.codeaffine.test.util.junit.ConditionalIgnoreRule;
import com.codeaffine.test.util.junit.ConditionalIgnoreRule.ConditionalIgnore;

public class BoundsReconciliationTest {

  @Rule public final ConditionalIgnoreRule conditionalIgnoreRule = new ConditionalIgnoreRule();
  @Rule public final DisplayHelper displayHelper = new DisplayHelper();

  private BoundsReconciliation reconciliation;
  private Composite adapter;
  private Tree scrollable;

  @Before
  public void setUp() {
    Shell shell = createShell( displayHelper );
    adapter = new Composite( shell, SWT.NONE );
    scrollable = createTree( adapter, 1, 1 );
    reconciliation = new BoundsReconciliation( adapter, new ScrollableControl<>( scrollable ) );
  }

  @Test
  public void run() {
    Rectangle expected = adapter.getBounds();

    reconciliation.run();

    assertThat( adapter.getBounds() ).isEqualTo( expected );
  }

  @Test
  public void runAfterScrollableBoundsHaveBeenChanged() {
    Rectangle expected = new Rectangle( 100, 200, 300, 400 );
    scrollable.setBounds( expected );

    reconciliation.run();

    assertThat( adapter.getBounds() ).isEqualTo( expected );
  }

  @Test
  public void runAfterScrollableSizeHaveBeenChanged() {
    scrollable.setSize( 50, 60 );

    reconciliation.run();

    assertThat( adapter.getBounds() ).isEqualToRectangleOf( 0, 0, 50, 60 );
  }

  @Test
  public void runAfterScrollableLocationHaveBeenChanged() {
    scrollable.setLocation( 50, 60 );

    reconciliation.run();

    assertThat( adapter.getBounds() ).isEqualToRectangleOf( 50, 60, 0, 0 );
  }

  @Test
  public void runAfterScrollableBoundsHaveChangedByTreeExpansion() {
    Rectangle expected = adapter.getBounds();
    scrollable.setBounds( new Rectangle( 100, 200, 300, 400 ) );
    trigger( SWT.Expand ).on( scrollable );

    reconciliation.run();

    assertThat( adapter.getBounds() ).isEqualTo( expected );
  }

  @Test
  public void runAfterScrollableBoundsHaveChangedByTreeCollaps() {
    Rectangle expected = adapter.getBounds();
    scrollable.setBounds( new Rectangle( 100, 200, 300, 400 ) );
    trigger( SWT.Collapse ).on( scrollable );

    reconciliation.run();

    assertThat( adapter.getBounds() ).isEqualTo( expected );
  }

  @Test
  public void subsequentRunWithoutChange() {
    Rectangle expected = adapter.getBounds();
    scrollable.setBounds( new Rectangle( 100, 200, 300, 400 ) );
    trigger( SWT.Expand ).on( scrollable );
    reconciliation.run();

    reconciliation.run();

    assertThat( adapter.getBounds() ).isEqualTo( expected );
  }

  @Test
  public void subsequentRunWithChange() {
    scrollable.setBounds( new Rectangle( 10, 20, 100, 500 ) );
    trigger( SWT.Expand ).on( scrollable );
    reconciliation.run();
    scrollable.setBounds( new Rectangle( 100, 200, 300, 400 ) );

    reconciliation.run();

    assertThat( adapter.getBounds() ).isEqualToRectangleOf( 90, 180, 300, 400 );
  }

  @Test
  public void subsequentRunWithLocationChange() {
    scrollable.setBounds( new Rectangle( 10, 20, 100, 500 ) );
    trigger( SWT.Expand ).on( scrollable );
    reconciliation.run();
    scrollable.setLocation( new Point( 100, 200 ) );

    reconciliation.run();

    assertThat( adapter.getBounds() ).isEqualToRectangleOf( 90, 180, 100, 500 );
  }

  @Test
  public void runWithInitialBorderOffsetOnOwnerDrawnScrollableWithBorder() {
    scrollable.dispose();
    scrollable = createTree( adapter, 1, 1, SWT.BORDER );
    scrollable.addListener( SWT.MeasureItem, evt -> {} );
    reconciliation = new BoundsReconciliation( adapter, new ScrollableControl<>( scrollable ) );
    Rectangle expected = adapter.getBounds();

    scrollable.setBounds( -scrollable.getBorderWidth(), -scrollable.getBorderWidth(), 0, 0 );
    reconciliation.run();

    assertThat( adapter.getBounds() ).isEqualTo( expected );
  }

  @Test
  @ConditionalIgnore( condition = GtkPlatform.class )
  public void runWithInitialBorderOffsetOnScrollableWithBorder() {
    scrollable.dispose();
    scrollable = createTree( adapter, 1, 1, SWT.BORDER );
    reconciliation = new BoundsReconciliation( adapter, new ScrollableControl<>( scrollable ) );
    Rectangle expected = adapter.getBounds();

    scrollable.setBounds( -scrollable.getBorderWidth(), -scrollable.getBorderWidth(), 0, 0 );
    reconciliation.run();

    assertThat( adapter.getBounds() ).isEqualTo( expected );
  }

  @Test
  @ConditionalIgnore( condition = GtkPlatform.class )
  public void runWithoutInitialBorderOffsetOnOwnerDrawnScrollable() {
    scrollable.addListener( SWT.MeasureItem, evt -> {} );
    Rectangle expected = new Rectangle( -2, -2, 0, 0 );

    scrollable.setBounds( expected );
    reconciliation.run();

    assertThat( adapter.getBounds() ).isEqualTo( expected );
  }

  @Test
  public void runSuspended() {
    Rectangle expected = adapter.getBounds();

    reconciliation.runSuspended( () -> scrollable.setBounds( new Rectangle( 100, 200, 300, 400 ) ) );
    reconciliation.run();

    assertThat( adapter.getBounds() ).isEqualTo( expected );
  }

  @Test
  public void runSuspendedWithProblem() {
    Rectangle expected = new Rectangle( 100, 200, 300, 400 );
    RuntimeException toBeThrown = new RuntimeException();

    Throwable problem = thrownBy( () -> reconciliation.runSuspended( stubRunnableWithProblem( toBeThrown ) ) );
    scrollable.setBounds( expected );
    reconciliation.run();

    assertThat( adapter.getBounds() ).isEqualTo( expected );
    assertThat( problem ).isSameAs( toBeThrown );
  }

  private static Runnable stubRunnableWithProblem( Throwable problem ) {
    Runnable result = mock( Runnable.class );
    doThrow( problem ).when( result ).run();
    return result;
  }
}