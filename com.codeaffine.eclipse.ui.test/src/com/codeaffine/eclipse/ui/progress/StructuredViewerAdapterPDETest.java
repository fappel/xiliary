package com.codeaffine.eclipse.ui.progress;

import static com.codeaffine.eclipse.swt.test.util.ShellHelper.createShell;
import static org.assertj.core.api.Assertions.assertThat;

import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Shell;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.eclipse.swt.test.util.DisplayHelper;

public class StructuredViewerAdapterPDETest {

  @Rule
  public final DisplayHelper displayHelper = new DisplayHelper();

  @Test
  public void isDisposed() {
    Shell shell = createShell( displayHelper );
    StructuredViewer structuredViewer = new TreeViewer( shell );
    StructuredViewerAdapter<StructuredViewer> adapter = new TestStructuredViewerAdapter( structuredViewer );
    structuredViewer.getControl().dispose();

    boolean actual = adapter.isDisposed();

    assertThat( actual ).isTrue();
  }
}