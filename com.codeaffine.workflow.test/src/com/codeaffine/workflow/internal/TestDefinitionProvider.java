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
package com.codeaffine.workflow.internal;

import static com.codeaffine.workflow.test.util.WorkflowDefinitionHelper.OPERATION_ID;

import com.codeaffine.workflow.definition.WorkflowDefinition;
import com.codeaffine.workflow.definition.WorkflowDefinitionProvider;
import com.codeaffine.workflow.test.util.WorkflowDefinitionHelper.TestTask;

public class TestDefinitionProvider implements WorkflowDefinitionProvider {

  @Override
  public void define( WorkflowDefinition definition ) {
    definition.setId( getClass().getName() );
    definition.addTask( OPERATION_ID, TestTask.class, null );
    definition.setStart( OPERATION_ID );
  }
}