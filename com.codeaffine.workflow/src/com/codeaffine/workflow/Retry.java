package com.codeaffine.workflow;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention( RetentionPolicy.RUNTIME )
@Target( {
  java.lang.annotation.ElementType.METHOD
} )
public @interface Retry {
  public abstract Class<? extends RuntimeException> on();
  public abstract int times();
  public abstract int delay();
}
