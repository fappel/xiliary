package com.codeaffine.eclipse.core.runtime;

import static com.codeaffine.eclipse.core.runtime.ArgumentVerification.verifyNotNull;

import java.util.Collection;

import com.codeaffine.eclipse.core.runtime.internal.Operator.ReadExtensionsOperator;

public class MultiProcessor<T> {

  private final ReadExtensionsOperator<T> operator;

  MultiProcessor( ReadExtensionsOperator<T> operator ) {
    this.operator = operator;
  }

  public MultiProcessor<T> thatMatches( Predicate predicate ) {
    verifyNotNull( predicate, "predicate" );

    operator.setPredicate( predicate );
    return this;
  }

  public Collection<T> process() {
    return operator.create();
  }
}