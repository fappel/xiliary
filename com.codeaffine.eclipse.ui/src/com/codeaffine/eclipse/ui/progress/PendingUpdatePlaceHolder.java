package com.codeaffine.eclipse.ui.progress;

import org.eclipse.jface.resource.ImageDescriptor;

public class PendingUpdatePlaceHolder extends org.eclipse.ui.progress.PendingUpdateAdapter {

  @Override
  protected boolean isRemoved() {
    return super.isRemoved();
  }

  @Override
  protected void setRemoved( boolean removedValue ) {
    super.setRemoved( removedValue );
  }

  @Override
  public Object getAdapter( @SuppressWarnings("rawtypes") Class adapter ) {
    return super.getAdapter( adapter );
  }

  @Override
  public Object[] getChildren( Object element ) {
    return super.getChildren( element );
  }

  @Override
  public ImageDescriptor getImageDescriptor( Object element ) {
    return super.getImageDescriptor( element );
  }

  @Override
  public String getLabel( Object element ) {
    return super.getLabel( element );
  }

  @Override
  public Object getParent( Object element ) {
    return super.getParent( element );
  }

  @Override
  public String toString() {
    return super.toString();
  }
}