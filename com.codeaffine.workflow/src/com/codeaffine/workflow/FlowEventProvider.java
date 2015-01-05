package com.codeaffine.workflow;

public interface FlowEventProvider {
  public void addFlowListener( FlowListener flowListener );
  public void removeFlowListener( FlowListener flowListener );
}