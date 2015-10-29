package com.codeaffine.test.util.lang;

public class ThrowableCaptor {

  @FunctionalInterface
  public interface Actor {
    void act() throws Throwable;
  }

  public static Throwable thrownBy( Actor actor ) {
    try {
      actor.act();
    } catch( Throwable thrown ) {
      return thrown;
    }
    return null;
  }

  /**
   * use ThrowableCaptor.thrownBy instead
   */
  @Deprecated
  public static Throwable thrown( Actor actor ) {
    return thrownBy( actor );
  }
}