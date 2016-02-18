package com.codeaffine.eclipse.ui.swt.theme;

import static com.codeaffine.eclipse.ui.swt.theme.FontLoader.FONTS_DIRECTORY;
import static org.osgi.framework.FrameworkUtil.getBundle;

import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.PlatformUI;
import org.osgi.framework.BundleContext;

public class StartupFontLoader implements IStartup {

  @Override
  public void earlyStartup() {
    Display display = PlatformUI.getWorkbench().getDisplay();
    display.asyncExec( () -> loadFonts() );
  }

  private static void loadFonts() {
    new FontLoader( FONTS_DIRECTORY ).load( getBundleContext() );
  }

  private static BundleContext getBundleContext() {
    return getBundle( StartupFontLoader.class ).getBundleContext();
  }
}