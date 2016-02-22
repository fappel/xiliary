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

import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

import com.codeaffine.eclipse.swt.widget.scrollbar.FlatScrollBar;

class StyledTextHorizontalSelectionListener extends SelectionAdapter {

  private final StyledText styledText;

  StyledTextHorizontalSelectionListener( StyledText styledText ) {
    this.styledText = styledText;
  }

  @Override
  public void widgetSelected( SelectionEvent event ) {
    int selection = ( ( FlatScrollBar )event.widget ).getSelection();
    styledText.setHorizontalPixel( selection );
  }
}