package com.codeaffine.eclipse.swt.widget.scrollable.context;

import static com.codeaffine.test.util.lang.ThrowableCaptor.thrownBy;
import static org.assertj.core.api.Assertions.assertThat;

import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
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

    Rectangle actual = testHelper.runReconciliation();

    assertThat( actual ).isNotEqualTo( initialAdapterBounds );
  }

  @Test
  @ConditionalIgnore( condition = GtkPlatform.class )
  public void runWithViewFormLayoutOnTopCenter() {
    Rectangle initialAdapterBounds = testHelper.setUpWithViewFormOnTopCenter();

    Rectangle actual = testHelper.runReconciliation();

    assertThat( actual ).isNotEqualTo( initialAdapterBounds );
  }

  @Test
  @ConditionalIgnore( condition = GtkPlatform.class )
  public void runWithViewFormLayoutOnTopLeft() {
    Rectangle initialAdapterBounds = testHelper.setUpWithViewFormOnTopLeft();

    Rectangle actual = testHelper.runReconciliation();

    assertThat( actual ).isNotEqualTo( initialAdapterBounds );
  }

  @Test
  @ConditionalIgnore( condition = GtkPlatform.class )
  public void runWithViewFormLayoutOnTopRight() {
    Rectangle initialAdapterBounds = testHelper.setUpWithViewFormOnTopRight();

    Rectangle actual = testHelper.runReconciliation();

    assertThat( actual ).isNotEqualTo( initialAdapterBounds );
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
}