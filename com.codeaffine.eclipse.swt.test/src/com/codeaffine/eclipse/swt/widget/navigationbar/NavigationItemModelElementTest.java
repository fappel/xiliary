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
package com.codeaffine.eclipse.swt.widget.navigationbar;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class NavigationItemModelElementTest {

  private static final String DISPLAY_NAME = "displayName";
  private static final String ID = "id";

  @Test
  public void getId() {
    NavigationItemModelElement element = new NavigationItemModelElement( ID, DISPLAY_NAME );

    String actual = element.getId();

    assertThat( actual ).isEqualTo( ID );
  }

  @Test
  public void getDisplayName() {
    NavigationItemModelElement element = new NavigationItemModelElement( ID, DISPLAY_NAME );

    String actual = element.getDisplayName();

    assertThat( actual ).isEqualTo( DISPLAY_NAME );
  }

  @Test( expected = IllegalArgumentException.class )
  public void constructWithNullAsId() {
    new NavigationItemModelElement( null, DISPLAY_NAME );
  }

  @Test( expected = IllegalArgumentException.class )
  public void constructWithNullAsDisplayNahem() {
    new NavigationItemModelElement( ID, null );
  }
}