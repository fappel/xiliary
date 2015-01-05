package com.codeaffine.workflow;

public class FlowEvent {
  
  private final String nodeId;
  
  public FlowEvent( String nodeId ) {
    this.nodeId = nodeId;
  }
  
  public String getNodeId() {
    return nodeId;
  }
}
