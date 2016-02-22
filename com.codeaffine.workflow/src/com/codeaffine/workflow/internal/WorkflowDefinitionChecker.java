/**
 * Copyright (c) 2014 - 2016 Frank Appel
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Frank Appel - initial API and implementation
 */
package com.codeaffine.workflow.internal;

import static java.lang.String.format;

import com.codeaffine.workflow.NodeDefinition;

class WorkflowDefinitionChecker {

  static final String MISSING_DEFINITION_ID = "WorkflowDefinition must specify an id.";
  static final String MISSING_START_NODE_DECLARATION = "Start node declaration is missing.";
  static final String MISSING_START_NODE_DEFINITION = "Start node <%s> is undefined.";
  static final String MISSING_SUCCESSOR_DEFINITION = "Successor <%s> of node <%s> does not exist.";

  void checkDefinition( WorkflowDefinitionImpl definition ) {
    checkDefinitionId( definition );
    checkStartNodeDeclaration( definition );
    checkStartNodeDefinition( definition );
    checkSuccessorDefinitions( definition );
  }

  private static void checkDefinitionId( WorkflowDefinitionImpl definition ) {
    if( definition.getId() == null ) {
      throw new IllegalArgumentException( MISSING_DEFINITION_ID );
    }
  }

  private static void checkStartNodeDeclaration( WorkflowDefinitionImpl definition ) {
    if( definition.getStart() == null ) {
      throw new IllegalArgumentException( MISSING_START_NODE_DECLARATION );
    }
  }
  
  private static void checkStartNodeDefinition( WorkflowDefinitionImpl definition ) {
    if( definition.getStartNode() == null ) {
      throw new IllegalArgumentException( format( MISSING_START_NODE_DEFINITION, definition.getStart() ) );
    }
  }
  
  private static void checkSuccessorDefinitions( WorkflowDefinitionImpl definition ) {
    NodeDefinition[] nodeDefinitions = definition.getNodeDefinitions();
    for( NodeDefinition nodeDefinition : nodeDefinitions ) {
      checkSuccessorDefinition( definition, nodeDefinition );
    }
  }
  
  private static void checkSuccessorDefinition( WorkflowDefinitionImpl definition, NodeDefinition nodeDefinition ) {
    String[] successors = nodeDefinition.getSuccessors();
    for( String successor: successors ) {
      NodeDefinition node = definition.getNode( successor );
      if( node == null ) {
        throw new IllegalArgumentException( format( MISSING_SUCCESSOR_DEFINITION, successor, nodeDefinition.getNodeId() ) );
      }
    }
  }
}