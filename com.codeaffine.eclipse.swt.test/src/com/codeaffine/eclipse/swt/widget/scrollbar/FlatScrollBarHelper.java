package com.codeaffine.eclipse.swt.widget.scrollbar;

import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.mockito.ArgumentCaptor;

public class FlatScrollBarHelper {

  static FlatScrollBar stubScrollBar( int selection, int increment ) {
    FlatScrollBar result = mock( FlatScrollBar.class );
    when( result.getIncrement() ).thenReturn( increment );
    when( result.getSelection() ).thenReturn( selection );
    return result;
  }

  static ScrollListener equipScrollBarWithListener( FlatScrollBar scrollBar ) {
    ScrollListener result = mock( ScrollListener.class );
    scrollBar.addScrollListener( result );
    return result;
  }

  static ScrollEvent verifyNotification( ScrollListener listener ) {
    ArgumentCaptor<ScrollEvent> captor = forClass( ScrollEvent.class );
    verify( listener ).selectionChanged( captor.capture() );
    return captor.getValue();
  }
}