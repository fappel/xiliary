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

import org.eclipse.e4.ui.css.swt.dom.ControlElement;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Scrollable;

@SuppressWarnings("restriction")
class ControlElements {

  static boolean hasControlElementOfType( Object element, Class<? extends Control> expectedType ) {
    return isControlElement( element ) && expectedType.isInstance( extractControl( element ) );
  }

  static boolean isControlElement( Object element ) {
    return element instanceof ControlElement;
  }

  static Scrollable extractScrollable( Object element ) {
    return ( Scrollable )extractControl( element );
  }

  static Control extractControl( Object element ) {
    ControlElement controlElement = ( ControlElement )element;
    return ( Control )controlElement.getNativeWidget();
  }
}