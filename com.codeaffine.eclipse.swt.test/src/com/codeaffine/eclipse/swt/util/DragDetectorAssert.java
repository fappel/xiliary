package com.codeaffine.eclipse.swt.util;

import org.assertj.core.api.AbstractAssert;

public class DragDetectorAssert extends AbstractAssert<DragDetectorAssert, DragDetector> {

  public DragDetectorAssert( DragDetector actual ) {
    super( actual, DragDetectorAssert.class );
  }

  public static DragDetectorAssert assertThat( DragDetector actual ) {
    return new DragDetectorAssert( actual );
  }

  public DragDetectorAssert dragEventGeneratedIsSet() {
    isNotNull();
    if( !actual.dragEventGenerated ) {
      failWithMessage( "Expected dragEventGenerated flag to be set but was not." );
    }
    return this;
  }

  public DragDetectorAssert dragEventGeneratedFlagIsNotSet() {
    isNotNull();
    if( actual.dragEventGenerated ) {
      failWithMessage( "Expected dragEventGenerated flag not to be set but was." );
    }
    return this;
  }

  public DragDetectorAssert hasLastMouseX( int expected ) {
    isNotNull();
    if( actual.lastMouseX != expected ) {
      failWithMessage( "Expected lastMouseX to be <%s> but was <%s>.", expected, actual.lastMouseX );
    }
    return this;
  }

  public DragDetectorAssert hasLastMouseY( int expected ) {
    isNotNull();
    if( actual.lastMouseY != expected ) {
      failWithMessage( "Expected lastMouseY to be <%s> but was <%s>.", expected, actual.lastMouseX );
    }
    return this;
  }
}