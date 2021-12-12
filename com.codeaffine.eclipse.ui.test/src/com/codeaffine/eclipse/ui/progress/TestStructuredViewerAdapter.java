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

import org.eclipse.jface.viewers.StructuredViewer;

class TestStructuredViewerAdapter extends StructuredViewerAdapter<StructuredViewer> {

  TestStructuredViewerAdapter( StructuredViewer viewer ) {
    super( viewer );
  }

  @Override
  public void remove( Object element ) {
  }

  @Override
  public void addElements( Object parent, Object[] children ) {
  }
}