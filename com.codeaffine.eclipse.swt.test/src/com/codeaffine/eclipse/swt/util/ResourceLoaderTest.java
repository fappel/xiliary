package com.codeaffine.eclipse.swt.util;

import static com.codeaffine.eclipse.swt.testhelper.TestResources.LOCATION;
import static com.codeaffine.test.util.lang.ThrowableCaptor.thrownBy;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

public class ResourceLoaderTest {

  private static final String UNKNOWN_PATH = "unknown";

  @Test
  public void load() throws IOException {
    int expectedLength = calculateExpectedSize( LOCATION );

    byte[] actual = new ResourceLoader().load( LOCATION );

    assertThat( actual.length ).isEqualTo( expectedLength );
  }

  @Test
  public void loadWithClassLoader() throws IOException {
    int expectedLength = calculateExpectedSize( LOCATION );

    byte[] actual = new ResourceLoader().load( LOCATION, getClass().getClassLoader() );

    assertThat( actual.length ).isEqualTo( expectedLength );
  }

  @Test
  public void loadUnkownResource() {
    Throwable actual = thrownBy( () ->  new ResourceLoader().load( UNKNOWN_PATH ) );

    assertThat( actual )
      .isInstanceOf( IllegalArgumentException.class )
      .hasMessageContaining( UNKNOWN_PATH );
  }

  private static int calculateExpectedSize( String location ) throws IOException {
    try( InputStream input = ResourceLoaderTest.class.getClassLoader().getResourceAsStream( location ); ) {
      return input.available();
    }
  }
}