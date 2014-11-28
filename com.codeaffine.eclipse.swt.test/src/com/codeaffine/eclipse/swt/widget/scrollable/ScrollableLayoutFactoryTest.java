package com.codeaffine.eclipse.swt.widget.scrollable;

import static org.assertj.core.api.Assertions.assertThat;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Scrollable;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.eclipse.swt.test.util.DisplayHelper;
import com.codeaffine.eclipse.swt.testhelper.ShellHelper;

public class ScrollableLayoutFactoryTest {

  @Rule
  public final DisplayHelper displayHelper = new DisplayHelper();

  private ScrollableLayoutFactorySpy factorySpy;
  private Scrollable scrollable;
  private Layout layout;
  private Shell shell;

  @Before
  public void setUp() {
    shell = ShellHelper.createShell( displayHelper );
    scrollable = new Text( shell, SWT.MULTI );
    factorySpy = new ScrollableLayoutFactorySpy();
    layout = factorySpy.create( shell, scrollable );
  }

  @Test
  public void horizontalScrollBarInitialization() {
    factorySpy.getHorizontal().notifyListeners( SWT.NONE );

    assertThat( factorySpy.getSelectionEvent().widget )
      .isNotNull()
      .isSameAs( factorySpy.getHorizontal() );
  }

  @Test
  public void verticalScrollBarInitialization() {
    factorySpy.getVertical().notifyListeners( SWT.NONE );

    assertThat( factorySpy.getSelectionEvent().widget )
      .isNotNull()
      .isSameAs( factorySpy.getVertical() );
  }

  @Test
  public void watchDogInitialization() {
    shell.dispose();

    assertThat( factorySpy.getDisposeEvent().widget )
      .isNotNull()
      .isSameAs( shell );
  }

  @Test
  public void createCompositeT() {
    assertThat( layout ).isSameAs( factorySpy.getLayout() );
  }
}