package com.codeaffine.eclipse.swt.widget.scrollable;

import static com.codeaffine.eclipse.swt.widget.scrollable.Platform.PlatformType.GTK;
import static com.codeaffine.eclipse.swt.widget.scrollable.Platform.PlatformType.WIN32;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;

public class FlatScrollBarTable extends ScrollableAdapter<Table> {

  @SuppressWarnings("unchecked")
  public FlatScrollBarTable( Composite parent, ScrollableFactory<Table> factory  ) {
    this( parent, new Platform(), factory, createLayoutMapping() );
  }

  FlatScrollBarTable(
    Composite parent, Platform platform, ScrollableFactory<Table> factory, LayoutMapping<Table> ... mappings  )
  {
    super( parent, platform, factory,  mappings );
  }

  public Table getTable() {
    return getScrollable();
  }

  static LayoutMapping<Table> createLayoutMapping() {
    return new LayoutMapping<Table>( new TableLayoutFactory(), WIN32, GTK );
  }
}