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
package com.codeaffine.eclipse.ui.swt.theme;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.eclipse.swt.test.util.DisplayHelper;
import com.codeaffine.eclipse.swt.test.util.SWTIgnoreConditions.GtkPlatform;
import com.codeaffine.eclipse.swt.widget.scrollable.ScrollableAdapterFactory;
import com.codeaffine.eclipse.swt.widget.scrollable.ScrollbarStyle;
import com.codeaffine.eclipse.swt.widget.scrollable.TreeAdapter;
import com.codeaffine.test.util.junit.ConditionalIgnoreRule;
import com.codeaffine.test.util.junit.ConditionalIgnoreRule.ConditionalIgnore;

public class ScrollbarStyleCollectorTest {

  @Rule public final ConditionalIgnoreRule conditionalIgnoreRule = new ConditionalIgnoreRule();
  @Rule public final DisplayHelper displayHelper = new DisplayHelper();

  private ScrollbarStyle style1;
  private ScrollbarStyle style2;

  @Before
  public void setUp() {
    ScrollableAdapterFactory factory = new ScrollableAdapterFactory();
    Shell shell = displayHelper.createShell();
    style1 = factory.create( new Tree( shell, SWT.NONE ), TreeAdapter.class ).get();
    Composite composite = new Composite( shell, SWT.NONE );
    style2 = factory.create( new Tree( composite, SWT.NONE ), TreeAdapter.class ).get();
  }

  @Test
  @ConditionalIgnore( condition = GtkPlatform.class )
  public void collect() {
    Collection<ScrollbarStyle> actual = new ScrollbarStyleCollector().collect();

    assertThat( actual ).containsOnly( style1, style2 );
  }
}