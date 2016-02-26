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
import static com.codeaffine.eclipse.swt.test.util.SWTEventHelper.trigger;
import static com.codeaffine.eclipse.swt.test.util.ShellHelper.createShell;
import static com.codeaffine.eclipse.swt.widget.scrollable.TableHelper.createTable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.widgets.Scrollable;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.eclipse.swt.test.util.DisplayHelper;
import com.codeaffine.eclipse.swt.test.util.SWTIgnoreConditions.GtkPlatform;
import com.codeaffine.eclipse.swt.widget.scrollable.context.ScrollableControl;
import com.codeaffine.test.util.junit.ConditionalIgnoreRule;
import com.codeaffine.test.util.junit.ConditionalIgnoreRule.ConditionalIgnore;

public class StructureScrollableRedrawInsuranceTest {

  @Rule public final ConditionalIgnoreRule conditionalIgnore = new ConditionalIgnoreRule();
  @Rule public final DisplayHelper displayHelper = new DisplayHelper();

  private Shell shell;

  @Before
  public void setUp() {
    shell = createShell( displayHelper );
  }

  @Test
  public void constructionWithVirtualAndOwnerDrawnStructuredScrollable() {
    Scrollable scrollable = createTable( shell, 10, SWT.VIRTUAL );
    scrollable.addListener( SWT.MeasureItem, evt -> {} );

    newRedrawInsurance( scrollable );

    assertThat( scrollable.getVerticalBar().getListeners( SWT.Selection ) ).hasSize( 1 );
  }

  @Test
  public void constructionWithVirtualAndNativeDrawnStructuredScrollable() {
    Scrollable scrollable = createTable( shell, 10, SWT.VIRTUAL );

    newRedrawInsurance( scrollable );

    assertThat( scrollable.getVerticalBar().getListeners( SWT.Selection ) ).isEmpty();
  }

  @Test
  public void constructionWithNonVirtualAndOwnerDrawnStructuredScrollable() {
    Scrollable scrollable = createTable( shell, 10 );
    scrollable.addListener( SWT.MeasureItem, evt -> {} );

    newRedrawInsurance( scrollable );

    assertThat( scrollable.getVerticalBar().getListeners( SWT.Selection ) ).isEmpty();
  }

  @Test
  public void constructionWithVirtualAndOwnerDrawnButNonStructuredScrollable() {
    Scrollable scrollable = new Text( shell, SWT.V_SCROLL | SWT.VIRTUAL | SWT.MULTI );
    scrollable.addListener( SWT.MeasureItem, evt -> {} );

    newRedrawInsurance( scrollable );

    assertThat( scrollable.getVerticalBar().getListeners( SWT.Selection ) ).isEmpty();
  }

  @Test
  public void constructionWithVirtualAndOwnerDrawnStructuredScrollableWithoutVerticalScrollbar() {
    Scrollable scrollable = createTable( shell, 10, SWT.VIRTUAL | SWT.NO_SCROLL );
    scrollable.addListener( SWT.MeasureItem, evt -> {} );

    newRedrawInsurance( scrollable );

    assertThat( scrollable.getVerticalBar() ).isNull();
  }

  @Test
  @ConditionalIgnore( condition = GtkPlatform.class )
  public void runOnVerticalScrollBarSelectionChange() {
    Table scrollable = createTable( shell, 100, SWT.VIRTUAL );
    scrollable.addListener( SWT.MeasureItem, evt -> {} );
    StructureScrollableRedrawInsurance redrawInsurance = newRedrawInsurance( scrollable );
    PaintListener listener = mock( PaintListener.class );
    shell.open();

    flushPendingEvents();
    trigger( SWT.Selection ).on( scrollable.getVerticalBar() );
    scrollable.addPaintListener( listener );
    redrawInsurance.run();
    flushPendingEvents();

    verify( listener ).paintControl( any( PaintEvent.class ) );
  }

  @Test
  @ConditionalIgnore( condition = GtkPlatform.class )
  public void runOnVerticalScrollBarWithoutSelectionChange() {
    Table scrollable = createTable( shell, 100, SWT.VIRTUAL );
    scrollable.addListener( SWT.MeasureItem, evt -> {} );
    StructureScrollableRedrawInsurance redrawInsurance = newRedrawInsurance( scrollable );
    PaintListener listener = mock( PaintListener.class );
    shell.open();

    flushPendingEvents();
    scrollable.addPaintListener( listener );
    redrawInsurance.run();
    flushPendingEvents();

    verify( listener, never() ).paintControl( any( PaintEvent.class ) );
  }

  private static StructureScrollableRedrawInsurance newRedrawInsurance( Scrollable scrollable ) {
    return new StructureScrollableRedrawInsurance( new ScrollableControl<>( scrollable ) );
  }
}