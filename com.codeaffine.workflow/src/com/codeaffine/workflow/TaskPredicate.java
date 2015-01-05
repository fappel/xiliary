package com.codeaffine.workflow;

import com.codeaffine.workflow.definition.Task;

public interface TaskPredicate {

  boolean apply( Task input );
}