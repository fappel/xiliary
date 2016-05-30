/**
 * Copyright (c) 2014 - 2016 Frank Appel
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Frank Appel - initial API and implementation
 */
package com.codeaffine.eclipse.swt.widget.scrollable;

import static com.codeaffine.eclipse.swt.test.util.ShellHelper.createShell;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.eclipse.swt.test.util.DisplayHelper;
import com.codeaffine.eclipse.swt.widget.scrollable.context.AdaptionContext;

public class CompositeSizeObserverTest {

  @Rule
  public final DisplayHelper displayHelper = new DisplayHelper();

  private CompositeSizeObserver observer;
  private Composite composite;

  @Before
  public void setUp() {
    composite = new Composite( createShell( displayHelper ), SWT.H_SCROLL | SWT.V_SCROLL );
    observer = new CompositeSizeObserver( composite );
  }

  @Test
  public void mustLayoutAdapter() {
    boolean actual = observer.mustLayoutAdapter( mock( AdaptionContext.class ) );

    assertThat( actual ).isFalse();
  }

  @Test
  public void mustLayoutAdapterAfterResize() {
    composite.setSize( 100, 200 );

    boolean actual = observer.mustLayoutAdapter( mock( AdaptionContext.class ) );

    assertThat( actual ).isTrue();
  }

  @Test
  public void mustLayoutAdapterAfterResizeWasConsumed() {
    composite.setSize( 100, 200 );

    boolean first = observer.mustLayoutAdapter( mock( AdaptionContext.class ) );
    boolean second = observer.mustLayoutAdapter( mock( AdaptionContext.class ) );

    assertThat( first ).isTrue();
    assertThat( second ).isFalse();
  }
}