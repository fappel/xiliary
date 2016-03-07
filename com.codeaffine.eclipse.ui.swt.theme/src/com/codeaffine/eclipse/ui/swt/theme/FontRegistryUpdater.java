/**
 * Copyright (c) 2014 - 2016 Frank Appel
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Frank Appel - initial API and implementation
 */
package com.codeaffine.eclipse.ui.swt.theme;

import static com.codeaffine.eclipse.ui.swt.theme.FontLoader.FONT_FACE;

import org.eclipse.jface.resource.FontRegistry;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

/*
 * Note that this is a highly experimental workaround. It tries to solve timing
 * issues when applying the font-face in case that there are open editors on workbench
 * startup. Without this code undetermined font faces would be applied, and only
 * manually resetting the appropriate font preferences would fix the problem.
 */
class FontRegistryUpdater {

  public void update( Shell shell ) {
    shell.getDisplay().asyncExec( () -> {
      shell.setRedraw( false );
      try {
        updateFontEntries( shell.getDisplay() );
      } finally {
        shell.setRedraw( true );
      }
    } );
  }

  private static void updateFontEntries( Display display ) {
    FontRegistry fontRegistry = PlatformUI.getWorkbench().getThemeManager().getCurrentTheme().getFontRegistry();
    if( fontRegistry.getFontData( "org.eclipse.jface.textfont" )[ 0 ].getName().equals( FONT_FACE ) ) {
      updateFontEntry( display, fontRegistry, "org.eclipse.ui.workbench.texteditor.blockSelectionModeFont" );
      updateFontEntry( display, fontRegistry, "org.eclipse.jface.textfont" );
      updateFontEntry( display, fontRegistry, "org.eclipse.jdt.ui.editors.textfont" );
    }
  }

  private static void updateFontEntry( Display display, FontRegistry fontRegistry, String symbolicName ) {
    Font textFont = fontRegistry.get( symbolicName );
    fontRegistry.put( symbolicName, display.getSystemFont().getFontData() );
    display.readAndDispatch();
    fontRegistry.put( symbolicName, textFont.getFontData() );
    display.readAndDispatch();
  }
}