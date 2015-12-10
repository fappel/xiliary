package com.codeaffine.eclipse.ui.swt.theme;

import static com.codeaffine.eclipse.ui.swt.theme.AttributeSetter.ADAPTER_BACKGROUND_SETTER;
import static com.codeaffine.eclipse.ui.swt.theme.AttributeSetter.ADAPTER_DEMEANOR_SETTER;
import static com.codeaffine.eclipse.ui.swt.theme.AttributeSetter.FLAT_SCROLLBAR_BACKGROUND_SETTER;
import static com.codeaffine.eclipse.ui.swt.theme.AttributeSetter.FLAT_SCROLLBAR_INCREMENT_SETTER;
import static com.codeaffine.eclipse.ui.swt.theme.AttributeSetter.FLAT_SCROLLBAR_PAGE_INCRECMENT_COLOR_SETTER;
import static com.codeaffine.eclipse.ui.swt.theme.AttributeSetter.FLAT_SCROLLBAR_THUMB_COLOR_SETTER;
import static java.lang.Integer.valueOf;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Tree;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.eclipse.swt.test.util.DisplayHelper;
import com.codeaffine.eclipse.swt.widget.scrollable.Demeanor;
import com.codeaffine.eclipse.swt.widget.scrollable.ScrollableAdapterFactory;
import com.codeaffine.eclipse.swt.widget.scrollable.ScrollbarStyle;
import com.codeaffine.eclipse.swt.widget.scrollable.TreeAdapter;

public class AttributeSetterTest {

  @Rule
  public final DisplayHelper displayHelper = new DisplayHelper();

  @Test
  public void FLAT_SCROLLBAR_BACKGROUND_COLOR_SETTER() {
    ScrollbarStyle style = mock( ScrollbarStyle.class );

    FLAT_SCROLLBAR_BACKGROUND_SETTER.accept( style, expectedColor() );

    verify( style ).setBackgroundColor( expectedColor() );
  }

  @Test
  public void FLAT_SCROLLBAR_THUMB_COLOR_SETTER() {
    ScrollbarStyle style = mock( ScrollbarStyle.class );

    FLAT_SCROLLBAR_THUMB_COLOR_SETTER.accept( style, expectedColor() );

    verify( style ).setThumbColor( expectedColor() );
  }

  @Test
  public void FLAT_SCROLLBAR_PAGE_INCRECMENT_COLOR_SETTER() {
    ScrollbarStyle style = mock( ScrollbarStyle.class );

    FLAT_SCROLLBAR_PAGE_INCRECMENT_COLOR_SETTER.accept( style, expectedColor() );

    verify( style ).setPageIncrementColor( expectedColor() );
  }

  @Test
  public void FLAT_SCROLLBAR_BUTTON_LENGHT_SETTER() {
    ScrollbarStyle style = mock( ScrollbarStyle.class );

    FLAT_SCROLLBAR_INCREMENT_SETTER.accept( style, valueOf( 7 ) );

    verify( style ).setIncrementButtonLength( 7 );
  }

  @Test
  public void ADAPTER_BACKGROUND_SETTER() {
    Tree scrollable = new Tree( displayHelper.createShell(), SWT.NONE );
    ScrollableAdapterFactory factory = new ScrollableAdapterFactory();
    TreeAdapter style = factory.create( scrollable, TreeAdapter.class );

    ADAPTER_BACKGROUND_SETTER.accept( style, expectedColor() );

    assertThat( style.getBackground() ).isEqualTo( expectedColor() );
  }

  @Test
  public void ADAPTER_DEMEANOR_SETTER() {
    ScrollbarStyle style = mock( ScrollbarStyle.class );

    ADAPTER_DEMEANOR_SETTER.accept( style, Demeanor.FIXED_SCROLL_BAR_BREADTH );

    verify( style ).setDemeanor( Demeanor.FIXED_SCROLL_BAR_BREADTH );
  }

  private Color expectedColor() {
    return displayHelper.getSystemColor( SWT.COLOR_BLACK );
  }
}