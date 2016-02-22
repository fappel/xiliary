/**
 * Copyright (c) 2014 - 2016 Frank Appel
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Frank Appel - initial API and implementation
 */
package com.codeaffine.eclipse.core.runtime;

import static com.codeaffine.eclipse.core.runtime.ArgumentVerification.verifyNoNullElement;
import static com.codeaffine.eclipse.core.runtime.ArgumentVerification.verifyNotNull;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static java.util.stream.StreamSupport.stream;

import java.util.function.Predicate;

public class Predicates {

  private final static Predicate<Extension> ALWAYS_TRUE = extension -> true;
  private final static Predicate<Extension> ALWAYS_FALSE = extension -> false;
  private final static Predicate<Extension> IS_NULL = extension -> extension == null;
  private final static Predicate<Extension> NOT_NULL = not( isNull() );

  public static Predicate<Extension> alwaysTrue() {
    return ALWAYS_TRUE;
  }

  public static Predicate<Extension> alwaysFalse() {
    return ALWAYS_FALSE;
  }

  public static Predicate<Extension> isNull() {
    return IS_NULL;
  }

  public static Predicate<Extension> notNull() {
    return NOT_NULL;
  }

  public static Predicate<Extension> not( Predicate<Extension> predicate ) {
    verifyNotNull( predicate, "predicate" );

    return predicate.negate();
  }

  public static Predicate<Extension> and( Iterable<? extends Predicate<Extension>> predicates ) {
    verifyNotNull( predicates, "predicates" );
    verifyNoNullElement( predicates, "predicates" );

    return extension -> calculateAnd( predicates, extension );
  }

  @SafeVarargs
  public static Predicate<Extension> and( Predicate<Extension> ... predicates ) {
    verifyNotNull( predicates, "predicates" );

    return and( asList( predicates ) );
  }

  @SuppressWarnings("unchecked")
  public static Predicate<Extension> and( Predicate<Extension> first, Predicate<Extension> second ) {
    return and( new Predicate[] { first, second } );
  }

  public static Predicate<Extension> or( Iterable<? extends Predicate<Extension>> predicates ) {
    verifyNotNull( predicates, "predicates" );
    verifyNoNullElement( predicates, "predicates" );

    return extension ->  calculateOr( predicates, extension );
  }

  @SafeVarargs
  public static Predicate<Extension> or( Predicate<Extension> ... predicates ) {
    verifyNotNull( predicates, "predicates" );

    return or( asList( predicates ) );
  }

  @SuppressWarnings("unchecked")
  public static Predicate<Extension> or( Predicate<Extension> first, Predicate<Extension> second ) {
    return or( new Predicate[] { first, second } );
  }

  public static Predicate<Extension> attribute( String name, String value ) {
    verifyNotNull( name, "name" );
    verifyNotNull( value, "value" );

    return extension -> value.equals( extension.getAttribute( name ) );
  }

  public static Predicate<Extension> attributeMatcher( String name, String regex ) {
    verifyNotNull( name, "name" );
    verifyNotNull( regex, "regex" );

    return extension -> extension.getAttribute( name ) != null && extension.getAttribute( name ).matches( regex );
  }

  public static Predicate<Extension> attributeIsNull( String name ) {
    verifyNotNull( name, "name" );

    return extension -> extension.getAttribute( name ) == null;
  }

  public static Predicate<Extension> name( String value ) {
    verifyNotNull( value, "regex" );

    return extension -> value.equals( extension.getName() );
  }

  public static Predicate<Extension> nameMatcher( String regex ) {
    verifyNotNull( regex, "regex" );

    return extension -> extension.getName().matches( regex );
  }

  private static boolean calculateAnd( Iterable<? extends Predicate<Extension>> predicates, Extension extension ) {
    return stream( predicates.spliterator(), false )
          .filter( predicate -> !predicate.test( extension ) )
          .collect( toList() )
          .isEmpty();
  }

  private static boolean calculateOr( Iterable<? extends Predicate<Extension>> predicates, Extension extension ) {
    return !stream( predicates.spliterator(), false )
          .filter( predicate -> predicate.test( extension ) )
          .collect( toList() )
          .isEmpty();
  }

  private Predicates() {}
}