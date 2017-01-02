/**
 * Copyright (c) 2014 - 2017 Frank Appel
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Frank Appel - initial API and implementation
 */
package com.codeaffine.workflow.definition;

public interface WorkflowDefinition {
  void setId( String string );
  String getId();
  void addActivity( String nodeId, Class<? extends Activity> activity, String successor );
  void addTask( String nodeId, Class<? extends Task> task, String successor );
  void addDecision(
    String nodeId, Class<? extends Decision> decision, String successor1, String successor2, String ... successors );
  void setStart( String nodeId );
  void setMatcher( Matcher matcher );
}