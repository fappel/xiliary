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
package com.codeaffine.eclipse.swt.widget.scrollable;

import static com.codeaffine.eclipse.swt.util.ControlReflectionUtil.$;
import static com.codeaffine.eclipse.swt.util.ControlReflectionUtil.CREATE_WIDGET;
import static com.codeaffine.eclipse.swt.util.ControlReflectionUtil.DISPLAY;
import static com.codeaffine.eclipse.swt.util.ControlReflectionUtil.PARENT;
import static com.codeaffine.eclipse.swt.util.ControlReflectionUtil.STYLE;
import static com.codeaffine.eclipse.swt.util.Platform.PlatformType.WIN32;
import static java.lang.Integer.valueOf;
import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableList;

import java.util.Collection;
import java.util.Optional;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Scrollable;

import com.codeaffine.eclipse.swt.util.ControlReflectionUtil;
import com.codeaffine.eclipse.swt.util.Platform;
import com.codeaffine.eclipse.swt.util.PlatformSupport;

public class ScrollableAdapterFactory {

  static final String ADAPTED = ScrollableAdapterFactory.class.getName() + "#adapted";
  static final Collection<Class<?>> SUPPORTED_TYPES = supportedTypes();

  private final ControlReflectionUtil reflectionUtil;
  private final PlatformSupport platformSupport;

  public interface Adapter<S extends Scrollable> {
    void adapt( S scrollable, PlatformSupport platformSupport );
    S getScrollable();
  }

  public ScrollableAdapterFactory() {
    reflectionUtil = new ControlReflectionUtil();
    platformSupport = new PlatformSupport( WIN32 );
  }

  public <S extends Scrollable, A extends Scrollable & Adapter<S>> Optional<A> create( S scrollable, Class<A> type ) {
    if( !usesScrollbars( scrollable ) ) {
      return Optional.empty();
    }
    ensureThatTypeIsSupported( type );
    return adapt( scrollable, type );
  }

  public <S extends Scrollable> void markAdapted( S scrollable ) {
    scrollable.setData( ADAPTED, Boolean.TRUE );
  }

  public boolean isAdapted( Scrollable scrollable ) {
    return Boolean.TRUE.equals( scrollable.getData( ADAPTED ) );
  }

  private static <S extends Scrollable> boolean usesScrollbars( S scrollable ) {
    int scrollableStyle = scrollable.getStyle();
    return ( scrollableStyle & SWT.H_SCROLL ) > 0 || ( scrollableStyle & SWT.V_SCROLL ) > 0;
  }

  private <A extends Scrollable & Adapter<S>, S extends Scrollable> Optional<A> adapt( S scrollable, Class<A> type ) {
    Composite parent = scrollable.getParent();
    int ordinalNumber = captureDrawingOrderOrdinalNumber( scrollable );
    A result = createAdapter( scrollable, type );
    if( platformSupport.isGranted() ) {
      markAdapted( scrollable );
      applyDrawingOrderOrdinalNumber( result, ordinalNumber );
    }
    result.adapt( scrollable, platformSupport );
    if( platformSupport.isGranted() ) {
      parent.layout();
      result.setBackground( scrollable.getBackground() );
      reflectionUtil.setField( scrollable, ControlReflectionUtil.PARENT, parent );
    }
    return Optional.of( result );
  }

  @SuppressWarnings("unchecked")
  static <T extends Scrollable> LayoutFactory<T> createLayoutFactory(
    Platform platform, LayoutMapping<T> ... mappings )
  {
    for( LayoutMapping<T> layoutMapping : mappings ) {
      if( platform.matchesOneOf( layoutMapping.getPlatformTypes() ) ) {
        return layoutMapping.getLayoutFactory();
      }
    }
    return new NativeLayoutFactory<T>();
  }

  private static <A extends Adapter<? extends Scrollable>> void ensureThatTypeIsSupported( Class<A> type ) {
    if( !SUPPORTED_TYPES.contains( type ) ) {
      throw new IllegalArgumentException( format( "Scrollable type <%s> is not supported.", type ) );
    }
  }

  private static int captureDrawingOrderOrdinalNumber( Control control ) {
    for( int i = 0; i < control.getParent().getChildren().length; i++ ) {
      if( control.getParent().getChildren()[ i ] == control ) {
        return i;
      }
    }
    throw new IllegalStateException( "Control is not contained in its parent's children list: " + control );
  }

  private <S extends Scrollable, A extends Scrollable & Adapter<S>> A createAdapter( S scrollable, Class<A> type ) {
    int style = SWT.BORDER & scrollable.getStyle();
    A result = reflectionUtil.newInstance( type );
    if( platformSupport.isGranted() ) {
      reflectionUtil.setField( result, DISPLAY, Display.getCurrent() );
      reflectionUtil.setField( result, PARENT, scrollable.getParent() );
      reflectionUtil.setField( result, STYLE, Integer.valueOf( style ) );
      reflectionUtil.invoke( result, CREATE_WIDGET, $( valueOf( 0 ), int.class ) );
    }
    return result;
  }

  private static void applyDrawingOrderOrdinalNumber( Control control, int ordinalNumber ) {
    control.moveAbove( control.getParent().getChildren()[ ordinalNumber ] );
  }

  private static Collection<Class<?>> supportedTypes() {
    return unmodifiableList( asList(
      TreeAdapter.class,
      TableAdapter.class,
      StyledTextAdapter.class,
      ScrolledCompositeAdapter.class ) );
  }
}