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
package com.codeaffine.eclipse.core.runtime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.PatternSyntaxException;
import java.util.stream.Stream;

import org.junit.Test;

public class PredicatesTest<E> {

  private static final String NAME = "name";
  private static final String ATTRIBUTE_VALUE = "value";
  private static final String ATTRIBUTE_NAME = "name";

  @Test
  public void alwaysTrue() {
    Predicate<Extension> predicate = Predicates.alwaysTrue();

    boolean actual = predicate.test( mock( Extension.class ) );

    assertThat( actual ).isTrue();
  }

  @Test
  public void alwaysTrueWithNullAsInput() {
    Predicate<Extension> predicate = Predicates.alwaysTrue();

    boolean actual = predicate.test( null );

    assertThat( actual ).isTrue();
  }

  @Test
  public void alwaysFalse() {
    Predicate<Extension> predicate = Predicates.alwaysFalse();

    boolean actual = predicate.test( mock( Extension.class ) );

    assertThat( actual ).isFalse();
  }

  @Test
  public void alwaysFalseWithNullAsInput() {
    Predicate<Extension> predicate = Predicates.alwaysFalse();

    boolean actual = predicate.test( null );

    assertThat( actual ).isFalse();
  }

  @Test
  public void isNull() {
    Predicate<Extension> predicate = Predicates.isNull();

    boolean actual = predicate.test( null );

    assertThat( actual ).isTrue();
  }

  @Test
  public void isNullWithInput() {
    Predicate<Extension> predicate = Predicates.isNull();

    boolean actual = predicate.test( mock( Extension.class ) );

    assertThat( actual ).isFalse();
  }

  @Test
  public void notNull() {
    Predicate<Extension> predicate = Predicates.notNull();

    boolean actual = predicate.test( mock( Extension.class ) );

    assertThat( actual ).isTrue();
  }

  @Test
  public void notNullWithNullAsInput() {
    Predicate<Extension> predicate = Predicates.notNull();

    boolean actual = predicate.test( null );

    assertThat( actual ).isFalse();
  }

  @Test
  public void not() {
    Predicate<Extension> predicate = Predicates.not( Predicates.alwaysTrue() );

    boolean actual = predicate.test( null );

    assertThat( actual ).isFalse();
  }

  @Test( expected = IllegalArgumentException.class )
  public void notWithNullAsPredictateArgument() {
    Predicates.not( null );
  }

  @Test
  public void and() {
    Predicate<Extension> predicate = Predicates.and( Predicates.alwaysTrue(), Predicates.alwaysTrue() );

    boolean actual = predicate.test( null );

    assertThat( actual ).isTrue();
  }

  @Test
  public void andWithFalsePredicate() {
    Predicate<Extension> predicate = Predicates.and( Predicates.alwaysTrue(), Predicates.alwaysFalse() );

    boolean actual = predicate.test( null );

    assertThat( actual ).isFalse();
  }

  @Test
  public void andOnIterable() {
    Predicate<Extension> predicate = Predicates.and( asList( Predicates.alwaysTrue(), Predicates.alwaysTrue() ) );

    boolean actual = predicate.test( null );

    assertThat( actual ).isTrue();
  }

  @Test
  public void andOnIterableWithFalsePredicate() {
    Predicate<Extension> predicate = Predicates.and( asList( Predicates.alwaysTrue(), Predicates.alwaysFalse() ) );

    boolean actual = predicate.test( null );

    assertThat( actual ).isFalse();
  }

  @Test
  public void andOnVarargs() {
    Predicate<Extension> predicate
      = Predicates.and( Predicates.alwaysTrue(), Predicates.alwaysTrue(), Predicates.alwaysTrue() );

    boolean actual = predicate.test( null );

    assertThat( actual ).isTrue();
  }

  @Test
  public void andOnVarargsWithFalsePredicate() {
    Predicate<Extension> predicate
      = Predicates.and( Predicates.alwaysTrue(), Predicates.alwaysTrue(), Predicates.alwaysFalse() );

    boolean actual = predicate.test( null );

    assertThat( actual ).isFalse();
  }

  @Test( expected = IllegalArgumentException.class )
  public void andWithNullAsFirstArgument() {
    Predicates.and( null, Predicates.alwaysFalse() );

  }

  @Test( expected = IllegalArgumentException.class )
  public void andWithNullAsSecondArgument() {
    Predicates.and( Predicates.alwaysFalse(), null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void andOnIterableWithNullArgument() {
    Predicates.and( ( Iterable<? extends Predicate<Extension>>)null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void andOnIterableWithNullAsIterableElement() {
    List<Predicate<Extension>> predicates = new ArrayList<>();
    predicates.add( null );

    Predicates.and( predicates );
  }

  @Test( expected = IllegalArgumentException.class )
  @SuppressWarnings("unchecked")
  public void andOnVarargsWithNullArgument() {
    Predicates.and( ( Predicate[] )null );
  }

  @Test( expected = IllegalArgumentException.class )
  @SuppressWarnings("unchecked")
  public void andOnVarargsWithNullAsArrayElement() {
    Predicates.and( new Predicate[] { null } );
  }

  @Test
  public void or() {
    Predicate<Extension> predicate = Predicates.or( Predicates.alwaysTrue(), Predicates.alwaysFalse() );

    boolean actual = predicate.test( null );

    assertThat( actual ).isTrue();
  }

  @Test
  public void orWithFalsePredicatesOnly() {
    Predicate<Extension> predicate = Predicates.or( Predicates.alwaysFalse(), Predicates.alwaysFalse() );

    boolean actual = predicate.test( null );

    assertThat( actual ).isFalse();
  }

  @Test
  public void orOnIterable() {
    Predicate<Extension> predicate = Predicates.or( asList( Predicates.alwaysTrue(), Predicates.alwaysFalse() ) );

    boolean actual = predicate.test( null );

    assertThat( actual ).isTrue();
  }

  @Test
  public void orOnIterableWithFalsePredicatesOnly() {
    Predicate<Extension> predicate = Predicates.or( asList( Predicates.alwaysFalse(), Predicates.alwaysFalse() ) );

    boolean actual = predicate.test( null );

    assertThat( actual ).isFalse();
  }

  @Test
  public void orOnVarargs() {
    Predicate<Extension> predicate
      = Predicates.or( Predicates.alwaysTrue(), Predicates.alwaysFalse(), Predicates.alwaysFalse() );

    boolean actual = predicate.test( null );

    assertThat( actual ).isTrue();
  }

  @Test
  public void orOnVarargsWithFalsePredicatesOnly() {
    Predicate<Extension> predicate
      = Predicates.or( Predicates.alwaysFalse(), Predicates.alwaysFalse(), Predicates.alwaysFalse() );

    boolean actual = predicate.test( null );

    assertThat( actual ).isFalse();
  }

  @Test( expected = IllegalArgumentException.class )
  public void orWithNullAsFirstArgument() {
    Predicates.or( null, Predicates.alwaysFalse() );

  }

  @Test( expected = IllegalArgumentException.class )
  public void orWithNullAsSecondArgument() {
    Predicates.or( Predicates.alwaysFalse(), null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void orOnIterableWithNullArgument() {
    Predicates.or( ( Iterable<? extends Predicate<Extension>>)null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void orOnIterableWithNullAsIterableElement() {
    List<Predicate<Extension>> predicates = new ArrayList<>();
    predicates.add( null );

    Predicates.or( predicates );
  }

  @Test( expected = IllegalArgumentException.class )
  @SuppressWarnings("unchecked")
  public void orOnVarargsWithNullArgument() {
    Predicates.or( ( Predicate[] )null );
  }

  @Test( expected = IllegalArgumentException.class )
  @SuppressWarnings("unchecked")
  public void orOnVarargsWithNullAsArrayElement() {
    Predicates.or( new Predicate[] { null } );
  }

  @Test
  public void attribute() {
    Extension extension = createExtension( ATTRIBUTE_NAME, ATTRIBUTE_VALUE );
    Predicate<Extension> predicate = Predicates.attribute( ATTRIBUTE_NAME, ATTRIBUTE_VALUE );

    boolean actual = predicate.test( extension );

    assertThat( actual ).isTrue();
  }

  @Test
  public void attributeWithNonMatchingValue() {
    Extension extension = createExtension( ATTRIBUTE_NAME, ATTRIBUTE_VALUE );
    Predicate<Extension> predicate = Predicates.attribute( ATTRIBUTE_NAME, "doesNotMatch" );

    boolean actual = predicate.test( extension );

    assertThat( actual ).isFalse();
  }

  @Test
  public void attributeWithNonMatchingNullValue() {
    Extension extension = createExtension( ATTRIBUTE_NAME, null );
    Predicate<Extension> predicate = Predicates.attribute( ATTRIBUTE_NAME, ATTRIBUTE_VALUE );

    boolean actual = predicate.test( extension );

    assertThat( actual ).isFalse();
  }

  @Test( expected = IllegalArgumentException.class )
  public void attributeWithNullAsName() {
    Predicates.attribute( null, ATTRIBUTE_VALUE );
  }

  @Test( expected = IllegalArgumentException.class )
  public void attributeWithNullAsValue() {
    Predicates.attribute( ATTRIBUTE_NAME, null );
  }

  @Test
  public void attributeIsNull() {
    Extension extension = createExtension( ATTRIBUTE_NAME, null );
    Predicate<Extension> predicate = Predicates.attributeIsNull( ATTRIBUTE_NAME );

    boolean actual = predicate.test( extension );

    assertThat( actual ).isTrue();
  }

  @Test
  public void attributeIsNullWithNonNullAttributeValue() {
    Extension extension = createExtension( ATTRIBUTE_NAME, ATTRIBUTE_VALUE );
    Predicate<Extension> predicate = Predicates.attributeIsNull( ATTRIBUTE_NAME );

    boolean actual = predicate.test( extension );

    assertThat( actual ).isFalse();
  }

  @Test( expected = IllegalArgumentException.class )
  public void attributeIsNullWithNullAsName() {
    Predicates.attributeIsNull( null );
  }

  @Test
  public void attributeMatcher() {
    Extension extension = createExtension( ATTRIBUTE_NAME, ATTRIBUTE_VALUE );
    Predicate<Extension> predicate = Predicates.attributeMatcher( ATTRIBUTE_NAME, ".*" );

    boolean actual = predicate.test( extension );

    assertThat( actual ).isTrue();
  }

  @Test
  public void attributeMatcherWithNonMatchingExpression() {
    Extension extension = createExtension( ATTRIBUTE_NAME, ATTRIBUTE_VALUE );
    Predicate<Extension> predicate = Predicates.attributeMatcher( ATTRIBUTE_NAME, "does.not.match" );

    boolean actual = predicate.test( extension );

    assertThat( actual ).isFalse();
  }

  @Test( expected = PatternSyntaxException.class )
  public void attributeMatcherWithIllegalSyntax() {
    Extension extension = createExtension( ATTRIBUTE_NAME, ATTRIBUTE_VALUE );
    Predicate<Extension> predicate = Predicates.attributeMatcher( ATTRIBUTE_NAME, "[" );

    predicate.test( extension );
  }

  @Test( expected = IllegalArgumentException.class )
  public void attributeMatcherWithNullAsName() {
    Predicates.attributeMatcher( null, ATTRIBUTE_VALUE );
  }

  @Test( expected = IllegalArgumentException.class )
  public void attributeWithNullAsRegex() {
    Predicates.attribute( ATTRIBUTE_NAME, null );
  }

  @Test
  public void name() {
    Extension extension = createExtension( NAME );
    Predicate<Extension> predicate = Predicates.name( NAME );

    boolean actual = predicate.test( extension );

    assertThat( actual ).isTrue();
  }

  @Test
  public void nameWithNonMatchingValue() {
    Extension extension = createExtension( NAME );
    Predicate<Extension> predicate = Predicates.name( "doesNotMatch" );

    boolean actual = predicate.test( extension );

    assertThat( actual ).isFalse();
  }

  @Test( expected = IllegalArgumentException.class )
  public void nameWithNullAsNameArgument() {
    Predicates.name( null );
  }

  @Test
  public void nameMatcher() {
    Extension extension = createExtension( NAME );
    Predicate<Extension> predicate = Predicates.nameMatcher( ".*" );

    boolean actual = predicate.test( extension );

    assertThat( actual ).isTrue();
  }

  @Test
  public void nameWithNonMatchingExpression() {
    Extension extension = createExtension( NAME );
    Predicate<Extension> predicate = Predicates.nameMatcher( "does.not.match" );

    boolean actual = predicate.test( extension );

    assertThat( actual ).isFalse();
  }

  @Test( expected = IllegalArgumentException.class )
  public void nameMatcherWithNullAsNameArgument() {
    Predicates.nameMatcher( null );
  }

  @Test( expected = PatternSyntaxException.class )
  public void nameMatcherWithIllegalSyntax() {
    Extension extension = createExtension( NAME );
    Predicate<Extension> predicate = Predicates.nameMatcher( "[" );

    predicate.test( extension );
  }

  private static Extension createExtension( String attributeName, String attributeValue ) {
    Extension result = mock( Extension.class );
    when( result.getAttribute( attributeName ) ).thenReturn( attributeValue );
    return result;
  }

  private static Extension createExtension( String name  ) {
    Extension result = mock( Extension.class );
    when( result.getName() ).thenReturn( name );
    return result;
  }

  @SafeVarargs
  private static Collection<Predicate<Extension>> asList( Predicate<Extension> ... predicates ) {
    List<Predicate<Extension>> result = new ArrayList<>();
    Stream.of( predicates ).forEach( predicate -> result.add( predicate ) );
    return result;
  }
}