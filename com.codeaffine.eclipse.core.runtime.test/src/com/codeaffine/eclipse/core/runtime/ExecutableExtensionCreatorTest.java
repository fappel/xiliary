package com.codeaffine.eclipse.core.runtime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;

import com.codeaffine.eclipse.core.runtime.internal.Operator.CreateExecutableExtensionOperator;

@SuppressWarnings("unchecked")
public class ExecutableExtensionCreatorTest {

  private ExecutableExtensionCreator<Runnable> creator;
  private CreateExecutableExtensionOperator<Runnable> operator;

  @Before
  public void setUp() {
    operator = mock( CreateExecutableExtensionOperator.class );
    creator = new ExecutableExtensionCreator<Runnable>( operator );
  }

  @Test
  public void withConfiguration() {
    ExecutableExtensionConfigurator<Runnable> configurator = mock( ExecutableExtensionConfigurator.class );

    ExecutableExtensionCreator<Runnable> actual = creator.withConfiguration( configurator );

    assertThat( actual ).isSameAs( creator );
    verify( operator ).setConfigurator( configurator );
  }

  @Test( expected = IllegalArgumentException.class )
  public void withConfigurationWithNullAsConfigurator() {
    creator.withConfiguration( null );
  }

  @Test
  public void withExceptionHandler() {
    ExtensionExceptionHandler handler
      = mock( ExtensionExceptionHandler.class );

    ExecutableExtensionCreator<Runnable> actual = creator.withExceptionHandler( handler );

    assertThat( actual ).isSameAs( creator );
    verify( operator ).setExceptionHandler( handler );

  }

  @Test( expected = IllegalArgumentException.class )
  public void withExceptionHandlerWithNullAsExceptionHandler() {
    creator.withExceptionHandler( null );
  }

  @Test
  public void withTypeAttribute() {
    String attributeType = "type";

    ExecutableExtensionCreator<Runnable> actual = creator.withTypeAttribute( attributeType );

    assertThat( actual ).isSameAs( creator );
    verify( operator ).setTypeAttribute( attributeType );

  }

  @Test( expected = IllegalArgumentException.class )
  public void withTypeAttributeWithNullAsTypeAttribute() {
    creator.withTypeAttribute( null );
  }

  @Test
  public void ofContributionTo() {
    String expected = "id";

    SingleProcessor<Runnable> actual = creator.ofContributionTo( expected );

    assertThat( actual ).isNotNull();
    verify( operator ).setExtensionPointId( expected );
  }

  @Test( expected = IllegalArgumentException.class )
  public void ofContributionToWithNullAsExtensionPointId() {
    creator.ofContributionTo( null );
  }
}