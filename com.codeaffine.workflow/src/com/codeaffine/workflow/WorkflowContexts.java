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