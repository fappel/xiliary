package com.codeaffine.eclipse.swt.widget.scrollable;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Listener;

class DisposalRouting {

  void register( Composite adapter, Control adaptable ) {
    Listener adaptableDisposeListener = evt -> handleDispose( adapter, adaptable );
    adaptable.addListener( SWT.Dispose, adaptableDisposeListener );
    adapter.addListener( SWT.Dispose, evt -> adaptable.removeListener( SWT.Dispose, adaptableDisposeListener ) );
  }

  private static void handleDispose( Composite adapter, Control adaptable ) {
    adaptable.setParent( adapter.getParent() );
    adapter.dispose();
  }
}