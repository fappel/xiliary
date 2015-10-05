package com.codeaffine.eclipse.swt.util;

import static com.codeaffine.eclipse.swt.util.PlatformTypeHelper.getCurrentType;
import static com.codeaffine.eclipse.swt.util.PlatformTypeHelper.getUnusedTypes;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

import com.codeaffine.eclipse.swt.util.Platform;
import com.codeaffine.eclipse.swt.util.Platform.PlatformType;

public class PlatformTest {

  private PlatformType[] unusedTypes;
  private PlatformType currentType;
  private Platform platform;

  @Before
  public void setUp() {
    currentType = getCurrentType();
    unusedTypes = getUnusedTypes();
    platform = new Platform();
  }

  @Test
  public void matchesWithCurrentType() {
    boolean actual = platform.matches( currentType );

    assertThat( actual ).isTrue();
  }

  @Test
  public void matchesWithUnusedType() {
    boolean actual = platform.matches( unusedTypes[ 0 ] );

    assertThat( actual ).isFalse();
  }

  @Test
  public void matchesOneOfWithAllTypes() {
    boolean actual = platform.matchesOneOf( PlatformType.values() );

    assertThat( actual ).isTrue();
  }

  @Test
  public void matchesOneOfWithUnusedTypes() {
    boolean actual = platform.matchesOneOf( unusedTypes );

    assertThat( actual ).isFalse();
  }
}