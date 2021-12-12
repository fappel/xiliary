/**
 * Copyright (c) 2014 - 2022 Frank Appel
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Frank Appel - initial API and implementation
 */
package com.codeaffine.eclipse.swt.layout;

import static com.codeaffine.test.util.lang.ThrowableCaptor.thrownBy;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.junit.Before;
import org.junit.Test;

public class LayoutWrapperTest {

  private static final Composite COMPOSITE = mock( Composite.class );
  private static final boolean FLUSH_CACHE = true;
  private static final int HEIGHT_HINT = 12;
  private static final int WIDTH_HINT = 10;

  private LayoutWrapper wrapper;
  private LayoutSpy layout;

  @Before
  public void setUp() {
    layout = new LayoutSpy();
    wrapper = new LayoutWrapper( layout );
  }

  @Test
  public void computeSize() {
    Point actual = wrapper.computeSize( COMPOSITE, WIDTH_HINT, HEIGHT_HINT, FLUSH_CACHE );

    assertThat( actual ).isSameAs( LayoutSpy.COMPUTE_SIZE_RESULT );
    assertThat( layout.getComposite() ).isSameAs( COMPOSITE );
    assertThat( layout.getWidthHint() ).isEqualTo( WIDTH_HINT );
    assertThat( layout.getHeightHint() ).isEqualTo( HEIGHT_HINT );
    assertThat( layout.isFlushCache() ).isEqualTo( FLUSH_CACHE );
  }

  @Test
  public void computeSizeWithRuntimeExceptionInWrappedLayout() {
    layout.throwRuntimeExceptionOnMethodInvocations();

    Throwable actual = thrownBy( () -> wrapper.computeSize( COMPOSITE, WIDTH_HINT, HEIGHT_HINT, FLUSH_CACHE ) );

    assertThat( actual ).isSameAs( layout.getRuntimeException() );
  }

  @Test
  public void layout() {
    wrapper.layout( COMPOSITE, FLUSH_CACHE );

    assertThat( layout.getComposite() ).isSameAs( COMPOSITE );
    assertThat( layout.isFlushCache() ).isEqualTo( FLUSH_CACHE );
  }

  @Test
  public void layoutWithRuntimeExceptionInWrappedLayout() {
    layout.throwRuntimeExceptionOnMethodInvocations();

    Throwable actual = thrownBy( () -> wrapper.layout( COMPOSITE, FLUSH_CACHE ) );

    assertThat( actual ).isSameAs( layout.getRuntimeException() );
  }
}