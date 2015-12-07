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

public class EnablementReconciliationTest {

  private EnablementReconciliation reconciliation;
  private Composite adapter;
  private Scrollable scrollable;

  @Before
  public void setUp() {
    adapter = stubAdapter();
    scrollable = stubScrollable();
    reconciliation = new EnablementReconciliation( adapter, new ScrollableControl<>( scrollable ) );
  }

  @Test
  public void enableBoth() {
    scrollable.setEnabled( true );
    reconciliation.setEnabled( true );
    reconciliation.run();

    assertThat( scrollable.getEnabled() ).isTrue();
    assertThat( adapter.getEnabled() ).isTrue();
  }

  @Test
  public void enableAdapterOnly() {
    reconciliation.setEnabled( true );
    reconciliation.run();

    assertThat( scrollable.getEnabled() ).isTrue();
    assertThat( adapter.getEnabled() ).isTrue();
  }

  @Test
  public void enableScrollableOnly() {
    scrollable.setEnabled( true );
    reconciliation.run();

    assertThat( scrollable.getEnabled() ).isTrue();
    assertThat( adapter.getEnabled() ).isTrue();
  }

  @Test
  public void enableNone() {
    reconciliation.setEnabled( false );
    reconciliation.run();

    assertThat( scrollable.getEnabled() ).isFalse();
    assertThat( adapter.getEnabled() ).isFalse();
  }

  @Test
  public void enableAdapterAndHideScrollable() {
    scrollable.setEnabled( true );
    reconciliation.run();
    adapter.setEnabled( false );

    scrollable.setEnabled( false );
    reconciliation.setEnabled( true );
    reconciliation.run();

    assertThat( scrollable.getEnabled() ).isFalse();
    assertThat( adapter.getEnabled() ).isFalse();
  }

  @Test
  public void disableBoth() {
    enableBoth();

    scrollable.setEnabled( false );
    reconciliation.setEnabled( false );
    reconciliation.run();

    assertThat( scrollable.getEnabled() ).isFalse();
    assertThat( adapter.getEnabled() ).isFalse();
  }

  @Test
  public void disableAdapterOnly() {
    disableBoth();

    reconciliation.setEnabled( false );
    reconciliation.run();

    assertThat( scrollable.getEnabled() ).isFalse();
    assertThat( adapter.getEnabled() ).isFalse();
  }

  @Test
  public void disableScrollableOnly() {
    enableBoth();

    scrollable.setEnabled( false );
    reconciliation.run();

    assertThat( scrollable.getEnabled() ).isFalse();
    assertThat( adapter.getEnabled() ).isFalse();
  }

  @Test
  public void disableNone() {
    enableBoth();

    reconciliation.setEnabled( true );
    reconciliation.run();

    assertThat( scrollable.getEnabled() ).isTrue();
    assertThat( adapter.getEnabled() ).isTrue();
  }

  @Test
  public void disableAdapterAndShowScrollable() {
    adapter.setEnabled( true );

    scrollable.setEnabled( true );
    reconciliation.setEnabled( false );
    reconciliation.run();

    assertThat( scrollable.getEnabled() ).isTrue();
    assertThat( adapter.getEnabled() ).isTrue();
  }

  @Test
  public void enableScrollableIfAdapterParentGetsEnabled() {
    scrollable.setEnabled( true );
    when( adapter.isEnabled() ).thenReturn( true );

    reconciliation.run();

    assertThat( scrollable.getEnabled() ).isTrue();
    assertThat( adapter.getEnabled() ).isTrue();
    assertThat( reconciliation.adapterEnabled ).isTrue();
  }

  private static Composite stubAdapter() {
    Composite result = mock( Composite.class );
    simulateEnablementAttribute( result );
    return result;
  }

  private static Scrollable stubScrollable() {
    Scrollable result = mock( Scrollable.class );
    simulateEnablementAttribute( result );
    return result;
  }

  private static void simulateEnablementAttribute( final Control control ) {
    doAnswer( enablementStub( control ) ).when( control ).setEnabled( anyBoolean() );
  }

  private static Answer<Object> enablementStub( final Control control ) {
    return invocation -> {
      boolean visible = ( Boolean )invocation.getArguments()[ 0 ];
      when( control.getEnabled() ).thenReturn( visible );
      return null;
    };
  }
}