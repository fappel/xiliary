package com.codeaffine.eclipse.swt.widget.scrollbar;

import java.util.Collection;
import java.util.HashSet;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Layout;

public class FlatScrollBar extends Composite {

  static final int DEFAULT_MINIMUM = 0;
  static final int DEFAULT_MAXIMUM = 100;
  static final int DEFAULT_INCREMENT = 1;
  static final int DEFAULT_THUMB = 10;
  static final int DEFAULT_PAGE_INCREMENT = DEFAULT_THUMB;
  static final int DEFAULT_SELECTION = 0;

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

  public FlatScrollBar( Composite parent, int style ) {
    super( parent, SWT.NONE );
    this.minimum = DEFAULT_MINIMUM;
    this.maximum = DEFAULT_MAXIMUM;
    this.increment = DEFAULT_INCREMENT;
    this.pageIncrement = DEFAULT_PAGE_INCREMENT;
    this.thumb = DEFAULT_THUMB;
    this.selection = DEFAULT_SELECTION;
    this.direction = ( style & SWT.HORIZONTAL ) > 0 ? Direction.HORIZONTAL : Direction.VERTICAL;
    super.setLayout( new FlatScrollBarLayout( direction ) );
    this.direction.setDefaultSize( this );
    this.up = new ClickControl( this, getSlowBackground(), new Decrementer( this ) );
    this.upFast = new ClickControl( this, getFastBackground(), new FastDecrementer( this ) );
    this.drag = new DragControl( this, getDragBackground(), new DragShifter( this ) );
    this.downFast = new ClickControl( this, getFastBackground(), new FastIncrementer( this ) );
    this.down = new ClickControl( this, getSlowBackground(), new Incrementer( this ) );
    addMouseTrackListener( new MouseTracker( this ) );
    addControlListener( new ResizeObserver( this ) );
    this.mouseWheelHandler = new MouseWheelShifter( this, parent );
    this.listeners = new HashSet<SelectionListener>();
  }

  @Override
  public void setLayout( Layout layout ) {
    throw new UnsupportedOperationException( FlatScrollBar.class.getName() + " does not allow to change layout." );
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
    direction.layout( this );
    update();
  }

  protected void setSelectionInternal( int selection ) {
    int oldSelection = this.selection;
    setSelection( selection );
    if( oldSelection != this.selection ) {
      notifyListeners();
    }
  }

  public void notifyListeners() {
    SelectionEvent selectionEvent = createEvent();
    for( SelectionListener listener : listeners ) {
      listener.widgetSelected( selectionEvent );
    }
  }

  private SelectionEvent createEvent() {
    Event event = new Event();
    event.widget = this;
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

  static Color getDragBackground() {
    return Display.getCurrent().getSystemColor( SWT.COLOR_WIDGET_NORMAL_SHADOW );
  }

  static Color getSlowBackground() {
    return Display.getCurrent().getSystemColor( SWT.COLOR_WIDGET_LIGHT_SHADOW );
  }

  static Color getFastBackground() {
    return new Color( Display.getCurrent(), 245, 245, 245 );
  }
}