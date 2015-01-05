package com.codeaffine.workflow.definition;

public interface ActivityAspect {
  void beforeExecute( Activity activity );
  void afterExecute( Activity activity, RuntimeException problem );
}