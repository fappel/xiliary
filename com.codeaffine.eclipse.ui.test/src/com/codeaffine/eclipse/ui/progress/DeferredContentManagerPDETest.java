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
import static com.codeaffine.eclipse.ui.progress.StructuredViewerAdapterHelperAssert.assertThat;
import static com.codeaffine.eclipse.ui.progress.TestItems.populateTestItemTree;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.progress.IWorkbenchSiteProgressService;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.codeaffine.eclipse.swt.test.util.DisplayHelper;
import com.codeaffine.eclipse.swt.test.util.SWTIgnoreConditions.CocoaPlatform;
import com.codeaffine.test.util.junit.ConditionalIgnoreRule;
import com.codeaffine.test.util.junit.ConditionalIgnoreRule.ConditionalIgnore;

public class DeferredContentManagerPDETest {

  @Rule
  public final ConditionalIgnoreRule conditionalIgnoreRule = new ConditionalIgnoreRule();
  @Rule
  public final DisplayHelper displayHelper = new DisplayHelper();

  private Shell shell;

  @Before
  public void setUp() {
    shell = createShell( displayHelper, SWT.SHELL_TRIM );
  }

  @Test
  @ConditionalIgnore( condition = CocoaPlatform.class )
  public void deferredChildFetchingWithTreeViewer() {
    TreeViewerAdapterHelper adapterHelper = new TreeViewerAdapterHelper( shell );
    adapterHelper.initializeViewer();

    adapterHelper.setInput( populateTestItemTree() );

    assertThat( adapterHelper )
      .hasPendingItem( expectedPendingText() )
      .completesDeferredLoading()
      .hasItemCount( TestItems.CHILD_COUNT );
  }

  @Test
  @ConditionalIgnore( condition = CocoaPlatform.class )
  public void deferredChildFetchingWithTableViewer() {
    TableViewerAdapterHelper adapterHelper = new TableViewerAdapterHelper( shell );
    adapterHelper.initializeViewer();

    adapterHelper.setInput( populateTestItemTree() );

    assertThat( adapterHelper )
      .hasPendingItem( expectedPendingText() )
      .completesDeferredLoading()
      .hasItemCount( TestItems.CHILD_COUNT );
  }

  @Test
  public void cancelDeferredChildFetching() {
    TableViewerAdapterHelper adapterHelper = new TableViewerAdapterHelper( shell );
    adapterHelper.initializeViewer( stubProgressServiceForDelayedSchedule() );
    adapterHelper.setInput( populateTestItemTree() );

    adapterHelper.cancel();

    assertThat( adapterHelper )
      .completesDeferredLoading()
      .hasItemCount( 0 );
  }

  @Test
  public void isDeferredAdapterWithAdaptableParent() {
    TreeViewerAdapterHelper adapterHelper = new TreeViewerAdapterHelper( shell );
    adapterHelper.initializeViewer();
    DeferredContentManager manager = adapterHelper.getContentManager();

    boolean actual = manager.isDeferredAdapter( populateTestItemTree() );

    assertThat( actual ).isTrue();
  }

  @Test
  @ConditionalIgnore( condition = CocoaPlatform.class )
  @Ignore( "check whether failure on mac is caused even if ignored" )
  public void isDeferredAdapterWithoutAdaptableParent() {
    TreeViewerAdapterHelper adapterHelper = new TreeViewerAdapterHelper( shell );
    adapterHelper.initializeViewer();
    DeferredContentManager manager = adapterHelper.getContentManager();

    boolean actual = manager.isDeferredAdapter( new Object() );

    assertThat( actual ).isFalse();
  }

  @Test
  public void getChildrenWithoutAdaptableParent() {
    TreeViewerAdapterHelper adapterHelper = new TreeViewerAdapterHelper( shell );
    adapterHelper.initializeViewer();
    DeferredContentManager manager = adapterHelper.getContentManager();

    Object[] actual = manager.getChildren( new Object() );

    assertThat( actual ).isEmpty();
  }

  @Test
  public void mayHaveChildrenWithoutAdaptableParent() {
    TreeViewerAdapterHelper adapterHelper = new TreeViewerAdapterHelper( shell );
    adapterHelper.initializeViewer();
    DeferredContentManager manager = adapterHelper.getContentManager();

    boolean actual = manager.mayHaveChildren( new Object() );

    assertThat( actual ).isFalse();
  }

  @Test
  public void mayHaveChildrenWithoutContainerAdapter() {
    TreeViewerAdapterHelper adapterHelper = new TreeViewerAdapterHelper( shell );
    adapterHelper.initializeViewer();
    DeferredContentManager manager = adapterHelper.getContentManager();
    TestItem item = prepareNonContainerAdapter( manager );

    boolean actual = manager.mayHaveChildren( item );

    assertThat( actual ).isFalse();
  }

  private static String expectedPendingText() {
    return new PendingUpdatePlaceHolder().toString();
  }

  static IWorkbenchPartSite stubProgressServiceForDelayedSchedule() {
    IWorkbenchPartSite result = mock( IWorkbenchPartSite.class );
    IWorkbenchSiteProgressService progressService = stubProgressService( TestItem.FETCH_CHILDREN_DELAY );
    when( result.getAdapter( IWorkbenchSiteProgressService.class ) ).thenReturn( progressService );
    return result;
  }

  private static IWorkbenchSiteProgressService stubProgressService( int schedulingDelay ) {
    IWorkbenchSiteProgressService result = mock( IWorkbenchSiteProgressService.class );
    doAnswer( delay( schedulingDelay ) ).when( result ).schedule( any( Job.class ) );
    return result;
  }

  private static Answer<Object> delay( final int schedulingDelay ) {
    return new Answer<Object>() {
      @Override
      public Object answer( InvocationOnMock invocation ) throws Throwable {
        Job job = ( Job )invocation.getArguments()[ 0 ];
        job.schedule( schedulingDelay );
        return null;
      }
    };
  }

  private static TestItem prepareNonContainerAdapter( DeferredContentManager manager ) {
    TestItem result = populateTestItemTree();
    TestItemAdapter adapter = ( TestItemAdapter )manager.getAdapter( result );
    adapter.unsetContainer();
    return result;
  }
}