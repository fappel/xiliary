package com.codeaffine.eclipse.swt.widget.scrollable;

import static com.codeaffine.eclipse.swt.test.util.ShellHelper.createShell;
import static com.codeaffine.eclipse.swt.widget.scrollable.TreeHelper.createTree;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.part.PageBook;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.eclipse.swt.test.util.DisplayHelper;
import com.codeaffine.eclipse.swt.test.util.SWTIgnoreConditions.GtkPlatform;
import com.codeaffine.test.util.junit.ConditionalIgnoreRule;
import com.codeaffine.test.util.junit.ConditionalIgnoreRule.ConditionalIgnore;

public class TreePageResizeFilterTest {

  private static final Rectangle BOUNDS = new Rectangle( 1, 2, 600, 800 );

  @Rule public final ConditionalIgnoreRule conditionIgnoreRule = new ConditionalIgnoreRule();
  @Rule public final DisplayHelper displayHelper = new DisplayHelper();

  private Shell shell;

  @Before
  public void setUp() {
    shell = createShell( displayHelper );
  }

  @Test
  @ConditionalIgnore( condition = GtkPlatform.class )
  public void filterEventsIfUninitialized() {
    PageBook pageBook = new PageBook( shell, SWT.NONE );
    Tree treePage = createAdaptedTree( pageBook );
    Listener listener = registerListenerSpy( treePage );

    treePage.setBounds( BOUNDS );

    verify( listener, never() ).handleEvent( any( Event.class ) );
  }

  @Test
  @ConditionalIgnore( condition = GtkPlatform.class )
  public void passEventsIfInitialized() {
    PageBook pageBook = new PageBook( shell, SWT.NONE );
    Tree treePage = createAdaptedTree( pageBook );
    pageBook.showPage( treePage );
    shell.layout();
    Listener listener = registerListenerSpy( treePage );

    treePage.setBounds( BOUNDS );

    verify( listener ).handleEvent( any( Event.class ) );
  }

  @Test
  @ConditionalIgnore( condition = GtkPlatform.class )
  public void passEventsIfNonPagebookParent() {
    Composite parent = new Composite( shell, SWT.NONE );
    Tree treePage = createAdaptedTree( parent );
    Listener listener = registerListenerSpy( treePage );

    treePage.setBounds( BOUNDS );

    verify( listener, atLeast( 1 ) ).handleEvent( any( Event.class ) );
  }

  @Test
  @ConditionalIgnore( condition = GtkPlatform.class )
  public void cleanupOnAdapterDisposal() {
    PageBook pageBook = new PageBook( shell, SWT.NONE );
    Tree treePage = createAdaptedTreeButDisposeAdapter( pageBook );
    Listener listener = registerListenerSpy( treePage );

    treePage.setBounds( BOUNDS );

    verify( listener, atLeast( 1 ) ).handleEvent( any( Event.class ) );
  }

  private static Tree createAdaptedTree( Composite parent ) {
    Tree result = createTree( parent, 4, 6 );
    new ScrollableAdapterFactory().create( result, TreeAdapter.class );
    return result;
  }

  private static Tree createAdaptedTreeButDisposeAdapter( PageBook pageBook ) {
    Tree result = createTree( pageBook, 4, 6 );
    TreeAdapter adapter = new ScrollableAdapterFactory().create( result, TreeAdapter.class );
    result.setParent( adapter );
    result.setParent( pageBook );
    adapter.dispose();
    return result;
  }

  private static Listener registerListenerSpy( Tree tree ) {
    Listener result = mock( Listener.class );
    tree.addListener( SWT.Resize, result );
    return result;
  }
}