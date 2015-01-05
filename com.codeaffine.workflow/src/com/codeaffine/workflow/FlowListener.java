package com.codeaffine.workflow;

public interface FlowListener {
  void onNodeEnter( FlowEvent event );
  void onNodeLeave( FlowEvent event );
}