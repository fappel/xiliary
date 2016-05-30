package com.codeaffine.eclipse.swt.widget.scrollable;

import static com.codeaffine.eclipse.swt.test.util.ShellHelper.createShell;
import static com.codeaffine.eclipse.swt.widget.scrollable.ScrolledCompositeHelper.createScrolledComposite;
import static com.codeaffine.eclipse.swt.widget.scrollbar.FlatScrollBarAssert.assertThat;
import static java.util.Arrays.asList;

import java.util.Collection;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Shell;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import com.codeaffine.eclipse.swt.test.util.DisplayHelper;
import com.codeaffine.eclipse.swt.widget.scrollbar.FlatScrollBar;

@RunWith( Parameterized.class )
public class ScrolledCompositeScrollBarUpdaterTest {

  private static final int SELECTION = 100;

  @Rule
  public final DisplayHelper displayHelper = new DisplayHelper();

  @Parameter
  public int orientation;

  private ScrolledCompositeScrollBarUpdater updater;
  private ScrolledComposite scrolledComposite;
  private FlatScrollBar flatScrollBar;

  @Parameters
  public static Collection<Integer> data() {
    return asList( SWT.HORIZONTAL, SWT.VERTICAL );
  }

  @Before
  public void setUp() {
    Shell shell = createShell( displayHelper );
    scrolledComposite = createScrolledComposite( shell );
    flatScrollBar = createFlatScrollBar( shell );
    updater = new ScrolledCompositeScrollBarUpdater( scrolledComposite, flatScrollBar, orientation );
    shell.open();
  }

  @Test
  public void update() {
    scrolledComposite.setOrigin( SELECTION, SELECTION );

    updater.update();

    assertThat( flatScrollBar )
      .hasIncrement( getScrollBar().getIncrement() )
      .hasMaximum( getScrollBar().getMaximum() )
      .hasMinimum( getScrollBar().getMinimum() )
      .hasPageIncrement( getScrollBar().getPageIncrement() )
      .hasThumb( getScrollBar().getThumb() )
      .hasSelection( SELECTION );
  }

  private ScrollBar getScrollBar() {
    if( orientation == SWT.HORIZONTAL ) {
      return scrolledComposite.getHorizontalBar();
    }
    return scrolledComposite.getVerticalBar();
  }

  private FlatScrollBar createFlatScrollBar( Shell shell ) {
    FlatScrollBar result = new FlatScrollBar( shell, orientation );
    result.setIncrement( 2 );
    result.setMaximum( 20 );
    result.setMinimum( 11 );
    result.setPageIncrement( 3 );
    result.setThumb( 5 );
    return result;
  }
}