package com.codeaffine.eclipse.swt.widget.scrollable;

import static com.codeaffine.eclipse.swt.test.util.ShellHelper.createShell;
import static com.codeaffine.eclipse.swt.widget.scrollable.TreeHelper.createTree;
import static org.assertj.core.api.Assertions.assertThat;

import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.eclipse.swt.test.util.DisplayHelper;

public class LayoutReconciliationTest {

  @Rule
  public final DisplayHelper displayHelper = new DisplayHelper();

  private LayoutReconciliation reconciliation;
  private TreeAdapter adapter;
  private Tree scrollable;
  private Shell parent;


  @Before
  public void setUp() {
    parent = createShell( displayHelper );
    scrollable = createTree( parent, 1, 1 );
    adapter = new ScrollableAdapterFactory().create( scrollable, TreeAdapter.class );
    reconciliation = new LayoutReconciliation( adapter, scrollable );
  }

  @Test
  public void run() {
    Rectangle expected = new Rectangle( 10, 20, 30, 40 );
    adapter.setBounds( expected );

    reconciliation.run();
    Rectangle actual = adapter.getBounds();

    assertThat( actual ).isEqualTo( expected );
  }

  @Test
  public void runWithStackLayout() {
    Rectangle expected = new Rectangle( 10, 20, 30, 40 );
    adapter.setBounds( expected );
    parent.setLayout( createStackLayout( scrollable ) );

    reconciliation.run();
    Rectangle actual = adapter.getBounds();

    assertThat( actual ).isNotEqualTo( expected );
  }

  private static StackLayout createStackLayout( Tree topControl ) {
    StackLayout result = new StackLayout();
    result.topControl = topControl;
    return result;
  }
}