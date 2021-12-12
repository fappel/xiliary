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

import org.eclipse.swt.widgets.Scrollable;

import com.codeaffine.eclipse.swt.util.Platform.PlatformType;

class LayoutMapping<T extends Scrollable> {

  private final LayoutFactory<T> layoutFactory;
  private final PlatformType[] platformTypes;

  LayoutMapping( LayoutFactory<T> layoutFactory, PlatformType ... platformTypes ) {
    this.layoutFactory = layoutFactory;
    this.platformTypes = platformTypes;
  }

  LayoutFactory<T> getLayoutFactory() {
    return layoutFactory;
  }

  PlatformType[] getPlatformTypes() {
    return platformTypes;
  }
}