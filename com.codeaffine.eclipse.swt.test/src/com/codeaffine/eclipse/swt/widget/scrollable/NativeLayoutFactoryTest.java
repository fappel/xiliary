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

import static com.codeaffine.eclipse.swt.widget.scrollable.NativeLayoutFactory.LAYOUT_TYPE;
import static org.assertj.core.api.Assertions.assertThat;

import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Scrollable;
import org.junit.Test;

public class NativeLayoutFactoryTest {

  @Test
  public void getLayout() {
    NativeLayoutFactory<Scrollable> factory = new NativeLayoutFactory<Scrollable>();

    Layout actual = factory.create( null );

    assertThat( actual ).isInstanceOf( LAYOUT_TYPE );
  }
}