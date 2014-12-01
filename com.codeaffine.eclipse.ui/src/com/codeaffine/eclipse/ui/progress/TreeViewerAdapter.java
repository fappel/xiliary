package com.codeaffine.eclipse.ui.progress;

import org.eclipse.jface.viewers.TreeViewer;

public class TreeViewerAdapter extends StructuredViewerAdapter<TreeViewer> {

  public TreeViewerAdapter( TreeViewer treeViewer ) {
    super( treeViewer );
  }

  @Override
  public void remove( Object element ) {
    viewer.remove( element );
  }

  @Override
  public void addElements( Object parent, Object[] children ) {
    viewer.add( parent, children );
  }
}