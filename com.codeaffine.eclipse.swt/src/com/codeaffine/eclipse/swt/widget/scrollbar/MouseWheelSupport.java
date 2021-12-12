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
      mouseWheelSupport.updateScrollBarSelection( event.detail );
      mouseWheelSupport.copySettings();
    }
  }

  static class ScrollBarAdapter extends SelectionAdapter implements ControlListener, DisposeListener {

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
    public void widgetSelected( SelectionEvent event ) {
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
    if( !scrollBar.isDisposed() ) {
      scrollBar.removeControlListener( scrollBarAdapter );
    }
    scrollBar.removeSelectionListener( scrollBarAdapter );
    slider.dispose();
  }

  public void create() {
    if( scrollBar.getDirection() == Direction.HORIZONTAL ) {
      slider = new Slider( scrollBar.getParent(), SWT.HORIZONTAL );
    } else {
      slider = new Slider( scrollBar.getParent(), SWT.VERTICAL );
    }
    scrollBarAdapter = new ScrollBarAdapter( this );
    scrollBar.addControlListener( scrollBarAdapter );
    scrollBar.addDisposeListener( scrollBarAdapter );
    scrollBar.addSelectionListener( scrollBarAdapter );
    slider.addSelectionListener( new SliderAdapter( this ) );
    copySettings();
  }

  protected void copySettings() {
    if( slider.getLayoutData() == null ) {
      slider.setBounds( scrollBar.getBounds() );
    }
    slider.moveBelow( null );
    slider.setMinimum( scrollBar.getMinimum() );
    slider.setMaximum( scrollBar.getMaximum() );
    slider.setThumb( scrollBar.getThumb() );
    slider.setIncrement( scrollBar.getIncrement() );
    slider.setPageIncrement( scrollBar.getPageIncrement() );
    slider.setSelection( scrollBar.getSelection() );
  }

  protected void updateScrollBarSelection( int detail ) {
    scrollBar.setSelectionInternal( slider.getSelection(), detail );
  }
}