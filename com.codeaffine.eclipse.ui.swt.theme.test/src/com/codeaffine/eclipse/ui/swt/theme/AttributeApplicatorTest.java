package com.codeaffine.eclipse.ui.swt.theme;

import static com.codeaffine.eclipse.ui.swt.theme.AttributeKey.colorKey;
import static com.codeaffine.eclipse.ui.swt.theme.ScrollableAdapterContribution.ADAPTER_BACKGROUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.util.function.BiConsumer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Tree;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.eclipse.swt.test.util.DisplayHelper;
import com.codeaffine.eclipse.swt.test.util.SWTIgnoreConditions.GtkPlatform;
import com.codeaffine.eclipse.swt.widget.scrollable.ScrollableAdapterFactory;
import com.codeaffine.eclipse.swt.widget.scrollable.ScrollbarStyle;
import com.codeaffine.eclipse.swt.widget.scrollable.TreeAdapter;
import com.codeaffine.test.util.junit.ConditionalIgnoreRule;
import com.codeaffine.test.util.junit.ConditionalIgnoreRule.ConditionalIgnore;

public class AttributeApplicatorTest {

  @Rule public final ConditionalIgnoreRule conditionalIgnoreRule = new ConditionalIgnoreRule();
  @Rule public final DisplayHelper displayHelper = new DisplayHelper();

  private AttributeApplicator applicator;
  private ScrollbarStyle adapterStyle;
  private Tree scrollable;

  @Before
  public void setUp() {
    scrollable = new Tree( displayHelper.createShell(), SWT.NONE );
    ScrollableAdapterFactory factory = new ScrollableAdapterFactory();
    adapterStyle = factory.create( scrollable, TreeAdapter.class );
    applicator = new AttributeApplicator( factory );
  }

  @Test
  @ConditionalIgnore( condition = GtkPlatform.class )
  public void canApply() {
    AttributeApplicator.attach( scrollable, adapterStyle );

    boolean actual = applicator.canApply( scrollable );

    assertThat( actual ).isTrue();
  }

  @Test
  @ConditionalIgnore( condition = GtkPlatform.class )
  public void canApplyWithoutStyleAttached() {
    boolean actual = applicator.canApply( scrollable );

    assertThat( actual ).isFalse();
  }

  @Test
  @ConditionalIgnore( condition = GtkPlatform.class )
  public void canApplyOfUnadaptedScrollable() {
    Tree unadapted = new Tree( displayHelper.createShell(), SWT.NONE );
    AttributeApplicator.attach( unadapted, adapterStyle );

    boolean actual = applicator.canApply( unadapted );

    assertThat( actual ).isFalse();
  }

  @Test
  @ConditionalIgnore( condition = GtkPlatform.class )
  public void apply() {
    AttributeApplicator.attach( scrollable, adapterStyle );

    applicator.apply( scrollable, ( style, color ) -> style.setBackgroundColor( color ), () -> expectedColor() );

    assertThat( adapterStyle.getBackgroundColor() ).isEqualTo( expectedColor() );
  }

  @Test
  @ConditionalIgnore( condition = GtkPlatform.class )
  public void applyFromBuffer() {
    AttributePreserver preserver = new AttributePreserver( scrollable );
    preserver.put( colorKey( ADAPTER_BACKGROUND ), expectedColor() );
    AttributeApplicator.attach( scrollable, adapterStyle );
    BiConsumer<ScrollbarStyle, Color> attributeSetter = ( style, color ) -> style.setBackgroundColor( color );

    applicator.applyFromBuffer( scrollable, colorKey( ADAPTER_BACKGROUND ), attributeSetter );

    assertThat( adapterStyle.getBackgroundColor() ).isEqualTo( expectedColor() );
  }

  @Test
  @ConditionalIgnore( condition = GtkPlatform.class )
  public void applyFromBufferIfNoValueWasPreserved() {
    AttributeApplicator.attach( scrollable, adapterStyle );
    BiConsumer<ScrollbarStyle, Color> attributeSetter = ( style, color ) -> style.setBackgroundColor( color );

    applicator.applyFromBuffer( scrollable, colorKey( ADAPTER_BACKGROUND ), attributeSetter );

    assertThat( adapterStyle.getBackgroundColor() ).isNotEqualTo( expectedColor() );
  }

  @Test
  @SuppressWarnings("unchecked")
  @ConditionalIgnore( condition = GtkPlatform.class )
  public void applyFromBufferIfNotAdapted() {
    Tree unadapted = new Tree( displayHelper.createShell(), SWT.NONE );
    AttributePreserver preserver = new AttributePreserver( unadapted );
    preserver.put( colorKey( ADAPTER_BACKGROUND ), expectedColor() );
    BiConsumer<ScrollbarStyle, Color> attributeSetter = mock( BiConsumer.class );

    applicator.applyFromBuffer( unadapted, colorKey( ADAPTER_BACKGROUND ), attributeSetter );

    verify( attributeSetter, never() ).accept( any( ScrollbarStyle.class ), any( Color.class ) );
  }

  private Color expectedColor() {
    return displayHelper.getSystemColor( SWT.COLOR_BLACK );
  }
}