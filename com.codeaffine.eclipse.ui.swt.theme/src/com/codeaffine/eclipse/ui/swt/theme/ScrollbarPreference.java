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

import java.util.function.BiConsumer;
import java.util.function.Supplier;

import org.eclipse.jface.preference.IPreferenceStore;

import com.codeaffine.eclipse.swt.widget.scrollable.ScrollbarStyle;

public class ScrollbarPreference {

  private final Supplier<IPreferenceStore> preferenceStoreSupplier;
  private final String name;

  ScrollbarPreference( String name ) {
    this( name, () -> Activator.getInstance().getPreferenceStore() );
  }

  ScrollbarPreference( String name, Supplier<IPreferenceStore> preferenceStoreSupplier ) {
    this.preferenceStoreSupplier = preferenceStoreSupplier;
    this.name = name;
  }

  public void apply( ScrollbarStyle result, BiConsumer<ScrollbarStyle, ScrollbarPreference> consumer ) {
    if( isCustomized() ) {
      consumer.accept( result, this );
    }
  }

  public boolean isCustomized() {
    return !getValue().equals( preferenceStoreSupplier.get().getDefaultString( name ) );
  }

  public String getValue() {
    return preferenceStoreSupplier.get().getString( name );
  }
}