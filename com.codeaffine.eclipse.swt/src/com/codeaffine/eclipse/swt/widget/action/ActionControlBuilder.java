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

import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.Function;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;

public class ActionControlBuilder {

  private final Function<Control, Menu> menuCreator;
  private final Consumer<Updatable> updateTrigger;
  private final BooleanSupplier enablement;
  private final Runnable action;
  private final Image image;

  public ActionControlBuilder() {
    this( null, null, null, null, null );
  }

  public ActionControlBuilder( Function<Control, Menu> menuCreator ) {
    this( menuCreator, null, null, null, null );
  }

  public ActionControlBuilder( Runnable action ) {
    this( null, action, null, null, null );
  }

  private ActionControlBuilder( Function<Control, Menu> menuCreator,
                                Runnable action,
                                Image image,
                                BooleanSupplier enablement,
                                Consumer<Updatable> updateTrigger )
  {
    this.menuCreator = menuCreator;
    this.action = action;
    this.image = image;
    this.enablement = enablement == null ? () -> true : enablement;
    this.updateTrigger = updateTrigger == null ? updatable -> {} : updateTrigger;
  }

  public ActionControlBuilder withImage( Image image ) {
    return new ActionControlBuilder( menuCreator, action, image, enablement, updateTrigger );
  }

  public ActionControlBuilder withEnablement( BooleanSupplier enablement ) {
    return new ActionControlBuilder( menuCreator, action, image, enablement, updateTrigger );
  }

  public ActionControlBuilder withUpdateTrigger( Consumer<Updatable> updateTrigger ) {
    return new ActionControlBuilder( menuCreator, action, image, enablement, updateTrigger );
  }

  public Control build( Composite parent ) {
    if( menuCreator != null ) {
      return new MenuSelector( menuCreator, image, enablement, updateTrigger ).create( parent );
    }
    if( action != null ) {
      return new ActionSelector( action, image, enablement, updateTrigger ).create( parent );
    }
    return createEmptyControl( parent );
  }

  public Function<Control, Menu> getMenuCreator() {
    return menuCreator;
  }

  public Runnable getAction() {
    return action;
  }

  private static Composite createEmptyControl( Composite parent ) {
    return new Composite( parent, SWT.NONE ) {
      @Override
      public Point computeSize( int wHint, int hHint, boolean changed ) {
        return new Point( 0, 0 );
      }
    };
  }
}