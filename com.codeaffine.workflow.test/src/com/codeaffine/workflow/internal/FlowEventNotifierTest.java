package com.codeaffine.workflow.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import com.codeaffine.workflow.FlowEvent;
import com.codeaffine.workflow.FlowListener;
import com.codeaffine.workflow.NodeDefinition;

public class FlowEventNotifierTest {

  private static final String NODE_ID = "id";
  private static final NodeDefinition NODE_DEFINITION = new NodeDefinition( NODE_ID, null, null );

  private ArgumentCaptor<FlowEvent> flowEventCaptor;
  private FlowEventNotifier flowEventNotifier;
  private FlowListener listener;

  @Before
  public void setUp() {
    listener = mock( FlowListener.class );
    flowEventCaptor = forClass( FlowEvent.class );
    flowEventNotifier = new FlowEventNotifier();
    flowEventNotifier.addFlowListener( listener );
  }

  @Test
  public void notifyOnNodeEnter() {
    flowEventNotifier.notifyOnNodeEnter( NODE_DEFINITION );

    verify( listener ).onNodeEnter( flowEventCaptor.capture() );
    assertThat( flowEventCaptor.getValue().getNodeId() ).isEqualTo( NODE_ID );
  }

  @Test
  public void notifyOnNodeEnterIfNodeDefinitionIsNull() {
    flowEventNotifier.notifyOnNodeEnter( null );

    verify( listener, never() ).onNodeEnter( any( FlowEvent.class ) );
  }

  @Test
  public void notifyOnNodeLeave() {
    flowEventNotifier.notifyOnNodeLeave( NODE_DEFINITION );

    verify( listener ).onNodeLeave( flowEventCaptor.capture() );
    assertThat( flowEventCaptor.getValue().getNodeId() ).isEqualTo( NODE_ID );
  }

  @Test
  public void notifyOnNodeLeaveIfNodeDefinitionIsNull() {
    flowEventNotifier.notifyOnNodeLeave( null );

    verify( listener, never() ).onNodeLeave( any( FlowEvent.class ) );
  }

  @Test
  public void remove() {
    flowEventNotifier.removeFlowListener( listener );
    flowEventNotifier.notifyOnNodeLeave( NODE_DEFINITION );

    verify( listener, never() ).onNodeLeave( any( FlowEvent.class ) );
  }
}