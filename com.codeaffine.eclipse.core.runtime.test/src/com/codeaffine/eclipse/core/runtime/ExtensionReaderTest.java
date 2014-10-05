package com.codeaffine.eclipse.core.runtime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;

import com.codeaffine.eclipse.core.runtime.internal.Operator.ReadExtensionOperator;

@SuppressWarnings("unchecked")
public class ExtensionReaderTest {

  private ExtensionReader extensionReader;
  private ReadExtensionOperator<Extension> operator;

  @Before
  public void setUp() {
    operator = mock( ReadExtensionOperator.class );
    extensionReader = new ExtensionReader( operator );
  }

  @Test
  public void ofContributionTo() {
    String expected = "id";

    SingleProcessor<Extension> actual = extensionReader.ofContributionTo( expected );

    assertThat( actual ).isNotNull();
    verify( operator ).setExtensionPointId( expected );
  }

  @Test( expected = IllegalArgumentException.class )
  public void ofContributionToWithNullAsExtensionPointId() {
     extensionReader.ofContributionTo( null );
  }
}