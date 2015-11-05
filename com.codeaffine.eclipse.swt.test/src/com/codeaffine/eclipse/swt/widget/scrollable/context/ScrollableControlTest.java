package com.codeaffine.eclipse.swt.widget.scrollable.context;

import static com.codeaffine.eclipse.swt.test.util.ShellHelper.createShell;
import static com.codeaffine.eclipse.swt.widget.scrollable.TreeHelper.createTree;
import static com.codeaffine.eclipse.swt.widget.scrollable.TreeHelper.expandRootLevelItems;
import static com.codeaffine.eclipse.swt.widget.scrollable.TreeHelper.expandTopBranch;
import static com.codeaffine.test.util.lang.ThrowableCaptor.thrownBy;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Scrollable;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Tree;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.eclipse.swt.test.util.DisplayHelper;

public class ScrollableControlTest {

  private static final String KEY = "key";
  private static final Object VALUE = new Object();

  @Rule
  public final DisplayHelper displayHelper = new DisplayHelper();

  @Test
  public void getItemHeightOfTable() {
    Table scrollable = createScrollable( Table.class );
    ScrollableControl<?> scrollableControl = new ScrollableControl<>( scrollable );

    int actual = scrollableControl.getItemHeight();

    assertThat( actual ).isSameAs( scrollable.getItemHeight() );
  }

  @Test
  public void getItemHeightOfTree() {
    Tree scrollable = createScrollable( Tree.class );
    ScrollableControl<?> scrollableControl = new ScrollableControl<>( scrollable );

    int actual = scrollableControl.getItemHeight();

    assertThat( actual ).isSameAs( scrollable.getItemHeight() );
  }

  @Test
  public void getItemHeightOfUnsupportedType() {
    ScrollableControl<?> scrollableControl = createScrollableControl( Composite.class );

    Throwable actual = thrownBy( () -> scrollableControl.getItemHeight() );

    assertThat( actual ).isInstanceOf( IllegalArgumentException.class );
  }

  @Test
  public void isOwnerDrawn() {
    ScrollableControl<?> scrollableControl = createScrollableControl( Tree.class );

    boolean actual = scrollableControl.isOwnerDrawn();

    assertThat( actual ).isFalse();
  }

  @Test
  public void isOwnerDrawnWithMeasurmentItemListenerIsRegistered() {
    Tree scrollable = createScrollable( Tree.class );
    scrollable.addListener( SWT.MeasureItem, evt -> {} );
    ScrollableControl<?> scrollableControl = new ScrollableControl<>( scrollable );

    boolean actual = scrollableControl.isOwnerDrawn();

    assertThat( actual ).isTrue();
  }

  @Test
  public void isSameAsWithScrollableAsArgument() {
    Tree scrollable = createScrollable( Tree.class );
    ScrollableControl<?> scrollableControl = new ScrollableControl<>( scrollable );

    boolean actual = scrollableControl.isSameAs( scrollable );

    assertThat( actual ).isTrue();
  }

  @Test
  public void isSameAsWithDifferentWidgetAsArgument() {
    Shell shell = displayHelper.createShell();
    Tree scrollable = new Tree( shell, SWT.NONE );
    Label other = new Label( shell, SWT.NONE );
    ScrollableControl<?> scrollableControl = new ScrollableControl<>( scrollable );

    boolean actual = scrollableControl.isSameAs( other );

    assertThat( actual ).isFalse();
  }

  @Test
  public void isSameAsWithNullAsArgument() {
    ScrollableControl<?> scrollableControl = createScrollableControl( Tree.class );

    boolean actual = scrollableControl.isSameAs( null );

    assertThat( actual ).isFalse();
  }

  @Test
  public void addListener() {
    Tree scrollable = createScrollable( Tree.class );
    ScrollableControl<?> scrollableControl = new ScrollableControl<>( scrollable );

    scrollableControl.addListener( SWT.MeasureItem, evt -> {} );
    boolean actual = scrollable.getListeners( SWT.MeasureItem ).length > 0;

    assertThat( actual ).isTrue();
  }

  @Test
  public void getDisplay() {
    ScrollableControl<?> scrollableControl = createScrollableControl( Tree.class );

    Display actual = scrollableControl.getDisplay();

    assertThat( actual ).isSameAs( displayHelper.getDisplay() );
  }

  @Test
  public void setParent() {
    Tree tree = createScrollable( Tree.class );
    Composite expected = createScrollable( Composite.class );
    ScrollableControl<?> scrollableControl = new ScrollableControl<>( tree );

    scrollableControl.setParent( expected );
    Composite actual = tree.getParent();

    assertThat( actual ).isSameAs( expected );
  }

  @Test
  public void isChildOf() {
    Tree scrollable = createScrollable( Tree.class );
    ScrollableControl<?> scrollableControl = new ScrollableControl<>( scrollable );

    boolean actual = scrollableControl.isChildOf( scrollable.getParent() );

    assertThat( actual ).isTrue();
  }

  @Test
  public void isChildOfWithNonMatchingParent() {
    Tree scrollable = createScrollable( Tree.class );
    ScrollableControl<?> scrollableControl = new ScrollableControl<>( scrollable );

    boolean actual = scrollableControl.isChildOf( scrollable );

    assertThat( actual ).isFalse();
  }

  @Test
  public void setRedraw() {
    Tree scrollable = mock( Tree.class );
    ScrollableControl<?> scrollableControl = new ScrollableControl<>( scrollable );

    scrollableControl.setRedraw( false );

    verify( scrollable ).setRedraw( false );
  }

  @Test
  public void computePreferredSize() {
    Tree scrollable = createScrollable( Tree.class );
    ScrollableControl<?> scrollableControl = new ScrollableControl<>( scrollable );
    Point expected = scrollable.computeSize( SWT.DEFAULT, SWT.DEFAULT, true );

    Point actual = scrollableControl.computePreferredSize();

    assertThat( actual )
      .isNotEqualTo( new Point( 0, 0 ) )
      .isEqualTo( expected );
  }

  @Test
  public void computeSize() {
    Tree scrollable = createScrollable( Tree.class );
    ScrollableControl<?> scrollableControl = new ScrollableControl<>( scrollable );
    Point expected = scrollable.computeSize( 100, 200, true );

    Point actual = scrollableControl.computeSize( 100, 200, true );

    assertThat( actual ).isEqualTo( expected );
  }

  @Test
  public void setSize() {
    Tree scrollable = createScrollable( Tree.class );
    ScrollableControl<?> scrollableControl = new ScrollableControl<>( scrollable );
    Point expected = new Point( 20, 30 );

    scrollableControl.setSize( expected.x, expected.y );
    Point actual = scrollableControl.getSize();

    assertThat( actual ).isEqualTo( expected );
  }

  @Test
  public void setSizeWithPointAsArgument() {
    Tree scrollable = createScrollable( Tree.class );
    ScrollableControl<?> scrollableControl = new ScrollableControl<>( scrollable );
    Point expected = new Point( 20, 30 );

    scrollableControl.setSize( expected );
    Point actual = scrollableControl.getSize();

    assertThat( actual ).isEqualTo( expected );
  }

  @Test
  public void getBounds() {
    Tree scrollable = createScrollable( Tree.class );
    ScrollableControl<?> scrollableControl = new ScrollableControl<>( scrollable );
    Rectangle expected = scrollable.getBounds();

    Rectangle actual = scrollableControl.getBounds();

    assertThat( actual ).isEqualTo( expected );
  }

  @Test
  public void setData() {
    Tree scrollable = createScrollable( Tree.class );
    ScrollableControl<?> scrollableControl = new ScrollableControl<>( scrollable );

    scrollableControl.setData( KEY, VALUE );
    Object expected = scrollableControl.getData( KEY );

    assertThat( VALUE ).isEqualTo( expected );
  }

  @Test
  public void hasStyle() {
    Scrollable scrollable = new Tree( displayHelper.createShell(), SWT.VIRTUAL );
    ScrollableControl<?> scrollableControl = new ScrollableControl<>( scrollable );

    boolean actual = scrollableControl.hasStyle( SWT.VIRTUAL );

    assertThat( actual ).isTrue();
  }

  @Test
  public void setVisible() {
    Scrollable scrollable = createScrollableWithVisibleScrollbars();
    ScrollableControl<Scrollable> scrollableControl = new ScrollableControl<>( scrollable );

    scrollableControl.setVisible( false );
    boolean actual = scrollableControl.getVisible();

    assertThat( actual ).isFalse();
  }

  @Test
  public void isInstanceOfWithMatchingTypeArgument() {
    ScrollableControl<?> scrollableControl = createScrollableControl( Tree.class );

    boolean actual = scrollableControl.isInstanceof( Tree.class );

    assertThat( actual ).isTrue();
  }

  @Test
  public void isInstanceOfWithNonMatchingTypeArgument() {
    ScrollableControl<?> scrollableControl = createScrollableControl( Tree.class );

    boolean actual = scrollableControl.isInstanceof( Table.class );

    assertThat( actual ).isFalse();
  }

  @Test
  public void isInstanceOfWithNullAsArgument() {
    ScrollableControl<?> scrollableControl = createScrollableControl( Tree.class );

    Throwable actual = thrownBy( () -> scrollableControl.isInstanceof( null ) );

    assertThat( actual ).isInstanceOf( IllegalArgumentException.class );
  }

  @Test
  public void getBackground() {
    Tree scrollable = createScrollable( Tree.class );
    ScrollableControl<?> scrollableControl = new ScrollableControl<>( scrollable );
    Color expected = displayHelper.getSystemColor( SWT.COLOR_BLUE );
    scrollable.setBackground( expected );

    Color actual = scrollableControl.getBackground();

    assertThat( actual ).isEqualTo( expected );
  }

  @Test
  public void getControl() {
    Tree expected = createScrollable( Tree.class );
    ScrollableControl<?> scrollableControl = new ScrollableControl<>( expected );

    Scrollable actual = scrollableControl.getControl();

    assertThat( actual ).isSameAs( expected );
  }

  @Test
  public void getBorderWidth() {
    Tree tree = new Tree( displayHelper.createShell(), SWT.BORDER );
    ScrollableControl<Tree> scrollableControl = new ScrollableControl<>( tree );
    int expected = tree.getBorderWidth();

    int actual = scrollableControl.getBorderWidth();

    assertThat( actual ).isEqualTo( expected );
  }

  @Test
  public void setLocation() {
    Tree scrollable = createScrollable( Tree.class );
    ScrollableControl<?> scrollableControl = new ScrollableControl<>( scrollable );
    Point expected = new Point( 2, 3 );

    scrollableControl.setLocation( expected );
    Point actual = scrollableControl.getLocation();

    assertThat( actual ).isEqualTo( expected );
  }

  @Test
  public void getBorderWidthIfNoBorderIsSet() {
    Tree tree = createScrollable( Tree.class );
    ScrollableControl<Tree> scrollableControl = new ScrollableControl<>( tree );

    int actual = scrollableControl.getBorderWidth();

    assertThat( actual ).isZero();
  }

  /////////////////////////////
  // scrollbar properties tests

  @Test
  public void isVerticalBarVisible() {
    Scrollable scrollable = createScrollableWithVisibleScrollbars();

    boolean actual = new ScrollableControl<>( scrollable ).isVerticalBarVisible();

    assertThat( actual ).isTrue();
  }

  @Test
  public void getVerticalBarSize() {
    Tree scrollable = createScrollable( Tree.class );
    ScrollableControl<?> scrollableControl = new ScrollableControl<>( scrollable );
    Point expected = scrollable.getVerticalBar().getSize();

    Point actual = scrollableControl.getVerticalBarSize();

    assertThat( actual ).isEqualTo( expected );
  }

  @Test
  public void isHorizontalBarVisible() {
    Scrollable scrollable = createScrollableWithVisibleScrollbars();

    boolean actual = new ScrollableControl<>( scrollable ).isHorizontalBarVisible();

    assertThat( actual ).isTrue();
  }

  @Test
  public void isHorizontalBarVisibleIfScrollableHasNoBars() {
    Tree scrollable = createScrollable( Tree.class );

    boolean actual = new ScrollableControl<>( scrollable ).isHorizontalBarVisible();

    assertThat( actual ).isFalse();
  }

  @Test
  public void setHorizontalBarVisible() {
    Scrollable scrollable = createScrollableWithVisibleScrollbars();

    new ScrollableControl<>( scrollable ).setHorizontalBarVisible( false );
    boolean actual = scrollable.getHorizontalBar().isVisible();

    assertThat( actual ).isFalse();
  }

  @Test
  public void getHorizontalBarSize() {
    Tree scrollable = createScrollable( Tree.class );
    ScrollableControl<?> scrollableControl = new ScrollableControl<>( scrollable );
    Point expected = scrollable.getHorizontalBar().getSize();

    Point actual = scrollableControl.getHorizontalBarSize();

    assertThat( actual ).isEqualTo( expected );
  }

  @Test
  public void getHorizontalBarMaximum() {
    Scrollable scrollable = createScrollableWithVisibleScrollbars();
    int expected = scrollable.getHorizontalBar().getMaximum();

    int actual = new ScrollableControl<>( scrollable ).getHorizontalBarMaximum();

    assertThat( actual ).isEqualTo( expected );
  }

  private Scrollable createScrollableWithVisibleScrollbars() {
    Shell shell = createShell( displayHelper );
    Tree result = createTree( shell, 6, 4 );
    expandRootLevelItems( result );
    expandTopBranch( result );
    shell.open();
    return result;
  }

  private <T extends Scrollable> ScrollableControl<T> createScrollableControl( Class<T> scrollableType ) {
    T scrollable = createScrollable( scrollableType );
    return new ScrollableControl<>( scrollable );
  }

  private <T extends Scrollable> T createScrollable( Class<T> scrollableType ) {
    try {
      return scrollableType.getConstructor( Composite.class, int.class )
                           .newInstance( displayHelper.createShell(), SWT.NONE );
    } catch( RuntimeException bad ) {
      throw bad;
    } catch( Exception veryBad ) {
      throw new IllegalArgumentException( veryBad );
    }
  }
}