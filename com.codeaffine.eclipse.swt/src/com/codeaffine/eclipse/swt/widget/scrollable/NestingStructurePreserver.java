/**
 * Copyright (c) 2014 - 2016 Frank Appel
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Frank Appel - initial API and implementation
 */
package com.codeaffine.eclipse.swt.widget.scrollable;

import static com.codeaffine.eclipse.swt.util.ControlReflectionUtil.PARENT;
import static java.util.Arrays.asList;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Scrollable;

import com.codeaffine.eclipse.swt.util.ControlReflectionUtil;
import com.codeaffine.eclipse.swt.widget.scrollable.context.AdaptionContext;
import com.codeaffine.eclipse.swt.widget.scrollable.context.ScrollableControl;

class NestingStructurePreserver {

  private final ControlReflectionUtil controlReflectionUtil;
  private final ScrollableControl<?> scrollable;
  private final Composite adapter;

  NestingStructurePreserver( AdaptionContext<? extends Scrollable> context ) {
    this( context.getScrollable(), context.getAdapter() );
  }

  NestingStructurePreserver( ScrollableControl<? extends Scrollable> scrollable, Composite adapter ) {
    this.controlReflectionUtil = new ControlReflectionUtil();
    this.scrollable = scrollable;
    this.adapter = adapter;
  }

  void run() {
    if( asList( adapter.getParent().getChildren() ).contains( scrollable.getControl() ) ) {
      scrollable.setParent( adapter );
      controlReflectionUtil.setField( scrollable.getControl(), PARENT, adapter.getParent() );
    }
  }
}