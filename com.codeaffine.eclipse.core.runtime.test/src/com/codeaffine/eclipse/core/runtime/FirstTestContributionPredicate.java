package com.codeaffine.eclipse.core.runtime;

public class FirstTestContributionPredicate implements ContributionPredicate {

  @Override
  public boolean apply( Extension input ) {
    return "1".equals( input.getAttribute( "id" ) );
  }
}