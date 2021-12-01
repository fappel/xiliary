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
import static com.codeaffine.eclipse.swt.widget.scrollable.TreeHelper.createTree;
import static com.codeaffine.eclipse.swt.widget.scrollable.TreeHelper.expandRootLevelItems;
import static com.codeaffine.eclipse.swt.widget.scrollable.TreeHelper.expandTopBranch;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import com.codeaffine.eclipse.swt.test.util.DisplayHelper;
import com.codeaffine.eclipse.swt.test.util.SWTIgnoreConditions.CocoaPlatform;
import com.codeaffine.eclipse.swt.widget.scrollable.context.AdaptionContext;
import com.codeaffine.eclipse.swt.widget.scrollable.context.ScrollableControl;
import com.codeaffine.test.util.junit.ConditionalIgnoreRule;
import com.codeaffine.test.util.junit.ConditionalIgnoreRule.ConditionalIgnore;

@RunWith( Parameterized.class )
public class VisibilityTest {

  @Parameters
  public static Collection<Object[]> data() {
    Collection<Object[]> result = new ArrayList<Object[]>();
    result.add( new Object[] { SWT.VERTICAL } );
    result.add( new Object[] { SWT.HORIZONTAL } );
    return result;
  }

  @Rule public final ConditionalIgnoreRule conditionalIgnore = new ConditionalIgnoreRule();
  @Rule public final DisplayHelper displayHelper = new DisplayHelper();

  @Parameter
  public int orientation;

  private AdaptionContext<Tree> context;
  private Visibility visibility;
  private Shell shell;
  private Tree tree;

  @Before
  public void setUp() {
    shell = createShell( displayHelper, SWT.RESIZE );
    tree = createTree( shell, 2, 4 );
    context = new AdaptionContext<>( shell, new ScrollableControl<>( tree ) );
    shell.open();
    visibility = new Visibility( orientation );
  }

  @Test
  public void isVisible() {
    boolean actual = visibility.isVisible();

    assertThat( actual ).isFalse();
  }

  @Test
  @ConditionalIgnore( condition = CocoaPlatform.class)
  public void isVisibleOnScrollBarStateChangeWithoutUpdate() {
    shell.setSize( 200, 100 );
    expandRootLevelItems( tree );
    expandTopBranch( tree );

    boolean actual = visibility.isVisible();

    assertThat( actual ).isFalse();
  }

  @Test
  @ConditionalIgnore( condition = CocoaPlatform.class )
  public void isVisibleOnScrollBarStateChangeAndUpdate() {
    shell.setSize( 200, 100 );
    expandRootLevelItems( tree );
    expandTopBranch( tree );
    context.updatePreferredSize();
    visibility.update( context.newContext() );

    boolean actual = visibility.isVisible();

    assertThat( actual ).isTrue();
  }

  @Test
  public void hasChanged() {
    boolean actual = visibility.hasChanged( context.newContext() );

    assertThat( actual ).isFalse();
  }

  @Test
  @ConditionalIgnore( condition = CocoaPlatform.class )
  public void hasChangedOnScrollBarStateChange() {
    shell.setSize( 200, 100 );
    expandRootLevelItems( tree );
    expandTopBranch( tree );
    context.updatePreferredSize();

    boolean actual = visibility.hasChanged( context.newContext() );

    assertThat( actual ).isTrue();
  }

  @Test
  @ConditionalIgnore( condition = CocoaPlatform.class)
  public void hasChangedOnScrollBarStateChangeAndUpdate() {
    shell.setSize( 200, 100 );
    expandRootLevelItems( tree );
    expandTopBranch( tree );

    visibility.update( context.newContext() );

    boolean actual = visibility.hasChanged( context.newContext() );

    assertThat( actual ).isFalse();
  }
}