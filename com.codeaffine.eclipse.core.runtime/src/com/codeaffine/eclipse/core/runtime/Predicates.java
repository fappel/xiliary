package com.codeaffine.eclipse.core.runtime;

import static com.codeaffine.eclipse.core.runtime.ArgumentVerification.verifyNoNullElement;
import static com.codeaffine.eclipse.core.runtime.ArgumentVerification.verifyNotNull;
import static java.util.Arrays.asList;

public class Predicates {

  private final static Predicate ALWAYS_TRUE = new Predicate() {
    @Override
    public boolean apply( Extension input ) {
      return true;
    }
  };

  private final static Predicate ALWAYS_FALSE = new Predicate() {
    @Override
    public boolean apply( Extension input ) {
      return false;
    }
  };

  private final static Predicate IS_NULL = new Predicate() {
    @Override
    public boolean apply( Extension input ) {
      return input == null;
    }
  };

  private final static Predicate NOT_NULL = not( isNull() );

  public static Predicate alwaysTrue() {
    return ALWAYS_TRUE;
  }

  public static Predicate alwaysFalse() {
    return ALWAYS_FALSE;
  }

  public static Predicate isNull() {
    return IS_NULL;
  }

  public static Predicate notNull() {
    return NOT_NULL;
  }

  public static Predicate not( final Predicate predicate ) {
    verifyNotNull( predicate, "predicate" );

    return new Predicate() {
      @Override
      public boolean apply( Extension input ) {
        return !predicate.apply( input );
      }
    };
  }

  public static Predicate and( final Iterable<? extends Predicate> predicates ) {
    verifyNotNull( predicates, "predicates" );
    verifyNoNullElement( predicates, "predicates" );

    return new Predicate() {
      @Override
      public boolean apply( Extension input ) {
        return calculateAnd( predicates, input );
      }
    };
  }

  public static Predicate and( Predicate ... predicates ) {
    verifyNotNull( predicates, "predicates" );

    return and( asList( predicates ) );
  }

  public static Predicate and( Predicate first, Predicate second ) {
    return and( new Predicate[] { first, second } );
  }

  public static Predicate or( final Iterable<? extends Predicate> predicates ) {
    verifyNotNull( predicates, "predicates" );
    verifyNoNullElement( predicates, "predicates" );

    return new Predicate() {
      @Override
      public boolean apply( Extension input ) {
        return calculateOr( predicates, input );
      }
    };
  }

  public static Predicate or( Predicate ... predicates ) {
    verifyNotNull( predicates, "predicates" );

    return or( asList( predicates ) );
  }

  public static Predicate or( Predicate first, Predicate second ) {
    return or( new Predicate[] { first, second } );
  }

  public static Predicate attribute( final String name, final String regex ) {
    verifyNotNull( name, "name" );
    verifyNotNull( regex, "regex" );

    return new Predicate() {
      @Override
      public boolean apply( Extension input ) {
        return input.getAttribute( name ).matches( regex );
      }
    };
  }

  private static boolean calculateAnd( Iterable<? extends Predicate> predicates, Extension input ) {
    for( Predicate predicate : predicates ) {
      if( !predicate.apply( input ) ) {
        return false;
      }
    }
    return true;
  }

  private static boolean calculateOr( Iterable<? extends Predicate> predicates, Extension input ) {
    for( Predicate predicate : predicates ) {
      if( predicate.apply( input ) ) {
        return true;
      }
    }
    return false;
  }

  private Predicates() {}
}