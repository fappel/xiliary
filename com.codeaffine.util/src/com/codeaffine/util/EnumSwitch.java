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