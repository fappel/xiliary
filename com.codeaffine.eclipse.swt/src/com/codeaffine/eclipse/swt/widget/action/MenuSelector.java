/**
 * Copyright (c) 2014 - 2017 Frank Appel
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Frank Appel - initial API and implementation
 */
package com.codeaffine.eclipse.swt.widget.action;

import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.Function;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;

public class MenuSelector {

  private final Function<Control, Menu> menuCreator;
  private final ActionSelector actionSelector;

  private Menu menu;

  public MenuSelector(
    Function<Control, Menu> menuCreator, Image image, BooleanSupplier enablement, Consumer<Updatable> updateWiring )
  {
    this.actionSelector = new ActionSelector( () -> getMenu().setVisible( true ), image, enablement, updateWiring );
    this.menuCreator = menuCreator;
  }

  public Control create( Composite parent ) {
    Control result = actionSelector.create( parent );
    menu = menuCreator.apply( result );
    result.setMenu( menu );
    result.addListener( SWT.Dispose, evt -> menu.dispose() );
    return result;
  }

  private Menu getMenu() {
    return menu;
  }
}