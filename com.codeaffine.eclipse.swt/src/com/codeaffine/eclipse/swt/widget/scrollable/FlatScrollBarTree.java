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
import static com.codeaffine.eclipse.swt.widget.scrollbar.FlatScrollBar.BAR_BREADTH;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;

import com.codeaffine.eclipse.swt.util.Platform;

public class FlatScrollBarTree extends ScrollableAdapter<Tree> {

  static final int MAX_EXPANSION = BAR_BREADTH + 2;

  public FlatScrollBarTree( Composite parent, ScrollableFactory<Tree> factory  ) {
    this( parent, new Platform(), factory, createLayoutMapping() );
  }

  @SafeVarargs
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