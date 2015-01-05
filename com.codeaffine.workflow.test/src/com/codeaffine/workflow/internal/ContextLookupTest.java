package com.codeaffine.workflow.internal;

import static com.codeaffine.workflow.WorkflowContext.VARIABLE_CONTEXT;
import static com.codeaffine.workflow.WorkflowContexts.lookup;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;

import com.codeaffine.workflow.WorkflowContext;
import com.codeaffine.workflow.definition.Activity;
import com.codeaffine.workflow.definition.VariableDeclaration;

public class ContextLookupTest {

  private static final VariableDeclaration<Integer> VARIABLE
    = new VariableDeclaration<Integer>( "variable", Integer.class );
  private static final VariableDeclaration<MyActivity> ACTIVITY
    = new VariableDeclaration<MyActivity>( "activity", MyActivity.class );

  private WorkflowDefinitionImpl definition;
  private WorkflowImpl workflow;

  public static class MyActivity implements Activity {

    private final WorkflowContext workflowContext = lookup( VARIABLE_CONTEXT );
    private final Integer variable = lookup( VARIABLE );

    @Override
    public void execute() {
      workflowContext.defineVariable( ACTIVITY, this );
    }
  }

  @Before
  public void setUp() {
    definition = new WorkflowDefinitionImpl();
    definition.addActivity( "start", MyActivity.class, null );
    definition.setStart( "start" );
    workflow = new WorkflowImpl( definition, mock( TaskListImpl.class ), new FlowEventNotifier(), new NodeLoaderImpl() );
  }

  @Test
  public void injectContext() {
    workflow.start();

    assertThat( getMyActivity().workflowContext ).isNotNull();
  }

  @Test
  public void lookupContextVariable() {
    Integer value = new Integer( 7 );

    workflow.defineVariable( VARIABLE, value );
    workflow.start();

    assertThat( getMyActivity().variable ).isSameAs( value );
  }

  @Test
  public void lookupNullValue() {
    workflow.defineVariable( VARIABLE, null );
    workflow.start();

    assertThat( getMyActivity().variable ).isNull();
  }

  private MyActivity getMyActivity() {
    return workflow.getContext().getVariableValue( ACTIVITY );
  }
}