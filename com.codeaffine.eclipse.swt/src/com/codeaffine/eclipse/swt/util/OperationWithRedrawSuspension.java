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

import org.eclipse.swt.widgets.Control;

public class OperationWithRedrawSuspension {

  public void execute( Control control, Runnable runnable ) {
    control.setRedraw( false );
    try {
      runnable.run();
    } finally {
      control.setRedraw( true );
    }
  }
}