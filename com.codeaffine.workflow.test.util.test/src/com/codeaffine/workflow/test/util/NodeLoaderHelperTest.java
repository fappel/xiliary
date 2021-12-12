/**
 * Copyright (c) 2014 - 2022 Frank Appel
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Frank Appel - initial API and implementation
 */
package com.codeaffine.workflow.test.util;

import static com.codeaffine.workflow.WorkflowContexts.lookup;
import static com.codeaffine.workflow.test.util.WorkflowDefinitionHelper.NAME;
import static com.codeaffine.workflow.test.util.WorkflowDefinitionHelper.VALUE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import org.junit.Test;

import com.codeaffine.workflow.WorkflowContext;
import com.codeaffine.workflow.WorkflowServiceFactory;

public class NodeLoaderHelperTest {

  static class Type {
    public final Object value = lookup( NAME );
  }

  static class TypeWithConstructor {
    public final Object value = lookup( NAME );
    public final Runnable runnable;

    TypeWithConstructor( Runnable runnable ) {
      this.runnable = runnable;
    }
  }

  @Test
  public void load() {
    WorkflowContext context = new WorkflowServiceFactory().createWorkflowContext();
    context.defineVariable( NAME, VALUE );

    Type actual = NodeLoaderHelper.load( context, Type.class );

    assertThat( actual.value ).isSameAs( VALUE );
  }

  @Test
  public void loadWithAdaper() {
    final Runnable parameter = mock( Runnable.class );
    WorkflowContext context = new WorkflowServiceFactory().createWorkflowContext();
    context.defineVariable( NAME, VALUE );

    TypeWithConstructor actual = NodeLoaderHelper.load( context, new LoadAdapter<TypeWithConstructor>() {

      @Override
      public TypeWithConstructor load() {
        return new TypeWithConstructor( parameter );
      }
    } );

    assertThat( actual.value ).isSameAs( VALUE );
    assertThat( actual.runnable ).isSameAs( parameter );
  }
}