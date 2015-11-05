package com.codeaffine.eclipse.swt.widget.scrollable.context;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Scrollable;

public class AdaptionContext<T extends Scrollable> {

  public static final int OVERLAY_OFFSET = 40;

  private final ScrollbarVisibility scrollbarVisibility;
  private final Point originOfScrollableOrdinates;
  private final ScrollableControl<T> scrollable;
  private final Reconciliation reconciliation;
  private final SizeComputer sizeComputer;
  private final Rectangle visibleArea;
  private final int verticalBarOffset;
  private final Composite adapter;
  private final int borderWidth;
  private final int itemHeight;
  private final int offset;

  public AdaptionContext( Composite adapter, ScrollableControl<T> scrollable ) {
    this( adapter, scrollable, 1, null );
  }

  private AdaptionContext(
    Composite adapter, ScrollableControl<T> scrollable, int itemHeight, Reconciliation reconciliation )
  {
    this.sizeComputer = new SizeComputer( scrollable, adapter );
    this.scrollbarVisibility = new ScrollbarVisibility( sizeComputer, scrollable, adapter.getClientArea(), itemHeight );
    this.reconciliation = reconciliation == null ? new Reconciliation( adapter, scrollable ) : reconciliation;
    this.verticalBarOffset = computeVerticalBarOffset( scrollable );
    this.offset = new OffsetComputer( scrollable ).compute();
    this.visibleArea = computeVisibleArea( adapter, scrollable );
    this.borderWidth = getBorderWidth( scrollable );
    this.originOfScrollableOrdinates = new Point( visibleArea.x - borderWidth, visibleArea.y - borderWidth );
    this.scrollable = scrollable;
    this.adapter = adapter;
    this.itemHeight = itemHeight;
  }

  public AdaptionContext<T> newContext( int itemHeight ) {
    return new AdaptionContext<T>( adapter, scrollable, itemHeight, reconciliation );
  }

  public AdaptionContext<T> newContext() {
    return new AdaptionContext<T>( adapter, scrollable, itemHeight, reconciliation );
  }

  public Reconciliation getReconciliation() {
    return reconciliation;
  }

  public Composite getAdapter() {
    return adapter;
  }

  public ScrollableControl<T> getScrollable() {
    return scrollable;
  }

  public int getOffset() {
    return offset;
  }

  public Point getOriginOfScrollableOrdinates() {
    return originOfScrollableOrdinates;
  }

  public Point getPreferredSize() {
    return sizeComputer.getPreferredSize();
  }

  public void updatePreferredSize() {
    sizeComputer.updatePreferredSize();
  }

  public void adjustPreferredWidthIfHorizontalBarIsVisible() {
    sizeComputer.adjustPreferredWidthIfHorizontalBarIsVisible();
  }

  public boolean isVerticalBarVisible() {
    return scrollbarVisibility.isVerticalBarVisible();
  }

  public boolean isHorizontalBarVisible() {
    return scrollbarVisibility.isHorizontalBarVisible();
  }

  public Rectangle getVisibleArea() {
    return visibleArea;
  }

  public int getHorizontalAdapterSelection() {
    ScrollBar horizontalBar = getAdapter().getHorizontalBar();
    if( horizontalBar != null ) {
      return horizontalBar.getSelection();
    }
    return 0;
  }

  public int getVerticalBarOffset() {
    return verticalBarOffset;
  }

  public boolean isScrollableReplacedByAdapter() {
    return scrollable.isChildOf( adapter.getParent() );
  }

  public int getBorderWidth() {
    return borderWidth;
  }

  private static int getBorderWidth( ScrollableControl<? extends Scrollable> scrollable ) {
    if( scrollable.hasStyle( SWT.BORDER ) ) {
      return scrollable.getBorderWidth();
    }
    return 0;
  }

  private static int computeVerticalBarOffset( ScrollableControl<? extends Scrollable> scrollable ) {
    int result = scrollable.getVerticalBarSize().x;
    if( result == 0 ) {
      result = OVERLAY_OFFSET;
    }
    return result;
  }

  private static Rectangle computeVisibleArea( Composite adapter, ScrollableControl<? extends Scrollable> scrollable ) {
    Rectangle area = adapter.getClientArea();
    int borderAdjustment = getBorderWidth( scrollable ) * 2;
    return new Rectangle( area.x, area.y, area.width + borderAdjustment, area.height + borderAdjustment );
  }
}