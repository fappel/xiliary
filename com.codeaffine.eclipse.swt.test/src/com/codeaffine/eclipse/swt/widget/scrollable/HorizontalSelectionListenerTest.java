package com.codeaffine.eclipse.swt.widget.scrollable;

import static com.codeaffine.eclipse.swt.test.util.ShellHelper.createShell;
import static com.codeaffine.eclipse.swt.widget.scrollable.SelectionEventHelper.createEvent;
import static com.codeaffine.eclipse.swt.widget.scrollable.TreeHelper.createTree;
import static org.assertj.core.api.Assertions.assertThat;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.eclipse.swt.test.util.DisplayHelper;

public class HorizontalSelectionListenerTest {

  private static final int SELECTION = 5;

  @Rule
  public final DisplayHelper displayHelper = new DisplayHelper();

  private Shell shell;
  private Tree tree;

  @Before
  public void setUp() {
    shell = createShell( displayHelper );
    tree = createTree( shell, 6, 4 );
    shell.open();
  }

  @Test
  public void selectionChangedIfAdapterIsParent() {
    LayoutContext<Tree> context = new LayoutContext<Tree>( shell, tree );
    HorizontalSelectionListener listener = new HorizontalSelectionListener( context );
    Point location = tree.getLocation();

    listener.widgetSelected( createEvent( shell, SELECTION ) );

    assertThat( tree.getLocation().x ).isEqualTo( location.x - SELECTION );
    assertThat( tree.getLocation().y ).isEqualTo( location.y );
  }

  @Test
  public void selectionChangedIfAdapterIsNotParent() {
    Composite adapter = new Composite( shell, SWT.NONE );
    reparentTree( adapter );
    provideShellWithLayoutMargin();
    LayoutContext<Tree> context = new LayoutContext<Tree>( adapter, tree );
    HorizontalSelectionListener listener = new HorizontalSelectionListener( context );
    Point location = tree.getLocation();

    listener.widgetSelected( createEvent( shell, SELECTION ) );

    assertThat( tree.getLocation().x ).isEqualTo( location.x - SELECTION );
    assertThat( tree.getLocation().y ).isEqualTo( location.y );
  }

  private void reparentTree( Composite adapter ) {
    tree.setParent( adapter );
    new ControlReflectionUtil().setField( tree, "parent", shell );
  }

  private void provideShellWithLayoutMargin() {
    FillLayout layout = new FillLayout( SWT.VERTICAL );
    layout.marginHeight = 10;
    layout.marginWidth = 10;
    shell.setLayout( layout );
    shell.layout();
  }
}