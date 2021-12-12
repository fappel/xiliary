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
package com.codeaffine.workflow.test.util;

import static com.codeaffine.workflow.WorkflowContext.VARIABLE_CONTEXT;
import static com.codeaffine.workflow.WorkflowContext.VARIABLE_TASK_LIST;
import static com.codeaffine.workflow.WorkflowContexts.lookup;
import static com.codeaffine.workflow.test.util.PersistenceTestHelper.VAR_STRING;
import static com.codeaffine.workflow.test.util.PersistenceTestHelper.VAR_RESULT;

import java.util.UUID;

import com.codeaffine.workflow.TaskList;
import com.codeaffine.workflow.WorkflowContext;
import com.codeaffine.workflow.definition.Task;

public class PersistenceTestTask implements Task {

  private final WorkflowContext context = lookup( VARIABLE_CONTEXT );
  private final TaskList taskList = lookup( VARIABLE_TASK_LIST );
  private final String name = lookup( VAR_STRING );

  @Override
  public String getDescription() {
    return "Description of: " + PersistenceTestTask.class.getName();
  }

  public boolean complete( UUID token ) {
    context.defineVariable( VAR_RESULT, name );
    return taskList.complete( token );
  }

  public WorkflowContext getContext() {
    return context;
  }
}