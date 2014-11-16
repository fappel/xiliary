package com.codeaffine.eclipse.swt.widget.scrollable;

import static com.codeaffine.eclipse.swt.testhelper.ShellHelper.createShell;
import static com.codeaffine.eclipse.swt.widget.scrollable.FlatScrollBarTree.BAR_BREADTH;
import static com.codeaffine.eclipse.swt.widget.scrollable.TreeHelper.createTree;
import static com.codeaffine.eclipse.swt.widget.scrollable.TreeHelper.expandRootLevelItems;
import static org.assertj.core.api.Assertions.assertThat;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.eclipse.swt.test.util.DisplayHelper;

public class PreferredWidthComputerTest {

  @Rule
  public final DisplayHelper displayHelper = new DisplayHelper();

  private Tree tree;
  private PreferredWidthComputer computer;

  @Before
  public void setUp() {
    Shell shell = createShell( displayHelper );
    tree = createTree( shell, 6, 4 );
    computer = new PreferredWidthComputer( tree );
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
    return tree.computeSize( SWT.DEFAULT, SWT.DEFAULT, true ).x;
  }

  private int overlayAdjustment() {
    return preferredWidth() + tree.getVerticalBar().getSize().x - BAR_BREADTH;
  }

}