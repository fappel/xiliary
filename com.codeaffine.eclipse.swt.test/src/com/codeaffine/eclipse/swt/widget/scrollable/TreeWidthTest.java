package com.codeaffine.eclipse.swt.widget.scrollable;

import static com.codeaffine.eclipse.swt.testhelper.ShellHelper.createShellWithoutLayout;
import static com.codeaffine.eclipse.swt.widget.scrollable.TreeHelper.createTree;
import static com.codeaffine.eclipse.swt.widget.scrollable.TreeHelper.expandRootLevelItems;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.eclipse.swt.test.util.DisplayHelper;

public class TreeWidthTest {

  @Rule
  public final DisplayHelper displayHelper = new DisplayHelper();

  private PreferredWidthComputer preferredWidthComputer;
  private TreeWidth treeWidth;
  private Shell shell;
  private Tree tree;

  @Before
  public void setUp() {
    shell = createShellWithoutLayout( displayHelper, SWT.RESIZE );
    tree = createTree( shell, 6, 4 );
    preferredWidthComputer = mock( PreferredWidthComputer.class );
    treeWidth = new TreeWidth( preferredWidthComputer, tree );
    shell.open();
  }

  @Test
  public void preferredWidthExceedsVisibleRange() {
    equipPreferredComputerWith( getVisbleRangeWidth() + getVerticalBarOffset() );

    boolean actual = treeWidth.hasScrollEffectingChange();

    assertThat( actual ).isTrue();
  }

  @Test
  public void preferredWidthExceedsVisibleRangeWhenVerticalScrollBarIsVisible() {
    expandRootLevelItems( tree );
    equipPreferredComputerWith( getVisbleRangeWidth() + getVerticalBarOffset() );

    boolean actual = treeWidth.hasScrollEffectingChange();

    assertThat( actual ).isFalse();
  }

  @Test
  public void preferredWidthDeclinesBackIntoVisibleRange() {
    setTreeWidth( getVisbleRangeWidth() + 100 );
    treeWidth.update();
    equipPreferredComputerWith( getVisbleRangeWidth() - 100 );

    boolean actual = treeWidth.hasScrollEffectingChange();

    assertThat( actual ).isTrue();
  }

  @Test
  public void noDeclineIfPreferredWidthIsGreaterBufferedWidth() {
    setTreeWidth( getVisbleRangeWidth() - 100 );
    treeWidth.update();
    equipPreferredComputerWith( getVisbleRangeWidth() - 50 );

    boolean actual = treeWidth.hasScrollEffectingChange();

    assertThat( actual ).isFalse();
  }

  @Test
  public void noDeclineIfTreeHeightIsEqualToVisibleRangeHeight() {
    tree.setSize( getVisbleRangeWidth() + 100 , getVisibleRangeHeight() );
    treeWidth.update();
    equipPreferredComputerWith( getVisbleRangeWidth() - 100 );

    boolean actual = treeWidth.hasScrollEffectingChange();

    assertThat( actual ).isFalse();
  }

  @Test
  public void widthHasNotChanged() {
    boolean actual = treeWidth.hasScrollEffectingChange();

    assertThat( actual ).isFalse();
  }

  private int getVisbleRangeWidth() {
    return shell.getClientArea().width;
  }

  private int getVisibleRangeHeight() {
    return shell.getClientArea().height;
  }

  private int getVerticalBarOffset() {
    return new TreeLayoutContext( tree ).getVerticalBarOffset();
  }

  private void equipPreferredComputerWith( int preferredWidth ) {
    when( preferredWidthComputer.compute() ).thenReturn( preferredWidth );
  }

  private void setTreeWidth( int expectedWidth ) {
    tree.setSize( expectedWidth, 0 );
  }
}