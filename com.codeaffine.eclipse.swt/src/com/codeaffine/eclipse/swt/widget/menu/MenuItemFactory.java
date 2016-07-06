/**
 * Copyright (c) 2014 - 2016 Frank Appel
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Frank Appel - initial API and implementation
 */
package com.codeaffine.eclipse.swt.widget.menu;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

public class MenuItemFactory {

  public void createPushItem( Menu menu, String label, Listener listener ) {
    MenuItem menuItem = new MenuItem( menu, SWT.PUSH );
    menuItem.setText( label );
    menuItem.addListener( SWT.Selection, listener );
  }

  public void createSeparator( Menu menu ) {
    new MenuItem( menu, SWT.SEPARATOR );
  }
}