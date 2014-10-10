package com.codeaffine.eclipse.core.runtime;

import static com.codeaffine.eclipse.core.runtime.ArgumentVerification.verifyNotNull;

import java.util.Collection;

import com.codeaffine.eclipse.core.runtime.internal.Operator.ReadExtensionsOperator;

public class ReadMultiProcessor<T> {

  protected final ReadExtensionsOperator<T> operator;

  ReadMultiProcessor( ReadExtensionsOperator<T> operator ) {
    this.operator = operator;
  }

  public ReadMultiProcessor<T> thatMatches( Predicate predicate ) {
    verifyNotNull( predicate, "predicate" );

    operator.setPredicate( predicate );
    return this;
  }

  public Collection<T> process() {
    return operator.create();
  }
}