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

import static com.codeaffine.eclipse.ui.swt.theme.FontLoader.FONTS_DIRECTORY;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

public class Activator extends AbstractUIPlugin {

  private static Activator instance;

  private final ScrollbarPreferenceApplicator preferenceApplictor;
  private final FontLoader fontLoader;

  public Activator() {
    fontLoader = new FontLoader( FONTS_DIRECTORY );
    new FontRegistryUpdater();
    preferenceApplictor = new ScrollbarPreferenceApplicator();
  }

  @Override
  public void start( BundleContext context ) throws Exception {
    instance = this;
    new Thread( () -> fontLoader.load( context ) ).start();
    getPreferenceStore().addPropertyChangeListener( preferenceApplictor );
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