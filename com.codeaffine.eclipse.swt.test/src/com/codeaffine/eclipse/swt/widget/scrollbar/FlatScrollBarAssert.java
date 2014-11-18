package com.codeaffine.eclipse.swt.widget.scrollbar;

import org.assertj.core.api.AbstractAssert;
import org.eclipse.swt.graphics.Rectangle;

public class FlatScrollBarAssert extends AbstractAssert<FlatScrollBarAssert, FlatScrollBar>{

  private static final String FAIL_MESSAGE_PATTERN_BOUNDS = "Expected %s bounds to be <%s> but were <%s>.";
  private static final String FAIL_MESSAGE_PATTERN_VALUES = "Expected %s to be <%s> but was <%s>.";

  public FlatScrollBarAssert( FlatScrollBar actual ) {
    super( actual, FlatScrollBarAssert.class );
  }

  public static FlatScrollBarAssert assertThat( FlatScrollBar actual ) {
    return new FlatScrollBarAssert( actual );
  }

  public FlatScrollBarAssert hasUpBounds( int x, int y, int width, int height ) {
    isNotNull();
    verifyViewComponentBounds( actual.up, "up", new Rectangle( x, y, width, height ) );
    return this;
  }

  public FlatScrollBarAssert hasUpFastBounds( int x, int y, int width, int height  ) {
    isNotNull();
    verifyViewComponentBounds( actual.upFast, "upFast", new Rectangle( x, y, width, height ) );
    return this;
  }

  public FlatScrollBarAssert hasDragBounds( int x, int y, int width, int height  ) {
    isNotNull();
    verifyViewComponentBounds( actual.drag, "drag", new Rectangle( x, y, width, height ) );
    return this;
  }

  public FlatScrollBarAssert hasDownFastBounds( int x, int y, int width, int height  ) {
    isNotNull();
    verifyViewComponentBounds( actual.downFast, "downFast", new Rectangle( x, y, width, height ) );
    return this;
  }

  public FlatScrollBarAssert hasDownBounds( int x, int y, int width, int height  ) {
    isNotNull();
    verifyViewComponentBounds( actual.down, "down", new Rectangle( x, y, width, height ) );
    return this;
  }

  public FlatScrollBarAssert isVisible() {
    isNotNull();
    if( !actual.getControl().getVisible() ) {
      failWithMessage( "Expected scrollbar set to be visible but was not."  );
    }
    return this;
  }

  public FlatScrollBarAssert isNotVisible() {
    isNotNull();
    if( actual.getControl().getVisible() ) {
      failWithMessage( "Expected scrollbar set to be invisible but was not."  );
    }
    return this;
  }

  public FlatScrollBarAssert hasBounds( int x, int y, int width, int height  ) {
    isNotNull();
    verifyViewComponentBounds( actual, "scrollbar", new Rectangle( x, y, width, height ) );
    return this;
  }

  public FlatScrollBarAssert hasIncrement( int expected ) {
    isNotNull();
    verifyValue( expected, actual.getIncrement(), "increment" );
    return this;
  }

  public FlatScrollBarAssert hasPageIncrement( int expected ) {
    isNotNull();
    verifyValue( expected, actual.getPageIncrement(), "page-increment" );
    return this;
  }

  public FlatScrollBarAssert hasMinimum( int expected ) {
    isNotNull();
    verifyValue( expected, actual.getMinimum(), "minimum" );
    return this;
  }

  public FlatScrollBarAssert hasMaximum( int expected ) {
    isNotNull();
    verifyValue( expected, actual.getMaximum(), "maximum" );
    return this;
  }

  public FlatScrollBarAssert hasThumb( int expected ) {
    isNotNull();
    verifyValue( expected, actual.getThumb(), "thumb" );
    return this;
  }

  public FlatScrollBarAssert hasSelection( int expected ) {
    isNotNull();
    verifyValue( expected, actual.getSelection(), "selection" );
    return this;
  }

  private void verifyValue( int expected, int value, String valueType ) {
    if( value != expected ) {
      failWithMessage( FAIL_MESSAGE_PATTERN_VALUES, valueType, expected, value );
    }
  }

  private void verifyViewComponentBounds( ViewComponent viewComponent, String viewComponentName, Rectangle expected ) {
    if( !getBounds( viewComponent ).equals( expected ) ) {
      failWithMessage( FAIL_MESSAGE_PATTERN_BOUNDS, viewComponentName, expected, getBounds( viewComponent ) );
    }
  }

  private static Rectangle getBounds( ViewComponent viewComponent ) {
    return viewComponent.getControl().getBounds();
  }
}