package com.codeaffine.workflow.test.util;

import java.util.Map;

import com.codeaffine.workflow.Activity;
import com.codeaffine.workflow.Decision;
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