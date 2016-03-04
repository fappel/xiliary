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

import static com.codeaffine.eclipse.core.runtime.Predicates.attribute;
import static com.codeaffine.eclipse.core.runtime.test.util.ExtensionAssert.assertThat;
import static org.assertj.core.api.Assertions.assertThat;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.eclipse.core.runtime.Extension;
import com.codeaffine.eclipse.core.runtime.RegistryAdapter;
import com.codeaffine.eclipse.swt.test.util.SWTIgnoreConditions.CocoaPlatform;
import com.codeaffine.test.util.junit.ConditionalIgnoreRule;
import com.codeaffine.test.util.junit.ConditionalIgnoreRule.ConditionalIgnore;

public class ScrollbarPreferencePagePDETest {

  @Rule
  public final ConditionalIgnoreRule conditionalIgnoreRule = new ConditionalIgnoreRule();

  private static final String PREFERENCE_PAGES_EP = "org.eclipse.ui.preferencePages";
  private static final String PAGE_ID = "com.codeaffine.eclipse.ui.swt.theme.ScrollbarPreferencePage";
  private static final String KEYWORDS_EP = "org.eclipse.ui.keywords";
  private static final String KEYWORDS_ID = "com.codeaffine.eclipse.ui.swt.theme.keywords.ScrollbarPreferencePage";

  @Test
  @ConditionalIgnore( condition = CocoaPlatform.class )
  public void pageRegistration() {
    Extension actual = new RegistryAdapter()
      .readExtension( PREFERENCE_PAGES_EP )
      .thatMatches( attribute( "id", PAGE_ID ) )
      .process();

    assertThat( actual )
      .hasAttributeValue( "category", "org.eclipse.ui.preferencePages.Views" )
      .hasAttributeValue( "name", "Scrollbars" )
      .hasAttributeValue( "class", ScrollbarPreferencePage.class.getName() )
      .hasChildWithAttributeValue( "id", KEYWORDS_ID );
  }

  @Test
  @ConditionalIgnore( condition = CocoaPlatform.class )
  public void pageKeywordRegistration() {
    Extension actual = new RegistryAdapter()
        .readExtension( KEYWORDS_EP )
        .thatMatches( attribute( "id", KEYWORDS_ID ) )
        .process();

    assertThat( actual )
      .hasAttributeValue( "label", "appearance scrollbars" );
  }

  @Test
  @ConditionalIgnore( condition = CocoaPlatform.class )
  public void pageCreation() {
    IWorkbenchPreferencePage actual = new RegistryAdapter()
        .createExecutableExtension( PREFERENCE_PAGES_EP, IWorkbenchPreferencePage.class )
        .thatMatches( attribute( "id", PAGE_ID ) )
        .process();

    assertThat( actual )
      .isInstanceOf( PreferencePage.class );
  }
}