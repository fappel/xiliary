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
package com.codeaffine.eclipse.swt.widget.navigationbar;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

import com.codeaffine.util.Disposable;

public class NavigationBarFactory implements Disposable {

  private final ActionControlImageAdapter imageProvider;
  private final List<NavigationItemModel> models;
  private final List<NavigationItem> items;

  public NavigationBarFactory( ImageProvider imageProvider ) {
    this.imageProvider = new ActionControlImageAdapter( imageProvider );
    this.models = new ArrayList<>();
    this.items = new ArrayList<>();
  }

  public NavigationBarFactory with( NavigationItemModel model, NavigationItemController controller, String iconName ) {
    models.add( model );
    items.add( new NavigationItem( imageProvider, model, controller, iconName ) );
    return this;
  }

  public Control create( Composite parent ) {
    Composite result = createComposite( parent );
    createItems( result );
    registerLayoutTrigger( result );
    return result;
  }

  @Override
  public void dispose() {
    imageProvider.dispose();
  }

  ImageProvider getImageProvider() {
    return imageProvider;
  }

  private static Composite createComposite( Composite parent ) {
    Composite result = new Composite( parent, SWT.NONE );
    RowLayout layout = new RowLayout();
    layout.marginLeft = 0;
    layout.marginTop = 0;
    layout.marginRight = 0;
    layout.marginBottom = 0;
    layout.center = true;
    layout.wrap = false;
    result.setLayout( layout );
    return result;
  }

  private void createItems( Composite parent ) {
    items.forEach( item -> createItem( parent, item ) );
    items.clear();
  }

  private void createItem( Composite parent, NavigationItem item ) {
    item.createControl( parent );
    if( !isLastItem( item ) ) {
      createSeparator( parent );
    }
  }

  private boolean isLastItem( NavigationItem item ) {
    return items.indexOf( item ) == items.size() - 1;
  }

  private static void createSeparator( Composite navigation ) {
    Label separator = new Label( navigation, SWT.NONE );
    separator.setText( " > " );
  }

  private void registerLayoutTrigger( Composite control ) {
    models.forEach( register( createLayoutTrigger( control ) ) );
    models.clear();
  }

  private static Consumer<? super NavigationItemModel> register( Runnable layoutTrigger ) {
    return model -> model.addSelectionChangedListener( layoutTrigger );
  }

  private static Runnable createLayoutTrigger( Composite control ) {
    return () -> control.getDisplay().syncExec( () -> control.layout() );
  }
}
