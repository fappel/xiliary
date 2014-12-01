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