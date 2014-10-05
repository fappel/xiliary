package com.codeaffine.eclipse.core.runtime;

public class ThrowableCaptor {

  public interface Actor {
    void act() throws Throwable;
  }

  public static Throwable thrown( ThrowableCaptor.Actor actor ) {
    Throwable result = null;
    try {
      actor.act();
    } catch( Throwable thrown ) {
      result = thrown;
    }
    return result;
  }
}