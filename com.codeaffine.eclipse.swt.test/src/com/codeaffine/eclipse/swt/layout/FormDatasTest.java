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
package com.codeaffine.eclipse.swt.layout;

import static com.codeaffine.eclipse.swt.layout.FormDatas.DENOMINATOR;
import static com.codeaffine.eclipse.swt.layout.FormDatas.attach;
import static com.codeaffine.eclipse.swt.layout.FormDatasAssert.assertThat;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.eclipse.swt.test.util.DisplayHelper;

public class FormDatasTest {

  private static final int NUMERATOR = 10;
  private static final int MARGIN = 20;
  private static final int WIDTH = 300;
  private static final int HEIGHT = 200;

  @Rule
  public final DisplayHelper displayHelper = new DisplayHelper();

  private Control otherControl;
  private Composite parent;
  private Control control;

  @Before
  public void setUp() {
    parent = displayHelper.createShell();
    control = new Label( parent, SWT.NONE );
    otherControl = new Label( parent, SWT.NONE );
  }

  @Test
  public void attachFormData() {
    FormDatas actual = attach( control );

    assertThat( actual ).isAssignedTo( control );
  }

  @Test
  public void toLeft() {
    FormDatas actual = attach( control ).toLeft();

    assertThat( actual ).isAttachedToLeft( 0, 0 );
  }

  @Test
  public void toLeftWithMargin() {
    FormDatas actual = attach( control ).toLeft( MARGIN );

    assertThat( actual ).isAttachedToLeft( 0, MARGIN );
  }

  @Test
  public void toRight() {
    FormDatas actual = attach( control ).toRight();

    assertThat( actual ).isAttachedToRight( DENOMINATOR, 0 );
  }

  @Test
  public void toRightWithMargin() {
    FormDatas actual = attach( control ).toRight( MARGIN );

    assertThat( actual ).isAttachedToRight( DENOMINATOR, -MARGIN );
  }

  @Test
  public void toTop() {
    FormDatas actual = attach( control ).toTop();

    assertThat( actual ).isAttachedToTop( 0, 0 );
  }

  @Test
  public void toTopWithMargin() {
    FormDatas actual = attach( control ).toTop( MARGIN );

    assertThat( actual ).isAttachedToTop( 0, MARGIN );
  }

  @Test
  public void toBottom() {
    FormDatas actual = attach( control ).toBottom();

    assertThat( actual ).isAttachedToBottom( DENOMINATOR, 0 );
  }

  @Test
  public void toBottomWithMargin() {
    FormDatas actual = attach( control ).toBottom( MARGIN );

    assertThat( actual ).isAttachedToBottom( DENOMINATOR, -MARGIN );
  }

  @Test
  public void toLeftTo() {
    FormDatas actual = attach( control ).atLeftTo( otherControl );

    assertThat( actual ).isAttachedAtLeftTo( otherControl, 0, SWT.DEFAULT );
  }

  @Test
  public void toLeftToWithMargin() {
    int margin = 10;
    FormDatas actual = attach( control ).atLeftTo( otherControl, margin );

    assertThat( actual ).isAttachedAtLeftTo( otherControl, margin, SWT.DEFAULT );
  }

  @Test
  public void toLeftToWithAligment() {
    FormDatas actual = attach( control ).atLeftTo( otherControl, 0, SWT.CENTER );

    assertThat( actual ).isAttachedAtLeftTo( otherControl, 0, SWT.CENTER );
  }

  @Test
  public void toRightTo() {
    FormDatas actual = attach( control ).atRightTo( otherControl );

    assertThat( actual ).isAttachedAtRightTo( otherControl, 0, SWT.DEFAULT );
  }

  @Test
  public void toRightToWithMargin() {
    int margin = 10;
    FormDatas actual = attach( control ).atRightTo( otherControl, margin );

    assertThat( actual ).isAttachedAtRightTo( otherControl, -margin, SWT.DEFAULT );
  }

  @Test
  public void toRightToWithAligment() {
    FormDatas actual = attach( control ).atRightTo( otherControl, 0, SWT.CENTER );

    assertThat( actual ).isAttachedAtRightTo( otherControl, 0, SWT.CENTER );
  }

  @Test
  public void toTopTo() {
    FormDatas actual = attach( control ).atTopTo( otherControl );

    assertThat( actual ).isAttachedAtTopTo( otherControl, 0, SWT.DEFAULT );
  }

  @Test
  public void toTopToWithMargin() {
    int margin = 10;
    FormDatas actual = attach( control ).atTopTo( otherControl, margin );

    assertThat( actual ).isAttachedAtTopTo( otherControl, margin, SWT.DEFAULT );
  }

  @Test
  public void toTopToWithAligment() {
    FormDatas actual = attach( control ).atTopTo( otherControl, 0, SWT.CENTER );

    assertThat( actual ).isAttachedAtTopTo( otherControl, 0, SWT.CENTER );
  }

  @Test
  public void toBottomTo() {
    FormDatas actual = attach( control ).atBottomTo( otherControl );

    assertThat( actual ).isAttachedAtBottomTo( otherControl, 0, SWT.DEFAULT );
  }

  @Test
  public void toBottomToWithMargin() {
    int margin = 10;
    FormDatas actual = attach( control ).atBottomTo( otherControl, margin );

    assertThat( actual ).isAttachedAtBottomTo( otherControl, -margin, SWT.DEFAULT );
  }

  @Test
  public void toBottomToWithAligment() {
    FormDatas actual = attach( control ).atBottomTo( otherControl, 0, SWT.CENTER );

    assertThat( actual ).isAttachedAtBottomTo( otherControl, 0, SWT.CENTER );
  }

  @Test
  public void fromLeft() {
    int numerator = 10;
    FormDatas actual = attach( control ).fromLeft( numerator );

    assertThat( actual ).isAttachedToLeft( numerator, 0 );
  }

  @Test
  public void fromLeftWithMargin() {
    FormDatas actual = attach( control ).fromLeft( NUMERATOR, MARGIN );

    assertThat( actual ).isAttachedToLeft( NUMERATOR, MARGIN );

  }

  @Test
  public void fromRight() {
    FormDatas actual = attach( control ).fromRight( NUMERATOR );

    assertThat( actual ).isAttachedToRight( DENOMINATOR - NUMERATOR, 0 );
  }

  @Test
  public void fromRightWithMargin() {
    FormDatas actual = attach( control ).fromRight( NUMERATOR, MARGIN );

    assertThat( actual ).isAttachedToRight( DENOMINATOR - NUMERATOR, -MARGIN );
  }

  @Test
  public void fromTop() {
    FormDatas actual = attach( control ).fromTop( NUMERATOR );

    assertThat( actual ).isAttachedToTop( NUMERATOR, 0 );
  }

  @Test
  public void fromTopWithMargin() {
    FormDatas actual = attach( control ).fromTop( NUMERATOR, MARGIN );

    assertThat( actual ).isAttachedToTop( NUMERATOR, MARGIN );
  }

  @Test
  public void fromBottom() {
    FormDatas actual = attach( control ).fromBottom( NUMERATOR );

    assertThat( actual ).isAttachedToBottom( DENOMINATOR - NUMERATOR, 0 );
  }

  @Test
  public void fromBottomWithMargin() {
    FormDatas actual = attach( control ).fromBottom( NUMERATOR, MARGIN );

    assertThat( actual ).isAttachedToBottom( DENOMINATOR - NUMERATOR, -MARGIN );
  }

  @Test
  public void withWidth() {
    FormDatas actual = attach( control ).withWidth( WIDTH );

    assertThat( actual ).hasWidth( WIDTH );
  }

  @Test
  public void withHeight() {
    FormDatas actual = attach( control ).withHeight( HEIGHT );

    assertThat( actual ).hasHeight( HEIGHT );
  }
}