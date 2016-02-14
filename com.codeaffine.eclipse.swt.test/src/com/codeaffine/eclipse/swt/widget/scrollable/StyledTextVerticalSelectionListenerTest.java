package com.codeaffine.eclipse.swt.widget.scrollable;

import static com.codeaffine.eclipse.swt.test.util.ShellHelper.createShell;
import static com.codeaffine.eclipse.swt.widget.scrollable.ScrollBarUpdater.SELECTION_RASTER_SMOOTH_FACTOR;
import static com.codeaffine.eclipse.swt.widget.scrollable.SelectionEventHelper.createEvent;
import static com.codeaffine.eclipse.swt.widget.scrollable.StyledTextHelper.createStyledText;
import static org.assertj.core.api.Assertions.assertThat;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Shell;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.eclipse.swt.test.util.DisplayHelper;
import com.codeaffine.eclipse.swt.widget.scrollbar.FlatScrollBar;

public class StyledTextVerticalSelectionListenerTest {

  private static final int LINE_INDEX = 2;

  @Rule
  public final DisplayHelper displayHelper = new DisplayHelper();

  @Test
  public void widgetSelected() {
    Shell shell = createShell( displayHelper );
    StyledText styledText = createStyledText( shell );
    FlatScrollBar scrollBar = prepareScrollBar( shell, styledText );
    SelectionListener listener = new StyledTextVerticalSelectionListener( styledText );

    listener.widgetSelected( createEvent( scrollBar, LINE_INDEX * SELECTION_RASTER_SMOOTH_FACTOR ) );

    assertThat( styledText.getTopIndex() ).isEqualTo( LINE_INDEX );
  }

  private static FlatScrollBar prepareScrollBar( Shell shell, StyledText styledText ) {
    FlatScrollBar result = new FlatScrollBar( shell, SWT.VERTICAL );
    new StyledTextVerticalScrollBarUpdater( styledText, result ).update();
    return result;
  }
}