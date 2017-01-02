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

import static com.codeaffine.eclipse.swt.widget.scrollable.StyledTextHelper.createStyledText;

import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Composite;

public class TestStyledTextFactory implements ScrollableFactory<StyledText> {

  private StyledText styledText;

  @Override
  public StyledText create( Composite parent ) {
    styledText = createStyledText( parent );
    return styledText;
  }

  public StyledText getStyledText() {
    return styledText;
  }
}