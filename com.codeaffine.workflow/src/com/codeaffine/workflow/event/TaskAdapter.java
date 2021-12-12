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
package com.codeaffine.workflow.event;

public class TaskAdapter implements TaskListener {

  @Override
  public void taskCreated( TaskEvent event ) {
  }

  @Override
  public void taskAssigned( TaskEvent event ) {
  }

  @Override
  public void taskAssignmentDropped( TaskEvent event ) {
  }

  @Override
  public void taskCompleted( TaskEvent event ) {
  }
}