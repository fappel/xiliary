package com.codeaffine.eclipse.ui.progress;

import static org.eclipse.core.runtime.Assert.isNotNull;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.Platform;

class Adapters {

  public <T> T getAdapter( Object adaptable, Class<T> adapterType ) {
    isNotNull( adapterType );

    if( adaptable != null ) {
      return doGetAdapter( adaptable, adapterType );
    }
    return null;
  }

  private static <T> T doGetAdapter( Object adaptable, Class<T> adapterType ) {
    T result;
    if( adapterType.isInstance( adaptable ) ) {
      result = adapterType.cast( adaptable );
    } else  if( adaptable instanceof IAdaptable ) {
      result = getAdapterFromAdaptable( adaptable, adapterType );
    } else {
      result = getAdapterFromRegistry( adaptable, adapterType );
    }
    return result;
  }

  private static <T> T getAdapterFromAdaptable( Object adaptable, Class<T> adapterType ) {
    Object adapter = ( ( IAdaptable )adaptable).getAdapter( adapterType );
    if( adapter != null ) {
      return adapterType.cast( adapter );
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