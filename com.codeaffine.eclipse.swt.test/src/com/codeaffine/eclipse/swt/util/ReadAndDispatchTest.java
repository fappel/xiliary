/**
 * Copyright (c) 2014 - 2017 Frank Appel
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Frank Appel - initial API and implementation
 */
package com.codeaffine.eclipse.swt.util;

import static com.codeaffine.test.util.lang.ThrowableCaptor.thrownBy;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.eclipse.swt.widgets.Shell;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.eclipse.swt.test.util.DisplayHelper;
import com.codeaffine.eclipse.swt.test.util.SWTIgnoreConditions.CocoaPlatform;
import com.codeaffine.eclipse.swt.util.ReadAndDispatch.ProblemHandler;
import com.codeaffine.test.util.junit.ConditionalIgnoreRule;
import com.codeaffine.test.util.junit.ConditionalIgnoreRule.ConditionalIgnore;

public class ReadAndDispatchTest {

  private static final int DURATION = 50;
  private static final int SCHEDULE = 50;

  @Rule
  public final DisplayHelper displayHelper = new DisplayHelper();
  @Rule
  public final ConditionalIgnoreRule conditionalIgnore = new ConditionalIgnoreRule();

  private Shell shell;

  @Before
  public void setUp() {
    shell = openShell();
  }

  @Test
  public void spinLoop() {
    ReadAndDispatch readAndDispatch = new ReadAndDispatch();
    displayHelper.getDisplay().timerExec( SCHEDULE, () -> shell.dispose() );

    readAndDispatch.spinLoop( shell );

    assertThat( shell.isDisposed() ).isTrue();
  }

  @Test
  public void spinLoopWithDuration() {
    ReadAndDispatch readAndDispatch = new ReadAndDispatch();
    long start = System.currentTimeMillis();

    readAndDispatch.spinLoop( shell, DURATION );
    long actual = System.currentTimeMillis() - start;

    assertThat( actual ).isGreaterThanOrEqualTo( DURATION );
  }

  @Test
  public void spinLoopWithProblem() {
    ReadAndDispatch readAndDispatch = new ReadAndDispatch();
    RuntimeException expected = new RuntimeException();
    displayHelper.getDisplay().timerExec( SCHEDULE, () -> { throw expected; } );

    Throwable actual = thrownBy( () -> readAndDispatch.spinLoop( shell ) );

    assertThat( actual ).isSameAs( expected );
  }

  @Test
  public void spinLoopWithProblemHandler() {
    ProblemHandler problemHandler = mock( ProblemHandler.class );
    RuntimeException expected = new RuntimeException();
    ReadAndDispatch readAndDispatch = new ReadAndDispatch( problemHandler );
    displayHelper.getDisplay().timerExec( SCHEDULE, () -> { throw expected; } );

    readAndDispatch.spinLoop( shell, SCHEDULE * 2 );

    verify( problemHandler ).handle( shell, expected );
  }

  @Test
  @ConditionalIgnore(condition = CocoaPlatform.class)
  public void openErrorDialog() {
    Shell problemShell = displayHelper.createShell();
    boolean[] wasOpened = new boolean[ 1 ];
    displayHelper.getDisplay().timerExec( SCHEDULE, () -> captureOpenStateAndClose( problemShell, wasOpened ) );

    ReadAndDispatch.openErrorDialog( problemShell, new RuntimeException() );

    assertThat( wasOpened[ 0 ] ).isTrue();
    assertThat( problemShell.isDisposed() ).isTrue();
  }

  @Test( expected = IllegalArgumentException.class )
  public void constructorWithNullAsProblemHandler() {
    new ReadAndDispatch( null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void openErrorDialogWithNullAsShell() {
    ReadAndDispatch.openErrorDialog( null, new RuntimeException() );
  }

  @Test( expected = IllegalArgumentException.class )
  public void openErrorDialogWithNullAsProblem() {
    ReadAndDispatch.openErrorDialog( shell, null );
  }

  private Shell openShell() {
    Shell result = displayHelper.createShell();
    result.open();
    return result;
  }

  private static void captureOpenStateAndClose( Shell problemShell, boolean[] problemShellOpened ) {
    captureDisposeState( problemShell, problemShellOpened );
    problemShell.close();
  }

  private static boolean captureDisposeState( Shell shell, boolean[] isOpen ) {
    return isOpen[ 0 ] = !shell.isDisposed();
  }
}