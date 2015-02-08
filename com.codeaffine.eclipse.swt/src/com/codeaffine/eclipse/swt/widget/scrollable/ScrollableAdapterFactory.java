package com.codeaffine.eclipse.swt.widget.scrollable;

import static java.lang.String.format;
import static java.util.Collections.unmodifiableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Scrollable;

public class ScrollableAdapterFactory {

  private final static Collection<Class<?>> SUPPORTED_TYPES = supportedTypes();

  private final ControlReflectionUtil reflectionUtil;

  public interface Adapter<S extends Scrollable> {
    void adapt( S scrollable );
  }

  public ScrollableAdapterFactory() {
    reflectionUtil = new ControlReflectionUtil();
  }

  public <S extends Scrollable, A extends Scrollable & Adapter<S>> A create( S scrollable, Class<A> type ) {
    ensureThatTypeIsSupported( type );

    int ordinalNumber = captureDrawingOrderOrdinalNumber( scrollable );
    A result = createAdapter( scrollable, type );
    applyDrawingOrderOrdinalNumber( result, ordinalNumber );
    result.adapt( scrollable );
    return result;
  }

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
    A result = reflectionUtil.newInstance( type );
    reflectionUtil.setField( result, "display", Display.getCurrent() );
    reflectionUtil.setField( result, "parent", scrollable.getParent() );
    reflectionUtil.invoke( result, "createWidget" );
    return result;
  }

  private static void applyDrawingOrderOrdinalNumber( Control control, int ordinalNumber ) {
    control.moveAbove( control.getParent().getChildren()[ ordinalNumber ] );
  }

  private static Collection<Class<?>> supportedTypes() {
    List<Class<?>> result = new ArrayList<Class<?>>();
    result.add( TreeAdapter.class );
    result.add( TableAdapter.class );
    return unmodifiableList( result );
  }
}