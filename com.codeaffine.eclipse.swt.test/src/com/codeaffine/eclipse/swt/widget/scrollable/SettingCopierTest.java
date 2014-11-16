package com.codeaffine.eclipse.swt.widget.scrollable;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.eclipse.swt.widgets.ScrollBar;
import org.junit.Test;

import com.codeaffine.eclipse.swt.widget.scrollbar.FlatScrollBar;

public class SettingCopierTest {

  private static final int INCREMENT = 1;
  private static final int MINIMUM = 0;
  private static final int MAXIMUM = 100;
  private static final int PAGE_INCREMENT = 10;
  private static final int THUMB = 20;
  private static final int SELECTION = 50;

  @Test
  public void copy() {
    ScrollBar from = stubScrollBar();
    FlatScrollBar to = mock( FlatScrollBar.class );
    SettingCopier copier = new SettingCopier( from, to );

    copier.copy();

    verify( to ).setIncrement( INCREMENT );
    verify( to ).setMinimum( MINIMUM );
    verify( to ).setMaximum( MAXIMUM );
    verify( to ).setPageIncrement( PAGE_INCREMENT );
    verify( to ).setThumb( THUMB );
    verify( to ).setSelection( SELECTION );
    verifyNoMoreInteractions( to );
  }

  private static ScrollBar stubScrollBar() {
    ScrollBar result = mock( ScrollBar.class );
    when( result.getIncrement() ).thenReturn( INCREMENT );
    when( result.getMinimum() ).thenReturn( MINIMUM );
    when( result.getMaximum() ).thenReturn( MAXIMUM );
    when( result.getPageIncrement() ).thenReturn( PAGE_INCREMENT );
    when( result.getThumb() ).thenReturn( THUMB );
    when( result.getSelection() ).thenReturn( SELECTION );
    return result;
  }
}