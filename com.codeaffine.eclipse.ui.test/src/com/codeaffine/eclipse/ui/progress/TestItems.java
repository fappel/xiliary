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
package com.codeaffine.eclipse.ui.progress;

import java.util.ArrayList;
import java.util.Collection;

class TestItems {

  static final int CHILD_COUNT = 20;

  static TestItem cast( Object element ) {
    return ( TestItem )element;
  }

  static TestItem populateTestItemTree() {
    TestItem result = new TestItem( null, "root" );
    Collection<TestItem> populateTestItemTree = populateTestItemTree( null );
    result.getChildren().addAll( populateTestItemTree );
    return result;
  }

  private static Collection<TestItem> populateTestItemTree( TestItem parent ) {
    ArrayList<TestItem> result = new ArrayList<TestItem>();
    for( int i = 0; i < CHILD_COUNT; i++ ) {
      TestItem item = new TestItem( parent, "item_" + i );
      result.add( item );
      if( isFirstLevel( parent ) ) {
        item.getChildren().addAll( populateTestItemTree( item ) );
      }
    }
    return result;
  }

  private static boolean isFirstLevel( TestItem parent ) {
    return parent == null;
  }
}