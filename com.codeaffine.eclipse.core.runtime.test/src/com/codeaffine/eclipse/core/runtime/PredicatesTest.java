package com.codeaffine.eclipse.core.runtime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.PatternSyntaxException;

import org.junit.Test;

public class PredicatesTest<E> {

  private static final String NAME = "name";
  private static final String ATTRIBUTE_VALUE = "value";
  private static final String ATTRIBUTE_NAME = "name";

  @Test
  public void alwaysTrue() {
    Predicate predicate = Predicates.alwaysTrue();

    boolean actual = predicate.apply( mock( Extension.class ) );

    assertThat( actual ).isTrue();
  }

  @Test
  public void alwaysTrueWithNullAsInput() {
    Predicate predicate = Predicates.alwaysTrue();

    boolean actual = predicate.apply( null );

    assertThat( actual ).isTrue();
  }

  @Test
  public void alwaysFalse() {
    Predicate predicate = Predicates.alwaysFalse();

    boolean actual = predicate.apply( mock( Extension.class ) );

    assertThat( actual ).isFalse();
  }

  @Test
  public void alwaysFalseWithNullAsInput() {
    Predicate predicate = Predicates.alwaysFalse();

    boolean actual = predicate.apply( null );

    assertThat( actual ).isFalse();
  }

  @Test
  public void isNull() {
    Predicate predicate = Predicates.isNull();

    boolean actual = predicate.apply( null );

    assertThat( actual ).isTrue();
  }

  @Test
  public void isNullWithInput() {
    Predicate predicate = Predicates.isNull();

    boolean actual = predicate.apply( mock( Extension.class ) );

    assertThat( actual ).isFalse();
  }

  @Test
  public void notNull() {
    Predicate predicate = Predicates.notNull();

    boolean actual = predicate.apply( mock( Extension.class ) );

    assertThat( actual ).isTrue();
  }

  @Test
  public void notNullWithNullAsInput() {
    Predicate predicate = Predicates.notNull();

    boolean actual = predicate.apply( null );

    assertThat( actual ).isFalse();
  }

  @Test
  public void not() {
    Predicate predicate = Predicates.not( Predicates.alwaysTrue() );

    boolean actual = predicate.apply( null );

    assertThat( actual ).isFalse();
  }

  @Test( expected = IllegalArgumentException.class )
  public void notWithNullAsPredictateArgument() {
    Predicates.not( null );
  }

  @Test
  public void and() {
    Predicate predicate = Predicates.and( Predicates.alwaysTrue(), Predicates.alwaysTrue() );

    boolean actual = predicate.apply( null );

    assertThat( actual ).isTrue();
  }

  @Test
  public void andWithFalsePredicate() {
    Predicate predicate = Predicates.and( Predicates.alwaysTrue(), Predicates.alwaysFalse() );

    boolean actual = predicate.apply( null );

    assertThat( actual ).isFalse();
  }

  @Test
  public void andOnIterable() {
    Predicate predicate = Predicates.and( asList( Predicates.alwaysTrue(), Predicates.alwaysTrue() ) );

    boolean actual = predicate.apply( null );

    assertThat( actual ).isTrue();
  }

  @Test
  public void andOnIterableWithFalsePredicate() {
    Predicate predicate = Predicates.and( asList( Predicates.alwaysTrue(), Predicates.alwaysFalse() ) );

    boolean actual = predicate.apply( null );

    assertThat( actual ).isFalse();
  }

  @Test
  public void andOnVarargs() {
    Predicate predicate = Predicates.and( Predicates.alwaysTrue(), Predicates.alwaysTrue(), Predicates.alwaysTrue() );

    boolean actual = predicate.apply( null );

    assertThat( actual ).isTrue();
  }

  @Test
  public void andOnVarargsWithFalsePredicate() {
    Predicate predicate = Predicates.and( Predicates.alwaysTrue(), Predicates.alwaysTrue(), Predicates.alwaysFalse() );

    boolean actual = predicate.apply( null );

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
    Predicates.and( ( Iterable<? extends Predicate>)null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void andOnIterableWithNullAsIterableElement() {
    ArrayList<Predicate> predicates = new ArrayList<Predicate>();
    predicates.add( null );

    Predicates.and( predicates );
  }

  @Test( expected = IllegalArgumentException.class )
  public void andOnVarargsWithNullArgument() {
    Predicates.and( ( Predicate[] )null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void andOnVarargsWithNullAsArrayElement() {
    Predicates.and( new Predicate[] { null } );
  }

  @Test
  public void or() {
    Predicate predicate = Predicates.or( Predicates.alwaysTrue(), Predicates.alwaysFalse() );

    boolean actual = predicate.apply( null );

    assertThat( actual ).isTrue();
  }

  @Test
  public void orWithFalsePredicatesOnly() {
    Predicate predicate = Predicates.or( Predicates.alwaysFalse(), Predicates.alwaysFalse() );

    boolean actual = predicate.apply( null );

    assertThat( actual ).isFalse();
  }

  @Test
  public void orOnIterable() {
    Predicate predicate = Predicates.or( asList( Predicates.alwaysTrue(), Predicates.alwaysFalse() ) );

    boolean actual = predicate.apply( null );

    assertThat( actual ).isTrue();
  }

  @Test
  public void orOnIterableWithFalsePredicatesOnly() {
    Predicate predicate = Predicates.or( asList( Predicates.alwaysFalse(), Predicates.alwaysFalse() ) );

    boolean actual = predicate.apply( null );

    assertThat( actual ).isFalse();
  }

  @Test
  public void orOnVarargs() {
    Predicate predicate = Predicates.or( Predicates.alwaysTrue(), Predicates.alwaysFalse(), Predicates.alwaysFalse() );

    boolean actual = predicate.apply( null );

    assertThat( actual ).isTrue();
  }

  @Test
  public void orOnVarargsWithFalsePredicatesOnly() {
    Predicate predicate = Predicates.or( Predicates.alwaysFalse(), Predicates.alwaysFalse(), Predicates.alwaysFalse() );

    boolean actual = predicate.apply( null );

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
    Predicates.or( ( Iterable<? extends Predicate>)null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void orOnIterableWithNullAsIterableElement() {
    ArrayList<Predicate> predicates = new ArrayList<Predicate>();
    predicates.add( null );

    Predicates.or( predicates );
  }

  @Test( expected = IllegalArgumentException.class )
  public void orOnVarargsWithNullArgument() {
    Predicates.or( ( Predicate[] )null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void orOnVarargsWithNullAsArrayElement() {
    Predicates.or( new Predicate[] { null } );
  }

  @Test
  public void attribute() {
    Extension extension = createExtension( ATTRIBUTE_NAME, ATTRIBUTE_VALUE );
    Predicate predicate = Predicates.attribute( ATTRIBUTE_NAME, ATTRIBUTE_VALUE );

    boolean actual = predicate.apply( extension );

    assertThat( actual ).isTrue();
  }

  @Test
  public void attributeWithNonMatchingValue() {
    Extension extension = createExtension( ATTRIBUTE_NAME, ATTRIBUTE_VALUE );
    Predicate predicate = Predicates.attribute( ATTRIBUTE_NAME, "doesNotMatch" );

    boolean actual = predicate.apply( extension );

    assertThat( actual ).isFalse();
  }

  @Test
  public void attributeWithNonMatchingNullValue() {
    Extension extension = createExtension( ATTRIBUTE_NAME, null );
    Predicate predicate = Predicates.attribute( ATTRIBUTE_NAME, ATTRIBUTE_VALUE );

    boolean actual = predicate.apply( extension );

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
    Predicate predicate = Predicates.attributeIsNull( ATTRIBUTE_NAME );

    boolean actual = predicate.apply( extension );

    assertThat( actual ).isTrue();
  }

  @Test
  public void attributeIsNullWithNonNullAttributeValue() {
    Extension extension = createExtension( ATTRIBUTE_NAME, ATTRIBUTE_VALUE );
    Predicate predicate = Predicates.attributeIsNull( ATTRIBUTE_NAME );

    boolean actual = predicate.apply( extension );

    assertThat( actual ).isFalse();
  }

  @Test( expected = IllegalArgumentException.class )
  public void attributeIsNullWithNullAsName() {
    Predicates.attributeIsNull( null );
  }

  @Test
  public void attributeMatcher() {
    Extension extension = createExtension( ATTRIBUTE_NAME, ATTRIBUTE_VALUE );
    Predicate predicate = Predicates.attributeMatcher( ATTRIBUTE_NAME, ".*" );

    boolean actual = predicate.apply( extension );

    assertThat( actual ).isTrue();
  }

  @Test
  public void attributeMatcherWithNonMatchingExpression() {
    Extension extension = createExtension( ATTRIBUTE_NAME, ATTRIBUTE_VALUE );
    Predicate predicate = Predicates.attributeMatcher( ATTRIBUTE_NAME, "does.not.match" );

    boolean actual = predicate.apply( extension );

    assertThat( actual ).isFalse();
  }

  @Test( expected = PatternSyntaxException.class )
  public void attributeMatcherWithIllegalSyntax() {
    Extension extension = createExtension( ATTRIBUTE_NAME, ATTRIBUTE_VALUE );
    Predicate predicate = Predicates.attributeMatcher( ATTRIBUTE_NAME, "[" );

    predicate.apply( extension );
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
    Predicate predicate = Predicates.name( NAME );

    boolean actual = predicate.apply( extension );

    assertThat( actual ).isTrue();
  }

  @Test
  public void nameWithNonMatchingValue() {
    Extension extension = createExtension( NAME );
    Predicate predicate = Predicates.name( "doesNotMatch" );

    boolean actual = predicate.apply( extension );

    assertThat( actual ).isFalse();
  }

  @Test( expected = IllegalArgumentException.class )
  public void nameWithNullAsNameArgument() {
    Predicates.name( null );
  }

  @Test
  public void nameMatcher() {
    Extension extension = createExtension( NAME );
    Predicate predicate = Predicates.nameMatcher( ".*" );

    boolean actual = predicate.apply( extension );

    assertThat( actual ).isTrue();
  }

  @Test
  public void nameWithNonMatchingExpression() {
    Extension extension = createExtension( NAME );
    Predicate predicate = Predicates.nameMatcher( "does.not.match" );

    boolean actual = predicate.apply( extension );

    assertThat( actual ).isFalse();
  }

  @Test( expected = IllegalArgumentException.class )
  public void nameMatcherWithNullAsNameArgument() {
    Predicates.nameMatcher( null );
  }

  @Test( expected = PatternSyntaxException.class )
  public void nameMatcherWithIllegalSyntax() {
    Extension extension = createExtension( NAME );
    Predicate predicate = Predicates.nameMatcher( "[" );

    predicate.apply( extension );
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

  private static Collection<Predicate> asList( Predicate ... predicates ) {
    ArrayList<Predicate> result = new ArrayList<Predicate>();
    for (Predicate predicate : predicates) {
      result.add( predicate );
    }
    return result;
  }
}