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

import static com.codeaffine.eclipse.ui.swt.theme.AttributeApplicator.attach;

import org.eclipse.swt.widgets.Scrollable;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;

import com.codeaffine.eclipse.swt.widget.scrollable.ScrollableAdapterFactory;
import com.codeaffine.eclipse.swt.widget.scrollable.ScrollbarStyle;
import com.codeaffine.eclipse.swt.widget.scrollable.TreeAdapter;

class ApplicatorTestHelper {

  private final ScrollableAdapterFactory factory;
  private final Scrollable scrollable;

  ApplicatorTestHelper( Scrollable scrollable ) {
    this.factory = new ScrollableAdapterFactory();
    this.scrollable = scrollable;
  }

  ScrollableAdapterFactory getFactory() {
    return factory;
  }

  void reparentScrollableOnChildShell() {
    Shell childShell = new Shell( scrollable.getShell() );
    scrollable.setParent( childShell );
  }

  ScrollbarStyle adapt() {
    ScrollbarStyle result = factory.create( ( Tree )scrollable, TreeAdapter.class ).get();
    attach( scrollable, result );
    return result;
  }
}