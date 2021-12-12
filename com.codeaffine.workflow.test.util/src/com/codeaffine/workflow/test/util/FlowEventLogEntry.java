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
package com.codeaffine.workflow.test.util;

public class FlowEventLogEntry {
  
  private final EventType eventType;
  private final String nodeId;

  public static enum EventType {
    ON_NODE_ENTER, ON_NODE_LEAVE
  }
  
  public FlowEventLogEntry( EventType eventType, String nodeId ){
    this.eventType = eventType;
    this.nodeId = nodeId;
  }
  
  public EventType getEventType() {
    return eventType;
  }
  
  public String getNodeId() {
    return nodeId;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ( ( eventType == null ) ? 0 : eventType.hashCode() );
    result = prime * result + ( ( nodeId == null ) ? 0 : nodeId.hashCode() );
    return result;
  }

  @Override
  public boolean equals( Object obj ) {
    if( this == obj ) {
      return true;
    }
    if( obj == null ) {
      return false;
    }
    if( getClass() != obj.getClass() ) {
      return false;
    }
    FlowEventLogEntry other = ( FlowEventLogEntry )obj;
    if( eventType != other.eventType ) {
      return false;
    }
    if( nodeId == null ) {
      if( other.nodeId != null ) {
        return false;
      }
    } else if( !nodeId.equals( other.nodeId ) ) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "FlowEventLogEntry [eventType=" + eventType + ", nodeId=" + nodeId + "]";
  }
}