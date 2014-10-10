package com.codeaffine.eclipse.core.runtime;

import static com.codeaffine.eclipse.core.runtime.ArgumentVerification.verifyNotNull;

import com.codeaffine.eclipse.core.runtime.internal.Operator.ReadExtensionOperator;

public class ReadSingleProcessor<T> {

  protected final ReadExtensionOperator<T> operator;

  ReadSingleProcessor( ReadExtensionOperator<T> operator ) {
    this.operator = operator;
  }

  public ReadSingleProcessor<T> thatMatches( Predicate predicate ) {
    verifyNotNull( predicate, "predicate" );

    operator.setPredicate( predicate );
    return this;
  }

  public T process() {
    return operator.create();
  }
}