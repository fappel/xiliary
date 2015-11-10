package com.codeaffine.eclipse.swt.widget.scrollable;

import static com.codeaffine.eclipse.swt.test.util.DisplayHelper.flushPendingEvents;
import static com.codeaffine.eclipse.swt.test.util.ShellHelper.createShell;
import static com.codeaffine.eclipse.swt.widget.scrollable.TableHelper.createTable;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Tree;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.eclipse.swt.test.util.DisplayHelper;
import com.codeaffine.eclipse.swt.test.util.SWTIgnoreConditions.GtkPlatform;
import com.codeaffine.eclipse.swt.util.ReadAndDispatch;
import com.codeaffine.eclipse.swt.widget.scrollable.context.ScrollableControl;
import com.codeaffine.test.util.junit.ConditionalIgnoreRule;
import com.codeaffine.test.util.junit.ConditionalIgnoreRule.ConditionalIgnore;

public class ItemHeightMeasurementEnablerTest {

  @Rule public final ConditionalIgnoreRule ignoreRule = new ConditionalIgnoreRule();
  @Rule public final DisplayHelper displayHelper = new DisplayHelper();

  private ScrollableAdapterFactory adapterFactory;
  private ScrollableControl<?> scrollableControl;
  private Shell shell;
  private Table table;

  @Before
  public void setUp() {
    adapterFactory = new ScrollableAdapterFactory();
    shell = createShell( displayHelper );
    table = createTable( shell, 10 );
    scrollableControl = new ScrollableControl<>( table );
  }

  @Test
  public void initialStateWithTable() {
    ItemHeightMeasurementEnabler actual = new ItemHeightMeasurementEnabler( scrollableControl, shell );

    assertThat( actual.height ).isEqualTo( table.getItemHeight() );
    assertThat( actual.intermediateHeightBuffer ).isZero();
    assertThat( actual.onMeasurement ).isFalse();
  }

  @Test
  @ConditionalIgnore( condition = GtkPlatform.class )
  public void initialStateWithTree() {
    Tree tree = new Tree( shell, SWT.NONE );
    ScrollableControl<?> scrollableControl = new ScrollableControl<>( tree );

    ItemHeightMeasurementEnabler actual = new ItemHeightMeasurementEnabler( scrollableControl, shell );

    assertThat( actual.height ).isEqualTo( tree.getItemHeight() );
    assertThat( actual.intermediateHeightBuffer ).isZero();
    assertThat( actual.onMeasurement ).isFalse();
  }

  @Test
  @ConditionalIgnore( condition = GtkPlatform.class )
  public void createTableAdapter() {
    adapterFactory.create( table, TableAdapter.class );

    assertThat( getListeners( table, SWT.MeasureItem ) ).hasSize( 0 );
    assertThat( getListeners( table, SWT.EraseItem ) ).hasSize( 0 );
  }

  @Test
  @ConditionalIgnore( condition = GtkPlatform.class )
  public void registerTableItemHeightAdjuster() {
    adapterFactory.create( table, TableAdapter.class );
    shell.open();

    configureTableItemHeightAdjuster();
    new ReadAndDispatch().spinLoop( shell, 100 );

    assertThat( getListeners( table, SWT.MeasureItem ) ).hasSize( 2 );
    assertThat( getListeners( table, SWT.EraseItem ) ).hasSize( 2 );
    assertThat( table.getParent() ).isSameAs( shell );
  }

  @Test
  @ConditionalIgnore( condition = GtkPlatform.class )
  public void registerTableItemHeightAdjusterOnOtherTable() {
    adapterFactory.create( table, TableAdapter.class );
    Table other = createTable( shell, 10 );
    adapterFactory.create( other, TableAdapter.class );

    other.addListener( SWT.MeasureItem, evt -> {} );
    flushPendingEvents();

    assertThat( getListeners( table, SWT.MeasureItem ) ).hasSize( 0 );
    assertThat( getListeners( table, SWT.EraseItem ) ).hasSize( 0 );
  }

  @Test
  @ConditionalIgnore( condition = GtkPlatform.class )
  public void changeTableItemHeight() {
    adapterFactory.create( table, TableAdapter.class );
    int expectedHeight = configureTableItemHeightAdjuster();
    shell.open();

    new ReadAndDispatch().spinLoop( shell, 100 );

    assertThat( table.getItemHeight() ).isEqualTo( expectedHeight );
    assertThat( getListeners( table, SWT.MeasureItem ) ).hasSize( 2 );
    assertThat( getListeners( table, SWT.EraseItem ) ).hasSize( 2 );
    assertThat( table.getParent() ).isSameAs( shell );
  }

  private int configureTableItemHeightAdjuster() {
    int result = 24;
    table.addListener( SWT.MeasureItem, evt -> evt.height = result );
    table.addListener( SWT.EraseItem, evt -> {} );
    table.addListener( SWT.PaintItem, evt -> {} );
    return result;
  }

  private static List<Listener> getListeners( Table table, int eventType ) {
    return asList( table.getListeners( eventType ) );
  }
}