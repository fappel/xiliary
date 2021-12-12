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
import static com.codeaffine.eclipse.swt.widget.scrollable.ScrolledCompositeHelper.createScrolledComposite;
import static com.codeaffine.test.util.lang.ThrowableCaptor.thrownBy;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.eclipse.swt.test.util.DisplayHelper;
import com.codeaffine.eclipse.swt.test.util.SWTIgnoreConditions.NonWindowsPlatform;
import com.codeaffine.eclipse.swt.util.ReadAndDispatch;
import com.codeaffine.test.util.junit.ConditionalIgnoreRule;
import com.codeaffine.test.util.junit.ConditionalIgnoreRule.ConditionalIgnore;

public class ScrolledCompositeAdapterTest {

  private static final int SELECTION = 50;

  @Rule public final ConditionalIgnoreRule ignoreRule = new ConditionalIgnoreRule();
  @Rule public final DisplayHelper displayHelper = new DisplayHelper();

  private ScrollableAdapterFactory adapterFactory;
  private ScrolledComposite scrolledComposite;
  private ScrolledCompositeAdapter adapter;
  private Shell shell;

  private Object layoutData;

  @Before
  public void setUp() {
    adapterFactory = new ScrollableAdapterFactory();
    shell = createShell( displayHelper );
    scrolledComposite = createScrolledComposite( shell );
    layoutData = new Object();
    scrolledComposite.setLayoutData( layoutData );
    adapter = adapterFactory.create( scrolledComposite, ScrolledCompositeAdapter.class ).get();
  }

  @Test
  @ConditionalIgnore( condition = NonWindowsPlatform.class )
  public void adapt() {
    assertThat( adapter.getChildren() ).contains( scrolledComposite );
    assertThat( adapter.getLayout() ).isInstanceOf( ScrollableLayout.class );
    assertThat( adapter.getLayoutData() ).isSameAs( layoutData );
    assertThat( adapter.getBounds() ).isEqualTo( shell.getClientArea() );
    assertThat( adapter.getScrollable() ).isSameAs( scrolledComposite );
  }

  @Test
  public void setLayout() {
    Throwable actual = thrownBy( () -> adapter.setLayout( new FillLayout() ) );

    assertThat( actual ).isInstanceOf( UnsupportedOperationException.class );
  }

  @Test
  public void getContent() {
    Control actual = adapter.getContent();

    assertThat( actual )
      .isNotNull()
      .isSameAs( scrolledComposite.getContent() );
  }

  @Test
  @ConditionalIgnore( condition = NonWindowsPlatform.class )
  public void disposalOfAdapter() {
    adapter.dispose();

    assertThat( scrolledComposite.isDisposed() ).isTrue();
  }

  @Test
  @ConditionalIgnore( condition = NonWindowsPlatform.class )
  public void disposalOfScrolledComposite() {
    scrolledComposite.dispose();

    assertThat( adapter.isDisposed() ).isTrue();
    assertThat( shell.getChildren() ).isEmpty();
  }

  @Test
  public void disposalWithDragSourceOnScrolledComposite() {
    ScrolledComposite scrolledComposite = new TestScrolledCompositeFactory().create( shell );
    ScrollableAdapterFactoryHelper.adapt( scrolledComposite, ScrolledCompositeAdapter.class );
    DragSource dragSource = new DragSource( scrolledComposite, DND.DROP_MOVE | DND.DROP_COPY );

    shell.dispose();

    assertThat( dragSource.isDisposed() ).isTrue();
  }

  @Test
  public void constructor() {
    Throwable actual = thrownBy( () -> new ScrolledCompositeAdapter() );

    assertThat( actual )
      .hasMessageContaining( "Argument cannot be null" )
      .isInstanceOf( IllegalArgumentException.class );
  }

  @Test
  @ConditionalIgnore( condition = NonWindowsPlatform.class )
  public void changeScrolledCompositeBounds() {
    openShellWithoutLayout();
    scrolledComposite = createScrolledComposite( shell, SWT.H_SCROLL | SWT.V_SCROLL, "" );
    adapter = adapterFactory.create( scrolledComposite, ScrolledCompositeAdapter.class ).get();
    waitForReconciliation();

    scrolledComposite.setBounds( expectedBounds() );
    waitForReconciliation();

    assertThat( adapter.getBounds() ).isEqualTo( expectedBounds() );
    assertThat( scrolledComposite.getBounds() ).isEqualTo( expectedBounds() );
  }

  @Test
  @ConditionalIgnore( condition = NonWindowsPlatform.class )
  public void changeScrolledCompositeBoundsWithVisibleScrollBars() {
    openShellWithoutLayout();
    scrolledComposite = createScrolledComposite( shell );
    adapter = adapterFactory.create( scrolledComposite, ScrolledCompositeAdapter.class ).get();

    scrolledComposite.setBounds( expectedBounds() );
    waitForReconciliation();

    assertThat( adapter.getBounds() ).isEqualTo( expectedBounds() );
    assertThat( scrolledComposite.getBounds() ).isNotEqualTo( expectedBounds() );
  }

  @Test
  @ConditionalIgnore( condition = NonWindowsPlatform.class )
  public void changeScrolledCompositeBoundsWithHorizontalScroll() {
    openShellWithoutLayout();

    scrolledComposite.setBounds( expectedBounds() );
    waitForReconciliation();
    scrollHorizontal( adapter, SELECTION );

    assertThat( adapter.getBounds() ).isEqualTo( expectedBounds() );
    assertThat( scrolledComposite.getBounds() ).isNotEqualTo( expectedBounds() );
    assertThat( adapter.getHorizontalBar().getSelection() ).isEqualTo( SELECTION );
  }

  @Test
  @ConditionalIgnore( condition = NonWindowsPlatform.class )
  public void changeScrolledCompositeVisibility() {
    scrolledComposite.setVisible( false );
    waitForReconciliation();

    assertThat( adapter.getVisible() ).isFalse();
  }

  @Test
  @ConditionalIgnore( condition = NonWindowsPlatform.class )
  public void changeScrolledCompositeEnablement() {
    scrolledComposite.setEnabled( false );
    waitForReconciliation();

    assertThat( adapter.getEnabled() ).isFalse();
  }

  @Test
  public void getLayoutData() {
    RowData expected = new RowData();
    scrolledComposite.setLayoutData( expected );

    Object actual = adapter.getLayoutData();

    assertThat( actual ).isSameAs( expected );
  }

  @Test
  public void setLayoutData() {
    Object expected = new Object();

    adapter.setLayoutData( expected );
    Object actual = scrolledComposite.getLayoutData();

    assertThat( actual ).isSameAs( expected );
  }

  @Test
  public void computeSize() {
    Point expected = scrolledComposite.computeSize( SWT.DEFAULT, SWT.DEFAULT, true );

    Point actual = adapter.computeSize( SWT.DEFAULT, SWT.DEFAULT, true );

    assertThat( actual ).isEqualTo( expected );
  }

  @Test
  public void adaptWithoutScrollBarStyle() {
    openShellWithoutLayout();
    scrolledComposite = new ScrolledComposite( shell, SWT.NO_SCROLL );
    Class<ScrolledCompositeAdapter> adapterType = ScrolledCompositeAdapter.class;
    Optional<ScrolledCompositeAdapter> adapter = adapterFactory.create( scrolledComposite, adapterType );

    assertThat( adapter.isPresent() ).isFalse();
  }

  private void scrollHorizontal( ScrolledCompositeAdapter adapter, int selection ) {
    int duration = 100;
    displayHelper.getDisplay().timerExec( duration, () -> adapter.getHorizontalBar().setSelection( selection ) );
    new ReadAndDispatch().spinLoop( shell, duration * 2 );
  }

  private void openShellWithoutLayout() {
    shell.setLayout( null );
    shell.open();
  }

  private void waitForReconciliation() {
    new ReadAndDispatch().spinLoop( shell, WatchDog.DELAY * 6 );
  }

  private Rectangle expectedBounds() {
    Rectangle clientArea = shell.getClientArea();
    return new Rectangle( 5, 10, clientArea.width - 10, clientArea.height - 20 );
  }
}