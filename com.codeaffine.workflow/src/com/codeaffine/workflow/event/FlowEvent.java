package com.codeaffine.workflow.event;

public class FlowEvent {
  
  private final String nodeId;
  
  public FlowEvent( String nodeId ) {
    this.nodeId = nodeId;
  }
  
  public String getNodeId() {
    return nodeId;
  }
}
