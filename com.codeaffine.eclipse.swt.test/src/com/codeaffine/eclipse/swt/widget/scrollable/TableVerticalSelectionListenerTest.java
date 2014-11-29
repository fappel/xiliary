package com.codeaffine.eclipse.swt.widget.scrollable;

import static com.codeaffine.eclipse.swt.test.util.ShellHelper.createShell;
import static com.codeaffine.eclipse.swt.widget.scrollable.SelectionEventHelper.createEvent;
import static com.codeaffine.eclipse.swt.widget.scrollable.TableHelper.createTable;
import static org.assertj.core.api.Assertions.assertThat;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.eclipse.swt.test.util.DisplayHelper;
import com.codeaffine.eclipse.swt.widget.scrollbar.FlatScrollBar;

public class TableVerticalSelectionListenerTest {

  @Rule
  public final DisplayHelper displayHelper = new DisplayHelper();

  @Test
  public void widgetSelected() {
    Shell shell = createShell( displayHelper );
    Table table = createTable( shell, 20 );
    FlatScrollBar scrollBar = prepareScrollBar( shell, table );
    TableVerticalSelectionListener listener = new TableVerticalSelectionListener( table );

    listener.widgetSelected( createEvent( scrollBar, 2 ) );

    assertThat( table.getTopIndex() ).isEqualTo( 2 );
  }

  private static FlatScrollBar prepareScrollBar( Shell shell, Table table ) {
    FlatScrollBar result = new FlatScrollBar( shell, SWT.VERTICAL );
    new TableVerticalScrollBarUpdater( table, result ).update();
    return result;
  }
}