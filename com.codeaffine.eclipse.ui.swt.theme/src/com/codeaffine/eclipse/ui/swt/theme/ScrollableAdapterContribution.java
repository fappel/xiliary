package com.codeaffine.eclipse.ui.swt.theme;

import static java.lang.Boolean.parseBoolean;
import static org.eclipse.e4.ui.css.swt.helpers.CSSSWTColorHelper.getRGBA;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

import org.eclipse.e4.ui.css.core.dom.properties.ICSSPropertyHandler;
import org.eclipse.e4.ui.css.core.engine.CSSEngine;
import org.eclipse.e4.ui.css.swt.dom.ControlElement;
import org.eclipse.jface.resource.ColorRegistry;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Scrollable;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Tree;
import org.w3c.dom.css.CSSValue;

import com.codeaffine.eclipse.swt.widget.scrollable.ScrollableAdapterFactory;
import com.codeaffine.eclipse.swt.widget.scrollable.ScrollableAdapterFactory.Adapter;
import com.codeaffine.eclipse.swt.widget.scrollable.ScrollbarStyle;
import com.codeaffine.eclipse.swt.widget.scrollable.TableAdapter;
import com.codeaffine.eclipse.swt.widget.scrollable.TreeAdapter;

@SuppressWarnings("restriction")
public class ScrollableAdapterContribution implements ICSSPropertyHandler {

  public static final String FLAT_SCROLL_BAR_PAGE_INCREMENT = "flat-scroll-bar-page-increment";
  public static final String FLAT_SCROLL_BAR_BACKGROUND = "flat-scroll-bar-background";
  public static final String FLAT_SCROLL_BAR_THUMB = "flat-scroll-bar-thumb";
  public static final String FLAT_SCROLL_BAR = "flat-scroll-bar";
  public static final String TOP_LEVEL_WINDOW_SELECTOR = "-top-level";

  @SuppressWarnings( { "unchecked", "rawtypes" } )
  static final TypePair<? extends Scrollable, ? extends Adapter>[] SUPPORTED_ADAPTERS = new TypePair[] {
    new TypePair<>( Tree.class, TreeAdapter.class ),
    new TypePair<>( Table.class, TableAdapter.class ),
  };

  private static final String SCROLLABLE_STYLE = ScrollableAdapterContribution.class.getName() + "#SCROLLABLE_STYLE";

  private final ScrollableAdapterFactory factory;
  private final ColorRegistry colorRegistry;

  public ScrollableAdapterContribution() {
    factory = new ScrollableAdapterFactory();
    colorRegistry = new ColorRegistry();
  }

  @Override
  public boolean applyCSSProperty( Object element, String property, CSSValue value, String pseudo, CSSEngine engine )
    throws Exception
  {
    if( mustApply( element ) ) {
      doApplyCssProperty( element, property, value );
    }
    return false;
  }

  @Override
  public String retrieveCSSProperty( Object element, String property, String pseudo, CSSEngine engine )
    throws Exception
  {
    return null;
  }

  private void doApplyCssProperty( Object element, String property, CSSValue value ) {
    switch( property ) {
      case FLAT_SCROLL_BAR:
        adapt( extractScrollable( element ), value );
        applyColor( element, FLAT_SCROLL_BAR_BACKGROUND, ( style, color ) -> style.setBackgroundColor( color ) );
        applyColor( element, FLAT_SCROLL_BAR_THUMB, ( style, color ) -> style.setThumbColor( color ) );
        applyColor( element, FLAT_SCROLL_BAR_PAGE_INCREMENT, ( style, color ) -> style.setPageIncrementColor( color ) );
        break;
      case FLAT_SCROLL_BAR_BACKGROUND:
        applyColor( element, value, property, ( style, color ) -> style.setBackgroundColor( color ) );
        break;
      case FLAT_SCROLL_BAR_THUMB:
        applyColor( element, value, property, ( style, color ) -> style.setThumbColor( color ) );
        break;
      case FLAT_SCROLL_BAR_PAGE_INCREMENT:
        applyColor( element, value, property, ( style, color ) -> style.setPageIncrementColor( color ) );
        break;
      case FLAT_SCROLL_BAR_BACKGROUND + TOP_LEVEL_WINDOW_SELECTOR:
        applyColor( element, value, property, ( style, color ) -> style.setBackgroundColor( color ) );
        break;
      case FLAT_SCROLL_BAR_THUMB + TOP_LEVEL_WINDOW_SELECTOR:
        applyColor( element, value, property, ( style, color ) -> style.setThumbColor( color ) );
        break;
      case FLAT_SCROLL_BAR_PAGE_INCREMENT + TOP_LEVEL_WINDOW_SELECTOR:
        applyColor( element, value, property, ( style, color ) -> style.setPageIncrementColor( color ) );
        break;
    }
  }

  private static boolean mustApply( Object element ) {
    return isControlElement( element ) && tryFindTypePair( extractControl( element ) ).isPresent();
  }

  private static boolean isControlElement( Object element ) {
    return element instanceof ControlElement;
  }

  @SuppressWarnings( { "rawtypes", "unchecked" } )
  private void adapt( Scrollable scrollable, CSSValue value ) {
    if( !factory.isAdapted( scrollable ) && parseBoolean( value.getCssText() ) ) {
      TypePair<? extends Scrollable, ? extends Adapter> typePair = lookupTypePair( scrollable );
      Scrollable scrollableExtension = typePair.scrollableType.cast( scrollable );
      ScrollbarStyle result = ( ScrollbarStyle )factory.create( scrollableExtension, typePair.adapterType );
      scrollable.setData( SCROLLABLE_STYLE, result );
    }
  }
  private void applyColor( Object element, String styleElement, BiConsumer<ScrollbarStyle, Color> changer ) {
    if( extractScrollable( element ).getData( styleElement ) != null ) {
      changer.accept( getAdapter( extractScrollable( element ) ), getColor( element, styleElement ) );
    }
  }

  private void applyColor( Object element, CSSValue value, String key, BiConsumer<ScrollbarStyle, Color> changer ) {
    ensureColorBuffering( value );
    Scrollable scrollable = extractScrollable( element );
    ColorApplicatorSwitch applicator = new ColorApplicatorSwitch( scrollable, key );
    if( factory.isAdapted( scrollable ) && getAdapter( scrollable ) != null ) {
      applicator.onDefault( () -> applyColor( changer, scrollable, value ) )
                .onTopLevelWindow( () -> applicator.buffer( key, getColor( value ) ) );
    } else {
      String topLevelKey = key.replaceAll( TOP_LEVEL_WINDOW_SELECTOR, "" );
      applicator.onDefault( () -> applicator.buffer( key, getColor( value ) ) )
                .onTopLevelWindow( () -> applicator.buffer( topLevelKey, getColor( value ) ) );
    }
    applicator.apply();
  }

  private void applyColor( BiConsumer<ScrollbarStyle, Color> changer, Scrollable scrollable, CSSValue value ) {
    changer.accept( getAdapter( scrollable ), getColor( value ) );
  }

  private static Scrollable extractScrollable( Object element ) {
    return ( Scrollable )extractControl( element );
  }

  private static Control extractControl( Object element ) {
    ControlElement controlElement = ( ControlElement )element;
    return ( Control )controlElement.getNativeWidget();
  }

  private void ensureColorBuffering( CSSValue value ) {
    if( !colorRegistry.hasValueFor( value.getCssText() ) ) {
      colorRegistry.put( value.getCssText(), getRGBA( value ).rgb );
    }
  }

  private static Color getColor( Object element, String styleElement ) {
    return ( Color )extractScrollable( element ).getData( styleElement );
  }

  private Color getColor( CSSValue value ) {
    return colorRegistry.get( value.getCssText() );
  }

  @SuppressWarnings("rawtypes")
  private static TypePair<? extends Scrollable, ? extends Adapter> lookupTypePair( Scrollable scrollable ) {
    return tryFindTypePair( scrollable ).get();
  }

  @SuppressWarnings("rawtypes")
  private static Optional<TypePair<? extends Scrollable, ? extends Adapter>> tryFindTypePair( Control control ) {
    return Stream.of( SUPPORTED_ADAPTERS )
      .filter( typePair -> typePair.scrollableType == control.getClass() )
      .findFirst();
  }

  ScrollbarStyle getAdapter( Scrollable scrollable ) {
    return ( ScrollbarStyle )scrollable.getData( SCROLLABLE_STYLE );
  }
}