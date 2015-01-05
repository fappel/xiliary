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