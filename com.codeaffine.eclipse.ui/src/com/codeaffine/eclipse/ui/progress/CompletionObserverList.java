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

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.core.runtime.ListenerList;
import org.eclipse.core.runtime.jobs.IJobChangeListener;

class CompletionObserverList {

  private ListenerList listeners;

  void add( IJobChangeListener listener ) {
    ensureList();
    listeners.add( listener );
  }

  void remove( IJobChangeListener listener ) {
    listeners.remove( listener );
    deleteEmptyList();
  }

  Iterator<IJobChangeListener> iterator() {
    ArrayList<IJobChangeListener> result = new ArrayList<IJobChangeListener>();
    if( listeners != null ) {
      Object[] observers = listeners.getListeners();
      for( Object object : observers ) {
        result.add( ( IJobChangeListener )object );
      }
    }
    return result.iterator();
  }

  private void ensureList() {
    if( listeners == null ) {
      listeners = new ListenerList();
    }
  }

  private void deleteEmptyList() {
    if( listeners.isEmpty() ) {
      listeners = null;
    }
  }
}