/**
 * Copyright (c) 2014 - 2022 Frank Appel
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Frank Appel - initial API and implementation
 */
package com.codeaffine.util;

import static com.codeaffine.util.EnumSwitchTest.EnumWithTwoElements.ONE;
import static com.codeaffine.util.Messages.ERROR_WRONG_NUMBER_OF_CASE_OPS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.function.Supplier;

import org.junit.Test;

public class EnumSwitchTest {

  private static final Object SUPPLIED_VALUE_ONE = new Object();
  private static final Object SUPPLIED_VALUE_TWO = new Object();

  enum EnumWithTwoElements {
    ONE, TWO
  }

  enum EnumWithOneElement {
    SINGLE
  }

  @Test
  public void switchCaseWithSupplier() {
    Supplier<Object> caseOneSupplier = stubSupplier( SUPPLIED_VALUE_ONE );
    Supplier<Object> caseTwoSupplier = stubSupplier( SUPPLIED_VALUE_TWO );

    Object actual = EnumSwitch.switchCase(ONE, caseOneSupplier, caseTwoSupplier );

    assertThat( actual ).isSameAs( SUPPLIED_VALUE_ONE );
    verify( caseTwoSupplier, never() ).get();
  }

  @Test
  public void switchCaseWithSupplierAndWrongNumberOfCaseActions() {
    Supplier<Object> caseOneSupplier = stubSupplier( SUPPLIED_VALUE_ONE );

    Throwable actual = catchThrowable( () -> EnumSwitch.switchCase( ONE, caseOneSupplier ) );

    assertThat( actual )
      .isInstanceOf( IllegalArgumentException.class )
      .hasMessage( ERROR_WRONG_NUMBER_OF_CASE_OPS );
  }

  @Test
  public void switchCaseWithRunnable() {
    Runnable caseOneAction = mock( Runnable.class );
    Runnable caseTwoAction = mock( Runnable.class );

    EnumSwitch.switchCase( ONE, caseOneAction, caseTwoAction );

    verify( caseOneAction ).run();
    verify( caseTwoAction, never() ).run();
  }

  @Test
  public void switchCaseWithRunnableAndWrongNumberOfCaseActions() {
    Runnable caseOneAction = mock( Runnable.class );

    Throwable actual = catchThrowable( () -> EnumSwitch.switchCase( ONE, caseOneAction ) );

    assertThat( actual )
      .isInstanceOf( IllegalArgumentException.class )
      .hasMessage( ERROR_WRONG_NUMBER_OF_CASE_OPS );
  }

  private static Supplier<Object> stubSupplier( Object suppliedValue ) {
    @SuppressWarnings( "unchecked" )
    Supplier<Object> result = mock( Supplier.class );
    when( result.get() ).thenReturn( suppliedValue );
    return result;
  }
}