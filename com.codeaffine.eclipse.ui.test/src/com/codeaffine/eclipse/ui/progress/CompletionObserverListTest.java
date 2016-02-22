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
package com.codeaffine.eclipse.ui.progress;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import java.util.Iterator;

import org.eclipse.core.runtime.jobs.IJobChangeListener;
import org.junit.Before;
import org.junit.Test;

public class CompletionObserverListTest {

  private CompletionObserverList observers;

  @Before
  public void setUp() {
    observers = new CompletionObserverList();
  }

  @Test
  public void iterator() {
    Iterator<IJobChangeListener> actual = observers.iterator();

    assertThat( actual ).isEmpty();
  }

  @Test
  public void iteratorEnsuresSaveCopy() {
    observers.add( mock( IJobChangeListener.class ) );

    Iterator<IJobChangeListener> iterator = observers.iterator();
    iterator.next();
    iterator.remove();
    Iterator<IJobChangeListener> actual = observers.iterator();

    assertThat( actual ).hasSize( 1 );
  }

  @Test
  public void add() {
    IJobChangeListener expected = mock( IJobChangeListener.class );

    observers.add( expected );

    assertThat( observers.iterator() )
      .hasSize( 1 )
      .containsExactly( expected );
  }

  @Test
  public void addMulti() {
    observers.add( mock( IJobChangeListener.class ) );
    observers.add( mock( IJobChangeListener.class ) );

    assertThat( observers.iterator() ).hasSize( 2 );
  }

  @Test
  public void remove() {
    IJobChangeListener listener = mock( IJobChangeListener.class );
    observers.add( listener );

    observers.remove( listener );

    assertThat( observers.iterator() ).isEmpty();
  }

  @Test
  public void removeWithMultiListeners() {
    IJobChangeListener expected = mock( IJobChangeListener.class );
    IJobChangeListener toRemove = mock( IJobChangeListener.class );
    observers.add( expected );
    observers.add( toRemove );

    observers.remove( toRemove );

    assertThat( observers.iterator() )
      .hasSize( 1 )
      .containsExactly( expected );
  }
}