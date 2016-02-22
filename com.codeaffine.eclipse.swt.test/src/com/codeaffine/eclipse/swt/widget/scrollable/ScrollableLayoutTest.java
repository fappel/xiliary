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

import static com.codeaffine.eclipse.swt.test.util.ShellHelper.createShell;
import static com.codeaffine.eclipse.swt.widget.scrollable.ReconciliationHelper.stubReconciliation;
import static com.codeaffine.eclipse.swt.widget.scrollable.TreeHelper.createTree;
import static com.codeaffine.eclipse.swt.widget.scrollable.TreeHelper.expandTopBranch;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Scrollable;
import org.eclipse.swt.widgets.Tree;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;

import com.codeaffine.eclipse.swt.test.util.DisplayHelper;
import com.codeaffine.eclipse.swt.widget.scrollable.context.AdaptionContext;
import com.codeaffine.eclipse.swt.widget.scrollable.context.Reconciliation;
import com.codeaffine.eclipse.swt.widget.scrollable.context.ScrollableControl;

@SuppressWarnings( "rawtypes" )
public class ScrollableLayoutTest {

  @Rule
  public final DisplayHelper displayHelper = new DisplayHelper();

  private StructuredScrollableLayouter scrollableLayouter;
  private OverlayLayouter overlayLayouter;
  private Reconciliation reconciliation;
  private AdaptionContext<?> context;
  private ScrollableLayout layout;

  @Before
  public void setUp() {
    context = createContext();
    overlayLayouter = mock( OverlayLayouter.class );
    scrollableLayouter = mock( StructuredScrollableLayouter.class );
    reconciliation = stubReconciliation();
    layout = new ScrollableLayout( context, overlayLayouter, scrollableLayouter, reconciliation );
  }

  @Test
  public void layout() {
    ArgumentCaptor<AdaptionContext> captor = forClass( AdaptionContext.class );

    layout.layout( null, true );

    InOrder order = order();
    order.verify( reconciliation ).runWhileSuspended( any( Runnable.class ) );
    order.verify( scrollableLayouter ).layout( captor.capture() );
    order.verify( overlayLayouter ).layout( captor.capture() );
    order.verifyNoMoreInteractions();
    assertContextGetsUpdated( captor );
  }

  @Test
  public void ensureReconciliationClamp() {
    doNothing().when( reconciliation ).runWhileSuspended( any( Runnable.class ) );
    expandTopBranch( ( Tree )context.getScrollable().getControl() );
    context.updatePreferredSize();

    layout.layout( null, true );

    InOrder order = order();
    order.verify( reconciliation ).runWhileSuspended( any( Runnable.class ) );
    order.verify( overlayLayouter, never() ).layout( any( AdaptionContext.class ) );
    order.verify( scrollableLayouter, never() ).layout( any( AdaptionContext.class ) );
    order.verifyNoMoreInteractions();
  }

  @Test
  public void computeSize() {
    Point actual = layout.computeSize( null, SWT.DEFAULT, SWT.DEFAULT, true );

    assertThat( actual ).isEqualTo( context.getScrollable().computeSize( SWT.DEFAULT, SWT.DEFAULT, true ) );
  }

  private AdaptionContext<Scrollable> createContext() {
    Scrollable scrollable = createTree( createShell( displayHelper ), 6, 4 );
    ScrollableControl<Scrollable> scrollableControl = new ScrollableControl<>( scrollable );
    return new AdaptionContext<>( scrollable.getParent(), scrollableControl );
  }

  private InOrder order() {
    return inOrder( reconciliation, overlayLayouter, scrollableLayouter );
  }

  private void assertContextGetsUpdated( ArgumentCaptor<AdaptionContext> captor ) {
    List<AdaptionContext> captured = captor.getAllValues();
    captured.forEach( actual -> assertThat( actual ).isNotSameAs( context ) );
    captured.forEach( actual -> assertThat( filterForActual( captured, actual ) ).hasSize( 1 ) );
  }

  private static List<AdaptionContext> filterForActual( List<AdaptionContext> captured, AdaptionContext actual ) {
    return captured.stream().filter( current -> current == actual ).collect( toList() );
  }
}