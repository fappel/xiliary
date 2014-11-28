package com.codeaffine.eclipse.swt.widget.scrollable;

import static org.assertj.core.api.Assertions.assertThat;

import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Scrollable;
import org.junit.Test;

public class NativeLayoutFactoryTest {

  @Test
  public void getLayout() {
    NativeLayoutFactory<Scrollable> factory = new NativeLayoutFactory<Scrollable>();

    Layout actual = factory.create( null, null );

    assertThat( actual ).isInstanceOf( FillLayout.class );
  }
}