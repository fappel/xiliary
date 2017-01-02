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
package com.codeaffine.eclipse.ui.progress;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;

public class DeferredContentJobFamilyTest {

  private DeferredContentManager contentManager;
  private DeferredContentJobFamily family;
  private Object element;

  @Before
  public void setUp() {
    element = new Object();
    contentManager = mock( DeferredContentManager.class );
    family = new DeferredContentJobFamily( contentManager, element );
  }

  @Test
  public void getSchedulingManager() {
    DeferredContentManager actual = family.getSchedulingManager();

    assertThat( actual ).isSameAs( contentManager );
  }

  @Test
  public void getElement() {
    Object actual = family.getElement();

    assertThat( actual ).isSameAs( element );
  }
}