package com.codeaffine.eclipse.swt.widget.scrollable;

import static com.codeaffine.eclipse.swt.testhelper.ShellHelper.createShell;
import static com.codeaffine.eclipse.swt.widget.scrollbar.FlatScrollBarAssert.assertThat;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.eclipse.swt.test.util.DisplayHelper;
import com.codeaffine.eclipse.swt.widget.scrollbar.FlatScrollBar;

public class TableVerticalScrollBarUpdaterTest {

  @Rule public final DisplayHelper displayHelper = new DisplayHelper();

  private TableVerticalScrollBarUpdater updater;
  private FlatScrollBar scrollbar;
  private Shell shell;
  private Table table;

  @Before
  public void setUp() {
    shell = createShell( displayHelper, SWT.RESIZE );
    table = new TestTableFactory().create( shell );
    scrollbar = new FlatScrollBar( shell, SWT.VERTICAL );
    updater = new TableVerticalScrollBarUpdater( table, scrollbar );
    shell.open();
  }

  @Test
  public void update() {
    updater.update();

    assertThat( scrollbar )
      .hasIncrement( 1 )
      .hasPageIncrement( updater.calculateThumb() )
      .hasThumb( updater.calculateThumb() )
      .hasMaximum( table.getItemCount() )
      .hasMinimum( 0 )
      .hasSelection( 0 );
  }

  @Test
  public void updateWithSelection() {
    int expectedSelection = 2;
    table.setTopIndex( expectedSelection );

    updater.update();

    assertThat( scrollbar )
      .hasIncrement( 1 )
      .hasPageIncrement( updater.calculateThumb() )
      .hasThumb( updater.calculateThumb() )
      .hasMaximum( table.getItemCount() )
      .hasMinimum( 0 )
      .hasSelection( expectedSelection );
  }
}