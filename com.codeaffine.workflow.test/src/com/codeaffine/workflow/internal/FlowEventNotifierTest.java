/**
 * Copyright (c) 2014 - 2022 Frank Appel
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Frank Appel - initial API and implementation
 */
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

import com.codeaffine.workflow.NodeDefinition;
import com.codeaffine.workflow.event.FlowEvent;
import com.codeaffine.workflow.event.FlowListener;

public class FlowEventNotifierTest {

  private static final String NODE_ID = "id";
  private static final NodeDefinition NODE_DEFINITION = new NodeDefinition( NODE_ID, null, ( String[])null );

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