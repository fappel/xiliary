package com.codeaffine.eclipse.ui.progress;

public interface ViewerAdapter {
  void remove( Object element );
  void addElements( Object parent, Object[] children );
  boolean isDisposed();
}