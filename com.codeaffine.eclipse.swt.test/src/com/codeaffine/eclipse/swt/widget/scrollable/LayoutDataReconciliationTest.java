package com.codeaffine.eclipse.swt.widget.scrollable;

import static com.codeaffine.eclipse.swt.test.util.ShellHelper.createShell;
import static com.codeaffine.eclipse.swt.widget.scrollable.TreeHelper.createTree;
import static org.assertj.core.api.Assertions.assertThat;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.eclipse.swt.test.util.DisplayHelper;

public class LayoutDataReconciliationTest {

  @Rule
  public final DisplayHelper displayHelper = new DisplayHelper();

  private LayoutDataReconciliation reconciliation;
  private Composite adapter;
  private Tree scrollable;
  private Shell shell;


  @Before
  public void setUp() {
    shell = createShell( displayHelper );
    adapter = new Composite( shell, SWT.NONE );
    scrollable = createTree( shell, 1, 1 );
    reconciliation = new LayoutDataReconciliation( adapter, scrollable );
  }

  @Test
  public void run() {
    Rectangle expectedBounds = adapter.getBounds();
    RowData expectedData = new RowData();
    setLayoutAndLayoutData( expectedData, new RowLayout() );

    reconciliation.run();

    assertThat( adapter.getLayoutData() ).isSameAs( expectedData );
    assertThat( adapter.getBounds() ).isNotEqualTo( expectedBounds );
  }

  @Test
  public void runIfDataHaveNotChanged() {
    Rectangle expectedBounds = new Rectangle( 10, 20, 30, 40 );
    RowData expectedData = new RowData();
    setLayoutAndLayoutData( expectedData, new RowLayout() );
    reconciliation.run();
    adapter.setBounds( expectedBounds );

    reconciliation.run();

    assertThat( adapter.getLayoutData() ).isSameAs( expectedData );
    assertThat( adapter.getBounds() ).isEqualTo( expectedBounds );
  }

  private void setLayoutAndLayoutData( RowData expectedData, RowLayout layout ) {
    shell.setLayout( layout );
    scrollable.setLayoutData( expectedData );
  }
}