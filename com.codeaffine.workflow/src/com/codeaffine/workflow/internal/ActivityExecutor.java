package com.codeaffine.workflow.internal;


import java.util.Collection;
import java.util.HashSet;

import com.codeaffine.workflow.definition.Activity;
import com.codeaffine.workflow.definition.ActivityAspect;

class ActivityExecutor {

  private final Collection<ActivityAspect> aspects;

  ActivityExecutor() {
    this.aspects = new HashSet<ActivityAspect>();
  }

  void add( ActivityAspect activityConditionCheck ) {
    aspects.add( activityConditionCheck );
  }

  void execute( Activity activity ) {
    RuntimeException problem = null;
    runBeforeAspects( activity );
    try {
      new RetryExecutor( activity ).execute();
    } catch( RuntimeException rte ) {
      problem = rte;
      throw rte;
    } finally {
      runAfterAspects( activity, problem );
    }
  }

  private void runBeforeAspects( Activity activity ) {
    for( ActivityAspect aspect : getActivityAspects() ) {
      aspect.beforeExecute( activity );
    }
  }

  private void runAfterAspects( Activity activity, RuntimeException problem ) {
    for( ActivityAspect aspect : getActivityAspects() ) {
      aspect.afterExecute( activity, problem );
    }
  }

  private ActivityAspect[] getActivityAspects() {
    return aspects.toArray( new ActivityAspect[ aspects.size() ] );
  }
}