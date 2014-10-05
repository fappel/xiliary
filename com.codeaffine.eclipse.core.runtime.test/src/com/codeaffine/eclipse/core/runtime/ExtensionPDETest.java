package com.codeaffine.eclipse.core.runtime;

import static com.codeaffine.eclipse.core.runtime.TestExtension.EXTENSION_POINT;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;

import org.eclipse.core.runtime.Platform;
import org.junit.Before;
import org.junit.Test;

public class ExtensionPDETest {

  private Extension extension;

  @Before
  public void setUp() {
    RegistryAdapter adapter = new RegistryAdapter( Platform.getExtensionRegistry() );
    extension = adapter.readExtension()
      .ofContributionTo( EXTENSION_POINT )
      .thatMatches( new FirstTestContributionPredicate() )
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
}