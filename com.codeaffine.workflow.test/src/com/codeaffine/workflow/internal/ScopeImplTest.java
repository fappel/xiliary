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

import static com.codeaffine.workflow.test.util.WorkflowDefinitionHelper.VALUE;
import static com.codeaffine.workflow.test.util.WorkflowDefinitionHelper.VAR_DECL;
import static com.codeaffine.workflow.test.util.WorkflowDefinitionHelper.VAR_LIST;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.codeaffine.workflow.definition.VariableDeclaration;

public class ScopeImplTest {

  private static final String NEW_VALUE = "newValue";

  private ScopeImpl scope;

  @Before
  public void setUp() {
    scope = new ScopeImpl();
  }

  @Test
  public void defineVariable() {
    String oldValue = scope.defineVariable( VAR_DECL, VALUE );
    String actual = scope.getVariableValue( VAR_DECL );

    assertThat( oldValue ).isNull();
    assertThat( actual ).isSameAs( VALUE );
  }

  @Test
  public void hasVariableDefinition() {
    scope.defineVariable( VAR_DECL, VALUE );

    boolean actual = scope.hasVariableDefinition( VAR_DECL );

    assertThat( actual ).isTrue();
  }

  @Test
  public void hasVariableDefinitionIfNotDefined() {
    boolean actual = scope.hasVariableDefinition( VAR_DECL );

    assertThat( actual ).isFalse();
  }

  @Test
  public void getVariableDeclarations() {
    scope.defineVariable( VAR_DECL, VALUE );

    VariableDeclaration<?>[] actual = scope.getVariableDeclarations();

    assertThat( actual )
      .hasSize( 1 )
      .contains( VAR_DECL );
  }

  @Test
  public void defineVariableWithNull() {
    scope.defineVariable( VAR_DECL, null );

    String actual = scope.getVariableValue( VAR_DECL );

    assertThat( actual ).isNull();
  }

  @Test
  public void defineVariableThatAlreadyExists() {
    scope.defineVariable( VAR_DECL, VALUE );

    String oldValue = scope.defineVariable( VAR_DECL, NEW_VALUE );
    String actual = scope.getVariableValue( VAR_DECL );

    assertThat( oldValue ).isSameAs( VALUE );
    assertThat( actual ).isSameAs( NEW_VALUE );
  }

  @Test( expected = IllegalArgumentException.class )
  public void getUndeclaredVariable() {
    scope.getVariableValue( VAR_DECL );
  }

  @Test( expected = IllegalArgumentException.class )
  public void getVariableValueWithNullParameter() {
    scope.getVariableValue( null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void defineVariableWithNullParameter() {
    scope.defineVariable( null, VALUE );
  }

  @Test
  public void getContent() {
    List<String> expected = createListWithValue();
    scope.defineVariable( VAR_LIST, expected );

    Map<VariableDeclaration<?>, Object> content = scope.getContent();

    assertThat( content ).containsEntry( VAR_LIST, expected );
  }

  @Test
  public void setContent() {
    List<String> list = createListWithValue();
    Map<VariableDeclaration<?>, Object> content = new HashMap<VariableDeclaration<?>, Object>();
    content.put( VAR_LIST, list );

    scope.setContent( content );

    assertThat( scope.hasVariableDefinition( VAR_LIST ) ).isTrue();
  }

  @Test
  public void changeScopeAfterGetContent() {
    List<String> expected = createListWithValue();
    scope.defineVariable( VAR_LIST, expected );

    Map<VariableDeclaration<?>, Object> content = scope.getContent();
    scope.defineVariable( VAR_DECL, VALUE );

    assertThat( content ).hasSize( 1 );
  }

  @Test
  public void changeContentAfterSetContent() {
    List<String> list = createListWithValue();
    Map<VariableDeclaration<?>, Object> content = new HashMap<VariableDeclaration<?>, Object>();
    content.put( VAR_LIST, list );

    scope.setContent( content );
    content.put( VAR_DECL, VALUE );

    assertThat( scope.getVariableDeclarations() ).hasSize( 1 );
  }

  private static List<String> createListWithValue() {
    List<String> result = new ArrayList<String>();
    result.add( VALUE );
    return result;
  }
}