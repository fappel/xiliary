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
package com.codeaffine.eclipse.ui.progress;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ui.progress.IElementCollector;

class ElementCollector implements IElementCollector {

  private final PendingUpdatePlaceHolder placeHolder;
  private final DeferredContentManager contentManager;
  private final Object parent;

  ElementCollector( DeferredContentManager contentManager, Object parent, PendingUpdatePlaceHolder placeHolder ) {
    this.contentManager = contentManager;
    this.placeHolder = placeHolder;
    this.parent = parent;
  }

  @Override
  public void add( Object element, IProgressMonitor monitor ) {
    add( new Object[] { element }, monitor );
  }

  @Override
  public void add( Object[] elements, IProgressMonitor monitor ) {
    contentManager.addChildren( parent, elements );
  }

  @Override
  public void done() {
    contentManager.clearPlaceholder( placeHolder );
  }
}