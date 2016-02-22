/**
 * Copyright (c) 2014 - 2016 Frank Appel
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Frank Appel - initial API and implementation
 */
package com.codeaffine.workflow;

import java.util.List;
import java.util.UUID;

import com.codeaffine.workflow.definition.Task;

public interface TaskList {
  List<Task> snapshot();
  List<Task> snapshot( TaskPredicate filter );
  Workflow getWorkflow( Task task );
  UUID acquireAssignment( Task task );
  void dropAssigment( UUID assignmentToken );
  boolean isAssigned( Task task );
  boolean complete( UUID assignmentToken );
  boolean isEmpty();
  int size();
}
