package com.codeaffine.eclipse.ui.swt.theme;

import static java.util.Collections.emptyMap;
import static java.util.Collections.list;
import static org.eclipse.core.runtime.FileLocator.find;
import static org.eclipse.core.runtime.FileLocator.toFileURL;
import static org.osgi.service.log.LogService.LOG_ERROR;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;

import org.eclipse.core.runtime.Path;
import org.eclipse.swt.widgets.Display;
import org.osgi.framework.BundleContext;
import org.osgi.service.log.LogService;
import org.osgi.util.tracker.ServiceTracker;

class FontLoader {

  static final String FONTS_DIRECTORY = "/fonts";

  private final String fontDirectory;
  private final Display display;

  FontLoader( String fontDirectory ) {
    this.fontDirectory = fontDirectory;
    this.display = Display.getCurrent();
  }

  void load( BundleContext context ) {
    try {
      doLoad( context );
    } catch( RuntimeException rte ) {
      getLogService( context ).log( LOG_ERROR, "Unable to load clean sheet fonts.", rte );
    }
  }

  private void doLoad( BundleContext context ) {
    list( getFontPaths( context, fontDirectory ) )
      .forEach( fontPath -> loadFont( context, fontPath ) );
  }

  private static LogService getLogService( BundleContext context ) {
    ServiceTracker<Object, Object> tracker = new ServiceTracker<>( context, LogService.class.getName(), null );
    tracker.open();
    LogService result = ( LogService )tracker.getService();
    tracker.close();
    return result;
  }

  private static Enumeration<String> getFontPaths( BundleContext context, String fontDirectory ) {
    return context.getBundle().getEntryPaths( fontDirectory );
  }

  private void loadFont( BundleContext context, String fontPath ) {
    URL url = find( context.getBundle(), new Path( fontPath ), emptyMap() );
    URL fontUrl = computeFontUrl( url );
    display.asyncExec( () -> display.loadFont( new File( fontUrl.getPath() ).toString() ) );
  }

  private static URL computeFontUrl( URL url ) {
    try {
      return toFileURL( url );
    } catch( IOException shouldNotHappen ) {
      throw new IllegalStateException( shouldNotHappen );
    }
  }
}