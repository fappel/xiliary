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
package com.codeaffine.eclipse.swt.widget.scrollbar;

import java.util.Collection;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

class UntypedSelectionAdapter extends SelectionAdapter {

  final Listener listener;

  UntypedSelectionAdapter( Listener listener ) {
    this.listener = listener;
  }

  @Override
  public void widgetSelected( SelectionEvent selectionEvent ) {
    Event event = new Event();
    event.widget = selectionEvent.widget;
    event.detail = selectionEvent.detail;
    listener.handleEvent( event );
  }

  static SelectionListener lookup( Collection<SelectionListener> listeners, Listener untypedListener ) {
    for( SelectionListener listener : listeners ) {
      if( isAdapterType( listener ) && matches( untypedListener, listener ) ) {
        return listener;
      }
    }
    return null;
  }

  private static boolean isAdapterType( SelectionListener listener ) {
    return listener instanceof UntypedSelectionAdapter;
  }

  private static boolean matches( Listener untypedListener, SelectionListener listener ) {
    return ( ( UntypedSelectionAdapter )listener ).listener == untypedListener;
  }
}