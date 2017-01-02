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
package com.codeaffine.eclipse.ui.progress;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;

public class BelongToJobFamilyCheckerPDETest {

  private DeferredContentManager contentManager;
  private BelongToJobFamilyChecker checker;
  private TestItem parentElement;
  private TestItem root;

  @Before
  public void setUp() {
    root = new TestItem( null, "root" );
    parentElement = new TestItem( root, "parentElement" );
    contentManager = mock( DeferredContentManager.class );

    checker = new BelongToJobFamilyChecker( contentManager, parentElement );
  }

  @Test
  public void check() {
    DeferredContentJobFamily family = new DeferredContentJobFamily( contentManager, parentElement );

    boolean actual = checker.check( family );

    assertThat( actual ).isTrue();
  }

  @Test
  public void checkParentCheck() {
    DeferredContentJobFamily family = new DeferredContentJobFamily( contentManager, root );

    boolean actual = checker.check( family );

    assertThat( actual ).isTrue();
  }

  @Test
  public void checkWithWrongFamily() {
    boolean actual = checker.check( new Object() );

    assertThat( actual ).isFalse();
  }

  @Test
  public void checkWithoutParent() {
    TestItem withoutParent = new TestItem( null, "withoutParent" );
    DeferredContentJobFamily family = new DeferredContentJobFamily( contentManager, withoutParent );

    boolean actual = checker.check( family );

    assertThat( actual ).isFalse();
  }

  @Test
  public void checkWithWrongContentManager() {
    DeferredContentManager unrelatedContentManager = mock( DeferredContentManager.class );
    DeferredContentJobFamily family = new DeferredContentJobFamily( unrelatedContentManager, parentElement );

    boolean actual = checker.check( family );

    assertThat( actual ).isFalse();
  }

  @Test
  public void checkWithoutWorkbenchAdapter() {
    parentElement.ignoreAdpater();
    DeferredContentJobFamily family = new DeferredContentJobFamily( contentManager, root );

    boolean actual = checker.check( family );

    assertThat( actual ).isFalse();
  }
}