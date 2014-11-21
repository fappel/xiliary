package com.codeaffine.eclipse.swt.widget.scrollbar;

import static com.codeaffine.eclipse.swt.widget.scrollbar.FlatScrollBarHelper.stubScrollBar;
import static org.mockito.Mockito.verify;

import org.eclipse.swt.SWT;
import org.junit.Test;

public class DecrementerTest {

  private static final int SELECTION = 3;
  private static final int INCREMENT = 1;

  @Test
  public void run() {
    FlatScrollBar scrollBar = stubScrollBar( SELECTION, INCREMENT );
    Decrementer decrementer = new Decrementer( scrollBar );

    decrementer.run();

    verify( scrollBar ).setSelectionInternal( SELECTION - INCREMENT, SWT.ARROW_UP );
  }
}