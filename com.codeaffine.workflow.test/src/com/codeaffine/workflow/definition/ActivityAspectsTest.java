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
package com.codeaffine.workflow.definition;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

public class ActivityAspectsTest {

  static class TestActivity implements Activity {
    @Override
    @Before
    public void execute() {
    }
  }

  @Test
  public void testGetAnnotation() {
    Before annotation = ActivityAspects.getAnnotation( new TestActivity(), Before.class );

    assertNotNull( annotation );
  }
}