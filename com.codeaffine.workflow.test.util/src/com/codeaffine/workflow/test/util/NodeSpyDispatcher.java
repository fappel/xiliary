/**
 * Copyright (c) 2014 - 2016 Frank Appel
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Frank Appel - initial API and implementation
 */
package com.codeaffine.workflow.test.util;

import java.util.Map;

import com.codeaffine.workflow.definition.Activity;
import com.codeaffine.workflow.definition.Decision;
import com.codeaffine.workflow.internal.WorkflowDefinitionImpl;

class NodeSpyDispatcher extends WorkflowDefinitionImpl {

  private final Map<String, DecisionConfiguration> decisionConfiguration;
  private final NodeLoaderHelper helper;

  NodeSpyDispatcher( NodeLoaderHelper helper, Map<String, DecisionConfiguration> decisionConfiguration ) {
    this.helper = helper;
    this.decisionConfiguration = decisionConfiguration;
  }

  @Override
  public void addActivity( String nodeId, Class<? extends Activity> activity, String successor ) {
    helper.spyActivity( activity );
  }

  @Override
  public void addDecision(
    String nodeId, Class<? extends Decision> decision, String successor1, String successor2, String... successors )
  {
    helper.spyDecision( nodeId, decision, decisionConfiguration );
  }
}