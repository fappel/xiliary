package com.codeaffine.workflow;

public interface TaskPredicate {

  boolean apply( Task input );
}