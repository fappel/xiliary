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
package com.codeaffine.eclipse.swt.widget.scrollable;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;

import com.codeaffine.eclipse.swt.widget.scrollbar.FlatScrollBar;

class SelectionEventHelper {

  static SelectionEvent createEvent( Shell shell, int selection ) {
    FlatScrollBar scrollBar = new FlatScrollBar( shell, SWT.HORIZONTAL );
    return createEvent( scrollBar, selection );
  }

  static SelectionEvent createEvent( FlatScrollBar scrollBar , int selection  ) {
    scrollBar.setSelection( selection );
    Event event = new Event();
    event.widget = scrollBar;
    return new SelectionEvent( event );
  }
}