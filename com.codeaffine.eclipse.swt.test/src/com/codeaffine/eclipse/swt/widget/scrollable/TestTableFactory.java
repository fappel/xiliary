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

import static com.codeaffine.eclipse.swt.widget.scrollable.TableHelper.createTable;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;

class TestTableFactory implements ScrollableFactory<Table> {

  private Table table;

  @Override
  public Table create( Composite parent ) {
    table = createTable( parent, 20 );
    return table;
  }

  Table getTable() {
    return table;
  }
}