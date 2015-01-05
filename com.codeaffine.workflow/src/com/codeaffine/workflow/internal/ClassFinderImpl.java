package com.codeaffine.workflow.internal;

import java.util.Iterator;
import java.util.Map;

import com.codeaffine.workflow.NodeDefinition;
import com.codeaffine.workflow.persistence.ClassFinder;

class ClassFinderImpl implements ClassFinder {

  private final Map<String, WorkflowDefinitionImpl> definitions;

  ClassFinderImpl( Map<String, WorkflowDefinitionImpl> definitions ) {
    this.definitions = definitions;
  }

  @Override
  public Class<?> find( String className ) {
    Class<?> result = null;
    synchronized( definitions ) {
      result = loadFromDefinitions( className );
    }
    if( result == null ) {
      result = loadFromLocalClassLoader( className );
    }
    return result;
  }

  private Class<?> loadFromDefinitions( String className ) {
    Class<?> result = null;
    Iterator<WorkflowDefinitionImpl> iterator = definitions.values().iterator();
    while( result == null && iterator.hasNext() ) {
      WorkflowDefinitionImpl definition = iterator.next();
      result = load( definition.getClass(), className );
      if( result == null ) {
        result = loadFromNodeTypes( className, definition );
      }
    }
    return result;
  }

  private static Class<?> loadFromNodeTypes( String className, WorkflowDefinitionImpl definition ) {
    Class<?> result = null;
    NodeDefinition[] nodeDefinitions = definition.getNodeDefinitions();
    for( int i = 0; result == null && i < nodeDefinitions.length; i++ ) {
      NodeDefinition nodeDefinition = nodeDefinitions[ i ];
      result = load( nodeDefinition.getType(), className );
    }
    return result;
  }

  private static Class<?> load( Class<?> type, String typeName ) {
    try {
      return type.getClassLoader().loadClass( typeName );
    } catch( ClassNotFoundException ignore ) {
    }
    return null;
  }

  private Class<?> loadFromLocalClassLoader( String className ) {
    try {
      return getClass().getClassLoader().loadClass( className );
    } catch( ClassNotFoundException cnfe ) {
      throw new RuntimeException( cnfe );
    }
  }
}