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

import static com.codeaffine.eclipse.swt.test.util.graphics.PointAssert.assertThat;
import static com.codeaffine.test.util.lang.ThrowableCaptor.thrownBy;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Scrollable;
import org.eclipse.swt.widgets.Shell;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.eclipse.swt.test.util.DisplayHelper;
import com.codeaffine.eclipse.swt.test.util.SWTIgnoreConditions.GtkPlatform;
import com.codeaffine.test.util.junit.ConditionalIgnoreRule;
import com.codeaffine.test.util.junit.ConditionalIgnoreRule.ConditionalIgnore;

public class LayoutReconciliationTest {

  private static final Point ZERO_POINT = new Point( 0, 0 );

  @Rule public final ConditionalIgnoreRule ignoreRule = new ConditionalIgnoreRule();
  @Rule public final DisplayHelper displayHelper = new DisplayHelper();

  private LayoutReconciliationHelper testHelper;

  @Before
  public void setUp() {
    testHelper = new LayoutReconciliationHelper( displayHelper );
  }

  @Test
  @ConditionalIgnore( condition = GtkPlatform.class )
  public void run() {
    Rectangle expected = testHelper.setUpWithFillLayout();

    Rectangle actual = testHelper.runReconciliation();

    assertThat( actual ).isEqualTo( expected );
  }

  @Test
  @ConditionalIgnore( condition = GtkPlatform.class )
  public void runWithStackLayout() {
    Rectangle initialAdapterBounds = testHelper.setUpWithStackLayout();

    Rectangle actual = testHelper.runReconciliation();

    assertThat( actual ).isNotEqualTo( initialAdapterBounds );
  }

  @Test
  @ConditionalIgnore( condition = GtkPlatform.class )
  public void runWithStackLayoutWithNonAdapterTopControl() {
    Rectangle initialAdapterBounds = testHelper.setUpWithStackLayoutWithNonAdapterTopControl();

    Rectangle actual = testHelper.runReconciliation();

    assertThat( actual ).isEqualTo( initialAdapterBounds );
  }

  @Test
  @ConditionalIgnore( condition = GtkPlatform.class )
  public void runWithViewFormLayoutOnContent() {
    Rectangle initialAdapterBounds = testHelper.setUpWithViewFormOnContent();
    List<Point> sizeChanges = registerAdapterSizeChangeRecorder();

    Rectangle actual = testHelper.runReconciliation();

    assertThat( sizeChanges )
      .hasSize( 3 );
    assertThat( sizeChanges.get( 1 ) )
      .isEqualTo( ZERO_POINT );
    assertThat( actual )
      .isNotEqualTo( initialAdapterBounds )
      .isNotEqualTo( ZERO_POINT );
  }

  @Test
  @ConditionalIgnore( condition = GtkPlatform.class )
  public void runWithViewFormLayoutOnTopCenter() {
    Rectangle initialAdapterBounds = testHelper.setUpWithViewFormOnTopCenter();
    List<Point> sizeChanges = registerAdapterSizeChangeRecorder();

    Rectangle actual = testHelper.runReconciliation();

    assertThat( sizeChanges )
      .hasSize( 3 );
    assertThat( sizeChanges.get( 1 ) )
      .isEqualTo( ZERO_POINT );
    assertThat( actual )
      .isNotEqualTo( initialAdapterBounds )
      .isNotEqualTo( ZERO_POINT );
  }

  @Test
  @ConditionalIgnore( condition = GtkPlatform.class )
  public void runWithViewFormLayoutOnTopLeft() {
    Rectangle initialAdapterBounds = testHelper.setUpWithViewFormOnTopLeft();
    List<Point> sizeChanges = registerAdapterSizeChangeRecorder();

    Rectangle actual = testHelper.runReconciliation();

    assertThat( sizeChanges )
      .hasSize( 4 );
    assertThat( sizeChanges.get( 2 ) )
      .isEqualTo( ZERO_POINT );
    assertThat( actual )
      .isNotEqualTo( initialAdapterBounds )
      .isNotEqualTo( ZERO_POINT );
  }

  @Test
  @ConditionalIgnore( condition = GtkPlatform.class )
  public void runWithViewFormLayoutOnTopRight() {
    Rectangle initialAdapterBounds = testHelper.setUpWithViewFormOnTopRight();
    List<Point> sizeChanges = registerAdapterSizeChangeRecorder();

    Rectangle actual = testHelper.runReconciliation();

    assertThat( sizeChanges )
      .hasSize( 3 );
    assertThat( sizeChanges.get( 1 ) )
      .isEqualTo( ZERO_POINT );
    assertThat( actual )
      .isNotEqualTo( initialAdapterBounds )
      .isNotEqualTo( ZERO_POINT );
  }

  @Test
  @ConditionalIgnore( condition = GtkPlatform.class )
  public void runWithPageBook() {
    Rectangle initialAdapterBounds = testHelper.setUpWithPageBook();

    Rectangle actual = testHelper.runReconciliation();

    assertThat( actual ).isNotEqualTo( initialAdapterBounds );
    assertThat( testHelper.getAdapter().isVisible() ).isTrue();
  }

  @Test
  @ConditionalIgnore( condition = GtkPlatform.class )
  public void runWithPageBookWithNonAdapterPage() {
    Rectangle initialAdapterBounds = testHelper.setUpWithPageBookWithNonAdapterPage();

    Rectangle actual = testHelper.runReconciliation();

    assertThat( actual ).isNotEqualTo( initialAdapterBounds );
    assertThat( testHelper.getAdapter().isVisible() ).isFalse();
  }

  @Test
  @ConditionalIgnore( condition = GtkPlatform.class )
  public void runWithSashForm() {
    Rectangle initialAdapterBounds = testHelper.setUpWithSashForm();

    Rectangle actual = testHelper.runReconciliation();

    assertThat( actual ).isNotEqualTo( initialAdapterBounds );
  }

  @Test
  @ConditionalIgnore( condition = GtkPlatform.class )
  public void runWithSashFormIfScrollableHasWrongParent() {
    testHelper.setUpWithSashForm();
    testHelper.reparentScrollableToAdapterParent();

    testHelper.runReconciliation();

    assertThat( testHelper.getParent().getChildren() ).doesNotContain( testHelper.getScrollable() );
    assertThat( testHelper.getScrollable().getParent() ).isEqualTo( testHelper.getAdapter().getParent() );
  }

  @Test
  @ConditionalIgnore( condition = GtkPlatform.class )
  public void runWithSashFormAndMaxControlSet() {
    testHelper.setUpWithSashForm();
    testHelper.getParent( SashForm.class ).setMaximizedControl( testHelper.getScrollable() );

    testHelper.runReconciliation();
    Control actual = testHelper.getParent( SashForm.class ).getMaximizedControl();

    assertThat( actual ).isSameAs( testHelper.getAdapter() );
  }

  @Test
  @ConditionalIgnore( condition = GtkPlatform.class )
  public void runWithCTabFolder() {
    Rectangle initialAdapterBounds = testHelper.setUpWithCTabFolder();

    Rectangle actual = testHelper.runReconciliation();

    assertThat( actual ).isNotEqualTo( initialAdapterBounds );
  }

  @Test
  public void registerSourceViewerRulerLayoutActor() {
    Shell shell = displayHelper.createShell();
    shell.setLayout( new SourceViewer.RulerLayout() );
    Composite adapter = new Composite( shell, SWT.NONE );
    Composite scrollable = new Composite( adapter, SWT.NONE );

    new LayoutReconciliation( adapter, new ScrollableControl<Scrollable>( scrollable ) );

    assertThat( shell.getLayout() ).isInstanceOf( LayoutActor.class );
  }

  @Test
  public void registerSourceViewerRulerLayoutActorIfAdapterParentHasNoLayout() {
    Shell shell = displayHelper.createShell();
    Composite adapter = new Composite( shell, SWT.NONE );
    Composite scrollable = new Composite( adapter, SWT.NONE );

    Throwable actual = thrownBy( () ->  {
      new LayoutReconciliation( adapter, new ScrollableControl<Scrollable>( scrollable ) );
    } );

    assertThat( actual ).isNull();
  }

  @Test
  public void registerSourceViewerRulerLayoutActorIfAdapterHasNoParent() {
    Shell shell = displayHelper.createShell();
    Composite scrollable = new Composite( shell, SWT.NONE );

    Throwable actual = thrownBy( () ->  {
      new LayoutReconciliation( shell, new ScrollableControl<Scrollable>( scrollable ) );
    } );

    assertThat( actual ).isNull();
  }

  private List<Point> registerAdapterSizeChangeRecorder() {
    List<Point> result = new ArrayList<>();
    Listener listener = evt -> result.add( testHelper.getAdapter().getSize() );
    testHelper.getAdapter().addListener( SWT.Resize, listener );
    return result;
  }
}