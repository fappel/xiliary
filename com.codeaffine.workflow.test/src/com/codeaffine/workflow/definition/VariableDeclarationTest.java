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
package com.codeaffine.workflow.definition;

import static com.codeaffine.test.util.lang.EqualsTester.newInstance;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.Callable;

import org.junit.Test;

import com.codeaffine.test.util.lang.EqualsTester;
import com.codeaffine.workflow.definition.VariableDeclaration;

public class VariableDeclarationTest {

  private static final String NAME = "name";

  @Test
  public void getName() {
    VariableDeclaration<Runnable> declaration = new VariableDeclaration<Runnable>( NAME, Runnable.class );

    String actual = declaration.getName();

    assertThat( actual ).isSameAs( NAME );
  }

  @Test
  public void getType() {
    VariableDeclaration<Runnable> declaration = new VariableDeclaration<Runnable>( NAME, Runnable.class );

    Class<Runnable> actual = declaration.getType();

    assertThat( actual ).isSameAs( Runnable.class );
  }

  @Test
  @SuppressWarnings( { "rawtypes", "unchecked" } )
  public void equalsAndHashCode() {
    EqualsTester<VariableDeclaration> tester = newInstance( new VariableDeclaration( NAME, Runnable.class ) );

    tester.assertImplementsEqualsAndHashCode();
    tester.assertEqual( new VariableDeclaration( NAME, Runnable.class ),
                        new VariableDeclaration( NAME, Runnable.class ) );
    tester.assertNotEqual( new VariableDeclaration<Runnable>( NAME, Runnable.class ),
                           new VariableDeclaration<Runnable>( "otherName", Runnable.class ) );
    tester.assertNotEqual( new VariableDeclaration<Runnable>( NAME, Runnable.class ),
                           new VariableDeclaration<Callable>( NAME, Callable.class ) );
  }

  @Test
  public void toStringRepresentation() {
    VariableDeclaration<Runnable> declaration = new VariableDeclaration<Runnable>( NAME, Runnable.class );

    String actual = declaration.toString();

    assertThat( actual )
      .contains( NAME )
      .contains( Runnable.class.getName() );
  }

  @Test( expected = IllegalArgumentException.class )
  public void constructorWithNullAsName() {
    new VariableDeclaration<Runnable>( null, Runnable.class );
  }

  @Test( expected = IllegalArgumentException.class )
  public void constructorWithIllegalName() {
    new VariableDeclaration<Runnable>( "[", Runnable.class );
  }

  @Test( expected = IllegalArgumentException.class )
  public void constructorWithNullAsType() {
    new VariableDeclaration<Runnable>( NAME, null );
  }
}