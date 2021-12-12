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
package com.codeaffine.eclipse.ui.swt.theme;

import static com.codeaffine.eclipse.core.runtime.Predicates.attribute;
import static org.assertj.core.api.Assertions.assertThat;

import org.eclipse.ui.IStartup;
import org.junit.Test;

import com.codeaffine.eclipse.core.runtime.RegistryAdapter;

public class FontOnStartupLoaderPDETest {

  @Test
  public void extensionRegistration() {
    IStartup actual = new RegistryAdapter()
      .createExecutableExtension( "org.eclipse.ui.startup", IStartup.class )
      .thatMatches( attribute( "class", FontOnStartupLoader.class.getName() ) )
      .process();

    assertThat( actual ).isInstanceOf( FontOnStartupLoader.class );
  }
}