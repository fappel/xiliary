package com.codeaffine.eclipse.core.runtime;

public enum DefaultContributionPredicate implements ContributionPredicate {

  ALL {
    @Override
    public boolean apply( Extension input ) {
      return true;
    }
  },

  NONE {
    @Override
    public boolean apply( Extension input ) {
      return false;
    }
  };
}