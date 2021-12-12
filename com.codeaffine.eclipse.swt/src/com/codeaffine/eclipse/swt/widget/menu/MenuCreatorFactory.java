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
package com.codeaffine.eclipse.swt.widget.menu;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;

public class MenuCreatorFactory {

  public Function<Control, Menu> create( Consumer<Menu> itemCreator ) {
    return control -> createMenu( control, itemCreator );
  }

  private static Menu createMenu( Control control, Consumer<Menu> itemCreator ) {
    Menu result = new Menu( control );
    result.addListener( SWT.Show, evt -> createMenuItems( result, itemCreator ) );
    return result;
  }

  private static void createMenuItems( Menu menu, Consumer<Menu> itemCreator ) {
    Stream.of( menu.getItems() ).forEach( item -> item.dispose() );
    itemCreator.accept( menu );
  }
}