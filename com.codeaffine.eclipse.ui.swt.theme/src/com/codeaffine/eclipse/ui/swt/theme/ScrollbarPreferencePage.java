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
package com.codeaffine.eclipse.ui.swt.theme;

import static com.codeaffine.eclipse.swt.layout.FillLayouts.applyFillLayoutTo;
import static com.codeaffine.eclipse.ui.swt.theme.ScrollableAdapterContribution.ADAPTER_DEMEANOR;
import static com.codeaffine.eclipse.ui.swt.theme.ScrollableAdapterContribution.DEMEANOR_EXPAND_ON_MOUSE_OVER;
import static com.codeaffine.eclipse.ui.swt.theme.ScrollableAdapterContribution.DEMEANOR_FIXED_WIDTH;
import static com.codeaffine.eclipse.ui.swt.theme.ScrollableAdapterContribution.FLAT_SCROLL_BAR_INCREMENT_LENGTH;
import static com.codeaffine.eclipse.ui.swt.theme.ScrollbarPreferencesInitializer.UNSET;

import org.eclipse.jface.preference.ComboFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class ScrollbarPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

  private static final String INCREMENT_LENGTH = "Increment Button Style";
  private static final String DEMEANOR = "Demeanor";
  private static final String[][] DEMEANOR_ENTRIES = new String[][] {
    { "default", UNSET },
    { "expand on mouse over", DEMEANOR_EXPAND_ON_MOUSE_OVER },
    { "fixed width", DEMEANOR_FIXED_WIDTH },
  };
  private static final String[][] INCREMENT_ENTRIES = new String[][] {
    { "default", UNSET },
    { "none", "0"},
    { "small", "7" },
    { "large", "12" },
  };

  public ScrollbarPreferencePage() {
    super( "Scrollbars", GRID );
  }

  @Override
  public void init( IWorkbench workbench ) {
  }

  @Override
  protected IPreferenceStore doGetPreferenceStore() {
    return Activator.getInstance().getPreferenceStore();
  }
  @Override
  protected void createFieldEditors() {
    Composite scrollbarGroup = createGroup( "Flat Scrollbar Style" );
    createDemeanorEditor( scrollbarGroup );
    createIncrementLengthEditor( scrollbarGroup );
  }

  private Composite createGroup( String text ) {
    Group group = new Group( getFieldEditorParent(), SWT.NONE );
    group.setLayoutData( new GridData( SWT.FILL, SWT.CENTER, true, false ) );
    applyFillLayoutTo( group ).withMargin( 5 );
    group.setText( text );
    return new Composite( group, SWT.NONE );
  }

  private void createDemeanorEditor( Composite parent ) {
    addField( new ComboFieldEditor( ADAPTER_DEMEANOR, DEMEANOR, DEMEANOR_ENTRIES, parent ) );
  }

  private void createIncrementLengthEditor( Composite parent ) {
    addField( new ComboFieldEditor( FLAT_SCROLL_BAR_INCREMENT_LENGTH, INCREMENT_LENGTH, INCREMENT_ENTRIES, parent ) );
  }
}