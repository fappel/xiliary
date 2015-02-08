package com.codeaffine.eclipse.swt.widget.scrollable;

import static com.codeaffine.test.util.lang.ThrowableCaptor.thrown;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Tree;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.eclipse.swt.test.util.DisplayHelper;
import com.codeaffine.test.util.lang.ThrowableCaptor.Actor;

public class ControlReflectionUtilTest {

  private static final String UNDECLARED = "undeclared";
  private static final String DISPLAY = "display";
  private static final String REDRAW = "redraw";

  @Rule
  public final DisplayHelper displayHelper = new DisplayHelper();

  private ControlReflectionUtil reflectionUtil;

  @Before
  public void setUp() {
    reflectionUtil = new ControlReflectionUtil();
  }

  @Test
  public void newInstance() {
    Tree actual = reflectionUtil.newInstance( Tree.class );

    assertThat( actual ).isNotNull();
  }

  @Test
  public void newInstanceWithUninstantiableType() {
    Throwable actual = thrown( new Actor() {
      @Override
      public void act() throws Throwable {
        reflectionUtil.newInstance( Control.class );
      }
    } );

    assertThat( actual )
      .hasMessageContaining( Control.class.getName() )
      .hasRootCauseInstanceOf( InstantiationException.class );
  }

  @Test
  public void invokeOfControlMethod() {
    Tree receiver = mock( Tree.class );

    reflectionUtil.invoke( receiver, REDRAW );

    verify( receiver ).redraw();
  }

  @Test
  public void invokeOfWidgetMethod() {
    Tree receiver = mock( Tree.class );

    reflectionUtil.invoke( receiver, "dispose" );

    verify( receiver ).dispose();
  }

  @Test
  public void invokeOfUndeclaredMethod() {
    Throwable actual = thrown( new Actor() {
      @Override
      public void act() throws Throwable {
        reflectionUtil.invoke( mock( Tree.class ), UNDECLARED );
      }
    } );

    assertThat( actual )
      .hasMessageContaining( UNDECLARED )
      .hasRootCauseInstanceOf( NoSuchMethodException.class );
  }

  @Test
  public void invokeOfMethodThatThrowsException() {
    RuntimeException expected = new RuntimeException( "thrownOnPurpose" );
    final Tree receiver = stubTreeWithProblemOnRedraw( expected );

    Throwable actual = thrown( new Actor() {
      @Override
      public void act() throws Throwable {
        reflectionUtil.invoke( receiver, REDRAW );
      }
    } );

    assertThat( actual )
      .isSameAs( expected );
  }

  @Test
  public void setFieldOfWidget() {
    Display expected = displayHelper.getDisplay();
    Tree receiver = reflectionUtil.newInstance( Tree.class );

    reflectionUtil.setField( receiver, DISPLAY, expected );
    Display actual = receiver.getDisplay();

    assertThat( actual ).isSameAs( expected );
  }

  @Test
  public void setFieldOfControl() {
    Composite expected = displayHelper.createShell();
    Tree receiver = reflectionUtil.newInstance( Tree.class );
    reflectionUtil.setField( receiver, DISPLAY, displayHelper.getDisplay() );

    reflectionUtil.setField( receiver, "parent", expected );
    Composite actual = receiver.getParent();

    assertThat( actual ).isSameAs( expected );
  }

  @Test
  public void setFieldWithUndeclaredName() {
    Throwable actual = thrown( new Actor() {

      @Override
      public void act() throws Throwable {
        reflectionUtil.setField( mock( Tree.class ), UNDECLARED, displayHelper.getDisplay() );
      }
    } );

    assertThat( actual )
      .hasMessageContaining( UNDECLARED )
      .hasCauseInstanceOf( NoSuchFieldException.class );
  }

  @Test
  public void setFieldWithWrongType() {
    Throwable actual = thrown( new Actor() {

      @Override
      public void act() throws Throwable {
        reflectionUtil.setField( mock( Tree.class ), DISPLAY, new Object() );
      }
    } );

    assertThat( actual )
      .hasMessageContaining( DISPLAY )
      .hasMessageContaining( Object.class.getName() )
      .isInstanceOf( IllegalArgumentException.class );
  }

  private static Tree stubTreeWithProblemOnRedraw( Throwable problem ) {
    Tree result = mock( Tree.class );
    doThrow( problem ).when( result ).redraw();
    return result;
  }
}