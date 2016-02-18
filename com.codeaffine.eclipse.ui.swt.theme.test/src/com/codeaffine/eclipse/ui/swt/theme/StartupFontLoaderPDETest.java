package com.codeaffine.eclipse.ui.swt.theme;

import static com.codeaffine.eclipse.core.runtime.Predicates.attribute;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import com.codeaffine.eclipse.core.runtime.Extension;
import com.codeaffine.eclipse.core.runtime.RegistryAdapter;

public class StartupFontLoaderPDETest {

  @Test
  public void extensionRegistration() {
    Extension extension = new RegistryAdapter()
      .readExtension( "org.eclipse.ui.startup" )
      .thatMatches( attribute( "class", "com.codeaffine.eclipse.ui.swt.theme.StartupFontLoader" ) )
      .process();

    assertThat( extension ).isNotNull();
  }
}