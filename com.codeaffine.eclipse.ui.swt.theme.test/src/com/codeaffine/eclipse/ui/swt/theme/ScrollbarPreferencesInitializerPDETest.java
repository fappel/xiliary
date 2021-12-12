/**
 * Copyright (c) 2014 - 2022 Frank Appel
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Frank Appel - initial API and implementation
 */
package com.codeaffine.eclipse.ui.swt.theme;

import static com.codeaffine.eclipse.core.runtime.Predicates.attribute;
import static com.codeaffine.eclipse.ui.swt.theme.ScrollableAdapterContribution.ADAPTER_DEMEANOR;
import static com.codeaffine.eclipse.ui.swt.theme.ScrollableAdapterContribution.FLAT_SCROLL_BAR_INCREMENT_LENGTH;
import static com.codeaffine.eclipse.ui.swt.theme.ScrollbarPreferencesInitializer.UNSET;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.eclipse.core.runtime.RegistryAdapter;
import com.codeaffine.eclipse.swt.test.util.SWTIgnoreConditions.CocoaPlatform;
import com.codeaffine.test.util.junit.ConditionalIgnoreRule;
import com.codeaffine.test.util.junit.ConditionalIgnoreRule.ConditionalIgnore;

public class ScrollbarPreferencesInitializerPDETest {

  @Rule
  public final ConditionalIgnoreRule conditionalIgnoreRule = new ConditionalIgnoreRule();

  private static final String EXTENION_POINT_ID = "org.eclipse.core.runtime.preferences";
  private static final Class<ScrollbarPreferencesInitializer> INITIALIZER_TYPE
    = ScrollbarPreferencesInitializer.class;

  @Test
  @ConditionalIgnore( condition = CocoaPlatform.class )
  public void creation() {
    ScrollbarPreferencesInitializer actual = createExtension( EXTENION_POINT_ID, INITIALIZER_TYPE );

    assertThat( actual ).isNotNull();
  }

  @Test
  public void initialization() {
    IPreferenceStore store = mock( IPreferenceStore.class );
    ScrollbarPreferencesInitializer initializer = new ScrollbarPreferencesInitializer( store );

    initializer.initializeDefaultPreferences();

    verify( store ).setDefault( ADAPTER_DEMEANOR, UNSET );
    verify( store ).setDefault( FLAT_SCROLL_BAR_INCREMENT_LENGTH, UNSET );
  }

  private static <T extends AbstractPreferenceInitializer> T createExtension(
    String extensionPointId , Class<T> initializerType  )
  {
    return initializerType.cast( new RegistryAdapter()
      .createExecutableExtension( extensionPointId, AbstractPreferenceInitializer.class )
      .thatMatches( attribute( "class", initializerType.getName() ) )
      .process() );
  }
}