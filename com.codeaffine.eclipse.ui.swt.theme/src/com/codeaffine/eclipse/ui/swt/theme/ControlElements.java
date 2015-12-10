package com.codeaffine.eclipse.ui.swt.theme;

import org.eclipse.e4.ui.css.swt.dom.ControlElement;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Scrollable;

@SuppressWarnings("restriction")
class ControlElements {

  static Scrollable extractScrollable( Object element ) {
    return ( Scrollable )extractControl( element );
  }

  static Control extractControl( Object element ) {
    ControlElement controlElement = ( ControlElement )element;
    return ( Control )controlElement.getNativeWidget();
  }
}