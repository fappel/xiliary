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
package com.codeaffine.eclipse.core.runtime.test.util;

import static java.lang.Integer.valueOf;

import java.util.ArrayList;
import java.util.Collection;

import org.assertj.core.api.AbstractAssert;

import com.codeaffine.eclipse.core.runtime.Extension;

public class ExtensionAssert extends AbstractAssert<ExtensionAssert, Extension> {

  static final String PATTERN_WRONG_ATTRIBUTE
    = "Expected value for attribute '%s' to be <%s> but was <%s>.";
  static final String PATTERN_EMPTY_ATTRIBUTE
    = "Expected value for attribute '%s' not to be empty but it was.";
  static final String PATTERN_NULL_ATTRIBUTE
    = "Expected value for attribute '%s' not to be empty but it was <null>.";
  static final String PATTERN_NOT_NULL_ATTRIBUTE
    = "Expected value for attribute '%s' to be null but it was <%s>.";
  static final String PATTERN_ERR_CREATE
    = "Expected extension to be an instantiable type of <%s> using value <%s> of attribute <%s> but was not.";
  static final String PATTERN_WRONG_CHILD_SIZE
    = "Expected <%s> child(ren) of <%s> with value <%s> but there was(were) <%s>.";
  static final String PATTERN_WRONG_CHILD_SIZE_WITH_ATTRIB
    = "Expected to found one child with attribute <%s[%s]> but found <%s>.";

  public ExtensionAssert( Extension actual ) {
    super( actual, ExtensionAssert.class );
  }

  public static ExtensionAssert assertThat( Extension actual ) {
    return new ExtensionAssert( actual );
  }

  public ExtensionAssert hasAttributeValue( String attributeName, String expectedValue ) {
    isNotNull();
    String actualValue = actual.getAttribute( attributeName );
    if( actualValue == null && expectedValue != null || !actualValue.equals( expectedValue ) ) {
      failWithMessage( PATTERN_WRONG_ATTRIBUTE, attributeName, expectedValue, actualValue );
    }
    return this;
  }

  public ExtensionAssert hasNonEmptyAttributeValueFor( String attributeName ) {
    isNotNull();
    if( actual.getAttribute( attributeName ) == null ) {
      failWithMessage( PATTERN_NULL_ATTRIBUTE, attributeName, actual.getAttribute( attributeName ) );
    }
    if( actual.getAttribute( attributeName ).isEmpty() ) {
      failWithMessage( PATTERN_EMPTY_ATTRIBUTE, attributeName, actual.getAttribute( attributeName ) );
    }
    return this;
  }

  public ExtensionAssert hasNoAttributeValueFor( String attributeName ) {
    isNotNull();
    if( actual.getAttribute( attributeName ) != null ) {
      failWithMessage( PATTERN_NOT_NULL_ATTRIBUTE, attributeName, actual.getAttribute( attributeName ) );
    }
    return this;
  }

  public ExtensionAssert isInstantiable( Class<?> type ) {
    return isInstantiable( "class", type );
  }

  public ExtensionAssert isInstantiable( String typeAttribute, Class<?> type  ) {
    isNotNull();
    try {
      type.cast( actual.createExecutableExtension( typeAttribute, Object.class ) );
    } catch ( Exception problem ) {
      failWithMessage( PATTERN_ERR_CREATE, type.getName(), actual.getAttribute( typeAttribute ), typeAttribute );
    }
    return this;
  }

  public ExtensionAssert hasChildrenWithNonEmptyValue( String name, int childAmount ) {
    isNotNull();
    Collection<Extension> children = actual.getChildren( name );
    Collection<Extension> found = new ArrayList<Extension>();
    for( Extension child : children ) {
      String value = child.getValue();
      if( value != null && value.length() != 0 ) {
        found.add( child );
      }
    }
    int size = found.size();
    if( size != childAmount ) {
      failWithMessage( PATTERN_WRONG_CHILD_SIZE, valueOf( childAmount ), actual.getName(), name, valueOf( size ) );
    }
    return this;
  }

  public ExtensionAssert hasChildWithAttributeValue( String attributeName, String value ) {
    isNotNull();
    Collection<Extension> children = actual.getChildren();
    Collection<Extension> found = new ArrayList<Extension>();
    for( Extension child : children ) {
      if( child.getAttribute( attributeName ) != null && child.getAttribute( attributeName ).equals( value ) ) {
        found.add( child );
      }
    }
    if( found.size() != 1 ) {
      failWithMessage( PATTERN_WRONG_CHILD_SIZE_WITH_ATTRIB, attributeName, value, valueOf( found.size() ) );
    }
    return this;
  }
}