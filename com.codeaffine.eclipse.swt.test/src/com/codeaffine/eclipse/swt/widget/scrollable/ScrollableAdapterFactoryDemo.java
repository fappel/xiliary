package com.codeaffine.eclipse.swt.widget.scrollable;

import static com.codeaffine.eclipse.swt.test.util.ShellHelper.createShell;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Tree;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.eclipse.swt.test.util.DisplayHelper;
import com.codeaffine.eclipse.swt.util.ReadAndDispatch;

public class ScrollableAdapterFactoryDemo {

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
    TreeHelper.expandRootLevelItems( tree );
    TreeHelper.expandTopBranch( tree );
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

  private void spinLoop() {
    try {
      new ReadAndDispatch().spinLoop( shell );
    } catch (RuntimeException e) {
      MessageBox messageBox = new MessageBox( shell, SWT.ICON_ERROR );
      messageBox.setText( "Error" );
      messageBox.setMessage( "The following problem occured:\n\n" + e.getMessage() + "\n\nSee log for more info." );
      messageBox.open();
      e.printStackTrace();
    }
  }
}