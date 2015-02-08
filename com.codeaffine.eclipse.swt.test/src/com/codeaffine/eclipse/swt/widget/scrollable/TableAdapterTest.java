package com.codeaffine.eclipse.swt.widget.scrollable;

import static com.codeaffine.eclipse.swt.test.util.ShellHelper.createShell;
import static com.codeaffine.eclipse.swt.widget.scrollable.TableHelper.createTable;
import static com.codeaffine.test.util.lang.ThrowableCaptor.thrown;
import static org.assertj.core.api.Assertions.assertThat;

import org.eclipse.swt.SWTException;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.eclipse.swt.test.util.DisplayHelper;
import com.codeaffine.test.util.lang.ThrowableCaptor.Actor;

public class TableAdapterTest {

  @Rule
  public final DisplayHelper displayHelper = new DisplayHelper();

  private ScrollableAdapterFactory adapterFactory;
  private TableAdapter adapter;
  private Shell shell;
  private Table table;

  @Before
  public void setUp() {
    adapterFactory = new ScrollableAdapterFactory();
    shell = createShell( displayHelper );
    table = createTable( shell, 10 );
    adapter = adapterFactory.create( table, TableAdapter.class );
  }

  @Test
  public void adapt() {
    assertThat( adapter ).isSameAs( table.getParent() );
    assertThat( adapter.getLayout() ).isInstanceOf( ScrollableLayout.class );
    assertThat( adapter.getBounds() ).isEqualTo( shell.getClientArea() );
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
  public void disposalOfAdapter() {
    adapter.dispose();

    assertThat( table.isDisposed() ).isTrue();
  }

  @Test
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
}