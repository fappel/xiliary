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
package com.codeaffine.eclipse.swt.widget.scrollbar;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;

public class DirectionHelper {

  static Direction stubSizeComputation(
    Composite composite, Point returnValue, int wHint, int hHint, boolean flushCache )
  {
    Direction result = mock( Direction.class );
    when( result.computeSize( composite, wHint, hHint, flushCache ) ).thenReturn( returnValue );
    return result;
  }

  public static Collection<Object[]> valuesForParameterizedTests() {
    Collection<Object[]> result = new ArrayList<Object[]>();
    result.add( new Object[] { SWT.HORIZONTAL } );
    result.add( new Object[] { SWT.VERTICAL } );
    return result;
  }
}