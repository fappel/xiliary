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

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Test;

public class ResizeObserverTest {

  @Test
  public void controlResized() {
    FlatScrollBar scrollBar = mock( FlatScrollBar.class );
    ResizeObserver observer = new ResizeObserver( scrollBar );

    observer.controlResized( null );

    verify( scrollBar ).layout();
    verify( scrollBar ).moveAbove( null );
  }
}