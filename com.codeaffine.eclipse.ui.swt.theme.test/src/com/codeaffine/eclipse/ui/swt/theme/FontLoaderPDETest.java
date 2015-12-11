package com.codeaffine.eclipse.ui.swt.theme;

import static org.assertj.core.api.Assertions.assertThat;
import static org.osgi.framework.FrameworkUtil.getBundle;

import org.eclipse.swt.graphics.FontData;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.eclipse.swt.test.util.DisplayHelper;
import com.codeaffine.eclipse.swt.test.util.SWTIgnoreConditions.GtkPlatform;
import com.codeaffine.test.util.junit.ConditionalIgnoreRule;
import com.codeaffine.test.util.junit.ConditionalIgnoreRule.ConditionalIgnore;

public class FontLoaderPDETest {

  @Rule public final ConditionalIgnoreRule conditionalIgnoreRule = new ConditionalIgnoreRule();
  @Rule public final DisplayHelper displayHelper = new DisplayHelper();

  @Test
  @ConditionalIgnore( condition = GtkPlatform.class )
  public void load() {
    FontLoader fontLoader = new FontLoader( FontLoader.FONTS_DIRECTORY );

    fontLoader.load( getBundle( FontLoader.class ).getBundleContext() );

    FontData[] fontList = displayHelper.getDisplay().getFontList( "Source Code Pro", true );
    assertThat( fontList ).isNotEmpty();
  }
}