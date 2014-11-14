package com.codeaffine.eclipse.swt.widget.scrollbar;

import static com.codeaffine.eclipse.swt.widget.scrollbar.FlatScrollBar.getDragBackground;
import static com.codeaffine.eclipse.swt.widget.scrollbar.FlatScrollBar.getFastBackground;
import static com.codeaffine.eclipse.swt.widget.scrollbar.FlatScrollBar.getSlowBackground;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Shell;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import com.codeaffine.eclipse.swt.test.util.DisplayHelper;
import com.codeaffine.eclipse.swt.test.util.SWTIgnoreConditions.GtkPlatform;
import com.codeaffine.eclipse.swt.util.ReadAndDispatch;
import com.codeaffine.test.util.junit.ConditionalIgnoreRule;
import com.codeaffine.test.util.junit.ConditionalIgnoreRule.ConditionalIgnore;

public class FlatScrollBarTest {

  @Rule public final ConditionalIgnoreRule ignoreRule = new ConditionalIgnoreRule();
  @Rule public final DisplayHelper displayHelper = new DisplayHelper();

  private Shell shell;

  @Before
  public void setUp() {
    shell = displayHelper.createShell( SWT.SHELL_TRIM );
    shell.setBackgroundMode( SWT.INHERIT_DEFAULT );
    shell.setLayout( new FillLayout( SWT.HORIZONTAL ) );
    shell.setBounds( 250, 200, 500, 400 );
    shell.setBackground( Display.getCurrent().getSystemColor( SWT.COLOR_LIST_BACKGROUND ) );
    shell.open();
  }

 @Test
 @Ignore
 public void demo() {
   Shell localShell = displayHelper.createShell( SWT.RESIZE );
   localShell.setBackgroundMode( SWT.INHERIT_DEFAULT );
   localShell.setLayout( new FillLayout( SWT.HORIZONTAL ) );
   localShell.setBackground( Display.getCurrent().getSystemColor( SWT.COLOR_LIST_BACKGROUND ) );
   new Demo().create( localShell );
   localShell.open();
   localShell.setBounds( 500, 200, 200, 300 );

   new Demo().createWithSlider( shell );
   shell.setBounds( 250, 200, 200, 300 );
   new ReadAndDispatch().spinLoop( shell );
 }

  @Test
  public void getControl() {
    FlatScrollBar scrollBar = new FlatScrollBar( shell, Orientation.HORIZONTAL );

    Control actual = scrollBar.getControl();

    assertThat( actual ).isSameAs( shell.getChildren()[ 0 ] );
  }

  @Test
  public void getOrientation() {
    FlatScrollBar scrollBar = new FlatScrollBar( shell, Orientation.HORIZONTAL );

    Orientation actual = scrollBar.getOrientation();

    assertThat( actual ).isSameAs( Orientation.HORIZONTAL );
  }

  @Test
  public void getMinimum() {
    FlatScrollBar scrollBar = new FlatScrollBar( shell, Orientation.HORIZONTAL );

    int actual = scrollBar.getMinimum();

    assertThat( actual ).isEqualTo( FlatScrollBar.DEFAULT_MINIMUM );
  }

  @Test
  public void setMinimum() {
    int expected = FlatScrollBar.DEFAULT_MAXIMUM - 1;
    FlatScrollBar scrollBar = new FlatScrollBar( shell, Orientation.HORIZONTAL );
    scrollBar.setMinimum( FlatScrollBar.DEFAULT_MAXIMUM - 1 );

    int actual = scrollBar.getMinimum();

    assertThat( actual ).isEqualTo( expected );
  }

  @Test
  public void setMinimumWithTooLargeValue() {
    FlatScrollBar scrollBar = new FlatScrollBar( shell, Orientation.HORIZONTAL );
    scrollBar.setMinimum( FlatScrollBar.DEFAULT_MAXIMUM );

    int actual = scrollBar.getMinimum();

    assertThat( actual ).isEqualTo( FlatScrollBar.DEFAULT_MINIMUM );
  }

  @Test
  public void setMinimumWithTooSmallValue() {
    FlatScrollBar scrollBar = new FlatScrollBar( shell, Orientation.HORIZONTAL );
    scrollBar.setMinimum( -1 );

    int actual = scrollBar.getMinimum();

    assertThat( actual ).isEqualTo( FlatScrollBar.DEFAULT_MINIMUM );
  }

  @Test
  public void setMinimumTriggersLayout() {
    FlatScrollBar scrollBar = new FlatScrollBar( shell, Orientation.HORIZONTAL );
    shell.layout();

    Rectangle before = scrollBar.drag.getControl().getBounds();
    scrollBar.setMinimum( 20 );
    Rectangle after = scrollBar.drag.getControl().getBounds();

    assertThat( after ).isNotEqualTo( before );
  }

  @Test
  public void getMaximum() {
    FlatScrollBar scrollBar = new FlatScrollBar( shell, Orientation.HORIZONTAL );

    int actual = scrollBar.getMaximum();

    assertThat( actual ).isEqualTo( FlatScrollBar.DEFAULT_MAXIMUM );
  }

  @Test
  public void setMaximum() {
    int expected = 110;
    FlatScrollBar scrollBar = new FlatScrollBar( shell, Orientation.HORIZONTAL );
    scrollBar.setMaximum( expected );

    int actual = scrollBar.getMaximum();

    assertThat( actual ).isEqualTo( expected );
  }

  @Test
  public void setMaximumWithValueEqualsMinimum() {
    FlatScrollBar scrollBar = new FlatScrollBar( shell, Orientation.HORIZONTAL );
    scrollBar.setMinimum( 10 );
    scrollBar.setMaximum( 10 );

    int actual = scrollBar.getMaximum();

    assertThat( actual ).isEqualTo( FlatScrollBar.DEFAULT_MAXIMUM );
  }

  @Test
  public void setMaximumWithNegativeValue() {
    FlatScrollBar scrollBar = new FlatScrollBar( shell, Orientation.HORIZONTAL );
    scrollBar.setMaximum( -1 );

    int actual = scrollBar.getMaximum();

    assertThat( actual ).isEqualTo( FlatScrollBar.DEFAULT_MAXIMUM );
  }

  @Test
  public void setMaximumTriggersLayout() {
    FlatScrollBar scrollBar = new FlatScrollBar( shell, Orientation.HORIZONTAL );
    shell.layout();

    Rectangle before = scrollBar.drag.getControl().getBounds();
    scrollBar.setMaximum( 80 );
    Rectangle after = scrollBar.drag.getControl().getBounds();

    assertThat( after ).isNotEqualTo( before );
  }

  @Test
  public void getThumb() {
    FlatScrollBar scrollBar = new FlatScrollBar( shell, Orientation.HORIZONTAL );

    int actual = scrollBar.getThumb();

    assertThat( actual ).isEqualTo( FlatScrollBar.DEFAULT_THUMB );
  }

  @Test
  public void getThumbOnMaximumAdjustment() {
    FlatScrollBar scrollBar = new FlatScrollBar( shell, Orientation.HORIZONTAL );
    scrollBar.setMaximum( 1 );

    int actual = scrollBar.getThumb();

    assertThat( actual ).isEqualTo( 1 );
  }

  @Test
  public void getThumbOnMinimumAdjustment() {
    FlatScrollBar scrollBar = new FlatScrollBar( shell, Orientation.HORIZONTAL );
    scrollBar.setMinimum( FlatScrollBar.DEFAULT_MAXIMUM - 1 );

    int actual = scrollBar.getThumb();

    assertThat( actual ).isEqualTo( 1 );
  }

  @Test
  public void setThumb() {
    int expected = 20;
    FlatScrollBar scrollBar = new FlatScrollBar( shell, Orientation.HORIZONTAL );
    scrollBar.setThumb( expected );

    int actual = scrollBar.getThumb();

    assertThat( actual ).isEqualTo( expected );
  }

  @Test
  public void setThumbWithTooSmallValue() {
    FlatScrollBar scrollBar = new FlatScrollBar( shell, Orientation.HORIZONTAL );
    scrollBar.setThumb( 0 );

    int actual = scrollBar.getThumb();

    assertThat( actual ).isEqualTo( FlatScrollBar.DEFAULT_THUMB );
  }

  @Test
  public void setThumbWithTooLargeValue() {
    FlatScrollBar scrollBar = new FlatScrollBar( shell, Orientation.HORIZONTAL );
    scrollBar.setThumb( FlatScrollBar.DEFAULT_MAXIMUM + 10 );

    int actual = scrollBar.getThumb();

    assertThat( actual ).isEqualTo( FlatScrollBar.DEFAULT_MAXIMUM );
  }

  @Test
  public void setThumbTriggersLayout() {
    FlatScrollBar scrollBar = new FlatScrollBar( shell, Orientation.HORIZONTAL );
    shell.layout();

    Rectangle before = scrollBar.drag.getControl().getBounds();
    scrollBar.setThumb( 20 );
    Rectangle after = scrollBar.drag.getControl().getBounds();

    assertThat( after ).isNotEqualTo( before );
  }

  @Test
  public void getIncrement() {
    FlatScrollBar scrollBar = new FlatScrollBar( shell, Orientation.HORIZONTAL );

    int actual = scrollBar.getIncrement();

    assertThat( actual ).isEqualTo( FlatScrollBar.DEFAULT_INCREMENT );
  }

  @Test
  public void setIncrement() {
    int expected = 10;
    FlatScrollBar scrollBar = new FlatScrollBar( shell, Orientation.HORIZONTAL );
    scrollBar.setIncrement( expected );

    int actual = scrollBar.getIncrement();

    assertThat( actual ).isEqualTo( expected );
  }

  @Test
  public void getPageIncrement() {
    FlatScrollBar scrollBar = new FlatScrollBar( shell, Orientation.HORIZONTAL );

    int actual = scrollBar.getPageIncrement();

    assertThat( actual ).isEqualTo( FlatScrollBar.DEFAULT_PAGE_INCREMENT );
  }

  @Test
  public void setPageIncrement() {
    int expected = 25;
    FlatScrollBar scrollBar = new FlatScrollBar( shell, Orientation.HORIZONTAL );
    scrollBar.setPageIncrement( expected );

    int actual = scrollBar.getPageIncrement();

    assertThat( actual ).isEqualTo( expected );
  }

  @Test
  public void getSelection() {
    FlatScrollBar scrollBar = new FlatScrollBar( shell, Orientation.HORIZONTAL );

    int actual = scrollBar.getSelection();

    assertThat( actual ).isEqualTo( FlatScrollBar.DEFAULT_SELECTION );
  }

  @Test
  public void getSelectionOnMinimumAdjustment() {
    FlatScrollBar scrollBar = new FlatScrollBar( shell, Orientation.HORIZONTAL );
    scrollBar.setMinimum( FlatScrollBar.DEFAULT_MAXIMUM - 1 );

    int actual = scrollBar.getSelection();

    assertThat( actual ).isEqualTo( 99 );
  }

  @Test
  public void getSelectionOnMaximumAdjustment() {
    FlatScrollBar scrollBar = new FlatScrollBar( shell, Orientation.HORIZONTAL );
    scrollBar.setThumb( 1 );
    scrollBar.setSelectionInternal( 10 );
    scrollBar.setMaximum( 2 );

    int actual = scrollBar.getSelection();

    assertThat( actual ).isEqualTo( 1 );
  }

  @Test
  public void setSelection() {
    int expected = 12;
    FlatScrollBar scrollBar = new FlatScrollBar( shell, Orientation.HORIZONTAL );
    scrollBar.setSelection( expected );

    int actual = scrollBar.getSelection();

    assertThat( actual ).isEqualTo( expected );
  }

  @Test
  public void setSelectionWithValueBelowMinimum() {
    FlatScrollBar scrollBar = new FlatScrollBar( shell, Orientation.HORIZONTAL );
    scrollBar.setSelection( FlatScrollBar.DEFAULT_MINIMUM - 1 );

    int actual = scrollBar.getSelection();

    assertThat( actual ).isEqualTo( scrollBar.getMinimum() );
  }

  @Test
  public void setSelectionWithMaximumValue() {
    FlatScrollBar scrollBar = new FlatScrollBar( shell, Orientation.HORIZONTAL );
    scrollBar.setSelection( FlatScrollBar.DEFAULT_MAXIMUM );

    int actual = scrollBar.getSelection();

    assertThat( actual ).isEqualTo( scrollBar.getMaximum() - scrollBar.getThumb() );
  }

  @Test
  public void setSelectionTriggersLayout() {
    FlatScrollBar scrollBar = new FlatScrollBar( shell, Orientation.HORIZONTAL );
    shell.layout();

    Rectangle before = scrollBar.drag.getControl().getBounds();
    scrollBar.setSelection( 20 );
    Rectangle after = scrollBar.drag.getControl().getBounds();

    assertThat( after ).isNotEqualTo( before );
  }

  @Test
  public void setSelectionInternal() {
    int expected = 12;
    FlatScrollBar scrollBar = new FlatScrollBar( shell, Orientation.HORIZONTAL );
    scrollBar.setSelectionInternal( expected );

    int actual = scrollBar.getSelection();

    assertThat( actual ).isEqualTo( expected );
  }

  @Test
  public void setSelectionInternalWithValueBelowMinimum() {
    FlatScrollBar scrollBar = new FlatScrollBar( shell, Orientation.HORIZONTAL );
    scrollBar.setSelectionInternal( FlatScrollBar.DEFAULT_MINIMUM - 1 );

    int actual = scrollBar.getSelection();

    assertThat( actual ).isEqualTo( scrollBar.getMinimum() );
  }

  @Test
  public void setSelectionInternalWithMaximumValue() {
    FlatScrollBar scrollBar = new FlatScrollBar( shell, Orientation.HORIZONTAL );
    scrollBar.setSelectionInternal( FlatScrollBar.DEFAULT_MAXIMUM );

    int actual = scrollBar.getSelection();

    assertThat( actual ).isEqualTo( scrollBar.getMaximum() - scrollBar.getThumb() );
  }

  @Test
  public void setSelectionInternalTriggersLayout() {
    FlatScrollBar scrollBar = new FlatScrollBar( shell, Orientation.HORIZONTAL );
    shell.layout();

    Rectangle before = scrollBar.drag.getControl().getBounds();
    scrollBar.setSelectionInternal( 20 );
    Rectangle after = scrollBar.drag.getControl().getBounds();

    assertThat( after ).isNotEqualTo( before );
  }

  @Test
  public void resize() {
    FlatScrollBar scrollBar = new FlatScrollBar( shell, Orientation.HORIZONTAL );
    shell.layout();

    Rectangle before = scrollBar.drag.getControl().getBounds();
    shell.setSize( 800, 800 );
    Rectangle after = scrollBar.drag.getControl().getBounds();

    assertThat( after ).isNotEqualTo( before );
  }

  @Test
  public void initialSizeSettings() {
    FlatScrollBar scrollBar = new FlatScrollBar( shell, Orientation.HORIZONTAL );

    Point size = scrollBar.getControl().getSize();

    assertThat( size.y ).isEqualTo( Orientation.BAR_BREADTH );
  }

  @Test
  @ConditionalIgnore(condition=GtkPlatform.class)
  public void colorSettings() {
    FlatScrollBar scrollBar = new FlatScrollBar( shell, Orientation.HORIZONTAL );

    assertThat( getBackground( scrollBar.up ) ).isEqualTo( getSlowBackground() );
    assertThat( getBackground( scrollBar.upFast ) ).isEqualTo( getFastBackground() );
    assertThat( getBackground( scrollBar.drag ) ).isEqualTo( getDragBackground() );
    assertThat( getBackground( scrollBar.downFast ) ).isEqualTo( getFastBackground() );
    assertThat( getBackground( scrollBar.down ) ).isEqualTo( getSlowBackground() );
  }

  @Test
  public void addScrollListener() {
    ArgumentCaptor<ScrollEvent> captor = ArgumentCaptor.forClass( ScrollEvent.class );
    FlatScrollBar scrollBar = new FlatScrollBar( shell, Orientation.HORIZONTAL );
    ScrollListener listener = mock( ScrollListener.class );

    scrollBar.addScrollListener( listener );
    scrollBar.setSelectionInternal( 2 );

    verify( listener ).selectionChanged( captor.capture() );
    assertThat( captor.getValue().getSelection() ).isEqualTo( 2 );
    assertThat( captor.getValue().getScrollBar() ).isSameAs( scrollBar );
  }

  @Test
  public void removeScrollListener() {
    FlatScrollBar scrollBar = new FlatScrollBar( shell, Orientation.HORIZONTAL );
    ScrollListener listener = mock( ScrollListener.class );
    scrollBar.addScrollListener( listener );

    scrollBar.removeScrollListener( listener );
    scrollBar.setSelectionInternal( 2 );

    verify( listener, never() ).selectionChanged( any( ScrollEvent.class ) );
  }

  @Test
  public void setSelectionDoesNotTriggerScrollEvent() {
    FlatScrollBar scrollBar = new FlatScrollBar( shell, Orientation.HORIZONTAL );
    ScrollListener listener = mock( ScrollListener.class );

    scrollBar.addScrollListener( listener );
    scrollBar.setSelection( 2 );

    verify( listener, never() ).selectionChanged( any( ScrollEvent.class ) );
  }

  @Test
  public void notifyListeners() {
    ArgumentCaptor<ScrollEvent> captor = ArgumentCaptor.forClass( ScrollEvent.class );
    FlatScrollBar scrollBar = new FlatScrollBar( shell, Orientation.HORIZONTAL );
    ScrollListener listener = mock( ScrollListener.class );
    scrollBar.addScrollListener( listener );
    scrollBar.setSelection( 2 );

    scrollBar.notifyListeners();

    verify( listener ).selectionChanged( captor.capture() );
    assertThat( captor.getValue().getSelection() ).isEqualTo( 2 );
    assertThat( captor.getValue().getScrollBar() ).isSameAs( scrollBar );
  }

  @Test
  public void layoutTriggersPaint() {
    FlatScrollBar scrollBar = new FlatScrollBar( shell, Orientation.HORIZONTAL );
    PaintListener listener = mock( PaintListener.class );
    scrollBar.getControl().addPaintListener( listener );

    Point size = scrollBar.getControl().getSize();
    scrollBar.getControl().setSize( size.x + 1, size.y );

    verify( listener ).paintControl( any( PaintEvent.class ) );
  }

  @Test
  public void controlLayout() {
    FlatScrollBar scrollBar = new FlatScrollBar( shell, Orientation.HORIZONTAL );

    Layout layout = scrollBar.control.getLayout();

    assertThat( layout ).isInstanceOf( FlatScrollBarLayout.class );
  }

  private static Color getBackground( ViewComponent viewComponent ) {
    return viewComponent.getControl().getBackground();
  }
}