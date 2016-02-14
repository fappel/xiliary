package com.codeaffine.eclipse.swt.util;

import org.eclipse.swt.widgets.Control;

public class OperationWithRedrawSuspension {

  public void execute( Control control, Runnable runnable ) {
    control.setRedraw( false );
    try {
      runnable.run();
    } finally {
      control.setRedraw( true );
    }
  }
}