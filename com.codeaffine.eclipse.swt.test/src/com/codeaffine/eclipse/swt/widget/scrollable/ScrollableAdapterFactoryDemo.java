package com.codeaffine.eclipse.swt.widget.scrollable;

import static com.codeaffine.eclipse.swt.test.util.ShellHelper.createShell;
import static com.codeaffine.eclipse.swt.util.ReadAndDispatch.ERROR_BOX_HANDLER;
import static com.codeaffine.eclipse.swt.widget.scrollable.TableHelper.createPackedSingleColumnTableDialog;
import static com.codeaffine.eclipse.swt.widget.scrollable.TreeHelper.expandRootLevelItems;
import static com.codeaffine.eclipse.swt.widget.scrollable.TreeHelper.expandTopBranch;
import static java.lang.Math.min;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Tree;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.eclipse.swt.test.util.DisplayHelper;
import com.codeaffine.eclipse.swt.util.ReadAndDispatch;

public class ScrollableAdapterFactoryDemo {

  private static final int TABLE_ITEM_COUNT = 20;

  @Rule
  public final DisplayHelper displayHelper = new DisplayHelper();

  private ScrollableAdapterFactory factory;
  private Shell shell;

  @Before
  public void setUp() {
    shell = createShell( displayHelper, SWT.SHELL_TRIM );
    shell.setBackground( displayHelper.getDisplay().getSystemColor( SWT.COLOR_WHITE ) );
    FillLayout layout = new FillLayout();
    layout.marginHeight = 10;
    layout.marginWidth = 10;
    shell.setLayout( layout );
    factory = new ScrollableAdapterFactory();
  }

  @Test
  public void treeDemo() {
    Tree tree = new TestTreeFactory().create( shell );
    TreeAdapter adapter = factory.create( tree, TreeAdapter.class );
    adapter.setThumbColor( Display.getCurrent().getSystemColor( SWT.COLOR_RED ) );
    shell.open();
    expandRootLevelItems( tree );
    expandTopBranch( tree );
    spinLoop();
  }

  @Test
  public void treeWithBorderDemo() {
    Tree tree = new TestTreeFactory( SWT.BORDER ).create( shell );
    TreeAdapter adapter = factory.create( tree, TreeAdapter.class );
    adapter.setThumbColor( Display.getCurrent().getSystemColor( SWT.COLOR_RED ) );
    shell.open();
    expandRootLevelItems( tree );
    expandTopBranch( tree );
    spinLoop();
  }

  @Test
  public void tableDemo() {
    Table table = new TestTableFactory().create( shell );
    TableAdapter adapter = factory.create( table, TableAdapter.class );
    adapter.setThumbColor( Display.getCurrent().getSystemColor( SWT.COLOR_RED ) );
    shell.open();
    spinLoop();
  }

  @Test
  public void tableDemoWithColumnWidthAdjustment() {
    Table table = createPackedSingleColumnTableDialog( shell, TABLE_ITEM_COUNT );
    TableAdapter adapter = factory.create( table, TableAdapter.class );
    adapter.setThumbColor( Display.getCurrent().getSystemColor( SWT.COLOR_RED ) );
    Rectangle tableBounds = adjustTableHeight( table, TABLE_ITEM_COUNT + 2 );
    shell.setBounds( computeShellTrim( tableBounds ) );
    table.getColumns()[ 0 ].setWidth( table.getClientArea().width );
    shell.setLocation( 300, 300 );
    shell.open();
    spinLoop();
  }

  private static Rectangle adjustTableHeight( Table table, int maxItems ) {
    Rectangle result = table.getBounds();
    result.height = min( result.height, table.getItemHeight() * maxItems );
    table.setBounds( result );
    return result;
  }

  private Rectangle computeShellTrim( Rectangle tableBounds ) {
    return shell.computeTrim( tableBounds.x, tableBounds.y, tableBounds.width, tableBounds.height );
  }

  private void spinLoop() {
    new ReadAndDispatch( ERROR_BOX_HANDLER ).spinLoop( shell );
  }
}