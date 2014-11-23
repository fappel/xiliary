package com.codeaffine.eclipse.swt.widget.scrollable;

import static org.assertj.core.api.Assertions.assertThat;

import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Layout;
import org.junit.Test;

public class NativeContentTest {

  @Test
  public void getLayout() {
    NativeContent nativeContent = new NativeContent();

    Layout actual = nativeContent.getLayout();

    assertThat( actual ).isInstanceOf( FillLayout.class );
  }
}