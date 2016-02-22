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
package com.codeaffine.workflow.persistence;

import static com.codeaffine.workflow.WorkflowContext.VARIABLE_SERVICE;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

import com.codeaffine.workflow.NodeLoader;
import com.codeaffine.workflow.WorkflowService;
import com.codeaffine.workflow.WorkflowServiceFactory;
import com.codeaffine.workflow.definition.VariableDeclaration;

public class WorkflowServiceAdapterTest {

  private WorkflowServiceAdapter serviceAdapter;

  @Before
  public void setUp() {
    WorkflowService service = new WorkflowServiceFactory().createWorkflowService();
    serviceAdapter = new WorkflowServiceAdapter( service );
  }

  @Test
  public void getClassFinder() {
    ClassFinder actual = serviceAdapter.getClassFinder();

    assertThat( actual ).isNotNull();
  }

  @Test
  public void getNodeLoader() {
    NodeLoader actual = serviceAdapter.getNodeLoader();

    assertThat( actual ).isNotNull();
  }

  @Test
  public void getVariableDeclarations() {
    VariableDeclaration<?>[] actual = serviceAdapter.getVariableDeclarations();

    assertThat( actual ).containsExactly( VARIABLE_SERVICE );
  }
}