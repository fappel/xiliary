/**
 * Copyright (c) 2014 - 2016 Frank Appel
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Frank Appel - initial API and implementation
 */
package com.codeaffine.eclipse.ui.swt.theme;

import static com.codeaffine.eclipse.ui.swt.theme.ScrollableAdapterContribution.TOP_LEVEL_WINDOW_SELECTOR;
import static com.codeaffine.eclipse.ui.swt.theme.AttributeKey.key;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.util.function.Consumer;

import org.eclipse.swt.widgets.Shell;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.eclipse.swt.test.util.DisplayHelper;

public class AttributeApplicationOperationTest {

  private static final AttributeKey<Object> KEY = key( "key", Object.class );
  private static final AttributeKey<Object> TOP_LEVEL_WINDOW_SELECTOR_KEY
    = key( KEY.identifier + TOP_LEVEL_WINDOW_SELECTOR, KEY.type );

  @Rule
  public final DisplayHelper displayHelper = new DisplayHelper();

  private Consumer<AttributePreserver> topLevelWindowHandler;
  private Consumer<AttributePreserver>defaultHandler;
  private Shell shell;

  @Before
  @SuppressWarnings("unchecked")
  public void setUp() {
    defaultHandler = mock( Consumer.class );
    topLevelWindowHandler = mock( Consumer.class );
    shell = displayHelper.createShell();
  }

  @Test
  public void executeDefaultKeyOnTopLevelWindow() {
    AttributeApplicationOperation operation = create( shell, KEY );

    operation.execute();

    verify( defaultHandler ).accept( operation.getStyleAttributePreserver() );
    verify( topLevelWindowHandler, never() ).accept( operation.getStyleAttributePreserver() );
  }

  @Test
  public void executeDefaultKeyOnChildWindow() {
    AttributeApplicationOperation operation = create( new Shell( shell ), KEY );

    operation.execute();

    verify( defaultHandler ).accept( operation.getStyleAttributePreserver() );
    verify( topLevelWindowHandler, never() ).accept( operation.getStyleAttributePreserver() );
  }

  @Test
  public void executeTopLevelKeyOnTopLevelWindow() {
    AttributeApplicationOperation operation = create( shell, TOP_LEVEL_WINDOW_SELECTOR_KEY );

    operation.execute();

    verify( defaultHandler ).accept( operation.getStyleAttributePreserver() );
    verify( topLevelWindowHandler ).accept( operation.getStyleAttributePreserver() );
  }

  @Test
  public void executeTopLevelKeyOnChildWindow() {
    AttributeApplicationOperation operation = create( new Shell( shell ), TOP_LEVEL_WINDOW_SELECTOR_KEY );

    operation.execute();

    verify( defaultHandler, never() ).accept( operation.getStyleAttributePreserver() );
    verify( topLevelWindowHandler, never() ).accept( operation.getStyleAttributePreserver() );
  }

  @Test
  public void executeDefaultKeyOnTopLevelWindowIfTopLevelIsSet() {
    AttributeApplicationOperation operation = create( shell, KEY );
    operation.getStyleAttributePreserver().put( TOP_LEVEL_WINDOW_SELECTOR_KEY, new Object() );

    operation.execute();

    verify( defaultHandler, never() ).accept( operation.getStyleAttributePreserver() );
    verify( topLevelWindowHandler, never() ).accept( operation.getStyleAttributePreserver() );
  }

  private AttributeApplicationOperation create( Shell shell, AttributeKey<Object> key ) {
    AttributeApplicationOperation result = new AttributeApplicationOperation( shell, key );
    result.onDefault( defaultHandler );
    result.onTopLevelWindow( topLevelWindowHandler );
    return result;
  }
}