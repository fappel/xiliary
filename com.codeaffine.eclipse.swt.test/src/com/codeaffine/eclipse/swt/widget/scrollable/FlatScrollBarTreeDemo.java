package com.codeaffine.eclipse.swt.widget.scrollable;

import static com.codeaffine.eclipse.swt.testhelper.ShellHelper.createShell;
import static com.codeaffine.eclipse.swt.widget.scrollable.TreeHelper.createTree;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.eclipse.swt.test.util.DisplayHelper;
import com.codeaffine.eclipse.swt.util.ReadAndDispatch;
import com.codeaffine.eclipse.swt.widget.scrollable.FlatScrollBarTree.TreeFactory;

public class FlatScrollBarTreeDemo {

  @Rule
  public final DisplayHelper displayHelper = new DisplayHelper();

  private Shell shell;
  private Tree tree;

  @Before
  public void setUp() {
    shell = createShell( displayHelper, SWT.SHELL_TRIM );
    shell.setBackground( displayHelper.getDisplay().getSystemColor( SWT.COLOR_WHITE ) );
    FillLayout layout = new FillLayout();
    layout.marginHeight = 10;
    layout.marginWidth = 10;
    shell.setLayout( layout );
    new FlatScrollBarTree( shell, new TreeFactory() {

      @Override
      public Tree create( Composite parent ) {
        tree = createTree( parent, 4, 6 );
        return tree;
      }
    } );
    shell.open();
  }

  @Test
  public void demo() {
    TreeHelper.expandRootLevelItems( tree );
    TreeHelper.expandTopBranch( tree );
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