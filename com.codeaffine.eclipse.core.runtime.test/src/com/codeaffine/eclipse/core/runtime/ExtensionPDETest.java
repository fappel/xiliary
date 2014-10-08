package com.codeaffine.eclipse.core.runtime;

import static com.codeaffine.eclipse.core.runtime.Predicates.attribute;
import static com.codeaffine.eclipse.core.runtime.TestExtension.EXTENSION_POINT;
import static com.codeaffine.eclipse.core.runtime.ThrowableCaptor.thrown;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;

import org.eclipse.core.runtime.CoreException;
import org.junit.Before;
import org.junit.Test;

import com.codeaffine.eclipse.core.runtime.ThrowableCaptor.Actor;

public class ExtensionPDETest {

  private Extension extension;

  @Before
  public void setUp() {
    extension = new RegistryAdapter()
      .readExtension()
      .ofContributionTo( EXTENSION_POINT )
      .thatMatches( attribute( "id", "1" ) )
      .process();
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
    Throwable expected = thrown( new Actor() {
      @Override
      public void act() throws Throwable {
        extension.createExecutableExtension( "unknown", TestExtension.class );
      }
    } );

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
}