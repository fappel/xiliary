package com.codeaffine.eclipse.swt.widget.scrollable;

import static com.codeaffine.eclipse.swt.widget.scrollable.NativeLayoutFactory.LAYOUT_TYPE;
import static org.assertj.core.api.Assertions.assertThat;

import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Scrollable;
import org.junit.Test;

public class NativeLayoutFactoryTest {

  @Test
  public void getLayout() {
    NativeLayoutFactory<Scrollable> factory = new NativeLayoutFactory<Scrollable>();

    Layout actual = factory.create( null );

    assertThat( actual ).isInstanceOf( LAYOUT_TYPE );
  }
}