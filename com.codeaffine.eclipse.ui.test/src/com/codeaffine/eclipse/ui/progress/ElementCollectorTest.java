package com.codeaffine.eclipse.ui.progress;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.junit.Before;
import org.junit.Test;

public class ElementCollectorTest {

  private DeferredContentManager contentManager;
  private PendingUpdatePlaceHolder placeHolder;
  private ElementCollector collector;
  private Object parent;

  @Before
  public void setUp() {
    parent = new Object();
    placeHolder = new PendingUpdatePlaceHolder();
    contentManager = mock( DeferredContentManager.class );
    collector = new ElementCollector( contentManager, parent, placeHolder );
  }

  @Test
  public void addSingleElement() {
    Object expected = new Object();

    collector.add( expected, new NullProgressMonitor() );

    verify( contentManager ).addChildren( eq( parent ), eq( new Object[] { expected } ) );
  }

  @Test
  public void addMultipleElements() {
    Object[] expected = { new Object() };

    collector.add( expected, new NullProgressMonitor() );

    verify( contentManager ).addChildren( parent, expected );
  }

  @Test
  public void done() {
    collector.done();

    verify( contentManager ).clearPlaceholder( placeHolder );
  }
}