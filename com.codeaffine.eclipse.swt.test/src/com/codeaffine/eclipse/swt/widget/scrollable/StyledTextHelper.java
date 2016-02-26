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

import static com.codeaffine.eclipse.swt.testhelper.LoremIpsum.PARAGRAPHS;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Composite;

public class StyledTextHelper {

  public static StyledText createStyledText( Composite parent ) {
    return createStyledText( parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL );
  }

  public static StyledText createStyledText( Composite parent, int style ) {
    StyledText result = new StyledText( parent, style );
    result.setText( PARAGRAPHS );
    return result;
  }
}