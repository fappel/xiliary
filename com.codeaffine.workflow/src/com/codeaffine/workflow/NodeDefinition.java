package com.codeaffine.workflow;

public class NodeDefinition {
  
  private final String[] successors;
  private final Class<?> type;
  private final String nodeId;

  public NodeDefinition( String nodeId, Class<?> type, String[] successors ) {
    this.nodeId = nodeId;
    this.type = type;
    this.successors = successors;
  }
  
  public String getNodeId() {
    return nodeId;
  }

  public Class<?> getType() {
    return type;
  }
  
  public String[] getSuccessors() {
    return successors.clone();
  }
}