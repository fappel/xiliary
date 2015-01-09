package com.codeaffine.workflow.internal;

import com.codeaffine.workflow.Scope;
import com.codeaffine.workflow.definition.VariableDeclaration;

public interface ScopeContext extends Scope {

  VariableDeclaration<?>[] getVariableDeclarations();
  boolean hasVariableDefinition( VariableDeclaration<?> declaration );
  <T> T getVariableValue( VariableDeclaration<T> declaration );
}