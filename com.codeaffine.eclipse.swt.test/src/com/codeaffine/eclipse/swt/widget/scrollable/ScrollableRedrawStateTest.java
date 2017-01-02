/**
 * Copyright (c) 2014 - 2017 Frank Appel
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Frank Appel - initial API and implementation
 */
package com.codeaffine.eclipse.swt.widget.scrollable;

import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;

import com.codeaffine.eclipse.swt.widget.scrollable.context.ScrollableControl;

public class ScrollableRedrawStateTest {

  private ScrollableRedrawState redrawState;
  private ScrollableControl<?> scrollable;

  @Before
  public void setUp() {
    scrollable = mock( ScrollableControl.class );
    redrawState = new ScrollableRedrawState( scrollable );
  }

  @Test
  public void updateWithRedrawSuspension() {
    redrawState.update( false );

    verify( scrollable ).setRedraw( false );
  }

  @Test
  public void updateWithRedrawSuspensionIfAlreadySuspended() {
    redrawState.update( false );
    reset( new ScrollableControl[] { scrollable } );

    redrawState.update( false );

    verify( scrollable, never() ).setRedraw( anyBoolean() );
  }

  @Test
  public void updateToResolveRedrawSuspension() {
    redrawState.update( false );
    reset( new ScrollableControl[] { scrollable } );

    redrawState.update( true );

    verify( scrollable ).setRedraw( true );
  }

  @Test
  public void updateToResolveRedrawSuspensionIfAlreadyResolved() {
    redrawState.update( false );
    redrawState.update( true );
    reset( new ScrollableControl[] { scrollable } );

    redrawState.update( true );

    verify( scrollable, never() ).setRedraw( anyBoolean() );
  }
}