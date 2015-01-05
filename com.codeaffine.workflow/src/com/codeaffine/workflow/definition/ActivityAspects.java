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