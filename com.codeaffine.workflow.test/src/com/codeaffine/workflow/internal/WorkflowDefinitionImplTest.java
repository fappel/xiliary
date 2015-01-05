package com.codeaffine.workflow.internal;

import static com.codeaffine.workflow.test.util.NodeDefinitionAssert.assertThat;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;

import com.codeaffine.workflow.NodeDefinition;
import com.codeaffine.workflow.definition.Matcher;
import com.codeaffine.workflow.test.util.WorkflowDefinitionHelper.TestActivity;
import com.codeaffine.workflow.test.util.WorkflowDefinitionHelper.TestDecision;
import com.codeaffine.workflow.test.util.WorkflowDefinitionHelper.TestTask;

public class WorkflowDefinitionImplTest {

  private static final String ID = "id";
  private static final String NODE_1 = "node1";
  private static final String NODE_2 = "node2";
  private static final String NODE_3 = "node3";

  private WorkflowDefinitionImpl definition;

  @Before
  public void setUp() {
    definition = new WorkflowDefinitionImpl();
  }

  @Test
  public void setId() {
    definition.setId( ID );

    assertThat( definition.getId() ).isEqualTo( ID );
  }

  @Test
  public void addActivity() {
    definition.addActivity( NODE_1, TestActivity.class, NODE_2 );

    NodeDefinition[] nodes = definition.getNodeDefinitions();

    assertThat( nodes[ 0 ] )
      .hasNodeId( NODE_1 )
      .hasType( TestActivity.class )
      .hasSuccessors( NODE_2 );
    assertThat( nodes ).hasSize( 1 );
  }

  @Test
  public void addTask() {
    definition.addTask( NODE_1, TestTask.class, NODE_2 );

    NodeDefinition[] nodes = definition.getNodeDefinitions();

    assertThat( nodes[ 0 ] )
      .hasNodeId( NODE_1 )
      .hasType( TestTask.class )
      .hasSuccessors( NODE_2 );
    assertThat( nodes ).hasSize( 1 );
  }

  @Test( expected = IllegalArgumentException.class )
  public void addActivityWithNullAsNodeId() {
    definition.addActivity( null, TestActivity.class, NODE_2 );
  }

  @Test( expected = IllegalArgumentException.class )
  public void addActivityWithNullAsType() {
    definition.addActivity( NODE_1, null, NODE_2 );
  }

  @Test
  public void addDecision() {
    definition.addDecision( NODE_1, TestDecision.class, NODE_1, NODE_2, NODE_3 );

    NodeDefinition[] nodes = definition.getNodeDefinitions();

    assertThat( nodes[ 0 ] )
      .hasNodeId( NODE_1 )
      .hasType( TestDecision.class )
      .hasSuccessors( NODE_1, NODE_2, NODE_3 );
    assertThat( nodes ).hasSize( 1 );
  }

  @Test( expected = IllegalArgumentException.class )
  public void addDecisionWithNullAsNodeId() {
    definition.addDecision( null, TestDecision.class, NODE_1, NODE_2 );
  }

  @Test( expected = IllegalArgumentException.class )
  public void addDecisionWithNullAsDecision() {
    definition.addDecision( NODE_1, null, NODE_1, NODE_2 );
  }

  @Test( expected = IllegalArgumentException.class )
  public void addDecisionWithNullAsSuccessor1() {
    definition.addDecision( NODE_1, TestDecision.class, null, NODE_2 );
  }

  @Test( expected = IllegalArgumentException.class )
  public void addDecisionWithNullAsSuccessor2() {
    definition.addDecision( NODE_1, TestDecision.class, NODE_1, null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void addDecisionWithNullAsVarArgSuccessors() {
    definition.addDecision( NODE_1, TestDecision.class, NODE_1, NODE_2, ( String[] )null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void addDecisionWithNullAsVarArgSuccessorsElement() {
    definition.addDecision( NODE_1, TestDecision.class, NODE_1, NODE_2, new String[] { null } );
  }

  @Test
  public void setStart() {
    definition.addActivity( NODE_1, TestActivity.class, NODE_2 );

    definition.setStart( NODE_1 );

    assertThat( definition.getStartNode() )
      .hasNodeId( NODE_1 )
      .hasType( TestActivity.class )
      .hasSuccessors( NODE_2 );
    assertThat( definition.getStart() ).isEqualTo( NODE_1 );
  }

  @Test( expected = IllegalArgumentException.class )
  public void setStartWithNullAsStartNodeId() {
    definition.setStart( null );
  }

  @Test
  public void getNode() {
    definition.addActivity( NODE_1, TestActivity.class, NODE_2 );

    definition.setStart( NODE_1 );

    assertThat( definition.getNode( NODE_1 ) )
      .hasNodeId( NODE_1 )
      .hasType( TestActivity.class )
      .hasSuccessors( NODE_2 );
  }

  @Test
  public void setMatcher() {
    Matcher expected = mock( Matcher.class );
    definition.setMatcher( expected );

    Matcher actual = definition.getMatcher();

    assertThat( actual ).isSameAs( expected );
  }
}