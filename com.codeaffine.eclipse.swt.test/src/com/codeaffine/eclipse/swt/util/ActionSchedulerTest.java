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
package com.codeaffine.eclipse.swt.util;

import static com.codeaffine.eclipse.swt.test.util.DisplayHelper.flushPendingEvents;
import static java.lang.Thread.sleep;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.eclipse.swt.test.util.DisplayHelper;
import com.codeaffine.eclipse.swt.util.ActionScheduler;

public class ActionSchedulerTest {

  private static final int DELAY = 50;

  @Rule
  public final DisplayHelper displayHelper = new DisplayHelper();

  @Test
  public void schedule() throws InterruptedException {
    Runnable action = mock( Runnable.class );
    ActionScheduler scheduler = new ActionScheduler( displayHelper.getDisplay(), action  );

    scheduler.schedule( DELAY );
    sleep( DELAY * 2 );
    flushPendingEvents();

    verify( action ).run();
  }

  @Test
  public void scheduleBeforeDelayHasBeenReached() {
    Runnable action = mock( Runnable.class );
    ActionScheduler scheduler = new ActionScheduler( displayHelper.getDisplay(), action  );

    scheduler.schedule( DELAY );
    flushPendingEvents();

    verify( action, never() ).run();
  }
}
