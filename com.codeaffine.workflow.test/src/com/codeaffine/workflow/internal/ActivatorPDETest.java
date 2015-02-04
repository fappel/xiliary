package com.codeaffine.workflow.internal;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;

import com.codeaffine.osgi.test.util.ServiceCollector;
import com.codeaffine.workflow.WorkflowService;

public class ActivatorPDETest {

  private static final int AMOUNT_OF_AUTOSTARTED_SERVICES = 1;

  private Activator activator;

  @Before
  public void setUp() {
    activator = new Activator();
  }

  @After
  public void tearDown() throws Exception {
    activator.stop( getBundleContext() );
    System.getProperties().remove( WorkflowService.PROPERTY_AUTOSTART );
  }

  @Test
  public void start() throws Exception {
    activator.start( getBundleContext() );

    assertThat( availableWorkflowServiceList() ).hasSize( AMOUNT_OF_AUTOSTARTED_SERVICES + 1 );
  }

  @Test
  public void startWithAutostartDeactivation() throws Exception {
    System.setProperty( WorkflowService.PROPERTY_AUTOSTART, "false" );

    activator.start( getBundleContext() );

    assertThat( availableWorkflowServiceList() ).hasSize( AMOUNT_OF_AUTOSTARTED_SERVICES );
  }

  @Test
  public void stop() throws Exception {
    activator.start( getBundleContext() );

    activator.stop( getBundleContext() );

    assertThat( availableWorkflowServiceList() ).hasSize( AMOUNT_OF_AUTOSTARTED_SERVICES );
  }

  private static List<WorkflowService> availableWorkflowServiceList() {
    return ServiceCollector.collectServices( WorkflowService.class, WorkflowServiceImpl.class );
  }

  private BundleContext getBundleContext() {
    return FrameworkUtil.getBundle( getClass() ).getBundleContext();
  }
}