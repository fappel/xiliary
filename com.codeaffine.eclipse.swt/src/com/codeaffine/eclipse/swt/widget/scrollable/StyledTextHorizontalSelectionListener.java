package com.codeaffine.eclipse.swt.widget.scrollable;

import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

import com.codeaffine.eclipse.swt.widget.scrollbar.FlatScrollBar;

class StyledTextHorizontalSelectionListener extends SelectionAdapter {

  private final StyledText styledText;

  StyledTextHorizontalSelectionListener( StyledText styledText ) {
    this.styledText = styledText;
  }

  @Override
  public void widgetSelected( SelectionEvent event ) {
    int selection = ( ( FlatScrollBar )event.widget ).getSelection();
    styledText.setHorizontalPixel( selection );
  }
}