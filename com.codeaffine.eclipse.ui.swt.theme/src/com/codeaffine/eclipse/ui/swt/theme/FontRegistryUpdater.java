package com.codeaffine.eclipse.ui.swt.theme;

import org.eclipse.jface.resource.FontRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

/*
 * Note that this is a highly experimental workaround. It tries to solve timing
 * issues when applying the font-face in case that there are open editors on workbench
 * startup. Without this code undetermined font faces would be applied, and only
 * manually resetting the appropriate font preferences would fix the problem.
 */
class FontRegistryUpdater {

  private static final String FONT_FACE = "Source Code Pro";

  private final Listener shellOpenObserver;
  private final Display display;

  FontRegistryUpdater() {
    this.display = Display.getCurrent();
    this.shellOpenObserver = evt -> onShellShow( evt );
    if( SWT.getPlatform().startsWith( "win32" ) ) {
      Display.getCurrent().addFilter( SWT.Show, shellOpenObserver );
    }
  }

  private void onShellShow( Event evt ) {
    if( evt.widget instanceof Shell ) {
      update();
      display.removeFilter( SWT.Show, shellOpenObserver );
    }
  }

  private void update() {
    waitTillFontIsLoaded();
    updateFontEntries();
  }

  private void waitTillFontIsLoaded() {
    long timeout = System.currentTimeMillis() + 1000;
    while( !isLoaded() && timeout > System.currentTimeMillis() ) {
      display.readAndDispatch();
    }
  }

  private void updateFontEntries() {
    FontRegistry fontRegistry = PlatformUI.getWorkbench().getThemeManager().getCurrentTheme().getFontRegistry();
    updateFontEntry( display, fontRegistry, "org.eclipse.ui.workbench.texteditor.blockSelectionModeFont" );
    updateFontEntry( display, fontRegistry, "org.eclipse.jface.textfont" );
    updateFontEntry( display, fontRegistry, "org.eclipse.jdt.ui.editors.textfont" );
  }

  private static  void updateFontEntry( Display display, FontRegistry fontRegistry, String symbolicName ) {
    Font textFont = fontRegistry.get( symbolicName );
    fontRegistry.put( symbolicName, display.getSystemFont().getFontData() );
    fontRegistry.put( symbolicName, textFont.getFontData() );
  }

  private boolean isLoaded() {
    return display.getFontList( FONT_FACE, true ).length != 0;
  }
}