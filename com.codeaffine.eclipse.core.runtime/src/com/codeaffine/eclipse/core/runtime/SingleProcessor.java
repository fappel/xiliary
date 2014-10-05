package com.codeaffine.eclipse.core.runtime;

import static com.codeaffine.eclipse.core.runtime.ArgumentVerification.verifyNotNull;

import com.codeaffine.eclipse.core.runtime.internal.Operator.ReadExtensionOperator;

public class SingleProcessor<T> {

  private final ReadExtensionOperator<T> operator;

  SingleProcessor( ReadExtensionOperator<T> operator ) {
    this.operator = operator;
  }

  public SingleProcessor<T> thatMatches( ContributionPredicate predicate ) {
    verifyNotNull( predicate, "predicate" );

    operator.setPredicate( predicate );
    return this;
  }

  public T process() {
    return operator.create();
  }
}