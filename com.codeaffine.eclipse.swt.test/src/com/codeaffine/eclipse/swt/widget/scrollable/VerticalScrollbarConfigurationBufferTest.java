package com.codeaffine.eclipse.swt.widget.scrollable;

import static org.assertj.core.api.Assertions.assertThat;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.eclipse.swt.test.util.DisplayHelper;
import com.codeaffine.eclipse.swt.test.util.ShellHelper;

public class VerticalScrollbarConfigurationBufferTest {

  @Rule
  public final DisplayHelper displayHelper = new DisplayHelper();

  @Test
  public void hasChanged() {
    VerticalScrollbarConfigurationBuffer buffer = createBufferWithScrollable( SWT.V_SCROLL );

    boolean actual = buffer.hasChanged();

    assertThat( actual ).isTrue();
  }

  @Test
  public void hasChangedOnIncrementChange() {
    Composite scrollable = createScrollable( SWT.V_SCROLL );
    VerticalScrollbarConfigurationBuffer buffer = createBuffer( scrollable );
    scrollable.getVerticalBar().setIncrement( 44 );

    boolean actual = buffer.hasChanged();

    assertThat( actual ).isTrue();
  }

  @Test
  public void hasChangedOnMaximumChange() {
    Composite scrollable = createScrollable( SWT.V_SCROLL );
    VerticalScrollbarConfigurationBuffer buffer = createBuffer( scrollable );
    scrollable.getVerticalBar().setMaximum( 55 );

    boolean actual = buffer.hasChanged();

    assertThat( actual ).isTrue();
  }

  @Test
  public void hasChangedOnPageIncrementChange() {
    Composite scrollable = createScrollable( SWT.V_SCROLL );
    VerticalScrollbarConfigurationBuffer buffer = createBuffer( scrollable );
    scrollable.getVerticalBar().setPageIncrement( 11 );

    boolean actual = buffer.hasChanged();

    assertThat( actual ).isTrue();
  }

  @Test
  public void hasChangedOnThumbChange() {
    Composite scrollable = createScrollable( SWT.V_SCROLL );
    VerticalScrollbarConfigurationBuffer buffer = createBuffer( scrollable );
    scrollable.getVerticalBar().setThumb( 77 );

    boolean actual = buffer.hasChanged();

    assertThat( actual ).isTrue();
  }

  @Test
  public void hasChangedOnSelectionChange() {
    Composite scrollable = createScrollable( SWT.V_SCROLL );
    VerticalScrollbarConfigurationBuffer buffer = createBuffer( scrollable );
    scrollable.getVerticalBar().setSelection( 10 );

    boolean actual = buffer.hasChanged();

    assertThat( actual ).isTrue();
  }

  @Test
  public void hasChangedIfNoScrollbarExists() {
    VerticalScrollbarConfigurationBuffer buffer = createBufferWithScrollable( SWT.NONE );

    boolean actual = buffer.hasChanged();

    assertThat( actual ).isTrue();
  }

  private VerticalScrollbarConfigurationBuffer createBufferWithScrollable( int scrollableStyle ) {
    return createBuffer( createScrollable( scrollableStyle ) );
  }

  private Composite createScrollable( int scrollableStyle ) {
    Shell shell = ShellHelper.createShell( displayHelper );
    return new Composite( shell, scrollableStyle );
  }

  private static VerticalScrollbarConfigurationBuffer createBuffer( Composite scrollable ) {
    return new VerticalScrollbarConfigurationBuffer( scrollable );
  }
}
