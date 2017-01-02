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

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;

import com.codeaffine.eclipse.swt.testhelper.LoremIpsum;

public class ScrolledCompositeHelper {

  public static ScrolledComposite createScrolledComposite( Composite parent ) {
    return createScrolledComposite( parent, SWT.H_SCROLL | SWT.V_SCROLL, LoremIpsum.PARAGRAPHS );
  }

  static ScrolledComposite createScrolledComposite( Composite parent, int style, String text ) {
    ScrolledComposite result = new ScrolledComposite( parent, style );
    Label label = new Label( result, SWT.NONE );
    label.setText( text );
    label.pack();
    label.setBackground( Display.getCurrent().getSystemColor( SWT.COLOR_DARK_GRAY ) );
    label.setForeground( Display.getCurrent().getSystemColor( SWT.COLOR_WHITE ) );
    result.setExpandHorizontal( true );
    result.setExpandVertical( true );
    result.setMinSize( label.getSize() );
    result.setContent( label );
    return result;
  }
}