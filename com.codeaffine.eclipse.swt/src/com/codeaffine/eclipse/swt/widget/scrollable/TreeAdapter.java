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

import static com.codeaffine.eclipse.swt.widget.scrollable.ScrollableAdapterFactory.createLayoutFactory;

import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;

import com.codeaffine.eclipse.swt.util.Platform;
import com.codeaffine.eclipse.swt.util.PlatformSupport;
import com.codeaffine.eclipse.swt.widget.scrollable.ScrollableAdapterFactory.Adapter;
import com.codeaffine.eclipse.swt.widget.scrollable.context.AdaptionContext;
import com.codeaffine.eclipse.swt.widget.scrollable.context.Reconciliation;
import com.codeaffine.eclipse.swt.widget.scrollable.context.ScrollableControl;

public class TreeAdapter extends Tree implements Adapter<Tree>, ScrollbarStyle {

  private LayoutFactory<Tree> layoutFactory;
  private AdaptionContext<Tree> context;
  private Reconciliation reconciliation;
  private Tree tree;

  TreeAdapter() {
    super( null, -1 );
  }

  @Override
  public Tree getScrollable() {
    return tree;
  }

  @Override
  @SuppressWarnings("unchecked")
  public void adapt( Tree tree, PlatformSupport platformSupport ) {
    this.layoutFactory = createLayoutFactory( new Platform(), createLayoutMapping( platformSupport ) );
    this.tree = tree;
    if( platformSupport.isGranted() ) {
      initialize();
    }
  }

  ///////////////////////////////
  // Tree overrides

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
    reconciliation.runWithSuspendedBoundsReconciliation( () -> super.setBounds( x, y, width, height ) );
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
  public TreeColumn getColumn( int index ) {
    return tree.getColumn( index );
  }

  @Override
  public int[] getColumnOrder() {
    return tree.getColumnOrder();
  }

  @Override
  public int getColumnCount() {
    return tree.getColumnCount();
  }

  @Override
  public TreeColumn[] getColumns() {
    return tree == null ? new TreeColumn[ 0 ] : tree.getColumns();
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
    return tree.computeSize( wHint, hHint, changed );
  }

  @Override
  public Object getLayoutData() {
    return tree.getLayoutData();
  }

  @Override
  public void setLayoutData( Object layoutData ) {
    tree.setLayoutData( layoutData );
  }

  @Override
  public Object getData() {
    return tree.getData();
  }

  @Override
  public Object getData( String key ) {
    return tree.getData( key );
  }

  @Override
  public void setBackgroundMode( int mode ) {
    super.setBackgroundMode( mode );
  }

  @Override
  public boolean setFocus() {
    return tree.setFocus();
  }

  @Override
  public void setTabList( Control[] tabList ) {
    tree.setTabList( tabList );
  }

  @Override
  public boolean forceFocus() {
    return tree.forceFocus();
  }

  @Override
  public Color getBackground() {
    return tree.getBackground();
  }

  @Override
  public void setData( Object data ) {
    tree.setData( data );
  }

  @Override
  public Image getBackgroundImage() {
    return tree.getBackgroundImage();
  }

  @Override
  public void setData( String key, Object value ) {
    tree.setData( key, value );
  }

  @Override
  public boolean getEnabled() {
    return tree.getEnabled();
  }

  @Override
  public Font getFont() {
    return tree.getFont();
  }

  @Override
  public Color getForeground() {
    return tree.getForeground();
  }

  @Override
  public boolean getLinesVisible() {
    return super.getLinesVisible();
  }

  @Override
  public String toString() {
    if( tree != null ) {
      return tree.toString();
    }
    return super.toString();
  }

  @Override
  public Control[] getTabList() {
    return tree.getTabList();
  }

  @Override
  public boolean getVisible() {
    return tree.getVisible();
  }

  @Override
  public boolean isFocusControl() {
    return tree.isFocusControl();
  }

  @Override
  public void setBackground( Color color ) {
    super.setBackground( color );
    tree.setBackground( color );
  }

  @Override
  public void setBackgroundImage( Image image ) {
    tree.setBackgroundImage( image );
  }

  @Override
  public boolean getHeaderVisible() {
    return tree.getHeaderVisible();
  }

  @Override
  public void setForeground( Color color ) {
    tree.setForeground( color );
  }

  @Override
  public boolean traverse( int traversal ) {
    return tree.traverse( traversal );
  }

  @Override
  public boolean traverse( int traversal, Event event ) {
    return tree.traverse( traversal, event );
  }

  @Override
  public boolean traverse( int traversal, KeyEvent event ) {
    return tree.traverse( traversal, event );
  }

  @Override
  public void setLinesVisible( boolean show ) {
    tree.setLinesVisible( show );
  }

  @Override
  public void setFont( Font font ) {
    tree.setFont( font );
  }

  @Override
  public void setHeaderVisible( boolean show ) {
    tree.setHeaderVisible( show );
  }

  ///////////////////////////////
  // private helper methods

  private void initialize() {
    tree.setParent( this );
    ScrollableControl<Tree> scrollableControl = new ScrollableControl<>( tree );
    new ItemHeightMeasurementEnabler( scrollableControl, this );
    context = new AdaptionContext<Tree>( this, new ScrollableControl<>( tree ) );
    reconciliation = context.getReconciliation();
    super.setLayout( layoutFactory.create( context ) );
    new DisposalRouting().register( this, tree );
    new TreePageResizeFilter().register( this, tree );
  }

  private static LayoutMapping<Tree> createLayoutMapping( PlatformSupport platformSupport ) {
    return new LayoutMapping<Tree>( new TreeLayoutFactory(), platformSupport.getSupportedTypes() );
  }
}