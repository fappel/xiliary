package com.codeaffine.workflow.internal;

import static com.codeaffine.workflow.internal.ArgumentVerification.verifyCondition;
import static com.codeaffine.workflow.internal.ArgumentVerification.verifyNotNull;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import com.codeaffine.workflow.WorkflowContext;
import com.codeaffine.workflow.definition.VariableDeclaration;
import com.codeaffine.workflow.event.WorkflowContextListener;
import com.codeaffine.workflow.persistence.WorkflowContextMemento;

public class WorkflowContextImpl implements WorkflowContext {

  static final String NO_DECLARATION = "Variable declaration <%s> does not exist.";

  private final Map<VariableDeclaration<?>, Object> content;
  private final Collection<WorkflowContextListener> listeners;

  public WorkflowContextImpl() {
    this.content = new HashMap<VariableDeclaration<?>, Object>();
    this.listeners = new HashSet<WorkflowContextListener>();
  }

  @Override
  public <T> void defineVariable( VariableDeclaration<T> declaration, T value ) {
    verifyNotNull( declaration, "declaration" );

    NotifyHandler<T> notifyHandler = doSetVariable( declaration, value );
    notifyHandler.triggerVariableChanged();
  }

  @Override
  public boolean hasVariableDefinition( VariableDeclaration<?> declaration ) {
    verifyNotNull( declaration, "declaration" );

    synchronized( content ) {
      return content.get( declaration ) != null;
    }
  }

  @Override
  public VariableDeclaration<?>[] getVariableDeclarations() {
    synchronized( content ) {
      return content.keySet().toArray( new VariableDeclaration<?>[ content.size() ] );
    }
  }

  @Override
  public void addWorkflowContextListener( WorkflowContextListener workflowContextListener ) {
    listeners.add( workflowContextListener );
  }

  @Override
  public void removeWorkflowContextListener( WorkflowContextListener workflowContextListener ) {
    listeners.remove( workflowContextListener );
  }

  public WorkflowContextMemento save() {
    Map<VariableDeclaration<?>, Object> copy = new HashMap<VariableDeclaration<?>, Object>();
    copy.putAll( content );
    return new WorkflowContextMemento( copy );
  }

  public void restore( WorkflowContextMemento memento ) {
    content.clear();
    content.putAll( memento.getContent() );
  }

  private WorkflowContextListener[] getListeners() {
    return listeners.toArray( new WorkflowContextListener[ listeners.size() ] );
  }

  @SuppressWarnings( "unchecked" )
  private <T> NotifyHandler<T> doSetVariable( VariableDeclaration<T> declaration, T value ) {
    synchronized( content ) {
      T oldValue = ( T )content.get( declaration );
      content.put( declaration, value );
      return new NotifyHandler<T>( declaration, value, oldValue, getListeners() );
    }
  }

  @Override
  @SuppressWarnings( "unchecked" )
  public <T> T getVariableValue( VariableDeclaration<T> declaration ) {
    verifyNotNull( declaration, "declaration" );
    synchronized( content ) {
      verifyCondition( content.containsKey( declaration ), NO_DECLARATION, declaration.toString() );

      return ( T )content.get( declaration );
    }
  }
}