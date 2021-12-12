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
package com.codeaffine.eclipse.swt.widget.navigationbar;

import static com.codeaffine.util.ArgumentVerification.verifyNotNull;

public class NavigationItemModelElement {

  private final String displayName;
  private final String id;

  public NavigationItemModelElement( String id, String displayName ) {
    verifyNotNull( displayName, "displayName" );
    verifyNotNull( id, "id" );

    this.displayName = displayName;
    this.id = id;
  }

  public String getId() {
    return id;
  }

  public String getDisplayName() {
    return displayName;
  }
}