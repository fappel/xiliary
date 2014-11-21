package com.codeaffine.eclipse.swt.widget.scrollbar;

import static com.codeaffine.eclipse.swt.widget.scrollbar.FlatScrollBarHelper.stubScrollBar;
import static org.mockito.Mockito.verify;

import org.eclipse.swt.SWT;
import org.junit.Test;

public class IncrementerTest {

  private static final int SELECTION = 3;
  private static final int INCREMENT = 1;

  @Test
  public void run() {
    FlatScrollBar scrollBar = stubScrollBar( SELECTION, INCREMENT );
    Incrementer incrementer = new Incrementer( scrollBar );

    incrementer.run();

    verify( scrollBar ).setSelectionInternal( INCREMENT + SELECTION, SWT.ARROW_DOWN );
  }
}