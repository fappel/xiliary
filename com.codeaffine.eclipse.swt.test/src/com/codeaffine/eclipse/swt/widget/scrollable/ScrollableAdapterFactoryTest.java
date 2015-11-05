package com.codeaffine.eclipse.swt.widget.scrollable;

import static com.codeaffine.eclipse.swt.test.util.ShellHelper.createShell;
import static com.codeaffine.eclipse.swt.widget.scrollable.TreeHelper.createTree;
import static com.codeaffine.test.util.lang.ThrowableCaptor.thrownBy;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Scrollable;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.eclipse.swt.test.util.DisplayHelper;
import com.codeaffine.eclipse.swt.test.util.SWTIgnoreConditions.GtkPlatform;
import com.codeaffine.eclipse.swt.util.Platform;
import com.codeaffine.eclipse.swt.util.Platform.PlatformType;
import com.codeaffine.eclipse.swt.widget.scrollable.context.AdaptionContext;
import com.codeaffine.eclipse.swt.widget.scrollable.context.ScrollableControl;
import com.codeaffine.test.util.junit.ConditionalIgnoreRule;
import com.codeaffine.test.util.junit.ConditionalIgnoreRule.ConditionalIgnore;

public class ScrollableAdapterFactoryTest {

  @Rule public final ConditionalIgnoreRule conditionalIgnoreRule = new ConditionalIgnoreRule();
  @Rule public final DisplayHelper displayHelper = new DisplayHelper();

  static class UnsupportedTreeAdapter extends TreeAdapter {};

  private ScrollableAdapterFactory factory;
  private Shell shell;

  @Before
  public void setUp() {
    shell = createShell( displayHelper );
    factory = new ScrollableAdapterFactory();
  }

  @Test
  @ConditionalIgnore( condition = GtkPlatform.class )
  public void create() {
    Tree tree = createTree( shell, 1, 1 );

    TreeAdapter actual = factory.create( tree, TreeAdapter.class );

    assertThat( actual.getParent() ).isSameAs( shell );
    assertThat( tree.getParent() ).isSameAs( shell );
    assertThat( actual.getChildren() ).contains( tree );
  }

  @Test
  @ConditionalIgnore( condition = GtkPlatform.class )
  public void createForTreeWithBorder() {
    Tree tree = new Tree( shell, SWT.BORDER );

    TreeAdapter actual = factory.create( tree, TreeAdapter.class );

    assertThat( actual.getStyle() & SWT.BORDER ).isEqualTo( SWT.BORDER );
  }

  @Test
  @ConditionalIgnore( condition = GtkPlatform.class )
  public void createKeepsDrawingOrder() {
    createChild();
    Tree tree = createTree( shell, 1, 1 );
    createChild();

    TreeAdapter actual = factory.create( tree, TreeAdapter.class );
    assertThat( actual  ).isSameAs( shell.getChildren()[ 1 ] );
  }

  @Test
  public void createWithUnsupportedType() {
    Throwable actual = thrownBy( () -> factory.create( createTree( shell, 1, 1 ), UnsupportedTreeAdapter.class ) );

    assertThat( actual )
      .hasMessageContaining( UnsupportedTreeAdapter.class.getName() )
      .isInstanceOf( IllegalArgumentException.class );
  }

  @Test
  @SuppressWarnings("unchecked")
  public void createLayoutFactory() {
    RowLayout expected = new RowLayout();
    Platform platform = stubPlatformWithSupportedType( PlatformType.GTK );
    LayoutMapping<Scrollable> mapping = createLayoutMapping( expected, PlatformType.GTK );

    LayoutFactory<Scrollable> factory = ScrollableAdapterFactory.createLayoutFactory( platform, mapping );
    Layout actual = factory.create( createLayoutContext() );

    assertThat( actual ).isSameAs( expected );
  }

  @Test
  @SuppressWarnings("unchecked")
  public void createLayoutFactoryWithUnsupportedType() {
    Platform platform = mock( Platform.class );
    LayoutMapping<Scrollable> mapping = createLayoutMapping( new RowLayout(), PlatformType.GTK );

    LayoutFactory<Scrollable> factory = ScrollableAdapterFactory.createLayoutFactory( platform, mapping );
    Layout actual = factory.create( createLayoutContext() );

    assertThat( actual ).isInstanceOf( NativeLayoutFactory.LAYOUT_TYPE );
  }

  private static Platform stubPlatformWithSupportedType( PlatformType type ) {
    Platform result = mock( Platform.class );
    when( result.matchesOneOf( type ) ).thenReturn( true );
    return result;
  }

  private static LayoutMapping<Scrollable> createLayoutMapping( Layout expected, PlatformType ... types ) {
    return new LayoutMapping<Scrollable>( new TestLayoutFactory( expected ), types );
  }

  private Label createChild() {
    return new Label( shell, SWT.NONE );
  }

  private AdaptionContext<Scrollable> createLayoutContext() {
    return new AdaptionContext<>( shell, new ScrollableControl<>( createTree( shell, 1, 1 ) ) );
  }
}