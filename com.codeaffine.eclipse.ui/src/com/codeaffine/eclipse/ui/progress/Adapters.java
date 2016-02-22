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
package com.codeaffine.eclipse.ui.progress;

import static org.eclipse.core.runtime.Assert.isNotNull;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.PlatformObject;

class Adapters {

  public <T> T getAdapter( Object adaptable, Class<T> adapterType ) {
    isNotNull( adapterType );

    if( adaptable == null ) {
      return null;
    }

    if( adapterType.isInstance( adaptable ) ) {
      return adapterType.cast( adaptable );
    }

    if( adaptable instanceof IAdaptable ) {
      return getAdapterFromAdaptable( adaptable, adapterType );
    }

    return getAdapterFromRegistry( adaptable, adapterType );
  }

  private static <T> T getAdapterFromAdaptable( Object adaptable, Class<T> adapterType ) {
    Object adapter = ( ( IAdaptable )adaptable).getAdapter( adapterType );
    if( adapter != null ) {
      return adapterType.cast( adapter );
    }
    return getAdapterOfNonPlatformObjectFromRegistry( adaptable, adapterType );
  }

  private static <T> T getAdapterOfNonPlatformObjectFromRegistry( Object adaptable, Class<T> adapterType) {
    if( !( adaptable instanceof PlatformObject ) ) {
      return getAdapterFromRegistry( adaptable, adapterType );
    }
    return null;
  }

  private static <T> T getAdapterFromRegistry( Object adaptable, Class<T> adapterType ) {
    Object adapter = Platform.getAdapterManager().getAdapter( adaptable, adapterType );
    if( adapter != null ) {
      return adapterType.cast( adapter );
    }
    return null;
  }
}