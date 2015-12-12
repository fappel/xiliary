package com.codeaffine.eclipse.ui.swt.theme;

import static com.codeaffine.eclipse.core.runtime.Predicates.attribute;
import static com.codeaffine.eclipse.ui.swt.theme.ScrollableAdapterContribution.ADAPTER_DEMEANOR;
import static com.codeaffine.eclipse.ui.swt.theme.ScrollableAdapterContribution.FLAT_SCROLL_BAR_INCREMENT_LENGTH;
import static com.codeaffine.eclipse.ui.swt.theme.ScrollbarPreferencesInitializer.UNSET;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.junit.Test;

import com.codeaffine.eclipse.core.runtime.RegistryAdapter;

public class ScrollbarPreferencesInitializerPDETest {

  private static final String EXTENION_POINT_ID = "org.eclipse.core.runtime.preferences";
  private static final Class<ScrollbarPreferencesInitializer> INITIALIZER_TYPE
    = ScrollbarPreferencesInitializer.class;

  @Test
  public void creation() {
    ScrollbarPreferencesInitializer actual = createExtension( EXTENION_POINT_ID, INITIALIZER_TYPE );

    assertThat( actual ).isNotNull();
  }

  @Test
  public void initialization() {
    IPreferenceStore store = mock( IPreferenceStore.class );
    ScrollbarPreferencesInitializer initializer = new ScrollbarPreferencesInitializer( store );

    initializer.initializeDefaultPreferences();

    verify( store ).setDefault( ADAPTER_DEMEANOR, UNSET );
    verify( store ).setDefault( FLAT_SCROLL_BAR_INCREMENT_LENGTH, UNSET );
  }

  private static <T extends AbstractPreferenceInitializer> T createExtension(
    String extensionPointId , Class<T> initializerType  )
  {
    return initializerType.cast( new RegistryAdapter()
      .createExecutableExtension( extensionPointId, AbstractPreferenceInitializer.class )
      .thatMatches( attribute( "class", initializerType.getName() ) )
      .process() );
  }
}