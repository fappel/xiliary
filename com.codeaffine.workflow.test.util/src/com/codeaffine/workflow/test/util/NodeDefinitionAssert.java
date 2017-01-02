/**
 * Copyright (c) 2014 - 2017 Frank Appel
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Frank Appel - initial API and implementation
 */
package com.codeaffine.workflow.test.util;

import java.util.Arrays;
import java.util.List;

import org.assertj.core.api.AbstractAssert;

import com.codeaffine.workflow.NodeDefinition;

public class NodeDefinitionAssert extends AbstractAssert<NodeDefinitionAssert, NodeDefinition>{

  public static NodeDefinitionAssert assertThat( NodeDefinition actual ) {
    return new NodeDefinitionAssert( actual );
  }

  public NodeDefinitionAssert( NodeDefinition actual ) {
    super( actual, NodeDefinitionAssert.class );
  }

  public NodeDefinitionAssert hasNodeId( String expected ) {
    isNotNull();
    if( !actual.getNodeId().equals( expected ) ) {
      failWithMessage( "Expected node's id to be <%s> but was <%s>", expected, actual.getNodeId() );
    }
    return this;
  }

  public NodeDefinitionAssert hasType( Class<?> expected ) {
    isNotNull();
    if( actual.getType() != expected ) {
      failWithMessage( "Expected node's type to be <%s> but was <%s>", expected, actual.getType() );
    }
    return this;
  }

  public NodeDefinitionAssert hasSuccessors( String ... expected ) {
    isNotNull();
    List<String> actuals = Arrays.asList( actual.getSuccessors() );
    List<String> expecteds = Arrays.asList( expected );
    if( !actuals.equals( expecteds ) ) {
      failWithMessage( "Expected node's successors to be <%s> but was <%s>", expecteds, actuals );
    }
    return this;
  }
}