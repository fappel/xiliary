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

import static com.codeaffine.eclipse.ui.progress.TestItems.cast;

import org.eclipse.jface.viewers.LabelProvider;

class TestItemLabelProvider extends LabelProvider {

  @Override
  public String getText( Object element ) {
    String result = super.getText( element );
    if( element instanceof TestItem ) {
      result = cast( element ).getName();
    }
    return result;
  }
}