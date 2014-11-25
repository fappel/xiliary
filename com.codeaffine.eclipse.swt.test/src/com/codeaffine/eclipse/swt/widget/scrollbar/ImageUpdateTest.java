package com.codeaffine.eclipse.swt.widget.scrollbar;

import static com.codeaffine.eclipse.swt.testhelper.ShellHelper.createShell;
import static org.assertj.core.api.Assertions.assertThat;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.eclipse.swt.test.util.DisplayHelper;

public class ImageUpdateTest {

  @Rule
  public final DisplayHelper displayHelper = new DisplayHelper();

  private ImageUpdate update;
  private Label control;
  private Shell shell;

  @Before
  public void setUp() {
    shell = createShell( displayHelper );
    control = new Label( shell, SWT.NONE );
    update = new ImageUpdate( control, FlatScrollBar.DEFAULT_MAX_EXPANSION, SWT.COLOR_RED );
    shell.open();
  }

  @Test
  public void update() {
    update.update();
    Image actual = control.getImage();

    assertThat( actual.getBounds() ).isEqualTo( expectedImageBounds() );
  }

  @Test
  public void updateOnTooSmallControlWidth() {
    control.setSize( 0, 10 );

    update.update();
    Image actual = control.getImage();

    assertThat( actual ).isNull();
  }

  @Test
  public void updateOnTooSmallControlHeight() {
    control.setSize( 10, 0 );

    update.update();
    Image actual = control.getImage();

    assertThat( actual ).isNull();
  }

  @Test
  public void updateWithPreviousImage() {
    Image oldImage = displayHelper.createImage( 10, 20 );
    control.setImage( oldImage );

    update.update();
    Image actual = control.getImage();

    assertThat( actual ).isNotSameAs( oldImage );
    assertThat( oldImage.isDisposed() ).isTrue();
  }

  private Rectangle expectedImageBounds() {
    return new Rectangle( 0, 0, control.getSize().x, control.getSize().y );
  }
}