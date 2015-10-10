package com.codeaffine.eclipse.swt.widget.scrollable;

import static com.codeaffine.eclipse.swt.test.util.ShellHelper.createDemoShell;
import static com.codeaffine.eclipse.swt.util.ReadAndDispatch.ERROR_BOX_HANDLER;

import org.eclipse.swt.widgets.Shell;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.eclipse.swt.test.util.DisplayHelper;
import com.codeaffine.eclipse.swt.util.ReadAndDispatch;

public class FlatScrollBarTableDemo {

  @Rule
  public final DisplayHelper displayHelper = new DisplayHelper();

  @Test
  public void demo() {
    Shell shell = createDemoShell( displayHelper );
    new FlatScrollBarTable( shell, new TestTableFactory() );
    shell.open();
    new ReadAndDispatch( ERROR_BOX_HANDLER ).spinLoop( shell );
  }
}