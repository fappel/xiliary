package com.codeaffine.workflow.internal;

import static com.codeaffine.workflow.WorkflowService.PROPERTY_AUTOSTART;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.codeaffine.workflow.WorkflowServiceActivator;

public class Activator implements BundleActivator {

  private WorkflowServiceActivator workflowServiceActivator;

  @Override
  public void start( BundleContext context ) throws Exception {
    workflowServiceActivator = new WorkflowServiceActivator( context );
    if( !"false".equals( context.getProperty( PROPERTY_AUTOSTART ) ) ) {
      workflowServiceActivator.activate();
    }
  }

  @Override
  public void stop( BundleContext context ) throws Exception {
    workflowServiceActivator.deactivate();
  }
}