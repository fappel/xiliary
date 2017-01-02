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
package com.codeaffine.eclipse.swt.widget.scrollbar;

import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.mockito.ArgumentCaptor;

public class FlatScrollBarHelper {

  static FlatScrollBar stubScrollBar( int selection, int increment ) {
    FlatScrollBar result = mock( FlatScrollBar.class );
    when( result.getIncrement() ).thenReturn( increment );
    when( result.getSelection() ).thenReturn( selection );
    return result;
  }

  static SelectionListener equipScrollBarWithListener( FlatScrollBar scrollBar ) {
    SelectionListener result = mock( SelectionListener.class );
    scrollBar.addSelectionListener( result );
    return result;
  }

  static SelectionEvent verifyNotification( SelectionListener listener ) {
    ArgumentCaptor<SelectionEvent> captor = forClass( SelectionEvent.class );
    verify( listener ).widgetSelected( captor.capture() );
    return captor.getValue();
  }
}