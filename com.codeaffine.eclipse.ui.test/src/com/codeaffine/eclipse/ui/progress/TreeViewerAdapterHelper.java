package com.codeaffine.eclipse.ui.progress;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Item;

class TreeViewerAdapterHelper extends StructuredViewerAdapterHelper<TreeViewer>{

  TreeViewerAdapterHelper( Composite parent ) {
    super( parent );
  }

  @Override
  TreeViewer createViewer( Composite parent ) {
    return new TreeViewer( parent );
  }

  @Override
  ViewerAdapter createAdapter( TreeViewer viewer ) {
    return new TreeViewerAdapter( viewer );
  }

  @Override
  public int getItemCount() {
    return viewer.getTree().getItemCount();
  }

  @Override
  public Item getItem( int index ) {
    return viewer.getTree().getItem( index );
  }
}