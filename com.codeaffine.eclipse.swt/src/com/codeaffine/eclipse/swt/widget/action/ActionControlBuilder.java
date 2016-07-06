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
package com.codeaffine.eclipse.swt.widget.action;

import java.util.function.Function;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;

public class ActionControlBuilder {

  private final Function<Control, Menu> menuCreator;
  private final Runnable action;

  public ActionControlBuilder( Function<Control, Menu> menuCreator ) {
    this.menuCreator = menuCreator;
    this.action = null;
  }

  public ActionControlBuilder( Runnable action ) {
    this.action = action;
    this.menuCreator = null;
  }

  public Control build( Composite parent, Image image ) {
    if( menuCreator != null ) {
      return new MenuSelector( menuCreator, image ).create( parent );
    }
    return new ActionSelector( action, image ).create( parent );
  }

  public Function<Control, Menu> getMenuCreator() {
    return menuCreator;
  }

  public Runnable getAction() {
    return action;
  }
}