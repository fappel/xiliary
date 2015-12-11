package com.codeaffine.eclipse.ui.swt.theme;

import static org.assertj.core.api.Assertions.assertThat;
import static org.osgi.framework.FrameworkUtil.getBundle;

import org.eclipse.swt.graphics.FontData;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.eclipse.swt.test.util.DisplayHelper;

public class FontLoaderPDETest {

  @Rule
  public final DisplayHelper displayHelper = new DisplayHelper();

  @Test
  public void load() {
    FontLoader fontLoader = new FontLoader( FontLoader.FONTS_DIRECTORY );

    fontLoader.load( getBundle( FontLoader.class ).getBundleContext() );

    FontData[] fontList = displayHelper.getDisplay().getFontList( "Source Code Pro", true );
    assertThat( fontList ).isNotEmpty();
  }
}