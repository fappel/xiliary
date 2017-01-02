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
package com.codeaffine.eclipse.core.runtime;

import static com.codeaffine.eclipse.core.runtime.Predicates.attribute;
import static com.codeaffine.eclipse.core.runtime.TestExtension.EXTENSION_POINT;
import static com.codeaffine.test.util.lang.ThrowableCaptor.thrownBy;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;
import java.util.Locale;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class ExtensionPDETest {

  private Extension extension;

  @Before
  public void setUp() {
    extension = new RegistryAdapter()
      .readExtension( EXTENSION_POINT )
      .thatMatches( attribute( "id", "1" ) )
      .process();
  }

  @Test
  public void getConfigurationElement() {
    IConfigurationElement actual = extension.getConfigurationElement();

    assertThat( actual ).isNotNull();
  }

  @Test
  public void getAttributeNames() {
    Collection<String> actuals = extension.getAttributeNames();

    assertThat( actuals ).containsExactly( "id", "class", "type" );
  }

  @Test
  public void getAttribute() {
     String actual = extension.getAttribute( "id" );

     assertThat( actual ).isEqualTo( "1" );
  }

  @Test
  public void getAttributeWithUnknownLocale() {
     String actual = extension.getAttribute( "id", "unknown" );

     assertThat( actual ).isEqualTo( "1" );
  }

  @Test
  public void getName() {
    String actual = extension.getName();

    assertThat( actual ).isEqualTo( "contribution" );
  }

  @Test
  public void createExecutableExtension() {
    TestExtension actual = extension.createExecutableExtension( TestExtension.class );

    assertThat( actual ).isNotNull();
  }

  @Test
  public void createExecutableExtensionWithTypeAttribute() {
    TestExtension actual = extension.createExecutableExtension( "type", TestExtension.class );

    assertThat( actual ).isNotNull();
  }

  @Test
  public void createExecutableExtensionWithUnknownTypeAttribute() {
    Throwable expected = thrownBy( () -> extension.createExecutableExtension( "unknown", TestExtension.class ) );

    assertThat( expected )
      .isInstanceOf( ExtensionException.class )
      .hasCauseInstanceOf( CoreException.class );

  }

  @Test( expected = IllegalArgumentException.class )
  public void createExecutableExtensionWithNullAsTypeAttribute() {
    extension.createExecutableExtension( null, TestExtension.class );
  }

  @Test( expected = IllegalArgumentException.class )
  public void createExecutableExtensionWithNullAsExtensionType() {
    extension.createExecutableExtension( "type", null );
  }

  @Test
  @Ignore( "https://github.com/fappel/xiliary/issues/3" )
  public void getValue() {
    String actual = extension.getValue();

    assertThat( actual ).isEqualTo( "value1" );
  }

  @Test
  @Ignore( "https://github.com/fappel/xiliary/issues/3" )
  public void getValueWithLocale() {
    String actual = extension.getValue( Locale.getDefault().toString() );

    assertThat( actual ).isEqualTo( "value1" );
  }

  @Test
  public void getChildren() {
    Collection<Extension> actuals = extension.getChildren();

    assertThat( actuals ).hasSize( 2 );
  }

  @Test
  public void getChildrenWithName() {
    Collection<Extension> actuals = extension.getChildren( "child" );

    assertThat( actuals ).hasSize( 2 );
  }

  @Test
  public void getChildrenWithUnknownName() {
    Collection<Extension> actuals = extension.getChildren( "unknown" );

    assertThat( actuals ).isEmpty();
  }
}