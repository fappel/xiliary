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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.Collection;
import java.util.LinkedList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Widget;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

public class UntypedSelectionAdapterTest<E> {

  private UntypedSelectionAdapter adapter;
  private ArgumentCaptor<Event> captor;
  private Listener listener;

  @Before
  public void setUp() {
    captor = forClass( Event.class );
    listener = mock( Listener.class );
    adapter = new UntypedSelectionAdapter( listener );
  }

  @Test
  public void widgetSelected() {
    Event event = new Event();
    event.widget = mock( Widget.class );
    event.detail = SWT.DRAG;

    SelectionEvent selectionEvent = new SelectionEvent( event );
    adapter.widgetSelected( selectionEvent );

    verify( listener ).handleEvent( captor.capture() );
    assertThat( captor.getValue().widget ).isSameAs( event.widget );
    assertThat( captor.getValue().detail ).isEqualTo( SWT.DRAG );
  }

  @Test
  public void lookup() {
    Collection<SelectionListener> list = new LinkedList<SelectionListener>();
    list.add( adapter );

    SelectionListener actual = UntypedSelectionAdapter.lookup( list, listener );

    assertThat( actual ).isSameAs( adapter );
  }

  @Test
  public void lookupWithNonAdapterElementInCollection() {
    Collection<SelectionListener> list = new LinkedList<SelectionListener>();
    list.add( mock( SelectionListener.class ) );

    SelectionListener actual = UntypedSelectionAdapter.lookup( list, listener );

    assertThat( actual ).isNull();
  }

  @Test
  public void lookupWithNonMatchingListener() {
    Collection<SelectionListener> list = new LinkedList<SelectionListener>();
    list.add( adapter );

    SelectionListener actual = UntypedSelectionAdapter.lookup( list, mock( Listener.class ) );

    assertThat( actual ).isNull();
  }
}