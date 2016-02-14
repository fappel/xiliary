package com.codeaffine.eclipse.swt.widget.scrollable;

import static com.codeaffine.eclipse.swt.test.util.ShellHelper.createShell;
import static com.codeaffine.eclipse.swt.testhelper.LoremIpsum.PARAGRAPHS;
import static com.codeaffine.eclipse.swt.widget.scrollable.SelectionEventHelper.createEvent;
import static org.assertj.core.api.Assertions.assertThat;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionListener;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.eclipse.swt.test.util.DisplayHelper;

public class StyledTextHorizontalSelectionListenerTest {

  private static final int SELECTION = 20;

  @Rule
  public final DisplayHelper displayHelper = new DisplayHelper();

  @Test
  public void widgetSelected() {
    StyledText styledText = createStyledText();
    SelectionListener listener = new StyledTextHorizontalSelectionListener( styledText );

    listener.widgetSelected( createEvent( createShell( displayHelper ), SELECTION ) );

    assertThat( styledText.getHorizontalPixel() ).isEqualTo( SELECTION );
  }

  private StyledText createStyledText() {
    StyledText result = new StyledText( createShell( displayHelper ), SWT.V_SCROLL | SWT.H_SCROLL );
    result.setText( PARAGRAPHS );
    return result;
  }
}