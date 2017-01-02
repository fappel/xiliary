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
package com.codeaffine.osgi.test.util;

import static org.osgi.framework.FrameworkUtil.getBundle;

import java.util.LinkedList;
import java.util.List;

import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

public class ServiceCollector {

  private static final Object[] EMPTY_ARRAY = new Object[ 0 ];

  public static <T> List<T> collectServices( Class<T> serviceType, Class<? extends T> implementationType ) {
    List<T> result = new LinkedList<T>();
    ServiceTracker<T, Object> tracker = createTracker( serviceType, implementationType );
    for( Object service : getTrackedServices( tracker ) ) {
      if( service.getClass() == implementationType ) {
        result.add( serviceType.cast( service ) );
      }
    }
    return result;
  }

  private static <T> ServiceTracker<T, Object> createTracker( Class<T> type, Class<? extends T> implementation ) {
    BundleContext context = getBundle( implementation ).getBundleContext();
    return new ServiceTracker<T, Object>( context, type, null );
  }

  private static <T> Object[] getTrackedServices( ServiceTracker<T, Object> tracker ) {
    try {
      tracker.open();
      return getServices( tracker );
    } finally {
      tracker.close();
    }
  }

  private static <T> Object[] getServices( ServiceTracker<T, Object> tracker ) {
    Object[] result = tracker.getServices();
    if( result == null ) {
      result = EMPTY_ARRAY;
    }
    return result;
  }
}