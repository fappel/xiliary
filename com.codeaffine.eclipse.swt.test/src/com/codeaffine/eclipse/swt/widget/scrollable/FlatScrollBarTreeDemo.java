package com.codeaffine.eclipse.swt.widget.scrollable;

import static com.codeaffine.eclipse.swt.test.util.ShellHelper.createShell;
import static com.codeaffine.eclipse.swt.util.ReadAndDispatch.ERROR_BOX_HANDLER;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Shell;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.eclipse.swt.test.util.DisplayHelper;
import com.codeaffine.eclipse.swt.util.ReadAndDispatch;

public class FlatScrollBarTreeDemo {

  @Rule
  public final DisplayHelper displayHelper = new DisplayHelper();

  private TestTreeFactory testTreeFactory;
  private Shell shell;

  @Before
  public void setUp() {
    shell = createShell( displayHelper, SWT.SHELL_TRIM );
    shell.setBackground( displayHelper.getDisplay().getSystemColor( SWT.COLOR_WHITE ) );
    FillLayout layout = new FillLayout();
    layout.marginHeight = 10;
    layout.marginWidth = 10;
    shell.setLayout( layout );
    testTreeFactory = new TestTreeFactory();
    new FlatScrollBarTree( shell, testTreeFactory );
    shell.open();
  }

  @Test
  public void demo() {
    TreeHelper.expandRootLevelItems( testTreeFactory.getTree() );
    TreeHelper.expandTopBranch( testTreeFactory.getTree() );
    new ReadAndDispatch( ERROR_BOX_HANDLER ).spinLoop( shell );
  }
}