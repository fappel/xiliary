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
package com.codeaffine.test.util.lang;

/**
 * This class helps unit testing the {@link Object#equals(Object)} and {@link Object#hashCode()}
 * methods.
 *
 * <p>The provided methods help ensuring that the requirements for an <code>equals()</code>
 *   implementation (<em>reflexive</em>, <em>symmetric</em>, <em>transitive</em>,
 *   <em>consistent</em>) and the contract between <code>equals()</code> and <code>hashCode()</code>
 *   are fulfilled.
 * </p>
 *
 * <p>A typical use case for <code>EqualsTester</code> would look like below:
 * </p>
 * <pre>
 * <code>
 * {@literal @Test}
 * public void testEqualsAndHashCode() {
 *   EqualsTester<File> tester = EqualsTester.newInstance( new File( "x" ) );
 *   tester.assertImplementsEqualsAndHashCode();
 *   tester.assertEqual( new File( "a" ), new File( "a" ) );
 *   tester.assertEqual( new File( "a" ), new File( "a" ), new File( "a" ) );
 *   tester.assertNotEqual( new File( "a" ), new File( "b" ) );
 * }
 * </code>
 * </pre>
 * @see Object#equals(Object)
 * @see Object#hashCode()
 */
public class EqualsTester<T> {

  /**
   * Creates an instance of of EqualsTester and executes <em>general</em> tests on the given
   * <code>defaultObject</code>.
   *
   * <p>The general tests that are done and which should apply to all implementations of
   *   {@link Object#equals(Object) equals()} and {@link Object#hashCode() hashCode()} are:
   * </p>
   * <ul>
   *   <li>Ensure that the object is equal with itself. I.e. <code>object.equals( object )</code>
   *     is true.</li>
   *   <li>Ensure that the object is not equal with <code>null</code>.</li>
   *   <li>Ensure that the object is not equal with <code>new Object()</code>.</li>
   * </ul>
   * @param defaultObject the object to execute the standard tests on. Must not be
   *   <code>null</code>.
   */
  public static <T> EqualsTester<T> newInstance( T defaultObject ) {
    return new EqualsTester<T>( defaultObject );
  }

  private final T defaultObject;
  private boolean omitHashCodeTestsForUnequalPairs;

  private EqualsTester( T defaultObject ) {
    checkNotNull( defaultObject, "defaultObject" );
    this.defaultObject = defaultObject;
    assertGeneralConditions( defaultObject );
  }

  /**
   * Prevents checking the {@link Object#hashCode() hash code} in {@link #assertNotEqual()}.
   *
   * <p>Once this method was called, <code>assertNotEqual()</code> does verify the hash code any
   *   more.
   * </p>
   */
  public void omitHashCodeTestForUnequalPairs() {
    omitHashCodeTestsForUnequalPairs = true;
  }

  /**
   * Ensures that the <code>defaultObject</code> that was passed to {@link #newInstance(Object)
   * newInstance()} implements both {@link Object#equals(Object) equals()} and {@link
   * Object#hashCode() hashCode()}.
   *
   * <p>It is generally necessary to override the <code>hashCode()</code> method whenever
   *   <code>equals()</code> is overridden, to maintain the general contract that equal objects
   *   must have equal hash codes. This method helps in ensuring this contract.
   * </p>
   */
  public void assertImplementsEqualsAndHashCode() {
    new Implementation( defaultObject.getClass() ).test();
  }

  /**
   * Ensures that the given objects and their hash code are equal.
   *
   * <p>The method tests if the given <code>object</code> is equal to <code>otherObject</code> and
   *   vice versa (is <em>symmetric</em>). Equality is determined by calling
   *   {@link Object#equals(Object) equals()} on the objects respectively.
   *   To also ensure <em>consistency</em>, the equals test is executed twice.
   * </p>
   * <p>If this holds true, the {@link Object#hashCode() hash code} of both objects is checked in
   *   that
   * </p>
   * <ul>
   *   <li>both objects must return the same hash code.</li>
   *   <li>it is <em>consistent</em>: return the same value when invoked more than once</li>
   * </ul>
   * <p>An {@link java.lang.AssertionError AssertionError} is thrown if any of the two mentioned
   *   conditions is not true.
   * </p>
   *
   * @param object an object that should be equal to <code>otherObject</code>. Must not be
   *   <code>null</code>
   * @param otherObject the object with which <code>object</code> is compared
   * @throws AssertionError if <code>object</code> and <code>otherObject</code> is either not equal
   *   or if their hash code differs.
   */
  public void assertEqual( T object, Object otherObject ) {
    checkNotNull( object, "object" );
    new EqualPair( object, otherObject ).test();
  }

  /**
   * Ensures that the <code>equals()</code> implementation of the given objects is
   * <em>transitive</em>.
   *
   * <p>This method ensures that</p>
   *   <ul>
   *     <li><code>object1.equals( object2 )</code> and</li>
   *     <li><code>object2.equals( object3 )</code> and</li>
   *     <li><code>object1.equals( object3 )</code></li>
   *   </ul>
   * <p>An {@link java.lang.AssertionError AssertionError} is thrown if any of the equals tests
   *   fail.
   * </p>
   *
   * @param object1 an object that should be equal to <code>object2</code> and <code>object3</code>.
   *   Must not be <code>null</code>.
   * @param object2 an object that should be equal to <code>object1</code> and <code>object3</code>.
   *   Must not be <code>null</code>.
   * @param object3 an object that should be equal to <code>object1</code> and <code>object2</code>.
   *   Must not be <code>null</code>.
   */
  public void assertEqual( T object1, Object object2, Object object3 ) {
    checkNotNull( object1, "object1" );
    checkNotNull( object2, "object2" );
    checkNotNull( object3, "object3" );
    new EqualPair( object1, object2 ).test();
    new EqualPair( object1, object3 ).test();
    new EqualPair( object2, object3 ).test();
  }

  /**
   * Ensures that the given objects are not equal. If not suppressed it is also ensured that the
   * hash code of the objects differs.
   *
   * <p>The method tests if the given <code>object</code> is not equal to <code>otherObject</code>
   *   by using its {@link Object#equals(Object) equals()} method.
   *   Though not strictly required by the equals and {@link Object#hashCode() hash code} contract,
   *   it helps hash tables when the hash code of unequal objects also differs. Therefore it is
   *   also ensured that the hash code of both objects isn't equal.
   *   This test can be suppressed by calling {@link #omitHashCodeTestForUnequalPairs()} beforehand.
   * </p>
   * <p>An {@link java.lang.AssertionError AssertionError} is thrown if any of the above mentioned
   *   conditions is not true.
   * </p>
   *
   * @param object an object that should not be equal to <code>otherObject</code>. Must not be
   *   <code>null</code>
   * @param otherObject the object with which <code>object</code> is compared
   * @throws AssertionError if <code>object</code> and <code>otherObject</code> is either equal
   *   or if their hash codes are the same.
   */
  public void assertNotEqual( T object, Object otherObject ) {
    checkNotNull( object, "object" );
    new UnequalPair( object, otherObject ).test();
  }

  private void assertGeneralConditions( T defaultObject ) {
    assertEqual( defaultObject, defaultObject );
    assertNotEqual( defaultObject, null );
    assertNotEqual( defaultObject, new Object() );
  }

  private static void checkNotNull( Object argument, String argumentName ) {
    if( argument == null ) {
      throw new IllegalArgumentException( "Argument must not be null: " + argumentName );
    }
  }

  private static void assertTrue( String message, boolean condition ) {
    if( !condition ) {
      throw new AssertionError( message );
    }
  }

  private static void assertFalse( String message, boolean condition ) {
    if( condition ) {
      throw new AssertionError( message );
    }
  }

  static abstract class Pair {
    final Object object1;
    final Object object2;

    Pair( Object object1, Object object2 ) {
      this.object1 = object1;
      this.object2 = object2;
    }

    void test() {
      testEquals();
      if( objectsNotNull() ) {
        testHashCode();
      }
    }

    abstract void testEquals();
    abstract void testHashCode();

    String messageForFailedEqualsTest( String messagePrefix ) {
      String string1 = String.valueOf( object1 );
      String string2 = String.valueOf( object2 );
      return String.format( "%s for: <%s> and: <%s>", messagePrefix, string1, string2 );
    }

    private boolean objectsNotNull() {
      return object1 != null && object2 != null;
    }
  }

  static class EqualPair extends Pair {

    EqualPair( Object object1, Object object2 ) {
      super( object1, object2 );
    }

    @Override
    void testEquals() {
      String message = messageForFailedEqualsTest( "Equals test failed" );
      assertTrue( message, object1.equals( object2 ) );
      assertTrue( message, object1.equals( object1 ) );  // ensure consistency
      assertTrue( message, object2.equals( object1 ) );
    }

    @Override
    void testHashCode() {
      boolean isEqual = object1.hashCode() == object2.hashCode();
      assertTrue( messageForUnequalHashCode(), isEqual );
      boolean isConsistent = object1.hashCode() == object1.hashCode();
      assertTrue( messageForInconsistentHashCode(), isConsistent );
    }

    private String messageForUnequalHashCode() {
      String message
        = "HashCode is unequal for equal objects, expected: %d for <%s>, was: %d for <%s>";
      String string1 = object1.toString();
      String string2 = object2.toString();
      Integer hashCode1 = Integer.valueOf( object1.hashCode() );
      Integer hashCode2 = Integer.valueOf( object2.hashCode() );
      Object[] args = { hashCode1, string1, hashCode2, string2 };
      return String.format( message, args );
    }

    private String messageForInconsistentHashCode() {
      return String.format( "HashCode is inconsistent for object: <%s>", object1.toString() );
    }
  }

  class UnequalPair extends Pair {

    UnequalPair( Object object1, Object object2 ) {
      super( object1, object2 );
    }

    @Override
    void testEquals() {
      String message = messageForFailedEqualsTest( "Unequals test failed" );
      assertFalse( message, object1.equals( object2 ) );
    }

    @Override
    void testHashCode() {
      if( !omitHashCodeTestsForUnequalPairs ) {
        String msg = messageForFailedHashCodeTest();
        assertTrue( msg, object1.hashCode() != object2.hashCode() );
      }
    }

    private String messageForFailedHashCodeTest() {
      String message
        = "HashCode test failed for unequal objects, was %d for: <%s> and for: <%s>";
      String string1 = object1.toString();
      String string2 = object2.toString();
      Integer hashCode = Integer.valueOf( object1.hashCode() );
      Object[] args = { hashCode, string1, string2 };
      return String.format( message, args );
    }
  }

  static class Implementation {
    private final Class<? extends Object> type;

    Implementation( Class<? extends Object> type ) {
      this.type = type;
    }

    void test() {
      String message = messageForUnimplementedEqualsAndHashCode();
      assertTrue( message, declaresEquals() && declaresHashCode() );
    }

    private boolean declaresEquals() {
      return declaresMethod( "equals", new Class[] { Object.class } );
    }

    private boolean declaresHashCode() {
      return declaresMethod( "hashCode", ( Class<?>[] )null );
    }

    private boolean declaresMethod( String methodName, Class<?>... parameters ) {
      boolean result = false;
      try {
        type.getDeclaredMethod( methodName, parameters );
        result = true;
      } catch( SecurityException ignore ) {
      } catch( NoSuchMethodException ignore ) {
      }
      return result;
    }

    private String messageForUnimplementedEqualsAndHashCode() {
      String className = type.getSimpleName();
      return String.format( "%s does not implement both, equals() and hashCode()", className );
    }
  }
}