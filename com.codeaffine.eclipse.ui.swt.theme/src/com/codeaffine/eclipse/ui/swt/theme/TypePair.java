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

import org.eclipse.swt.widgets.Scrollable;

import com.codeaffine.eclipse.swt.widget.scrollable.ScrollableAdapterFactory.Adapter;

class TypePair <S extends Scrollable, A extends Scrollable & Adapter<S>> {

  final Class<S> scrollableType;
  final Class<A> adapterType;

  TypePair( Class<S> scrollableType, Class<A> adapterType ) {
    verifyNotNull( scrollableType, "scrollableType" );
    verifyNotNull( adapterType, "adapterType" );

    this.scrollableType = scrollableType;
    this.adapterType = adapterType;
  }
}