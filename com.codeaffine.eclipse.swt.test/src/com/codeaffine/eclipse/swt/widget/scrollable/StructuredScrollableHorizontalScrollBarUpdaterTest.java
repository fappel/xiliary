package com.codeaffine.eclipse.swt.widget.scrollable;

import static com.codeaffine.eclipse.swt.test.util.ShellHelper.createShell;
import static com.codeaffine.eclipse.swt.widget.scrollbar.FlatScrollBarAssert.assertThat;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.eclipse.swt.test.util.DisplayHelper;
import com.codeaffine.eclipse.swt.test.util.SWTIgnoreConditions.GtkPlatform;
import com.codeaffine.eclipse.swt.widget.scrollable.context.AdaptionContext;
import com.codeaffine.eclipse.swt.widget.scrollable.context.ScrollableControl;
import com.codeaffine.eclipse.swt.widget.scrollbar.FlatScrollBar;
import com.codeaffine.test.util.junit.ConditionalIgnoreRule;
import com.codeaffine.test.util.junit.ConditionalIgnoreRule.ConditionalIgnore;

public class StructuredScrollableHorizontalScrollBarUpdaterTest {

  @Rule public final ConditionalIgnoreRule conditionalIgnoreRule = new ConditionalIgnoreRule();
  @Rule public final DisplayHelper displayHelper = new DisplayHelper();

  @Test
  @ConditionalIgnore( condition = GtkPlatform.class )
  public void update() {
    Shell shell = createShell( displayHelper );
    AdaptionContext<?> context = createContext( shell );
    FlatScrollBar scrollBar = new FlatScrollBar( shell, SWT.HORIZONTAL );
    ScrollBarUpdater updater = new StructuredScrollableHorizontalScrollBarUpdater( context, scrollBar );
    shell.layout();

    updater.update();

    assertThat( scrollBar )
      .hasThumb( 99 )
      .hasPageIncrement( 99 )
      .hasSelection( 0 )
      .hasMaximum( 99 )
      .hasMinimum( 0 )
      .hasIncrement( 1 );
  }

  private static AdaptionContext<?> createContext( Shell shell ) {
    Composite adapter = new Composite( shell, SWT.NONE );
    adapter.setLayout( new FillLayout() );
    ScrollableControl<Composite> scrollable = new ScrollableControl<>( new Composite( adapter, SWT.NONE ) );
    return new AdaptionContext<>( adapter, scrollable );
  }
}