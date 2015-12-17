package com.codeaffine.eclipse.ui.swt.theme;

import static com.codeaffine.eclipse.ui.swt.theme.AttributeApplicator.attach;

import org.eclipse.swt.widgets.Scrollable;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;

import com.codeaffine.eclipse.swt.widget.scrollable.ScrollableAdapterFactory;
import com.codeaffine.eclipse.swt.widget.scrollable.ScrollbarStyle;
import com.codeaffine.eclipse.swt.widget.scrollable.TreeAdapter;

class ApplicatorTestHelper {

  private final ScrollableAdapterFactory factory;
  private final Scrollable scrollable;

  ApplicatorTestHelper( Scrollable scrollable ) {
    this.factory = new ScrollableAdapterFactory();
    this.scrollable = scrollable;
  }

  ScrollableAdapterFactory getFactory() {
    return factory;
  }

  void reparentScrollableOnChildShell() {
    Shell childShell = new Shell( scrollable.getShell() );
    scrollable.setParent( childShell );
  }

  ScrollbarStyle adapt() {
    ScrollbarStyle result = factory.create( ( Tree )scrollable, TreeAdapter.class );
    attach( scrollable, result );
    return result;
  }
}