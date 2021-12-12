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

import static com.codeaffine.eclipse.swt.test.util.ShellHelper.createShell;
import static com.codeaffine.eclipse.swt.test.util.UiThreadHelper.runInOwnThreadWithReadAndDispatch;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.concurrent.atomic.AtomicBoolean;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import com.codeaffine.eclipse.swt.test.util.DisplayHelper;
import com.codeaffine.eclipse.swt.widget.action.ActionControlBuilder;

public class NavigationBarFactoryTest {

  private static final String ICON = "icon";

  @Rule
  public final DisplayHelper displayHelper = new DisplayHelper();

  private NavigationItemController controller;
  private NavigationBarFactory factory;
  private ImageProvider imageProvider;
  private NavigationItemModel model;
  private Shell parent;

  @Before
  public void setUp() {
    imageProvider = mock( ImageProvider.class );
    factory = new NavigationBarFactory( imageProvider );
    parent = createShell( displayHelper );
    model = stubModel();
    controller = stubController();
  }

  @Test
  public void create() {
    Control bar = factory
      .with( model, controller, ICON )
      .with( model, controller, ICON )
      .create( parent );

    verify( model, times( 2 ) ).getSelection();
    verify( model, times( 4 ) ).addSelectionChangedListener( any( Runnable.class ) );
    verify( controller, times( 2 ) ).getAddControlBuilder();
    verify( controller, times( 2 ) ).getRemoveControlBuilder();
    verify( controller, times( 2 ) ).getSelectControlBuilder();
    verify( imageProvider, times( 2 ) ).getImage( ICON );
    assertThat( parent.getChildren() ).hasSize( 1 );
    assertThat( bar ).isNotNull();
  }

  @Test
  public void createMultipleBars() {
    Control bar1 = factory.with( model, controller, ICON ).create( parent );
    Control bar2 = factory.with( model, controller, ICON ).create( parent );
    Control bar3 = factory.create( parent );

    verify( model, times( 2 ) ).getSelection();
    verify( model, times( 4 ) ).addSelectionChangedListener( any( Runnable.class ) );
    verify( controller, times( 2 ) ).getAddControlBuilder();
    verify( controller, times( 2 ) ).getRemoveControlBuilder();
    verify( controller, times( 2 ) ).getSelectControlBuilder();
    verify( imageProvider, times( 2 ) ).getImage( ICON );
    assertThat( parent.getChildren() ).hasSize( 3 );
    assertThat( bar1 ).isNotSameAs( bar2 );
    assertThat( bar2 ).isNotSameAs( bar3 );
    assertThat( bar1 ).isNotSameAs( bar3 );
    assertThat( bar1 ).isNotNull();
    assertThat( bar2 ).isNotNull();
    assertThat( bar3 ).isNotNull();
  }

  @Test
  public void selectionChanged() {
    Control bar = factory.with( model, controller, ICON ).create( parent );
    AtomicBoolean layoutPerformed = registerLayoutPerformedStatusRecorder( bar );

    simulateSelectionChanged( model );

    assertThat( layoutPerformed.get() ).isTrue();
  }

  @Test
  public void getActionControlImageFromImageProvider() {
    Image result = factory.getImageProvider().getImage( ActionControlImageAdapter.PLUS );

    assertThat( result ).isNotNull();
  }

  @Test
  public void dispose() {
    Image image = factory.getImageProvider().getImage( ActionControlImageAdapter.PLUS );

    factory.dispose();

    assertThat( image.isDisposed() ).isTrue();
  }

  private static AtomicBoolean registerLayoutPerformedStatusRecorder( Control navigationBar ) {
    AtomicBoolean result = new AtomicBoolean();
    ( ( Composite )navigationBar ).setLayout( new LayoutRecorder( result ) );
    return result;
  }

  private static void simulateSelectionChanged( NavigationItemModel model ) {
    ArgumentCaptor<Runnable> captor = forClass( Runnable.class );
    verify( model, times( 2 ) ).addSelectionChangedListener( captor.capture() );
    captor.getAllValues().forEach( runnable -> runInOwnThreadWithReadAndDispatch( runnable ) );
  }

  private static NavigationItemModel stubModel() {
    NavigationItemModel result = mock( NavigationItemModel.class );
    when( result.getSelection() ).thenReturn( new NavigationItemModelElement( "id", "displayName" ) );
    return result;
  }

  private static NavigationItemController stubController() {
    NavigationItemController result = mock( NavigationItemController.class );
    when( result.getAddControlBuilder() ).thenReturn( new ActionControlBuilder( () -> {} ) );
    when( result.getRemoveControlBuilder() ).thenReturn( new ActionControlBuilder( () -> {} ) );
    when( result.getSelectControlBuilder() ).thenReturn( new ActionControlBuilder( () -> {} ) );
    return result;
  }
}