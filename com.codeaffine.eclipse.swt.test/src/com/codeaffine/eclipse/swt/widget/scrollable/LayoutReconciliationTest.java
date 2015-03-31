package com.codeaffine.eclipse.swt.widget.scrollable;

import static org.assertj.core.api.Assertions.assertThat;

import org.eclipse.swt.graphics.Rectangle;
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
}