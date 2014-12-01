package com.codeaffine.eclipse.ui.progress;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;

public class BelongToJobFamilyCheckerTest {

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