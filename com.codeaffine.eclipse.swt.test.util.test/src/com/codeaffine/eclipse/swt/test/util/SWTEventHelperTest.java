/**
 * Copyright (c) 2014 - 2017 Frank Appel
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Frank Appel - initial API and implementation
 */
package com.codeaffine.eclipse.swt.test.util;

import static com.codeaffine.eclipse.swt.test.util.SWTEventHelper.trigger;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Touch;
import org.eclipse.swt.widgets.Widget;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

public class SWTEventHelperTest {

  @Rule
  public final DisplayHelper displayHelper = new DisplayHelper();

  @Test
  public void eventCreation() {
   Event actual = SWTEventHelper.trigger( SWT.Selection ).event;

    assertThat( actual.type ).isEqualTo( SWT.Selection );
  }

  @Test
  public void atX() {
    int expectedX = 8;

    Event actual = trigger( SWT.Selection ).atX( expectedX ).event;

    assertThat( actual.x ).isEqualTo( expectedX );
  }

  @Test
  public void atY() {
    int expectedY = 8;

    Event actual = trigger( SWT.Selection ).atY( expectedY ).event;

    assertThat( actual.y ).isEqualTo( expectedY );
  }

  @Test
  public void at() {
    int expectedX = 4;
    int expectedY = 8;

    Event actual = trigger( SWT.Selection ).at( expectedX, expectedY ).event;

    assertThat( actual.x ).isEqualTo( expectedX );
    assertThat( actual.y ).isEqualTo( expectedY );
  }

  @Test
  public void withCount() {
    int expectedCount = 8;

    Event actual = trigger( SWT.Selection ).withCount( expectedCount ).event;

    assertThat( actual.count ).isEqualTo( expectedCount );
  }

  @Test
  public void withStateMask() {
    int expectedStateMask = 8;

    Event actual = trigger( SWT.Selection ).withStateMask( expectedStateMask ).event;

    assertThat( actual.stateMask ).isEqualTo( expectedStateMask );
  }

  @Test
  public void withButton() {
    int expectedButton = 1;

    Event actual = trigger( SWT.Selection ).withButton( expectedButton ).event;

    assertThat( actual.button ).isEqualTo( expectedButton );
  }

  @Test
  public void withCharacter() {
    char expectedCharacter = 'c';

    Event actual = trigger( SWT.Selection ).withCharacter( expectedCharacter ).event;

    assertThat( actual.character ).isEqualTo( expectedCharacter );
  }

  @Test
  public void withData() {
    Object expectedData = new Object();

    Event actual = trigger( SWT.Selection ).withData( expectedData ).event;

    assertThat( actual.data ).isEqualTo( expectedData );
  }

  @Test
  public void withDetail() {
    int expectedDetail = 4;

    Event actual = trigger( SWT.Selection ).withDetail( expectedDetail ).event;

    assertThat( actual.detail ).isEqualTo( expectedDetail );
  }

  @Test
  public void withEnd() {
    int expectedEnd = 4;

    Event actual = trigger( SWT.Selection ).withEnd( expectedEnd ).event;

    assertThat( actual.end ).isEqualTo( expectedEnd );
  }

  @Test
  public void withStart() {
    int expectedStart = 4;

    Event actual = trigger( SWT.Selection ).withStart( expectedStart ).event;

    assertThat( actual.start ).isEqualTo( expectedStart );
  }

  @Test
  public void withRange() {
    int expectedStart = 4;
    int expectedEnd = 6;

    Event actual = trigger( SWT.Selection ).withRange( expectedStart, expectedEnd ).event;

    assertThat( actual.start ).isEqualTo( expectedStart );
    assertThat( actual.end ).isEqualTo( expectedEnd );
  }

  @Test
  public void withGC() {
    GC expectedGc = new GC( displayHelper.createShell() );

    Event actual = trigger( SWT.Selection ).withGC( expectedGc ).event;

    assertThat( actual.gc ).isEqualTo( expectedGc );
  }

  @Test
  public void withWidth() {
    int expectedWidth = 100;

    Event actual = trigger( SWT.Selection ).withWidth( expectedWidth ).event;

    assertThat( actual.width ).isEqualTo( expectedWidth );
  }

  @Test
  public void withHeight() {
    int expectedHeight = 100;

    Event actual = trigger( SWT.Selection ).withHeight( expectedHeight ).event;

    assertThat( actual.height ).isEqualTo( expectedHeight );
  }

  @Test
  public void withSize() {
    int expectedWidth = 200;
    int expectedHeight = 100;

    Event actual = trigger( SWT.Selection ).withSize( expectedWidth, expectedHeight ).event;

    assertThat( actual.width).isEqualTo( expectedWidth );
    assertThat( actual.height ).isEqualTo( expectedHeight );
  }

  @Test
  public void withIndex() {
    int expectedIndex = 100;

    Event actual = trigger( SWT.Selection ).withIndex( expectedIndex ).event;

    assertThat( actual.index ).isEqualTo( expectedIndex );
  }

  @Test
  public void withItem() {
    Item expectedItem = mock( Item.class );

    Event actual = trigger( SWT.Selection ).withItem( expectedItem ).event;

    assertThat( actual.item ).isEqualTo( expectedItem );
  }

  @Test
  public void withKeyCode() {
    int expectedKeyCode = 2;

    Event actual = trigger( SWT.Selection ).withKeyCode( expectedKeyCode ).event;

    assertThat( actual.keyCode ).isEqualTo( expectedKeyCode );
  }

  @Test
  public void withKeyLocation() {
    int expectedKeyLocation = 2;

    Event actual = trigger( SWT.Selection ).withKeyLocation( expectedKeyLocation ).event;

    assertThat( actual.keyLocation ).isEqualTo( expectedKeyLocation );
  }

  @Test
  public void withMagnification() {
    double expectedMagnification = 2;

    Event actual = trigger( SWT.Selection ).withMagnification( expectedMagnification ).event;

    assertThat( actual.magnification ).isEqualTo( expectedMagnification );
  }

  @Test
  public void withRotation() {
    double expectedRotation = 2;

    Event actual = trigger( SWT.Selection ).withRotation( expectedRotation ).event;

    assertThat( actual.rotation ).isEqualTo( expectedRotation );
  }

  @Test
  public void withText() {
    String expectedText = "text";

    Event actual = trigger( SWT.Selection ).withText( expectedText ).event;

    assertThat( actual.text ).isEqualTo( expectedText );
  }

  @Test
  public void withTime() {
    int expectedTime = 23;

    Event actual = trigger( SWT.Selection ).withTime( expectedTime ).event;

    assertThat( actual.time ).isEqualTo( expectedTime );
  }

  @Test
  public void withTouches() {
    Touch[] expectedTouches = new Touch[ 0 ];

    Event actual = trigger( SWT.Selection ).withTouches( expectedTouches ).event;

    assertThat( actual.touches ).isEqualTo( expectedTouches );
  }

  @Test
  public void withXDirection() {
    int expectedXDirection = 3;

    Event actual = trigger( SWT.Selection ).withXDirection( expectedXDirection ).event;

    assertThat( actual.xDirection ).isEqualTo( expectedXDirection );
  }

  @Test
  public void withYDirection() {
    int expectedYDirection = 3;

    Event actual = trigger( SWT.Selection ).withYDirection( expectedYDirection ).event;

    assertThat( actual.yDirection ).isEqualTo( expectedYDirection );
  }

  @Test
  public void eventNotification() {
    Widget widget = mock( Widget.class );

    SWTEventHelper.trigger( SWT.Selection ).on( widget );

    ArgumentCaptor<Event> captor = ArgumentCaptor.forClass( Event.class );
    verify( widget ).notifyListeners( eq( SWT.Selection ), captor.capture() );
    assertThat( captor.getValue().type ).isEqualTo( SWT.Selection );
  }
}