package com.codeaffine.eclipse.ui.swt.theme;

import static com.codeaffine.eclipse.swt.util.ArgumentVerification.verifyNotNull;

import org.eclipse.swt.widgets.Scrollable;

import com.codeaffine.eclipse.swt.widget.scrollable.ScrollableAdapterFactory.Adapter;

class TypePair <S extends Scrollable, A extends Scrollable & Adapter<S>> {

  final Class<S> scrollableType;
  final Class<A> adapterType;

  TypePair( Class<S> scrollableType, Class<A> adapterType ) {
    verifyNotNull( scrollableType, "scrollableType" );
    verifyNotNull( adapterType, "adapterType" );

    this.scrollableType = scrollableType;
    this.adapterType = adapterType;
  }
}