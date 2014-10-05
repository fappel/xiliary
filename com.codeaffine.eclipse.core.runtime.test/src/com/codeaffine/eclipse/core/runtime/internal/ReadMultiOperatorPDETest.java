package com.codeaffine.eclipse.core.runtime.internal;

import static com.codeaffine.eclipse.core.runtime.TestExtension.EXTENSION_POINT;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;

import org.eclipse.core.runtime.Platform;
import org.junit.Before;
import org.junit.Test;

import com.codeaffine.eclipse.core.runtime.Extension;
import com.codeaffine.eclipse.core.runtime.FirstTestContributionPredicate;

public class ReadMultiOperatorPDETest {

  private ReadMultiOperator operator;

  @Before
  public void setUp() {
    operator = new ReadMultiOperator( Platform.getExtensionRegistry() );
    operator.setExtensionPointId( EXTENSION_POINT );
  }

  @Test
  public void create() {
    Collection<Extension> actuals = operator.create();

    assertThat( actuals ).hasSize( 2 );
  }

  @Test
  public void createWithPredication() {
    operator.setPredicate( new FirstTestContributionPredicate() );

    Collection<Extension> actuals = operator.create();

    assertThat( actuals ).hasSize( 1 );
  }
}