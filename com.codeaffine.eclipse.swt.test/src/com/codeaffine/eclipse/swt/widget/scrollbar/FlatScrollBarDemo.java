package com.codeaffine.eclipse.swt.widget.scrollbar;

import static com.codeaffine.eclipse.swt.layout.FormDatas.attach;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Slider;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.eclipse.swt.test.util.DisplayHelper;
import com.codeaffine.eclipse.swt.testhelper.LoremIpsum;
import com.codeaffine.eclipse.swt.util.ReadAndDispatch;

public class FlatScrollBarDemo {

  private static final int MARGIN = Direction.BAR_BREADTH - Direction.CLEARANCE + 1;

  @Rule public final DisplayHelper displayHelper = new DisplayHelper();

  private Shell shell;

  @Before
  public void setUp() {
    shell = createShell();
    shell.open();
  }

  @Test
  public void demo() {
    Shell localShell = createShell();
    localShell.setBounds( 500, 200, 200, 300 );
    Composite localParent = new Composite( localShell, SWT.NONE );
    new FlatScrollBarDemo().create( localParent );
    localShell.open();

    Composite parent = new Composite( shell, SWT.NONE );
    new FlatScrollBarDemo().createWithSlider( parent );
    shell.setBounds( 250, 200, 200, 300 );
    new ReadAndDispatch().spinLoop( shell );
  }

  void create( final Composite parent ) {
    parent.setLayout( new FormLayout() );

    Composite content = new Composite( parent, SWT.NONE );
    final FlatScrollBar hScroll = new FlatScrollBar( parent, SWT.HORIZONTAL );
    final FlatScrollBar vScroll = new FlatScrollBar( parent, SWT.VERTICAL );

    final Label label = new Label( content, SWT.NONE );
    label.setText( LoremIpsum.PARAGRAPHS );
    label.pack();

    final int slideWidth = Direction.BAR_BREADTH;
    attach( hScroll ).toLeft().toBottom().toRight( slideWidth + MARGIN ).withHeight( slideWidth );
    attach( vScroll ).toRight().toTop().toBottom( slideWidth + MARGIN ).withWidth( slideWidth );
    attach( content ).toLeft().toTop().toBottom( MARGIN ).toRight( MARGIN );

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
      public void widgetSelected( SelectionEvent event ) {
        Point location = label.getLocation();
        label.setLocation( -hScroll.getSelection(), location.y );
      }
    } );

    vScroll.addSelectionListener( new SelectionAdapter() {
      @Override
      public void widgetSelected( SelectionEvent event ) {
        Point location = label.getLocation();
        label.setLocation( location.x, -vScroll.getSelection() );
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
    attach( content ).toLeft().toTop().toBottom( MARGIN ).toRight( MARGIN );

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

  private Shell createShell() {
    Shell result = displayHelper.createShell( SWT.SHELL_TRIM );
    result.setBackgroundMode( SWT.INHERIT_DEFAULT );
    result.setBackground( Display.getCurrent().getSystemColor( SWT.COLOR_LIST_BACKGROUND ) );
    result.setLayout( createShellLayout() );
    return result;
  }

  private static FillLayout createShellLayout() {
    FillLayout result = new FillLayout( SWT.HORIZONTAL );
    result.marginHeight = 10;
    result.marginWidth = 10;
    return result;
  }
}