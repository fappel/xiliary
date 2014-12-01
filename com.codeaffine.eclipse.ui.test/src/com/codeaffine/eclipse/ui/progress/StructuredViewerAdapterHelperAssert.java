package com.codeaffine.eclipse.ui.progress;

import org.assertj.core.api.AbstractAssert;


public class StructuredViewerAdapterHelperAssert
  extends AbstractAssert<StructuredViewerAdapterHelperAssert, StructuredViewerAdapterHelper<?>>
{

  public static StructuredViewerAdapterHelperAssert assertThat( StructuredViewerAdapterHelper<?> actual ) {
    return new StructuredViewerAdapterHelperAssert( actual );
  }

  public StructuredViewerAdapterHelperAssert( StructuredViewerAdapterHelper<?> actual ) {
    super( actual, StructuredViewerAdapterHelperAssert.class );
  }

  public StructuredViewerAdapterHelperAssert hasItemCount( int expected ) {
    isNotNull();
    if( actual.getItemCount() != expected ) {
      failWithMessage( "Expected item count to be <%s> but was <%s>.", expected, actual.getItemCount() );
    }
    return this;
  }

  public StructuredViewerAdapterHelperAssert hasPendingItem( String expected ) {
    isNotNull();
    hasItemCount( 1 );
    String actualText = actual.getItem( 0 ).getText();
    if( !actualText.equals( expected ) ) {
      failWithMessage( "Expected pending item with text <%s> but was <%s>.", expected, actualText );
    }
    return this;
  }

  public StructuredViewerAdapterHelperAssert completesDeferredLoading() {
    isNotNull();
    actual.waitTillJobHasFinished();
    return this;
  }
}