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
package com.codeaffine.eclipse.ui.progress;

import static com.codeaffine.eclipse.swt.test.util.ShellHelper.createShell;
import static com.codeaffine.eclipse.ui.progress.TestItems.CHILD_COUNT;
import static com.codeaffine.eclipse.ui.progress.TestItems.populateTestItemTree;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Shell;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.eclipse.swt.test.util.DisplayHelper;
import com.codeaffine.eclipse.swt.test.util.SWTIgnoreConditions.CocoaPlatform;
import com.codeaffine.test.util.junit.ConditionalIgnoreRule;
import com.codeaffine.test.util.junit.ConditionalIgnoreRule.ConditionalIgnore;

@Ignore( "Check whether macos build works if completely ignored" )
public class TreeViewerAdapterTest {

  @Rule
  public final DisplayHelper displayHelper = new DisplayHelper();
  @Rule
  public final ConditionalIgnoreRule conditionalyIgnoreRule = new ConditionalIgnoreRule();

  private TreeViewerAdapter adapter;
  private TreeViewer treeViewer;
  private TestItem root;

  @Before
  public void setUp() {
    root = populateTestItemTree();
    treeViewer = createTreeViewer( root );
    adapter = new TreeViewerAdapter( treeViewer );
  }

  @Test
  @ConditionalIgnore( condition = CocoaPlatform.class )
  public void addElements() {
    Object[] children = { new TestItem( root, "child" ) };

    adapter.addElements( root, children );

    assertThat( treeViewer.getTree().getItemCount() ).isEqualTo( CHILD_COUNT + 1 );
  }

  @Test
  @ConditionalIgnore( condition = CocoaPlatform.class )
  public void remove() {
    Object[] children = { new TestItem( root, "child" ) };
    adapter.addElements( root, children );

    adapter.remove( children[ 0 ] );

    assertThat( treeViewer.getTree().getItemCount() ).isEqualTo( CHILD_COUNT );
  }

  private TreeViewer createTreeViewer( TestItem root  ) {
    Shell shell = createShell( displayHelper );
    TreeViewer result = new TreeViewer( shell );
    result.setContentProvider( createTreeContentProvider() );
    result.setInput( root );
    return result;
  }

  private TestItemContentProvider createTreeContentProvider() {
    DeferredContentManager contentManager = mock( DeferredContentManager.class );
    when( contentManager.getChildren( root ) ).thenReturn( root.getChildren().toArray() );
    return new TestItemContentProvider( contentManager );
  }
}