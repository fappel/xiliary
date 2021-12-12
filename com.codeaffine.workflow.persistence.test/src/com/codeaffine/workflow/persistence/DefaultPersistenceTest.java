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
package com.codeaffine.workflow.persistence;

import static com.codeaffine.workflow.WorkflowContext.VARIABLE_SERVICE;
import static com.codeaffine.workflow.test.util.PersistenceTestHelper.VALUE;
import static com.codeaffine.workflow.test.util.PersistenceTestHelper.VAR_LIST;
import static com.codeaffine.workflow.test.util.PersistenceTestHelper.VAR_RESULT;
import static com.codeaffine.workflow.test.util.PersistenceTestHelper.VAR_STRING;
import static com.codeaffine.workflow.test.util.PersistenceTestHelper.createNodeLoader;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.Test;

import com.codeaffine.workflow.Workflow;
import com.codeaffine.workflow.WorkflowService;
import com.codeaffine.workflow.test.util.PersistenceTestHelper;
import com.codeaffine.workflow.test.util.PersistenceTestTask;
import com.codeaffine.workflow.test.util.TestClassFinder;

@SuppressWarnings( "unchecked" )
public class DefaultPersistenceTest {

  private static final List<String> LIST_VALUE = createListVariable( VALUE );

  @Test
  public void saveAndRestore() {
    PersistenceTestHelper serializer = createTestHelper();
    Workflow workflow = serializer.createWorkflow();
    workflow.defineVariable( VAR_STRING, VALUE );
    workflow.defineVariable( VAR_LIST, LIST_VALUE );
    workflow.start();
    UUID token = serializer.acquireAssignment();

    PersistenceTestHelper deserializer = createTestHelper();
    deserializer.deserialize( serializer.serialize() );
    PersistenceTestTask task = deserializer.getTask();
    boolean actualIsCompleted = task.complete( token );
    String actualStringVariable = task.getContext().getVariableValue( VAR_RESULT );
    List<String> actualListVariable = task.getContext().getVariableValue( VAR_LIST );
    WorkflowService actualService = task.getContext().getVariableValue( VARIABLE_SERVICE );

    assertThat( actualService).isNotNull();
    assertThat( actualIsCompleted ).isTrue();
    assertThat( actualStringVariable ).isEqualTo( VALUE );
    assertThat( actualListVariable )
      .isEqualTo( LIST_VALUE )
      .isNotSameAs( LIST_VALUE );

  }

  private static List<String> createListVariable( String element ) {
    List<String> result = new ArrayList<String>();
    result.add( element );
    return result;
  }

  private static PersistenceTestHelper createTestHelper() {
    PersistenceTestHelper result = new PersistenceTestHelper();
    DefaultPersistence defaultPersistence = new DefaultPersistence( result, createNodeLoader(), new TestClassFinder() );
    result.setPersistence( defaultPersistence );
    return result;
  }
}