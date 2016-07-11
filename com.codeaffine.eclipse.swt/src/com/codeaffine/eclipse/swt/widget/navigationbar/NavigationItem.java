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

import static com.codeaffine.eclipse.swt.layout.FormDatas.attach;
import static com.codeaffine.eclipse.swt.widget.navigationbar.ActionControlImageAdapter.ARROW_DOWN;
import static com.codeaffine.eclipse.swt.widget.navigationbar.ActionControlImageAdapter.MINUS;
import static com.codeaffine.eclipse.swt.widget.navigationbar.ActionControlImageAdapter.PLUS;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;

public class NavigationItem {

  private final NavigationItemController controller;
  private final ImageProvider imageProvider;
  private final Runnable selectionListener;
  private final NavigationItemModel model;
  private final String iconName;

  private Label displayLabel;
  private Display display;

  public NavigationItem( ImageProvider imageProvider,
                         NavigationItemModel model,
                         NavigationItemController controller,
                         String iconName )
  {
    this.imageProvider = imageProvider;
    this.controller = controller;
    this.model = model;
    this.iconName = iconName;
    this.selectionListener = NavigationItem.this :: updateSelection;
    this.model.addSelectionChangedListener( selectionListener );
  }

  public void createControl( Composite parent ) {
    Composite navigationItem = createComposite( parent );
    createIcon( iconName, navigationItem );
    createActionSection( navigationItem );
    createSeparator( navigationItem );
    displayLabel = new Label( navigationItem, SWT.NONE );
    display = displayLabel.getDisplay();
    updateSelection();
    navigationItem.addListener( SWT.Dispose, evt -> dispose() );
  }

  String getSelectionText() {
    return displayLabel.getText();
  }

  private static Composite createComposite( Composite parent ) {
    Composite result = new Composite( parent, SWT.NONE );
    RowLayout layout = new RowLayout();
    layout.center = true;
    layout.fill = true;
    result.setLayout( layout );
    return result;
  }

  private void createIcon( String icon, Composite navigationItem ) {
    Label iconHolder = new Label( navigationItem, SWT.NONE );
    iconHolder.setImage( getImage( icon ) );
  }

  private void createActionSection( Composite navigationItem ) {
    Composite section = new Composite( navigationItem, SWT.NONE );
    section.setLayout( new FormLayout() );
    Control add = controller.getAddControlBuilder().build( section, getImage( PLUS ) );
    attach( add ).toLeft().toTop();
    Control remove = controller.getRemoveControlBuilder().build( section, getImage( MINUS ) );
    attach( remove ).toLeft().atTopTo( add ).toBottom();
    Control select = controller.getSelectControlBuilder().build( section, getImage( ARROW_DOWN ) );
    attach( select ).atLeftTo( add, 2 ).toTop().toRight().toBottom();
  }

  private static void createSeparator( Composite navigation ) {
    Label separator = new Label( navigation, SWT.NONE );
    separator.setText( " " );
  }

  private void updateSelection() {
    display.syncExec( () -> displayLabel.setText( model.getSelection().getDisplayName() ) );
  }

  private Image getImage( String imageName ) {
    return imageProvider.getImage( imageName );
  }

  private void dispose() {
    model.removeSelectionChangedListener( selectionListener );
    model.dispose();
  }
}