package com.codeaffine.eclipse.swt.widget.scrollable;

import static com.codeaffine.eclipse.swt.test.util.ShellHelper.createShell;
import static com.codeaffine.eclipse.swt.widget.scrollable.TableHelper.createTable;
import static com.codeaffine.test.util.lang.ThrowableCaptor.thrown;
import static org.assertj.core.api.Assertions.assertThat;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
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
import com.codeaffine.test.util.lang.ThrowableCaptor.Actor;

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
  }

  @Test
  public void setLayout() {
    Throwable actual = thrown( new Actor() {
      @Override
      public void act() throws Throwable {
        adapter.setLayout( new FillLayout() );
      }
    } );

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
  public void disposalOfTree() {
    table.dispose();

    assertThat( adapter.isDisposed() ).isTrue();
    assertThat( shell.getChildren() ).isEmpty();
  }

  @Test
  public void constructor() {
    Throwable actual = thrown( new Actor() {
      @Override
      public void act() throws Throwable {
        new TreeAdapter();
      }
    } );

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

    table.setBounds( expectedBounds() );
    waitForReconciliation();

    assertThat( adapter.getBounds() ).isEqualTo( expectedBounds() );
    assertThat( table.getBounds() ).isEqualTo( expectedBounds() );
  }

  @Test
  @ConditionalIgnore( condition = GtkPlatform.class )
  public void changeTableBoundsWithVisibleScrollBars() {
    openShellWithoutLayout();

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
    table.setVisible( false );
    waitForReconciliation();

    assertThat( adapter.getVisible() ).isFalse();
  }

  private void scrollHorizontal( final TableAdapter adapter, final int selection ) {
    final int duration = 100;
    displayHelper.getDisplay().timerExec( duration, new Runnable() {
      @Override
      public void run() {
        adapter.getHorizontalBar().setSelection( selection );
      }
    } );
    new ReadAndDispatch().spinLoop( shell, duration * 2 );
  }

  private void openShellWithoutLayout() {
    shell.setLayout( null );
    shell.open();
  }

  private void waitForReconciliation() {
    new ReadAndDispatch().spinLoop( shell, WatchDog.DELAY * 3 );
  }

  private Rectangle expectedBounds() {
    Rectangle clientArea = shell.getClientArea();
    return new Rectangle( 5, 10, clientArea.width - 10, clientArea.height - 20 );
  }
}