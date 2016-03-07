package com.codeaffine.eclipse.ui.swt.theme;

import static com.codeaffine.eclipse.ui.swt.theme.FontLoader.FONTS_DIRECTORY;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.PlatformUI;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;

public class FontOnStartupLoader implements IStartup {

  private final FontRegistryUpdater fontRegistryUpdater;
  private final FontLoader fontLoader;

  public FontOnStartupLoader() {
    fontLoader = new FontLoader( FONTS_DIRECTORY );
    fontRegistryUpdater = new FontRegistryUpdater();
  }

  @Override
  public void earlyStartup() {
    waitTillWorkbenchWindowExists();
    if( isPlatformSupported() ) {
      loadFont( getShell() );
    }
  }

  private static void waitTillWorkbenchWindowExists() {
    long timeout = System.currentTimeMillis() + 1000;
    while( PlatformUI.getWorkbench().getWorkbenchWindows().length == 0 && timeout > System.currentTimeMillis() ) {
      try {
        Thread.sleep( 50 );
      } catch( InterruptedException shouldNotHappen ) {
        throw new IllegalStateException( shouldNotHappen );
      }
    }
  }

  private static boolean isPlatformSupported() {
    return SWT.getPlatform().startsWith( "win32" ) || SWT.getPlatform().startsWith( "cocoa" );
  }

  private static Shell getShell() {
    return PlatformUI.getWorkbench().getWorkbenchWindows()[ 0 ].getShell();
  }

  private void loadFont( Shell shell ) {
    BundleContext context = FrameworkUtil.getBundle( FontOnStartupLoader.class ).getBundleContext();
    fontLoader.load( context, shell.getDisplay() );
    fontRegistryUpdater.update( shell );
  }
}
