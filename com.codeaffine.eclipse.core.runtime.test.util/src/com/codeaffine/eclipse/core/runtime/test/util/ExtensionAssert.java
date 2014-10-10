package com.codeaffine.eclipse.core.runtime.test.util;

import static java.lang.Integer.valueOf;

import java.util.ArrayList;
import java.util.Collection;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;

import com.codeaffine.eclipse.core.runtime.Extension;

public class ExtensionAssert extends AbstractAssert<ExtensionAssert, Extension> {
  private static final String PATTERN_WRONG_ATTRIBUTE
    = "Expected value for attribute '%s' to be <%s> but was <%s>";
  private static final String PATTERN_ERR_CREATE
    = "Expected extension to be instantiable using value <%s> of attribute <%s> but was not.";
  private static final String PATTERN_WRONG_CHILD_SIZE
    = "Expected <%s> child(ren) of <%s> with name <%s> but was(were) <%s>.";
  private static final String PATTERN_CHILD_WITHOUT_VALUE
    = "Expected child <%s> of <%s> to have a value but was <null>.";
  private static final String PATTERN_CHILD_WITH_EMPTY_VALUE
    = "Expected child <%s> of <%s> to have a non empty value but was <%s>.";
  private static final String PATTERN_WRONG_CHILD_SIZE_WITH_ATTRIB
    = "Expected to found one child with attribute <%s[%s]> but found <5s>.";

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
    Assertions.assertThat( actual.getAttribute( attributeName ) ).isNotEmpty();
    return this;
  }

  public ExtensionAssert hasNoAttributeValueFor( String attributeName ) {
    isNotNull();
    Assertions.assertThat( actual.getAttribute( attributeName ) ).isNull();
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
      failWithMessage( PATTERN_ERR_CREATE, actual.getAttribute( typeAttribute ), typeAttribute );
    }
    return this;
  }

  public void hasChildrenWithNonEmptyValue( String name, int childAmount ) {
    isNotNull();
    Collection<Extension> children = actual.getChildren( name );
    int size = children.size();
    if( size != childAmount ) {
      failWithMessage( PATTERN_WRONG_CHILD_SIZE, valueOf( childAmount ), actual.getName(), name, valueOf( size ) );
    }
    for( Extension child : children ) {
      String value = child.getValue();
      if( value == null ) {
        failWithMessage( PATTERN_CHILD_WITHOUT_VALUE, name, actual.getName() );
      }
      if( value.length() == 0 ) {
        failWithMessage( PATTERN_CHILD_WITH_EMPTY_VALUE, name, actual.getName(), value );
      }
    }
  }

  public ExtensionAssert hasChildWithAttributeValue( String attributeName, String value ) {
    isNotNull();
    Collection<Extension> children = actual.getChildren();
    Collection<Extension> found = new ArrayList<Extension>();
    for( Extension child : children ) {
      if( child.getAttribute( attributeName ).equals( value ) ) {
        found.add( child );
      }
    }
    if( found.size() != 1 ) {
      failWithMessage( PATTERN_WRONG_CHILD_SIZE_WITH_ATTRIB, attributeName, value, valueOf( found.size() ) );
    }
    return this;
  }
}