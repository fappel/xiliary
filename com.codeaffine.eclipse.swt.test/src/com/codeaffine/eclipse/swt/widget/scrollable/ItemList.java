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

import java.util.ArrayList;
import java.util.List;

class ItemList {

  private static int FETCH_COUNT = 100;

  private final List<String> items;

  ItemList() {
    items = new ArrayList<>();
  }

  void fetchPage() {
    int startIndex = items.size();
    for( int i = startIndex; i < startIndex + FETCH_COUNT; i++ ) {
      items.add(  "Item " + i + " with very important description.");
    }
  }

  String[] getItems() {
    return items.toArray( new String[ items.size() ] );
  }
}