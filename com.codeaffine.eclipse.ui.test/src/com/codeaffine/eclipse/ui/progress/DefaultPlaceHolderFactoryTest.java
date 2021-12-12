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
package com.codeaffine.eclipse.ui.progress;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class DefaultPlaceHolderFactoryTest {

  @Test
  public void create() {
    DefaultPlaceHolderFactory factory = new DefaultPlaceHolderFactory();

    PendingUpdatePlaceHolder actual = factory.create();

    assertThat( actual ).isInstanceOf( PendingUpdatePlaceHolder.class );
  }
}