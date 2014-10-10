package com.codeaffine.eclipse.core.runtime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;

import com.codeaffine.eclipse.core.runtime.internal.Operator.CreateExecutableExtensionsOperator;

@SuppressWarnings("unchecked")
public class CreateMultiProcessorTest {

  private CreateExecutableExtensionsOperator<Runnable> operator;
  private CreateMultiProcessor<Runnable> processor;

  @Before
  public void setUp() {
    operator = mock( CreateExecutableExtensionsOperator.class );
    processor = new CreateMultiProcessor<Runnable>( operator );
  }

  @Test
  public void withConfiguration() {
    ExecutableExtensionConfigurator<Runnable> configurator
      = mock( ExecutableExtensionConfigurator.class );

    CreateMultiProcessor<Runnable> actual = processor.withConfiguration( configurator );

    assertThat( actual ).isSameAs( processor );
    verify( operator ).setConfigurator( configurator );
  }

  @Test( expected = IllegalArgumentException.class )
  public void withConfigurationWithNullAsConfigurator() {
    processor.withConfiguration( null );
  }

  @Test
  public void withExceptionHandler() {
    ExtensionExceptionHandler handler
      = mock( ExtensionExceptionHandler.class );

    CreateMultiProcessor<Runnable> actual = processor.withExceptionHandler( handler );

    assertThat( actual ).isSameAs( processor );
    verify( operator ).setExceptionHandler( handler );
  }

  @Test( expected = IllegalArgumentException.class )
  public void withExceptionHandlerWithNullAsExceptionHandler() {
    processor.withExceptionHandler( null );
  }

  @Test
  public void withTypeAttribute() {
    String typeAttribute = "type";

    CreateMultiProcessor<Runnable> actual = processor.withTypeAttribute( typeAttribute );

    assertThat( actual ).isSameAs( processor );
    verify( operator ).setTypeAttribute( typeAttribute );
  }

  @Test( expected = IllegalArgumentException.class )
  public void withTypeAttributeWithNullAsTypeAttribute() {
    processor.withTypeAttribute( null );
  }
}