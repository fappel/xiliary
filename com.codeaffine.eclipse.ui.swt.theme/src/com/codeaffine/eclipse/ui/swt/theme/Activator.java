package com.codeaffine.eclipse.ui.swt.theme;

import static com.codeaffine.eclipse.ui.swt.theme.FontLoader.FONTS_DIRECTORY;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

public class Activator extends AbstractUIPlugin {

  private static Activator instance;

  private final ScrollbarPreferenceApplicator preferenceApplictor;
  private final FontLoader fontLoader;


  public Activator() {
    fontLoader = new FontLoader( FONTS_DIRECTORY );
    preferenceApplictor = new ScrollbarPreferenceApplicator();
  }

  @Override
  public void start( BundleContext context ) throws Exception {
    new Thread( () -> fontLoader.load( context ) ).start();
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