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
package com.codeaffine.eclipse.ui.swt.theme;

import static com.codeaffine.eclipse.ui.swt.theme.AttributeKey.key;
import static com.codeaffine.test.util.lang.EqualsTester.newInstance;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.Callable;

import org.eclipse.swt.graphics.Color;
import org.junit.Test;

import com.codeaffine.eclipse.swt.widget.scrollable.Demeanor;
import com.codeaffine.test.util.lang.EqualsTester;

public class AttributeKeyTest {

  private static final String IDENTIFIER = "identifier";
  private static final Class<Runnable> TYPE = Runnable.class;

  @Test
  public void getName() {
    AttributeKey<Runnable> key = key( IDENTIFIER, TYPE );

    String actual = key.identifier;

    assertThat( actual ).isSameAs( IDENTIFIER );
  }

  @Test
  public void getType() {
    AttributeKey<Runnable> key = key( IDENTIFIER, TYPE );

    Class<Runnable> actual = key.type;

    assertThat( actual ).isSameAs( TYPE );
  }

  @Test
  public void equalsAndHashCode() {
    EqualsTester<AttributeKey<Runnable>> tester = newInstance( key( IDENTIFIER, TYPE ) );

    tester.assertImplementsEqualsAndHashCode();
    tester.assertEqual( key( IDENTIFIER, TYPE ), key( IDENTIFIER, TYPE ) );
    tester.assertNotEqual( key( IDENTIFIER, TYPE ), key( "otherKey", TYPE ) );
    tester.assertNotEqual( key( IDENTIFIER, TYPE ), key( IDENTIFIER, Callable.class ) );
  }

  @Test
  public void toStringRepresentation() {
    AttributeKey<Runnable> key = key( IDENTIFIER, TYPE );

    String actual = key.toString();

    assertThat( actual )
      .contains( IDENTIFIER )
      .contains( TYPE.getName() );
  }

  @Test
  public void colorKey() {
    AttributeKey<Color> actual = AttributeKey.colorKey( IDENTIFIER );

    assertThat( actual.identifier ).isSameAs( IDENTIFIER );
    assertThat( actual.type ).isSameAs( Color.class );
  }

  @Test
  public void demeanorKey() {
    AttributeKey<Demeanor> actual = AttributeKey.demeanorKey( IDENTIFIER );

    assertThat( actual.identifier ).isSameAs( IDENTIFIER );
    assertThat( actual.type ).isSameAs( Demeanor.class );
  }

  @Test
  public void integerKey() {
    AttributeKey<Integer> actual = AttributeKey.integerKey( IDENTIFIER );

    assertThat( actual.identifier ).isSameAs( IDENTIFIER );
    assertThat( actual.type ).isSameAs( Integer.class );
  }

  @Test( expected = IllegalArgumentException.class )
  public void constructWithNullAsName() {
    key( null, TYPE );
  }

  @Test( expected = IllegalArgumentException.class )
  public void constructWithNullAsType() {
    key( IDENTIFIER, null );
  }
}