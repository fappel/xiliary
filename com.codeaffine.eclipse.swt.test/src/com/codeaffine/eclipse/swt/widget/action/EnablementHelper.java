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
package com.codeaffine.eclipse.swt.widget.action;

import static org.mockito.Mockito.when;

import java.util.function.BooleanSupplier;

class EnablementHelper {

  static BooleanSupplier configureAsEnabled( BooleanSupplier enablement ) {
    when( enablement.getAsBoolean() ).thenReturn( true );
    return enablement;
  }

  static BooleanSupplier configureAsDisabled( BooleanSupplier enablement ) {
    when( enablement.getAsBoolean() ).thenReturn( false );
    return enablement;
  }
}