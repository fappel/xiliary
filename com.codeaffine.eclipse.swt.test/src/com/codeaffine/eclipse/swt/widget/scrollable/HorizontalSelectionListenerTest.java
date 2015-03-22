package com.codeaffine.eclipse.swt.widget.scrollable;

import static com.codeaffine.eclipse.swt.test.util.ShellHelper.createShell;
import static com.codeaffine.eclipse.swt.widget.scrollable.SelectionEventHelper.createEvent;
import static com.codeaffine.eclipse.swt.widget.scrollable.TreeHelper.createTree;
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
  public void selectionChanged() {
    LayoutContext<Tree> context = new LayoutContext<Tree>( shell, tree );
    HorizontalSelectionListener listener = new HorizontalSelectionListener( context );
    Point adapterLocation = shell.getLocation();
    Point location = tree.getLocation();

    listener.widgetSelected( createEvent( shell, SELECTION ) );

    assertThat( tree.getLocation().x ).isEqualTo( -SELECTION );
    assertThat( tree.getLocation().y ).isEqualTo( location.y );
    assertThat( shell.getLocation() ).isEqualTo( adapterLocation );
  }

  @Test
  public void selectionChangedIfHeaderVisible() {
    tree.setHeaderVisible( true );
    LayoutContext<Tree> context = new LayoutContext<Tree>( shell, tree );
    HorizontalSelectionListener listener = new HorizontalSelectionListener( context );
    Point adapterLocation = shell.getLocation();
    Point location = tree.getLocation();

    listener.widgetSelected( createEvent( shell, SELECTION ) );

    assertThat( tree.getLocation().x ).isEqualTo( -SELECTION );
    assertThat( tree.getLocation().y ).isEqualTo( location.y );
    assertThat( shell.getLocation() ).isEqualTo( adapterLocation );
  }

  @Test
  public void selectionChangedWithReparentedAdapter() {
    Composite adapter = reparentScrollable( new Composite( shell, SWT.NONE ), tree );
    equipShellWithLayoutMargin();
    LayoutContext<Tree> context = new LayoutContext<Tree>( adapter, tree );
    HorizontalSelectionListener listener = new HorizontalSelectionListener( context );
    Point adapterLocation = shell.getLocation();
    Point location = tree.getLocation();

    listener.widgetSelected( createEvent( shell, SELECTION ) );

    assertThat( tree.getLocation().x ).isEqualTo( location.x - SELECTION );
    assertThat( tree.getLocation().y ).isEqualTo( location.y );
    assertThat( shell.getLocation() ).isEqualTo( adapterLocation );
  }

  @Test
  public void selectionChangedWithHeaderVisibleWithReparentedAdapter() {
    tree.setHeaderVisible( true );
    Composite adapter = reparentScrollable( new Composite( shell, SWT.NONE ), tree );
    equipShellWithLayoutMargin();
    LayoutContext<Tree> context = new LayoutContext<Tree>( adapter, tree );
    HorizontalSelectionListener listener = new HorizontalSelectionListener( context );
    Point adapterLocation = shell.getLocation();
    Point location = tree.getLocation();

    listener.widgetSelected( createEvent( shell, SELECTION ) );

    assertThat( tree.getLocation().x ).isEqualTo( location.x - SELECTION );
    assertThat( tree.getLocation().y ).isEqualTo( location.y );
    assertThat( shell.getLocation() ).isEqualTo( adapterLocation );
  }

  private Composite reparentScrollable( Composite adapter, Scrollable scrollable ) {
    scrollable.setParent( adapter );
    new ControlReflectionUtil().setField( scrollable, "parent", shell );
    return adapter;
  }

  private void equipShellWithLayoutMargin() {
    FillLayout layout = new FillLayout( SWT.VERTICAL );
    layout.marginHeight = 10;
    layout.marginWidth = 10;
    shell.setLayout( layout );
    shell.layout();
  }
}