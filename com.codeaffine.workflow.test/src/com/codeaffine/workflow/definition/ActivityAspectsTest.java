package com.codeaffine.workflow.definition;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

public class ActivityAspectsTest {

  static class TestActivity implements Activity {
    @Override
    @Before
    public void execute() {
    }
  }

  @Test
  public void testGetAnnotation() {
    Before annotation = ActivityAspects.getAnnotation( new TestActivity(), Before.class );

    assertNotNull( annotation );
  }
}