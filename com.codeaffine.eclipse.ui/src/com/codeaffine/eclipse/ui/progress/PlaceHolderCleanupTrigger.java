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
package com.codeaffine.eclipse.ui.progress;

import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;

class PlaceHolderCleanupTrigger extends JobChangeAdapter {

  private final PendingUpdatePlaceHolder placeHolder;
  private final DeferredContentManager contentManager;

  PlaceHolderCleanupTrigger( DeferredContentManager contentManager, PendingUpdatePlaceHolder placeHolder ) {
    this.contentManager = contentManager;
    this.placeHolder = placeHolder;
  }

  @Override
  public void done( IJobChangeEvent event ) {
    contentManager.clearPlaceholder( placeHolder );
  }
}