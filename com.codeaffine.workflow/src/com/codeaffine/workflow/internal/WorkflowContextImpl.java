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
package com.codeaffine.workflow.internal;

import java.util.Collection;
import java.util.HashSet;

import com.codeaffine.workflow.WorkflowContext;
import com.codeaffine.workflow.definition.VariableDeclaration;
import com.codeaffine.workflow.event.WorkflowContextListener;
import com.codeaffine.workflow.persistence.WorkflowContextMemento;

public class WorkflowContextImpl implements WorkflowContext {

  private final Collection<WorkflowContextListener> listeners;
  private final ScopeImpl content;

  public WorkflowContextImpl() {
    content = new ScopeImpl();
    listeners = new HashSet<WorkflowContextListener>();
  }

  @Override
  public <T> T defineVariable( VariableDeclaration<T> declaration, T value ) {
    T result = content.defineVariable( declaration, value );
    NotifyHandler<T> notifyHandler = new NotifyHandler<T>( declaration, value, result, getListeners() );
    notifyHandler.triggerVariableChanged();
    return result;
  }

  @Override
  public boolean hasVariableDefinition( VariableDeclaration<?> declaration ) {
    return content.hasVariableDefinition( declaration );
  }

  @Override
  public VariableDeclaration<?>[] getVariableDeclarations() {
    return content.getVariableDeclarations();
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
    return new WorkflowContextMemento( content.getContent() );
  }

  public void restore( WorkflowContextMemento memento ) {
    content.setContent( memento.getContent() );
  }

  private WorkflowContextListener[] getListeners() {
    return listeners.toArray( new WorkflowContextListener[ listeners.size() ] );
  }

  @Override
  public <T> T getVariableValue( VariableDeclaration<T> declaration ) {
    return content.getVariableValue( declaration );
  }
}