package com.codeaffine.workflow;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

public class NodeDefinitionTest {

  private static final String ID = "id";
  private static final Class<Runnable> TYPE = Runnable.class;
  private static final String[] SUCCESSORS = new String[] { "successor" };

  private NodeDefinition nodeDefinition;

  @Before
  public void setUp() {
    nodeDefinition = new NodeDefinition( ID, TYPE, SUCCESSORS );
  }

  @Test
  public void getNodeId() {
    String actual = nodeDefinition.getNodeId();

    assertThat( actual ).isSameAs( ID );
  }

  @Test
  public void getType() {
    Class<?> actual = nodeDefinition.getType();

    assertThat( ( Object )actual ).isSameAs( TYPE );
  }

  @Test
  public void getSuccessors() {
    String[] actual = nodeDefinition.getSuccessors();

    assertThat( actual )
      .isNotSameAs( SUCCESSORS )
      .isEqualTo( SUCCESSORS );
  }
}
