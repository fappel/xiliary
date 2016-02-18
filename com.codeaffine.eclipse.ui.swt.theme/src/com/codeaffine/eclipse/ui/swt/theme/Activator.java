package com.codeaffine.eclipse.ui.swt.theme;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

public class Activator extends AbstractUIPlugin {

  private static Activator instance;

  private final ScrollbarPreferenceApplicator preferenceApplictor;

  public Activator() {
    preferenceApplictor = new ScrollbarPreferenceApplicator();
  }

  @Override
  public void start( BundleContext context ) throws Exception {
    getPreferenceStore().addPropertyChangeListener( preferenceApplictor );
    instance = this;
  }

  @Override
  public void stop( BundleContext context ) throws Exception {
    getPreferenceStore().removePropertyChangeListener( preferenceApplictor );
    instance = null;
  }

  public static AbstractUIPlugin getInstance() {
    return instance;
  }
}