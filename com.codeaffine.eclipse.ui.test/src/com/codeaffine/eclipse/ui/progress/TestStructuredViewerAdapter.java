package com.codeaffine.eclipse.ui.progress;

import org.eclipse.jface.viewers.StructuredViewer;

class TestStructuredViewerAdapter extends StructuredViewerAdapter<StructuredViewer> {

  TestStructuredViewerAdapter( StructuredViewer viewer ) {
    super( viewer );
  }

  @Override
  public void remove( Object element ) {
  }

  @Override
  public void addElements( Object parent, Object[] children ) {
  }
}