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
package com.codeaffine.eclipse.swt.widget.navigationbar;

import static com.codeaffine.eclipse.swt.test.util.ShellHelper.createShell;
import static com.codeaffine.eclipse.swt.test.util.UiThreadHelper.runInOwnThreadWithReadAndDispatch;
import static com.codeaffine.eclipse.swt.widget.navigationbar.ActionControlImageAdapter.ARROW_DOWN;
import static com.codeaffine.eclipse.swt.widget.navigationbar.ActionControlImageAdapter.MINUS;
import static com.codeaffine.eclipse.swt.widget.navigationbar.ActionControlImageAdapter.PLUS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.stream.Stream;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import com.codeaffine.eclipse.swt.test.util.DisplayHelper;
import com.codeaffine.eclipse.swt.widget.action.ActionControlBuilder;

public class NavigationItemTest {

  public final DisplayHelper displayHelper = new DisplayHelper();

  private static final ActionControlBuilder SELECT_CONTROL_BUILDER = createActionControlBuilderSpy();
  private static final ActionControlBuilder ADD_CONTROL_BUILDER = createActionControlBuilderSpy();
  private static final ActionControlBuilder REMOVE_CONTROL_BUILDER = createActionControlBuilderSpy();
  private static final NavigationItemModelElement SELECTION = new NavigationItemModelElement( "id", "displayName" );
  private static final String ICON_NAME = "iconName";

  private NavigationItemController controller;
  private NavigationItem navigationItem;
  private ImageProvider imageProvider;
  private Map<String, Image> images;
  private NavigationItemModel model;
  private Shell parent;

  @Before
  public void setUp() {
    images = new HashMap<>();
    parent = createShell( displayHelper );
    imageProvider = stubImageProvider( ICON_NAME, ARROW_DOWN, MINUS, PLUS );
    model = stubNavigationItemModel( SELECTION );
    controller = stubController();
    navigationItem = new NavigationItem( imageProvider, model, controller, ICON_NAME );
  }

  @After
  public void tearDown() {
    images.values().forEach( image -> image.dispose() );
  }

  @Test
  public void createControl() {
    navigationItem.createControl( parent );

    verify( imageProvider ).getImage( ICON_NAME );
    verify( SELECT_CONTROL_BUILDER ).withImage( images.get( ARROW_DOWN ) );
    verify( SELECT_CONTROL_BUILDER ).build( any( Composite.class ) );
    verify( REMOVE_CONTROL_BUILDER ).withImage( images.get( MINUS ) );
    verify( REMOVE_CONTROL_BUILDER ).build( any( Composite.class ) );
    verify( ADD_CONTROL_BUILDER ).withImage( images.get( PLUS ) );
    verify( ADD_CONTROL_BUILDER ).build( any( Composite.class ) );
    verify( model ).addSelectionChangedListener( any( Runnable.class ) );
    assertThat( navigationItem.getSelectionText() ).isEqualTo( SELECTION.getDisplayName() );
    assertThat( parent.getChildren() ).hasSize( 1 );
  }

  @Test
  public void selectionChanged() {
    NavigationItemModelElement expected = new NavigationItemModelElement( "newId", "newSelection" );
    navigationItem.createControl( parent );
    equipWithSelection( model, expected );

    runInOwnThreadWithReadAndDispatch( captureSelectionChangedListener() );

    assertThat( navigationItem.getSelectionText() ).isEqualTo( expected.getDisplayName() );
  }

  @Test
  public void dispose() {
    navigationItem.createControl( parent );
    Runnable listener = captureSelectionChangedListener();

    parent.dispose();

    verify( model ).removeSelectionChangedListener( listener );
    verify( model ).dispose();
  }

  private Runnable captureSelectionChangedListener() {
    ArgumentCaptor<Runnable> captor = forClass( Runnable.class );
    verify( model ).addSelectionChangedListener( captor.capture() );
    return captor.getValue();
  }

  private ImageProvider stubImageProvider( String ... imageNames ) {
    ImageProvider result = mock( ImageProvider.class );
    Stream.of( imageNames ).forEach( imageName -> equipWithImage( result, imageName ) );
    return result;
  }

  private void equipWithImage( ImageProvider imageProvider, String iconName ) {
    Image image = new Image( displayHelper.getDisplay(), 1, 1 );
    images.put( iconName, image );
    when( imageProvider.getImage( iconName ) ).thenReturn( image );
  }

  private static NavigationItemController stubController() {
    NavigationItemController result = mock( NavigationItemController.class );
    when( result.getAddControlBuilder() ).thenReturn( ADD_CONTROL_BUILDER );
    when( result.getRemoveControlBuilder() ).thenReturn( REMOVE_CONTROL_BUILDER );
    when( result.getSelectControlBuilder() ).thenReturn( SELECT_CONTROL_BUILDER );
    return result;
  }

  private static NavigationItemModel stubNavigationItemModel( NavigationItemModelElement selection ) {
    NavigationItemModel result = mock( NavigationItemModel.class );
    equipWithSelection( result, selection );
    return result;
  }

  private static void equipWithSelection( NavigationItemModel model, NavigationItemModelElement selection ) {
    when( model.getSelection() ).thenReturn( selection );
  }

  @SuppressWarnings("unchecked")
  private static ActionControlBuilder createActionControlBuilderSpy() {
    ActionControlBuilder result = spy( new ActionControlBuilder( mock( Runnable.class ) ) );
    when( result.withEnablement( any( BooleanSupplier.class ) ) ).thenReturn( result );
    when( result.withImage( any( Image.class ) ) ).thenReturn( result );
    when( result.withUpdateTrigger( ( any( Consumer.class ) ) ) ).thenReturn( result );
    return result;
  }
}