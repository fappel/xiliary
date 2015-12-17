package com.codeaffine.eclipse.ui.swt.theme;

import java.util.Collection;
import java.util.HashSet;
import java.util.stream.Stream;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;

import com.codeaffine.eclipse.swt.widget.scrollable.ScrollbarStyle;

class ScrollbarStyleCollector {

  Collection<ScrollbarStyle> collect() {
    return collect( Display.getCurrent().getShells() );
  }

  private static Collection<ScrollbarStyle> collect( Control[] children ) {
    Collection<ScrollbarStyle> result = new HashSet<>();
    Stream.of( children ).forEach( child -> result.addAll( collect( child ) ) );
    return result;
  }

  private static Collection<ScrollbarStyle> collect( Control child ) {
    Collection<ScrollbarStyle> result = new HashSet<>();
    if( child instanceof ScrollbarStyle ) {
      result.add( ( ScrollbarStyle )child );
    }
    if( child instanceof Composite ){
      result.addAll( collect( ( ( Composite )child ).getChildren() ) );
    }
    return result;
  }
}