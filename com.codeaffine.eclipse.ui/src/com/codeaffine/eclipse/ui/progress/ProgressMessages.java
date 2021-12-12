/**
 * Copyright (c) 2014 - 2022 Frank Appel
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Frank Appel - initial API and implementation
 */
package com.codeaffine.eclipse.ui.progress;

import org.eclipse.osgi.util.NLS;

public class ProgressMessages extends NLS {

  private static final Class<ProgressMessages> CLASS = ProgressMessages.class;
  private static final String BUNDLE_NAME = CLASS.getPackage().getName() +".messages";

  public static String DeferredTreeContentManager_AddingChildren;
  public static String DeferredTreeContentManager_NotDeferred;
  public static String DeferredTreeContentManager_ClearJob;
  public static String DeferredTreeContentManager_FetchingName;

  static {
    NLS.initializeMessages( BUNDLE_NAME, CLASS );
  }
}