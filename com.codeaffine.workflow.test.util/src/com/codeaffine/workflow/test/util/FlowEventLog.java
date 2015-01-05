package com.codeaffine.workflow.test.util;

import java.util.LinkedList;
import java.util.List;

import com.codeaffine.workflow.FlowEvent;
import com.codeaffine.workflow.FlowEventProvider;
import com.codeaffine.workflow.FlowListener;
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