package com.codeaffine.eclipse.swt.widget.scrollbar;

import java.util.Collection;
import java.util.HashSet;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Layout;

public class FlatScrollBar extends Composite {

  static final int DEFAULT_MINIMUM = 0;
  static final int DEFAULT_MAXIMUM = 100;
  static final int DEFAULT_INCREMENT = 1;
  static final int DEFAULT_THUMB = 10;
  static final int DEFAULT_PAGE_INCREMENT = DEFAULT_THUMB;
  static final int DEFAULT_SELECTION = 0;
  static final int DEFAULT_BUTTON_LENGTH = 0;
  static final int DEFAULT_MAX_EXPANSION = Direction.CLEARANCE + 4;

  final ClickControl up;
  final ClickControl upFast;
  final DragControl drag;
  final ClickControl downFast;
  final ClickControl down;
  final Direction direction;
  final MouseWheelShifter mouseWheelHandler;
  final Collection<SelectionListener> listeners;

  private int minimum;
  private int maximum;
  private int increment;
  private int pageIncrement;
  private int thumb;
  private int selection;
  private final int buttonLength;

  public FlatScrollBar( final Composite parent, int style ) {
    this( parent, style, DEFAULT_BUTTON_LENGTH, DEFAULT_MAX_EXPANSION );
  }

  FlatScrollBar( final Composite parent, int style, int buttonLength, int maxExpansion ) {
    super( parent, SWT.NONE );
    super.setLayout( new FlatScrollBarLayout( getDirection( style ) ) );
    Overlay overlay = new Overlay( this );
    this.minimum = DEFAULT_MINIMUM;
    this.maximum = DEFAULT_MAXIMUM;
    this.increment = DEFAULT_INCREMENT;
    this.pageIncrement = DEFAULT_PAGE_INCREMENT;
    this.thumb = DEFAULT_THUMB;
    this.selection = DEFAULT_SELECTION;
    this.buttonLength = buttonLength;
    this.direction = getDirection( style );
    this.direction.setDefaultSize( this );
    this.up = new ClickControl( overlay, new Decrementer( this ) );
    this.upFast = new ClickControl( overlay, new FastDecrementer( this ) );
    this.drag = new DragControl( overlay, new DragShifter( this, buttonLength ), maxExpansion );
    this.downFast = new ClickControl( overlay, new FastIncrementer( this ) );
    this.down = new ClickControl( overlay, new Incrementer( this ) );
    this.mouseWheelHandler = new MouseWheelShifter( this, parent, buttonLength );
    this.listeners = new HashSet<SelectionListener>();
    overlay.getControl().addMouseTrackListener( new MouseTracker( this, maxExpansion ) );
    overlay.getControl().addControlListener( new ResizeObserver( this ) );
  }

  @Override
  public void setLayout( Layout layout ) {
    throw new UnsupportedOperationException( FlatScrollBar.class.getName() + " does not allow to change layout." );
  };

  @Override
  public int getStyle() {
    return direction != null ? super.getStyle() | direction.value() : super.getStyle();
  };

  Direction getDirection() {
    return direction;
  }

  public void setMinimum( int minimum ) {
    if( minimum >= 0 && minimum < maximum ) {
      this.minimum = minimum;
      adjustThumb();
      adjustSelection();
      layout();
    }
  }

  public int getMinimum() {
    return minimum;
  }

  public void setMaximum( int maximum ) {
    if( maximum >= 0 && maximum > minimum ) {
      this.maximum = maximum;
      adjustThumb();
      adjustSelection();
      layout();
    }
  }

  public int getMaximum() {
    return maximum;
  }

  public void setThumb( int thumb ) {
    if( thumb >= 1 ) {
      this.thumb = thumb;
      adjustThumb();
      layout();
    }
  }

  public int getThumb() {
    return thumb;
  }

  public void setIncrement( int increment ) {
    this.increment = increment;
    layout();
  }

  public int getIncrement() {
    return increment;
  }

  public void setPageIncrement( int pageIncrement ) {
    this.pageIncrement = pageIncrement;
  }

  public int getPageIncrement() {
    return pageIncrement;
  }

  public void setSelection( int selection ) {
    this.selection = selection;
    adjustSelection();
    layout();
  }

  public int getSelection() {
    return selection;
  }

  public void addSelectionListener( SelectionListener selectionListener ) {
    listeners.add( selectionListener );
  }

  public void removeSelectionListener( SelectionListener selectionListener ) {
    listeners.remove( selectionListener );
  }

  @Override
  public void layout() {
    direction.layout( this, buttonLength );
    update();
  }

  protected void setSelectionInternal( int selection, int detail ) {
    int oldSelection = this.selection;
    setSelection( selection );
    if( oldSelection != this.selection ) {
      notifyListeners( detail );
    }
  }

  public void notifyListeners( int detail ) {
    SelectionEvent selectionEvent = createEvent( detail );
    for( SelectionListener listener : listeners ) {
      listener.widgetSelected( selectionEvent );
    }
  }

  private SelectionEvent createEvent( int detail ) {
    Event event = new Event();
    event.widget = this;
    event.detail = detail;
    return new SelectionEvent( event );
  }

  private void adjustThumb() {
    if( thumb > maximum - minimum ) {
      thumb = Math.min( maximum - minimum, thumb );
      thumb = Math.max( 1, thumb );
    }
  }

  private void adjustSelection() {
    selection = Math.min( selection, maximum - thumb );
    selection = Math.max( selection, minimum );
  }

  private static Direction getDirection( int style ) {
    return ( style & SWT.HORIZONTAL ) > 0 ? Direction.HORIZONTAL : Direction.VERTICAL;
  }
}