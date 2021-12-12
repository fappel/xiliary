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
package com.codeaffine.eclipse.swt.widget.scrollable;

import static com.codeaffine.eclipse.swt.widget.scrollable.ScrolledCompositeHelper.createScrolledComposite;

import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.widgets.Composite;

class TestScrolledCompositeFactory implements ScrollableFactory<ScrolledComposite> {

  private ScrolledComposite scrolledComposite;

  @Override
  public ScrolledComposite create( Composite parent ) {
    scrolledComposite = createScrolledComposite( parent );
    return scrolledComposite;
  }

  ScrolledComposite getTable() {
    return scrolledComposite;
  }
}