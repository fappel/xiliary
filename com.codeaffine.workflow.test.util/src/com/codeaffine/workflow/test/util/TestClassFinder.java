package com.codeaffine.workflow.test.util;

import com.codeaffine.workflow.persistence.ClassFinder;

public class TestClassFinder implements ClassFinder {

  @Override
  public Class<?> find( String className ) {
    try {
      return getClass().getClassLoader().loadClass( className );
    } catch( ClassNotFoundException cnfe ) {
      throw new RuntimeException( cnfe );
    }
  }
}