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
package com.codeaffine.workflow.internal;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.codeaffine.workflow.NodeDefinition;
import com.codeaffine.workflow.event.FlowEvent;
import com.codeaffine.workflow.event.FlowEventProvider;
import com.codeaffine.workflow.event.FlowListener;

public class FlowEventNotifier implements FlowEventProvider {

  private final Set<FlowListener> listeners;

  public FlowEventNotifier() {
    listeners = new HashSet<FlowListener>();
  }

  @Override
  public void addFlowListener( FlowListener listener ) {
    synchronized( listeners ) {
      listeners.add( listener );
    }
  }

  @Override
  public void removeFlowListener( FlowListener listener ) {
    synchronized( listeners ) {
      listeners.remove( listener );
    }
  }

  public void notifyOnNodeLeave( NodeDefinition nodeDefinition ) {
    if( nodeDefinition != null ) {
      FlowEvent event = new FlowEvent( nodeDefinition.getNodeId() );
      for( FlowListener listener : getListeners() ) {
        listener.onNodeLeave( event );
      }
    }
  }

  public void notifyOnNodeEnter( NodeDefinition nodeDefinition ) {
    if( nodeDefinition != null ) {
      FlowEvent event = new FlowEvent( nodeDefinition.getNodeId() );
      for( FlowListener listener : getListeners() ) {
        listener.onNodeEnter( event );
      }
    }
  }

  private Set<FlowListener> getListeners() {
    synchronized( listeners ) {
      return Collections.unmodifiableSet( listeners );
    }
  }
}