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

import static org.assertj.core.api.Assertions.assertThat;

import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.eclipse.swt.test.util.DisplayHelper;

public class DisposalRoutingTest {

  @Rule
  public final DisplayHelper displayHelper = new DisplayHelper();

  private Composite adapter;
  private Control adaptable;

  @Before
  public void setUp() {
    Shell shell = displayHelper.createShell();
    adapter = new Composite( shell, SWT.NONE );
    adaptable = new Label( adapter, SWT.NONE );
    new DisposalRouting().register( adapter, adaptable );
  }

  @Test
  public void adaptableDisposal() {
    adaptable.dispose();

    assertThat( adapter.isDisposed() ).isTrue();
  }

  @Test
  public void adaptableDisposalWithDragSource() {
    DragSource dragSource = createDragSource();

    adaptable.dispose();

    assertThat( adapter.isDisposed() ).isTrue();
    assertThat( dragSource.isDisposed() ).isTrue();
  }

  @Test
  public void adapterDisposal() {
    adapter.dispose();

    assertThat( adaptable.isDisposed() ).isTrue();
  }

  @Test
  public void adapterDisposalWithDragSource() {
    DragSource dragSource = createDragSource();

    adapter.dispose();

    assertThat( adaptable.isDisposed() ).isTrue();
    assertThat( dragSource.isDisposed() ).isTrue();
  }

  private DragSource createDragSource() {
    return new DragSource( adaptable, DND.DROP_MOVE | DND.DROP_COPY );
  }
}