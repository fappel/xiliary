package com.codeaffine.eclipse.ui.progress;

class DeferredContentJobFamily {

  private final DeferredContentManager schedulingManager;
  private final Object element;

  DeferredContentJobFamily( DeferredContentManager schedulingManager, Object element ) {
    this.schedulingManager = schedulingManager;
    this.element = element;
  }

  DeferredContentManager getSchedulingManager() {
    return schedulingManager;
  }

  Object getElement() {
    return element;
  }
}