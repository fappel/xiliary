package com.codeaffine.eclipse.ui.swt.theme;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;

import org.eclipse.core.runtime.IPath;
import org.eclipse.swt.graphics.FontData;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.eclipse.swt.test.util.DisplayHelper;
import com.codeaffine.eclipse.swt.test.util.SWTIgnoreConditions.GtkPlatform;
import com.codeaffine.test.util.junit.AwaitConditionRule;
import com.codeaffine.test.util.junit.AwaitConditionRule.AwaitCondition;
import com.codeaffine.test.util.junit.AwaitConditionRule.AwaitConditionDeclaration;
import com.codeaffine.test.util.junit.ConditionalIgnoreRule;
import com.codeaffine.test.util.junit.ConditionalIgnoreRule.ConditionalIgnore;

public class FontLoaderPDETest {

  @Rule public final ConditionalIgnoreRule conditionalIgnoreRule = new ConditionalIgnoreRule();
  @Rule public final AwaitConditionRule awaitConditionRule = new AwaitConditionRule();
  @Rule public final DisplayHelper displayHelper = new DisplayHelper();

  static class FontBuffering implements AwaitCondition {

    @Override
    public boolean isSatisfied() {
      IPath stateLocation = Activator.getInstance().getStateLocation();
      File dataFile = stateLocation.append( FontLoader.FONTS_DIRECTORY ).toFile();
      return dataFile.listFiles().length == 14;
    }
  }

  @Test
  @ConditionalIgnore( condition = GtkPlatform.class )
  @AwaitConditionDeclaration( timeout = 1000, condition = FontBuffering.class )
  public void load() {
    assertThat( getFontList( FontLoader.FONT_FACE ) ).isNotEmpty();
  }

  private FontData[] getFontList( String faceName ) {
    return displayHelper.getDisplay().getFontList( faceName, true );
  }
}