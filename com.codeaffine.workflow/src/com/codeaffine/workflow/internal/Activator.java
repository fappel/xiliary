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