package com.codeaffine.eclipse.swt.widget.scrollable;

import static com.codeaffine.eclipse.swt.testhelper.ShellHelper.createShell;
import static junitparams.JUnitParamsRunner.$;
import static org.assertj.core.api.Assertions.assertThat;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Scrollable;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.codeaffine.eclipse.swt.test.util.DisplayHelper;
import com.codeaffine.eclipse.swt.widget.scrollbar.FlatScrollBar;

@RunWith( JUnitParamsRunner.class )
public class ScrollBarFactoryTest {

  @Rule
  public final DisplayHelper displayHelper = new DisplayHelper();

  @Test
  @Parameters( method = "directions" )
  public void create( int direction ) {
    Shell parent = createShell( displayHelper );
    Scrollable scrollable = new Text( parent, SWT.MULTI );
    ScrollBarFactory factory = new ScrollBarFactory();

    FlatScrollBar actual = factory.create( parent, scrollable, direction );

    assertThat( actual.getParent() ).isSameAs( parent );
    assertThat( actual.getStyle() & direction ).isEqualTo( direction );
    assertThat( actual.getBackground() ).isEqualTo( scrollable.getBackground() );
  }

  static Object directions() {
    return $( $( SWT.HORIZONTAL ), $( SWT.VERTICAL ) );
  }
}