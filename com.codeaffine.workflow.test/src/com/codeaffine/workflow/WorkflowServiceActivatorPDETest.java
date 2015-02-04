package com.codeaffine.workflow;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;

import com.codeaffine.osgi.test.util.Registration;
import com.codeaffine.osgi.test.util.ServiceCollector;
import com.codeaffine.osgi.test.util.ServiceRegistrationRule;
import com.codeaffine.workflow.definition.WorkflowDefinition;
import com.codeaffine.workflow.definition.WorkflowDefinitionProvider;
import com.codeaffine.workflow.internal.TestDefinitionProvider;
import com.codeaffine.workflow.internal.WorkflowServiceImpl;

public class WorkflowServiceActivatorPDETest {

  private static final int AMOUNT_OF_AUTOSTARTED_SERVICES = 1;

  @Rule
  public final ServiceRegistrationRule serviceRegistration = new ServiceRegistrationRule( getClass() );

  private WorkflowServiceActivator activator;

  @Before
  public void setUp() {
    activator = new WorkflowServiceActivator( getBundleContext() );
  }

  @After
  public void tearDown() {
    activator.deactivate();
  }

  @Test
  public void activate() {
    activator.activate();

    assertThat( availableWorkflowServiceList() ).hasSize( AMOUNT_OF_AUTOSTARTED_SERVICES + 1 );
  }

  @Test
  public void deactivate() {
    activator.activate();

    activator.deactivate();

    assertThat( availableWorkflowServiceList() ).hasSize( AMOUNT_OF_AUTOSTARTED_SERVICES );
  }

  @Test
  public void workflowDefinitionProviderRegistration() {
    WorkflowDefinitionProvider provider = spy( new TestDefinitionProvider() );
    activator.activate();

    serviceRegistration.register( WorkflowDefinitionProvider.class, provider );

    verify( provider, times( AMOUNT_OF_AUTOSTARTED_SERVICES  + 1 ) ).define( any( WorkflowDefinition.class ) );
  }

  @Test
  public void workflowDefinitionProviderDeregistration() {
    activator.activate();
    WorkflowDefinitionProvider provider = spy( new TestDefinitionProvider() );
    Registration<WorkflowDefinitionProvider> registration
      = serviceRegistration.register( WorkflowDefinitionProvider.class, provider );

    registration.unregister();

    for( WorkflowService workflowService : availableWorkflowServiceList() ) {
      assertThat( workflowService.getWorkflowDefinitionIds() ).hasSize( 0 );
    }
  }

  private static List<WorkflowService> availableWorkflowServiceList() {
    return ServiceCollector.collectServices( WorkflowService.class, WorkflowServiceImpl.class );
  }

  private BundleContext getBundleContext() {
    return FrameworkUtil.getBundle( getClass() ).getBundleContext();
  }
}