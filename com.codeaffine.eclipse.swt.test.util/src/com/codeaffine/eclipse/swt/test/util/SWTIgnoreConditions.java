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

  public static class CocoaPlatform implements IgnoreCondition {
    @Override
    public boolean isSatisfied() {
      return "cocoa".equals( SWT.getPlatform() );
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