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
package com.codeaffine.test.util.lang;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

import org.junit.Test;

import com.codeaffine.test.util.lang.ThrowableCaptor.Actor;

public class ThrowableCaptorTest {

  @Test
  public void thrownBy() throws Throwable {
    Throwable expected = new Throwable();
    Actor actor = createActorThatThrows( expected );

    Throwable actual = ThrowableCaptor.thrownBy( actor );

    assertThat( actual ).isSameAs( expected );
  }

  @Test
  public void thrownByIfNoThrowableOccurs() {
    Actor actor = mock( Actor.class );

    Throwable actual = ThrowableCaptor.thrownBy( actor );

    assertThat( actual ).isNull();
  }

  private static Actor createActorThatThrows( Throwable toBeThrown ) throws Throwable {
    Actor result = mock( Actor.class );
    doThrow( toBeThrown ).when( result ).act();
    return result;
  }
}
