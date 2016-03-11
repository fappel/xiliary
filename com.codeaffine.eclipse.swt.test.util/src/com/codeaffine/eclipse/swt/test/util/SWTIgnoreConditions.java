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
package com.codeaffine.eclipse.swt.test.util;

import org.eclipse.swt.SWT;

import com.codeaffine.test.util.junit.ConditionalIgnoreRule.IgnoreCondition;

public class SWTIgnoreConditions {

  public static class GtkPlatform implements IgnoreCondition {
    @Override
    public boolean isSatisfied() {
      return "gtk".equals( SWT.getPlatform() );
    }
  }

  public static class NonGtkPlatform implements IgnoreCondition {
    @Override
    public boolean isSatisfied() {
      return !"gtk".equals( SWT.getPlatform() );
    }
  }

  public static class CocoaPlatform implements IgnoreCondition {
    @Override
    public boolean isSatisfied() {
      return "cocoa".equals( SWT.getPlatform() );
    }
  }

  public static class NonCocoaPlatform implements IgnoreCondition {
    @Override
    public boolean isSatisfied() {
      return !"cocoa".equals( SWT.getPlatform() );
    }
  }

  public static class NonWindowsPlatform implements IgnoreCondition {
    @Override
    public boolean isSatisfied() {
      return !"win32".equals( SWT.getPlatform() );
    }
  }

  private SWTIgnoreConditions() {}
}