package com.codeaffine.workflow;

import com.codeaffine.workflow.definition.VariableDeclaration;

public interface Scope {
  <T> T defineVariable( VariableDeclaration<T> declaration, T value );
}
