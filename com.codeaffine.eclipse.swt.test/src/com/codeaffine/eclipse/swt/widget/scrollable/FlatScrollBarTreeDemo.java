package com.codeaffine.eclipse.swt.widget.scrollable;

import static com.codeaffine.eclipse.swt.test.util.ShellHelper.createDemoShell;
import static com.codeaffine.eclipse.swt.util.ReadAndDispatch.ERROR_BOX_HANDLER;
import static com.codeaffine.eclipse.swt.widget.scrollable.TreeHelper.expandRootLevelItems;
import static com.codeaffine.eclipse.swt.widget.scrollable.TreeHelper.expandTopBranch;

import org.eclipse.swt.widgets.Shell;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.eclipse.swt.test.util.DisplayHelper;
import com.codeaffine.eclipse.swt.util.ReadAndDispatch;

public class FlatScrollBarTreeDemo {

  @Rule
  public final DisplayHelper displayHelper = new DisplayHelper();

  @Test
  public void demo() {
    Shell shell = createDemoShell( displayHelper );
    TestTreeFactory testTreeFactory = new TestTreeFactory();
    new FlatScrollBarTree( shell, testTreeFactory );
    shell.open();
    expandRootLevelItems( testTreeFactory.getTree() );
    expandTopBranch( testTreeFactory.getTree() );
    new ReadAndDispatch( ERROR_BOX_HANDLER ).spinLoop( shell );
  }
}