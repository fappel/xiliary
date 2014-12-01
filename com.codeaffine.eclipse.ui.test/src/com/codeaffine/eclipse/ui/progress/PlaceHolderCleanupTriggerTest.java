package com.codeaffine.eclipse.ui.progress;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Test;

public class PlaceHolderCleanupTriggerTest {

  @Test
  public void done() {
    DeferredContentManager contentManager = mock( DeferredContentManager.class );
    PendingUpdatePlaceHolder placeHolder = new PendingUpdatePlaceHolder();
    PlaceHolderCleanupTrigger trigger = new PlaceHolderCleanupTrigger( contentManager, placeHolder );

    trigger.done( null );

    verify( contentManager ).clearPlaceholder( placeHolder );
  }
}