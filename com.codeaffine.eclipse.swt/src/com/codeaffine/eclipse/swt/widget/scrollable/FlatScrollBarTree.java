package com.codeaffine.eclipse.swt.widget.scrollable;

import static com.codeaffine.eclipse.swt.widget.scrollable.Platform.PlatformType.GTK;
import static com.codeaffine.eclipse.swt.widget.scrollable.Platform.PlatformType.WIN32;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;

public class FlatScrollBarTree extends ScrollableAdapter<Tree> {

  static final int BAR_BREADTH = 6;
  static final int MAX_EXPANSION = BAR_BREADTH + 2;

  @SuppressWarnings("unchecked")
  public FlatScrollBarTree( Composite parent, ScrollableFactory<Tree> factory  ) {
    this( parent, new Platform(), factory, createLayoutMapping() );
  }

  FlatScrollBarTree(
    Composite parent, Platform platform, ScrollableFactory<Tree> factory, LayoutMapping<Tree> ...mappings )
  {
    super( parent, platform, factory, mappings );
  }

  public Tree getTree() {
    return getScrollable();
  }

  static LayoutMapping<Tree> createLayoutMapping() {
    return new LayoutMapping<Tree>( new TreeLayoutFactory(), WIN32, GTK );
  }
}