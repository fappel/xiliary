/**
 * Copyright (c) 2014 - 2022 Frank Appel
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Frank Appel - initial API and implementation
 */
package com.codeaffine.eclipse.swt.util;

import static com.codeaffine.util.ArgumentVerification.verifyNotNull;
import static java.lang.String.format;
import static java.util.Arrays.asList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

public class ReadAndDispatch {

  public static final ProblemHandler ERROR_BOX_HANDLER = ( shell, problem ) -> openErrorDialog( shell, problem );

  static final String ERROR_BOX_TITLE = "Error";
  static final String PROBLEM_MSG = "Problem occured:\n%s\n\n.";

  private final ProblemHandler problemHandler;

  @FunctionalInterface
  public interface ProblemHandler {
    void handle( Shell shell, RuntimeException problem );
  }

  public ReadAndDispatch() {
    this( ( shell, problem ) -> { throw problem; } );
  }

  public ReadAndDispatch( ProblemHandler problemHandler ) {
    verifyNotNull( problemHandler, "problemHandler" );

    this.problemHandler = problemHandler;
  }

  public void spinLoop( Shell shell ) {
    spinLoop( shell, Long.MAX_VALUE - System.currentTimeMillis() );
  }

  public void spinLoop( Shell shell, long duration ) {
    long end = System.currentTimeMillis() + duration;
    while( !shell.isDisposed() && ( end - System.currentTimeMillis() ) > 0 ) {
      readAndDispatch( shell, shell.getDisplay() );
    }
  }

  private void readAndDispatch( Shell shell, Display display ) {
    try {
      if( !display.readAndDispatch() ) {
        display.sleep();
      }
    } catch( RuntimeException rte ) {
      problemHandler.handle( shell, rte );
    }
  }

  public static void openErrorDialog( Shell shell, RuntimeException problem ) {
    verifyNotNull( shell, "shell" );
    verifyNotNull( problem, "problem" );

    MessageBox messageBox = new MessageBox( shell, SWT.ICON_ERROR );
    messageBox.setText( ERROR_BOX_TITLE );
    messageBox.setMessage( createMessage( problem ) );
    messageBox.open();
  }

  private static String createMessage( RuntimeException problem ) {
    StringBuilder messageBuilder = new StringBuilder();
    messageBuilder.append( format( PROBLEM_MSG, problem.getMessage() ) );
    asList( problem.getStackTrace() ).forEach( element -> messageBuilder.append( element.toString() ).append( "\n" ) );
    return messageBuilder.toString();
  }
}