package com.codeaffine.eclipse.swt.widget.scrollable;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Tree;

import com.codeaffine.eclipse.swt.widget.scrollbar.FlatScrollBar;

public class FlatScrollBarTree extends Composite {

  static final int BAR_BREADTH = 6;

  private final FlatScrollBar horizontalBar;
  private final FlatScrollBar verticalBar;
  private final WatchDog watchDog;
  private final Tree tree;


  public interface TreeFactory {
    Tree create( Composite parent );
  }

  public FlatScrollBarTree( Composite parent, TreeFactory treeFactory  ) {
    super( parent, SWT.NONE );
    this.tree = treeFactory.create( this );
    this.horizontalBar = createFlatScrollBar( this, tree, SWT.HORIZONTAL );
    this.verticalBar = createFlatScrollBar( this, tree, SWT.VERTICAL );
    this.watchDog = new WatchDog( tree, verticalBar );
    super.setLayout( new FlatScrollBarTreeLayout( tree, horizontalBar, verticalBar ) );
    setBackground( tree.getBackground() );
    horizontalBar.addSelectionListener( new HorizontalSelectionListener( tree ) );
    verticalBar.addSelectionListener( new VerticalSelectionListener( tree ) );
    addDisposeListener( watchDog );
  }

  public Tree getTree() {
    return tree;
  }

  @Override
  public void setLayout( Layout layout ) {
    throw new UnsupportedOperationException( FlatScrollBarTree.class.getName() + " does not allow to change layout." );
  }

  private static FlatScrollBar createFlatScrollBar( Composite parent, Tree tree, int direction  ) {
    FlatScrollBar result = new FlatScrollBar( parent, direction );
    result.setBackground( tree.getBackground() );
    result.moveAbove( null );
    return result;
  }
}