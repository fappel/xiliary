package com.codeaffine.workflow.internal;

import static java.lang.String.format;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import com.codeaffine.workflow.NodeLoader;
import com.codeaffine.workflow.WorkflowContext;
import com.codeaffine.workflow.definition.VariableDeclaration;

public class NodeLoaderImpl implements NodeLoader {

  static final String ERROR_DEFAULT_CONSTRUCTOR_MISSING = "Node types must provide a default constructor [%s].";
  static final String ERROR_CHECKED_EXCEPTION = "Node type constructors must not throw checked exceptions [%s].";
  static final String ERROR_OUTSIDE_LOAD = "Call to lookup outside of workflow node creation is not allowed.";

  public static volatile WorkflowContext lookupContext;

  private static final Object LOCK = new Object();

  public static <T> T lookup( VariableDeclaration<T> declaration ) {
    checkOnLoad();

    T result = null;
    if( lookupContext.hasVariableDefinition( declaration  ) ) {
      Object value = lookupContext.getVariableValue( declaration );
      result = declaration.getType().cast( value );
    }
    return result;
  }

  @Override
  public <T> T load( Class<T> toLoad, WorkflowContext context ) {
    try {
      Constructor<T> constructor = toLoad.getDeclaredConstructor();
      constructor.setAccessible( true );
      return doLoad( context, constructor );
    } catch( RuntimeException re ) {
      throw re;
    } catch( NoSuchMethodException nsme ) {
      throw new IllegalArgumentException( format( ERROR_DEFAULT_CONSTRUCTOR_MISSING, toLoad.getName() ), nsme );
    } catch( InvocationTargetException ite ) {
      if( ite.getCause() instanceof RuntimeException ) {
        throw ( RuntimeException )ite.getCause();
      }
      throw new IllegalArgumentException( format( ERROR_CHECKED_EXCEPTION, toLoad.getName() ), ite.getCause() );
    } catch( IllegalAccessException shouldNotHappen ) {
      throw new IllegalArgumentException( shouldNotHappen );
    } catch( InstantiationException shouldNotHappen ) {
      throw new IllegalArgumentException( shouldNotHappen );
    }
  }

  private static <T> T doLoad( WorkflowContext context, Constructor<T> constructor )
    throws InstantiationException, IllegalAccessException, InvocationTargetException
  {
    synchronized( LOCK ) {
      lookupContext = context;
      try {
        return constructor.newInstance();
      } finally {
        lookupContext = null;
      }
    }
  }

  private static void checkOnLoad() {
    if( lookupContext == null ) {
      throw new IllegalStateException( ERROR_OUTSIDE_LOAD );
    }
  }
}