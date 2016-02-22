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

import static com.codeaffine.workflow.internal.ArgumentVerification.verifyCondition;
import static com.codeaffine.workflow.internal.ArgumentVerification.verifyNotNull;

import java.util.HashMap;
import java.util.Map;

import com.codeaffine.workflow.definition.VariableDeclaration;

class ScopeImpl implements ScopeContext {

  static final String NO_DECLARATION = "Variable declaration <%s> does not exist.";

  private final Map<VariableDeclaration<?>, Object> content;

  ScopeImpl() {
    this.content = new HashMap<VariableDeclaration<?>, Object>();
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T> T defineVariable( VariableDeclaration<T> declaration, T value ) {
    verifyNotNull( declaration, "declaration" );

    T result;
    synchronized( content ) {
      result = ( T )content.get( declaration );
      content.put( declaration, value );
    }
    return result;
  }

  @Override
  public VariableDeclaration<?>[] getVariableDeclarations() {
    synchronized( content ) {
      return content.keySet().toArray( new VariableDeclaration<?>[ content.size() ] );
    }
  }

  @Override
  public boolean hasVariableDefinition( VariableDeclaration<?> declaration ) {
    verifyNotNull( declaration, "declaration" );

    synchronized( content ) {
      return content.get( declaration ) != null;
    }
  }

  @Override
  @SuppressWarnings( "unchecked" )
  public <T> T getVariableValue( VariableDeclaration<T> declaration ) {
    verifyNotNull( declaration, "declaration" );
    synchronized( content ) {
      verifyCondition( content.containsKey( declaration ), NO_DECLARATION, declaration.toString() );

      return ( T )content.get( declaration );
    }
  }

  Map<VariableDeclaration<?>, Object> getContent() {
    HashMap<VariableDeclaration<?>, Object> result = new HashMap<VariableDeclaration<?>, Object>();
    synchronized( content ) {
      result.putAll( content );
    }
    return result;
  }

  void setContent( Map<VariableDeclaration<?>, Object> newContent ) {
    synchronized( content ) {
      content.clear();
      content.putAll( newContent );
    }
  }
}