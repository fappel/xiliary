package com.codeaffine.workflow.internal;

import static com.codeaffine.workflow.WorkflowContext.VARIABLE_SERVICE;

import java.util.HashMap;
import java.util.Map;

import com.codeaffine.workflow.NodeLoader;
import com.codeaffine.workflow.TaskList;
import com.codeaffine.workflow.Workflow;
import com.codeaffine.workflow.WorkflowService;
import com.codeaffine.workflow.definition.WorkflowDefinitionProvider;
import com.codeaffine.workflow.event.FlowListener;
import com.codeaffine.workflow.event.TaskListener;
import com.codeaffine.workflow.persistence.ClassFinder;
import com.codeaffine.workflow.persistence.Memento;

public class WorkflowServiceImpl implements WorkflowService {

  private final WorkflowDefinitionChecker definitionChecker;
  private final Map<String, WorkflowDefinitionImpl> definitions;
  private final FlowEventNotifier flowEventNotifier;
  private final TaskEventNotifier taskEventNotifier;
  private final ClassFinderImpl classFinderImpl;
  private final TaskListImpl taskList;
  private final NodeLoader nodeLoader;

  public WorkflowServiceImpl() {
    this( new NodeLoaderImpl() );
  }

  public WorkflowServiceImpl( NodeLoader nodeLoader ) {
    this.definitionChecker = new WorkflowDefinitionChecker();
    this.flowEventNotifier = new FlowEventNotifier();
    this.taskEventNotifier = new TaskEventNotifier();
    this.taskList = new TaskListImpl( taskEventNotifier );
    this.nodeLoader = nodeLoader;
    this.definitions = new HashMap<String, WorkflowDefinitionImpl>();
    this.classFinderImpl = new ClassFinderImpl( definitions );
  }

  @Override
  public Workflow create( String id ) {
    synchronized( definitions ) {
      return doCreate( id );
    }
  }

  @Override
  public String[] getWorkflowDefinitionIds() {
    synchronized( definitions ) {
      return definitions.keySet().toArray( new String[ definitions.keySet().size() ]);
    }
  }

  @Override
  public TaskList getTaskList() {
    return taskList;
  }

  @Override
  public Memento save() {
    return taskList.save();
  }

  @Override
  public void restore( Memento memento ) {
    taskList.restore( memento );
  }

  @Override
  public void addFlowListener( FlowListener flowListener ) {
    flowEventNotifier.addFlowListener( flowListener );
  }

  @Override
  public void removeFlowListener( FlowListener flowListener ) {
    flowEventNotifier.removeFlowListener( flowListener );
  }

  @Override
  public void addTaskListener( TaskListener taskListener ) {
    taskEventNotifier.addTaskListener( taskListener );
  }

  @Override
  public void removeTaskListener( TaskListener taskListener ) {
    taskEventNotifier.removeTaskListener( taskListener );
  }

  public void addWorkflowDefinition( WorkflowDefinitionProvider definitionProvider ) {
    synchronized( definitions ) {
      WorkflowDefinitionImpl definition = defineWorkflow( definitionProvider );
      definitions.put( definition.getId(), definition );
    }
  }

  public void removeWorkflowDefinition( WorkflowDefinitionProvider definitionProvider ) {
    synchronized( definitions ) {
      WorkflowDefinitionImpl definition = defineWorkflow( definitionProvider );
      definitions.remove( definition.getId() );
    }
  }

  public NodeLoader getNodeLoader() {
    return nodeLoader;
  }

  public ClassFinder getClassFinder() {
    return classFinderImpl;
  }

  private WorkflowDefinitionImpl defineWorkflow( WorkflowDefinitionProvider definitionProvider ) {
    WorkflowDefinitionImpl result = new WorkflowDefinitionImpl();
    definitionProvider.define( result );
    definitionChecker.checkDefinition( result );
    return result;
  }

  private Workflow doCreate( String id ) {
    WorkflowImpl result = null;
    if( definitions.get( id ) != null ) {
      result = new WorkflowImpl( definitions.get( id ), taskList, flowEventNotifier, nodeLoader );
      result.defineVariable( VARIABLE_SERVICE, this );
    }
    return result;
  }
}