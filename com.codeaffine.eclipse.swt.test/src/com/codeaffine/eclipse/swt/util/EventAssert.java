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
package com.codeaffine.eclipse.swt.util;

import org.assertj.core.api.AbstractAssert;
import org.eclipse.swt.widgets.Event;

public class EventAssert extends AbstractAssert<EventAssert, Event>{

  public EventAssert( Event actual ) {
    super( actual, EventAssert.class );
  }

  public static EventAssert assertThat( Event actual ) {
    return new EventAssert( actual );
  }

  public EventAssert hasX( int expected ) {
    isNotNull();
    if( actual.x != expected ) {
      failWithMessage( "Expected x to be <%s> but was <%s>.", expected, actual.x );
    }
    return this;
  }

  public EventAssert hasY( int expected ) {
    isNotNull();
    if( actual.y != expected ) {
      failWithMessage( "Expected y to be <%s> but was <%s>.", expected, actual.y );
    }
    return this;
  }

  public EventAssert hasType( int expected ) {
    isNotNull();
    if( actual.type != expected ) {
      failWithMessage( "Expected type to be <%s> but was <%s>.", expected, actual.type );
    }
    return this;
  }
}