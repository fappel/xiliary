package com.codeaffine.eclipse.ui.progress;

import org.eclipse.jface.viewers.StructuredViewer;

public abstract class StructuredViewerAdapter<T extends StructuredViewer> implements ViewerAdapter {

  protected final T viewer;

  public StructuredViewerAdapter( T viewer ) {
    this.viewer = viewer;
  }

  @Override
  public boolean isDisposed() {
    return viewer.getControl().isDisposed();
  }
}