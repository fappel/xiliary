package com.codeaffine.workflow;

public class WorkflowContextEvent<T> {
  
  private VariableDeclaration<T> declaration;
  private final T newValue;
  private final T oldValue;

  public WorkflowContextEvent( VariableDeclaration<T> declaration, T newValue, T oldValue ) {
    this.declaration = declaration;
    this.newValue = newValue;
    this.oldValue = oldValue;
  }
  
  public VariableDeclaration<T> getDeclaration() {
    return declaration;
  }
  
  public T getNewValue() {
    return newValue;
  }

  public T getOldValue() {
    return oldValue;
  }
}