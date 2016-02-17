package com.codeaffine.eclipse.swt.widget.scrollable;

import static com.codeaffine.eclipse.swt.test.util.ShellHelper.createShell;
import static com.codeaffine.eclipse.swt.widget.scrollable.TableHelper.createTable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.widgets.Scrollable;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.eclipse.swt.test.util.DisplayHelper;
import com.codeaffine.eclipse.swt.test.util.SWTIgnoreConditions.GtkPlatform;
import com.codeaffine.eclipse.swt.widget.scrollable.context.ScrollableControl;
import com.codeaffine.test.util.junit.ConditionalIgnoreRule;
import com.codeaffine.test.util.junit.ConditionalIgnoreRule.ConditionalIgnore;

public class ItemRedrawInsuranceTest {

  @Rule public final ConditionalIgnoreRule conditionalIgnore = new ConditionalIgnoreRule();
  @Rule public final DisplayHelper displayHelper = new DisplayHelper();

  private Shell shell;
  private ItemRedrawInsurance itemRedrawInsurance;

  @Before
  public void setUp() {
    shell = createShell( displayHelper );
    itemRedrawInsurance = new ItemRedrawInsurance();
  }

  @Test
  public void register() {
    Scrollable scrollable = createTable( shell, 10, SWT.VIRTUAL );
    scrollable.addListener( SWT.MeasureItem, evt -> {} );

    itemRedrawInsurance.register( new ScrollableControl<>( scrollable ) );

    assertThat( scrollable.getVerticalBar().getListeners( SWT.Selection ) ).hasSize( 1 );
  }

  @Test
  public void registerOnNativeDrawnScrollable() {
    Scrollable scrollable = createTable( shell, 10, SWT.VIRTUAL );

    itemRedrawInsurance.register( new ScrollableControl<>( scrollable ) );

    assertThat( scrollable.getVerticalBar().getListeners( SWT.Selection ) ).isEmpty();
  }

  @Test
  public void registerOnNonVirtualScrollable() {
    Scrollable scrollable = createTable( shell, 10 );
    scrollable.addListener( SWT.MeasureItem, evt -> {} );

    itemRedrawInsurance.register( new ScrollableControl<>( scrollable ) );

    assertThat( scrollable.getVerticalBar().getListeners( SWT.Selection ) ).isEmpty();
  }

  @Test
  public void registerOnScrollableWithoutVerticalScrollbar() {
    Scrollable scrollable = createTable( shell, 10, SWT.VIRTUAL | SWT.NO_SCROLL );
    scrollable.addListener( SWT.MeasureItem, evt -> {} );

    itemRedrawInsurance.register( new ScrollableControl<>( scrollable ) );

    assertThat( scrollable.getVerticalBar() ).isNull();
  }

  @Test
  @ConditionalIgnore( condition = GtkPlatform.class )
  public void verticalScrollBarSelectionChange() {
    Table scrollable = createTable( shell, 100, SWT.VIRTUAL );
    scrollable.addListener( SWT.MeasureItem, evt -> {} );
    itemRedrawInsurance.register( new ScrollableControl<>( scrollable ) );
    PaintListener listener = mock( PaintListener.class );
    shell.open();

    scrollable.addPaintListener( listener );
    scrollable.setTopIndex( 50 );
    DisplayHelper.flushPendingEvents();

    verify( listener ).paintControl( any( PaintEvent.class ) );
  }
}