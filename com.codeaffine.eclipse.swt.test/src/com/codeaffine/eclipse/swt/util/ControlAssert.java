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
package com.codeaffine.eclipse.swt.util;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.AbstractAssert;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.mockito.ArgumentCaptor;

public class ControlAssert extends AbstractAssert<ControlAssert, Control> {

  interface SubAssert<T> {
    T that();
  }

  public ControlAssert( Control actual ) {
    super( actual, ControlAssert.class );
  }

  public static ControlAssert assertThat( Control actual ) {
    return new ControlAssert( actual );
  }

  public SubAssert<EventAssert> hasBeenNotifiedAboutEvent( int eventType ) {
    isNotNull();
    final ArgumentCaptor<Event> captor = ArgumentCaptor.forClass( Event.class );
    verify( actual ).notifyListeners( eq( eventType ), captor.capture() );
    return new SubAssert<EventAssert>() {
      @Override
      public EventAssert that() {
        return  new EventAssert( captor.getValue() );
      }
    };
  }

  public ControlAssert hasNotBeenNotifiedAboutEvent( int eventType ) {
    isNotNull();
    verify( actual, never() ).notifyListeners( eq( eventType ), any( Event.class ) );
    return this;
  }
}