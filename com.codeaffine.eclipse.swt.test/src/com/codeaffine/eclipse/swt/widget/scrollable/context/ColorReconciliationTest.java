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
package com.codeaffine.eclipse.swt.widget.scrollable.context;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Scrollable;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.eclipse.swt.test.util.DisplayHelper;
import com.codeaffine.eclipse.swt.widget.scrollable.ScrollbarStyle;

public class ColorReconciliationTest {

  @Rule
  public final DisplayHelper displayHelper = new DisplayHelper();

  private ScrollbarStyle scrollbarStyle;
  private Scrollable scrollable;
  private Color color;

  @Before
  public void setUp() {
    color = new Color( displayHelper.getDisplay(), 1, 2, 3 );
    scrollable = displayHelper.createShell();
    scrollbarStyle = stubScrollbarStyle( color );
  }

  @Test
  public void runIfScrollableBackgroundIsDifferentToScrollbarStyleBackground() {
    ColorReconciliation reconciliation = new ColorReconciliation( scrollbarStyle, newScrollbarControl() );

    reconciliation.run();

    verify( scrollbarStyle ).setBackgroundColor( scrollable.getBackground() );
  }

  @Test
  public void runIfScrollableBackgroundIsEqualsToScrollbarStyleBackground() {
    scrollable.setBackground( color );
    ColorReconciliation reconciliation = new ColorReconciliation( scrollbarStyle, newScrollbarControl() );

    reconciliation.run();

    verify( scrollbarStyle, never() ).setBackgroundColor( any( Color.class ) );
  }

  @Test
  public void runWithNullAsScrollbarStyle() {
    ColorReconciliation reconciliation = new ColorReconciliation( null, newScrollbarControl() );

    reconciliation.run();

    verify( scrollbarStyle, never() ).setBackgroundColor( scrollable.getBackground() );
  }

  private ScrollableControl<Scrollable> newScrollbarControl() {
    return new ScrollableControl<>( scrollable );
  }

  private static ScrollbarStyle stubScrollbarStyle( Color color ) {
    ScrollbarStyle result = mock( ScrollbarStyle.class );
    when( result.getBackgroundColor() ).thenReturn( color );
    return result;
  }
}