package com.codeaffine.eclipse.swt.widget.scrollable.context;

import static com.codeaffine.eclipse.swt.test.util.ShellHelper.createShell;
import static com.codeaffine.eclipse.swt.widget.scrollable.TreeHelper.createTree;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.custom.ViewForm;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.part.PageBook;

import com.codeaffine.eclipse.swt.test.util.DisplayHelper;
import com.codeaffine.eclipse.swt.widget.scrollable.ScrollableAdapterFactory;
import com.codeaffine.eclipse.swt.widget.scrollable.TreeAdapter;

class LayoutReconciliationHelper {

  private static final Rectangle INITIAL_ADAPTER_BOUNDS = new Rectangle( 10, 20, 30, 40 );

  private final Shell shell;

  private LayoutReconciliation reconciliation;
  private TreeAdapter adapter;
  private Composite parent;
  private Tree scrollable;

  LayoutReconciliationHelper( DisplayHelper displayHelper ) {
    shell = createShell( displayHelper );
  }

  Rectangle setUpWithFillLayout() {
    parent = shell;
    scrollable = createTree( parent, 1, 1 );
    adapter = new ScrollableAdapterFactory().create( scrollable, TreeAdapter.class );
    reconciliation = new LayoutReconciliation( adapter, new ScrollableControl<>( scrollable ) );
    shell.open();
    adapter.setBounds( INITIAL_ADAPTER_BOUNDS );
    return INITIAL_ADAPTER_BOUNDS;
  }

  Rectangle setUpWithStackLayout() {
    parent = shell;
    scrollable = createTree( parent, 1, 1 );
    adapter = new ScrollableAdapterFactory().create( scrollable, TreeAdapter.class );
    reconciliation = new LayoutReconciliation( adapter, new ScrollableControl<>( scrollable ) );
    shell.open();
    adapter.setBounds( INITIAL_ADAPTER_BOUNDS );
    parent.setLayout( createStackLayout( scrollable ) );
    return INITIAL_ADAPTER_BOUNDS;
  }

  Rectangle setUpWithStackLayoutWithNonAdapterTopControl() {
    Rectangle result = setUpWithStackLayout();
    Label topControl = new Label( shell, SWT.NONE );
    ( ( StackLayout )shell.getLayout() ).topControl = topControl;
    return result;
  }

  Rectangle setUpWithViewFormOnContent() {
    ViewForm viewForm = new ViewForm( shell, SWT.NONE );
    parent = viewForm;
    scrollable = createTree( viewForm, 1, 1 );
    adapter = new ScrollableAdapterFactory().create( scrollable, TreeAdapter.class );
    adapter.setBounds( INITIAL_ADAPTER_BOUNDS );
    viewForm.setContent( scrollable );
    reconciliation = new LayoutReconciliation( adapter, new ScrollableControl<>( scrollable ) );
    shell.open();
    return INITIAL_ADAPTER_BOUNDS;
  }

  Rectangle setUpWithViewFormOnTopCenter() {
    ViewForm viewForm = new ViewForm( shell, SWT.NONE );
    parent = viewForm;
    scrollable = createTree( viewForm, 1, 1 );
    adapter = new ScrollableAdapterFactory().create( scrollable, TreeAdapter.class );
    adapter.setBounds( INITIAL_ADAPTER_BOUNDS );
    viewForm.setTopCenter( scrollable );
    reconciliation = new LayoutReconciliation( adapter, new ScrollableControl<>( scrollable ) );
    shell.open();
    return INITIAL_ADAPTER_BOUNDS;
  }

  Rectangle setUpWithViewFormOnTopLeft() {
    ViewForm viewForm = new ViewForm( shell, SWT.NONE );
    parent = viewForm;
    scrollable = createTree( viewForm, 1, 1 );
    adapter = new ScrollableAdapterFactory().create( scrollable, TreeAdapter.class );
    adapter.setBounds( INITIAL_ADAPTER_BOUNDS );
    viewForm.setTopLeft( scrollable );
    reconciliation = new LayoutReconciliation( adapter, new ScrollableControl<>( scrollable ) );
    shell.open();
    return INITIAL_ADAPTER_BOUNDS;
  }

  Rectangle setUpWithViewFormOnTopRight() {
    ViewForm viewForm = new ViewForm( shell, SWT.NONE );
    parent = viewForm;
    scrollable = createTree( viewForm, 1, 1 );
    adapter = new ScrollableAdapterFactory().create( scrollable, TreeAdapter.class );
    adapter.setBounds( INITIAL_ADAPTER_BOUNDS );
    viewForm.setTopRight( scrollable );
    reconciliation = new LayoutReconciliation( adapter, new ScrollableControl<>( scrollable ) );
    shell.open();
    return INITIAL_ADAPTER_BOUNDS;
  }

  Rectangle setUpWithPageBook() {
    PageBook pageBook = new PageBook( shell, SWT.NONE );
    scrollable = createTree( pageBook, 1, 1 );
    return setUpWithPageBook( pageBook, scrollable );
  }

  Rectangle setUpWithPageBookWithNonAdapterPage() {
    PageBook pageBook = new PageBook( shell, SWT.NONE );
    Label page = new Label( pageBook, SWT.NONE );
    scrollable = createTree( pageBook, 1, 1 );
    return setUpWithPageBook( pageBook, page );
  }

  Rectangle setUpWithSashForm() {
    SashForm sashForm = new SashForm( shell, SWT.NONE );
    parent = sashForm;
    scrollable = createTree( sashForm, 1, 1 );
    adapter = new ScrollableAdapterFactory().create( scrollable, TreeAdapter.class );
    adapter.setBounds( INITIAL_ADAPTER_BOUNDS );
    new Label( sashForm, SWT.NONE );
    reconciliation = new LayoutReconciliation( adapter, new ScrollableControl<>( scrollable ) );
    shell.open();
    return INITIAL_ADAPTER_BOUNDS;
  }

  Rectangle setUpWithCTabFolder() {
    CTabFolder cTabFolder = new CTabFolder( shell, SWT.BOTTOM | SWT.FLAT );
    parent = cTabFolder;
    scrollable = createTree( parent, 1, 1 );
    adapter = new ScrollableAdapterFactory().create( scrollable, TreeAdapter.class );
    CTabItem item = new CTabItem( cTabFolder, SWT.NONE, 0 );
    item.setText( "item" );
    item.setControl( scrollable );
    adapter.setBounds( INITIAL_ADAPTER_BOUNDS );
    reconciliation = new LayoutReconciliation( adapter, new ScrollableControl<>( scrollable ) );
    shell.open();
    return INITIAL_ADAPTER_BOUNDS;
  }

  Rectangle runReconciliation() {
    reconciliation.run();
    return adapter.getBounds();
  }

  LayoutReconciliation getReconciliation() {
    return reconciliation;
  }

  TreeAdapter getAdapter() {
    return adapter;
  }

  Tree getScrollable() {
    return scrollable;
  }

  Composite getParent() {
    return parent;
  }

  <T extends Composite> T getParent( Class<T> type ) {
    return type.cast( parent );
  }

  void reparentScrollableToAdapterParent() {
    getScrollable().setParent( getAdapter() );
    getScrollable().setParent( getAdapter().getParent() );
  }

  private static StackLayout createStackLayout( Tree topControl ) {
    StackLayout result = new StackLayout();
    result.topControl = topControl;
    return result;
  }

  private Rectangle setUpWithPageBook( PageBook pageBook, Control page ) {
    parent = pageBook;
    adapter = new ScrollableAdapterFactory().create( scrollable, TreeAdapter.class );
    adapter.setVisible( false );
    pageBook.showPage( page );
    reconciliation = new LayoutReconciliation( adapter, new ScrollableControl<>( scrollable ) );
    shell.open();
    return INITIAL_ADAPTER_BOUNDS;
  }
}