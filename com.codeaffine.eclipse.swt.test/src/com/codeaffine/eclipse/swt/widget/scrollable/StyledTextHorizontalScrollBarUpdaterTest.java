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
package com.codeaffine.eclipse.swt.widget.scrollable;

import static com.codeaffine.eclipse.swt.test.util.ShellHelper.createShell;
import static com.codeaffine.eclipse.swt.testhelper.LoremIpsum.PARAGRAPHS;
import static com.codeaffine.eclipse.swt.widget.scrollbar.FlatScrollBarAssert.assertThat;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Shell;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.eclipse.swt.test.util.DisplayHelper;
import com.codeaffine.eclipse.swt.test.util.SWTIgnoreConditions.NonWindowsPlatform;
import com.codeaffine.eclipse.swt.widget.scrollbar.FlatScrollBar;
import com.codeaffine.test.util.junit.ConditionalIgnoreRule;
import com.codeaffine.test.util.junit.ConditionalIgnoreRule.ConditionalIgnore;

public class StyledTextHorizontalScrollBarUpdaterTest {

  @Rule public final ConditionalIgnoreRule conditionalIgnoreRule = new ConditionalIgnoreRule();
  @Rule public final DisplayHelper displayHelper = new DisplayHelper();

  @Test
  @ConditionalIgnore( condition = NonWindowsPlatform.class )
  @Ignore( "Fix assert for windows server headless build" )
  public void update() {
    Shell shell = createShell( displayHelper );
    StyledText styledText = new StyledText( shell, SWT.H_SCROLL | SWT.V_SCROLL );
    styledText.setText( PARAGRAPHS );
    FlatScrollBar scrollBar = new FlatScrollBar( shell, SWT.HORIZONTAL );
    ScrollBarUpdater updater = new StyledTextHorizontalScrollBarUpdater( styledText, scrollBar );
    shell.layout();

    updater.update();

    assertThat( scrollBar )
      .hasThumb( 78 )
      .hasPageIncrement( 78 )
      .hasSelection( 0 )
      .hasMaximum( 550 )
      .hasMinimum( 0 )
      .hasIncrement( 8 );
  }
}