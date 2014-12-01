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