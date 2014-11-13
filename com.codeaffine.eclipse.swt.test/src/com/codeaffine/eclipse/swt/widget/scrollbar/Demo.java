package com.codeaffine.eclipse.swt.widget.scrollbar;

import static com.codeaffine.eclipse.swt.layout.FormDatas.attach;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Slider;

import com.codeaffine.eclipse.swt.testhelper.LoremIpsum;

class Demo {

  void create( final Composite parent ) {
    parent.setLayout( new FormLayout() );

    final FlatScrollBar hScroll = new FlatScrollBar( parent, Orientation.HORIZONTAL );
    final FlatScrollBar vScroll = new FlatScrollBar( parent, Orientation.VERTICAL );

    Composite content = new Composite( parent, SWT.NONE );

    final Label label = new Label( content, SWT.NONE );
    label.setText( LoremIpsum.PARAGRAPHS );
    label.pack();

    final int slideWidth = Orientation.BAR_BREADTH;
    Control hScrollControl = hScroll.getControl();
    Control vScrollControl = vScroll.getControl();
    attach( hScrollControl ).toLeft().toBottom().toRight( slideWidth );
    attach( vScrollControl ).toRight().toTop().toBottom( slideWidth ).withWidth( slideWidth );
    attach( content ).toLeft().toTop().atBottomTo( hScrollControl ).atRightTo( vScrollControl );

    parent.addControlListener( new ControlAdapter() {
      @Override
      public void controlResized( ControlEvent evt ) {
        hScroll.setMaximum( label.getSize().x );
        hScroll.setThumb( parent.getClientArea().width - slideWidth );
        hScroll.setPageIncrement( parent.getClientArea().width - slideWidth );

        vScroll.setMaximum( label.getSize().y );
        vScroll.setThumb( parent.getClientArea().height - slideWidth );
        vScroll.setPageIncrement( parent.getClientArea().height - slideWidth );
      };
    } );

    hScroll.addScrollListener( new ScrollListener() {
      @Override
      public void selectionChanged( ScrollEvent event ) {
        Point location = label.getLocation();
        label.setLocation( -event.getSelection(), location.y );
      }
    } );

    vScroll.addScrollListener( new ScrollListener() {
      @Override
      public void selectionChanged( ScrollEvent event ) {
        Point location = label.getLocation();
        label.setLocation( location.x, -event.getSelection() );
      }
    } );
  }

  void createWithSlider( final Composite parent ) {
    parent.setLayout( new FormLayout() );

    final Slider hScroll = new Slider( parent, SWT.HORIZONTAL );
    final Slider vScroll = new Slider( parent, SWT.VERTICAL );

    Composite content = new Composite( parent, SWT.NONE );

    final Label label = new Label( content, SWT.NONE );
    label.setText( LoremIpsum.PARAGRAPHS );
    label.pack();

    final int slideWidth = 17;
    attach( hScroll ).toLeft().toBottom().toRight( slideWidth );
    attach( vScroll ).toRight().toTop().toBottom( slideWidth ).withWidth( slideWidth );
    attach( content ).toLeft().toTop().atBottomTo( hScroll ).atRightTo( vScroll );

    parent.addControlListener( new ControlAdapter() {
      @Override
      public void controlResized( ControlEvent evt ) {
        hScroll.setMaximum( label.getSize().x );
        hScroll.setThumb( parent.getClientArea().width - slideWidth );
        hScroll.setPageIncrement( parent.getClientArea().width - slideWidth );

        vScroll.setMaximum( label.getSize().y );
        vScroll.setThumb( parent.getClientArea().height - slideWidth );
        vScroll.setPageIncrement( parent.getClientArea().height - slideWidth );
      };
    } );

    hScroll.addSelectionListener( new SelectionAdapter() {
      @Override
      public void widgetSelected( SelectionEvent e ) {
        Point location = label.getLocation();
        label.setLocation( -hScroll.getSelection(), location.y );
      }
    } );

    vScroll.addSelectionListener( new SelectionAdapter() {
      @Override
      public void widgetSelected( SelectionEvent e ) {
        Point location = label.getLocation();
        label.setLocation( location.x, -vScroll.getSelection() );
      }
    } );
  }
}