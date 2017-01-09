/*******************************************************************************
 * Copyright (c) 2014 - 2017 Rüdiger Herrmann
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Rüdiger Herrmann - initial API and implementation
 *   Matt Morrissette - allow to use non-static inner IgnoreConditions
 ******************************************************************************/
package com.codeaffine.test.util.junit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.junit.Assume;
import org.junit.rules.MethodRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

public class ConditionalIgnoreRule implements MethodRule {

  public interface IgnoreCondition {
    boolean isSatisfied();
  }

  @Retention(RetentionPolicy.RUNTIME)
  @Target({ElementType.METHOD})
  public @interface ConditionalIgnore {
    Class<? extends IgnoreCondition> condition();
  }

  @Override
  public Statement apply( Statement base, FrameworkMethod method, Object target ) {
    Statement result = base;
    if( hasConditionalIgnoreAnnotation( method ) ) {
      IgnoreCondition condition = getIgnoreContition( target, method );
      if( condition.isSatisfied() ) {
        result = new IgnoreStatement( condition );
      }
    }
    return result;
  }

  private static boolean hasConditionalIgnoreAnnotation( FrameworkMethod method ) {
    return method.getAnnotation( ConditionalIgnore.class ) != null;
  }

  private static IgnoreCondition getIgnoreContition( Object target, FrameworkMethod method ) {
    ConditionalIgnore annotation = method.getAnnotation( ConditionalIgnore.class );
    return new ConditionCreator<>( target, annotation.condition() ).create();
  }

  private static class IgnoreStatement extends Statement {
    private final IgnoreCondition condition;

    IgnoreStatement( IgnoreCondition condition ) {
      this.condition = condition;
    }

    @Override
    public void evaluate() {
      Assume.assumeTrue( "Ignored by " + condition.getClass().getSimpleName(), false );
    }
  }

}