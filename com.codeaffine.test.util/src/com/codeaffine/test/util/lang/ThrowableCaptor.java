package com.codeaffine.test.util.lang;

public class ThrowableCaptor {

  public interface Actor {
    void act() throws Throwable;
  }

  public static Throwable thrown( Actor actor ) {
    Throwable result = null;
    try {
      actor.act();
    } catch( Throwable thrown ) {
      result = thrown;
    }
    return result;
  }
}