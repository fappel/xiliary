package com.codeaffine.eclipse.swt.widget.scrollable;

import static com.codeaffine.eclipse.swt.widget.scrollable.StyledTextHelper.createStyledText;

import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Composite;

public class TestStyledTextFactory implements ScrollableFactory<StyledText> {

  private StyledText styledText;

  @Override
  public StyledText create( Composite parent ) {
    styledText = createStyledText( parent );
    return styledText;
  }

  public StyledText getStyledText() {
    return styledText;
  }
}