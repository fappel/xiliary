package com.codeaffine.eclipse.ui.swt.theme;

import static com.codeaffine.eclipse.ui.swt.theme.ScrollableAdapterContribution.ADAPTER_DEMEANOR;
import static com.codeaffine.eclipse.ui.swt.theme.ScrollableAdapterContribution.FLAT_SCROLL_BAR_INCREMENT_LENGTH;
import static com.codeaffine.eclipse.ui.swt.theme.ScrollbarPreferencesInitializer.UNSET;

import java.util.concurrent.CopyOnWriteArraySet;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.junit.rules.ExternalResource;

public class ScrollbarPreferenceRule extends ExternalResource {

  private final CopyOnWriteArraySet<IPropertyChangeListener> listeners;

  private IPreferenceStore preferenceStore;

  public ScrollbarPreferenceRule() {
    listeners = new CopyOnWriteArraySet<>();
  }

  void setValue( String name, String value ) {
    preferenceStore.setValue( name, value );
  }

  void addListener( IPropertyChangeListener listener ) {
    preferenceStore.addPropertyChangeListener( listener );
    listeners.add( listener );
  }

  @Override
  protected void before() throws Throwable {
    preferenceStore = Activator.getInstance().getPreferenceStore();
  }

  @Override
  protected void after() {
    listeners.forEach( listener -> preferenceStore.removePropertyChangeListener( listener ) );
    preferenceStore.setValue( FLAT_SCROLL_BAR_INCREMENT_LENGTH, UNSET );
    preferenceStore.setValue( ADAPTER_DEMEANOR, UNSET );
  }
}