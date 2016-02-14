package com.codeaffine.eclipse.swt.widget.scrollable;

import static com.codeaffine.eclipse.swt.testhelper.LoremIpsum.PARAGRAPHS;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Composite;

public class StyledTextHelper {

  public static StyledText createStyledText( Composite parent ) {
    StyledText result = new StyledText( parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL );
    result.setText( PARAGRAPHS );
    return result;
  }
}