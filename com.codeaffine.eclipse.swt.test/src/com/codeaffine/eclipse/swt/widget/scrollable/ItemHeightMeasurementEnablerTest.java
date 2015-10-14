package com.codeaffine.eclipse.swt.widget.scrollable;

import static com.codeaffine.eclipse.swt.test.util.ShellHelper.createShell;
import static com.codeaffine.eclipse.swt.widget.scrollable.TableHelper.createTable;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Listener;
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

public class ItemHeightMeasurementEnablerTest {

  @Rule public final ConditionalIgnoreRule ignoreRule = new ConditionalIgnoreRule();
  @Rule public final DisplayHelper displayHelper = new DisplayHelper();

  private ScrollableAdapterFactory adapterFactory;
  private Shell shell;
  private Table table;

  @Before
  public void setUp() {
    adapterFactory = new ScrollableAdapterFactory();
    shell = createShell( displayHelper );
    table = createTable( shell, 10 );
  }

  @Test
  @ConditionalIgnore( condition = GtkPlatform.class )
  public void registerOnTableAdapterCreation() {
    adapterFactory.create( table, TableAdapter.class );

    assertThat( getListeners( table, SWT.MeasureItem ) )
      .hasSize( 1 );
    assertThat( getListeners( table, SWT.EraseItem ) )
      .hasSize( 1 );
  }

  @Test
  @ConditionalIgnore( condition = GtkPlatform.class )
  public void changeTableItemHeight() {
    adapterFactory.create( table, TableAdapter.class );
    int expectedHeight = configureTableItemHeightAdjuster();
    shell.open();

    new ReadAndDispatch().spinLoop( shell, 100 );

    assertThat( table.getItemHeight() ).isEqualTo( expectedHeight );
  }

  private int configureTableItemHeightAdjuster() {
    int result = 24;
    table.addListener( SWT.MeasureItem, evt -> evt.height = result );
    return result;
  }

  private static List<Listener> getListeners( Table table, int eventType ) {
    return asList( table.getListeners( eventType ) );
  }
}