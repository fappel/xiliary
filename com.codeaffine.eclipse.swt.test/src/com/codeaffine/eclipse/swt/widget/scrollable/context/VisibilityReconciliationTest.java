/**
 * Copyright (c) 2014 - 2017 Frank Appel
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Frank Appel - initial API and implementation
 */
package com.codeaffine.eclipse.swt.widget.scrollable.context;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Scrollable;
import org.junit.Before;
import org.junit.Test;
import org.mockito.stubbing.Answer;

public class VisibilityReconciliationTest {

  private VisibilityReconciliation reconciliation;
  private Composite adapter;
  private Scrollable scrollable;

  @Before
  public void setUp() {
    adapter = stubAdapter();
    scrollable = stubScrollable();
    reconciliation = new VisibilityReconciliation( adapter, new ScrollableControl<>( scrollable ) );
  }

  @Test
  public void showBoth() {
    scrollable.setVisible( true );
    reconciliation.setVisible( true );
    reconciliation.run();

    assertThat( scrollable.getVisible() ).isTrue();
    assertThat( adapter.getVisible() ).isTrue();
  }

  @Test
  public void showAdapterOnly() {
    reconciliation.setVisible( true );
    reconciliation.run();

    assertThat( scrollable.getVisible() ).isTrue();
    assertThat( adapter.getVisible() ).isTrue();
  }

  @Test
  public void showScrollableOnly() {
    scrollable.setVisible( true );
    reconciliation.run();

    assertThat( scrollable.getVisible() ).isTrue();
    assertThat( adapter.getVisible() ).isTrue();
  }

  @Test
  public void showNone() {
    reconciliation.setVisible( false );
    reconciliation.run();

    assertThat( scrollable.getVisible() ).isFalse();
    assertThat( adapter.getVisible() ).isFalse();
  }

  @Test
  public void showAdapterAndHideScrollable() {
    scrollable.setVisible( true );
    reconciliation.run();
    adapter.setVisible( false );

    scrollable.setVisible( false );
    reconciliation.setVisible( true );
    reconciliation.run();

    assertThat( scrollable.getVisible() ).isFalse();
    assertThat( adapter.getVisible() ).isFalse();
  }

  @Test
  public void hideBoth() {
    showBoth();

    scrollable.setVisible( false );
    reconciliation.setVisible( false );
    reconciliation.run();

    assertThat( scrollable.getVisible() ).isFalse();
    assertThat( adapter.getVisible() ).isFalse();
  }

  @Test
  public void hideAdapterOnly() {
    showBoth();

    reconciliation.setVisible( false );
    reconciliation.run();

    assertThat( scrollable.getVisible() ).isFalse();
    assertThat( adapter.getVisible() ).isFalse();
  }

  @Test
  public void hideScrollableOnly() {
    showBoth();

    scrollable.setVisible( false );
    reconciliation.run();

    assertThat( scrollable.getVisible() ).isFalse();
    assertThat( adapter.getVisible() ).isFalse();
  }

  @Test
  public void hideNone() {
    showBoth();

    reconciliation.setVisible( true );
    reconciliation.run();

    assertThat( scrollable.getVisible() ).isTrue();
    assertThat( adapter.getVisible() ).isTrue();
  }

  @Test
  public void hideAdapterAndShowScrollable() {
    adapter.setVisible( true );

    scrollable.setVisible( true );
    reconciliation.setVisible( false );
    reconciliation.run();

    assertThat( scrollable.getVisible() ).isTrue();
    assertThat( adapter.getVisible() ).isTrue();
  }

  private static Composite stubAdapter() {
    Composite result = mock( Composite.class );
    simulateVisibilityAttribute( result );
    return result;
  }

  private static Scrollable stubScrollable() {
    Scrollable result = mock( Scrollable.class );
    simulateVisibilityAttribute( result );
    return result;
  }

  private static void simulateVisibilityAttribute( final Control control ) {
    doAnswer( visibilityStub( control ) ).when( control ).setVisible( anyBoolean() );
  }

  private static Answer<Object> visibilityStub( final Control control ) {
    return invocation -> {
      boolean visible = ( Boolean )invocation.getArguments()[ 0 ];
      when( control.getVisible() ).thenReturn( visible );
      return null;
    };
  }
}