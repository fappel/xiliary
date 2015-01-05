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