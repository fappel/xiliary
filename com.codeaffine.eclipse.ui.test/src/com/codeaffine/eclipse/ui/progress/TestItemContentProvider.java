/**
 * Copyright (c) 2014 - 2017 Frank Appel
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Frank Appel - initial API and implementation
 */
package com.codeaffine.eclipse.ui.progress;

import static com.codeaffine.eclipse.ui.progress.TestItems.cast;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

class TestItemContentProvider implements ITreeContentProvider {

  private final DeferredContentManager contentManager;

  TestItemContentProvider( DeferredContentManager contentManager ) {
    this.contentManager = contentManager;
  }

  @Override
  public void inputChanged( Viewer viewer, Object oldInput, Object newInput ) {
  }

  @Override
  public void dispose() {
  }

  @Override
  public boolean hasChildren( Object element ) {
    return contentManager.mayHaveChildren( element );
  }

  @Override
  public Object getParent( Object element ) {
    return cast( element ).getParent();
  }

  @Override
  public Object[] getElements( Object inputElement ) {
    return getChildren( inputElement );
  }

  @Override
  public Object[] getChildren( Object parentElement ) {
    return contentManager.getChildren( parentElement );
  }
}