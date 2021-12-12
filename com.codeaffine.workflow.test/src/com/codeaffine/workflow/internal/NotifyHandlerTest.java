/**
 * Copyright (c) 2014 - 2022 Frank Appel
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Frank Appel - initial API and implementation
 */
package com.codeaffine.workflow.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import com.codeaffine.workflow.definition.VariableDeclaration;
import com.codeaffine.workflow.event.WorkflowContextEvent;
import com.codeaffine.workflow.event.WorkflowContextListener;

public class NotifyHandlerTest {

  @SuppressWarnings( "rawtypes" )
  private ArgumentCaptor<WorkflowContextEvent> captor;
  private VariableDeclaration<Runnable> declaration;
  private WorkflowContextListener listener;

  @Before
  public void setUp() {
    declaration = new VariableDeclaration<Runnable>( "name", Runnable.class );
    listener = mock( WorkflowContextListener.class );
    captor = forClass( WorkflowContextEvent.class );
  }

  @Test
  public void triggerVariableChanged() {
    Runnable newValue = mock( Runnable.class );
    Runnable oldValue = mock( Runnable.class );
    NotifyHandler<Runnable> notifyHandler = createHandler( newValue, oldValue );

    notifyHandler.triggerVariableChanged();

    verify( listener ).variableChanged( captor.capture() );
    assertThat( captor.getValue().getDeclaration() ).isSameAs( declaration );
    assertThat( captor.getValue().getOldValue() ).isSameAs( oldValue );
    assertThat( captor.getValue().getNewValue() ).isSameAs( newValue );
  }

  @Test
  public void triggerVariableChangedToNull() {
    Runnable oldValue = mock( Runnable.class );
    NotifyHandler<Runnable> notifyHandler = createHandler( null, oldValue );

    notifyHandler.triggerVariableChanged();

    verify( listener ).variableChanged( captor.capture() );
    assertThat( captor.getValue().getDeclaration() ).isSameAs( declaration );
    assertThat( captor.getValue().getOldValue() ).isSameAs( oldValue );
    assertThat( captor.getValue().getNewValue() ).isNull();
  }

  @Test
  public void triggerVariableChangedIfValuesAreEquals() {
    Runnable sameValue = mock( Runnable.class );
    NotifyHandler<Runnable> notifyHandler = createHandler( sameValue, sameValue );

    notifyHandler.triggerVariableChanged();

    verify( listener, never() ).variableChanged( any( WorkflowContextEvent.class ) );
  }

  @Test
  public void triggerVariableChangedIfBothValuesAreNull() {
    NotifyHandler<Runnable> notifyHandler = createHandler( null, null );

    notifyHandler.triggerVariableChanged();

    verify( listener, never() ).variableChanged( any( WorkflowContextEvent.class ) );
  }

  private NotifyHandler<Runnable> createHandler( Runnable newValue, Runnable oldValue ) {
    return new NotifyHandler<Runnable>( declaration, newValue, oldValue, listener );
  }
}
