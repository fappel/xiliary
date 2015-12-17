package com.codeaffine.eclipse.ui.swt.theme;

import static com.codeaffine.eclipse.ui.swt.theme.ScrollableAdapterContribution.ADAPTER_DEMEANOR;
import static com.codeaffine.eclipse.ui.swt.theme.ScrollableAdapterContribution.FLAT_SCROLL_BAR_INCREMENT_LENGTH;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

public class ScrollbarPreferencesInitializer extends AbstractPreferenceInitializer {

  static final String UNSET = "unset";

  private final IPreferenceStore preferenceStore;

  public ScrollbarPreferencesInitializer() {
    this( Activator.getInstance().getPreferenceStore() );
  }

  ScrollbarPreferencesInitializer( IPreferenceStore preferenceStore ) {
    this.preferenceStore = preferenceStore;
  }

  @Override
  public void initializeDefaultPreferences() {
    preferenceStore.setDefault( ADAPTER_DEMEANOR, UNSET );
    preferenceStore.setDefault( FLAT_SCROLL_BAR_INCREMENT_LENGTH, UNSET );
  }
}