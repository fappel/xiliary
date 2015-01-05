package com.codeaffine.workflow;

import com.codeaffine.workflow.definition.VariableDeclaration;
import com.codeaffine.workflow.internal.NodeLoaderImpl;

public class WorkflowContexts {

  public static <T> T lookup( VariableDeclaration<T> contextAttribute ) {
    return NodeLoaderImpl.lookup( contextAttribute );
  }
}