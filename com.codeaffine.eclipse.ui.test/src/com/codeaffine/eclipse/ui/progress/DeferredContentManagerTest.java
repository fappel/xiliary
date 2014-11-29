package com.codeaffine.eclipse.ui.progress;

import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.eclipse.swt.test.util.DisplayHelper;
import com.codeaffine.eclipse.swt.test.util.ShellHelper;

public class DeferredContentManagerTest {

  @Rule
  public final DisplayHelper displayHelper = new DisplayHelper();

  @Test
  public void name() {
    ShellHelper.createShell( displayHelper );
  }
}