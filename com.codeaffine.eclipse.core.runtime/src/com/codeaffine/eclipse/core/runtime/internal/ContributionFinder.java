/**
 * Copyright (c) 2014 - 2022 Frank Appel
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Frank Appel - initial API and implementation
 */
package com.codeaffine.eclipse.core.runtime.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;

import com.codeaffine.eclipse.core.runtime.Extension;
import com.codeaffine.eclipse.core.runtime.FindException;

class ContributionFinder {

  static final String ERROR_ZERO_CONTRIBUTIONS
    = "Found zero contributions that matches predicate.";
  static final String ERROR_TOO_MANY_CONTRIBUTIONS
    = "Found too many contributions that match predicate.";

  private final ContributionElementLoop loop;

  ContributionFinder( IExtensionRegistry registry ) {
    loop = new ContributionElementLoop( registry );
  }

  IConfigurationElement find( String extensionPointId, Predicate<Extension> predicate ) {
    List<IConfigurationElement> contributions = findContributions( extensionPointId, predicate );
    ensureExactMatch( contributions );
    return contributions.get( 0 );
  }

  private List<IConfigurationElement> findContributions( String extensionPointId , Predicate<Extension> predicate ) {
    List<IConfigurationElement> result = new ArrayList<IConfigurationElement>();
    loop.forEach( extensionPointId, predicate, element -> result.add( element ) );
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