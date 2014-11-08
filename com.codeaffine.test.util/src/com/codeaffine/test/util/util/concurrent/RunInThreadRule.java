package com.codeaffine.test.util.util.concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

public class RunInThreadRule implements TestRule {

  static class ExecutorServiceFactory {
    ExecutorService create() {
      return Executors.newSingleThreadExecutor();
    }
  }

  @Override
  public Statement apply( Statement base, Description description ) {
    Statement result = base;
    RunInThread annotation = description.getAnnotation( RunInThread.class );
    if( annotation != null ) {
      result = new RunInThreadStatement( base );
    }
    return result;
  }
}