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

import static com.codeaffine.eclipse.swt.widget.scrollable.ScrollableAdapterFactory.createLayoutFactory;

import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.ScrollBar;

import com.codeaffine.eclipse.swt.util.ControlReflectionUtil;
import com.codeaffine.eclipse.swt.util.Platform;
import com.codeaffine.eclipse.swt.util.PlatformSupport;
import com.codeaffine.eclipse.swt.widget.scrollable.ScrollableAdapterFactory.Adapter;
import com.codeaffine.eclipse.swt.widget.scrollable.context.AdaptionContext;
import com.codeaffine.eclipse.swt.widget.scrollable.context.Reconciliation;
import com.codeaffine.eclipse.swt.widget.scrollable.context.ScrollableControl;

public class ScrolledCompositeAdapter extends ScrolledComposite implements Adapter<ScrolledComposite>, ScrollbarStyle {

  private LayoutFactory<ScrolledComposite> layoutFactory;
  private AdaptionContext<ScrolledComposite> context;
  private Reconciliation reconciliation;
  private ScrolledComposite scrolledComposite;

  ScrolledCompositeAdapter() {
    super( null, -1 );
  }

  @Override
  public ScrolledComposite getScrollable() {
    return scrolledComposite;
  }

  @Override
  @SuppressWarnings("unchecked")
  public void adapt( ScrolledComposite scrolledComposite, PlatformSupport platformSupport ) {
    this.layoutFactory = createLayoutFactory( new Platform(), createLayoutMapping( platformSupport ) );
    this.scrolledComposite = scrolledComposite;
    if( platformSupport.isGranted() ) {
      initialize();
    }
  }

  ///////////////////////////////
  // ScrolledComposite overrides

  @Override
  public void setLayout( Layout layout ) {
    throw new UnsupportedOperationException( "TreeAdapter does not allow to change its layout" );
  }

  @Override
  public ScrollBar getVerticalBar() {
    return layoutFactory.getVerticalBarAdapter();
  }

  @Override
  public ScrollBar getHorizontalBar() {
    return layoutFactory.getHorizontalBarAdapter();
  }

  @Override
  public void setSize( int width, int height ) {
    reconciliation.runWithSuspendedBoundsReconciliation( () -> super.setSize( width, height ) );
  }

  @Override
  public void setBounds( int x, int y, int width, int height ) {
    reconciliation.runWithSuspendedBoundsReconciliation( () ->  super.setBounds( x, y, width, height ) );
    if( mustWorkaroundPageCreationProblemOfPreferenceDialog( x, y, width, height ) ) {
      reconciliation.runWithSuspendedBoundsReconciliation( () ->  super.setBounds( x, y, width, height ) );
    }
  }

  @Override
  public void setLocation( int x, int y ) {
    reconciliation.runWithSuspendedBoundsReconciliation( () -> super.setLocation( x, y ) );
  }

  @Override
  public void setVisible( boolean visible ) {
    super.setVisible( reconciliation.setVisible( visible ) );
  }

  @Override
  public void setEnabled( boolean enabled ) {
    super.setEnabled( reconciliation.setEnabled( enabled ) );
  }

  @Override
  public Control getContent() {
    return scrolledComposite.getContent();
  }

  ////////////////////////////////////////////////////
  // scroll bar style attributes

  @Override
  public void setIncrementButtonLength( int length ) {
    layoutFactory.setIncrementButtonLength( length );
  }

  @Override
  public int getIncrementButtonLength() {
    return layoutFactory.getIncrementButtonLength();
  }

  @Override
  public void setIncrementColor( Color color ) {
    layoutFactory.setIncrementColor( color );
  }

  @Override
  public Color getIncrementColor() {
    return layoutFactory.getIncrementColor();
  }

  @Override
  public void setPageIncrementColor( Color color ) {
    layoutFactory.setPageIncrementColor( color );
  }

  @Override
  public Color getPageIncrementColor() {
    return layoutFactory.getPageIncrementColor();
  }

  @Override
  public void setThumbColor( Color color ) {
    layoutFactory.setThumbColor( color );
  }

  @Override
  public Color getThumbColor() {
    return layoutFactory.getThumbColor();
  }

  @Override
  public void setBackgroundColor( Color color ) {
    layoutFactory.setBackgroundColor( color );
  }

  @Override
  public Color getBackgroundColor() {
    return layoutFactory.getBackgroundColor();
  }

  @Override
  public void setDemeanor( Demeanor demeanor ) {
    layoutFactory.setDemeanor( demeanor );
  }

  @Override
  public Demeanor getDemeanor() {
    return layoutFactory.getDemeanor();
  }

  //////////////////////////////////////////////////////
  // delegating adapter methods

  @Override
  public Point computeSize( int wHint, int hHint, boolean changed ) {
    return scrolledComposite.computeSize( wHint, hHint, changed );
  }

  @Override
  public Object getLayoutData() {
    return scrolledComposite.getLayoutData();
  }

  @Override
  public void setLayoutData( Object layoutData ) {
    scrolledComposite.setLayoutData( layoutData );
  }

  @Override
  public Object getData() {
    return scrolledComposite.getData();
  }

  @Override
  public Object getData( String key ) {
    return scrolledComposite.getData( key );
  }

  @Override
  public void setBackgroundMode( int mode ) {
    scrolledComposite.setBackgroundMode( mode );
  }

  @Override
  public boolean setFocus() {
    return scrolledComposite.setFocus();
  }

  @Override
  public boolean forceFocus() {
    return scrolledComposite.forceFocus();
  }

  @Override
  public Color getBackground() {
    return scrolledComposite.getBackground();
  }

  @Override
  public void setData( Object data ) {
    scrolledComposite.setData( data );
  }

  @Override
  public Image getBackgroundImage() {
    return scrolledComposite.getBackgroundImage();
  }

  @Override
  public void setData( String key, Object value ) {
    scrolledComposite.setData( key, value );
  }

  @Override
  public void setTabList( Control[] tabList ) {
    scrolledComposite.setTabList( tabList );
  }

  @Override
  public boolean getEnabled() {
    return scrolledComposite.getEnabled();
  }

  @Override
  public Font getFont() {
    return scrolledComposite.getFont();
  }

  @Override
  public Color getForeground() {
    return scrolledComposite.getForeground();
  }

  @Override
  public String toString() {
    if( scrolledComposite != null ) {
      return scrolledComposite.toString();
    }
    return scrolledComposite.toString();
  }

  @Override
  public Control[] getTabList() {
    return scrolledComposite.getTabList();
  }

  @Override
  public boolean getVisible() {
    return scrolledComposite.getVisible();
  }

  @Override
  public boolean isFocusControl() {
    return scrolledComposite.isFocusControl();
  }

  @Override
  public void setBackground( Color color ) {
    scrolledComposite.setBackground( color );
  }

  @Override
  public void setBackgroundImage( Image image ) {
    scrolledComposite.setBackgroundImage( image );
  }

  @Override
  public void setForeground( Color color ) {
    scrolledComposite.setForeground( color );
  }

  @Override
  public boolean traverse( int traversal ) {
    return scrolledComposite.traverse( traversal );
  }

  @Override
  public boolean traverse( int traversal, Event event ) {
    return scrolledComposite.traverse( traversal, event );
  }

  @Override
  public boolean traverse( int traversal, KeyEvent event ) {
    return scrolledComposite.traverse( traversal, event );
  }

  @Override
  public void setFont( Font font ) {
    scrolledComposite.setFont( font );
  }

  @Override
  public void setMinHeight( int height ) {
    scrolledComposite.setMinHeight( height );
  }

  @Override
  public int getMinHeight() {
    return scrolledComposite.getMinHeight();
  }

  @Override
  public void setMinWidth( int width ) {
    scrolledComposite.setMinWidth( width );
  }

  @Override
  public int getMinWidth() {
    return scrolledComposite.getMinWidth();
  }

  @Override
  public void setMinSize( Point size ) {
    scrolledComposite.setMinSize( size );
  }

  @Override
  public void setExpandHorizontal( boolean expand ) {
    scrolledComposite.setExpandHorizontal( expand );
  }

  @Override
  public boolean getExpandHorizontal() {
    return scrolledComposite.getExpandHorizontal();
  }

  @Override
  public void setExpandVertical( boolean expand ) {
    scrolledComposite.setExpandVertical( expand );
  }

  @Override
  public boolean getExpandVertical() {
    return scrolledComposite.getExpandVertical();
  }

  ///////////////////////////////
  // private helper methods

  private void initialize() {
    scrolledComposite.setParent( this );
    ScrollableControl<ScrolledComposite> scrollableControl = new ScrollableControl<>( scrolledComposite );
    context = new AdaptionContext<>( this, scrollableControl );
    reconciliation = context.getReconciliation();
    new ControlReflectionUtil().setField( this, "layout", layoutFactory.create( context ) );
    new DisposalRouting().register( this, scrolledComposite );
  }

  private boolean mustWorkaroundPageCreationProblemOfPreferenceDialog( int x, int y, int width, int height ) {
    // Note [fappel]: This passage isn't covered with unit tests, since I wasn't able to reproduce the layout
    //                issue in a test scenario. However, since this workaround avoids the size setting problem when
    //                opening the workbench's preference dialog (adapter was rendered too small) I leave it at that
    //                for now.
    return !new Rectangle( x, y, width, height ).equals( getBounds() );
  }

  private static LayoutMapping<ScrolledComposite> createLayoutMapping( PlatformSupport platformSupport ) {
    return new LayoutMapping<ScrolledComposite>( new ScrolledCompositeLayoutFactory(),
                                                 platformSupport.getSupportedTypes() );
  }
}