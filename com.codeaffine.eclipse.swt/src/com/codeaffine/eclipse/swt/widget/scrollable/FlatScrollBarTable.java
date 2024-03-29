/**
 * Copyright (c) 2014 - 2022 Frank Appel
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Frank Appel - initial API and implementation
 */
package com.codeaffine.eclipse.swt.widget.scrollable;

import static com.codeaffine.eclipse.swt.util.Platform.PlatformType.GTK;
import static com.codeaffine.eclipse.swt.util.Platform.PlatformType.WIN32;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;

import com.codeaffine.eclipse.swt.util.Platform;

public class FlatScrollBarTable extends ScrollableAdapter<Table> {

  public FlatScrollBarTable( Composite parent, ScrollableFactory<Table> factory  ) {
    this( parent, new Platform(), factory, createLayoutMapping() );
  }

  @SafeVarargs
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