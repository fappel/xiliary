package com.codeaffine.eclipse.us.swt.theme;

import org.eclipse.e4.ui.css.core.dom.properties.ICSSPropertyHandler;
import org.eclipse.e4.ui.css.core.engine.CSSEngine;
import org.eclipse.e4.ui.css.swt.dom.ControlElement;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Tree;
import org.w3c.dom.css.CSSValue;

import com.codeaffine.eclipse.swt.widget.scrollable.ScrollableAdapterFactory;
import com.codeaffine.eclipse.swt.widget.scrollable.TableAdapter;
import com.codeaffine.eclipse.swt.widget.scrollable.TreeAdapter;


@SuppressWarnings("restriction")
public class ScrollableAdapter implements ICSSPropertyHandler {

  private final ScrollableAdapterFactory factory;
  private Color thumbColor;
  private Color pageIncrementColor;

  public ScrollableAdapter() {
    factory = new ScrollableAdapterFactory();
  }

  @Override
  public boolean applyCSSProperty( Object element, String property, CSSValue value, String pseudo, CSSEngine engine )
    throws Exception
  {
    if( thumbColor == null ) {
      thumbColor = new Color( Display.getCurrent(), new RGB( 210, 225, 240 ) );
      pageIncrementColor = new Color( Display.getCurrent(), new RGB( 248, 248, 248 ) );
    }
    if( ( element instanceof ControlElement ) ) {
      ControlElement controlElement = ( ControlElement )element;
      Object widget = controlElement.getNativeWidget();

      if( (    widget instanceof Tree
            && !( widget instanceof TreeAdapter )
            && !( factory.isAdapted( ( Tree )widget ) ) ) )
      {
        TreeAdapter adapter = factory.create( ( Tree )widget, TreeAdapter.class );
        adapter.setThumbColor( thumbColor );
        adapter.setPageIncrementColor( pageIncrementColor );
      }

      if(    ( widget instanceof Table
          && !( widget instanceof TableAdapter )
          && !!( factory.isAdapted( ( Table )widget ) ) ) )
      {
        TableAdapter adapter = factory.create( ( Table )widget, TableAdapter.class );
        adapter.setThumbColor( thumbColor );
        adapter.setPageIncrementColor( pageIncrementColor );
      }
    }
    return false;
  }

  @Override
  public String retrieveCSSProperty( Object element, String property, String pseudo, CSSEngine engine )
    throws Exception
  {
    return null;
  }
}
