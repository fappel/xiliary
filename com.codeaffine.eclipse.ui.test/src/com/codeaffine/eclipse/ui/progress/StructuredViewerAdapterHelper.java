package com.codeaffine.eclipse.ui.progress;

import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Item;
import org.eclipse.ui.IWorkbenchPartSite;

abstract class StructuredViewerAdapterHelper<T extends StructuredViewer> {

  protected final ViewerAdapter adapter;
  protected final JobHelper jobHelper;
  protected final T viewer;

  private DeferredContentManager contentManager;

  StructuredViewerAdapterHelper( Composite parent ) {
    viewer = createViewer( parent );
    adapter = createAdapter( viewer );
    jobHelper = new JobHelper();
  }

  abstract T createViewer( Composite parent );
  abstract ViewerAdapter createAdapter( T viewer );
  abstract int getItemCount();
  abstract Item getItem( int index );

  ViewerAdapter getViewerAdapter() {
    return adapter;
  }

  T getViewer() {
    return viewer;
  }

  DeferredContentManager getContentManager() {
    return contentManager;
  }

  void setInput( Object input ) {
    viewer.setInput( input );
  }

  void initializeViewer( IWorkbenchPartSite site ) {
    contentManager = createManager( site );
    viewer.setUseHashlookup( true );
    viewer.setContentProvider( new TestItemContentProvider( contentManager ) );
    viewer.setLabelProvider( new TestItemLabelProvider() );
  }

  void initializeViewer() {
    initializeViewer( null );
  }

  void waitTillJobHasFinished() {
    jobHelper.waitTillJobHasFinished();
  }

  private DeferredContentManager createManager( IWorkbenchPartSite site ) {
    DeferredContentManager result = new DeferredContentManager( adapter, site );
    result.addUpdateCompleteListener( jobHelper );
    return result;
  }

  void cancel() {
    contentManager.cancel( viewer.getInput() );
  }
}