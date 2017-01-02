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
package com.codeaffine.workflow.definition;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;


public class ActivityAspects {

  public static <T extends Annotation> T getAnnotation(
    Activity activity, Class<T> annotationType )
  {
    try {
      Method method = activity.getClass().getDeclaredMethod( "execute" );
      return method.getAnnotation( annotationType );
    } catch( SecurityException shouldNotHappen ) {
      throw new IllegalStateException( shouldNotHappen );
    } catch( NoSuchMethodException shouldNotHappen ) {
      throw new IllegalStateException( shouldNotHappen );
    }
  }
}