package com.codeaffine.workflow.test.util;

public class DecisionConfiguration {
  
  String nodeId;
  String decision;
  String[] decisions;

  public DecisionConfiguration( String nodeId, String decision, String ... decisions ) {
    this.nodeId = nodeId;
    this.decision = decision;
    this.decisions = decisions;
  }
}