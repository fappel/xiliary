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
package com.codeaffine.eclipse.ui.progress;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Test;

public class PlaceHolderCleanupTriggerTest {

  @Test
  public void done() {
    DeferredContentManager contentManager = mock( DeferredContentManager.class );
    PendingUpdatePlaceHolder placeHolder = new PendingUpdatePlaceHolder();
    PlaceHolderCleanupTrigger trigger = new PlaceHolderCleanupTrigger( contentManager, placeHolder );

    trigger.done( null );

    verify( contentManager ).clearPlaceholder( placeHolder );
  }
}