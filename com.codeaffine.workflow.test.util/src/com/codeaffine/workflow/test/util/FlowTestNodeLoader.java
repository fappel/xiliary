/**
 * Copyright (c) 2014 - 2017 Frank Appel
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Frank Appel - initial API and implementation
 */
package com.codeaffine.workflow.test.util;

import static org.mockito.Mockito.mock;

import com.codeaffine.workflow.NodeLoader;
import com.codeaffine.workflow.WorkflowContext;
import com.codeaffine.workflow.definition.Decision;

public class FlowTestNodeLoader implements NodeLoader {

  @Override
  public <T> T load( Class<T> toLoad, WorkflowContext context ) {
    if( Decision.class.isAssignableFrom( toLoad ) ) {
      return NodeLoaderHelper.load( context, toLoad );
    }
    return mock( toLoad );
  }
}