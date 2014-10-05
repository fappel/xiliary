package com.codeaffine.eclipse.core.runtime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;

import com.codeaffine.eclipse.core.runtime.internal.Operator.CreateExecutableExtensionsOperator;

@SuppressWarnings("unchecked")
public class ExecutableExtensionsCreatorTest {

  private ExecutableExtensionsCreator<Runnable> collector;
  private CreateExecutableExtensionsOperator<Runnable> operator;

  @Before
  public void setUp() {
    operator = mock( CreateExecutableExtensionsOperator.class );
    collector = new ExecutableExtensionsCreator<Runnable>( operator );
  }

  @Test
  public void withConfiguration() {
    ExecutableExtensionConfigurator<Runnable> configurator
      = mock( ExecutableExtensionConfigurator.class );

    ExecutableExtensionsCreator<Runnable> actual = collector.withConfiguration( configurator );

    assertThat( actual ).isSameAs( collector );
    verify( operator ).setConfigurator( configurator );
  }

  @Test( expected = IllegalArgumentException.class )
  public void withConfigurationWithNullAsConfigurator() {
    collector.withConfiguration( null );
  }

  @Test
  public void withExceptionHandler() {
    ExtensionExceptionHandler handler
      = mock( ExtensionExceptionHandler.class );

    ExecutableExtensionsCreator<Runnable> actual = collector.withExceptionHandler( handler );

    assertThat( actual ).isSameAs( collector );
    verify( operator ).setExceptionHandler( handler );
  }

  @Test( expected = IllegalArgumentException.class )
  public void withExceptionHandlerWithNullAsExceptionHandler() {
    collector.withExceptionHandler( null );
  }

  @Test
  public void withTypeAttribute() {
    String typeAttribute = "type";

    ExecutableExtensionsCreator<Runnable> actual = collector.withTypeAttribute( typeAttribute );

    assertThat( actual ).isSameAs( collector );
    verify( operator ).setTypeAttribute( typeAttribute );
  }

  @Test( expected = IllegalArgumentException.class )
  public void withTypeAttributeWithNullAsTypeAttribute() {
    collector.withTypeAttribute( null );
  }

  @Test
  public void forEachContributionTo() {
    String expected = "id";

    MultiProcessor<Runnable> actual = collector.forEachContributionTo( expected );

    assertThat( actual ).isNotNull();
    verify( operator ).setExtensionPointId( expected );
  }

  @Test( expected = IllegalArgumentException.class )
  public void forEachContributionToWithNullAsExtensionPointId() {
    collector.forEachContributionTo( null );
  }
}