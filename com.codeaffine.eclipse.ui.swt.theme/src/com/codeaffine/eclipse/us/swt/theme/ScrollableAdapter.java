package com.codeaffine.eclipse.us.swt.theme;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.e4.ui.css.core.dom.properties.ICSSPropertyHandler;
import org.eclipse.e4.ui.css.core.engine.CSSEngine;
import org.eclipse.e4.ui.css.swt.dom.ControlElement;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Tree;
import org.w3c.dom.css.CSSValue;

import com.codeaffine.eclipse.swt.widget.scrollable.ScrollableAdapterFactory;
import com.codeaffine.eclipse.swt.widget.scrollable.ScrollbarStyle;
import com.codeaffine.eclipse.swt.widget.scrollable.TableAdapter;
import com.codeaffine.eclipse.swt.widget.scrollable.TreeAdapter;

@SuppressWarnings("restriction")
public class ScrollableAdapter implements ICSSPropertyHandler {

  private final Map<String, ScrollbarColorDefinition> colors;
  private final ScrollableAdapterFactory factory;

  public ScrollableAdapter() {
    factory = new ScrollableAdapterFactory();
    colors = new HashMap<String, ScrollbarColorDefinition>();
    colors.put( "win7", new ScrollbarColorDefinition( new RGB( 210, 225, 240 ), new RGB( 248, 248, 248 ) ) );
    colors.put( "dark", new ScrollbarColorDefinition( new RGB( 248, 248, 248 ), new RGB( 73, 74, 77 ) ) );
  }

  @Override
  public boolean applyCSSProperty( Object element, String property, CSSValue value, String pseudo, CSSEngine engine )
    throws Exception
  {
    if( ( element instanceof ControlElement ) ) {
      Object widget = getWidget( element );
      if( mustAdaptTree( widget ) ) {
        TreeAdapter treeAdapter = factory.create( ( Tree )widget, TreeAdapter.class );
        adjustScrollbarStyle( treeAdapter, value );
      }
      if( mustAdaptTable( widget ) ) {
        TableAdapter tableAdapter = factory.create( ( Table )widget, TableAdapter.class );
        adjustScrollbarStyle( tableAdapter, value );
      }
    }
    return false;
  }

  private boolean mustAdaptTable( Object widget ) {
    return    widget instanceof Table
           && !( widget instanceof TableAdapter )
           && !factory.isAdapted( ( Table )widget );
  }

  private boolean mustAdaptTree( Object widget ) {
    return    widget instanceof Tree
           && !( widget instanceof TreeAdapter )
           && !factory.isAdapted( ( Tree )widget );
  }

  @Override
  public String retrieveCSSProperty( Object element, String property, String pseudo, CSSEngine engine )
    throws Exception
  {
    return null;
  }

  private static Object getWidget( Object element ) {
    ControlElement controlElement = ( ControlElement )element;
    return controlElement.getNativeWidget();
  }

  private void adjustScrollbarStyle( ScrollbarStyle scrollbarStyle, CSSValue value  ) {
    Control control = (  Control )scrollbarStyle;
    if( control.getShell().getParent() == null ) {
      scrollbarStyle.setThumbColor( colors.get( value.getCssText() ).getThumbColor() );
      scrollbarStyle.setPageIncrementColor( colors.get( value.getCssText() ).getPageIncrementColor() );
      if( value.getCssText().equals( "dark" ) ) {
        scrollbarStyle.setBackgroundColor( new Color( Display.getCurrent(), new RGB( 38, 38, 38 ) ) );
      }
    } else {
      scrollbarStyle.setThumbColor( Display.getCurrent().getSystemColor( SWT.COLOR_WIDGET_NORMAL_SHADOW ) );
      scrollbarStyle.setPageIncrementColor( control.getBackground() );
    }
  }
}