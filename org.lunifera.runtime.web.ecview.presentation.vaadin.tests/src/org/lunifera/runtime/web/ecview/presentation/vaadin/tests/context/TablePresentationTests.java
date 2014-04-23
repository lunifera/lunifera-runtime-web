/**
 * Copyright (c) 2012 Lunifera GmbH (Austria) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Florian Pirchner - initial API and implementation
 */
package org.lunifera.runtime.web.ecview.presentation.vaadin.tests.context;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;

import org.eclipse.emf.ecp.ecview.common.context.ContextException;
import org.eclipse.emf.ecp.ecview.common.editpart.DelegatingEditPartManager;
import org.eclipse.emf.ecp.ecview.common.editpart.IElementEditpart;
import org.eclipse.emf.ecp.ecview.common.editpart.IEmbeddableEditpart;
import org.eclipse.emf.ecp.ecview.common.editpart.IViewEditpart;
import org.eclipse.emf.ecp.ecview.common.model.binding.YBindingSet;
import org.eclipse.emf.ecp.ecview.common.model.core.YElement;
import org.eclipse.emf.ecp.ecview.common.model.core.YEmbeddableCollectionEndpoint;
import org.eclipse.emf.ecp.ecview.common.model.core.YEmbeddableSelectionEndpoint;
import org.eclipse.emf.ecp.ecview.common.model.core.YView;
import org.eclipse.emf.ecp.ecview.common.presentation.IWidgetPresentation;
import org.eclipse.emf.ecp.ecview.extension.model.extension.YGridLayout;
import org.eclipse.emf.ecp.ecview.extension.model.extension.YTable;
import org.eclipse.emf.ecp.ecview.extension.model.extension.util.SimpleExtensionModelFactory;
import org.eclipse.emf.ecp.ecview.ui.core.editparts.extension.ITableEditpart;
import org.junit.Before;
import org.junit.Test;
import org.lunifera.runtime.web.ecview.presentation.vaadin.VaadinRenderer;
import org.lunifera.runtime.web.ecview.presentation.vaadin.internal.AbstractVaadinWidgetPresenter;
import org.lunifera.runtime.web.ecview.presentation.vaadin.internal.TablePresentation;
import org.osgi.framework.BundleException;
import org.osgi.service.cm.ConfigurationException;

import com.vaadin.data.Container;
import com.vaadin.data.Container.Indexed;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;

/**
 * Tests the {@link TablePresentation}.
 */
@SuppressWarnings("restriction")
public class TablePresentationTests {

	private SimpleExtensionModelFactory factory = new SimpleExtensionModelFactory();
	private CssLayout rootLayout = new CssLayout();

	/**
	 * Setup tests.
	 * 
	 * @throws ConfigurationException
	 * @throws BundleException
	 */
	@Before
	public void setup() throws ConfigurationException, BundleException {
		UI.setCurrent(new DefaultUI());
		UI.getCurrent().setContent(rootLayout);
	}

	/**
	 * Tests rendering issues.
	 * 
	 * @throws Exception
	 */
	@Test
	// BEGIN SUPRESS CATCH EXCEPTION
	public void test_isRendered_unrender_byModel() throws Exception {
		// END SUPRESS CATCH EXCEPTION
		// build the view model
		// ...> yView
		// ......> yGridLayout
		// .........> yTable
		YView yView = factory.createView();
		YGridLayout yGridlayout = factory.createGridLayout();
		yView.setContent(yGridlayout);
		YTable yTable = factory.createTable();
		yGridlayout.getElements().add(yTable);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		ITableEditpart tableEditpart = DelegatingEditPartManager.getInstance()
				.getEditpart(yTable);
		IWidgetPresentation<Component> presentation = tableEditpart
				.getPresentation();
		assertTrue(presentation.isRendered());
		assertFalse(presentation.isDisposed());

		yGridlayout.getElements().remove(yTable);
		assertFalse(presentation.isRendered());
		assertFalse(presentation.isDisposed());
	}

	/**
	 * Tests the internal structure.
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("unused")
	@Test
	// BEGIN SUPRESS CATCH EXCEPTION
	public void test_InternalStructure() throws Exception {
		// END SUPRESS CATCH EXCEPTION
		// build the view model
		// ...> yView
		// ......> yTable
		YView yView = factory.createView();
		YTable yTable = factory.createTable();
		yView.setContent(yTable);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		ITableEditpart tableEditpart = DelegatingEditPartManager.getInstance()
				.getEditpart(yTable);
		IWidgetPresentation<Component> presentation = tableEditpart
				.getPresentation();
		ComponentContainer baseComponentContainer = (ComponentContainer) presentation
				.getWidget();

		Table label = (Table) unwrapTable(baseComponentContainer);
		assertEquals(1, baseComponentContainer.getComponentCount());

		// assert layout
		CssLayout layout = (CssLayout) baseComponentContainer;
	}

	/**
	 * Test the internal structure based on CSS.
	 * 
	 * @throws Exception
	 */
	@Test
	// BEGIN SUPRESS CATCH EXCEPTION
	public void test_InternalStructure__CSS() throws Exception {
		// END SUPRESS CATCH EXCEPTION
		// build the view model
		// ...> yView
		// ......> yTable
		YView yView = factory.createView();
		YGridLayout yLayout = factory.createGridLayout();
		yView.setContent(yLayout);
		YTable yTable1 = factory.createTable();
		yTable1.setCssID("ID_0815");
		yTable1.setCssClass("anyOtherClass");
		yLayout.getElements().add(yTable1);
		YTable yTable2 = factory.createTable();
		yLayout.getElements().add(yTable2);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		ITableEditpart table1Editpart = DelegatingEditPartManager.getInstance()
				.getEditpart(yTable1);
		ITableEditpart table2Editpart = DelegatingEditPartManager.getInstance()
				.getEditpart(yTable2);
		IWidgetPresentation<Component> table1Presentation = table1Editpart
				.getPresentation();
		IWidgetPresentation<Component> table2Presentation = table2Editpart
				.getPresentation();
		ComponentContainer table1BaseComponentContainer = (ComponentContainer) table1Presentation
				.getWidget();
		ComponentContainer table2BaseComponentContainer = (ComponentContainer) table2Presentation
				.getWidget();

		Table label1 = (Table) unwrapTable(table1BaseComponentContainer);
		Table label2 = (Table) unwrapTable(table2BaseComponentContainer);

		// assert css class
		assertTrue(table1BaseComponentContainer.getStyleName().contains(
				AbstractVaadinWidgetPresenter.CSS_CLASS__CONTROL_BASE));
		assertTrue(table2BaseComponentContainer.getStyleName().contains(
				AbstractVaadinWidgetPresenter.CSS_CLASS__CONTROL_BASE));

		assertTrue(label1.getStyleName().contains("anyOtherClass"));
		assertTrue(label2.getStyleName().contains(
				AbstractVaadinWidgetPresenter.CSS_CLASS__CONTROL));

		// assert css id
		assertEquals("ID_0815", table1BaseComponentContainer.getId());
		assertNull(label1.getId());
		assertEquals(table2Editpart.getId(), table2BaseComponentContainer.getId());
		assertNull(label2.getId());
	}

	/**
	 * Test the internal structure based on CSS.
	 * 
	 * @throws Exception
	 */
	@Test
	// BEGIN SUPRESS CATCH EXCEPTION
	public void test_Bindings() throws Exception {
		// END SUPRESS CATCH EXCEPTION
		// build the view model
		// ...> yView
		// ......> yTable
		YView yView = factory.createView();
		YGridLayout yLayout = factory.createGridLayout();
		yView.setContent(yLayout);
		YTable yTable1 = factory.createTable();
		yLayout.getElements().add(yTable1);
		YTable yTable2 = factory.createTable();
		yLayout.getElements().add(yTable2);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		ITableEditpart label1Editpart = DelegatingEditPartManager.getInstance()
				.getEditpart(yTable1);
		ITableEditpart label2Editpart = DelegatingEditPartManager.getInstance()
				.getEditpart(yTable2);
		IWidgetPresentation<Component> table1Presentation = label1Editpart
				.getPresentation();
		IWidgetPresentation<Component> table2Presentation = label2Editpart
				.getPresentation();
		ComponentContainer table1BaseComponentContainer = (ComponentContainer) table1Presentation
				.getWidget();
		ComponentContainer table2BaseComponentContainer = (ComponentContainer) table2Presentation
				.getWidget();
		Table label1 = (Table) unwrapTable(table1BaseComponentContainer);
		Table label2 = (Table) unwrapTable(table2BaseComponentContainer);

		// start tests
		//
		assertTrue(label1.isVisible());
		assertTrue(label1.isEnabled());
		assertFalse(label1.isReadOnly());

		assertTrue(label2.isVisible());
		assertTrue(label2.isEnabled());
		assertFalse(label2.isReadOnly());

		yTable1.setVisible(false);
		assertFalse(label1.isVisible());

		yTable1.setEnabled(false);
		assertFalse(label1.isEnabled());

	}

	/**
	 * Test the internal structure based on CSS.
	 * 
	 * @throws Exception
	 */
	@Test
	// BEGIN SUPRESS CATCH EXCEPTION
	public void test_CollectionBinding_Native() throws Exception {
		// END SUPRESS CATCH EXCEPTION
		// build the view model
		// ...> yView
		// ......> yText
		YView yView = factory.createView();
		YGridLayout yLayout = factory.createGridLayout();
		yView.setContent(yLayout);
		YTable yTable1 = factory.createTable();
		yLayout.getElements().add(yTable1);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		ITableEditpart table1Editpart = DelegatingEditPartManager.getInstance()
				.getEditpart(yTable1);
		IWidgetPresentation<Component> table1Presentation = table1Editpart
				.getPresentation();
		ComponentContainer table1BaseComponentContainer = (ComponentContainer) table1Presentation
				.getWidget();
		Table table1 = (Table) unwrapTable(table1BaseComponentContainer);

		// start tests
		//

		Container.Indexed container = (Indexed) table1.getContainerDataSource();
		assertEquals(0, container.size());

		// add
		container.addItem("Blabla");
		assertEquals(1, yTable1.getCollection().size());
		assertEquals(1, container.size());

		yTable1.getCollection().add("Huhu");
		assertEquals(2, yTable1.getCollection().size());
		assertEquals(2, container.size());

		// add at index
		yTable1.getCollection().add(0, "First");
		assertEquals("First", yTable1.getCollection().get(0));
		assertEquals("First", container.getItemIds(0, 1).get(0));

		container.addItemAt(0, "Another First");
		assertEquals("Another First", yTable1.getCollection().get(0));
		assertEquals("Another First", container.getItemIds(0, 1).get(0));

		// move
		yTable1.getCollection().move(1, 0);
		assertEquals("First", yTable1.getCollection().get(0));
		assertEquals("First", container.getItemIds(0, 1).get(0));

		// remove all
		container.removeAllItems();
		assertEquals(0, yTable1.getCollection().size());
		assertEquals(0, container.size());

	}

	/**
	 * Test the internal structure based on CSS.
	 * 
	 * @throws Exception
	 */
	@Test
	// BEGIN SUPRESS CATCH EXCEPTION
	public void test_CollectionBinding_TableToTable() throws Exception {
		// END SUPRESS CATCH EXCEPTION
		// build the view model
		// ...> yView
		// ......> yText
		YView yView = factory.createView();
		YGridLayout yLayout = factory.createGridLayout();
		yView.setContent(yLayout);
		YTable yTable1 = factory.createTable();
		yLayout.getElements().add(yTable1);
		YTable yTable2 = factory.createTable();
		yLayout.getElements().add(yTable2);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		ITableEditpart table1Editpart = DelegatingEditPartManager.getInstance()
				.getEditpart(yTable1);
		ITableEditpart table2Editpart = DelegatingEditPartManager.getInstance()
				.getEditpart(yTable2);
		IWidgetPresentation<Component> table1Presentation = table1Editpart
				.getPresentation();
		IWidgetPresentation<Component> table2Presentation = table2Editpart
				.getPresentation();
		ComponentContainer table1BaseComponentContainer = (ComponentContainer) table1Presentation
				.getWidget();
		ComponentContainer table2BaseComponentContainer = (ComponentContainer) table2Presentation
				.getWidget();
		Table table1 = (Table) unwrapTable(table1BaseComponentContainer);
		Table table2 = (Table) unwrapTable(table2BaseComponentContainer);

		Container.Indexed indexedDs1 = (Indexed) table1.getContainerDataSource();
		Container.Indexed indexedDs2 = (Indexed) table2.getContainerDataSource();

		YBindingSet yBindingSet = yView.getOrCreateBindingSet();

		// start tests
		//

		YEmbeddableCollectionEndpoint endp1 = yTable1.createCollectionEndpoint();
		YEmbeddableCollectionEndpoint endp2 = yTable2.createCollectionEndpoint();
		yBindingSet.addBinding(endp1, endp2);

		assertEquals(0, yTable1.getCollection().size());
		assertEquals(0, yTable2.getCollection().size());
		assertEquals(0, indexedDs1.size());
		assertEquals(0, indexedDs2.size());

		// add to yTable1
		yTable1.getCollection().add("Huhu");
		assertEquals(1, yTable2.getCollection().size());
		assertEquals("Huhu", yTable2.getCollection().get(0));
		assertEquals("Huhu", indexedDs1.getItemIds(0, 1).get(0));
		assertEquals("Huhu", indexedDs2.getItemIds(0, 1).get(0));
		assertEquals(1, indexedDs1.size());
		assertEquals(1, indexedDs2.size());

		// add to yTable1
		yTable1.getCollection().add("Huhu2");
		assertEquals(2, yTable2.getCollection().size());
		assertEquals("Huhu", yTable2.getCollection().get(0));
		assertEquals("Huhu2", yTable2.getCollection().get(1));
		assertEquals("Huhu", yTable1.getCollection().get(0));
		assertEquals("Huhu2", yTable1.getCollection().get(1));
		assertEquals("Huhu", indexedDs1.getItemIds(0, 1).get(0));
		assertEquals("Huhu2", indexedDs1.getItemIds(1, 1).get(0));
		assertEquals("Huhu", indexedDs2.getItemIds(0, 1).get(0));
		assertEquals("Huhu2", indexedDs2.getItemIds(1, 1).get(0));
		assertEquals(2, indexedDs1.size());
		assertEquals(2, indexedDs2.size());

		// remove from yTable2
		yTable2.getCollection().remove("Huhu");
		assertEquals(1, yTable1.getCollection().size());
		assertEquals(1, yTable2.getCollection().size());
		assertEquals(1, indexedDs1.size());
		assertEquals(1, indexedDs2.size());

		// add another to yTable2
		yTable2.getCollection().add("Blabla");
		assertEquals(2, yTable1.getCollection().size());
		assertEquals(2, yTable2.getCollection().size());
		assertEquals("Huhu2", yTable1.getCollection().get(0));
		assertEquals("Blabla", yTable1.getCollection().get(1));
		assertEquals("Huhu2", yTable2.getCollection().get(0));
		assertEquals("Blabla", yTable2.getCollection().get(1));
		assertEquals("Huhu2", indexedDs1.getItemIds(0, 1).get(0));
		assertEquals("Blabla", indexedDs1.getItemIds(1, 1).get(0));
		assertEquals("Huhu2", indexedDs2.getItemIds(0, 1).get(0));
		assertEquals("Blabla", indexedDs2.getItemIds(1, 1).get(0));
		assertEquals(2, indexedDs1.size());
		assertEquals(2, indexedDs2.size());

		yTable2.getCollection().move(0, 1);
		assertEquals(2, yTable1.getCollection().size());
		assertEquals(2, yTable2.getCollection().size());
		assertEquals("Blabla", yTable1.getCollection().get(0));
		assertEquals("Huhu2", yTable1.getCollection().get(1));
		assertEquals("Blabla", yTable2.getCollection().get(0));
		assertEquals("Huhu2", yTable2.getCollection().get(1));
		assertEquals("Blabla", indexedDs1.getItemIds(0, 1).get(0));
		assertEquals("Huhu2", indexedDs1.getItemIds(1, 1).get(0));
		assertEquals("Blabla", indexedDs2.getItemIds(0, 1).get(0));
		assertEquals("Huhu2", indexedDs2.getItemIds(1, 1).get(0));
		assertEquals(2, indexedDs1.size());
		assertEquals(2, indexedDs2.size());

		yTable2.getCollection().clear();
		assertEquals(0, yTable1.getCollection().size());
		assertEquals(0, yTable2.getCollection().size());
		assertEquals(0, indexedDs1.size());
		assertEquals(0, indexedDs2.size());
	}

	@Test
	// BEGIN SUPRESS CATCH EXCEPTION
	public void test_SelectionBinding_Single_Native() throws Exception {
		// END SUPRESS CATCH EXCEPTION
		// build the view model
		// ...> yView
		// ......> yText
		YView yView = factory.createView();
		YGridLayout yLayout = factory.createGridLayout();
		yView.setContent(yLayout);
		YTable yTable1 = factory.createTable();
		yLayout.getElements().add(yTable1);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		ITableEditpart table1Editpart = DelegatingEditPartManager.getInstance()
				.getEditpart(yTable1);
		IWidgetPresentation<Component> table1Presentation = table1Editpart
				.getPresentation();
		ComponentContainer table1BaseComponentContainer = (ComponentContainer) table1Presentation
				.getWidget();
		Table table1 = (Table) unwrapTable(table1BaseComponentContainer);

		// start tests
		//
		Container.Indexed container = (Indexed) table1.getContainerDataSource();
		assertEquals(0, container.size());

		assertNull(table1.getValue());
		assertNull(yTable1.getSelection());

		// add
		yTable1.getCollection().add("Huhu");
		yTable1.getCollection().add("Haha");
		assertEquals(2, container.size());

		assertNull(yTable1.getSelection());
		assertNull(table1.getValue());

		// test set selection
		yTable1.setSelection("Huhu");
		assertEquals("Huhu", yTable1.getSelection());
		assertEquals("Huhu", table1.getValue());

		table1.setValue("Haha");
		assertEquals("Haha", yTable1.getSelection());
		assertEquals("Haha", table1.getValue());

		// test set selection null
		table1.setValue(null);
		assertNull(yTable1.getSelection());
		assertNull(table1.getValue());

		table1.setValue("Haha");
		assertEquals("Haha", yTable1.getSelection());
		assertEquals("Haha", table1.getValue());

		yTable1.setSelection(null);
		assertNull(yTable1.getSelection());
		assertNull(table1.getValue());

		// test remove element that is selected
		// add
		yTable1.getCollection().add("Huhu");
		yTable1.getCollection().add("Haha");
		assertEquals(2, container.size());

		yTable1.setSelection("Huhu");
		assertEquals("Huhu", yTable1.getSelection());
		assertEquals("Huhu", table1.getValue());

		yTable1.getCollection().remove("Huhu");
		assertNull(table1.getValue());
		assertNull(yTable1.getSelection());

		// test remove element that is selected
		// add
		yTable1.getCollection().add("Huhu");
		assertEquals(2, container.size());

		yTable1.setSelection("Huhu");
		assertEquals("Huhu", yTable1.getSelection());
		assertEquals("Huhu", table1.getValue());

		table1.setValue(null);
		assertNull(yTable1.getSelection());
		assertNull(table1.getValue());

	}

	@Test
	// BEGIN SUPRESS CATCH EXCEPTION
	public void test_SelectionBinding_Single_EmptyCollection() throws Exception {
		// END SUPRESS CATCH EXCEPTION
		// build the view model
		// ...> yView
		// ......> yText
		YView yView = factory.createView();
		YGridLayout yLayout = factory.createGridLayout();
		yView.setContent(yLayout);
		YTable yTable1 = factory.createTable();
		yLayout.getElements().add(yTable1);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		ITableEditpart table1Editpart = DelegatingEditPartManager.getInstance()
				.getEditpart(yTable1);
		IWidgetPresentation<Component> table1Presentation = table1Editpart
				.getPresentation();
		ComponentContainer table1BaseComponentContainer = (ComponentContainer) table1Presentation
				.getWidget();
		Table table1 = (Table) unwrapTable(table1BaseComponentContainer);

		// start tests
		//
		Container.Indexed container = (Indexed) table1.getContainerDataSource();
		assertEquals(0, container.size());

		assertNull(table1.getValue());
		assertNull(yTable1.getSelection());

		// add
		assertNull(yTable1.getSelection());
		assertNull(table1.getValue());

		// test set selection
		yTable1.setSelection("Huhu");
		assertNull(yTable1.getSelection());
		assertNull(table1.getValue());

	}

	/**
	 * Test the internal structure based on CSS.
	 * 
	 * @throws Exception
	 */
	@Test
	// BEGIN SUPRESS CATCH EXCEPTION
	public void test_SelectionBinding_TableToTable() throws Exception {
		// END SUPRESS CATCH EXCEPTION
		// build the view model
		// ...> yView
		// ......> yText
		YView yView = factory.createView();
		YGridLayout yLayout = factory.createGridLayout();
		yView.setContent(yLayout);
		YTable yTable1 = factory.createTable();
		yLayout.getElements().add(yTable1);
		YTable yTable2 = factory.createTable();
		yLayout.getElements().add(yTable2);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		ITableEditpart table1Editpart = DelegatingEditPartManager.getInstance()
				.getEditpart(yTable1);
		ITableEditpart table2Editpart = DelegatingEditPartManager.getInstance()
				.getEditpart(yTable2);
		IWidgetPresentation<Component> table1Presentation = table1Editpart
				.getPresentation();
		IWidgetPresentation<Component> table2Presentation = table2Editpart
				.getPresentation();
		ComponentContainer table1BaseComponentContainer = (ComponentContainer) table1Presentation
				.getWidget();
		ComponentContainer table2BaseComponentContainer = (ComponentContainer) table2Presentation
				.getWidget();
		Table table1 = (Table) unwrapTable(table1BaseComponentContainer);
		Table table2 = (Table) unwrapTable(table2BaseComponentContainer);

		Container.Indexed indexedDs1 = (Indexed) table1.getContainerDataSource();
		Container.Indexed indexedDs2 = (Indexed) table2.getContainerDataSource();

		YBindingSet yBindingSet = yView.getOrCreateBindingSet();

		// start tests
		//

		YEmbeddableCollectionEndpoint endp1 = yTable1.createCollectionEndpoint();
		YEmbeddableCollectionEndpoint endp2 = yTable2.createCollectionEndpoint();
		yBindingSet.addBinding(endp1, endp2);

		YEmbeddableSelectionEndpoint endpSel1 = yTable1
				.createSelectionEndpoint();
		YEmbeddableSelectionEndpoint endpSel2 = yTable2
				.createSelectionEndpoint();
		yBindingSet.addBinding(endpSel1, endpSel2);

		Container.Indexed container1 = (Indexed) table1.getContainerDataSource();
		Container.Indexed container2 = (Indexed) table2.getContainerDataSource();
		assertEquals(0, container1.size());
		assertEquals(0, container2.size());

		assertNull(table1.getValue());
		assertNull(yTable1.getSelection());

		// add
		yTable1.getCollection().add("Huhu");
		yTable2.getCollection().add("Haha");
		assertEquals(2, container1.size());
		assertEquals(2, container2.size());

		assertNull(yTable1.getSelection());
		assertNull(table1.getValue());
		assertNull(yTable2.getSelection());
		assertNull(table2.getValue());

		// test set selection
		yTable1.setSelection("Huhu");
		assertEquals("Huhu", yTable1.getSelection());
		assertEquals("Huhu", table1.getValue());
		assertEquals("Huhu", yTable2.getSelection());
		assertEquals("Huhu", table2.getValue());

		table1.setValue("Haha");
		assertEquals("Haha", yTable1.getSelection());
		assertEquals("Haha", table1.getValue());
		assertEquals("Haha", yTable2.getSelection());
		assertEquals("Haha", table2.getValue());

		// test set selection null
		table1.setValue(null);
		assertNull(yTable1.getSelection());
		assertNull(table1.getValue());
		assertNull(yTable2.getSelection());
		assertNull(table2.getValue());

		table1.setValue("Haha");
		assertEquals("Haha", yTable1.getSelection());
		assertEquals("Haha", table1.getValue());
		assertEquals("Haha", yTable2.getSelection());
		assertEquals("Haha", table2.getValue());

		table2.setValue(null);
		assertNull(yTable1.getSelection());
		assertNull(table1.getValue());
		assertNull(yTable2.getSelection());
		assertNull(table2.getValue());

		table2.setValue("Haha");
		assertEquals("Haha", yTable1.getSelection());
		assertEquals("Haha", table1.getValue());
		assertEquals("Haha", yTable2.getSelection());
		assertEquals("Haha", table2.getValue());

		yTable1.setSelection(null);
		assertNull(yTable1.getSelection());
		assertNull(table1.getValue());
		assertNull(yTable2.getSelection());
		assertNull(table2.getValue());

		table2.setValue("Haha");
		assertEquals("Haha", yTable1.getSelection());
		assertEquals("Haha", table1.getValue());
		assertEquals("Haha", yTable2.getSelection());
		assertEquals("Haha", table2.getValue());

		yTable2.setSelection(null);
		assertNull(yTable1.getSelection());
		assertNull(table1.getValue());
		assertNull(yTable2.getSelection());
		assertNull(table2.getValue());

		// test remove element that is selected
		// add
		assertEquals(2, container1.size());
		assertEquals(2, container2.size());

		yTable1.setSelection("Huhu");
		assertEquals("Huhu", yTable1.getSelection());
		assertEquals("Huhu", yTable2.getSelection());
		assertEquals("Huhu", table1.getValue());
		assertEquals("Huhu", table2.getValue());

		yTable1.getCollection().remove("Huhu");
		assertNull(table1.getValue());
		assertNull(table2.getValue());
		assertNull(yTable1.getSelection());
		assertNull(yTable2.getSelection());

		// test remove element that is selected
		// add
		yTable2.getCollection().add("Huhu");
		assertEquals(2, container1.size());
		assertEquals(2, container2.size());

		yTable1.setSelection("Huhu");
		assertEquals("Huhu", yTable1.getSelection());
		assertEquals("Huhu", table1.getValue());
		assertEquals("Huhu", yTable2.getSelection());
		assertEquals("Huhu", table2.getValue());

		table2.setValue(null);
		assertNull(yTable1.getSelection());
		assertNull(table1.getValue());
		assertNull(yTable2.getSelection());
		assertNull(table2.getValue());
	}

	/**
	 * Test the automatic disposal of bindings
	 * 
	 * @throws ContextException
	 */
	@Test
	public void testBindingIsDisposed() throws ContextException {
		YView yView = factory.createView();
		YGridLayout yGridlayout = factory.createGridLayout();
		yView.setContent(yGridlayout);
		YTable yTable = factory.createTable();
		yGridlayout.getElements().add(yTable);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		ITableEditpart tableEditpart = DelegatingEditPartManager.getInstance()
				.getEditpart(yTable);
		IWidgetPresentation<Component> presentation = tableEditpart
				.getPresentation();
		assertTrue(presentation.isRendered());
		assertFalse(presentation.isDisposed());
		assertEquals(6, presentation.getUIBindings().size());

		presentation.dispose();
		assertFalse(presentation.isRendered());
		assertTrue(presentation.isDisposed());
		assertEquals(0, presentation.getUIBindings().size());
	}

	/**
	 * Unwraps the component from its parent composite.
	 * 
	 * @param component
	 * @return
	 */
	private Component unwrapTable(Component component) {
		if (component instanceof ComponentContainer) {
			ComponentContainer composite = (ComponentContainer) component;
			Iterator<Component> iter = composite.iterator();
			return iter.next();
		}
		return component;
	}

	/**
	 * Returns the component for the given model element.
	 * 
	 * @param yView
	 *            model element
	 * @return component
	 */
	@SuppressWarnings("unchecked")
	protected Component getComponent(YElement yView) {
		IElementEditpart editpart = DelegatingEditPartManager.getInstance()
				.getEditpart(yView);

		IWidgetPresentation<Component> presentation = null;
		if (editpart instanceof IViewEditpart) {
			presentation = (IWidgetPresentation<Component>) ((IViewEditpart) editpart)
					.getPresentation();
		} else {
			presentation = ((IEmbeddableEditpart) editpart).getPresentation();
		}
		Component widget = presentation.getWidget();
		return widget;
	}
}