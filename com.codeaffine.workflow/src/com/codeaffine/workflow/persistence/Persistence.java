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
package com.codeaffine.workflow.persistence;

import java.io.InputStream;
import java.io.OutputStream;

public interface Persistence {

  Object serialize( Memento memento );
  void serialize( Memento memento, OutputStream out );

  Memento deserialize( Object data );
  Memento deserialize( InputStream in );
}
