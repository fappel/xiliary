package com.codeaffine.workflow;

import java.util.List;
import java.util.UUID;

public interface TaskList {
  List<Task> snapshot();
  List<Task> snapshot( TaskPredicate filter );
  UUID acquireAssignment( Task task );
  void dropAssigment( UUID assignmentToken );
  boolean isAssigned( Task task );
  boolean complete( UUID assignmentToken );
  boolean isEmpty();
  int size();
}
