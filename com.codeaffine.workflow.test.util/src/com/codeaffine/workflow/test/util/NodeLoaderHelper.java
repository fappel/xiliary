/**
 * Copyright (c) 2014 - 2017 Frank Appel
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Frank Appel - initial API and implementation
 */
package com.codeaffine.workflow.test.util;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import com.codeaffine.workflow.NodeLoader;
import com.codeaffine.workflow.WorkflowContext;
import com.codeaffine.workflow.definition.Activity;
import com.codeaffine.workflow.definition.Decision;
import com.codeaffine.workflow.definition.WorkflowDefinitionProvider;
import com.codeaffine.workflow.internal.NodeLoaderImpl;

public class NodeLoaderHelper {

  private final NodeLoader nodeLoader;

  public static <T> T load( WorkflowContext context, Class<T> type ) {
    return new NodeLoaderImpl().load( type, context );
  }

  public static <T> T load( WorkflowContext context, LoadAdapter<T> loadAdapter ) {
    try {
      NodeLoaderImpl.lookupContext.set( context );
      return loadAdapter.load();
    } finally {
      NodeLoaderImpl.lookupContext.set( null );
    }
  }

  public NodeLoaderHelper() {
    nodeLoader = mock( NodeLoader.class );
  }

  public NodeLoader getNodeLoader() {
    return nodeLoader;
  }

  public void configureNodeLoader( WorkflowDefinitionProvider provider, DecisionConfiguration ... configurations ) {
    Map<String, DecisionConfiguration> decisionConfiguration = mapToNodeId( configurations );
    provider.define( new NodeSpyDispatcher( this, decisionConfiguration ) );
  }

  private static Map<String, DecisionConfiguration> mapToNodeId( DecisionConfiguration... configurations ) {
    Map<String, DecisionConfiguration> result = new HashMap<String, DecisionConfiguration>();
    for( DecisionConfiguration decisionConfiguration : configurations ) {
      result.put( decisionConfiguration.nodeId, decisionConfiguration );
    }
    return result;
  }

  public void spyActivity( Class<? extends Activity> activity ) {
    spyNode( nodeLoader, activity );
  }

  public void spyDecision(
    String nodeId, Class<? extends Decision> decisionType, Map<String, DecisionConfiguration> configurationMap )
  {
    Decision decisionInstance = spyNode( nodeLoader, decisionType );
    DecisionConfiguration decisionConfiguration = configurationMap.get( nodeId );
    when( decisionInstance.decide() ).thenReturn( decisionConfiguration.decision, decisionConfiguration.decisions );
  }

  private static <T> T spyNode( NodeLoader nodeLoader, Class<T> type ) {
    T result = mock( type );
    when( nodeLoader.load( eq( type ), any( WorkflowContext.class ) ) ).thenReturn( result );
    return result;
  }
}