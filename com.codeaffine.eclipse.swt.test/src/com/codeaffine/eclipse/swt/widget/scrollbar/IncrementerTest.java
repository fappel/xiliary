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
package com.codeaffine.eclipse.swt.widget.scrollbar;

import static com.codeaffine.eclipse.swt.widget.scrollbar.FlatScrollBarHelper.stubScrollBar;
import static org.mockito.Mockito.verify;

import org.eclipse.swt.SWT;
import org.junit.Test;

public class IncrementerTest {

  private static final int SELECTION = 3;
  private static final int INCREMENT = 1;

  @Test
  public void run() {
    FlatScrollBar scrollBar = stubScrollBar( SELECTION, INCREMENT );
    Incrementer incrementer = new Incrementer( scrollBar );

    incrementer.run();

    verify( scrollBar ).setSelectionInternal( INCREMENT + SELECTION, SWT.ARROW_DOWN );
  }
}