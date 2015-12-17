package com.codeaffine.eclipse.ui.swt.theme;

import static com.codeaffine.eclipse.swt.util.ArgumentVerification.verifyNotNull;
import static com.codeaffine.eclipse.ui.swt.theme.AttributeKey.key;
import static com.codeaffine.eclipse.ui.swt.theme.ScrollableAdapterContribution.TOP_LEVEL_WINDOW_SELECTOR;
import static java.lang.Boolean.valueOf;

import java.util.function.Consumer;
import java.util.function.Supplier;

import org.eclipse.swt.widgets.Scrollable;

class AttributeApplicationOperation {

  private final AttributePreserver styleAttributePreserver;
  private final Supplier<Boolean> topLevelWindowChecker;
  private final AttributeKey<?> key;

  private Consumer<AttributePreserver> topLevelApplicator;
  private Consumer<AttributePreserver> defaultApplicator;

  AttributeApplicationOperation( Scrollable scrollable, AttributeKey<?> key ) {
    this.styleAttributePreserver = new AttributePreserver( scrollable );
    this.topLevelWindowChecker = () -> valueOf( scrollable.getShell().getParent() == null );
    this.topLevelApplicator = preserver -> {};
    this.defaultApplicator = preserver -> {};
    this.key = key;
  }

  AttributeApplicationOperation onTopLevelWindow( Consumer<AttributePreserver> topLevelApplicator ) {
    verifyNotNull( topLevelApplicator, "topLevelApplicator" );

    this.topLevelApplicator = topLevelApplicator;
    return this;
  }

  AttributeApplicationOperation onDefault( Consumer<AttributePreserver> defaultApplicator ) {
    verifyNotNull( defaultApplicator, "defaultApplicator" );

    this.defaultApplicator = defaultApplicator;
    return this;
  }

  void execute() {
    if( isTopLevelSelector() ) {
      applyTopLevelSelector();
    } else {
      applyDefault();
    }
  }

  AttributePreserver getStyleAttributePreserver() {
    return styleAttributePreserver;
  }

  private void applyTopLevelSelector() {
    if( isTopLevelWindow() ) {
      defaultApplicator.accept( styleAttributePreserver );
      topLevelApplicator.accept( styleAttributePreserver );
    }
  }

  private void applyDefault() {
    if( !hasTopLevelValue( key( key.identifier + TOP_LEVEL_WINDOW_SELECTOR, key.type ) ) ) {
      defaultApplicator.accept( styleAttributePreserver );
    }
  }

  private boolean hasTopLevelValue( AttributeKey<?> key ) {
    return styleAttributePreserver.get( key ).isPresent();
  }

  private boolean isTopLevelSelector() {
    return key.identifier.endsWith( TOP_LEVEL_WINDOW_SELECTOR );
  }

  private boolean isTopLevelWindow() {
    return topLevelWindowChecker.get().booleanValue();
  }
}