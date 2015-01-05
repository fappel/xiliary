package com.codeaffine.workflow.event;

public interface FlowListener {
  void onNodeEnter( FlowEvent event );
  void onNodeLeave( FlowEvent event );
}