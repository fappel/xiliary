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
import static com.codeaffine.eclipse.swt.widget.scrollable.TreeHelper.createTree;
import static com.codeaffine.eclipse.swt.widget.scrollable.TreeHelper.expandRootLevelItems;
import static org.assertj.core.api.Assertions.assertThat;

import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.eclipse.swt.test.util.DisplayHelper;
import com.codeaffine.eclipse.swt.test.util.SWTIgnoreConditions.CocoaPlatform;
import com.codeaffine.eclipse.swt.widget.scrollable.context.AdaptionContext;
import com.codeaffine.eclipse.swt.widget.scrollable.context.ScrollableControl;
import com.codeaffine.test.util.junit.ConditionalIgnoreRule;
import com.codeaffine.test.util.junit.ConditionalIgnoreRule.ConditionalIgnore;

public class PreferredWidthComputerTest {

  @Rule public final ConditionalIgnoreRule conditionIgnoreRule = new ConditionalIgnoreRule();
  @Rule public final DisplayHelper displayHelper = new DisplayHelper();

  private AdaptionContext<Tree> layoutContext;
  private PreferredWidthComputer computer;
  private Tree tree;


  @Before
  public void setUp() {
    Shell shell = createShell( displayHelper );
    tree = createTree( shell, 6, 4 );
    layoutContext = new AdaptionContext<>( shell, new ScrollableControl<>( tree ) );
    computer = new PreferredWidthComputer();
    shell.open();
  }

  @Test
  public void compute() {
    layoutContext.updatePreferredSize();

    int actual = computer.compute( layoutContext.newContext() );

    assertThat( actual ).isEqualTo( preferredWidth() );
  }

  @Test
  @ConditionalIgnore( condition = CocoaPlatform.class)
  public void computeIfVerticalScrollBarVisible() {
    expandRootLevelItems( tree );
    layoutContext.updatePreferredSize();

    int actual = computer.compute( layoutContext.newContext() );

    assertThat( actual ).isEqualTo( overlayAdjustment() );
  }

  private int preferredWidth() {
    AdaptionContext<?> context = layoutContext.newContext( tree.getItemHeight() );
    return context.getPreferredSize().x + context.getOffset() * 2;
  }

  private int overlayAdjustment() {
    AdaptionContext<?> context = layoutContext.newContext( tree.getItemHeight() );
    return preferredWidth() + context.getVerticalBarOffset();
  }
}