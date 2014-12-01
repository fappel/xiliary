package com.codeaffine.eclipse.ui.progress;

import org.eclipse.jface.viewers.TableViewer;

public class TableViewerAdapter extends StructuredViewerAdapter<TableViewer> {

  public TableViewerAdapter( TableViewer tableViewer ) {
    super( tableViewer );
  }

  @Override
  public void remove( Object element ) {
    viewer.remove( element );
  }

  @Override
  public void addElements( Object parent, Object[] children ) {
    viewer.add( children );
  }
}