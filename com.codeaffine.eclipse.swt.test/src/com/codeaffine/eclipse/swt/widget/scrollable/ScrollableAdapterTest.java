package com.codeaffine.eclipse.swt.widget.scrollable;

import static com.codeaffine.eclipse.swt.testhelper.ShellHelper.createShell;
import static com.codeaffine.eclipse.swt.widget.scrollable.PlatformTypeHelper.getUnusedTypes;
import static com.codeaffine.test.util.lang.ThrowableCaptor.thrown;
import static org.assertj.core.api.Assertions.assertThat;

import java.awt.IllegalComponentStateException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Scrollable;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.eclipse.swt.test.util.DisplayHelper;
import com.codeaffine.eclipse.swt.widget.scrollable.Platform.PlatformType;
import com.codeaffine.test.util.lang.ThrowableCaptor.Actor;

@SuppressWarnings("unchecked")
public class ScrollableAdapterTest {

  @Rule
  public final DisplayHelper displayHelper = new DisplayHelper();

  private TestScrollableFactory testScrollableFactory;
  private Platform platform;
  private Shell shell;

  @Before
  public void setUp() {
    shell = createShell( displayHelper );
    platform = new Platform();
    testScrollableFactory = new TestScrollableFactory();
  }

  @Test
  public void getScrollable() {
    ScrollableAdapter<Scrollable >adapter = createAdapter();

    Scrollable actual = adapter.getScrollable();

    assertThat( actual ).isSameAs( testScrollableFactory.getScrollable() );
  }

  @Test
  public void setLayout() {
    final ScrollableAdapter<Scrollable >adapter = createAdapter();

    Throwable actual = thrown( new Actor() {
      @Override
      public void act() throws Throwable {
        adapter.setLayout( null );
      }
    } );

    assertThat( actual )
      .isExactlyInstanceOf( UnsupportedOperationException.class )
      .hasMessageContaining( ScrollableAdapter.class.getName() );
  }

  @Test
  public void scrollableParentRelationDoesNotMatch() {
    final Scrollable scrollableWithWrongParent = new Text( shell, SWT.NONE );
    final ScrollableFactory<Scrollable> factory = new ScrollableFactory<Scrollable>() {
      @Override
      public Scrollable create( Composite parent ) {
        return scrollableWithWrongParent;
      }
    };

    Throwable actual = thrown( new Actor() {
      @Override
      public void act() throws Throwable {
        new ScrollableAdapter<Scrollable>( shell, platform, factory );
      }
    } );

    assertThat( actual ).isInstanceOf( IllegalComponentStateException.class );
  }

  @Test
  public void getLayoutWithoutLayoutMapping() {
    ScrollableAdapter<Scrollable >adapter = createAdapter();

    Layout layout = adapter.getLayout();

    assertThat( layout ).isExactlyInstanceOf( FillLayout.class );
  }

  @Test
  public void getLayoutWithMatchingLayoutMapping() {
    RowLayout expected = new RowLayout();
    LayoutMapping<Scrollable> mapping = createLayoutMapping( expected, PlatformType.values() );
    ScrollableAdapter<Scrollable >adapter = createAdapter( mapping );

    Layout layout = adapter.getLayout();

    assertThat( layout ).isSameAs( expected );
  }

  @Test
  public void getLayoutWithNonMatchingLayoutMapping() {
    LayoutMapping<Scrollable> mapping = createLayoutMapping( new RowLayout(), getUnusedTypes() );
    ScrollableAdapter<Scrollable >adapter = createAdapter( mapping );

    Layout layout = adapter.getLayout();

    assertThat( layout ).isExactlyInstanceOf( FillLayout.class );
  }

  private static LayoutMapping<Scrollable> createLayoutMapping( final Layout expected, PlatformType ... types ) {
    return new LayoutMapping<Scrollable>( new LayoutFactory<Scrollable>() {
      @Override
      public Layout create( Composite parent, Scrollable scrollable ) {
        return expected;
      }
    }, types );
  }

  private ScrollableAdapter<Scrollable> createAdapter( LayoutMapping<Scrollable> ... layoutMappings ) {
    return new ScrollableAdapter<Scrollable>( shell, platform, testScrollableFactory, layoutMappings );
  }
}