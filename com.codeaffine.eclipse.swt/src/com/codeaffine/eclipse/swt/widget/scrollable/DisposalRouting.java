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
package com.codeaffine.eclipse.swt.widget.scrollable;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Listener;

class DisposalRouting {

  void register( Composite adapter, Control adaptable ) {
    Listener adaptableDisposeListener = evt -> handleDispose( adapter, adaptable );
    adaptable.addListener( SWT.Dispose, adaptableDisposeListener );
    adapter.addListener( SWT.Dispose, evt -> adaptable.removeListener( SWT.Dispose, adaptableDisposeListener ) );
  }

  private static void handleDispose( Composite adapter, Control adaptable ) {
    adaptable.setParent( adapter.getParent() );
    adapter.dispose();
  }
}