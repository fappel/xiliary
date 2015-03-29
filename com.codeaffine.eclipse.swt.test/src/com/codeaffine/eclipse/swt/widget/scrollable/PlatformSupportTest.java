package com.codeaffine.eclipse.swt.widget.scrollable;

import static com.codeaffine.eclipse.swt.widget.scrollable.PlatformTypeHelper.getCurrentType;
import static com.codeaffine.eclipse.swt.widget.scrollable.PlatformTypeHelper.getUnusedTypes;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

import com.codeaffine.eclipse.swt.widget.scrollable.Platform.PlatformType;

public class PlatformSupportTest {

  private PlatformType currentType;
  private PlatformType unusedType;

  @Before
  public void setUp() {
    currentType = getCurrentType();
    unusedType = getUnusedTypes()[ 0 ];
  }

  @Test
  public void isGrantedOnSupportedPlatforms() {
    PlatformSupport support = new PlatformSupport( currentType );

    boolean actual = support.isGranted();

    assertThat( actual ).isTrue();
  }

  @Test
  public void isGrantedOnUnsupportedPlatforms() {
    PlatformSupport support = new PlatformSupport( unusedType );

    boolean actual = support.isGranted();

    assertThat( actual ).isFalse();
  }

  @Test
  public void getSupportedTypes() {
    PlatformSupport support = new PlatformSupport( currentType );

    PlatformType[] actual = support.getSupportedTypes();

    assertThat( actual ).containsExactly( currentType );
  }
}