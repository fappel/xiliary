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