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

public class ThrowableCaptor {

  @FunctionalInterface
  public interface Actor {
    void act() throws Throwable;
  }

  public static Throwable thrownBy( Actor actor ) {
    try {
      actor.act();
    } catch( Throwable thrown ) {
      return thrown;
    }
    return null;
  }

  /**
   * use ThrowableCaptor.thrownBy instead
   */
  @Deprecated
  public static Throwable thrown( Actor actor ) {
    return thrownBy( actor );
  }
}