package com.codeaffine.eclipse.swt.test.util;

import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

public class DisplayHelper implements TestRule {

  private final Collection<Shell> capturedShells;
  private final Collection<Image> images;

  private Display display;
  private boolean displayOwner;

  public DisplayHelper() {
    capturedShells = new ArrayList<Shell>();
    capturedShells.addAll( asList( captureShells() ) );
    images = new ArrayList<Image>();
  }

  public static void flushPendingEvents() {
    EventQueueHelper.flushPendingEvents();
  }

  public Display getDisplay() {
    if( display == null ) {
      displayOwner = Display.getCurrent() == null;
      display = Display.getDefault();
    }
    return display;
  }

  public Shell[] getNewShells() {
    Collection<Shell> newShells = new ArrayList<Shell>();
    Shell[] shells = captureShells();
    for( Shell shell : shells ) {
      if( !capturedShells.contains( shell ) ) {
        newShells.add( shell );
      }
    }
    return newShells.toArray( new Shell[ newShells.size() ] );
  }

  public Shell createShell() {
    return createShell( SWT.NONE );
  }

  public Shell createShell( int style ) {
    return new Shell( getDisplay(), style );
  }

  public void ensureDisplay() {
    getDisplay();
  }

  public Image createImage( int width, int height ) {
    Image result = new Image( getDisplay(), width, height );
    images.add( result );
    return result;
  }

  public void dispose() {
    flushPendingEvents();
    disposeNewShells();
    disposeImages();
    disposeDisplay();
  }

  @Override
  public Statement apply( final Statement base, Description description ) {
    return new Statement() {
      @Override
      public void evaluate() throws Throwable {
        try {
          base.evaluate();
        } finally {
          dispose();
        }
      }
    };
  }

  private void disposeNewShells() {
    Shell[] newShells = getNewShells();
    for( Shell shell : newShells ) {
      shell.dispose();
    }
  }

  private void disposeImages() {
    for( Image image : images ) {
      image.dispose();
    }
    images.clear();
  }

  private static Shell[] captureShells() {
    Shell[] result = new Shell[ 0 ];
    Display currentDisplay = Display.getCurrent();
    if( currentDisplay != null ) {
      result = currentDisplay.getShells();
    }
    return result;
  }

  private void disposeDisplay() {
    if( display != null && displayOwner ) {
      if( !display.isDisposed() ) {
        display.dispose();
      }
      display = null;
    }
  }
}