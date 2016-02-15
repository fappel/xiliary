package com.codeaffine.eclipse.swt.widget.scrollable.context;

import static com.codeaffine.eclipse.swt.test.util.SWTEventHelper.trigger;
import static com.codeaffine.eclipse.swt.test.util.ShellHelper.createShell;
import static com.codeaffine.eclipse.swt.test.util.graphics.PointAssert.assertThat;
import static com.codeaffine.eclipse.swt.widget.scrollable.TreeHelper.createTree;
import static com.codeaffine.eclipse.swt.widget.scrollable.TreeHelper.expandTopBranch;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Scrollable;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.eclipse.swt.test.util.DisplayHelper;
import com.codeaffine.eclipse.swt.test.util.SWTIgnoreConditions.GtkPlatform;
import com.codeaffine.eclipse.swt.widget.scrollable.StyledTextHelper;
import com.codeaffine.test.util.junit.ConditionalIgnoreRule;
import com.codeaffine.test.util.junit.ConditionalIgnoreRule.ConditionalIgnore;

public class PreferredSizeProviderTest {

  @Rule public final ConditionalIgnoreRule conditionalIgnoreRule = new ConditionalIgnoreRule();
  @Rule public final DisplayHelper displayHelper = new DisplayHelper();

  private Shell shell;

  @Before
  public void setUp() {
    shell = createShell( displayHelper );
  }

  @Test
  public void getSizeOnExpand() {
    Tree scrollable = createTree( shell, 4, 6 );
    PreferredSizeProvider provider = newPreferredSizeProvider( scrollable );

    Point first = provider.getSize();
    triggerExpansion( scrollable );
    Point second = provider.getSize();

    assertThat( provider.useLastCalculation() ).isTrue();
    assertThat( first ).isNotEqualTo( second );
  }

  @Test
  @ConditionalIgnore( condition = GtkPlatform.class )
  public void getSizeOnCollapse() {
    Tree scrollable = createTree( shell, 4, 6 );
    triggerExpansion( scrollable );
    PreferredSizeProvider provider = newPreferredSizeProvider( scrollable );

    Point first = provider.getSize();
    triggerCollapse( scrollable );
    Point second = provider.getSize();

    assertThat( provider.useLastCalculation() ).isTrue();
    assertThat( first ).isNotEqualTo( second );
  }

  @Test
  public void getSizeOnModify() {
    StyledText scrollable = StyledTextHelper.createStyledText( shell );
    PreferredSizeProvider provider = newPreferredSizeProvider( scrollable );

    Point first = provider.getSize();
    scrollable.setText( "some text" );
    Point second = provider.getSize();

    assertThat( provider.useLastCalculation() ).isTrue();
    assertThat( first ).isNotEqualTo( second );
  }

  @Test
  public void getSizeRecurrently() {
    Tree scrollable = createTree( shell, 2, 3 );
    PreferredSizeProvider provider = newPreferredSizeProvider( scrollable );

    Point first = provider.getSize();
    Point second = provider.getSize();

    assertThat( provider.useLastCalculation() ).isTrue();
    assertThat( first ).isEqualTo( scrollable.computeSize( SWT.DEFAULT, SWT.DEFAULT, true ) );
    assertThat( first ).isEqualTo( second );
  }

  @Test
  public void getSizeOnOwnerDrawnAndVirtual() {
    Tree scrollable = createTree( shell, 2, 3, SWT.VIRTUAL );
    scrollable.addListener( SWT.MeasureItem, evt -> {} );
    PreferredSizeProvider provider = newPreferredSizeProvider( scrollable );

    Point actual = provider.getSize();

    assertThat( provider.useLastCalculation() ).isFalse();
    assertThat( actual ).isEqualTo( scrollable.computeSize( SWT.DEFAULT, SWT.DEFAULT, true ) );
  }

  @Test
  public void useLastCalculationOnMove() {
    Tree scrollable = createTree( shell, 4, 6 );
    PreferredSizeProvider provider = newPreferredSizeProvider( scrollable );

    provider.getSize();
    boolean before = provider.useLastCalculation();
    scrollable.setLocation( 10, 20 );
    boolean after = provider.useLastCalculation();

    assertThat( before ).isTrue();
    assertThat( after ).isFalse();
  }

  @Test
  public void useLastCalculationOnResize() {
    Tree scrollable = createTree( shell, 4, 6 );
    PreferredSizeProvider provider = newPreferredSizeProvider( scrollable );

    provider.getSize();
    boolean before = provider.useLastCalculation();
    scrollable.setSize( 100, 200 );
    boolean after = provider.useLastCalculation();

    assertThat( before ).isTrue();
    assertThat( after ).isFalse();
  }

  private static void triggerExpansion( Tree tree ) {
    expandTopBranch( tree );
    trigger( SWT.Expand ).on( tree );
  }

  private static void triggerCollapse( Tree tree ) {
    Stream.of( tree.getItems() ).forEach( item -> item.setExpanded( false ) );
    trigger( SWT.Collapse ).on( tree );
  }

  private static PreferredSizeProvider newPreferredSizeProvider( Scrollable scrollable ) {
    return new PreferredSizeProvider( new ScrollableControl<Scrollable>( scrollable ) );
  }
}