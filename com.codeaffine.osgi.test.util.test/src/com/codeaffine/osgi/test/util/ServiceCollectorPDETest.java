package com.codeaffine.osgi.test.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Rule;
import org.junit.Test;

public class ServiceCollectorPDETest {

  @Rule
  public final ServiceRegistrationRule serviceRegistrationRule = new ServiceRegistrationRule( getClass() );

  @Test
  public void collectService() {
    TestServiceImpl expected = new TestServiceImpl();
    serviceRegistrationRule.register( TestService.class, expected, null );

    List<TestService> actual = ServiceCollector.collectServices( TestService.class, TestServiceImpl.class );

    assertThat( actual ).containsExactly( expected );
  }

  @Test
  public void collectServiceWithMultipleInstances() {
    TestServiceImpl expected1 = new TestServiceImpl();
    TestServiceImpl expected2 = new TestServiceImpl();
    serviceRegistrationRule.register( TestService.class, expected1, null );
    serviceRegistrationRule.register( TestService.class, expected2, null );

    List<TestService> actual = ServiceCollector.collectServices( TestService.class, TestServiceImpl.class );

    assertThat( actual )
      .contains( expected1, expected2 )
      .hasSize( 2 );
  }

  @Test
  public void collectServiceWithoutInterface() {
    TestServiceImpl expected = new TestServiceImpl();
    serviceRegistrationRule.register( TestServiceImpl.class, expected, null );

    List<TestServiceImpl> actual = ServiceCollector.collectServices( TestServiceImpl.class, TestServiceImpl.class );

    assertThat( actual ).containsExactly( expected );
  }

  @Test
  public void collectServiceWithNonMatchingImplementation() {
    serviceRegistrationRule.register( TestService.class, new TestService() {}, null );

    List<TestService> actual = ServiceCollector.collectServices( TestService.class, TestServiceImpl.class );

    assertThat( actual ).isEmpty();
  }

  @Test
  public void collectServiceIfNoServiceWasRegistered() {
    List<TestService> actual = ServiceCollector.collectServices( TestService.class, TestServiceImpl.class );

    assertThat( actual ).isEmpty();
  }
}