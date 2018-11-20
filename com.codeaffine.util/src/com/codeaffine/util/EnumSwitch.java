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
package com.codeaffine.util;

import static com.codeaffine.util.ArgumentVerification.verifyCondition;
import static com.codeaffine.util.Messages.ERROR_WRONG_NUMBER_OF_CASE_OPS;
import static java.util.Arrays.asList;

import java.util.function.Supplier;

public class EnumSwitch {

  @SafeVarargs
  public static <T> T switchCase( Object enumConstant, Supplier<T> ...caseSuppliers ) {
    verifyCondition( getEnumConstantCount( enumConstant ) == caseSuppliers.length, ERROR_WRONG_NUMBER_OF_CASE_OPS );

    return caseSuppliers[ getOrdinal( enumConstant ) ].get();
  }

  @SafeVarargs
  public static void switchCase( Object enumConstant, Runnable ... caseActions ) {
    verifyCondition( getEnumConstantCount( enumConstant ) == caseActions.length, ERROR_WRONG_NUMBER_OF_CASE_OPS );

    caseActions[ getOrdinal( enumConstant ) ].run();
  }

  private static int getEnumConstantCount( Object enumConstant ) {
    return enumConstant.getClass().getEnumConstants().length;
  }

  private static int getOrdinal( Object enumConstant ) {
    return asList( enumConstant.getClass().getEnumConstants() ).indexOf( enumConstant );
  }
}