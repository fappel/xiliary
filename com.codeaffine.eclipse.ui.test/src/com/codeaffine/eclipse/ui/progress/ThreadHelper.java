package com.codeaffine.eclipse.ui.progress;

class ThreadHelper {

  static void sleep( int millis ) {
    try {
      Thread.sleep( millis );
    } catch( InterruptedException shouldNotHappen ) {
      throw new IllegalStateException( shouldNotHappen );
    }
  }
}