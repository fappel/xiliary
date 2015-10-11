package com.codeaffine.eclipse.swt.widget.scrollable;

import static com.codeaffine.eclipse.swt.test.util.ShellHelper.computeTrim;
import static com.codeaffine.eclipse.swt.test.util.ShellHelper.createDemoShell;
import static com.codeaffine.eclipse.swt.util.ReadAndDispatch.ERROR_BOX_HANDLER;
import static com.codeaffine.eclipse.swt.widget.scrollable.ScrollableAdapterFactoryHelper.adapt;
import static com.codeaffine.eclipse.swt.widget.scrollable.TableHelper.createPackedSingleColumnTable;
import static com.codeaffine.eclipse.swt.widget.scrollable.TreeHelper.expandRootLevelItems;
import static com.codeaffine.eclipse.swt.widget.scrollable.TreeHelper.expandTopBranch;
import static java.lang.Math.min;

import java.util.Random;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.part.PageBook;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.eclipse.swt.test.util.DisplayHelper;
import com.codeaffine.eclipse.swt.util.ReadAndDispatch;

public class ScrollableAdapterFactoryDemo {

  private static final int TABLE_ITEM_COUNT = 20;

  @Rule
  public final DisplayHelper displayHelper = new DisplayHelper();

  private Shell shell;

  @Before
  public void setUp() {
    shell = createDemoShell( displayHelper );
  }

  @Test
  public void treeDemo() {
    Tree tree = new TestTreeFactory().create( shell );
    adapt( tree, TreeAdapter.class );
    shell.open();
    expandRootLevelItems( tree );
    expandTopBranch( tree );
    spinLoop();
  }

  @Test
  public void treeWithBorderDemo() {
    Tree tree = new TestTreeFactory( SWT.BORDER ).create( shell );
    adapt( tree, TreeAdapter.class );
    shell.open();
    expandRootLevelItems( tree );
    expandTopBranch( tree );
    spinLoop();
  }

  @Test
  public void treeInPageBookDemo() {
    PageBook pageBook = new PageBook( shell, SWT.NONE );
    pageBook.setBackground( displayHelper.getDisplay().getSystemColor( SWT.COLOR_BLUE ) );
    Label label = new Label( pageBook, SWT.NONE );
    label.setText( "Frontpage" );
    Tree tree = new TestTreeFactory().create( pageBook );
    adapt( tree, TreeAdapter.class );
    expandRootLevelItems( tree );
    expandTopBranch( tree );
    scheduleRandomPageSelection( pageBook, label, tree );
    shell.open();
    spinLoop();
  }

  @Test
  public void tableDemo() {
    adapt( new TestTableFactory().create( shell ), TableAdapter.class );
    shell.open();
    spinLoop();
  }

  @Test
  public void tableDemoWithColumnWidthAdjustment() {
    Table table = equipWithSingleColumnTable( shell );
    adapt( table, TableAdapter.class );
    shell.setBounds( computeTrim( shell, adjustTableHeight( table, TABLE_ITEM_COUNT + 2 ) ) );
    table.getColumns()[ 0 ].setWidth( table.getClientArea().width );
    shell.setLocation( 300, 300 );
    shell.open();
    spinLoop();
  }

  private void spinLoop() {
    new ReadAndDispatch( ERROR_BOX_HANDLER ).spinLoop( shell );
  }

  private void scheduleRandomPageSelection( PageBook pageBook, Control page1, Control page2 ) {
    displayHelper.getDisplay().timerExec( 2000, () -> {
      pageBook.showPage( page1 );
      if( new Random().nextBoolean() ) {
        pageBook.showPage( page2 );
      }
      scheduleRandomPageSelection( pageBook, page1, page2 );
    } );
  }

  private static Table equipWithSingleColumnTable( Shell shell ) {
    shell.setLayout( new FillLayout() );
    Table result = createPackedSingleColumnTable( shell, TABLE_ITEM_COUNT );
    shell.pack();
    return result;
  }

  private static Rectangle adjustTableHeight( Table table, int maxItems ) {
    Rectangle result = table.getBounds();
    result.height = min( result.height, table.getItemHeight() * maxItems );
    table.setBounds( result );
    return result;
  }
}