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

import static com.codeaffine.workflow.test.util.WorkflowDefinitionHelper.VALUE;
import static com.codeaffine.workflow.test.util.WorkflowDefinitionHelper.VAR_DECL;
import static com.codeaffine.workflow.test.util.WorkflowDefinitionHelper.VAR_LIST;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.codeaffine.workflow.definition.VariableDeclaration;
import com.codeaffine.workflow.event.WorkflowContextEvent;
import com.codeaffine.workflow.event.WorkflowContextListener;
import com.codeaffine.workflow.persistence.WorkflowContextMemento;

@SuppressWarnings( "unchecked" )
public class WorkflowContextImplTest {

  private static final String NEW_VALUE = "newValue";

  private WorkflowContextImpl context;

  @Before
  public void setUp() {
    context = new WorkflowContextImpl();
  }

  @Test
  public void defineVariable() {
    String oldValue = context.defineVariable( VAR_DECL, VALUE );
    String actual = context.getVariableValue( VAR_DECL );

    assertThat( oldValue ).isNull();
    assertThat( actual ).isSameAs( VALUE );
  }

  @Test
  public void hasVariableDefinition() {
    context.defineVariable( VAR_DECL, VALUE );

    boolean actual = context.hasVariableDefinition( VAR_DECL );

    assertThat( actual ).isTrue();
  }

  @Test
  public void hasVariableDefinitionIfNotDefined() {
    boolean actual = context.hasVariableDefinition( VAR_DECL );

    assertThat( actual ).isFalse();
  }

  @Test
  public void defineVariableWithRegisteredListener() {
    WorkflowContextListener listener = mock( WorkflowContextListener.class );
    context.addWorkflowContextListener( listener );

    context.defineVariable( VAR_DECL, VALUE );

    verify( listener ).variableChanged( any( WorkflowContextEvent.class ) );
  }

  @Test
  public void defineVariableAfterListenerRemoval() {
    WorkflowContextListener listener = mock( WorkflowContextListener.class );
    context.addWorkflowContextListener( listener );

    context.removeWorkflowContextListener( listener );
    context.defineVariable( VAR_DECL, VALUE );

    verify( listener, never() ).variableChanged( any( WorkflowContextEvent.class ) );
  }

  @Test
  public void defineVariableThatAlreadyExists() {
    context.defineVariable( VAR_DECL, VALUE );

    String oldValue = context.defineVariable( VAR_DECL, NEW_VALUE );
    String actual = context.getVariableValue( VAR_DECL );

    assertThat( oldValue ).isSameAs( VALUE );
    assertThat( actual ).isSameAs( NEW_VALUE );
  }

  @Test
  public void getVariableDeclarations() {
    context.defineVariable( VAR_DECL, VALUE );

    VariableDeclaration<?>[] actual = context.getVariableDeclarations();

    assertThat( actual )
      .hasSize( 1 )
      .contains( VAR_DECL );
  }

  @Test
  public void defineVariableWithNull() {
    context.defineVariable( VAR_DECL, null );

    String actual = context.getVariableValue( VAR_DECL );

    assertThat( actual ).isNull();
  }

  @Test( expected = IllegalArgumentException.class )
  public void getUndeclaredVariable() {
    context.getVariableValue( VAR_DECL );
  }

  @Test( expected = IllegalArgumentException.class )
  public void getVariableValueWithNullParameter() {
    context.getVariableValue( null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void defineVariableWithNullParameter() {
    context.defineVariable( null, VALUE );
  }

  @Test
  public void saveAndRestore() {
    List<String> expected = createListWithValue();
    context.defineVariable( VAR_LIST, expected );

    WorkflowContextMemento memento = context.save();
    List<String> actual = restore( memento, new WorkflowContextImpl() );

    assertThat( actual ).isSameAs( expected );
  }

  @Test
  public void changeContextAfterSave() {
    List<String> expected = createListWithValue();
    context.defineVariable( VAR_LIST, expected );

    WorkflowContextMemento memento = context.save();
    context.defineVariable( VAR_DECL, VALUE );
    List<String> actual = restore( memento, new WorkflowContextImpl() );

    assertThat( actual ).hasSize( 1 );
  }

  @Test
  public void changeContextBeforeRestore() {
    List<String> expected = createListWithValue();
    context.defineVariable( VAR_LIST, expected );

    WorkflowContextMemento memento = context.save();
    WorkflowContextImpl newContext = new WorkflowContextImpl();
    newContext.defineVariable( VAR_DECL, VALUE );
    List<String> actual = restore( memento, newContext );

    assertThat( actual ).hasSize( 1 );
  }

  private static List<String> createListWithValue() {
    List<String> result = new ArrayList<String>();
    result.add( VALUE );
    return result;
  }

  private static List<String> restore( WorkflowContextMemento memento, WorkflowContextImpl newContext ) {
    newContext.restore( memento );
    return newContext.getVariableValue( VAR_LIST );
  }
}