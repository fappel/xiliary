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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;

import com.codeaffine.workflow.NodeLoader;
import com.codeaffine.workflow.WorkflowContext;
import com.codeaffine.workflow.test.util.WorkflowDefinitionHelper.TestActivity;
import com.codeaffine.workflow.test.util.WorkflowDefinitionHelper.TestDecision;
import com.codeaffine.workflow.test.util.WorkflowDefinitionHelper.TestTask;

public class FlowTestNodeLoaderTest {

  private NodeLoader loader;

  @Before
  public void setUp() {
    loader = new FlowTestNodeLoader();
  }

  @Test
  public void loadDecision() {
    TestDecision actual = loader.load( TestDecision.class, mock( WorkflowContext.class ) );

    assertThat( actual ).isExactlyInstanceOf( TestDecision.class );
  }

  @Test
  public void loadActivity() {
    TestActivity actual = loader.load( TestActivity.class, mock( WorkflowContext.class ) );

    assertThat( actual ).isNotExactlyInstanceOf( TestActivity.class );
  }

  @Test
  public void loadTask() {
    TestTask actual = loader.load( TestTask.class, mock( WorkflowContext.class ) );

    assertThat( actual ).isNotExactlyInstanceOf( TestDecision.class );
  }
}