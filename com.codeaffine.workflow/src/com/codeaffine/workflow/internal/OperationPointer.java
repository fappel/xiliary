package com.codeaffine.workflow.internal;

import com.codeaffine.workflow.NodeDefinition;
import com.codeaffine.workflow.NodeLoader;
import com.codeaffine.workflow.WorkflowContext;
import com.codeaffine.workflow.definition.Decision;
import com.codeaffine.workflow.definition.WorkflowDefinition;
import com.codeaffine.workflow.persistence.OperationPointerMemento;

public class OperationPointer {

  static final String ERROR_UNAVAILABLE_OPERATION = "Must not call acquire on undefined operation pointer";
  static final String ERROR_ILLEGAL_MOVE = "Must not move on before current node has been acquired.";

  private final WorkflowDefinitionImpl definition;
  private final WorkflowContext context;
  private final FlowEventNotifier notifier;
  private final NodeLoader nodeLoader;

  private NodeDefinition currentNode;
  private boolean initialized;
  private boolean acquired;

  public OperationPointer(
    NodeLoader loader, FlowEventNotifier notifier, WorkflowContext context, WorkflowDefinition definition )
  {
    this.nodeLoader = loader;
    this.context = context;
    this.definition = ( WorkflowDefinitionImpl )definition;
    this.notifier = notifier;
  }

  boolean isAvailable() {
    return hasCurrentNode() && !acquired;
  }

  public NodeDefinition acquire() {
    checkAvailability();

    acquired = true;
    return currentNode;
  }

  public void move() {
    checkAcquirementState();

    ensureInitialization();
    while( canMove() ) {
      acquired = false;
      currentNode = getSuccessor( currentNode );
    }
  }

  public OperationPointerMemento save() {
    return new OperationPointerMemento( currentNode, initialized, acquired ) ;
  }

  public void restore( OperationPointerMemento memento ) {
    currentNode = memento.getCurrentNode();
    initialized = memento.isInitialized();
    acquired = memento.isAcquired();
  }

  private void checkAcquirementState() {
    if( initialized && !acquired ) {
      throw new IllegalStateException( ERROR_ILLEGAL_MOVE );
    }
  }

  private void ensureInitialization() {
    if( !initialized ) {
      currentNode = definition.getStartNode();
      notifier.notifyOnNodeEnter( currentNode );
      initialized = true;
    }
  }

  private boolean canMove() {
    return hasCurrentNode() && ( !isOperation( currentNode ) || acquired );
  }

  private boolean hasCurrentNode() {
    return currentNode != null;
  }

  private NodeDefinition getSuccessor( NodeDefinition nodeDefinition ) {
    notifier.notifyOnNodeLeave( currentNode );
    String successor = getSuccessorId( nodeDefinition );
    NodeDefinition result = definition.getNode( successor );
    notifier.notifyOnNodeEnter( result );
    return result;
  }

  private String getSuccessorId( NodeDefinition nodeDefinition ) {
    if( isOperation( nodeDefinition ) ) {
      return getOperationSuccessorId( nodeDefinition );
    }
    return getDecisionSuccessorId( nodeDefinition );
  }

  private static String getOperationSuccessorId( NodeDefinition nodeDefinition ) {
    if( nodeDefinition.getSuccessors().length == 1 ) {
      return nodeDefinition.getSuccessors()[ 0 ];
    }
    return null;
  }

  private String getDecisionSuccessorId( NodeDefinition nodeDefinition ) {
    Decision decision = ( Decision )nodeLoader.load( nodeDefinition.getType(), context );
    return decision.decide();
  }

  private static boolean isOperation( NodeDefinition nodeDefinition ) {
    return !Decision.class.isAssignableFrom( nodeDefinition.getType() );
  }

  private void checkAvailability() {
    if( !isAvailable() ) {
      throw new IllegalStateException( ERROR_UNAVAILABLE_OPERATION );
    }
  }
}