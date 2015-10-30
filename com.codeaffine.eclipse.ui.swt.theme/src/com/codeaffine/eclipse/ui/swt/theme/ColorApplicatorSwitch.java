package com.codeaffine.eclipse.ui.swt.theme;

import static com.codeaffine.eclipse.swt.util.ArgumentVerification.verifyNotNull;
import static com.codeaffine.eclipse.ui.swt.theme.ScrollableAdapterContribution.TOP_LEVEL_WINDOW_SELECTOR;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Scrollable;

class ColorApplicatorSwitch {

  private final Scrollable scrollable;
  private final String key;

  private Runnable topLevelApplicator;
  private Runnable defaultApplicator;

  ColorApplicatorSwitch( Scrollable scrollable, String key ) {
    this.topLevelApplicator = () -> {};
    this.defaultApplicator = () -> {};
    this.scrollable = scrollable;
    this.key = key;
  }

  ColorApplicatorSwitch onTopLevelWindow( Runnable topLevelApplicator ) {
    verifyNotNull( topLevelApplicator, "topLevelApplicator" );

    this.topLevelApplicator = topLevelApplicator;
    return this;
  }

  ColorApplicatorSwitch onDefault( Runnable defaultApplicator ) {
    verifyNotNull( defaultApplicator, "defaultApplicator" );

    this.defaultApplicator = defaultApplicator;
    return this;
  }

  void apply() {
    if( isTopLevelSelector() ) {
      applyTopLevelSelector();
    } else {
      applyDefault();
    }
  }

  void buffer( String key, Color value ) {
    scrollable.setData( key, value );
  }

  private void applyTopLevelSelector() {
    if( isTopLevelWindow() ) {
      defaultApplicator.run();
      topLevelApplicator.run();
    }
  }

  private void applyDefault() {
    if( !hasTopLevelValue( key + TOP_LEVEL_WINDOW_SELECTOR ) ) {
      defaultApplicator.run();
    }
  }

  private boolean hasTopLevelValue( String key ) {
    return scrollable.getData( key ) != null;
  }

  private boolean isTopLevelSelector() {
    return key.endsWith( TOP_LEVEL_WINDOW_SELECTOR );
  }

  private boolean isTopLevelWindow() {
    return scrollable.getShell().getParent() == null;
  }
}