package com.codeaffine.workflow.test.util;

import static com.codeaffine.workflow.test.util.FlowEventLog.$;
import static com.codeaffine.workflow.test.util.FlowEventLog.expectedLog;
import static com.codeaffine.workflow.test.util.FlowEventLogEntry.EventType.ON_NODE_ENTER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;

import com.codeaffine.workflow.event.FlowEvent;
import com.codeaffine.workflow.event.FlowEventProvider;
import com.codeaffine.workflow.test.util.FlowEventLogEntry.EventType;

public class FlowEventLogTest {

  private static final String NODE_ID = "nodeId";

  private FlowEventLog log;

  @Before
  public void setUp() {
    log = new FlowEventLog();
  }

  @Test
  public void onFlowEventEnter() {
    log.onNodeEnter( new FlowEvent( NODE_ID ) );
    FlowEventLogEntry[] actual = log.getContent();

    assertThat( actual ).isEqualTo( expectedLog(
      $( ON_NODE_ENTER, NODE_ID )
    ) );
  }

  @Test
  public void onFlowEventLeave() {
    log.onNodeLeave( new FlowEvent( NODE_ID ) );
    FlowEventLogEntry[] actual = log.getContent();

    assertThat( actual ).isEqualTo( expectedLog(
      $( EventType.ON_NODE_LEAVE, NODE_ID )
    ) );
  }

  @Test
  public void registerFlowLogEvent() {
    FlowEventProvider provider = mock( FlowEventProvider.class );

    FlowEventLog actual = FlowEventLog.registerFlowEventLog( provider );

    verify( provider ).addFlowListener( actual );
  }


  @Test
  public void clear() {
    log.onNodeEnter( new FlowEvent( NODE_ID ) );
    log.clear();

    FlowEventLogEntry[] actual = log.getContent();

    assertThat( actual ).isEmpty();
  }

}