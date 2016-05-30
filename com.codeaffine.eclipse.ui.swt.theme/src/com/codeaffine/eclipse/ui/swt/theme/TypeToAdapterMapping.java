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
package com.codeaffine.eclipse.ui.swt.theme;

import static com.codeaffine.eclipse.swt.util.ArgumentVerification.verifyNotNull;

import java.util.Optional;
import java.util.stream.Stream;

import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Scrollable;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Tree;

import com.codeaffine.eclipse.swt.widget.scrollable.ScrollableAdapterFactory.Adapter;
import com.codeaffine.eclipse.swt.widget.scrollable.ScrolledCompositeAdapter;
import com.codeaffine.eclipse.swt.widget.scrollable.StyledTextAdapter;
import com.codeaffine.eclipse.swt.widget.scrollable.TableAdapter;
import com.codeaffine.eclipse.swt.widget.scrollable.TreeAdapter;

class TypeToAdapterMapping <S extends Scrollable, A extends Scrollable & Adapter<S>> {

  @SuppressWarnings( { "unchecked", "rawtypes" } )
  static final TypeToAdapterMapping<? extends Scrollable, ? extends Adapter>[] SUPPORTED_MAPPINGS
    = new TypeToAdapterMapping[]
  {
    new TypeToAdapterMapping<>( Tree.class, TreeAdapter.class ),
    new TypeToAdapterMapping<>( Table.class, TableAdapter.class ),
    new TypeToAdapterMapping<>( StyledText.class, StyledTextAdapter.class ),
    new TypeToAdapterMapping<>( ScrolledComposite.class, ScrolledCompositeAdapter.class ),
  };

  final Class<S> scrollableType;
  final Class<A> adapterType;

  TypeToAdapterMapping( Class<S> scrollableType, Class<A> adapterType ) {
    verifyNotNull( scrollableType, "scrollableType" );
    verifyNotNull( adapterType, "adapterType" );

    this.scrollableType = scrollableType;
    this.adapterType = adapterType;
  }

  @SuppressWarnings("rawtypes")
  static Optional<TypeToAdapterMapping<? extends Scrollable, ? extends Adapter>>
    tryFindTypeToAdapterMapping( Control control )
  {
    return Stream.of( SUPPORTED_MAPPINGS )
        .filter( typePair -> isDerivedFrom( control, typePair.scrollableType ) )
        .findFirst();
  }

  private static boolean isDerivedFrom( Control control, Class<? extends Scrollable> scrollableType ) {
    Class<?> type = control.getClass();
    boolean result = scrollableType == type;
    while( !result && type.getSuperclass() != null ) {
      type = type.getSuperclass();
      result = scrollableType == type;
    }
    return result;
  }
}