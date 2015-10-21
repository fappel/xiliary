package com.codeaffine.eclipse.core.runtime.internal;

import static com.codeaffine.eclipse.core.runtime.Predicates.alwaysFalse;
import static com.codeaffine.eclipse.core.runtime.Predicates.alwaysTrue;
import static com.codeaffine.eclipse.core.runtime.TestExtension.EXTENSION_POINT;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.function.Consumer;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.junit.Before;
import org.junit.Test;

public class ContributionElementLoopPDETest {

  private Consumer<IConfigurationElement> handler;
  private ContributionElementLoop loop;

  @Before
  @SuppressWarnings("unchecked")
  public void setUp() {
    loop = new ContributionElementLoop( Platform.getExtensionRegistry() );
    handler = mock( Consumer.class );
  }

  @Test
  public void forEach() {
    loop.forEach( EXTENSION_POINT, alwaysTrue(), handler );

    verify( handler, times( 2 ) ).accept( any( IConfigurationElement.class ) );
  }

  @Test
  public void forEachWithFilter() {
    loop.forEach( EXTENSION_POINT, alwaysFalse(), handler );

    verify( handler, never() ).accept( any( IConfigurationElement.class ) );
  }

  @Test
  public void forEachWithUnknownExtensionPoint() {
    loop.forEach( "unknown", alwaysTrue(), handler );

    verify( handler, never() ).accept( any( IConfigurationElement.class ) );
  }
}