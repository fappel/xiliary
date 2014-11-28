package com.codeaffine.eclipse.swt.widget.scrollable;

import static com.codeaffine.eclipse.swt.testhelper.ShellHelper.createShell;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyVararg;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.eclipse.swt.test.util.DisplayHelper;
import com.codeaffine.eclipse.swt.widget.scrollable.Platform.PlatformType;

public class FlatScrollBarTableTest {

  @Rule
  public final DisplayHelper displayHelper = new DisplayHelper();

  private TestTableFactory testTableFactory;
  private Platform platform;
  private Shell shell;

  @Before
  public void setUp() {
    shell = createShell( displayHelper, SWT.RESIZE );
    platform = mock( Platform.class );
    testTableFactory = new TestTableFactory();
    shell.open();
  }

  @Test
  public void getTree() {
    FlatScrollBarTable adapter = createTableAdapter();

    Table actual = adapter.getTable();

    assertThat( actual ).isSameAs( testTableFactory.getTable() );
  }

  @Test
  public void getLayoutIfPlatformMatches() {
    stubPlatformToMatchAnyType( platform );
    FlatScrollBarTable adapter = createTableAdapter();

    Layout actual = adapter.getLayout();

    assertThat( actual ).isExactlyInstanceOf( ScrollableLayout.class );
  }

  @Test
  public void getLayoutIfPlatformDoesNotMatch() {
    FlatScrollBarTable adapter = createTableAdapter();

    Layout actual = adapter.getLayout();

    assertThat( actual ).isExactlyInstanceOf( FillLayout.class );
  }

  @SuppressWarnings("unchecked")
  private FlatScrollBarTable createTableAdapter() {
    return new FlatScrollBarTable( shell, platform, testTableFactory, FlatScrollBarTable.createLayoutMapping() );
  }

  private static void stubPlatformToMatchAnyType( Platform platform ) {
    when( platform.matchesOneOf( ( PlatformType[] )anyVararg() ) ).thenReturn( true );
  }
}