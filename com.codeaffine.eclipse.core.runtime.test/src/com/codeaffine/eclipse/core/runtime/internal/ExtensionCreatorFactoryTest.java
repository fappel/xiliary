package com.codeaffine.eclipse.core.runtime.internal;

import static com.codeaffine.eclipse.core.runtime.ExtensionExceptionHandler.DEFAULT_HANDLER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import org.eclipse.core.runtime.IConfigurationElement;
import org.junit.Test;

import com.codeaffine.eclipse.core.runtime.ExecutableExtensionConfigurator;
import com.codeaffine.eclipse.core.runtime.ExecutableExtensionConfigurator.DefaultConfigurator;
import com.codeaffine.eclipse.core.runtime.ExtensionExceptionHandler;

public class ExtensionCreatorFactoryTest {

  @Test
  public void create() {
    IConfigurationElement element = mock( IConfigurationElement.class );
    ExtensionCreatorFactory<Runnable> factory = new ExtensionCreatorFactory<Runnable>();
    Class<Runnable> extensionType = Runnable.class;
    ExtensionExceptionHandler exceptionHandler = DEFAULT_HANDLER;
    ExecutableExtensionConfigurator<Runnable> configurator = new DefaultConfigurator<Runnable>();
    String typeAttribute = "class";

    ExtensionCreator<Runnable> actual
      = factory.create( element, extensionType, exceptionHandler, configurator, typeAttribute );

    assertThat( actual.element ).isSameAs( element );
    assertThat( actual.extensionType ).isSameAs( extensionType );
    assertThat( actual.exceptionHandler ).isSameAs( exceptionHandler );
    assertThat( actual.configurator ).isSameAs( configurator );
    assertThat( actual.typeAttribute ).isSameAs( typeAttribute );
  }
}