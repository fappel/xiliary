package com.codeaffine.eclipse.swt.widget.scrollable;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Tree;

public class FlatScrollBarTree extends Composite {

  static final int BAR_BREADTH = 6;

  private final Tree tree;

  public interface TreeFactory {
    Tree create( Composite parent );
  }

  interface Content {
    Layout getLayout();
  }

  public FlatScrollBarTree( Composite parent, TreeFactory treeFactory  ) {
    super( parent, SWT.NONE );
    this.tree = treeFactory.create( this );
    super.setLayout( createContent().getLayout() );
  }

  public Tree getTree() {
    return tree;
  }

  @Override
  public void setLayout( Layout layout ) {
    throw new UnsupportedOperationException( FlatScrollBarTree.class.getName() + " does not allow to change layout." );
  }

  private Content createContent() {
    Content result = new NativeContent();
    if( "win32".equals( SWT.getPlatform() ) || "gtk".equals( SWT.getPlatform() ) ) {
      result = new CustomContent( this, tree );
    }
    return result;
  }
}