package com.codeaffine.workflow.persistence;

import static com.codeaffine.workflow.WorkflowContext.VARIABLE_SERVICE;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

import com.codeaffine.workflow.NodeLoader;
import com.codeaffine.workflow.WorkflowService;
import com.codeaffine.workflow.WorkflowServiceFactory;
import com.codeaffine.workflow.definition.VariableDeclaration;

public class WorkflowServiceAdapterTest {

  private WorkflowServiceAdapter serviceAdapter;

  @Before
  public void setUp() {
    WorkflowService service = new WorkflowServiceFactory().createWorkflowService();
    serviceAdapter = new WorkflowServiceAdapter( service );
  }

  @Test
  public void getClassFinder() {
    ClassFinder actual = serviceAdapter.getClassFinder();

    assertThat( actual ).isNotNull();
  }

  @Test
  public void getNodeLoader() {
    NodeLoader actual = serviceAdapter.getNodeLoader();

    assertThat( actual ).isNotNull();
  }

  @Test
  public void getVariableDeclarations() {
    VariableDeclaration<?>[] actual = serviceAdapter.getVariableDeclarations();

    assertThat( actual ).containsExactly( VARIABLE_SERVICE );
  }
}