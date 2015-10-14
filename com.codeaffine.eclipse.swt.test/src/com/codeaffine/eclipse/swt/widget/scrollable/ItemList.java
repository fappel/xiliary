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
      items.add(  "Item " + i );
    }
  }

  String[] getItems() {
    return items.toArray( new String[ items.size() ] );
  }
}