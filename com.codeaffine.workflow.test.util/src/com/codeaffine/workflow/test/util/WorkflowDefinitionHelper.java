package com.codeaffine.workflow.test.util;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import com.codeaffine.workflow.definition.Activity;
import com.codeaffine.workflow.definition.Decision;
import com.codeaffine.workflow.definition.Matcher;
import com.codeaffine.workflow.definition.Task;
import com.codeaffine.workflow.definition.VariableDeclaration;
import com.codeaffine.workflow.definition.WorkflowDefinition;
import com.codeaffine.workflow.internal.WorkflowDefinitionImpl;

@SuppressWarnings("rawtypes")
public class WorkflowDefinitionHelper {

  public static final String START = "start";
  public static final String DECISION_ID = "decision";
  public static final String OPERATION_ID = "operationId";
  public static final String OPERATION_ID_1 = "operationId1";
  public static final String OPERATION_ID_2 = "operationId2";
  public static final VariableDeclaration<Object> NAME = new VariableDeclaration<Object>( "name", Object.class );
  public static final VariableDeclaration<String> VAR_DECL = new VariableDeclaration<String>( "varDecl", String.class );
  public static final VariableDeclaration<List> VAR_LIST = new VariableDeclaration<List>( "list", List.class );
  public static final String VALUE = "value";

  private final WorkflowDefinitionImpl definition;

  public static class TestActivity implements Activity {

    @Override
    public void execute() {}
  }

  public static class TestTask implements Task {

    @Override
    public String getDescription() {
      return "Task for testing purposes";
    }
  }

  public static class TestDecision implements Decision {

    @Override
    public String decide() {
      return WorkflowDefinitionHelper.OPERATION_ID;
    }
  }

  public static WorkflowDefinition newInstance() {
    return new WorkflowDefinitionImpl();
  }

  public WorkflowDefinitionHelper() {
    this.definition = ( WorkflowDefinitionImpl )newInstance();
  }

  public WorkflowDefinition getDefinition() {
    return definition;
  }

  public void addActivityNode( String nodeId, Class<? extends Activity> type, String successor ) {
    definition.addActivity( nodeId, type, successor );
  }

  private void addTaskNode( String nodeId, Class<? extends Task> type, String successor ) {
    definition.addTask( nodeId, type, successor );
  }

  public void addDecisionNode(
    String nodeId, Class<? extends Decision> decision, String successor1, String successor2, String ... successors )
  {
    definition.addDecision( nodeId, decision, successor1, successor2, successors );
  }

  public void setStart( String nodeId ) {
    definition.setStart( nodeId );
  }

  public void registerMatcherFor( Object toMatch ) {
    Matcher matcher = mock( Matcher.class );
    when( Boolean.valueOf( matcher.matches( toMatch ) ) ).thenReturn( Boolean.TRUE );
    definition.setMatcher( matcher );
  }

  public void addStartNodeWithOperation( String successor ) {
    addActivityNode( START, TestActivity.class, successor );
    setStart( START );
  }

  public void addStartNodeWithTask( String successor ) {
    addTaskNode( START, TestTask.class, successor );
    setStart( START );
  }

  public void addStartNodeWithDecision() {
    addDecisionNode( START, TestDecision.class, OPERATION_ID, OPERATION_ID_1, OPERATION_ID_2 );
    setStart( START );
  }
}