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
package com.codeaffine.eclipse.swt.widget.action;

import static com.codeaffine.eclipse.swt.test.util.ShellHelper.createShell;
import static com.codeaffine.eclipse.swt.test.util.graphics.PointAssert.assertThat;
import static com.codeaffine.eclipse.swt.widget.action.EnablementHelper.configureAsDisabled;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import java.util.function.BooleanSupplier;
import java.util.function.Function;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.eclipse.swt.test.util.DisplayHelper;

public class ActionControlBuilderTest {

  @Rule
  public final DisplayHelper displayHelper = new DisplayHelper();

  private Updatable updatable;
  private Shell parent;

  @Before
  public void setUp() {
    parent = createShell( displayHelper );
  }

  @Test
  public void buildWithRunnable() {
    Runnable runnable = mock( Runnable.class );

    ActionControlBuilder builder = new ActionControlBuilder( runnable );
    Control actual = builder.build( parent );

    assertThat( actual.getMenu() ).isNull();
    assertThat( builder.getAction() ).isSameAs( runnable );
    assertThat( builder.getMenuCreator() ).isNull();
  }

  @Test
  public void buildWithMenuCreator() {
    Function<Control, Menu> menuCreator = control -> new Menu( control );

    ActionControlBuilder builder = new ActionControlBuilder( menuCreator );
    Control actual = builder.build( parent );

    assertThat( actual.getMenu() ).isNotNull();
    assertThat( builder.getAction() ).isNull();
    assertThat( builder.getMenuCreator() ).isSameAs( menuCreator );
  }

  @Test
  public void buildEmptyPlaceholder() {
    ActionControlBuilder builder = new ActionControlBuilder();

    Control actual = builder.build( parent );
    Point preferredSize = actual.computeSize( SWT.DEFAULT, SWT.DEFAULT, true );

    assertThat( preferredSize ).isEqualToPointOf( 0, 0 );
  }

  @Test
  public void buildWithImage() {
    Runnable runnable = mock( Runnable.class );
    Image expected = createImage();

    Label actual = ( Label )new ActionControlBuilder( runnable ).withImage( expected ).build( parent );

    assertThat( actual.getImage() ).isSameAs( expected );
  }

  @Test
  public void buildIfDisabled() {
    Runnable runnable = mock( Runnable.class );
    Image enabledImage = createImage();

    Label actual = ( Label )new ActionControlBuilder( runnable )
      .withImage( enabledImage )
      .withEnablement( () -> false )
      .build( parent );

    assertThat( actual.getImage() ).isNotSameAs( enabledImage );
  }

  @Test
  public void buildAndDisable() {
    BooleanSupplier enablement = mock( BooleanSupplier.class );
    Runnable runnable = mock( Runnable.class );
    Image enabledImage = createImage();

    Label actual = ( Label )new ActionControlBuilder( runnable )
        .withImage( enabledImage )
        .withEnablement( enablement )
        .withUpdateTrigger( updatable -> this.updatable = updatable )
        .build( parent );
    configureAsDisabled( enablement );
    updatable.update();

    assertThat( actual.getImage() ).isNotSameAs( enabledImage );
  }

  private Image createImage() {
    return displayHelper.createImage( 1, 1 );
  }
}