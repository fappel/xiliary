package com.codeaffine.eclipse.swt.widget.scrollable;

import static com.codeaffine.eclipse.swt.testhelper.ShellHelper.createShell;
import static org.assertj.core.api.Assertions.assertThat;

import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.eclipse.swt.test.util.DisplayHelper;
import com.codeaffine.eclipse.swt.test.util.SWTIgnoreConditions.GtkPlatform;
import com.codeaffine.test.util.junit.ConditionalIgnoreRule;
import com.codeaffine.test.util.junit.ConditionalIgnoreRule.ConditionalIgnore;

public class TreeTopItemSelectorTest {

  private static final int TEN_ITEMS_PER_LEVEL = 10;
  private static final int TWO_LEVELS = 2;
  private static final int INDEX_OF_SECOND_TOP_LEVEL_ITEM = 11;

  @Rule public final ConditionalIgnoreRule conditionalIgnoreRule = new ConditionalIgnoreRule();
  @Rule public final DisplayHelper displayHelper = new DisplayHelper();

  @Test
  @ConditionalIgnore( condition = GtkPlatform.class )
  public void select() {
    Tree tree = createTree( TEN_ITEMS_PER_LEVEL, TWO_LEVELS );
    expandFirstTopLevelItem( tree );
    TreeTopItemSelector selector = new TreeTopItemSelector( tree );

    selector.select( INDEX_OF_SECOND_TOP_LEVEL_ITEM );

    assertThat( tree.getTopItem() ).isSameAs( getSecondTopLevelItem( tree ) );
  }

  private Tree createTree( int tenItemsPerLevel, int levelCount ) {
    Shell shell = createShell( displayHelper );
    return TreeHelper.createTree( shell, tenItemsPerLevel, levelCount );
  }

  private static void expandFirstTopLevelItem( Tree tree ) {
    tree.getItem( 0 ).setExpanded( true );
  }

  private static TreeItem getSecondTopLevelItem( Tree tree ) {
    return tree.getItem( 1 );
  }
}