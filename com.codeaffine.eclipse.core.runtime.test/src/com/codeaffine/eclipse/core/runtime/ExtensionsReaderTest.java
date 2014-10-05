package com.codeaffine.eclipse.core.runtime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;

import com.codeaffine.eclipse.core.runtime.internal.Operator.ReadExtensionsOperator;

@SuppressWarnings("unchecked")
public class ExtensionsReaderTest {

  private ExtensionsReader collector;
  private ReadExtensionsOperator<Extension> operator;

  @Before
  public void setUp() {
    operator = mock( ReadExtensionsOperator.class );
    collector = new ExtensionsReader( operator );
  }

  @Test
  public void forEachContributionTo() {
    String expected = "id";

    MultiProcessor<Extension> actual = collector.forEachContributionTo( expected );

    assertThat( actual ).isNotNull();
    verify( operator ).setExtensionPointId( expected );
  }

  @Test( expected = IllegalArgumentException.class )
  public void forEachContributionToWithNullAsExtensionPointId() {
    collector.forEachContributionTo( null );
  }
}