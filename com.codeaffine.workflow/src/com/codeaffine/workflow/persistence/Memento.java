package com.codeaffine.workflow.persistence;

import java.util.List;

import com.codeaffine.workflow.TaskHolder;

public class Memento {

  private final List<TaskHolder> content;
  
  public Memento( List<TaskHolder> content ) {
    this.content = content;
  }
  
  public List<TaskHolder> getContent() {
    return content;
  }
}