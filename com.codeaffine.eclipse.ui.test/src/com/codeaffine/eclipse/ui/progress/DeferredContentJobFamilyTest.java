package com.codeaffine.eclipse.ui.progress;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;

public class DeferredContentJobFamilyTest {

  private DeferredContentManager contentManager;
  private DeferredContentJobFamily family;
  private Object element;

  @Before
  public void setUp() {
    element = new Object();
    contentManager = mock( DeferredContentManager.class );
    family = new DeferredContentJobFamily( contentManager, element );
  }

  @Test
  public void getSchedulingManager() {
    DeferredContentManager actual = family.getSchedulingManager();

    assertThat( actual ).isSameAs( contentManager );
  }

  @Test
  public void getElement() {
    Object actual = family.getElement();

    assertThat( actual ).isSameAs( element );
  }
}