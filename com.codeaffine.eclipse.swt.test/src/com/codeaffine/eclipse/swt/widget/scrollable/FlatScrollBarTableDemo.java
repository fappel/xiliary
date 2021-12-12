/**
 * Copyright (c) 2014 - 2022 Frank Appel
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Frank Appel - initial API and implementation
 */
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