package com.codeaffine.workflow.internal;

import static com.codeaffine.workflow.WorkflowContext.VARIABLE_SERVICE;
import static java.lang.String.format;

import java.util.HashMap;
import java.util.Map;

import com.codeaffine.workflow.NodeLoader;
import com.codeaffine.workflow.TaskList;
import com.codeaffine.workflow.Workflow;
import com.codeaffine.workflow.WorkflowFactory;
import com.codeaffine.workflow.WorkflowService;
import com.codeaffine.workflow.definition.VariableDeclaration;
import com.codeaffine.workflow.definition.WorkflowDefinitionProvider;
import com.codeaffine.workflow.event.FlowListener;
import com.codeaffine.workflow.event.TaskListener;
import com.codeaffine.workflow.persistence.ClassFinder;
import com.codeaffine.workflow.persistence.Memento;

public class WorkflowServiceImpl implements WorkflowService {

  static final String ERROR_DEFINITON_NOT_FOUND = "Could not find definintion with id <%s>.";

  private final Map<String, WorkflowDefinitionImpl> definitions;
  private final FlowEventNotifier flowEventNotifier;
  private final TaskEventNotifier taskEventNotifier;
  private final DefinitionFactory definitionFactory;
  private final ClassFinderImpl classFinderImpl;
  private final WorkflowFactory flowFactory;
  private final ScopeImpl serviceScope;
  private final TaskListImpl taskList;
  private final NodeLoader nodeLoader;

  public WorkflowServiceImpl() {
    this( new NodeLoaderImpl() );
  }

  public WorkflowServiceImpl( NodeLoader nodeLoader ) {
    this.definitionFactory = new DefinitionFactory();
    this.flowEventNotifier = new FlowEventNotifier();
    this.taskEventNotifier = new TaskEventNotifier();
    this.taskList = new TaskListImpl( taskEventNotifier );
    this.nodeLoader = nodeLoader;
    this.definitions = new HashMap<String, WorkflowDefinitionImpl>();
    this.classFinderImpl = new ClassFinderImpl( definitions );
    this.serviceScope = new ScopeImpl();
    this.flowFactory = new WorkflowFactoryImpl( serviceScope, definitions, taskList, flowEventNotifier, nodeLoader );
    defineVariable( VARIABLE_SERVICE, this );
  }

  @Override
  public Workflow create( String id ) {
    synchronized( definitions ) {
      return checkExistence( flowFactory.create( id ), id );
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
  public <T> T defineVariable( VariableDeclaration<T> declaration, T value ) {
    return serviceScope.defineVariable( declaration, value );
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
      WorkflowDefinitionImpl definition = definitionFactory.defineWorkflow( definitionProvider );
      definitions.put( definition.getId(), definition );
    }
  }

  public void removeWorkflowDefinition( WorkflowDefinitionProvider definitionProvider ) {
    synchronized( definitions ) {
      WorkflowDefinitionImpl definition = definitionFactory.defineWorkflow( definitionProvider );
      definitions.remove( definition.getId() );
    }
  }

  public NodeLoader getNodeLoader() {
    return nodeLoader;
  }

  public ClassFinder getClassFinder() {
    return classFinderImpl;
  }

  public ScopeImpl getServiceScope() {
    return serviceScope;
  }

  private static Workflow checkExistence( Workflow result, String id ) {
    if( result == null ) {
      throw new IllegalArgumentException( format( ERROR_DEFINITON_NOT_FOUND, id ) );
    }
    return result;
  }
}