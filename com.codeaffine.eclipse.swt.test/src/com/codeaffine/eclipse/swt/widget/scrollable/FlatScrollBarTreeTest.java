package com.codeaffine.eclipse.swt.widget.scrollable;

import static com.codeaffine.eclipse.swt.test.util.ShellHelper.createShell;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyVararg;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.eclipse.swt.test.util.DisplayHelper;
import com.codeaffine.eclipse.swt.util.Platform;
import com.codeaffine.eclipse.swt.util.Platform.PlatformType;

public class FlatScrollBarTreeTest {

  @Rule
  public final DisplayHelper displayHelper = new DisplayHelper();

  private TestTreeFactory testTreeFactory;
  private Platform platform;
  private Shell shell;

  @Before
  public void setUp() {
    shell = createShell( displayHelper, SWT.RESIZE );
    platform = mock( Platform.class );
    testTreeFactory = new TestTreeFactory();
    shell.open();
  }

  @Test
  public void getTree() {
    FlatScrollBarTree adapter = createTreeAdapter();

    Tree actual = adapter.getTree();

    assertThat( actual ).isSameAs( testTreeFactory.getTree() );
  }

  @Test
  public void getLayoutIfPlatformMatches() {
    stubPlatformToMatchAnyType( platform );
    FlatScrollBarTree adapter = createTreeAdapter();

    Layout actual = adapter.getLayout();

    assertThat( actual ).isExactlyInstanceOf( ScrollableLayout.class );
  }

  @Test
  public void getLayoutIfPlatformDoesNotMatch() {
    FlatScrollBarTree adapter = createTreeAdapter();

    Layout actual = adapter.getLayout();

    assertThat( actual ).isExactlyInstanceOf( FillLayout.class );
  }

  @SuppressWarnings("unchecked")
  private FlatScrollBarTree createTreeAdapter() {
    return new FlatScrollBarTree( shell, platform, testTreeFactory, FlatScrollBarTree.createLayoutMapping() );
  }

  private static void stubPlatformToMatchAnyType( Platform platform ) {
    when( platform.matchesOneOf( ( PlatformType[] )anyVararg() ) ).thenReturn( true );
  }
}