package com.codeaffine.workflow.event;

public interface FlowEventProvider {
  void addFlowListener( FlowListener flowListener );
  void removeFlowListener( FlowListener flowListener );
}