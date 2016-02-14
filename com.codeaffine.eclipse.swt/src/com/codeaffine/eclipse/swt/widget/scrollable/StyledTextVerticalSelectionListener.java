package com.codeaffine.eclipse.swt.widget.scrollable;

import static com.codeaffine.eclipse.swt.widget.scrollable.ScrollBarUpdater.SELECTION_RASTER_SMOOTH_FACTOR;

import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

import com.codeaffine.eclipse.swt.widget.scrollbar.FlatScrollBar;

public class StyledTextVerticalSelectionListener extends SelectionAdapter {

  private final StyledText styledText;

  StyledTextVerticalSelectionListener( StyledText styledText ) {
    this.styledText = styledText;
  }

  @Override
  public void widgetSelected( SelectionEvent event ) {
    int selection = ( ( FlatScrollBar )event.widget ).getSelection();
    styledText.setTopIndex( selection / SELECTION_RASTER_SMOOTH_FACTOR );
  }
}