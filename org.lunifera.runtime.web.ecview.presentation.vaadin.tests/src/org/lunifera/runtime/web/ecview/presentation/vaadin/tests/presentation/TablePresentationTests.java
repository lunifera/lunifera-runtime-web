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
package org.lunifera.runtime.web.ecview.presentation.vaadin.tests.presentation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.eclipse.emf.ecp.ecview.common.context.ContextException;
import org.eclipse.emf.ecp.ecview.common.context.II18nService;
import org.eclipse.emf.ecp.ecview.common.context.IViewContext;
import org.eclipse.emf.ecp.ecview.common.editpart.DelegatingEditPartManager;
import org.eclipse.emf.ecp.ecview.common.editpart.IElementEditpart;
import org.eclipse.emf.ecp.ecview.common.editpart.IEmbeddableEditpart;
import org.eclipse.emf.ecp.ecview.common.editpart.IViewEditpart;
import org.eclipse.emf.ecp.ecview.common.model.binding.YBeanValueBindingEndpoint;
import org.eclipse.emf.ecp.ecview.common.model.binding.YBindingSet;
import org.eclipse.emf.ecp.ecview.common.model.binding.YDetailValueBindingEndpoint;
import org.eclipse.emf.ecp.ecview.common.model.core.YElement;
import org.eclipse.emf.ecp.ecview.common.model.core.YEmbeddableCollectionEndpoint;
import org.eclipse.emf.ecp.ecview.common.model.core.YEmbeddableMultiSelectionEndpoint;
import org.eclipse.emf.ecp.ecview.common.model.core.YEmbeddableSelectionEndpoint;
import org.eclipse.emf.ecp.ecview.common.model.core.YView;
import org.eclipse.emf.ecp.ecview.common.presentation.IWidgetPresentation;
import org.eclipse.emf.ecp.ecview.extension.model.extension.YGridLayout;
import org.eclipse.emf.ecp.ecview.extension.model.extension.YLabel;
import org.eclipse.emf.ecp.ecview.extension.model.extension.YSelectionType;
import org.eclipse.emf.ecp.ecview.extension.model.extension.YTable;
import org.eclipse.emf.ecp.ecview.extension.model.extension.YTextField;
import org.eclipse.emf.ecp.ecview.extension.model.extension.util.SimpleExtensionModelFactory;
import org.eclipse.emf.ecp.ecview.ui.core.editparts.extension.ILabelEditpart;
import org.eclipse.emf.ecp.ecview.ui.core.editparts.extension.ITableEditpart;
import org.eclipse.emf.ecp.ecview.ui.core.editparts.extension.ITextFieldEditpart;
import org.junit.Before;
import org.junit.Test;
import org.lunifera.runtime.web.ecview.presentation.vaadin.VaadinRenderer;
import org.lunifera.runtime.web.ecview.presentation.vaadin.internal.AbstractVaadinWidgetPresenter;
import org.lunifera.runtime.web.ecview.presentation.vaadin.internal.TablePresentation;
import org.lunifera.runtime.web.ecview.presentation.vaadin.tests.emf.model.EmfBar;
import org.lunifera.runtime.web.ecview.presentation.vaadin.tests.emf.model.EmfFoo;
import org.lunifera.runtime.web.ecview.presentation.vaadin.tests.emf.model.ModelFactory;
import org.lunifera.runtime.web.ecview.presentation.vaadin.tests.emf.model.ModelPackage;
import org.lunifera.runtime.web.ecview.presentation.vaadin.tests.model.Bar;
import org.lunifera.runtime.web.ecview.presentation.vaadin.tests.model.Foo;
import org.lunifera.runtime.web.ecview.presentation.vaadin.tests.model.ValueBean;
import org.osgi.framework.BundleException;
import org.osgi.service.cm.ConfigurationException;

import com.vaadin.data.Container;
import com.vaadin.data.Container.Indexed;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
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
		yTable.setType(String.class);
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
		yTable.setType(String.class);
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
		yTable1.setType(String.class);
		yLayout.getElements().add(yTable1);
		YTable yTable2 = factory.createTable();
		yTable2.setType(String.class);
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
		assertEquals(table2Editpart.getId(),
				table2BaseComponentContainer.getId());
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
		yTable1.setType(String.class);
		yLayout.getElements().add(yTable1);
		YTable yTable2 = factory.createTable();
		yTable2.setType(String.class);
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
		yTable1.setType(String.class);
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
		yTable1.setType(String.class);
		yLayout.getElements().add(yTable1);
		YTable yTable2 = factory.createTable();
		yTable2.setType(String.class);
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

		Container.Indexed indexedDs1 = (Indexed) table1
				.getContainerDataSource();
		Container.Indexed indexedDs2 = (Indexed) table2
				.getContainerDataSource();

		YBindingSet yBindingSet = yView.getOrCreateBindingSet();

		// start tests
		//

		YEmbeddableCollectionEndpoint endp1 = yTable1
				.createCollectionEndpoint();
		YEmbeddableCollectionEndpoint endp2 = yTable2
				.createCollectionEndpoint();
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
		yTable1.setType(String.class);
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
		yTable1.setType(String.class);
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
		yTable1.setType(String.class);
		yLayout.getElements().add(yTable1);
		YTable yTable2 = factory.createTable();
		yTable2.setType(String.class);
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

		Container.Indexed indexedDs1 = (Indexed) table1
				.getContainerDataSource();
		Container.Indexed indexedDs2 = (Indexed) table2
				.getContainerDataSource();

		YBindingSet yBindingSet = yView.getOrCreateBindingSet();

		// start tests
		//

		YEmbeddableCollectionEndpoint endp1 = yTable1
				.createCollectionEndpoint();
		YEmbeddableCollectionEndpoint endp2 = yTable2
				.createCollectionEndpoint();
		yBindingSet.addBinding(endp1, endp2);

		YEmbeddableSelectionEndpoint endpSel1 = yTable1
				.createSelectionEndpoint();
		YEmbeddableSelectionEndpoint endpSel2 = yTable2
				.createSelectionEndpoint();
		yBindingSet.addBinding(endpSel1, endpSel2);

		Container.Indexed container1 = (Indexed) table1
				.getContainerDataSource();
		Container.Indexed container2 = (Indexed) table2
				.getContainerDataSource();
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
		yTable.setType(String.class);
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

	@Test
	// BEGIN SUPRESS CATCH EXCEPTION
	public void test_SelectionBinding_Multi_EmptyCollection() throws Exception {
		// END SUPRESS CATCH EXCEPTION
		// build the view model
		// ...> yView
		// ......> yText
		YView yView = factory.createView();
		YGridLayout yLayout = factory.createGridLayout();
		yView.setContent(yLayout);
		YTable yTable = factory.createTable();
		yTable.setSelectionType(YSelectionType.MULTI);
		yTable.setType(String.class);
		yLayout.getElements().add(yTable);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		ITableEditpart tableEditpart = DelegatingEditPartManager.getInstance()
				.getEditpart(yTable);
		IWidgetPresentation<Component> tablePresentation = tableEditpart
				.getPresentation();
		ComponentContainer tableBaseComponentContainer = (ComponentContainer) tablePresentation
				.getWidget();
		Table table1 = (Table) unwrapTable(tableBaseComponentContainer);

		// start tests
		//
		Container.Indexed container = (Indexed) table1.getContainerDataSource();
		assertEquals(0, container.size());

		assertTrue(asList(table1.getValue()).isEmpty());
		assertTrue(yTable.getMultiSelection().isEmpty());

		// test set selection by model
		yTable.getMultiSelection().add("Huhu");
		assertEquals("Huhu", yTable.getMultiSelection().get(0));
		assertEquals("Huhu", asList(table1.getValue()).get(0));
		assertEquals(1, yTable.getMultiSelection().size());
		assertEquals(1, asList(table1.getValue()).size());

		yTable.getMultiSelection().add("Haha");
		assertEquals("Huhu", yTable.getMultiSelection().get(0));
		assertEquals("Haha", yTable.getMultiSelection().get(1));
		assertEquals("Huhu", asList(table1.getValue()).get(0));
		assertEquals("Haha", asList(table1.getValue()).get(1));
		assertEquals(2, yTable.getMultiSelection().size());
		assertEquals(2, asList(table1.getValue()).size());

		// remove selection
		yTable.getMultiSelection().remove("Huhu");
		assertEquals("Haha", yTable.getMultiSelection().get(0));
		assertEquals("Haha", asList(table1.getValue()).get(0));
		assertEquals(1, yTable.getMultiSelection().size());
		assertEquals(1, asList(table1.getValue()).size());

		yTable.getMultiSelection().clear();
		assertEquals(0, yTable.getMultiSelection().size());
		assertEquals(0, asList(table1.getValue()).size());

		// test set selection by widget
		List<String> selection = new ArrayList<String>();
		selection.add("Huhu");
		table1.setValue(selection);
		assertEquals("Huhu", yTable.getMultiSelection().get(0));
		assertEquals("Huhu", asList(table1.getValue()).get(0));
		assertEquals(1, yTable.getMultiSelection().size());
		assertEquals(1, asList(table1.getValue()).size());

		selection = new ArrayList<String>();
		selection.add("Huhu");
		selection.add("Haha");
		table1.setValue(selection);
		assertEquals("Huhu", yTable.getMultiSelection().get(0));
		assertEquals("Haha", yTable.getMultiSelection().get(1));
		assertEquals("Huhu", asList(table1.getValue()).get(0));
		assertEquals("Haha", asList(table1.getValue()).get(1));
		assertEquals(2, yTable.getMultiSelection().size());
		assertEquals(2, asList(table1.getValue()).size());

		selection = new ArrayList<String>();
		selection.add("Haha");
		table1.setValue(selection);
		assertEquals("Haha", yTable.getMultiSelection().get(0));
		assertEquals("Haha", asList(table1.getValue()).get(0));
		assertEquals(1, yTable.getMultiSelection().size());
		assertEquals(1, asList(table1.getValue()).size());

		selection = new ArrayList<String>();
		table1.setValue(selection);
		assertEquals(0, yTable.getMultiSelection().size());
		assertEquals(0, asList(table1.getValue()).size());
	}

	/**
	 * Test the internal structure based on CSS.
	 * 
	 * @throws Exception
	 */
	@Test
	// BEGIN SUPRESS CATCH EXCEPTION
	public void test_SelectionBinding_Multi_ListToList() throws Exception {
		// END SUPRESS CATCH EXCEPTION
		// build the view model
		// ...> yView
		// ......> yText
		YView yView = factory.createView();
		YGridLayout yLayout = factory.createGridLayout();
		yView.setContent(yLayout);
		YTable yTable1 = factory.createTable();
		yTable1.setSelectionType(YSelectionType.MULTI);
		yTable1.setType(String.class);
		yLayout.getElements().add(yTable1);
		YTable yTable2 = factory.createTable();
		yTable2.setSelectionType(YSelectionType.MULTI);
		yTable2.setType(String.class);
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

		Container.Indexed indexedDs1 = (Indexed) table1
				.getContainerDataSource();
		Container.Indexed indexedDs2 = (Indexed) table2
				.getContainerDataSource();

		YBindingSet yBindingSet = yView.getOrCreateBindingSet();

		// start tests
		//

		YEmbeddableCollectionEndpoint endp1 = yTable1
				.createCollectionEndpoint();
		YEmbeddableCollectionEndpoint endp2 = yTable2
				.createCollectionEndpoint();
		yBindingSet.addBinding(endp1, endp2);

		YEmbeddableMultiSelectionEndpoint endpSel1 = yTable1
				.createMultiSelectionEndpoint();
		YEmbeddableMultiSelectionEndpoint endpSel2 = yTable2
				.createMultiSelectionEndpoint();
		yBindingSet.addBinding(endpSel1, endpSel2);

		Container.Indexed container1 = (Indexed) table1
				.getContainerDataSource();
		Container.Indexed container2 = (Indexed) table2
				.getContainerDataSource();
		assertEquals(0, container1.size());
		assertEquals(0, container2.size());

		assertTrue(asList(table1.getValue()).isEmpty());
		assertTrue(yTable1.getMultiSelection().isEmpty());
		assertTrue(asList(table2.getValue()).isEmpty());
		assertTrue(yTable2.getMultiSelection().isEmpty());

		// add
		yTable1.getCollection().add("Huhu");
		yTable2.getCollection().add("Haha");
		assertEquals(2, container1.size());
		assertEquals(2, container2.size());

		assertTrue(asList(table1.getValue()).isEmpty());
		assertTrue(yTable1.getMultiSelection().isEmpty());
		assertTrue(asList(table2.getValue()).isEmpty());
		assertTrue(yTable2.getMultiSelection().isEmpty());

		// test set selection
		yTable1.getMultiSelection().add("Huhu");
		assertEquals("Huhu", yTable1.getMultiSelection().get(0));
		assertEquals("Huhu", asList(table1.getValue()).get(0));
		assertEquals(1, yTable1.getMultiSelection().size());
		assertEquals(1, asList(table1.getValue()).size());
		assertEquals("Huhu", yTable2.getMultiSelection().get(0));
		assertEquals("Huhu", asList(table2.getValue()).get(0));
		assertEquals(1, yTable2.getMultiSelection().size());
		assertEquals(1, asList(table2.getValue()).size());

		yTable2.getMultiSelection().add("Haha");
		assertEquals("Huhu", yTable1.getMultiSelection().get(0));
		assertEquals("Huhu", asList(table1.getValue()).get(0));
		assertEquals("Haha", yTable1.getMultiSelection().get(1));
		assertEquals("Haha", asList(table1.getValue()).get(1));
		assertEquals(2, yTable1.getMultiSelection().size());
		assertEquals(2, asList(table1.getValue()).size());
		assertEquals("Huhu", yTable2.getMultiSelection().get(0));
		assertEquals("Huhu", asList(table2.getValue()).get(0));
		assertEquals("Haha", yTable2.getMultiSelection().get(1));
		assertEquals("Haha", asList(table2.getValue()).get(1));
		assertEquals(2, yTable2.getMultiSelection().size());
		assertEquals(2, asList(table2.getValue()).size());

		yTable1.getMultiSelection().remove("Huhu");
		assertEquals("Haha", yTable1.getMultiSelection().get(0));
		assertEquals("Haha", asList(table1.getValue()).get(0));
		assertEquals(1, yTable1.getMultiSelection().size());
		assertEquals(1, asList(table1.getValue()).size());
		assertEquals("Haha", yTable2.getMultiSelection().get(0));
		assertEquals("Haha", asList(table2.getValue()).get(0));
		assertEquals(1, yTable2.getMultiSelection().size());
		assertEquals(1, asList(table2.getValue()).size());

		// clear
		yTable2.getMultiSelection().clear();
		assertTrue(asList(table1.getValue()).isEmpty());
		assertTrue(yTable1.getMultiSelection().isEmpty());
		assertTrue(asList(table2.getValue()).isEmpty());
		assertTrue(yTable2.getMultiSelection().isEmpty());

		// test set selection null
		List<String> selection = new ArrayList<String>();
		selection.add("Huhu");
		table1.setValue(selection);
		assertEquals("Huhu", yTable1.getMultiSelection().get(0));
		assertEquals("Huhu", asList(table1.getValue()).get(0));
		assertEquals(1, yTable1.getMultiSelection().size());
		assertEquals(1, asList(table1.getValue()).size());
		assertEquals("Huhu", yTable2.getMultiSelection().get(0));
		assertEquals("Huhu", asList(table2.getValue()).get(0));
		assertEquals(1, yTable2.getMultiSelection().size());
		assertEquals(1, asList(table2.getValue()).size());

		selection.add("Haha");
		table2.setValue(selection);
		assertEquals("Huhu", yTable1.getMultiSelection().get(0));
		assertEquals("Huhu", asList(table1.getValue()).get(0));
		assertEquals("Haha", yTable1.getMultiSelection().get(1));
		assertEquals("Haha", asList(table1.getValue()).get(1));
		assertEquals(2, yTable1.getMultiSelection().size());
		assertEquals(2, asList(table1.getValue()).size());
		assertEquals("Huhu", yTable2.getMultiSelection().get(0));
		assertEquals("Huhu", asList(table2.getValue()).get(0));
		assertEquals("Haha", yTable2.getMultiSelection().get(1));
		assertEquals("Haha", asList(table2.getValue()).get(1));
		assertEquals(2, yTable2.getMultiSelection().size());
		assertEquals(2, asList(table2.getValue()).size());

		table2.setValue(new ArrayList<String>());
		assertTrue(asList(table2.getValue()).isEmpty());
		assertTrue(yTable2.getMultiSelection().isEmpty());
		assertTrue(asList(table1.getValue()).isEmpty());
		assertTrue(yTable1.getMultiSelection().isEmpty());
	}

	/**
	 * Test the internal structure based on CSS.
	 * 
	 * @throws Exception
	 */
	@Test
	// BEGIN SUPRESS CATCH EXCEPTION
	public void test_SelectionBinding_Single_ListToList() throws Exception {
		// END SUPRESS CATCH EXCEPTION
		// build the view model
		// ...> yView
		// ......> yText
		YView yView = factory.createView();
		YGridLayout yLayout = factory.createGridLayout();
		yView.setContent(yLayout);
		YTable yTable1 = factory.createTable();
		yTable1.setType(String.class);
		yLayout.getElements().add(yTable1);
		YTable yTable2 = factory.createTable();
		yTable2.setType(String.class);
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

		Container.Indexed indexedDs1 = (Indexed) table1
				.getContainerDataSource();
		Container.Indexed indexedDs2 = (Indexed) table2
				.getContainerDataSource();

		YBindingSet yBindingSet = yView.getOrCreateBindingSet();

		// start tests
		//

		YEmbeddableCollectionEndpoint endp1 = yTable1
				.createCollectionEndpoint();
		YEmbeddableCollectionEndpoint endp2 = yTable2
				.createCollectionEndpoint();
		yBindingSet.addBinding(endp1, endp2);

		YEmbeddableSelectionEndpoint endpSel1 = yTable1
				.createSelectionEndpoint();
		YEmbeddableSelectionEndpoint endpSel2 = yTable2
				.createSelectionEndpoint();
		yBindingSet.addBinding(endpSel1, endpSel2);

		Container.Indexed container1 = (Indexed) table1
				.getContainerDataSource();
		Container.Indexed container2 = (Indexed) table2
				.getContainerDataSource();
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

	@Test
	// BEGIN SUPRESS CATCH EXCEPTION
	public void test_SelectionBinding_Single_DetailBinding_ToBean()
			throws Exception {
		// END SUPRESS CATCH EXCEPTION
		// build the view model
		// ...> yView
		// ......> yText
		YView yView = factory.createView();
		YGridLayout yLayout = factory.createGridLayout();
		yView.setContent(yLayout);
		YTable yTable1 = factory.createTable();
		yTable1.setType(Bar.class);
		yLayout.getElements().add(yTable1);
		YTextField yText = factory.createTextField();
		yLayout.getElements().add(yText);

		YBindingSet yBindingSet = yView.getOrCreateBindingSet();
		YDetailValueBindingEndpoint yDetailEndpoint = yTable1
				.createSelectionEndpoint().createDetailValueEndpoint();
		yDetailEndpoint.setPropertyPath("myfoo.name");
		yDetailEndpoint.setType(Bar.class);
		yBindingSet.addBinding(yText.createValueEndpoint(), yDetailEndpoint);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		ITableEditpart table1Editpart = DelegatingEditPartManager.getInstance()
				.getEditpart(yTable1);
		IWidgetPresentation<Component> table1Presentation = table1Editpart
				.getPresentation();
		ComponentContainer table1BaseComponentContainer = (ComponentContainer) table1Presentation
				.getWidget();
		Table table1 = (Table) unwrapTable(table1BaseComponentContainer);

		ITextFieldEditpart textEditpart = DelegatingEditPartManager
				.getInstance().getEditpart(yText);
		IWidgetPresentation<Component> textPresentation = textEditpart
				.getPresentation();
		ComponentContainer textBaseComponentContainer = (ComponentContainer) textPresentation
				.getWidget();
		TextField text = (TextField) unwrapTable(textBaseComponentContainer);

		// start tests
		//
		Container.Indexed container = (Indexed) table1.getContainerDataSource();
		assertEquals(0, container.size());

		assertNull(table1.getValue());
		assertNull(yTable1.getSelection());
		assertNull(yText.getValue());
		assertNull(text.getValue());

		// add
		Bar bar1 = new Bar();
		bar1.setName("Bar1");
		Foo foo1 = new Foo();
		foo1.setName("Foo1");
		bar1.setMyfoo(foo1);

		Bar bar2 = new Bar();
		bar2.setName("Bar2");
		Foo foo2 = new Foo();
		foo2.setName("Foo2");
		bar2.setMyfoo(foo2);

		yTable1.getCollection().add(bar1);
		yTable1.getCollection().add(bar2);
		assertEquals(2, container.size());

		assertNull(yTable1.getSelection());
		assertNull(table1.getValue());
		assertNull(yText.getValue());
		assertNull(text.getValue());

		// test set selection
		yTable1.setSelection(bar1);
		assertEquals("Foo1", yText.getValue());
		assertEquals("Foo1", text.getValue());

		yTable1.setSelection(bar2);
		assertEquals("Foo2", yText.getValue());
		assertEquals("Foo2", text.getValue());

		// test set selection null
		yTable1.setSelection(null);
		assertNull(yText.getValue());
		assertNull(text.getValue());

		table1.setValue(bar1);
		assertEquals("Foo1", yText.getValue());
		assertEquals("Foo1", text.getValue());

		table1.setValue(bar2);
		assertEquals("Foo2", yText.getValue());
		assertEquals("Foo2", text.getValue());

		// test set selection null
		table1.setValue(null);
		assertNull(yText.getValue());
		assertNull(text.getValue());

		// test remove element that is selected
		// add
		table1.setValue(bar2);
		assertEquals("Foo2", yText.getValue());
		assertEquals("Foo2", text.getValue());

		yTable1.getCollection().clear();

		assertNull(yTable1.getSelection());
		assertNull(table1.getValue());

		// test setValue to textfield
		yTable1.getCollection().add(bar1);
		yTable1.getCollection().add(bar2);
		assertEquals(2, container.size());

		yTable1.setSelection(bar2);
		assertEquals("Foo2", yText.getValue());
		assertEquals("Foo2", text.getValue());

		yText.setValue("Foo2_1");
		assertEquals("Foo2_1", foo2.getName());
		assertEquals("Foo2_1", text.getValue());

		text.setValue("Foo2_2");
		assertEquals("Foo2_2", foo2.getName());
		assertEquals("Foo2_2", yText.getValue());

		yTable1.setSelection(bar1);
		assertEquals("Foo1", yText.getValue());
		assertEquals("Foo1", text.getValue());

		yText.setValue("Foo1_1");
		assertEquals("Foo1_1", foo1.getName());
		assertEquals("Foo1_1", text.getValue());

		text.setValue("Foo1_2");
		assertEquals("Foo1_2", foo1.getName());
		assertEquals("Foo1_2", yText.getValue());

	}
	
	@Test
	public void test_SelectionBinding_Single_WithAttributePath()
			throws Exception {
		// END SUPRESS CATCH EXCEPTION
		// build the view model
		// ...> yView
		// ......> yText
		YView yView = factory.createView();
		YGridLayout yLayout = factory.createGridLayout();
		yView.setContent(yLayout);
		YTable yTable1 = factory.createTable();
		yTable1.setType(Bar.class);
		yLayout.getElements().add(yTable1);
		YTextField yText = factory.createTextField();
		yLayout.getElements().add(yText);

		YBindingSet yBindingSet = yView.getOrCreateBindingSet();
		YEmbeddableSelectionEndpoint selectionBindingEndpoint = yTable1
				.createSelectionEndpoint();
		selectionBindingEndpoint.setAttributePath("myfoo.name");
		yBindingSet.addBinding(yText.createValueEndpoint(),
				selectionBindingEndpoint);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		ITableEditpart table1Editpart = DelegatingEditPartManager.getInstance()
				.getEditpart(yTable1);
		IWidgetPresentation<Component> table1Presentation = table1Editpart
				.getPresentation();
		ComponentContainer table1BaseComponentContainer = (ComponentContainer) table1Presentation
				.getWidget();
		Table table1 = (Table) unwrapTable(table1BaseComponentContainer);

		ITextFieldEditpart textEditpart = DelegatingEditPartManager
				.getInstance().getEditpart(yText);
		IWidgetPresentation<Component> textPresentation = textEditpart
				.getPresentation();
		ComponentContainer textBaseComponentContainer = (ComponentContainer) textPresentation
				.getWidget();
		TextField text = (TextField) unwrapTable(textBaseComponentContainer);

		// start tests
		//
		Container.Indexed container = (Indexed) table1.getContainerDataSource();
		assertEquals(0, container.size());

		assertNull(table1.getValue());
		assertNull(yTable1.getSelection());
		assertNull(yText.getValue());
		assertNull(text.getValue());

		// add
		Bar bar1 = new Bar();
		bar1.setName("Bar1");
		Foo foo1 = new Foo();
		foo1.setName("Foo1");
		bar1.setMyfoo(foo1);

		Bar bar2 = new Bar();
		bar2.setName("Bar2");
		Foo foo2 = new Foo();
		foo2.setName("Foo2");
		bar2.setMyfoo(foo2);

		yTable1.getCollection().add(bar1);
		yTable1.getCollection().add(bar2);
		assertEquals(2, container.size());

		assertNull(yTable1.getSelection());
		assertNull(table1.getValue());
		assertNull(yText.getValue());
		assertNull(text.getValue());

		// test set selection
		yTable1.setSelection(bar1);
		assertEquals("Foo1", yText.getValue());
		assertEquals("Foo1", text.getValue());

		yTable1.setSelection(bar2);
		assertEquals("Foo2", yText.getValue());
		assertEquals("Foo2", text.getValue());

		// test set selection null
		yTable1.setSelection(null);
		assertNull(yText.getValue());
		assertNull(text.getValue());

		table1.setValue(bar1);
		assertEquals("Foo1", yText.getValue());
		assertEquals("Foo1", text.getValue());

		table1.setValue(bar2);
		assertEquals("Foo2", yText.getValue());
		assertEquals("Foo2", text.getValue());

		// test set selection null
		table1.setValue(null);
		assertNull(yText.getValue());
		assertNull(text.getValue());

		// test remove element that is selected
		// add
		table1.setValue(bar2);
		assertEquals("Foo2", yText.getValue());
		assertEquals("Foo2", text.getValue());

		yTable1.getCollection().clear();

		assertNull(yTable1.getSelection());
		assertNull(table1.getValue());

		// test setValue to textfield
		yTable1.getCollection().add(bar1);
		yTable1.getCollection().add(bar2);
		assertEquals(2, container.size());

		yTable1.setSelection(bar2);
		assertEquals("Foo2", yText.getValue());
		assertEquals("Foo2", text.getValue());

		yText.setValue("Foo2_1");
		assertEquals("Foo2_1", foo2.getName());
		assertEquals("Foo2_1", text.getValue());

		text.setValue("Foo2_2");
		assertEquals("Foo2_2", foo2.getName());
		assertEquals("Foo2_2", yText.getValue());

		yTable1.setSelection(bar1);
		assertEquals("Foo1", yText.getValue());
		assertEquals("Foo1", text.getValue());

		yText.setValue("Foo1_1");
		assertEquals("Foo1_1", foo1.getName());
		assertEquals("Foo1_1", text.getValue());

		text.setValue("Foo1_2");
		assertEquals("Foo1_2", foo1.getName());
		assertEquals("Foo1_2", yText.getValue());

	}
	@Test
	// BEGIN SUPRESS CATCH EXCEPTION
	public void test_SelectionBinding_Single_DetailBinding_ToEmf()
			throws Exception {
		// END SUPRESS CATCH EXCEPTION
		// build the view model
		// ...> yView
		// ......> yText
		YView yView = factory.createView();
		YGridLayout yLayout = factory.createGridLayout();
		yView.setContent(yLayout);
		YTable yTable1 = factory.createTable();
		yTable1.setType(EmfBar.class);
		yLayout.getElements().add(yTable1);
		YTextField yText = factory.createTextField();
		yLayout.getElements().add(yText);

		YBindingSet yBindingSet = yView.getOrCreateBindingSet();
		YDetailValueBindingEndpoint yDetailEndpoint = yTable1
				.createSelectionEndpoint().createDetailValueEndpoint();
		yDetailEndpoint.setPropertyPath("myfoo.name");
		yDetailEndpoint.setType(EmfBar.class);
		yDetailEndpoint.setEmfNSUri(ModelPackage.eNS_URI);
		yBindingSet.addBinding(yText.createValueEndpoint(), yDetailEndpoint);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		ITableEditpart table1Editpart = DelegatingEditPartManager.getInstance()
				.getEditpart(yTable1);
		IWidgetPresentation<Component> table1Presentation = table1Editpart
				.getPresentation();
		ComponentContainer table1BaseComponentContainer = (ComponentContainer) table1Presentation
				.getWidget();
		Table table1 = (Table) unwrapTable(table1BaseComponentContainer);

		ITextFieldEditpart textEditpart = DelegatingEditPartManager
				.getInstance().getEditpart(yText);
		IWidgetPresentation<Component> textPresentation = textEditpart
				.getPresentation();
		ComponentContainer textBaseComponentContainer = (ComponentContainer) textPresentation
				.getWidget();
		TextField text = (TextField) unwrapTable(textBaseComponentContainer);

		// start tests
		//
		Container.Indexed container = (Indexed) table1.getContainerDataSource();
		assertEquals(0, container.size());

		assertNull(table1.getValue());
		assertNull(yTable1.getSelection());
		assertNull(yText.getValue());
		assertNull(text.getValue());

		// add
		EmfBar bar1 = ModelFactory.eINSTANCE.createEmfBar();
		bar1.setName("Bar1");
		EmfFoo foo1 = ModelFactory.eINSTANCE.createEmfFoo();
		foo1.setName("Foo1");
		bar1.setMyfoo(foo1);

		EmfBar bar2 = ModelFactory.eINSTANCE.createEmfBar();
		bar2.setName("Bar2");
		EmfFoo foo2 = ModelFactory.eINSTANCE.createEmfFoo();
		foo2.setName("Foo2");
		bar2.setMyfoo(foo2);

		yTable1.getCollection().add(bar1);
		yTable1.getCollection().add(bar2);
		assertEquals(2, container.size());

		assertNull(yTable1.getSelection());
		assertNull(table1.getValue());
		assertNull(yText.getValue());
		assertNull(text.getValue());

		// test set selection
		yTable1.setSelection(bar1);
		assertEquals("Foo1", yText.getValue());
		assertEquals("Foo1", text.getValue());

		yTable1.setSelection(bar2);
		assertEquals("Foo2", yText.getValue());
		assertEquals("Foo2", text.getValue());

		// test set selection null
		yTable1.setSelection(null);
		assertNull(yText.getValue());
		assertNull(text.getValue());

		table1.setValue(bar1);
		assertEquals("Foo1", yText.getValue());
		assertEquals("Foo1", text.getValue());

		table1.setValue(bar2);
		assertEquals("Foo2", yText.getValue());
		assertEquals("Foo2", text.getValue());

		// test set selection null
		table1.setValue(null);
		assertNull(yText.getValue());
		assertNull(text.getValue());

		// test remove element that is selected
		// add
		table1.setValue(bar2);
		assertEquals("Foo2", yText.getValue());
		assertEquals("Foo2", text.getValue());

		yTable1.getCollection().clear();

		assertNull(yTable1.getSelection());
		assertNull(table1.getValue());

		// test setValue to textfield
		yTable1.getCollection().add(bar1);
		yTable1.getCollection().add(bar2);
		assertEquals(2, container.size());

		yTable1.setSelection(bar2);
		assertEquals("Foo2", yText.getValue());
		assertEquals("Foo2", text.getValue());

		yText.setValue("Foo2_1");
		assertEquals("Foo2_1", foo2.getName());
		assertEquals("Foo2_1", text.getValue());

		text.setValue("Foo2_2");
		assertEquals("Foo2_2", foo2.getName());
		assertEquals("Foo2_2", yText.getValue());

		yTable1.setSelection(bar1);
		assertEquals("Foo1", yText.getValue());
		assertEquals("Foo1", text.getValue());

		yText.setValue("Foo1_1");
		assertEquals("Foo1_1", foo1.getName());
		assertEquals("Foo1_1", text.getValue());

		text.setValue("Foo1_2");
		assertEquals("Foo1_2", foo1.getName());
		assertEquals("Foo1_2", yText.getValue());

	}

	@Test
	public void test_i18n() throws ContextException {

		// switch the global locale to german
		Locale.setDefault(Locale.GERMAN);

		YView yView = factory.createView();
		YGridLayout yLayout = factory.createGridLayout();
		yView.setContent(yLayout);
		YTable yTable = factory.createTable();
		yTable.setType(String.class);
		yLayout.getElements().add(yTable);

		// set the i18n key
		yTable.setLabelI18nKey(TestI18nService.KEY__AGE);

		// prepare the I18nService and pass it to the renderer
		Map<String, Object> parameter = new HashMap<String, Object>();
		Map<String, Object> services = new HashMap<String, Object>();
		parameter.put(IViewContext.PARAM_SERVICES, services);
		services.put(II18nService.ID, new TestI18nService());

		VaadinRenderer renderer = new VaadinRenderer();
		IViewContext context = renderer.render(rootLayout, yView, parameter);
		ITableEditpart editpart = DelegatingEditPartManager.getInstance()
				.getEditpart(yTable);
		TablePresentation presentation = editpart.getPresentation();

		Table table = (Table) unwrapTable(presentation.getWidget());
		assertEquals("Alter", presentation.getWidget().getCaption());

		context.setLocale(Locale.ENGLISH);
		assertEquals("Age", presentation.getWidget().getCaption());
	}
	
	@Test
	// BEGIN SUPRESS CATCH EXCEPTION
	public void test_Readonly_Binding() throws Exception {
		// END SUPRESS CATCH EXCEPTION
		// build the view model
		YView yView = factory.createView();
		YGridLayout yLayout = factory.createGridLayout();
		yView.setContent(yLayout);
		YTable yTable = factory.createTable();
		yTable.setType(String.class);
		yLayout.getElements().add(yTable);
		
		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		ITableEditpart editpart = DelegatingEditPartManager
				.getInstance().getEditpart(yTable);
		IWidgetPresentation<Component> presentation = editpart
				.getPresentation();
		ComponentContainer baseComponentContainer = (ComponentContainer) presentation
				.getWidget();
		Table table = (Table) unwrapTable(presentation.getWidget());		
		
		ValueBean bean = new ValueBean(false);
		YBeanValueBindingEndpoint yBeanBinding = factory.createBeanBindingEndpoint();
		yBeanBinding.setBean(bean);
		yBeanBinding.setPropertyPath("boolValue");
		YBindingSet yBindingSet = yView.getOrCreateBindingSet();
		yBindingSet.addBinding(yTable.createEditableEndpoint(),
				yBeanBinding);

		// test binding
		assertFalse(yTable.isEditable());
		assertFalse(!table.isReadOnly());
		assertFalse(bean.isBoolValue());
		
		bean.setBoolValue(true);
		assertTrue(yTable.isEditable());
		assertTrue(!table.isReadOnly());
		assertTrue(bean.isBoolValue());
	}
	
	@Test
	// BEGIN SUPRESS CATCH EXCEPTION
	public void test_Visible_Binding() throws Exception {
		// END SUPRESS CATCH EXCEPTION
		// build the view model
		YView yView = factory.createView();
		YGridLayout yLayout = factory.createGridLayout();
		yView.setContent(yLayout);
		YTable yTable = factory.createTable();
		yTable.setType(String.class);
		yLayout.getElements().add(yTable);
		
		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		ITableEditpart editpart = DelegatingEditPartManager
				.getInstance().getEditpart(yTable);
		IWidgetPresentation<Component> presentation = editpart
				.getPresentation();
		ComponentContainer textBaseComponentContainer = (ComponentContainer) presentation
				.getWidget();
		Table table = (Table) unwrapTable(presentation.getWidget());		
		
		ValueBean bean = new ValueBean(false);
		YBeanValueBindingEndpoint yBeanBinding = factory.createBeanBindingEndpoint();
		yBeanBinding.setBean(bean);
		yBeanBinding.setPropertyPath("boolValue");
		YBindingSet yBindingSet = yView.getOrCreateBindingSet();
		yBindingSet.addBinding(yTable.createVisibleEndpoint(),
				yBeanBinding);

		// test binding
		assertFalse(yTable.isVisible());
		assertFalse(table.isVisible());
		assertFalse(bean.isBoolValue());
		
		bean.setBoolValue(true);
		assertTrue(yTable.isVisible());
		assertTrue(table.isVisible());
		assertTrue(bean.isBoolValue());
	}
	
	@Test
	// BEGIN SUPRESS CATCH EXCEPTION
	public void test_Enabled_Binding() throws Exception {
		// END SUPRESS CATCH EXCEPTION
		// build the view model
		YView yView = factory.createView();
		YGridLayout yLayout = factory.createGridLayout();
		yView.setContent(yLayout);
		YTable yTable = factory.createTable();
		yTable.setType(String.class);
		yLayout.getElements().add(yTable);
		
		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		ITableEditpart editpart = DelegatingEditPartManager
				.getInstance().getEditpart(yTable);
		IWidgetPresentation<Component> presentation = editpart
				.getPresentation();
		ComponentContainer textBaseComponentContainer = (ComponentContainer) presentation
				.getWidget();
		Table table = (Table) unwrapTable(presentation.getWidget());		
		
		ValueBean bean = new ValueBean(false);
		YBeanValueBindingEndpoint yBeanBinding = factory.createBeanBindingEndpoint();
		yBeanBinding.setBean(bean);
		yBeanBinding.setPropertyPath("boolValue");
		YBindingSet yBindingSet = yView.getOrCreateBindingSet();
		yBindingSet.addBinding(yTable.createEnabledEndpoint(),
				yBeanBinding);

		// test binding
		assertFalse(yTable.isEnabled());
		assertFalse(table.isEnabled());
		assertFalse(bean.isBoolValue());
		
		bean.setBoolValue(true);
		assertTrue(yTable.isEnabled());
		assertTrue(table.isEnabled());
		assertTrue(bean.isBoolValue());
	}
	
	@Test
	public void test_type_String() throws ContextException {
		fail("Captions don't look good");
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

	private Collection<?> castCollection(Object value) {
		return (Collection<?>) value;
	}

	private List<?> asList(Object value) {
		return value != null ? new ArrayList<Object>(castCollection(value))
				: new ArrayList<Object>();
	}
}
