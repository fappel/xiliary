package com.codeaffine.workflow.test.util;

import static com.codeaffine.workflow.test.util.WorkflowDefinitionHelper.OPERATION_ID;
import static org.mockito.Mockito.mock;

import java.util.List;
import java.util.UUID;

import com.codeaffine.workflow.NodeLoader;
import com.codeaffine.workflow.Task;
import com.codeaffine.workflow.VariableDeclaration;
import com.codeaffine.workflow.Workflow;
import com.codeaffine.workflow.WorkflowDefinition;
import com.codeaffine.workflow.WorkflowFactory;
import com.codeaffine.workflow.internal.FlowEventNotifier;
import com.codeaffine.workflow.internal.NodeLoaderImpl;
import com.codeaffine.workflow.internal.OperationPointer;
import com.codeaffine.workflow.internal.TaskEventNotifier;
import com.codeaffine.workflow.internal.TaskListImpl;
import com.codeaffine.workflow.internal.WorkflowImpl;
import com.codeaffine.workflow.persistence.Persistence;

@SuppressWarnings( "rawtypes" )
public class PersistenceTestHelper implements WorkflowFactory {

  public static final String WORKFLOW_DEFINITION_ID = PersistenceTestHelper.class.getName();
  public static final String VALUE = "value";
  public static final VariableDeclaration<List> VAR_LIST = new VariableDeclaration<List>( "list", List.class );
  public static final VariableDeclaration<String> VAR_STRING = new VariableDeclaration<String>( "name", String.class );
  public static final VariableDeclaration<String> VAR_RESULT = new VariableDeclaration<String>( "result", String.class );

  private final WorkflowDefinition definition;
  private final FlowEventNotifier notifier;
  private final TaskListImpl taskList;

  private Persistence persistence;

  public PersistenceTestHelper() {
    this.definition = createDefinition();
    this.taskList = new TaskListImpl( mock( TaskEventNotifier.class ) );
    this.notifier = new FlowEventNotifier();
  }

  public void setPersistence( Persistence persistence ) {
    this.persistence = persistence;
  }

  @Override
  public Workflow create( String definitionId ) {
    if( definitionId.equals( definition.getId() ) ) {
      return new WorkflowImpl( definition, taskList, notifier, createNodeLoader() );
    }
    return null;
  }

  public Workflow createWorkflow() {
    return create( WORKFLOW_DEFINITION_ID );
  }

  OperationPointer createOperationPointer() {
    return new OperationPointer( new NodeLoaderImpl(), notifier, createWorkflow().getContext(), definition );
  }

  public static NodeLoader createNodeLoader() {
    return new NodeLoaderImpl();
  }

  public UUID acquireAssignment() {
    return taskList.acquireAssignment( taskList.snapshot().get( 0 ) );
  }

  public PersistenceTestTask getTask() {
    List<Task> snapshot = taskList.snapshot();
    if( snapshot.size() != 1 ) {
      throw new IllegalStateException( "Operation only allowed if workflow has been suspended on task" );
    }
    return ( PersistenceTestTask )snapshot.get( 0 );
  }

  public Object serialize() {
    return persistence.serialize( taskList.save() );
  }

  public void deserialize( Object data ) {
    taskList.restore( persistence.deserialize( data ) );
  }

  private static WorkflowDefinition createDefinition() {
    WorkflowDefinition result = WorkflowDefinitionHelper.newInstance();
    result.addTask( OPERATION_ID, PersistenceTestTask.class, null );
    result.setStart( OPERATION_ID );
    result.setId( WORKFLOW_DEFINITION_ID );
    return result;
  }
}