package com.codeaffine.eclipse.core.runtime;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;

public class TestExceptionHandler implements ExtensionExceptionHandler {

  private final ArrayList<CoreException> exceptions;

  public TestExceptionHandler() {
    this.exceptions = new ArrayList<CoreException>();
  }

  @Override
  public void handle( CoreException creationException ) {
    exceptions.add( creationException );
  }


  public List<CoreException> getExceptions() {
    return exceptions;
  }
}