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
package com.codeaffine.workflow;

import com.codeaffine.workflow.definition.VariableDeclaration;
import com.codeaffine.workflow.internal.NodeLoaderImpl;

public class WorkflowContexts {

  private static final String PREFIX = WorkflowContext.class.getPackage().getName().replaceAll( "\\.", "_" )  + "__";

  public static <T> T lookup( VariableDeclaration<T> contextAttribute ) {
    return NodeLoaderImpl.lookup( contextAttribute );
  }

  static String prefix( String name ) {
    return PREFIX + name;
  }
}