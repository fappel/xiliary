package com.codeaffine.eclipse.ui.swt.theme;

import static org.assertj.core.api.Assertions.assertThat;

import org.eclipse.swt.widgets.Table;
import org.junit.Test;

import com.codeaffine.eclipse.swt.widget.scrollable.TableAdapter;

public class TypePairTest {

  @Test
  public void initialState() {
    Class<Table> scrollableType = Table.class;
    Class<TableAdapter> adapterType = TableAdapter.class;

    TypePair<Table, TableAdapter> actual = new TypePair<>( scrollableType, adapterType );

    assertThat( actual.scrollableType ).isSameAs( scrollableType );
    assertThat( actual.adapterType ).isSameAs( adapterType );
  }

  @Test( expected = IllegalArgumentException.class )
  public void constructorWithNullAsScrollableType() {
    new TypePair<>( null, TableAdapter.class );
  }

  @Test( expected = IllegalArgumentException.class )
  public void constructorWithNullAsAdapterType() {
    new TypePair<>( Table.class, null );
  }
}