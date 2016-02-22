/**
 * Copyright (c) 2014 - 2016 Frank Appel
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Frank Appel - initial API and implementation
 */
package com.codeaffine.workflow.test.util;

import com.codeaffine.workflow.persistence.ClassFinder;

public class TestClassFinder implements ClassFinder {

  @Override
  public Class<?> find( String className ) {
    try {
      return getClass().getClassLoader().loadClass( className );
    } catch( ClassNotFoundException cnfe ) {
      throw new RuntimeException( cnfe );
    }
  }
}