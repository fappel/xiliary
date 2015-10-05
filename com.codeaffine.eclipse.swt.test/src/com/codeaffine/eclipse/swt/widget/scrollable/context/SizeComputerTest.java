package com.codeaffine.eclipse.swt.widget.scrollable.context;

import static com.codeaffine.eclipse.swt.test.util.ShellHelper.createShell;
import static com.codeaffine.eclipse.swt.widget.scrollable.TreeHelper.createTree;
import static com.codeaffine.eclipse.swt.widget.scrollable.TreeHelper.expandTopBranch;
import static org.assertj.core.api.Assertions.assertThat;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Scrollable;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.eclipse.swt.test.util.DisplayHelper;
import com.codeaffine.eclipse.swt.util.ControlReflectionUtil;

public class SizeComputerTest {

  @Rule public final DisplayHelper displayHelper = new DisplayHelper();

  private SizeComputer computer;
  private Composite adapter;
  private Tree scrollable;
  private Shell shell;

  @Before
  public void setUp() {
    shell = createShell( displayHelper );
    adapter = createAdapter( shell );
    scrollable = createTree( adapter, 6, 4 );
    computer = new SizeComputer( scrollable, adapter );
    shell.layout();
    shell.open();
  }

  @Test
  public void getPreferredSizeIfSmallerThanAdapterArea() {
    Point actual = computer.getPreferredSize();

    assertThat( actual ).isEqualTo( expectedSizeWithClientAreaWidth() );
  }

  @Test
  public void getPreferredSizeIfLargerThanAdapterArea() {
    expandTopBranch( scrollable );

    Point actual = computer.getPreferredSize();

    assertThat( actual ).isEqualTo( expectedSize() );
  }

  @Test
  public void preferredSizeGetsBuffered() {
    Point expected = computer.getPreferredSize();

    expandTopBranch( scrollable );
    Point actual = computer.getPreferredSize();

    assertThat( actual ).isEqualTo( expected );
  }

  @Test
  public void adjustPreferredWidthIfHorizontalBarIsNotVisible() {
    computer.adjustPreferredWidthIfHorizontalBarIsVisible();
    Point actual = computer.getPreferredSize();

    assertThat( actual ).isEqualTo( expectedSizeWithClientAreaWidth() );
  }

  @Test
  public void adjustPreferredWidthIfHorizontalBarIsVisible() {
    expandTopBranch( scrollable );

    computer.adjustPreferredWidthIfHorizontalBarIsVisible();
    Point actual = computer.getPreferredSize();

    assertThat( actual ).isEqualTo( expectedSize() );
  }

  @Test
  public void adjustPreferredWidthIfHorizontalBarIsVisibleIfReparented() {
    reparentScrollable( shell, scrollable );
    expandTopBranch( scrollable );

    computer.adjustPreferredWidthIfHorizontalBarIsVisible();
    Point actual = computer.getPreferredSize();

    assertThat( actual ).isEqualTo( expectedSizeWithPreferredWidthAdjustment() );
  }

  @Test
  public void updatePreferredSize() {
    Point expected = computer.getPreferredSize();
    expandTopBranch( scrollable );

    computer.updatePreferredSize();
    Point actual = computer.getPreferredSize();

    assertThat( actual ).isNotEqualTo( expected );
  }

  private static Composite createAdapter( Composite parent ) {
    Composite result = new Composite( parent, SWT.NONE );
    result.setLayout( new FillLayout() );
    return result;
  }

  private Point expectedSizeWithClientAreaWidth() {
    return new Point( adapter.getClientArea().width, computePreferredScrollableSize().y );
  }

  private Point expectedSize() {
    return new Point( computePreferredScrollableSize().x, computePreferredScrollableSize().y );
  }

  private Point expectedSizeWithPreferredWidthAdjustment() {
    return new Point( ( computePreferredScrollableSize().x ) * 2, computePreferredScrollableSize().y );
  }

  private Point computePreferredScrollableSize() {
    return scrollable.computeSize( SWT.DEFAULT, SWT.DEFAULT, true );
  }

  private static void reparentScrollable( Composite parent, Scrollable scrollable ) {
    new ControlReflectionUtil().setField( scrollable, "parent", parent );
  }
}