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
import static com.codeaffine.eclipse.swt.widget.scrollable.Demeanor.EXPAND_SCROLL_BAR_ON_MOUSE_OVER;
import static com.codeaffine.eclipse.swt.widget.scrollable.Demeanor.FIXED_SCROLL_BAR_BREADTH;
import static org.assertj.core.api.Assertions.assertThat;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Scrollable;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.eclipse.swt.test.util.DisplayHelper;
import com.codeaffine.eclipse.swt.widget.scrollable.context.AdaptionContext;
import com.codeaffine.eclipse.swt.widget.scrollable.context.ScrollableControl;

public class ScrollableLayoutFactoryTest {

  @Rule
  public final DisplayHelper displayHelper = new DisplayHelper();

  private ScrollableLayoutFactorySpy factorySpy;
  private AdaptionContext<Scrollable> context;
  private Scrollable scrollable;
  private Layout layout;
  private Shell shell;

  @Before
  public void setUp() {
    shell = createShell( displayHelper );
    scrollable = new Text( shell, SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL );
    factorySpy = new ScrollableLayoutFactorySpy();
    context = new AdaptionContext<>( shell, new ScrollableControl<>( scrollable ) );
    layout = factorySpy.create( context );
  }

  @Test
  public void horizontalScrollBarInitialization() {
    factorySpy.getHorizontal().notifyListeners( SWT.NONE );

    assertThat( factorySpy.getSelectionEvent().widget )
      .isNotNull()
      .isSameAs( factorySpy.getHorizontal() );
  }

  @Test
  public void verticalScrollBarInitialization() {
    factorySpy.getVertical().notifyListeners( SWT.NONE );

    assertThat( factorySpy.getSelectionEvent().widget )
      .isNotNull()
      .isSameAs( factorySpy.getVertical() );
  }

  @Test
  public void watchDogInitialization() {
    shell.dispose();

    assertThat( factorySpy.getDisposeEvent().widget )
      .isNotNull()
      .isSameAs( shell );
  }

  @Test
  public void createCompositeT() {
    assertThat( layout ).isSameAs( factorySpy.getLayout() );
  }

  @Test
  public void setIncrementButtonLength() {
    int expected = 20;

    factorySpy.setIncrementButtonLength( expected );

    assertThat( factorySpy.getHorizontal().getIncrementButtonLength() ).isEqualTo( expected );
    assertThat( factorySpy.getVertical().getIncrementButtonLength() ).isEqualTo( expected );
    assertThat( factorySpy.getIncrementButtonLength() ).isEqualTo( expected );
  }

  @Test
  public void setIncrementColor() {
    Color expected = displayHelper.getSystemColor( SWT.COLOR_BLACK );

    factorySpy.setIncrementColor( expected );

    assertThat( factorySpy.getHorizontal().getIncrementColor() ).isEqualTo( expected );
    assertThat( factorySpy.getVertical().getIncrementColor() ).isEqualTo( expected );
    assertThat( factorySpy.getIncrementColor() ).isEqualTo( expected );
  }

  @Test
  public void setPageIncrementColor() {
    Color expected = displayHelper.getSystemColor( SWT.COLOR_BLACK );

    factorySpy.setPageIncrementColor( expected );

    assertThat( factorySpy.getHorizontal().getPageIncrementColor() ).isEqualTo( expected );
    assertThat( factorySpy.getVertical().getPageIncrementColor() ).isEqualTo( expected );
    assertThat( factorySpy.getPageIncrementColor() ).isEqualTo( expected );
  }

  @Test
  public void setThumbColor() {
    Color expected = displayHelper.getSystemColor( SWT.COLOR_BLACK );

    factorySpy.setThumbColor( expected );

    assertThat( factorySpy.getHorizontal().getThumbColor() ).isEqualTo( expected );
    assertThat( factorySpy.getVertical().getThumbColor() ).isEqualTo( expected );
    assertThat( factorySpy.getThumbColor() ).isEqualTo( expected );
  }

  @Test
  public void setBackgroundColor() {
    Color expected = displayHelper.getSystemColor( SWT.COLOR_BLACK );

    factorySpy.setBackgroundColor( expected );

    assertThat( factorySpy.getHorizontal().getBackground() ).isEqualTo( expected );
    assertThat( factorySpy.getVertical().getBackground() ).isEqualTo( expected );
    assertThat( factorySpy.getHorizontal().getParent().getBackground() ).isEqualTo( expected );
    assertThat( factorySpy.getBackgroundColor() ).isEqualTo( expected );
    assertThat( factorySpy.getCornerOverlay().getBackground() ).isEqualTo( expected );
  }

  @Test
  public void getDemeanor() {
    Demeanor actual = factorySpy.getDemeanor();

    assertThat( actual ).isSameAs( EXPAND_SCROLL_BAR_ON_MOUSE_OVER );
    assertThat( actual ).isSameAs( context.get( Demeanor.class ) );
  }

  @Test
  public void setDemeanor() {
    factorySpy.setDemeanor( FIXED_SCROLL_BAR_BREADTH );

    Demeanor actual = factorySpy.getDemeanor();

    assertThat( actual ).isSameAs( FIXED_SCROLL_BAR_BREADTH );
    assertThat( actual ).isSameAs( context.get( Demeanor.class ) );
  }

  @Test
  public void getHorizontalBarAdapter() {
    Point expected = new Point( 100, 200 );
    factorySpy.getHorizontal().setSize( expected );

    ScrollBar actual = factorySpy.getHorizontalBarAdapter();

    assertThat( actual.getSize() ).isEqualTo( expected );
  }

  @Test
  public void getVerticalBarAdapter() {
    Point expected = new Point( 100, 200 );
    factorySpy.getVertical().setSize( expected );

    ScrollBar actual = factorySpy.getVerticalBarAdapter();

    assertThat( actual.getSize() ).isEqualTo( expected );
  }
}