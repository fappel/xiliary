package com.codeaffine.workflow;

import static com.codeaffine.test.util.lang.EqualsTester.newInstance;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.Callable;

import org.junit.Test;

import com.codeaffine.test.util.lang.EqualsTester;

public class VariableDeclarationTest {

  private static final String NAME = "name";

  @Test
  public void getName() {
    VariableDeclaration<Runnable> declaration = new VariableDeclaration<Runnable>( NAME, Runnable.class );

    String actual = declaration.getName();

    assertThat( actual ).isSameAs( NAME );
  }

  @Test
  public void getType() {
    VariableDeclaration<Runnable> declaration = new VariableDeclaration<Runnable>( NAME, Runnable.class );

    Class<Runnable> actual = declaration.getType();

    assertThat( actual ).isSameAs( Runnable.class );
  }

  @Test
  @SuppressWarnings( { "rawtypes", "unchecked" } )
  public void equalsAndHashCode() {
    EqualsTester<VariableDeclaration> tester = newInstance( new VariableDeclaration( NAME, Runnable.class ) );

    tester.assertImplementsEqualsAndHashCode();
    tester.assertEqual( new VariableDeclaration( NAME, Runnable.class ),
                        new VariableDeclaration( NAME, Runnable.class ) );
    tester.assertNotEqual( new VariableDeclaration<Runnable>( NAME, Runnable.class ),
                           new VariableDeclaration<Runnable>( "otherName", Runnable.class ) );
    tester.assertNotEqual( new VariableDeclaration<Runnable>( NAME, Runnable.class ),
                           new VariableDeclaration<Callable>( NAME, Callable.class ) );
  }

  @Test
  public void toStringRepresentation() {
    VariableDeclaration<Runnable> declaration = new VariableDeclaration<Runnable>( NAME, Runnable.class );

    String actual = declaration.toString();

    assertThat( actual )
      .contains( NAME )
      .contains( Runnable.class.getName() );
  }

  @Test( expected = IllegalArgumentException.class )
  public void constructorWithNullAsName() {
    new VariableDeclaration<Runnable>( null, Runnable.class );
  }

  @Test( expected = IllegalArgumentException.class )
  public void constructorWithIllegalName() {
    new VariableDeclaration<Runnable>( "[", Runnable.class );
  }

  @Test( expected = IllegalArgumentException.class )
  public void constructorWithNullAsType() {
    new VariableDeclaration<Runnable>( NAME, null );
  }
}