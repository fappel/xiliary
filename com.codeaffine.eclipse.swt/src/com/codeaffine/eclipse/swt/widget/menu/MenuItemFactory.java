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