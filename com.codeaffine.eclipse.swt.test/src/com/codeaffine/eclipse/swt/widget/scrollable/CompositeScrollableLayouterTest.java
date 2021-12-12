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

import static com.codeaffine.eclipse.swt.test.util.ShellHelper.createShellWithoutLayout;
import static com.codeaffine.eclipse.swt.test.util.graphics.RectangleAssert.assertThat;
import static com.codeaffine.eclipse.swt.testhelper.LoremIpsum.PARAGRAPHS;
import static com.codeaffine.eclipse.swt.widget.scrollbar.FlatScrollBar.BAR_BREADTH;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Shell;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.eclipse.swt.test.util.DisplayHelper;
import com.codeaffine.eclipse.swt.test.util.SWTIgnoreConditions.NonWindowsPlatform;
import com.codeaffine.eclipse.swt.widget.scrollable.context.AdaptionContext;
import com.codeaffine.eclipse.swt.widget.scrollable.context.ScrollableControl;
import com.codeaffine.test.util.junit.ConditionalIgnoreRule;
import com.codeaffine.test.util.junit.ConditionalIgnoreRule.ConditionalIgnore;

public class CompositeScrollableLayouterTest {

  private static final int OFF_SET = 1;

  @Rule public final ConditionalIgnoreRule conditionalIgnoreRule = new ConditionalIgnoreRule();
  @Rule public final DisplayHelper displayHelper = new DisplayHelper();

  private ScrollableLayouter scrollableLayouter;
  private AdaptionContext<StyledText> context;
  private StyledText styledText;
  private Shell adapter;

  @Before
  public void setUp() {
    adapter = createShellWithoutLayout( displayHelper, SWT.NONE );
    styledText = new StyledText( adapter, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL );
    ScrollableControl<StyledText> scrollableControl = new ScrollableControl<StyledText>( styledText );
    context = new AdaptionContext<>( adapter, scrollableControl );
    scrollableLayouter = new CompositeScrollableLayouter( scrollableControl );
  }

  @Test
  public void layout() {
    exerciseLayoutCall();

    assertThat( styledText.getBounds() )
      .isEqualToRectangleOf( getExpectedX(), getExpectedY(), getExpectedWidth(), getExpectedHeight() );
  }

  @Test
  @ConditionalIgnore( condition = NonWindowsPlatform.class )
  public void layoutIfScrollbarsVisible() {
    adapter.open();
    styledText.setText( PARAGRAPHS );

    exerciseLayoutCall();

    assertThat( styledText.getBounds() )
      .isEqualToRectangleOf( getExpectedX(),
                             getExpectedY(),
                             getExpectedWidth() + computeBreadthOffset( styledText.getVerticalBar().getSize().x ),
                             getExpectedHeight() + computeBreadthOffset( styledText.getHorizontalBar().getSize().y ) );
  }

  private void exerciseLayoutCall() {
    scrollableLayouter.layout( stubOffset( context.newContext() ) );
  }

  private static AdaptionContext<?> stubOffset( AdaptionContext<StyledText> context ) {
    AdaptionContext<StyledText> result = spy( context );
    when( result.getOffset() ).thenReturn( OFF_SET );
    return result;
  }

  private Rectangle getAdapterClientArea() {
    return adapter.getClientArea();
  }

  private int getExpectedX() {
    return getAdapterClientArea().x - styledText.getBorderWidth() - OFF_SET;
  }

  private int getExpectedY() {
    return getAdapterClientArea().y - styledText.getBorderWidth() - OFF_SET;
  }

  private int getExpectedWidth() {
    return getAdapterClientArea().width + styledText.getBorderWidth() * 2 + OFF_SET * 2;
  }

  private int getExpectedHeight() {
    return getAdapterClientArea().height + styledText.getBorderWidth() * 2 + OFF_SET * 2;
  }

  private static int computeBreadthOffset( int scrollBarBreadth ) {
    return scrollBarBreadth - BAR_BREADTH;
  }
}