/**
 * Copyright (c) 2014 - 2022 Frank Appel
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Frank Appel - initial API and implementation
 */
package com.codeaffine.test.util.junit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.junit.rules.MethodRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

public class AwaitConditionRule implements MethodRule {

  public interface AwaitCondition {
    boolean isSatisfied();
  }

  @Retention( RetentionPolicy.RUNTIME )
  @Target( { ElementType.METHOD } )
  public @interface AwaitConditionDeclaration {
    int timeout();
    Class<? extends AwaitCondition> condition();
  }

  @Override
  public Statement apply( Statement base, FrameworkMethod method, Object target ) {
    if( hasDeclaration( method ) ) {
      return createStatement( method, target );
    }
    return base;
  }

  private static boolean hasDeclaration( FrameworkMethod method ) {
    return getDeclaration( method ) != null;
  }

  private static AwaitConditionDeclaration getDeclaration( FrameworkMethod method ) {
    return method.getAnnotation( AwaitConditionDeclaration.class );
  }

  private static Statement createStatement( FrameworkMethod method, Object target ) {
    AwaitConditionDeclaration declaration = getDeclaration( method );
    return new AwaitConditionStatement( declaration.timeout(), createCondition( target, declaration.condition() ) );
  }

  private static AwaitCondition createCondition( Object target, Class<? extends AwaitCondition> conditionType ) {
    return new ConditionCreator<>( target, conditionType ).create();
  }
}