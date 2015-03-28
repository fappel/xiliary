package com.codeaffine.eclipse.swt.widget.scrollable;

import static com.codeaffine.eclipse.swt.test.util.ShellHelper.createShell;
import static com.codeaffine.eclipse.swt.widget.scrollable.TreeHelper.createTree;
import static com.codeaffine.eclipse.swt.widget.scrollable.TreeHelper.expandTopBranch;
import static org.assertj.core.api.Assertions.assertThat;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Scrollable;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.eclipse.swt.test.util.DisplayHelper;


public class HorizontalSelectionComputerTest {

  private static final int SELECTION = 4;

  @Rule
  public final DisplayHelper displayHelper = new DisplayHelper();

  private HorizontalSelectionComputer computer;
  private Tree scrollable;
  private Shell adapter;

  @Before
  public void setUp() {
    adapter = createShell( displayHelper, SWT.H_SCROLL | SWT.V_SCROLL );
    scrollable = createTree( adapter, 6, 4 );
    computer = new HorizontalSelectionComputer();
    adapter.open();
  }

  @Test
  public void compute() {
    LayoutContext<Scrollable> context = new LayoutContext<Scrollable>( adapter, scrollable );

    int actual = computer.compute( context );

    assertThat( actual ).isEqualTo( 0 );
  }

  @Test
  public void computeWithSelection() {
    expandTopBranch( scrollable );
    adapter.getHorizontalBar().setSelection( SELECTION );
    LayoutContext<Scrollable> context = new LayoutContext<Scrollable>( adapter, scrollable );

    int actual = computer.compute( context );

    assertThat( actual ).isEqualTo( 0 );
  }

  @Test
  public void computeWithAdapterReplacementFake() {
    Composite composite = new Composite( scrollable.getParent(), SWT.H_SCROLL | SWT.V_SCROLL );
    LayoutContext<Scrollable> context = new LayoutContext<Scrollable>( composite, scrollable );

    int actual = computer.compute( context );

    assertThat( actual ).isEqualTo( 0 );
  }

  @Test
  public void computeWithAdapterReplacementFakeAndSelection() {
    Composite composite = new Composite( scrollable.getParent(), SWT.H_SCROLL | SWT.V_SCROLL );
    composite.getHorizontalBar().setSelection( SELECTION );
    expandTopBranch( scrollable );
    LayoutContext<Scrollable> context = new LayoutContext<Scrollable>( composite, scrollable );

    int actual = computer.compute( context );

    assertThat( actual ).isEqualTo( SELECTION );
  }

  @Test
  public void computeWithAdapterReplacementFakeAndSelectionOnSmallScrollable() {
    Composite composite = new Composite( scrollable.getParent(), SWT.H_SCROLL | SWT.V_SCROLL );
    composite.getHorizontalBar().setSelection( SELECTION );
    scrollable.setSize( 0, 0 );
    LayoutContext<Scrollable> context = new LayoutContext<Scrollable>( composite, scrollable );

    int actual = computer.compute( context );

    assertThat( actual ).isEqualTo( 0 );
  }
}