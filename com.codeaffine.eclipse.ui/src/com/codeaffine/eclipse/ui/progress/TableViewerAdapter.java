package com.codeaffine.eclipse.ui.progress;

import org.eclipse.jface.viewers.AbstractTableViewer;

public class TableViewerAdapter extends StructuredViewerAdapter<AbstractTableViewer> {

  public TableViewerAdapter( AbstractTableViewer tableViewer ) {
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