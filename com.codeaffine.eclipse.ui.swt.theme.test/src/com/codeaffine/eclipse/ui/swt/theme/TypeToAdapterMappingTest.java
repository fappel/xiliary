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
package com.codeaffine.eclipse.ui.swt.theme;

import static com.codeaffine.eclipse.swt.test.util.ShellHelper.createShell;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Scrollable;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.eclipse.swt.test.util.DisplayHelper;
import com.codeaffine.eclipse.swt.widget.scrollable.ScrollableAdapterFactory.Adapter;
import com.codeaffine.eclipse.swt.widget.scrollable.TableAdapter;

public class TypeToAdapterMappingTest {

  @Rule
  public final DisplayHelper displayHelper = new DisplayHelper();

  @Test
  public void initialState() {
    Class<Table> scrollableType = Table.class;
    Class<TableAdapter> adapterType = TableAdapter.class;

    TypeToAdapterMapping<Table, TableAdapter> actual = new TypeToAdapterMapping<>( scrollableType, adapterType );

    assertThat( actual.scrollableType ).isSameAs( scrollableType );
    assertThat( actual.adapterType ).isSameAs( adapterType );
  }

  @Test( expected = IllegalArgumentException.class )
  public void constructorWithNullAsScrollableType() {
    new TypeToAdapterMapping<>( null, TableAdapter.class );
  }

  @Test( expected = IllegalArgumentException.class )
  public void constructorWithNullAsAdapterType() {
    new TypeToAdapterMapping<>( Table.class, null );
  }

  @Test
  @SuppressWarnings("rawtypes")
  public void tryFindTypeToAdapterMapping() {
    Shell shell = createShell( displayHelper );
    Control control = new ScrolledComposite( shell, SWT.H_SCROLL | SWT.V_SCROLL );

    Optional<TypeToAdapterMapping<? extends Scrollable, ? extends Adapter>> actual
      = TypeToAdapterMapping.tryFindTypeToAdapterMapping( control );

    assertThat( actual ).isPresent();
  }

  @Test
  @SuppressWarnings("rawtypes")
  public void tryFindTypeToAdapterMappingOfDerivedType() {
    Shell shell = createShell( displayHelper );
    Control control = new ScrolledComposite( shell, SWT.H_SCROLL | SWT.V_SCROLL ) {};

    Optional<TypeToAdapterMapping<? extends Scrollable, ? extends Adapter>> actual
    = TypeToAdapterMapping.tryFindTypeToAdapterMapping( control );

    assertThat( actual ).isPresent();
  }

  @Test
  @SuppressWarnings("rawtypes")
  public void tryFindTypeToAdapterMappingOfUnsupportedType() {
    Shell shell = createShell( displayHelper );
    Control control = new Label( shell, SWT.NONE );

    Optional<TypeToAdapterMapping<? extends Scrollable, ? extends Adapter>> actual
    = TypeToAdapterMapping.tryFindTypeToAdapterMapping( control );

    assertThat( actual ).isNotPresent();
  }
}