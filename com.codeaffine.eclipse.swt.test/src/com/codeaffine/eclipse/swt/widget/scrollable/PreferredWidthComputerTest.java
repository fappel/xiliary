package com.codeaffine.eclipse.swt.widget.scrollable;

import static com.codeaffine.eclipse.swt.test.util.ShellHelper.createShell;
import static com.codeaffine.eclipse.swt.widget.scrollable.TreeHelper.createTree;
import static com.codeaffine.eclipse.swt.widget.scrollable.TreeHelper.expandRootLevelItems;
import static org.assertj.core.api.Assertions.assertThat;

import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.eclipse.swt.test.util.DisplayHelper;
import com.codeaffine.eclipse.swt.widget.scrollable.TreeLayoutFactory.TreeLayoutContextFactory;

public class PreferredWidthComputerTest {

  @Rule public final DisplayHelper displayHelper = new DisplayHelper();

  private PreferredWidthComputer computer;
  private Tree tree;

  @Before
  public void setUp() {
    Shell shell = createShell( displayHelper );
    tree = createTree( shell, 6, 4 );
    computer = new PreferredWidthComputer( new TreeLayoutContextFactory( tree ) );
    shell.open();
  }

  @Test
  public void compute() {
    int actual = computer.compute();

    assertThat( actual ).isEqualTo( preferredWidth() );
  }

  @Test
  public void computeIfVerticalScrollBarVisible() {
    expandRootLevelItems( tree );

    int actual = computer.compute();

    assertThat( actual ).isEqualTo( overlayAdjustment() );
  }

  private int preferredWidth() {
    LayoutContext context = new LayoutContext( tree, tree.getItemHeight() );
    return context.getPreferredSize().x + context.getOffset() * 2;
  }

  private int overlayAdjustment() {
    return preferredWidth() + new LayoutContext( tree, tree.getItemHeight() ).getVerticalBarOffset();
  }
}