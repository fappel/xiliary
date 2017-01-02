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
package com.codeaffine.eclipse.swt.test.util;

import static com.codeaffine.test.util.lang.ThrowableCaptor.thrownBy;
import static org.assertj.core.api.Assertions.assertThat;
import static org.eclipse.swt.SWT.NONE;
import static org.eclipse.swt.SWT.SHELL_TRIM;
import static org.junit.runner.Description.createSuiteDescription;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runners.model.Statement;

public class DisplayHelperTest {

  private DisplayHelper displayHelper;

  @Before
  public void setUp() {
    displayHelper = new DisplayHelper();
  }

  @After
  public void tearDown() {
    displayHelper.dispose();
  }

  @Test
  public void DisplayHelper() {
    assertThat( displayHelper.getNewShells() ).isEmpty();
  }

  @Test
  public void getDisplay() {
    assertThat( displayHelper.getDisplay() ).isNotNull();
  }

  @Test
  public void getNewShells() {
    Shell oldShell = displayHelper.createShell();
    DisplayHelper other = new DisplayHelper();
    Shell newShell = other.createShell();

    assertThat( other.getNewShells() )
      .doesNotContain( oldShell )
      .containsOnlyOnce( newShell );
  }

  @Test
  public void createShell() {
    Shell shell = displayHelper.createShell();

    assertThat( shell.getStyle() ).isEqualTo( NONE | shell.getStyle() );
    assertThat( shell.getDisplay() ).isSameAs( displayHelper.getDisplay() );
  }

  @Test
  public void createShellWithStyleMask() {
    Shell shell = displayHelper.createShell( SHELL_TRIM );

    assertThat( shell.getStyle() ).isEqualTo( SHELL_TRIM | shell.getStyle() );
    assertThat( shell.getDisplay() ).isSameAs( displayHelper.getDisplay() );
  }

  @Test
  public void ensureDisplay() {
    displayHelper.ensureDisplay();

    assertThat( Display.getCurrent() ).isNotNull();
  }

  @Test
  public void createImage() {
    Image image = displayHelper.createImage( 10, 20 );

    assertThat( image.getBounds() ).isEqualTo( new Rectangle( 0, 0, 10, 20 ) );
  }

  @Test
  public void dispose() {
    Shell shell = displayHelper.createShell();
    Image image = displayHelper.createImage( 10, 20 );

    displayHelper.dispose();

    assertThat( shell.isDisposed() ).isTrue();
    assertThat( image.isDisposed() ).isTrue();
  }

  @Test
  public void applyOnSuccessfulTestExecution() throws Throwable {
    Shell shell = displayHelper.createShell();
    Statement originTest = mock( Statement.class );

    Statement encapsulation = displayHelper.apply( originTest, createSuiteDescription( "testName" ) );
    encapsulation.evaluate();

    verify( originTest ).evaluate();
    assertThat( shell.isDisposed() ).isTrue();
  }

  @Test
  public void applyOnFailedTestExecution() throws Throwable {
    Shell shell = displayHelper.createShell();
    Exception toBeThrown = new Exception( "bad" );
    Statement originTest = stubOriginTestEvaluationWithProblem( toBeThrown );
    Statement statement = displayHelper.apply( originTest, createSuiteDescription( "testName" ) );

    Throwable captured = thrownBy( () -> statement.evaluate() );

    assertThat( captured ).isSameAs( toBeThrown );
    assertThat( shell.isDisposed() ).isTrue();
  }

  @Test
  public void getColorCode() {
    Color expected = displayHelper.getDisplay().getSystemColor( SWT.COLOR_BLUE );

    Color actual = displayHelper.getSystemColor( SWT.COLOR_BLUE );

    assertThat( actual ).isEqualTo( expected );
  }

  private static Statement stubOriginTestEvaluationWithProblem( Throwable toBeThrown ) throws Throwable {
    Statement result = mock( Statement.class );
    doThrow( toBeThrown ).when( result ).evaluate();
    return result;
  }
}