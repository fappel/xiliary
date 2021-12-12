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

import static com.codeaffine.eclipse.swt.testhelper.TestResources.PROTECTED_CLASS_NAME;
import static com.codeaffine.eclipse.swt.util.ControlReflectionUtil.$;
import static com.codeaffine.eclipse.swt.widget.scrollable.TreeHelper.createTree;
import static com.codeaffine.test.util.lang.ThrowableCaptor.thrownBy;
import static java.lang.Integer.valueOf;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.lang.reflect.Field;

import org.eclipse.swt.accessibility.Accessible;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Tree;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.eclipse.swt.test.util.DisplayHelper;
import com.codeaffine.eclipse.swt.test.util.SWTIgnoreConditions.GtkPlatform;
import com.codeaffine.eclipse.swt.widget.scrollable.StyledTextAdapter;
import com.codeaffine.eclipse.swt.widget.scrollable.TreeAdapter;
import com.codeaffine.test.util.junit.ConditionalIgnoreRule;
import com.codeaffine.test.util.junit.ConditionalIgnoreRule.ConditionalIgnore;

public class ControlReflectionUtilTest {

  private static final String UNDECLARED = "undeclared";
  private static final String FIELD_NAME_DISPLAY = "display";
  private static final String FIELD_NAME_PARENT = "parent";
  private static final String FIELD_NAME_COLUMN_COUNT = "columnCount";

  @Rule public final ConditionalIgnoreRule conditionIgnoreRule = new ConditionalIgnoreRule();
  @Rule public final DisplayHelper displayHelper = new DisplayHelper();

  private ControlReflectionUtil reflectionUtil;

  @Before
  public void setUp() {
    reflectionUtil = new ControlReflectionUtil();
  }

  @Test
  public void defineWidgetClass() {
    Class<?> actual = reflectionUtil.defineWidgetClass( PROTECTED_CLASS_NAME );

    assertThat( actual.getName() ).isSameAs( PROTECTED_CLASS_NAME );
  }

  @Test
  public void defineWidgetClassThatDoesNotExist() {
    Throwable actual = thrownBy( () -> reflectionUtil.defineWidgetClass( "path.to.Unknown" ) );

    assertThat( actual )
      .hasMessageContaining( "path/to/Unknown.class" )
      .isInstanceOf( IllegalArgumentException.class );
  }

  @Test
  public void newInstance() {
    Tree actual = reflectionUtil.newInstance( Tree.class );

    assertThat( actual ).isNotNull();
  }

  @Test
  public void newInstanceWithUninstantiableType() {
    Throwable actual = thrownBy( () -> reflectionUtil.newInstance( Control.class ) );

    assertThat( actual )
      .hasMessageContaining( Control.class.getName() )
      .hasRootCauseInstanceOf( InstantiationException.class );
  }

  @Test
  public void invokeOfControlMethod() {
    TreeAdapter receiver = reflectionUtil.newInstance( TreeAdapter.class );
    reflectionUtil.setField( receiver, FIELD_NAME_DISPLAY, displayHelper.getDisplay() );
    reflectionUtil.setField( receiver, FIELD_NAME_PARENT, displayHelper.createShell() );

    reflectionUtil.invoke( receiver, "createWidget", $( valueOf( 0 ), int.class ) );

    assertThat( receiver.isDisposed() ).isFalse();
  }

  @Test
  public void invokeOfReceiverMethod() {
    Tree receiver = mock( Tree.class );

    reflectionUtil.invoke( receiver, "showSelection" );

    verify( receiver ).showSelection();
  }

  @Test
  @ConditionalIgnore( condition = GtkPlatform.class )
  public void invokeOfReceiverExtensionMethod() {
    StyledText receiver = reflectionUtil.newInstance( StyledTextAdapter.class );
    reflectionUtil.setField( receiver, FIELD_NAME_DISPLAY, displayHelper.getDisplay() );

    reflectionUtil.invoke( receiver, "initializeAccessible" );
    Accessible actual = reflectionUtil.getField( receiver, "acc", Accessible.class );

    assertThat( actual ).isNotNull();
  }

  @Test
  public void invokeOfUndeclaredMethod() {
    Throwable actual = thrownBy( () -> reflectionUtil.invoke( mock( Tree.class ), UNDECLARED ) );

    assertThat( actual )
      .hasMessageContaining( UNDECLARED )
      .hasRootCauseInstanceOf( NoSuchMethodException.class );
  }

  @Test
  public void invokeOfMethodThatThrowsException() {
    RuntimeException expected = new RuntimeException( "thrownOnPurpose" );
    final Tree receiver = stubTreeWithProblemOnRedraw( expected );

    Throwable actual = thrownBy( () -> reflectionUtil.invoke( receiver, "redraw" ) );

    assertThat( actual )
      .isSameAs( expected );
  }

  @Test
  public void setFieldOfWidget() {
    Display expected = displayHelper.getDisplay();
    Tree receiver = reflectionUtil.newInstance( Tree.class );

    reflectionUtil.setField( receiver, FIELD_NAME_DISPLAY, expected );
    Display actual = receiver.getDisplay();

    assertThat( actual ).isSameAs( expected );
  }

  @Test
  public void setFieldOfControl() {
    Composite expected = displayHelper.createShell();
    Tree receiver = reflectionUtil.newInstance( Tree.class );
    reflectionUtil.setField( receiver, FIELD_NAME_DISPLAY, displayHelper.getDisplay() );

    reflectionUtil.setField( receiver, FIELD_NAME_PARENT, expected );
    Composite actual = receiver.getParent();

    assertThat( actual ).isSameAs( expected );
  }

  @Test
  public void setFieldOfReceiver() throws Exception {
    Tree receiver = createTree( displayHelper.createShell(), 1, 1 );
    int expected = 10;

    reflectionUtil.setField( receiver, FIELD_NAME_COLUMN_COUNT, expected );
    Object actual = readAndreset( receiver, 0 );

    assertThat( actual ).isEqualTo( expected );
  }

  @Test
  public void setFieldWithUndeclaredName() {
    Tree tree = mock( Tree.class );

    Throwable actual = thrownBy( () -> reflectionUtil.setField( tree, UNDECLARED, displayHelper.getDisplay() ) );

    assertThat( actual )
      .hasMessageContaining( UNDECLARED )
      .hasCauseInstanceOf( NoSuchFieldException.class );
  }

  @Test
  public void setFieldWithWrongType() {
    Throwable actual = thrownBy( () -> reflectionUtil.setField( mock( Tree.class ), FIELD_NAME_DISPLAY, new Object() ) );

    assertThat( actual )
      .hasMessageContaining( FIELD_NAME_DISPLAY )
      .hasMessageContaining( Object.class.getName() )
      .isInstanceOf( IllegalArgumentException.class );
  }

  @Test
  public void getFieldOfWidget() {
    Display expected = displayHelper.getDisplay();
    Tree receiver = reflectionUtil.newInstance( Tree.class );
    reflectionUtil.setField( receiver, FIELD_NAME_DISPLAY, expected );

    Display actual = reflectionUtil.getField( receiver, FIELD_NAME_DISPLAY, Display.class );

    assertThat( actual ).isSameAs( expected );
  }

  @Test
  public void getFieldOfControl() {
    Composite expected = displayHelper.createShell();
    Tree receiver = reflectionUtil.newInstance( Tree.class );
    reflectionUtil.setField( receiver, FIELD_NAME_DISPLAY, displayHelper.getDisplay() );
    reflectionUtil.setField( receiver, FIELD_NAME_PARENT, expected );

    Composite actual = reflectionUtil.getField( receiver, FIELD_NAME_PARENT, Composite.class );

    assertThat( actual ).isSameAs( expected );
  }

  @Test
  public void getFieldOfReceiver() {
    int expected = 10;
    Tree receiver = reflectionUtil.newInstance( Tree.class );
    reflectionUtil.setField( receiver, FIELD_NAME_COLUMN_COUNT, expected );

    int actual = reflectionUtil.getField( receiver, FIELD_NAME_COLUMN_COUNT, Integer.class );

    assertThat( actual ).isEqualTo( expected );
  }

  @Test
  @ConditionalIgnore( condition = GtkPlatform.class )
  public void getFieldOfReceiverExtension() {
    int expected = 10;
    Tree receiver = reflectionUtil.newInstance( TreeAdapter.class );
    reflectionUtil.setField( receiver, FIELD_NAME_COLUMN_COUNT, expected );

    int actual = reflectionUtil.getField( receiver, FIELD_NAME_COLUMN_COUNT, Integer.class );

    assertThat( actual ).isEqualTo( expected );
  }

  @Test
  public void getFieldWithUndeclaredName() {
    Tree tree = mock( Tree.class );

    Throwable actual = thrownBy( () -> reflectionUtil.getField( tree, UNDECLARED, Display.class ) );

    assertThat( actual )
      .hasMessageContaining( UNDECLARED )
      .hasCauseInstanceOf( NoSuchFieldException.class );
  }

  @Test
  public void getFieldWithWrongType() {
    Tree receiver = reflectionUtil.newInstance( Tree.class );
    reflectionUtil.setField( receiver, FIELD_NAME_DISPLAY, displayHelper.getDisplay() );

    Throwable actual = thrownBy( () -> reflectionUtil.getField( receiver, FIELD_NAME_DISPLAY, Runnable.class ) );

    assertThat( actual )
      .hasMessageContaining( Display.class.getName() )
      .hasMessageContaining( Runnable.class.getName() )
      .isInstanceOf( ClassCastException.class );
  }

  private Object readAndreset( Tree receiver, int resetValue ) throws Exception {
    Field field = Tree.class.getDeclaredField( FIELD_NAME_COLUMN_COUNT );
    field.setAccessible( true );
    Object result = field.get( receiver );
    reflectionUtil.setField( receiver, FIELD_NAME_COLUMN_COUNT, resetValue );
    return result;
  }

  private static Tree stubTreeWithProblemOnRedraw( Throwable problem ) {
    Tree result = mock( Tree.class );
    doThrow( problem ).when( result ).redraw();
    return result;
  }
}