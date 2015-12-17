package com.codeaffine.eclipse.swt.widget.scrollable;

import static com.codeaffine.eclipse.swt.test.util.ShellHelper.createShell;
import static com.codeaffine.eclipse.swt.widget.scrollable.TableHelper.HEADER_TITLES;
import static com.codeaffine.eclipse.swt.widget.scrollable.TableHelper.createTable;
import static com.codeaffine.eclipse.swt.widget.scrollable.TableHelper.createTableInSingleCellGridLayout;
import static com.codeaffine.test.util.lang.ThrowableCaptor.thrownBy;
import static org.assertj.core.api.Assertions.assertThat;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.eclipse.swt.test.util.DisplayHelper;
import com.codeaffine.eclipse.swt.test.util.SWTIgnoreConditions.GtkPlatform;
import com.codeaffine.eclipse.swt.util.ReadAndDispatch;
import com.codeaffine.test.util.junit.ConditionalIgnoreRule;
import com.codeaffine.test.util.junit.ConditionalIgnoreRule.ConditionalIgnore;
import com.codeaffine.test.util.lang.ThrowableCaptor;

public class TableAdapterTest {

  private static final int SELECTION = 50;

  @Rule public final ConditionalIgnoreRule ignoreRule = new ConditionalIgnoreRule();
  @Rule public final DisplayHelper displayHelper = new DisplayHelper();

  private ScrollableAdapterFactory adapterFactory;
  private TableAdapter adapter;
  private Object layoutData;
  private Shell shell;
  private Table table;

  @Before
  public void setUp() {
    adapterFactory = new ScrollableAdapterFactory();
    shell = createShell( displayHelper );
    table = createTable( shell, 10 );
    layoutData = new Object();
    table.setLayoutData( layoutData );
    adapter = adapterFactory.create( table, TableAdapter.class );
  }

  @Test
  @ConditionalIgnore( condition = GtkPlatform.class )
  public void adapt() {
    assertThat( adapter.getChildren() ).contains( table );
    assertThat( adapter.getLayout() ).isInstanceOf( ScrollableLayout.class );
    assertThat( adapter.getBounds() ).isEqualTo( shell.getClientArea() );
    assertThat( adapter.getLayoutData() ).isSameAs( layoutData );
    assertThat( adapter.getScrollable() ).isSameAs( table );
  }

  @Test
  public void setLayout() {
    Throwable actual = ThrowableCaptor.thrownBy( () -> adapter.setLayout( new FillLayout() ) );

    assertThat( actual ).isInstanceOf( UnsupportedOperationException.class );
  }

  @Test
  @ConditionalIgnore( condition = GtkPlatform.class )
  public void disposalOfAdapter() {
    adapter.dispose();

    assertThat( table.isDisposed() ).isTrue();
  }

  @Test
  @ConditionalIgnore( condition = GtkPlatform.class )
  public void disposalOfTable() {
    table.dispose();

    assertThat( adapter.isDisposed() ).isTrue();
    assertThat( shell.getChildren() ).isEmpty();
  }

  @Test
  public void disposalWithDragSourceOnTable() {
    Table table = new TestTableFactory().create( shell );
    ScrollableAdapterFactoryHelper.adapt( table, TableAdapter.class );
    DragSource dragSource = new DragSource( table, DND.DROP_MOVE | DND.DROP_COPY );

    shell.dispose();

    assertThat( dragSource.isDisposed() ).isTrue();
  }

  @Test
  public void constructor() {
    Throwable actual = thrownBy( () -> new TreeAdapter() );

    assertThat( actual )
      .hasMessageContaining( "Subclassing not allowed" )
      .isInstanceOf( SWTException.class );
  }

  @Test
  @ConditionalIgnore( condition = GtkPlatform.class )
  public void changeTableBounds() {
    openShellWithoutLayout();
    table = new Table( shell, SWT.NONE );
    adapter = adapterFactory.create( table, TableAdapter.class );
    waitForReconciliation();

    table.setBounds( expectedBounds() );
    waitForReconciliation();

    assertThat( adapter.getBounds() ).isEqualTo( expectedBounds() );
    assertThat( table.getBounds() ).isEqualTo( expectedBounds() );
  }

  @Test
  @ConditionalIgnore( condition = GtkPlatform.class )
  public void changeTableBoundsWithVisibleScrollBars() {
    openShellWithoutLayout();
    waitForReconciliation();

    table.setBounds( expectedBounds() );
    waitForReconciliation();

    assertThat( adapter.getBounds() ).isEqualTo( expectedBounds() );
    assertThat( table.getBounds() ).isNotEqualTo( expectedBounds() );
  }

  @Test
  @ConditionalIgnore( condition = GtkPlatform.class )
  public void changeTableBoundsWithHorizontalScroll() {
    openShellWithoutLayout();

    table.setBounds( expectedBounds() );
    waitForReconciliation();
    scrollHorizontal( adapter, SELECTION );

    assertThat( adapter.getBounds() ).isEqualTo( expectedBounds() );
    assertThat( table.getBounds() ).isNotEqualTo( expectedBounds() );
    assertThat( adapter.getHorizontalBar().getSelection() ).isEqualTo( SELECTION );
  }

  @Test
  @ConditionalIgnore( condition = GtkPlatform.class )
  public void changeTableVisibility() {
    waitForReconciliation();

    table.setVisible( false );
    waitForReconciliation();

    assertThat( adapter.getVisible() ).isFalse();
  }

  @Test
  @ConditionalIgnore( condition = GtkPlatform.class )
  public void changeTableEnablement() {
    waitForReconciliation();

    table.setEnabled( false );
    waitForReconciliation();

    assertThat( adapter.getEnabled() ).isFalse();
  }

  @Test
  public void getLayoutData() {
    RowData expected = new RowData();
    table.setLayoutData( expected );

    Object actual = adapter.getLayoutData();

    assertThat( actual ).isSameAs( expected );
  }

  @Test
  public void setLayoutData() {
    Object expected = new Object();

    adapter.setLayoutData( expected );
    Object actual = table.getLayoutData();

    assertThat( actual ).isSameAs( expected );
  }

  @Test
  public void computeSize() {
    Point expected = table.computeSize( SWT.DEFAULT, SWT.DEFAULT, true );

    Point actual = adapter.computeSize( SWT.DEFAULT, SWT.DEFAULT, true );

    assertThat( actual ).isEqualTo( expected );
  }

  @Test
  public void tableColumnDelegation() {
    assertThat( adapter.getColumn( 0 ) ).isNotNull();
    assertThat( adapter.getColumnCount() ).isEqualTo( HEADER_TITLES.length );
    assertThat( adapter.getColumnOrder() ).isEqualTo( table.getColumnOrder() );
    assertThat( adapter.getColumns() ).hasSize( HEADER_TITLES.length );
  }

  @Test
  @ConditionalIgnore( condition = GtkPlatform.class )
  public void changeTableItemHeight() {
    int expectedHeight = configureTableItemHeightAdjuster();
    shell.open();
    waitForReconciliation();

    assertThat( table.getItemHeight() ).isEqualTo( expectedHeight );
  }

  @Test
  @ConditionalIgnore( condition = GtkPlatform.class )
  public void packPopupShell() {
    shell.open();
    Shell popup = new Shell( shell, SWT.ON_TOP );
    popup.setLocation( shell.getLocation() );
    createTableInSingleCellGridLayout( popup,
      table -> ScrollableAdapterFactoryHelper.adapt( table, TableAdapter.class )
    );
    popup.pack();
    popup.setVisible( true );

    waitForReconciliation();

    TableAdapter actual = ( TableAdapter )popup.getChildren()[ 0 ];
    assertThat( actual.getVerticalBar().isVisible() ).isFalse();
  }

  private int configureTableItemHeightAdjuster() {
    int result = 24;
    table.addListener( SWT.MeasureItem, evt -> evt.height = result );
    return result;
  }

  private void scrollHorizontal( TableAdapter adapter, int selection ) {
    int duration = 100;
    displayHelper.getDisplay().timerExec( duration, () -> adapter.getHorizontalBar().setSelection( selection ) );
    new ReadAndDispatch().spinLoop( shell, duration * 2 );
  }

  private void openShellWithoutLayout() {
    shell.setLayout( null );
    shell.open();
  }

  private void waitForReconciliation() {
    new ReadAndDispatch().spinLoop( shell, WatchDog.DELAY * 6 );
  }

  private Rectangle expectedBounds() {
    Rectangle clientArea = shell.getClientArea();
    return new Rectangle( 5, 10, clientArea.width - 10, clientArea.height - 20 );
  }
}