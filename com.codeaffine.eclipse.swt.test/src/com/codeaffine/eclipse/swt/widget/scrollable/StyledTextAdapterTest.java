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
import static com.codeaffine.eclipse.swt.widget.scrollable.StyledTextAdapter.UNSUPPORTED_CREATION;
import static com.codeaffine.eclipse.swt.widget.scrollable.StyledTextHelper.createStyledText;
import static com.codeaffine.test.util.lang.ThrowableCaptor.thrownBy;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.util.Optional;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.eclipse.swt.test.util.DisplayHelper;
import com.codeaffine.eclipse.swt.test.util.SWTEventHelper;
import com.codeaffine.eclipse.swt.test.util.SWTIgnoreConditions.NonWindowsPlatform;
import com.codeaffine.eclipse.swt.util.ReadAndDispatch;
import com.codeaffine.test.util.junit.ConditionalIgnoreRule;
import com.codeaffine.test.util.junit.ConditionalIgnoreRule.ConditionalIgnore;
import com.codeaffine.test.util.lang.ThrowableCaptor;

public class StyledTextAdapterTest {

  @Rule public final ConditionalIgnoreRule ignoreRule = new ConditionalIgnoreRule();
  @Rule public final DisplayHelper displayHelper = new DisplayHelper();

  private ScrollableAdapterFactory adapterFactory;
  private StyledTextAdapter adapter;
  private StyledText styledText;
  private Object layoutData;
  private Shell shell;

  @Before
  public void setUp() {
    adapterFactory = new ScrollableAdapterFactory();
    shell = createShell( displayHelper );
    styledText = createStyledText( shell );
    layoutData = new Object();
    styledText.setLayoutData( layoutData );
    adapter = adapterFactory.create( styledText, StyledTextAdapter.class ).get();
  }

  @Test
  @ConditionalIgnore( condition = NonWindowsPlatform.class )
  public void adapt() {
    assertThat( adapter.getChildren() ).contains( styledText );
    assertThat( adapter.getLayout() ).isInstanceOf( ScrollableLayout.class );
    assertThat( adapter.getBounds() ).isEqualTo( shell.getClientArea() );
    assertThat( adapter.getLayoutData() ).isSameAs( layoutData );
    assertThat( adapter.getScrollable() ).isSameAs( styledText );
  }

  @Test
  public void setLayout() {
    Throwable actual = ThrowableCaptor.thrownBy( () -> adapter.setLayout( new FillLayout() ) );

    assertThat( actual ).isInstanceOf( UnsupportedOperationException.class );
  }

  @Test
  @ConditionalIgnore( condition = NonWindowsPlatform.class )
  public void disposalOfAdapter() {
    adapter.dispose();

    assertThat( styledText.isDisposed() ).isTrue();
  }

  @Test
  @ConditionalIgnore( condition = NonWindowsPlatform.class )
  public void disposalOfStyledText() {
    styledText.dispose();

    assertThat( adapter.isDisposed() ).isTrue();
    assertThat( shell.getChildren() ).isEmpty();
  }

  @Test
  public void disposalWithDragSourceOnStyledText() {
    StyledText styledText = new TestStyledTextFactory().create( shell );
    ScrollableAdapterFactoryHelper.adapt( styledText, StyledTextAdapter.class );
    DragSource dragSource = new DragSource( styledText, DND.DROP_MOVE | DND.DROP_COPY );

    shell.dispose();

    assertThat( dragSource.isDisposed() ).isTrue();
  }

  @Test
  public void constructor() {
    Throwable actual = thrownBy( () -> new StyledTextAdapter() );

    assertThat( actual )
      .hasMessageContaining( UNSUPPORTED_CREATION )
      .isInstanceOf( UnsupportedOperationException.class );
  }

  @Test
  @ConditionalIgnore( condition = NonWindowsPlatform.class )
  public void changeStyledTextBoundsWithVisibleScrollBars() {
    openShellWithoutLayout();
    waitForReconciliation();

    styledText.setBounds( expectedBounds() );
    waitForReconciliation();

    assertThat( adapter.getBounds() ).isEqualTo( expectedBounds() );
    assertThat( styledText.getBounds() ).isNotEqualTo( expectedBounds() );
  }

  @Test
  @ConditionalIgnore( condition = NonWindowsPlatform.class )
  public void changeStyledTextVisibility() {
    waitForReconciliation();

    styledText.setVisible( false );
    waitForReconciliation();

    assertThat( adapter.getVisible() ).isFalse();
  }

  @Test
  @ConditionalIgnore( condition = NonWindowsPlatform.class )
  public void changeStyledTextEnablement() {
    waitForReconciliation();

    styledText.setEnabled( false );
    waitForReconciliation();

    assertThat( adapter.getEnabled() ).isFalse();
  }

  @Test
  public void adaptWithoutScrollBarStyle() {
    openShellWithoutLayout();
    styledText = new StyledText( shell, SWT.NONE );
    Optional<StyledTextAdapter> adapter = adapterFactory.create( styledText, StyledTextAdapter.class );

    assertThat( adapter.isPresent() ).isFalse();
  }

  @Test
  public void getLayoutData() {
    RowData expected = new RowData();
    styledText.setLayoutData( expected );

    Object actual = adapter.getLayoutData();

    assertThat( actual ).isSameAs( expected );
  }

  @Test
  public void setLayoutData() {
    Object expected = new Object();

    adapter.setLayoutData( expected );
    Object actual = styledText.getLayoutData();

    assertThat( actual ).isSameAs( expected );
  }

  @Test
  public void computeSize() {
    Point expected = styledText.computeSize( SWT.DEFAULT, SWT.DEFAULT, true );

    Point actual = adapter.computeSize( SWT.DEFAULT, SWT.DEFAULT, true );

    assertThat( actual ).isEqualTo( expected );
  }

  @Test
  @ConditionalIgnore( condition = NonWindowsPlatform.class )
  // Workaround for https://github.com/fappel/xiliary/issues/63
  public void avoidMouseWheelEventPropagationToFlatScrollBars() {
    Listener listener = mock( Listener.class );
    adapter.addListener( SWT.MouseWheel, listener );

    SWTEventHelper.trigger( SWT.MouseWheel ).on( adapter );

    verify( listener, never() ).handleEvent( any( Event.class ) );
  }

  @Test
  public void hideNativeHorizontalScrollBarEvenIfResetToVisible() {
    openShellWithoutLayout();
    waitForReconciliation();

    assertThat( styledText.getHorizontalBar().isVisible() ).isFalse();

    styledText.getHorizontalBar().setVisible( true );
    waitForReconciliation();

    assertThat( styledText.getHorizontalBar().isVisible() ).isFalse();
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