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
package com.codeaffine.eclipse.swt.widget.scrollable;

import org.eclipse.swt.graphics.Color;

public interface ScrollbarStyle {
  void setIncrementButtonLength( int length );
  int getIncrementButtonLength();
  void setIncrementColor( Color color );
  Color getIncrementColor();
  void setPageIncrementColor( Color color );
  Color getPageIncrementColor();
  void setThumbColor( Color color );
  Color getThumbColor();
  void setBackgroundColor( Color color );
  Color getBackgroundColor();
  void setDemeanor( Demeanor demeanor );
  Demeanor getDemeanor();
}