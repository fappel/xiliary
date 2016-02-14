package org.eclipse.jface.text.source;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Layout;

public class SourceViewer {

  public static class RulerLayout extends Layout {

    @Override
    protected Point computeSize( Composite composite, int wHint, int hHint, boolean flushCache ) {
      return null;
    }

    @Override
    protected void layout( Composite composite, boolean flushCache ) {
    }
  }
}