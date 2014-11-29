package com.codeaffine.eclipse.swt.widget.scrollable;

import static com.codeaffine.eclipse.swt.test.util.ShellHelper.createShell;
import static com.codeaffine.eclipse.swt.widget.scrollable.TreeHelper.createTree;
import static com.codeaffine.eclipse.swt.widget.scrollable.TreeHelper.expandRootLevelItems;
import static com.codeaffine.eclipse.swt.widget.scrollable.TreeHelper.expandTopBranch;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.codeaffine.eclipse.swt.test.util.DisplayHelper;
import com.codeaffine.eclipse.swt.widget.scrollable.TreeLayoutFactory.TreeLayoutContextFactory;

@RunWith( Parameterized.class )
public class VisibilityTest {

  @Parameters
  public static Collection<Object[]> data() {
    Collection<Object[]> result = new ArrayList<Object[]>();
    result.add( new Object[] { SWT.VERTICAL } );
    result.add( new Object[] { SWT.HORIZONTAL } );
    return result;
  }

  @Rule
  public final DisplayHelper displayHelper = new DisplayHelper();

  private final int orientation;

  private Visibility visibility;
  private Shell shell;
  private Tree tree;

  public VisibilityTest( int orientation ) {
    this.orientation = orientation;
  }

  @Before
  public void setUp() {
    shell = createShell( displayHelper, SWT.RESIZE );
    tree = createTree( shell, 2, 4 );
    shell.open();
    visibility = createVisibility();
  }

  @Test
  public void isVisible() {
    boolean actual = visibility.isVisible();

    assertThat( actual ).isFalse();
  }

  @Test
  public void isVisibleOnScrollBarStateChangeWithoutUpdate() {
    shell.setSize( 200, 100 );
    expandRootLevelItems( tree );
    expandTopBranch( tree );

    boolean actual = visibility.isVisible();

    assertThat( actual ).isFalse();
  }

  @Test
  public void isVisibleOnScrollBarStateChangeAndUpdate() {
    shell.setSize( 200, 100 );
    expandRootLevelItems( tree );
    expandTopBranch( tree );
    visibility.update();

    boolean actual = visibility.isVisible();

    assertThat( actual ).isTrue();
  }

  @Test
  public void hasChanged() {
    boolean actual = visibility.hasChanged();

    assertThat( actual ).isFalse();
  }

  @Test
  public void hasChangedOnScrollBarStateChange() {
    shell.setSize( 200, 100 );
    expandRootLevelItems( tree );
    expandTopBranch( tree );

    boolean actual = visibility.hasChanged();

    assertThat( actual ).isTrue();
  }

  @Test
  public void hasChangedOnScrollBarStateChangeAndUpdate() {
    shell.setSize( 200, 100 );
    expandRootLevelItems( tree );
    expandTopBranch( tree );

    visibility.update();

    boolean actual = visibility.hasChanged();

    assertThat( actual ).isFalse();
  }

  private Visibility createVisibility() {
    TreeLayoutContextFactory factory = new TreeLayoutContextFactory( tree );
    Visibility result = new Visibility( tree.getHorizontalBar(), factory );
    if( ( orientation & SWT.VERTICAL ) > 0  ) {
      result = new Visibility( tree.getVerticalBar(), factory );
    }
    return result;
  }
}