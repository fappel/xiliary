package com.codeaffine.eclipse.swt.widget.scrollbar;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.eclipse.swt.widgets.Shell;

class OverlayHelper {

  static Overlay stubOverlay( Shell shell ) {
    Overlay result = mock( Overlay.class );
    when( result.getControl() ).thenReturn( shell );
    return result;
  }
}