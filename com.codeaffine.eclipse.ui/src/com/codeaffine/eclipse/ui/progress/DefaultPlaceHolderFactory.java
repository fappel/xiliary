package com.codeaffine.eclipse.ui.progress;

class DefaultPlaceHolderFactory implements PendingUpdatePlaceHolderFactory {

  @Override
  public PendingUpdatePlaceHolder create() {
    return new PendingUpdatePlaceHolder();
  }
}