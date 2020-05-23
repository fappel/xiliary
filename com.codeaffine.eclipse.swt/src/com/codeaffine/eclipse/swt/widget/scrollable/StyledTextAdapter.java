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
import static java.util.Objects.nonNull;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
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

public class StyledTextAdapter extends StyledText implements Adapter<StyledText>, ScrollbarStyle {

  static String UNSUPPORTED_CREATION = "Direct Instantiation of StyledTextAdapter is not allowed.";

  private LayoutFactory<StyledText> layoutFactory;
  private AdaptionContext<StyledText> context;
  private Reconciliation reconciliation;
  private StyledText styledText;

  StyledTextAdapter() {
    super( throwUnsupportedCreation(), -1 );
  }

  @Override
  public StyledText getScrollable() {
    return styledText;
  }

  @Override
  @SuppressWarnings("unchecked")
  public void adapt( StyledText styledText, PlatformSupport platformSupport ) {
    this.layoutFactory = createLayoutFactory( new Platform(), createLayoutMapping( platformSupport ) );
    this.styledText = styledText;
    if( platformSupport.isGranted() ) {
      initialize();
    }
  }

  ///////////////////////////////
  // StyleText overrides

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
   return styledText.computeSize( wHint, hHint, changed );
  }

  @Override
  public Object getLayoutData() {
    return styledText.getLayoutData();
  }

  @Override
  public void setLayoutData( Object layoutData ) {
    styledText.setLayoutData( layoutData );
  }

  @Override
  public Object getData() {
    return styledText.getData();
  }

  @Override
  public Object getData( String key ) {
    return styledText.getData( key );
  }

  @Override
  public void setBackgroundMode( int mode ) {
    styledText.setBackgroundMode( mode );
  }

  @Override
  public boolean setFocus() {
    return styledText.setFocus();
  }

  @Override
  public boolean forceFocus() {
    return styledText.forceFocus();
  }

  @Override
  public Color getBackground() {
    return styledText.getBackground();
  }

  @Override
  public void setData( Object data ) {
    styledText.setData( data );
  }

  @Override
  public Image getBackgroundImage() {
    return styledText.getBackgroundImage();
  }

  @Override
  public void setData( String key, Object value ) {
    styledText.setData( key, value );
  }

  @Override
  public void setTabList( Control[] tabList ) {
    styledText.setTabList( tabList );
  }

  @Override
  public boolean getEnabled() {
    return styledText.getEnabled();
  }

  @Override
  public Font getFont() {
    return styledText.getFont();
  }

  @Override
  public Color getForeground() {
    return styledText.getForeground();
  }

  @Override
  public String toString() {
    if( styledText != null ) {
      return styledText.toString();
    }
    return styledText.toString();
  }

  @Override
  public Control[] getTabList() {
    return styledText.getTabList();
  }

  @Override
  public boolean getVisible() {
    return styledText.getVisible();
  }

  @Override
  public boolean isFocusControl() {
    return styledText.isFocusControl();
  }

  @Override
  public void setBackground( Color color ) {
    styledText.setBackground( color );
  }

  @Override
  public void setBackgroundImage( Image image ) {
    styledText.setBackgroundImage( image );
  }

  @Override
  public void setForeground( Color color ) {
    styledText.setForeground( color );
  }

  @Override
  public boolean traverse( int traversal ) {
    return styledText.traverse( traversal );
  }

  @Override
  public boolean traverse( int traversal, Event event ) {
    return styledText.traverse( traversal, event );
  }

  @Override
  public boolean traverse( int traversal, KeyEvent event ) {
    return styledText.traverse( traversal, event );
  }

  @Override
  public void setFont( Font font ) {
    styledText.setFont( font );
  }

  ///////////////////////////////
  // private helper methods

  private void initialize() {
    styledText.setParent( this );
    ensureNativeScrollbarIsAlwaysHidden();
    context = new AdaptionContext<>( this, new ScrollableControl<>( styledText ) );
    reconciliation = context.getReconciliation();
    super.setLayout( layoutFactory.create( context ) );
    new DisposalRouting().register( this, styledText );
    new ControlReflectionUtil().invoke( this, "initializeAccessible" );
    avoidMouseWheelEventPropagationToFlatScrollBars();
    layout();
  }

  /*
   * Fixes https://github.com/fappel/xiliary/issues/87
   * Hide native horizontal bar to avoid rendering issues with neighbouring widgets (e.g. ruler in eclipse text
   * editors). External code trying to unhide it would trigger a PaintEvent, hence the listener to ensure it's always
   * hidden.
   */
  private void ensureNativeScrollbarIsAlwaysHidden() {
    styledText.addPaintListener( evt -> hideNativeHorizontalBar() );
  }

  private void hideNativeHorizontalBar() {
    if( nonNull( styledText.getHorizontalBar() ) ) {
      styledText.getHorizontalBar().setVisible( false );
    }
  }

  // Workaround for https://github.com/fappel/xiliary/issues/63
  private void avoidMouseWheelEventPropagationToFlatScrollBars() {
    getDisplay().addFilter( SWT.MouseWheel, this::cancelMouseWheelEventPropagationToFlatScrollBars );
  }

  private void cancelMouseWheelEventPropagationToFlatScrollBars( Event evt ) {
    if( evt.widget == StyledTextAdapter.this ) {
      evt.type = SWT.None;
    }
  }

  private static LayoutMapping<StyledText> createLayoutMapping( PlatformSupport platformSupport ) {
    return new LayoutMapping<StyledText>( new StyledTextLayoutFactory(), platformSupport.getSupportedTypes() );
  }

  private static Composite throwUnsupportedCreation() {
    throw new UnsupportedOperationException( UNSUPPORTED_CREATION );
  }
}