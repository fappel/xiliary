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
package com.codeaffine.workflow.test.util;

import java.util.LinkedList;
import java.util.List;

import com.codeaffine.workflow.event.FlowEvent;
import com.codeaffine.workflow.event.FlowEventProvider;
import com.codeaffine.workflow.event.FlowListener;
import com.codeaffine.workflow.test.util.FlowEventLogEntry.EventType;

public class FlowEventLog implements FlowListener {

  private final List<FlowEventLogEntry> log;

  public FlowEventLog() {
    log = new LinkedList<FlowEventLogEntry>();
  }

  @Override
  public void onNodeLeave( FlowEvent event ) {
    log.add( $( EventType.ON_NODE_LEAVE, event.getNodeId() ) );
  }

  @Override
  public void onNodeEnter( FlowEvent event ) {
    log.add( $( EventType.ON_NODE_ENTER, event.getNodeId() ) );
  }

  public FlowEventLogEntry[] getContent() {
    return log.toArray( new FlowEventLogEntry[ log.size() ] );
  }

  public void clear() {
    log.clear();
  }

  public static FlowEventLog registerFlowEventLog( FlowEventProvider provider ) {
    FlowEventLog result = new FlowEventLog();
    provider.addFlowListener( result );
    return result;
  }

  public static FlowEventLogEntry $( EventType type, String nodeId ) {
    return new FlowEventLogEntry( type, nodeId );
  }

  public static FlowEventLogEntry[] expectedLog( FlowEventLogEntry ... logEntries ) {
    return logEntries;
  }
}