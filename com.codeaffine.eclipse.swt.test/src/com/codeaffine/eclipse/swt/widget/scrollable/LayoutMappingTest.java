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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import org.eclipse.swt.widgets.Scrollable;
import org.junit.Before;
import org.junit.Test;

import com.codeaffine.eclipse.swt.util.Platform.PlatformType;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class LayoutMappingTest {

  private LayoutMapping<Scrollable> mapping;
  private LayoutFactory layoutFactory;

  @Before
  public void setUp() {
    layoutFactory = mock( LayoutFactory.class );
    mapping = new LayoutMapping<Scrollable>( layoutFactory, PlatformType.values() );
  }
  @Test
  public void getLayoutFactory() {
    LayoutFactory<Scrollable> actual = mapping.getLayoutFactory();

    assertThat( actual ).isSameAs( layoutFactory );
  }

  @Test
  public void getPlatformTypes() {
    PlatformType[] actual = mapping.getPlatformTypes();

    assertThat( actual ).containsExactly( PlatformType.values() );
  }
}