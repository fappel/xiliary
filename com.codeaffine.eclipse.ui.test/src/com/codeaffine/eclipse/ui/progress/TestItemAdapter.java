package com.codeaffine.eclipse.ui.progress;

import static com.codeaffine.eclipse.ui.progress.TestItems.cast;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.progress.IDeferredWorkbenchAdapter;
import org.eclipse.ui.progress.IElementCollector;

public class TestItemAdapter implements IDeferredWorkbenchAdapter {

  private boolean container;

  public TestItemAdapter() {
    container = true;
  }

  @Override
  public Object[] getChildren( Object testItem ) {
    return cast( testItem ).getChildren().toArray();
  }

  @Override
  public ImageDescriptor getImageDescriptor( Object testItem ) {
    return null;
  }

  @Override
  public String getLabel( Object testItem ) {
    return cast( testItem ).getName();
  }

  @Override
  public Object getParent( Object testItem ) {
    return cast( testItem ).getParent();
  }

  @Override
  public void fetchDeferredChildren( Object testItem, IElementCollector collector, IProgressMonitor monitor ) {
    collector.add( cast( testItem ).getChildren().toArray(), monitor );
  }

  @Override
  public boolean isContainer() {
    return container;
  }

  @Override
  public ISchedulingRule getRule( Object testItem ) {
    return null;
  }

  void unsetContainer() {
    container = false;
  }
}