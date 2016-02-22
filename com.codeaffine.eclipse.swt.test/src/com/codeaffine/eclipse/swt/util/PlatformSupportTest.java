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
package com.codeaffine.eclipse.swt.util;

import static com.codeaffine.eclipse.swt.util.PlatformTypeHelper.getCurrentType;
import static com.codeaffine.eclipse.swt.util.PlatformTypeHelper.getUnusedTypes;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

import com.codeaffine.eclipse.swt.util.Platform.PlatformType;

public class PlatformSupportTest {

  private PlatformType currentType;
  private PlatformType unusedType;

  @Before
  public void setUp() {
    currentType = getCurrentType();
    unusedType = getUnusedTypes()[ 0 ];
  }

  @Test
  public void isGrantedOnSupportedPlatforms() {
    PlatformSupport support = new PlatformSupport( currentType );

    boolean actual = support.isGranted();

    assertThat( actual ).isTrue();
  }

  @Test
  public void isGrantedOnUnsupportedPlatforms() {
    PlatformSupport support = new PlatformSupport( unusedType );

    boolean actual = support.isGranted();

    assertThat( actual ).isFalse();
  }

  @Test
  public void getSupportedTypes() {
    PlatformSupport support = new PlatformSupport( currentType );

    PlatformType[] actual = support.getSupportedTypes();

    assertThat( actual ).containsExactly( currentType );
  }
}