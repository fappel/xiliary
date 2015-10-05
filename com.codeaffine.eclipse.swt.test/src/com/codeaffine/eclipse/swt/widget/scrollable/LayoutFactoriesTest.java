package com.codeaffine.eclipse.swt.widget.scrollable;

import static com.codeaffine.eclipse.swt.test.util.ShellHelper.createShell;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Scrollable;
import org.eclipse.swt.widgets.Shell;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.codeaffine.eclipse.swt.test.util.DisplayHelper;
import com.codeaffine.eclipse.swt.widget.scrollable.context.AdaptionContext;
import com.codeaffine.eclipse.swt.widget.scrollbar.FlatScrollBar;

@RunWith( Parameterized.class )
public class LayoutFactoriesTest {

  @Parameters
  public static Collection<Object[]> data() {
    Collection<Object[]> result = new ArrayList<Object[]>();
    result.add( new Object[] { new TestTreeFactory(), new TreeLayoutFactory() } );
    result.add( new Object[] { new TestTableFactory(), new TableLayoutFactory() } );
    return result;
  }

  @Rule public final DisplayHelper displayHelper = new DisplayHelper();

  private final ScrollableFactory<Scrollable> scrollableFactory;
  private final LayoutFactory<Scrollable> layoutFactory;

  private Composite flatScrollBarControl;
  private Scrollable scrollable;
  private Layout layout;
  private Shell shell;

  public LayoutFactoriesTest(
    ScrollableFactory<Scrollable> scrollableFactory, LayoutFactory<Scrollable> layoutFactory )
  {
    this.scrollableFactory = scrollableFactory;
    this.layoutFactory = layoutFactory;
  }

  @Before
  public void setUp() {
    shell = createShell( displayHelper, SWT.RESIZE );
    flatScrollBarControl = new Composite( shell, SWT.NONE );
    scrollable = scrollableFactory.create( flatScrollBarControl );
    layout = layoutFactory.create( new AdaptionContext<Scrollable>( flatScrollBarControl, scrollable ) );
    shell.open();
  }

  @Test
  public void create() {
    assertThat( layout ).isInstanceOf( ScrollableLayout.class );
  }

  @Test
  public void structureAndDrawingOrder() {
    Control[] children = flatScrollBarControl.getChildren();
    assertThat( children ).hasSize( 4 );
    assertThat( children[ 0 ] ).isExactlyInstanceOf( Label.class );
    assertThat( children[ 1 ] ).isSameAs( scrollable );
    assertThat( children[ 2 ] ).isExactlyInstanceOf( FlatScrollBar.class );
    assertThat( children[ 3 ] ).isExactlyInstanceOf( FlatScrollBar.class );
  }

  @Test
  public void background() {
    Control[] children = flatScrollBarControl.getChildren();
    assertThat( children[ 0 ].getBackground() ).isEqualTo( flatScrollBarControl.getBackground() );
    assertThat( children[ 1 ].getBackground() ).isEqualTo( flatScrollBarControl.getBackground() );
    assertThat( children[ 2 ].getBackground() ).isEqualTo( flatScrollBarControl.getBackground() );
    assertThat( children[ 3 ].getBackground() ).isEqualTo( flatScrollBarControl.getBackground() );
  }

  @Test
  public void dispose() {
    flatScrollBarControl.dispose();

    assertThat( shell.getChildren() ).isEmpty();
  }
}