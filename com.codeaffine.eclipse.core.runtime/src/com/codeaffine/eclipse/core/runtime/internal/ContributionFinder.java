package com.codeaffine.eclipse.core.runtime.internal;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;

import com.codeaffine.eclipse.core.runtime.Predicate;
import com.codeaffine.eclipse.core.runtime.FindException;
import com.codeaffine.eclipse.core.runtime.internal.ContributionElementLoop.ConfigurationElementHandler;

class ContributionFinder {

  static final String ERROR_ZERO_CONTRIBUTIONS
    = "Found zero contributions that matches predicate.";
  static final String ERROR_TOO_MANY_CONTRIBUTIONS
    = "Found too many contributions that match predicate.";

  private final ContributionElementLoop loop;

  ContributionFinder( IExtensionRegistry registry ) {
    loop = new ContributionElementLoop( registry );
  }

  IConfigurationElement find( String extensionPointId, Predicate predicate ) {
    List<IConfigurationElement> contributions = findContributions( extensionPointId, predicate );
    ensureExactMatch( contributions );
    return contributions.get( 0 );
  }

  private List<IConfigurationElement> findContributions(
    String extensionPointId , Predicate predicate )
  {
    final List<IConfigurationElement> result = new ArrayList<IConfigurationElement>();
    loop.forEach( extensionPointId, predicate, new ConfigurationElementHandler() {
      @Override
      public void handle( IConfigurationElement element ) {
        result.add( element );
      }
    } );
    return result;
  }

  private static void ensureExactMatch( List<IConfigurationElement> result ) {
    if( result.isEmpty() ) {
      throw new FindException( ERROR_ZERO_CONTRIBUTIONS );
    }
    if( result.size() > 1 ) {
      throw new FindException( ERROR_TOO_MANY_CONTRIBUTIONS );
    }
  }
}