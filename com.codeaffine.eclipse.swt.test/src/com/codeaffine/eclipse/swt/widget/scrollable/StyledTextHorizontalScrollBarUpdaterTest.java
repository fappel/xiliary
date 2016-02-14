package com.codeaffine.eclipse.swt.widget.scrollable;

import static com.codeaffine.eclipse.swt.test.util.ShellHelper.createShell;
import static com.codeaffine.eclipse.swt.testhelper.LoremIpsum.PARAGRAPHS;
import static com.codeaffine.eclipse.swt.widget.scrollbar.FlatScrollBarAssert.assertThat;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Shell;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.eclipse.swt.test.util.DisplayHelper;
import com.codeaffine.eclipse.swt.widget.scrollbar.FlatScrollBar;

public class StyledTextHorizontalScrollBarUpdaterTest {

  @Rule
  public final DisplayHelper displayHelper = new DisplayHelper();

  @Test
  public void update() {
    Shell shell = createShell( displayHelper );
    StyledText styledText = new StyledText( shell, SWT.H_SCROLL | SWT.V_SCROLL );
    styledText.setText( PARAGRAPHS );
    FlatScrollBar scrollBar = new FlatScrollBar( shell, SWT.HORIZONTAL );
    ScrollBarUpdater updater = new StyledTextHorizontalScrollBarUpdater( styledText, scrollBar );
    shell.layout();

    updater.update();

    assertThat( scrollBar )
      .hasThumb( 78 )
      .hasPageIncrement( 78 )
      .hasSelection( 0 )
      .hasMaximum( 550 )
      .hasMinimum( 0 )
      .hasIncrement( 8 );
  }
}