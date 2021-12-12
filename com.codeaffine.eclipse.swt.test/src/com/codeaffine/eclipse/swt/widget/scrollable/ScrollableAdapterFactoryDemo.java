/**
 * Copyright (c) 2014 - 2022 Frank Appel
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Frank Appel - initial API and implementation
 */
package com.codeaffine.eclipse.swt.widget.scrollable;

import static com.codeaffine.eclipse.swt.test.util.ShellHelper.computeTrim;
import static com.codeaffine.eclipse.swt.test.util.ShellHelper.createDemoShell;
import static com.codeaffine.eclipse.swt.util.ReadAndDispatch.ERROR_BOX_HANDLER;
import static com.codeaffine.eclipse.swt.widget.scrollable.ScrollableAdapterFactoryHelper.adapt;
import static com.codeaffine.eclipse.swt.widget.scrollable.TableHelper.createPackedSingleColumnTable;
import static com.codeaffine.eclipse.swt.widget.scrollable.TableHelper.createTableInSingleCellGridLayout;
import static com.codeaffine.eclipse.swt.widget.scrollable.TableHelper.createVirtualTableWithOwnerDrawnItems;
import static com.codeaffine.eclipse.swt.widget.scrollable.TreeHelper.createChildren;
import static com.codeaffine.eclipse.swt.widget.scrollable.TreeHelper.createTree;
import static com.codeaffine.eclipse.swt.widget.scrollable.TreeHelper.expandRootLevelItems;
import static com.codeaffine.eclipse.swt.widget.scrollable.TreeHelper.expandTopBranch;
import static java.lang.Math.min;

import java.util.Random;
import java.util.stream.Stream;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.part.PageBook;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.eclipse.swt.test.util.DisplayHelper;
import com.codeaffine.eclipse.swt.util.ReadAndDispatch;

public class ScrollableAdapterFactoryDemo {

  private static final int TABLE_ITEM_COUNT = 20;

  @Rule
  public final DisplayHelper displayHelper = new DisplayHelper();

  private Shell shell;

  @Before
  public void setUp() {
    shell = createDemoShell( displayHelper );
  }

  @Test
  public void treeDemo() {
    Tree tree = new TestTreeFactory().create( shell );
    adapt( tree, TreeAdapter.class );
    shell.open();
    expandRootLevelItems( tree );
    expandTopBranch( tree );
    spinLoop();
  }

  @Test
  public void treeDemoWithBorder() {
    Tree tree = new TestTreeFactory( SWT.BORDER ).create( shell );
    adapt( tree, TreeAdapter.class );
    shell.open();
    expandRootLevelItems( tree );
    expandTopBranch( tree );
    spinLoop();
  }

  @Test
  public void treeDemoWithoutHorizontalBar() {
    Tree tree = new TestTreeFactory( SWT.NO_SCROLL | SWT.V_SCROLL ).create( shell );
    adapt( tree, TreeAdapter.class );
    shell.open();
    expandRootLevelItems( tree );
    expandTopBranch( tree );
    spinLoop();
  }

  @Test
  public void treeDemoWithPageBook() {
    PageBook pageBook = new PageBook( shell, SWT.NONE );
    pageBook.setBackground( displayHelper.getDisplay().getSystemColor( SWT.COLOR_BLUE ) );
    Label label = new Label( pageBook, SWT.NONE );
    label.setText( "Frontpage" );
    Tree tree = createTree( pageBook, 8, 3 );
    adapt( tree, TreeAdapter.class );
    scheduleRandomPageSelection( pageBook, label, tree, () -> replaceTreeItems( tree ), 3 );
    shell.open();
    spinLoop();
  }

  @Test
  public void scrollableDemoWithCTabFolder() {
    shell.setSize( 400, 200 );
    CTabFolder container = new CTabFolder( shell, SWT.BOTTOM | SWT.FLAT );

    Tree tree = new TestTreeFactory().create( container );
    adapt( tree, TreeAdapter.class );
    CTabItem treeItem = new CTabItem( container, SWT.NONE, 0 );
    treeItem.setText( "Tree" );
    treeItem.setControl( tree );
    expandRootLevelItems( tree );
    expandTopBranch( tree );

    Table table = new TestTableFactory().create( container );
    adapt( table, TableAdapter.class );
    CTabItem tableItem = new CTabItem( container, SWT.NONE, 1 );
    tableItem.setText( "Table" );
    tableItem.setControl( table );

    StyledText styledText = new TestStyledTextFactory().create( container );
    adapt( styledText, StyledTextAdapter.class );
    CTabItem styledTextItem = new CTabItem( container, SWT.NONE, 2 );
    styledTextItem.setText( "StyledText" );
    styledTextItem.setControl( styledText );

    ScrolledComposite scrolledComposite = new TestScrolledCompositeFactory().create( container );
    adapt( scrolledComposite, ScrolledCompositeAdapter.class );
    CTabItem scrolledCompositeItem = new CTabItem( container, SWT.NONE, 3 );
    scrolledCompositeItem.setText( "ScrolledComposite" );
    scrolledCompositeItem.setControl( scrolledComposite );

    shell.open();
    spinLoop();
  }

  @Test
  public void tableDemo() {
    adapt( new TestTableFactory().create( shell ), TableAdapter.class );
    shell.open();
    spinLoop();
  }

  @Test
  public void tableDemoWithColumnWidthAdjustment() {
    Table table = equipWithSingleColumnTable( shell );
    adapt( table, TableAdapter.class );
    shell.setBounds( computeTrim( shell, adjustTableHeight( table, TABLE_ITEM_COUNT + 2 ) ) );
    table.getColumns()[ 0 ].setWidth( table.getClientArea().width );
    shell.setLocation( 300, 300 );
    shell.open();
    spinLoop();
  }

  @Test
  public void tableDemoWithPackedPopupShell() {
    shell.open();
    Shell popup = new Shell( shell, SWT.NO_TRIM );
    popup.setLocation( shell.getLocation() );
    Table table = createTableInSingleCellGridLayout( popup, tbl -> adapt( tbl, TableAdapter.class ) );
    popup.pack();
    popup.open();
    spinLoop( 1000 );

    table.getItem( 1 ).dispose();
    GridData data = new GridData( GridData.FILL_BOTH );
    data.widthHint = table.getSize().x;
    data.heightHint = table.getItemHeight();
    table.setLayoutData( data );
    popup.pack();
    spinLoop( 1000 );

    TableItem tableItem = new TableItem( table, SWT.NONE );
    tableItem.setText( "replaced" );
    data.heightHint = table.getItemHeight() * 2;
    table.setLayoutData( data );
    popup.pack();
    spinLoop();
  }

  @Test
  public void virtualTableDemoWithOwnerDrawnItems() {
    ItemList itemList = new ItemList();
    Table table = createVirtualTableWithOwnerDrawnItems( shell, itemList );
    adapt( table, TableAdapter.class );
    table.setItemCount( itemList.getItems().length );
    shell.open();
    spinLoop();
  }

  @Test
  public void styledTextDemo() {
    adapt( new TestStyledTextFactory().create( shell ), StyledTextAdapter.class );
    shell.open();
    spinLoop();
  }

  @Test
  public void scrolledCompositeDemo() {
    adapt( new TestScrolledCompositeFactory().create( shell ), ScrolledCompositeAdapter.class );
    shell.open();
    spinLoop();
  }

  @Test
  public void sashFormWithAdaptedControls() {
    SashForm sashForm = new SashForm( shell, SWT.SMOOTH | SWT.VERTICAL );
    adapt( TreeHelper.createOwnerDrawnTreeWithColumn( sashForm ), TreeAdapter.class );
    adapt( new TestStyledTextFactory().create( sashForm ), StyledTextAdapter.class );
    sashForm.setWeights( new int[] { 7, 3 } );
    shell.open();
    spinLoop();
  }

  private void spinLoop() {
    newReadAndDispatch().spinLoop( shell );
  }

  private void spinLoop( long duration ) {
    newReadAndDispatch().spinLoop( shell, duration );
  }

  private static ReadAndDispatch newReadAndDispatch() {
    return new ReadAndDispatch( ERROR_BOX_HANDLER );
  }

  private void scheduleRandomPageSelection(
    PageBook pageBook, Control page1, Control page2, Runnable page2Changer, int showPageRecurranceNumberToRunChange )
  {
    displayHelper.getDisplay().timerExec( 2000, () -> {
      int newShowPageRecurranceNumberToRunChange = showPageRecurranceNumberToRunChange;
      pageBook.showPage( page1 );
      if( new Random().nextBoolean() ) {
        if( showPageRecurranceNumberToRunChange == 0 ) {
          page2Changer.run();
        }
        pageBook.showPage( page2 );
        newShowPageRecurranceNumberToRunChange--;
      }
      scheduleRandomPageSelection( pageBook, page1, page2, page2Changer, newShowPageRecurranceNumberToRunChange );
    } );
  }

  private static void replaceTreeItems( Tree tree ) {
    Stream.of( tree.getItems() ).forEach( item -> item.dispose() );
    createChildren( tree, "blub", 8, 3 );
    tree.pack();
  }

  private static Table equipWithSingleColumnTable( Shell shell ) {
    shell.setLayout( new FillLayout() );
    Table result = createPackedSingleColumnTable( shell, TABLE_ITEM_COUNT );
    shell.pack();
    return result;
  }

  private static Rectangle adjustTableHeight( Table table, int maxItems ) {
    Rectangle result = table.getBounds();
    result.height = min( result.height, table.getItemHeight() * maxItems );
    table.setBounds( result );
    return result;
  }
}