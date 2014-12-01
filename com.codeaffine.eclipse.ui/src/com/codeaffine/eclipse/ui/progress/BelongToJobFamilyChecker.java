package com.codeaffine.eclipse.ui.progress;

import org.eclipse.ui.model.IWorkbenchAdapter;

class BelongToJobFamilyChecker {

  private final DeferredContentManager contentManager;
  private final Object parentElement;

  BelongToJobFamilyChecker( DeferredContentManager contentManager, Object parentElement ) {
    this.contentManager = contentManager;
    this.parentElement = parentElement;
  }

  boolean check( Object family ) {
    if( family instanceof DeferredContentJobFamily ) {
      return check( ( DeferredContentJobFamily )family );
    }
    return false;
  }

  private boolean check( DeferredContentJobFamily contentFamily ) {
    if( contentFamily.getSchedulingManager() == contentManager ) {
      return belongsTo( contentFamily, parentElement );
    }
    return false;
  }

  private boolean belongsTo( DeferredContentJobFamily family, Object element ) {
    if( !family.getElement().equals( element ) ) {
      return belongsToElementParent( family, element );
    }
    return true;
  }

  private boolean belongsToElementParent( DeferredContentJobFamily family, Object element ) {
    IWorkbenchAdapter adapter = getWorkbenchAdapter( element );
    if( hasParent( adapter, element ) ) {
      return belongsTo( family, adapter.getParent( element ) );
    }
    return false;
  }

  private static boolean hasParent( IWorkbenchAdapter adapter, Object element ) {
    return adapter != null && adapter.getParent( element ) != null;
  }

  private static IWorkbenchAdapter getWorkbenchAdapter( Object child ) {
    return new Adapters().getAdapter( child, IWorkbenchAdapter.class );
  }
}