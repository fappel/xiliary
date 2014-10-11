package com.codeaffine.eclipse.core.runtime.test.util;

import static com.codeaffine.eclipse.core.runtime.Predicates.attribute;
import static com.codeaffine.eclipse.core.runtime.test.util.AssertMessageHelper.format;
import static com.codeaffine.eclipse.core.runtime.test.util.ExtensionAssert.PATTERN_EMPTY_ATTRIBUTE;
import static com.codeaffine.eclipse.core.runtime.test.util.ExtensionAssert.PATTERN_ERR_CREATE;
import static com.codeaffine.eclipse.core.runtime.test.util.ExtensionAssert.PATTERN_NOT_NULL_ATTRIBUTE;
import static com.codeaffine.eclipse.core.runtime.test.util.ExtensionAssert.PATTERN_NULL_ATTRIBUTE;
import static com.codeaffine.eclipse.core.runtime.test.util.ExtensionAssert.PATTERN_WRONG_ATTRIBUTE;
import static com.codeaffine.eclipse.core.runtime.test.util.ExtensionAssert.PATTERN_WRONG_CHILD_SIZE;
import static com.codeaffine.eclipse.core.runtime.test.util.ExtensionAssert.PATTERN_WRONG_CHILD_SIZE_WITH_ATTRIB;
import static com.codeaffine.eclipse.core.runtime.test.util.TestExtension.EXTENSION_POINT_ID;
import static com.codeaffine.test.util.ThrowableCaptor.thrown;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.codeaffine.eclipse.core.runtime.Extension;
import com.codeaffine.eclipse.core.runtime.RegistryAdapter;
import com.codeaffine.test.util.ThrowableCaptor.Actor;

public class ExtensionAssertPDETest {

  private static final String ATTRIBUTE_NAME = "id";
  private static final String ATTRIBUTE_VALUE = "1";
  private static final String DOES_NOT_MATCH = "doesNotMatch";
  private static final String UNKNOWN = "unknown";
  private static final String EMPTY = "empty";
  private static final String TYPE = "type";
  private static final String CONTRIBUTION = "contribution";
  private static final String CHILD = "child";

  private Extension extension;
  private ExtensionAssert extensionAssert;

  @Before
  public void setUp() {
    extension = new RegistryAdapter()
      .readExtension( EXTENSION_POINT_ID )
      .thatMatches( attribute( ATTRIBUTE_NAME, ATTRIBUTE_VALUE ) )
      .process();
    extensionAssert = ExtensionAssert.assertThat( extension );
  }

  @Test
  public void assertThatFactory() {
    assertThat( extensionAssert ).isNotNull();
  }

  @Test
  public void hasAttributeValue() {
    ExtensionAssert actual = extensionAssert.hasAttributeValue( ATTRIBUTE_NAME, ATTRIBUTE_VALUE );

    assertThat( actual ).isSameAs( extensionAssert );
  }

  @Test
  public void hasAttributeValueThatDoesNotMatch() {
    Throwable actual = thrown( new Actor() {
      @Override
      public void act() throws Throwable {
        extensionAssert.hasAttributeValue( ATTRIBUTE_NAME, DOES_NOT_MATCH );
      }
    } );

    assertThat( actual )
      .hasMessage( format( PATTERN_WRONG_ATTRIBUTE, ATTRIBUTE_NAME, DOES_NOT_MATCH, ATTRIBUTE_VALUE ) )
      .isInstanceOf( AssertionError.class );
  }

  @Test
  public void hasAttributeValueWithUnknownName() {
    Throwable actual = thrown( new Actor() {
      @Override
      public void act() throws Throwable {
        extensionAssert.hasAttributeValue( UNKNOWN, DOES_NOT_MATCH );
      }
    } );

    assertThat( actual )
      .hasMessage( format( PATTERN_WRONG_ATTRIBUTE, UNKNOWN, DOES_NOT_MATCH, null ) )
      .isInstanceOf( AssertionError.class );
  }

  @Test
  public void hasNonEmptyAttributeValueFor() {
    ExtensionAssert actual = extensionAssert.hasNonEmptyAttributeValueFor( ATTRIBUTE_NAME );

    assertThat( actual ).isSameAs( extensionAssert );
  }

  @Test
  public void hasNonEmptyAttributeValueForWithEmptyValue() {
    Throwable actual = thrown( new Actor() {
      @Override
      public void act() throws Throwable {
        extensionAssert.hasNonEmptyAttributeValueFor( EMPTY );
      }
    } );

    assertThat( actual )
      .hasMessage( format( PATTERN_EMPTY_ATTRIBUTE, EMPTY ) )
      .isInstanceOf( AssertionError.class );
  }

  @Test
  public void hasNonEmptyAttributeValueForWithNullValue() {
    Throwable actual = thrown( new Actor() {
      @Override
      public void act() throws Throwable {
        extensionAssert.hasNonEmptyAttributeValueFor( UNKNOWN );
      }
    } );

    assertThat( actual )
      .hasMessage( format( PATTERN_NULL_ATTRIBUTE,  UNKNOWN ) )
      .isInstanceOf( AssertionError.class );
  }

  @Test
  public void hasNoAttributeValueFor() {
    ExtensionAssert actual = extensionAssert.hasNoAttributeValueFor( UNKNOWN );

    assertThat( actual ).isSameAs( extensionAssert );
  }

  @Test
  public void hasNoAttributeValueForWithValue() {
    Throwable actual = thrown( new Actor() {
      @Override
      public void act() throws Throwable {
        extensionAssert.hasNoAttributeValueFor( ATTRIBUTE_NAME );
      }
    } );

    assertThat( actual )
      .hasMessage( format( PATTERN_NOT_NULL_ATTRIBUTE, ATTRIBUTE_NAME, ATTRIBUTE_VALUE ) )
      .isInstanceOf( AssertionError.class );
  }

  @Test
  public void isInstantiable() {
     ExtensionAssert actual = extensionAssert.isInstantiable( TYPE, TestExtension.class );

     assertThat( actual ).isSameAs( extensionAssert );
  }

  @Test
  public void isInstantiableWithUnknownType() {
    Throwable actual = thrown( new Actor() {
      @Override
      public void act() throws Throwable {
        extensionAssert.isInstantiable( UNKNOWN, TestExtension.class );
      }
    } );

    assertThat( actual )
      .hasMessage( format( PATTERN_ERR_CREATE, TestExtension.class.getName(), null, UNKNOWN ) )
      .isInstanceOf( AssertionError.class );
  }

  @Test
  public void isInstantiableWithWrongType() {
    Throwable actual = thrown( new Actor() {
      @Override
      public void act() throws Throwable {
        extensionAssert.isInstantiable( TYPE, Runnable.class );
      }
    } );

    assertThat( actual )
      .hasMessage( format( PATTERN_ERR_CREATE, Runnable.class.getName(), TestExtension.class.getName(), TYPE ) )
      .isInstanceOf( AssertionError.class );
  }

  @Test
  @Ignore( "https://github.com/fappel/xiliary/issues/3" )
  public void hasChildrenWithNonEmptyValue() {
    ExtensionAssert actual = extensionAssert.hasChildrenWithNonEmptyValue( CHILD, 1 );

    assertThat( actual ).isSameAs( extensionAssert );
  }

  @Test
  @Ignore( "https://github.com/fappel/xiliary/issues/3" )
  public void hasChildrenWithNonEmptyValueWithWrongChildAmount() {
    Throwable actual = thrown( new Actor() {
      @Override
      public void act() throws Throwable {
        extensionAssert.hasChildrenWithNonEmptyValue( CHILD, 2 );
      }
    } );

    assertThat( actual )
      .hasMessage( format( PATTERN_WRONG_CHILD_SIZE, 2, CONTRIBUTION, CHILD, 1 ) )
      .isInstanceOf( AssertionError.class );
  }

  @Test
  public void hasChildWithAttributeValue() {
    ExtensionAssert actual = extensionAssert.hasChildWithAttributeValue( ATTRIBUTE_NAME, ATTRIBUTE_VALUE );

    assertThat( actual ).isSameAs( extensionAssert );
  }

  @Test
  public void hasChildWithAttributeValueThatDoesNotMatch() {
    Throwable actual = thrown( new Actor() {
      @Override
      public void act() throws Throwable {
        extensionAssert.hasChildWithAttributeValue( ATTRIBUTE_NAME, DOES_NOT_MATCH );
      }
    } );

    assertThat( actual )
      .hasMessage( format( PATTERN_WRONG_CHILD_SIZE_WITH_ATTRIB, ATTRIBUTE_NAME, DOES_NOT_MATCH, 0 ) )
      .isInstanceOf( AssertionError.class );
  }

  @Test
  public void hasChildWithAttributeValueWithTooManyMatches() {
    Throwable actual = thrown( new Actor() {
      @Override
      public void act() throws Throwable {
        extensionAssert.hasChildWithAttributeValue( "category", "A" );
      }
    } );

    assertThat( actual )
      .hasMessage( format( PATTERN_WRONG_CHILD_SIZE_WITH_ATTRIB, "category", "A", 2 ) )
      .isInstanceOf( AssertionError.class );
  }
}