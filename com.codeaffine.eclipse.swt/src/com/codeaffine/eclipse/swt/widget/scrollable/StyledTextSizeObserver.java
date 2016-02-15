package com.codeaffine.eclipse.swt.widget.scrollable;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;

import com.codeaffine.eclipse.swt.widget.scrollable.context.AdaptionContext;

class StyledTextSizeObserver implements SizeObserver {

  private final StyledText styledText;

  private boolean mustLayoutAdapter;

  public StyledTextSizeObserver( StyledText styledText ) {
    this.styledText = styledText;
    this.styledText.addListener( SWT.Resize, evt -> mustLayoutAdapter = true );
  }

  @Override
  public boolean mustLayoutAdapter( AdaptionContext<?> context ) {
    boolean result = mustLayoutAdapter;
    mustLayoutAdapter = false;
    return result;
  }

  @Override
  public void update( AdaptionContext<?> context ) {
  }
}