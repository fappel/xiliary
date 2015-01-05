package com.codeaffine.workflow;

public interface NodeLoader {
  <T> T load( Class<T> toLoad, WorkflowContext context );
}