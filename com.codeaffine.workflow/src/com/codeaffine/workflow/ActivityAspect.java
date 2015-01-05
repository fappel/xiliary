package com.codeaffine.workflow;

public interface ActivityAspect {
  void beforeExecute( Activity activity );
  void afterExecute( Activity activity, RuntimeException problem );
}