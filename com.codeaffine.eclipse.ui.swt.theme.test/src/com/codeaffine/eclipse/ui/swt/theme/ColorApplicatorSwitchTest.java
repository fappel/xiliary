package com.codeaffine.eclipse.ui.swt.theme;

import static com.codeaffine.eclipse.ui.swt.theme.ScrollableAdapterContribution.TOP_LEVEL_WINDOW_SELECTOR;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Shell;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.eclipse.swt.test.util.DisplayHelper;

public class ColorApplicatorSwitchTest {

  private static final String KEY = "key";

  @Rule
  public final DisplayHelper displayHelper = new DisplayHelper();

  private Runnable topLevelWindowHandler;
  private Runnable defaultHandler;
  private Shell shell;

  @Before
  public void setUp() {
    defaultHandler = mock( Runnable.class );
    topLevelWindowHandler = mock( Runnable.class );
    shell = displayHelper.createShell();
  }

  @Test
  public void applyDefaultKeyOnTopLevelWindow() {
    ColorApplicatorSwitch applicator = createApplicator( shell, KEY );

    applicator.apply();

    verify( defaultHandler ).run();
    verify( topLevelWindowHandler, never() ).run();
  }

  @Test
  public void applyDefaultKeyOnChildWindow() {
    ColorApplicatorSwitch applicator = createApplicator( new Shell( shell ), KEY );

    applicator.apply();

    verify( defaultHandler ).run();
    verify( topLevelWindowHandler, never() ).run();
  }

  @Test
  public void applyTopLevelKeyOnTopLevelWindow() {
    ColorApplicatorSwitch applicator = createApplicator( shell, KEY + TOP_LEVEL_WINDOW_SELECTOR );

    applicator.apply();

    verify( defaultHandler ).run();
    verify( topLevelWindowHandler ).run();
  }

  @Test
  public void applyTopLevelKeyOnChildWindow() {
    ColorApplicatorSwitch applicator = createApplicator( new Shell( shell ), KEY + TOP_LEVEL_WINDOW_SELECTOR );

    applicator.apply();

    verify( defaultHandler, never() ).run();
    verify( topLevelWindowHandler, never() ).run();
  }

  @Test
  public void applyDefaultKeyOnTopLevelWindowIfTopLevelIsSet() {
    ColorApplicatorSwitch applicator = createApplicator( shell, KEY );
    applicator.buffer( KEY + TOP_LEVEL_WINDOW_SELECTOR, anyColor() );

    applicator.apply();

    verify( defaultHandler, never() ).run();
    verify( topLevelWindowHandler, never() ).run();
  }

  private Color anyColor() {
    return new Color( displayHelper.getDisplay(), 10, 20, 30 );
  }

  private ColorApplicatorSwitch createApplicator( Shell shell, String key ) {
    ColorApplicatorSwitch result = new ColorApplicatorSwitch( shell, key );
    result.onDefault( defaultHandler );
    result.onTopLevelWindow( topLevelWindowHandler );
    return result;
  }
}