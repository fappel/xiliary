package com.codeaffine.eclipse.ui.swt.theme;

import static java.nio.file.Files.copy;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static java.util.Collections.emptyMap;
import static java.util.Collections.list;
import static org.eclipse.core.runtime.FileLocator.find;
import static org.osgi.service.log.LogService.LOG_ERROR;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.util.Enumeration;

import org.eclipse.core.runtime.FileLocator;
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
    if( fontPath.endsWith( ".ttf" ) ) {
      URL url = computeFontUrl( find( context.getBundle(), new Path( fontPath ), emptyMap() ) );
      File diskLocation = getDiskLocation( context, fontPath );
      copyToDisk( url, diskLocation );
      display.asyncExec( () -> display.loadFont( diskLocation.toString() ) );
    }
  }

  private static File getDiskLocation( BundleContext context, String fontPath ) {
    try {
      return context.getBundle().getDataFile( fontPath ).getCanonicalFile();
    } catch( IOException shouldNotHappen ) {
      throw new IllegalStateException( shouldNotHappen );
    }
  }

  private static URL computeFontUrl( URL url ) {
    try {
      return FileLocator.toFileURL( url );
    } catch( IOException shouldNotHappen ) {
      throw new IllegalStateException( shouldNotHappen );
    }
  }

  private static void copyToDisk( URL url, File diskLocation ) {
    try( InputStream input = url.openStream() ) {
      ensureDirectoryExists( diskLocation.toPath() );
      copy( input, diskLocation.toPath(), REPLACE_EXISTING );
    } catch (IOException shouldNotHappen ) {
      throw new IllegalStateException( shouldNotHappen );
    }
  }

  private static void ensureDirectoryExists( java.nio.file.Path path ) throws IOException {
    if( !Files.exists( path.getParent() ) ) {
      Files.createDirectories( path.getParent() );
    }
  }
}