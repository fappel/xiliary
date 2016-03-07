package com.codeaffine.eclipse.ui.swt.theme;

import static com.codeaffine.eclipse.core.runtime.Predicates.attribute;
import static org.assertj.core.api.Assertions.assertThat;

import org.eclipse.ui.IStartup;
import org.junit.Test;

import com.codeaffine.eclipse.core.runtime.RegistryAdapter;

public class FontOnStartupLoaderPDETest {

  @Test
  public void extensionRegistration() {
    IStartup actual = new RegistryAdapter()
      .createExecutableExtension( "org.eclipse.ui.startup", IStartup.class )
      .thatMatches( attribute( "class", FontOnStartupLoader.class.getName() ) )
      .process();

    assertThat( actual ).isInstanceOf( FontOnStartupLoader.class );
  }
}