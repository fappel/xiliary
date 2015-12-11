package com.codeaffine.eclipse.ui.swt.theme;

import static com.codeaffine.eclipse.ui.swt.theme.FontLoader.FONTS_DIRECTORY;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;


public class Activator implements BundleActivator {

  private final FontLoader fontLoader;

  public Activator() {
    fontLoader = new FontLoader( FONTS_DIRECTORY );
  }

  @Override
  public void start( BundleContext context ) throws Exception {
    new Thread( () -> fontLoader.load( context ) ).start();
  }

  @Override
  public void stop( BundleContext context ) throws Exception {
  }
}