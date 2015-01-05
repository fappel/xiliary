package com.codeaffine.workflow.event;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import com.codeaffine.workflow.event.FlowEvent;

public class FlowEventTest {

  private static final String NODE_ID = "nodeId";

  @Test
  public void getNodeId() {
    FlowEvent event = new FlowEvent( NODE_ID );

    String actual = event.getNodeId();

    assertThat( actual ).isEqualTo( NODE_ID );
  }
}
