package com.codeaffine.test.util.junit;

import static java.lang.String.format;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

class ConditionCreator<T> {

  private static final String ILLEGAL_CONDITION_PATTERN
    =   "Conditional class '%s' is a member class but was not declared inside the test case using it.\n"
      + "Either make this class a static class, standalone class (by declaring it in it's own file), "
      + "or move it inside the test case using it";

  private final Class<T> conditionType;
  private final Object target;

  ConditionCreator( Object target, Class<T> conditionType ) {
    this.target = target;
    this.conditionType = conditionType;
  }

  T create() {
    checkConditionType();
    try {
      return createCondition();
    } catch( RuntimeException re ) {
      throw re;
    } catch( Exception e ) {
      throw new RuntimeException( e );
    }
  }

  private T createCondition() throws Exception {
    if( isConditionTypeStandalone() ) {
      return createFromStandaloneType();
    }
    return createFromEmbeddedType();
  }

  private void checkConditionType() {
    if( !isConditionTypeStandalone() && !isConditionTypeDeclaredInTarget() ) {
      throw new IllegalArgumentException( format ( ILLEGAL_CONDITION_PATTERN, conditionType.getName() ) );
    }
  }

  private boolean isConditionTypeStandalone() {
    return !conditionType.isMemberClass() || Modifier.isStatic( conditionType.getModifiers() );
  }

  private boolean isConditionTypeDeclaredInTarget() {
    return target.getClass().isAssignableFrom( conditionType.getDeclaringClass() );
  }

  private T createFromStandaloneType() throws Exception {
    try {
      return conditionType.newInstance();
    } catch( IllegalAccessException iae ) {
      Constructor<T> constructor = conditionType.getDeclaredConstructor();
      constructor.setAccessible( true );
      return constructor.newInstance();
    }
  }

  private T createFromEmbeddedType() throws Exception {
    Constructor<T> constructor = conditionType.getDeclaredConstructor( target.getClass() );
    constructor.setAccessible( true );
    return constructor.newInstance( target );
  }
}