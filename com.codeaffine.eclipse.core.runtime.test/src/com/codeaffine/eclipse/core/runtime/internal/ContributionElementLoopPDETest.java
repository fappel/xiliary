package com.codeaffine.eclipse.core.runtime.internal;

import static com.codeaffine.eclipse.core.runtime.Predicates.alwaysFalse;
import static com.codeaffine.eclipse.core.runtime.Predicates.alwaysTrue;
import static com.codeaffine.eclipse.core.runtime.TestExtension.EXTENSION_POINT;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.junit.Before;
import org.junit.Test;

import com.codeaffine.eclipse.core.runtime.internal.ContributionElementLoop.ConfigurationElementHandler;

public class ContributionElementLoopPDETest {

  private ContributionElementLoop loop;

  @Before
  public void setUp() {
    loop = new ContributionElementLoop( Platform.getExtensionRegistry() );
  }

  @Test
  public void forEach() {
    ConfigurationElementHandler handler = mock( ConfigurationElementHandler.class );

    loop.forEach( EXTENSION_POINT, alwaysTrue(), handler );

    verify( handler, times( 2 ) ).handle( any( IConfigurationElement.class ) );
  }

  @Test
  public void forEachWithFilter() {
    ConfigurationElementHandler handler = mock( ConfigurationElementHandler.class );

    loop.forEach( EXTENSION_POINT, alwaysFalse(), handler );

    verify( handler, never() ).handle( any( IConfigurationElement.class ) );
  }

  @Test
  public void forEachWithUnknownExtensionPoint() {
    ConfigurationElementHandler handler = mock( ConfigurationElementHandler.class );

    loop.forEach( "unknown", alwaysTrue(), handler );

    verify( handler, never() ).handle( any( IConfigurationElement.class ) );
  }
}