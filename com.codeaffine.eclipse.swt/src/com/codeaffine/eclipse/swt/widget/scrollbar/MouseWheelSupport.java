package com.codeaffine.eclipse.swt.widget.scrollbar;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Slider;

// Workaround for https://github.com/rherrmann/gitplus/issues/594
public class MouseWheelSupport {

  private final FlatScrollBar scrollBar;

  private Slider slider;
  private ScrollBarAdapter scrollBarAdapter;

  static class SliderAdapter extends SelectionAdapter {

    private final MouseWheelSupport mouseWheelSupport;

    SliderAdapter( MouseWheelSupport mouseWheelSupport ) {
      this.mouseWheelSupport = mouseWheelSupport;
    }

    @Override
    public void widgetSelected( SelectionEvent event ) {
      mouseWheelSupport.updateScrollBarSelection();
      mouseWheelSupport.copySettings();
    }
  }

  static class ScrollBarAdapter implements ControlListener, ScrollListener, DisposeListener {

    private final MouseWheelSupport mouseWheelSupport;

    ScrollBarAdapter( MouseWheelSupport mouseWheelSupport ) {
      this.mouseWheelSupport = mouseWheelSupport;
    }

    @Override
    public void controlResized( ControlEvent event ) {
      mouseWheelSupport.copySettings();
    }

    @Override
    public void controlMoved( ControlEvent event ) {
      mouseWheelSupport.copySettings();
    }

    @Override
    public void selectionChanged( ScrollEvent event ) {
      mouseWheelSupport.copySettings();
    }
    @Override
    public void widgetDisposed( DisposeEvent event ) {
      mouseWheelSupport.dispose();
    }
  }

  public MouseWheelSupport( FlatScrollBar scrollBar ) {
    this.scrollBar = scrollBar;
  }

  public Control getControl() {
    return slider;
  }

  public void dispose() {
    if( !scrollBar.getControl().isDisposed() ) {
      scrollBar.getControl().removeControlListener( scrollBarAdapter );
    }
    scrollBar.removeScrollListener( scrollBarAdapter );
    slider.dispose();
  }

  public void create() {
    if( scrollBar.getOrientation() == Orientation.HORIZONTAL ) {
      slider = new Slider( scrollBar.getControl().getParent(), SWT.HORIZONTAL );
    } else {
      slider = new Slider( scrollBar.getControl().getParent(), SWT.VERTICAL );
    }
    scrollBarAdapter = new ScrollBarAdapter( this );
    scrollBar.getControl().addControlListener( scrollBarAdapter );
    scrollBar.getControl().addDisposeListener( scrollBarAdapter );
    scrollBar.addScrollListener( scrollBarAdapter );
    slider.addSelectionListener( new SliderAdapter( this ) );
    copySettings();
  }

  protected void copySettings() {
    if( slider.getLayoutData() == null ) {
      slider.setBounds( scrollBar.getControl().getBounds() );
    }
    slider.moveBelow( null );
    slider.setMinimum( scrollBar.getMinimum() );
    slider.setMaximum( scrollBar.getMaximum() );
    slider.setThumb( scrollBar.getThumb() );
    slider.setIncrement( scrollBar.getIncrement() );
    slider.setPageIncrement( scrollBar.getPageIncrement() );
    slider.setSelection( scrollBar.getSelection() );
  }

  protected void updateScrollBarSelection() {
    scrollBar.setSelectionInternal( slider.getSelection() );
  }
}