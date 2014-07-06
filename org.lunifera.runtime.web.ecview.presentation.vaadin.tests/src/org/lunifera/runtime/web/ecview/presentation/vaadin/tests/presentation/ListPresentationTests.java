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
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

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
import org.eclipse.emf.ecp.ecview.extension.model.extension.YList;
import org.eclipse.emf.ecp.ecview.extension.model.extension.YSelectionType;
import org.eclipse.emf.ecp.ecview.extension.model.extension.YTextField;
import org.eclipse.emf.ecp.ecview.extension.model.extension.util.SimpleExtensionModelFactory;
import org.eclipse.emf.ecp.ecview.ui.core.editparts.extension.IListEditpart;
import org.eclipse.emf.ecp.ecview.ui.core.editparts.extension.ITextFieldEditpart;
import org.junit.Before;
import org.junit.Test;
import org.lunifera.runtime.web.ecview.presentation.vaadin.VaadinRenderer;
import org.lunifera.runtime.web.ecview.presentation.vaadin.internal.AbstractVaadinWidgetPresenter;
import org.lunifera.runtime.web.ecview.presentation.vaadin.internal.ListPresentation;
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
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.AbstractSelect.ItemCaptionMode;

/**
 * Tests the {@link ListPresentation}.
 */
@SuppressWarnings("restriction")
public class ListPresentationTests {

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
		// .........> yList
		YView yView = factory.createView();
		YGridLayout yGridlayout = factory.createGridLayout();
		yView.setContent(yGridlayout);
		YList yList = factory.createList();
		yList.setType(String.class);
		yList.setType(String.class);
		yGridlayout.getElements().add(yList);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		IListEditpart listEditpart = DelegatingEditPartManager.getInstance()
				.getEditpart(yList);
		IWidgetPresentation<Component> presentation = listEditpart
				.getPresentation();
		assertTrue(presentation.isRendered());
		assertFalse(presentation.isDisposed());

		yGridlayout.getElements().remove(yList);
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
		// ......> yList
		YView yView = factory.createView();
		YList yList = factory.createList();
		yList.setType(String.class);
		yView.setContent(yList);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		IListEditpart listEditpart = DelegatingEditPartManager.getInstance()
				.getEditpart(yList);
		IWidgetPresentation<Component> presentation = listEditpart
				.getPresentation();
		ComponentContainer baseComponentContainer = (ComponentContainer) presentation
				.getWidget();

		ListSelect label = (ListSelect) unwrapList(baseComponentContainer);
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
		// ......> yList
		YView yView = factory.createView();
		YGridLayout yLayout = factory.createGridLayout();
		yView.setContent(yLayout);
		YList yList1 = factory.createList();
		yList1.setType(String.class);
		yList1.setCssID("ID_0815");
		yList1.setCssClass("anyOtherClass");
		yLayout.getElements().add(yList1);
		YList yList2 = factory.createList();
		yList2.setType(String.class);
		yLayout.getElements().add(yList2);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		IListEditpart list1Editpart = DelegatingEditPartManager.getInstance()
				.getEditpart(yList1);
		IListEditpart list2Editpart = DelegatingEditPartManager.getInstance()
				.getEditpart(yList2);
		IWidgetPresentation<Component> list1Presentation = list1Editpart
				.getPresentation();
		IWidgetPresentation<Component> list2Presentation = list2Editpart
				.getPresentation();
		ComponentContainer list1BaseComponentContainer = (ComponentContainer) list1Presentation
				.getWidget();
		ComponentContainer list2BaseComponentContainer = (ComponentContainer) list2Presentation
				.getWidget();

		ListSelect label1 = (ListSelect) unwrapList(list1BaseComponentContainer);
		ListSelect label2 = (ListSelect) unwrapList(list2BaseComponentContainer);

		// assert css class
		assertTrue(list1BaseComponentContainer.getStyleName().contains(
				AbstractVaadinWidgetPresenter.CSS_CLASS_CONTROL_BASE));
		assertTrue(list2BaseComponentContainer.getStyleName().contains(
				AbstractVaadinWidgetPresenter.CSS_CLASS_CONTROL_BASE));

		assertTrue(label1.getStyleName().contains("anyOtherClass"));
		assertTrue(label2.getStyleName().contains(
				AbstractVaadinWidgetPresenter.CSS_CLASS_CONTROL));

		// assert css id
		assertEquals("ID_0815", list1BaseComponentContainer.getId());
		assertNull(label1.getId());
		assertEquals(list2Editpart.getId(), list2BaseComponentContainer.getId());
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
		// ......> yList
		YView yView = factory.createView();
		YGridLayout yLayout = factory.createGridLayout();
		yView.setContent(yLayout);
		YList yList1 = factory.createList();
		yList1.setType(String.class);
		yLayout.getElements().add(yList1);
		YList yList2 = factory.createList();
		yList2.setType(String.class);
		yLayout.getElements().add(yList2);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		IListEditpart label1Editpart = DelegatingEditPartManager.getInstance()
				.getEditpart(yList1);
		IListEditpart label2Editpart = DelegatingEditPartManager.getInstance()
				.getEditpart(yList2);
		IWidgetPresentation<Component> list1Presentation = label1Editpart
				.getPresentation();
		IWidgetPresentation<Component> list2Presentation = label2Editpart
				.getPresentation();
		ComponentContainer list1BaseComponentContainer = (ComponentContainer) list1Presentation
				.getWidget();
		ComponentContainer list2BaseComponentContainer = (ComponentContainer) list2Presentation
				.getWidget();
		ListSelect label1 = (ListSelect) unwrapList(list1BaseComponentContainer);
		ListSelect label2 = (ListSelect) unwrapList(list2BaseComponentContainer);

		// start tests
		//
		assertTrue(label1.isVisible());
		assertTrue(label1.isEnabled());
		assertFalse(label1.isReadOnly());

		assertTrue(label2.isVisible());
		assertTrue(label2.isEnabled());
		assertFalse(label2.isReadOnly());

		yList1.setVisible(false);
		assertFalse(label1.isVisible());

		yList1.setEnabled(false);
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
		YList yList1 = factory.createList();
		yList1.setType(String.class);
		yLayout.getElements().add(yList1);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		IListEditpart list1Editpart = DelegatingEditPartManager.getInstance()
				.getEditpart(yList1);
		IWidgetPresentation<Component> list1Presentation = list1Editpart
				.getPresentation();
		ComponentContainer list1BaseComponentContainer = (ComponentContainer) list1Presentation
				.getWidget();
		ListSelect list1 = (ListSelect) unwrapList(list1BaseComponentContainer);

		// start tests
		//

		Container.Indexed container = (Indexed) list1.getContainerDataSource();
		assertEquals(0, container.size());

		// add
		container.addItem("Blabla");
		assertEquals(1, yList1.getCollection().size());
		assertEquals(1, container.size());

		yList1.getCollection().add("Huhu");
		assertEquals(2, yList1.getCollection().size());
		assertEquals(2, container.size());

		// add at index
		yList1.getCollection().add(0, "First");
		assertEquals("First", yList1.getCollection().get(0));
		assertEquals("First", container.getItemIds(0, 1).get(0));

		container.addItemAt(0, "Another First");
		assertEquals("Another First", yList1.getCollection().get(0));
		assertEquals("Another First", container.getItemIds(0, 1).get(0));

		// move
		yList1.getCollection().move(1, 0);
		assertEquals("First", yList1.getCollection().get(0));
		assertEquals("First", container.getItemIds(0, 1).get(0));

		// remove all
		container.removeAllItems();
		assertEquals(0, yList1.getCollection().size());
		assertEquals(0, container.size());

	}

	/**
	 * Test the internal structure based on CSS.
	 * 
	 * @throws Exception
	 */
	@Test
	// BEGIN SUPRESS CATCH EXCEPTION
	public void test_CollectionBinding_ListToList() throws Exception {
		// END SUPRESS CATCH EXCEPTION
		// build the view model
		// ...> yView
		// ......> yText
		YView yView = factory.createView();
		YGridLayout yLayout = factory.createGridLayout();
		yView.setContent(yLayout);
		YList yList1 = factory.createList();
		yList1.setType(String.class);
		yLayout.getElements().add(yList1);
		YList yList2 = factory.createList();
		yList2.setType(String.class);
		yLayout.getElements().add(yList2);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		IListEditpart list1Editpart = DelegatingEditPartManager.getInstance()
				.getEditpart(yList1);
		IListEditpart list2Editpart = DelegatingEditPartManager.getInstance()
				.getEditpart(yList2);
		IWidgetPresentation<Component> list1Presentation = list1Editpart
				.getPresentation();
		IWidgetPresentation<Component> list2Presentation = list2Editpart
				.getPresentation();
		ComponentContainer list1BaseComponentContainer = (ComponentContainer) list1Presentation
				.getWidget();
		ComponentContainer list2BaseComponentContainer = (ComponentContainer) list2Presentation
				.getWidget();
		ListSelect list1 = (ListSelect) unwrapList(list1BaseComponentContainer);
		ListSelect list2 = (ListSelect) unwrapList(list2BaseComponentContainer);

		Container.Indexed indexedDs1 = (Indexed) list1.getContainerDataSource();
		Container.Indexed indexedDs2 = (Indexed) list2.getContainerDataSource();

		YBindingSet yBindingSet = yView.getOrCreateBindingSet();

		// start tests
		//

		YEmbeddableCollectionEndpoint endp1 = yList1.createCollectionEndpoint();
		YEmbeddableCollectionEndpoint endp2 = yList2.createCollectionEndpoint();
		yBindingSet.addBinding(endp1, endp2);

		assertEquals(0, yList1.getCollection().size());
		assertEquals(0, yList2.getCollection().size());
		assertEquals(0, indexedDs1.size());
		assertEquals(0, indexedDs2.size());

		// add to yList1
		yList1.getCollection().add("Huhu");
		assertEquals(1, yList2.getCollection().size());
		assertEquals("Huhu", yList2.getCollection().get(0));
		assertEquals("Huhu", indexedDs1.getItemIds(0, 1).get(0));
		assertEquals("Huhu", indexedDs2.getItemIds(0, 1).get(0));
		assertEquals(1, indexedDs1.size());
		assertEquals(1, indexedDs2.size());

		// add to yList1
		yList1.getCollection().add("Huhu2");
		assertEquals(2, yList2.getCollection().size());
		assertEquals("Huhu", yList2.getCollection().get(0));
		assertEquals("Huhu2", yList2.getCollection().get(1));
		assertEquals("Huhu", yList1.getCollection().get(0));
		assertEquals("Huhu2", yList1.getCollection().get(1));
		assertEquals("Huhu", indexedDs1.getItemIds(0, 1).get(0));
		assertEquals("Huhu2", indexedDs1.getItemIds(1, 1).get(0));
		assertEquals("Huhu", indexedDs2.getItemIds(0, 1).get(0));
		assertEquals("Huhu2", indexedDs2.getItemIds(1, 1).get(0));
		assertEquals(2, indexedDs1.size());
		assertEquals(2, indexedDs2.size());

		// remove from yList2
		yList2.getCollection().remove("Huhu");
		assertEquals(1, yList1.getCollection().size());
		assertEquals(1, yList2.getCollection().size());
		assertEquals(1, indexedDs1.size());
		assertEquals(1, indexedDs2.size());

		// add another to yList2
		yList2.getCollection().add("Blabla");
		assertEquals(2, yList1.getCollection().size());
		assertEquals(2, yList2.getCollection().size());
		assertEquals("Huhu2", yList1.getCollection().get(0));
		assertEquals("Blabla", yList1.getCollection().get(1));
		assertEquals("Huhu2", yList2.getCollection().get(0));
		assertEquals("Blabla", yList2.getCollection().get(1));
		assertEquals("Huhu2", indexedDs1.getItemIds(0, 1).get(0));
		assertEquals("Blabla", indexedDs1.getItemIds(1, 1).get(0));
		assertEquals("Huhu2", indexedDs2.getItemIds(0, 1).get(0));
		assertEquals("Blabla", indexedDs2.getItemIds(1, 1).get(0));
		assertEquals(2, indexedDs1.size());
		assertEquals(2, indexedDs2.size());

		yList2.getCollection().move(0, 1);
		assertEquals(2, yList1.getCollection().size());
		assertEquals(2, yList2.getCollection().size());
		assertEquals("Blabla", yList1.getCollection().get(0));
		assertEquals("Huhu2", yList1.getCollection().get(1));
		assertEquals("Blabla", yList2.getCollection().get(0));
		assertEquals("Huhu2", yList2.getCollection().get(1));
		assertEquals("Blabla", indexedDs1.getItemIds(0, 1).get(0));
		assertEquals("Huhu2", indexedDs1.getItemIds(1, 1).get(0));
		assertEquals("Blabla", indexedDs2.getItemIds(0, 1).get(0));
		assertEquals("Huhu2", indexedDs2.getItemIds(1, 1).get(0));
		assertEquals(2, indexedDs1.size());
		assertEquals(2, indexedDs2.size());

		yList2.getCollection().clear();
		assertEquals(0, yList1.getCollection().size());
		assertEquals(0, yList2.getCollection().size());
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
		YList yList1 = factory.createList();
		yList1.setType(String.class);
		yLayout.getElements().add(yList1);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		IListEditpart list1Editpart = DelegatingEditPartManager.getInstance()
				.getEditpart(yList1);
		IWidgetPresentation<Component> list1Presentation = list1Editpart
				.getPresentation();
		ComponentContainer list1BaseComponentContainer = (ComponentContainer) list1Presentation
				.getWidget();
		ListSelect list1 = (ListSelect) unwrapList(list1BaseComponentContainer);

		// start tests
		//
		Container.Indexed container = (Indexed) list1.getContainerDataSource();
		assertEquals(0, container.size());

		assertNull(list1.getValue());
		assertNull(yList1.getSelection());

		// add
		yList1.getCollection().add("Huhu");
		yList1.getCollection().add("Haha");
		assertEquals(2, container.size());

		assertNull(yList1.getSelection());
		assertNull(list1.getValue());

		// test set selection
		yList1.setSelection("Huhu");
		assertEquals("Huhu", yList1.getSelection());
		assertEquals("Huhu", list1.getValue());

		list1.setValue("Haha");
		assertEquals("Haha", yList1.getSelection());
		assertEquals("Haha", list1.getValue());

		// test set selection null
		list1.setValue(null);
		assertNull(yList1.getSelection());
		assertNull(list1.getValue());

		list1.setValue("Haha");
		assertEquals("Haha", yList1.getSelection());
		assertEquals("Haha", list1.getValue());

		yList1.setSelection(null);
		assertNull(yList1.getSelection());
		assertNull(list1.getValue());

		// test remove element that is selected
		// add
		yList1.getCollection().add("Huhu");
		yList1.getCollection().add("Haha");
		assertEquals(2, container.size());

		yList1.setSelection("Huhu");
		assertEquals("Huhu", yList1.getSelection());
		assertEquals("Huhu", list1.getValue());

		yList1.getCollection().remove("Huhu");
		assertNull(list1.getValue());
		assertNull(yList1.getSelection());

		// test remove element that is selected
		// add
		yList1.getCollection().add("Huhu");
		assertEquals(2, container.size());

		yList1.setSelection("Huhu");
		assertEquals("Huhu", yList1.getSelection());
		assertEquals("Huhu", list1.getValue());

		list1.setValue(null);
		assertNull(yList1.getSelection());
		assertNull(list1.getValue());

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
		YList yList1 = factory.createList();
		yList1.setType(Bar.class);
		yLayout.getElements().add(yList1);
		YTextField yText = factory.createTextField();
		yLayout.getElements().add(yText);

		YBindingSet yBindingSet = yView.getOrCreateBindingSet();
		YDetailValueBindingEndpoint yDetailEndpoint = yList1
				.createSelectionEndpoint().createDetailValueEndpoint();
		yDetailEndpoint.setPropertyPath("myfoo.name");
		yDetailEndpoint.setType(Bar.class);
		yBindingSet.addBinding(yText.createValueEndpoint(), yDetailEndpoint);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		IListEditpart list1Editpart = DelegatingEditPartManager.getInstance()
				.getEditpart(yList1);
		IWidgetPresentation<Component> list1Presentation = list1Editpart
				.getPresentation();
		ComponentContainer list1BaseComponentContainer = (ComponentContainer) list1Presentation
				.getWidget();
		ListSelect list1 = (ListSelect) unwrapList(list1BaseComponentContainer);

		ITextFieldEditpart textEditpart = DelegatingEditPartManager
				.getInstance().getEditpart(yText);
		IWidgetPresentation<Component> textPresentation = textEditpart
				.getPresentation();
		ComponentContainer textBaseComponentContainer = (ComponentContainer) textPresentation
				.getWidget();
		TextField text = (TextField) unwrapList(textBaseComponentContainer);

		// start tests
		//
		Container.Indexed container = (Indexed) list1.getContainerDataSource();
		assertEquals(0, container.size());

		assertNull(list1.getValue());
		assertNull(yList1.getSelection());
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

		yList1.getCollection().add(bar1);
		yList1.getCollection().add(bar2);
		assertEquals(2, container.size());

		assertNull(yList1.getSelection());
		assertNull(list1.getValue());
		assertNull(yText.getValue());
		assertNull(text.getValue());

		// test set selection
		yList1.setSelection(bar1);
		assertEquals("Foo1", yText.getValue());
		assertEquals("Foo1", text.getValue());

		yList1.setSelection(bar2);
		assertEquals("Foo2", yText.getValue());
		assertEquals("Foo2", text.getValue());

		// test set selection null
		yList1.setSelection(null);
		assertNull(yText.getValue());
		assertNull(text.getValue());

		list1.setValue(bar1);
		assertEquals("Foo1", yText.getValue());
		assertEquals("Foo1", text.getValue());

		list1.setValue(bar2);
		assertEquals("Foo2", yText.getValue());
		assertEquals("Foo2", text.getValue());

		// test set selection null
		list1.setValue(null);
		assertNull(yText.getValue());
		assertNull(text.getValue());

		// test remove element that is selected
		// add
		list1.setValue(bar2);
		assertEquals("Foo2", yText.getValue());
		assertEquals("Foo2", text.getValue());

		yList1.getCollection().clear();

		assertNull(yList1.getSelection());
		assertNull(list1.getValue());

		// test setValue to textfield
		yList1.getCollection().add(bar1);
		yList1.getCollection().add(bar2);
		assertEquals(2, container.size());

		yList1.setSelection(bar2);
		assertEquals("Foo2", yText.getValue());
		assertEquals("Foo2", text.getValue());

		yText.setValue("Foo2_1");
		assertEquals("Foo2_1", foo2.getName());
		assertEquals("Foo2_1", text.getValue());

		text.setValue("Foo2_2");
		assertEquals("Foo2_2", foo2.getName());
		assertEquals("Foo2_2", yText.getValue());

		yList1.setSelection(bar1);
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
		YList yList1 = factory.createList();
		yList1.setType(EmfBar.class);
		yList1.setEmfNsURI(ModelPackage.eINSTANCE.getNsURI());
		yLayout.getElements().add(yList1);
		YTextField yText = factory.createTextField();
		yLayout.getElements().add(yText);

		YBindingSet yBindingSet = yView.getOrCreateBindingSet();
		YDetailValueBindingEndpoint yDetailEndpoint = yList1
				.createSelectionEndpoint().createDetailValueEndpoint();
		yDetailEndpoint.setType(EmfBar.class);
		yDetailEndpoint.getFeatures().add(ModelPackage.eINSTANCE.getEmfBar_Myfoo());
		yDetailEndpoint.getFeatures().add(ModelPackage.eINSTANCE.getEmfFoo_Name());
		yBindingSet.addBinding(yText.createValueEndpoint(), yDetailEndpoint);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		IListEditpart list1Editpart = DelegatingEditPartManager.getInstance()
				.getEditpart(yList1);
		IWidgetPresentation<Component> list1Presentation = list1Editpart
				.getPresentation();
		ComponentContainer list1BaseComponentContainer = (ComponentContainer) list1Presentation
				.getWidget();
		ListSelect list1 = (ListSelect) unwrapList(list1BaseComponentContainer);

		ITextFieldEditpart textEditpart = DelegatingEditPartManager
				.getInstance().getEditpart(yText);
		IWidgetPresentation<Component> textPresentation = textEditpart
				.getPresentation();
		ComponentContainer textBaseComponentContainer = (ComponentContainer) textPresentation
				.getWidget();
		TextField text = (TextField) unwrapList(textBaseComponentContainer);

		// start tests
		//
		Container.Indexed container = (Indexed) list1.getContainerDataSource();
		assertEquals(0, container.size());

		assertNull(list1.getValue());
		assertNull(yList1.getSelection());
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

		yList1.getCollection().add(bar1);
		yList1.getCollection().add(bar2);
		assertEquals(2, container.size());

		assertNull(yList1.getSelection());
		assertNull(list1.getValue());
		assertNull(yText.getValue());
		assertNull(text.getValue());

		// test set selection
		yList1.setSelection(bar1);
		assertEquals("Foo1", yText.getValue());
		assertEquals("Foo1", text.getValue());

		yList1.setSelection(bar2);
		assertEquals("Foo2", yText.getValue());
		assertEquals("Foo2", text.getValue());

		// test set selection null
		yList1.setSelection(null);
		assertNull(yText.getValue());
		assertNull(text.getValue());

		list1.setValue(bar1);
		assertEquals("Foo1", yText.getValue());
		assertEquals("Foo1", text.getValue());

		list1.setValue(bar2);
		assertEquals("Foo2", yText.getValue());
		assertEquals("Foo2", text.getValue());

		// test set selection null
		list1.setValue(null);
		assertNull(yText.getValue());
		assertNull(text.getValue());

		// test remove element that is selected
		// add
		list1.setValue(bar2);
		assertEquals("Foo2", yText.getValue());
		assertEquals("Foo2", text.getValue());

		yList1.getCollection().clear();

		assertNull(yList1.getSelection());
		assertNull(list1.getValue());

		// test setValue to textfield
		yList1.getCollection().add(bar1);
		yList1.getCollection().add(bar2);
		assertEquals(2, container.size());

		yList1.setSelection(bar2);
		assertEquals("Foo2", yText.getValue());
		assertEquals("Foo2", text.getValue());

		yText.setValue("Foo2_1");
		assertEquals("Foo2_1", foo2.getName());
		assertEquals("Foo2_1", text.getValue());

		text.setValue("Foo2_2");
		assertEquals("Foo2_2", foo2.getName());
		assertEquals("Foo2_2", yText.getValue());

		yList1.setSelection(bar1);
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
	public void test_SelectionBinding_Multi_Native() throws Exception {
		// END SUPRESS CATCH EXCEPTION
		// build the view model
		// ...> yView
		// ......> yText
		YView yView = factory.createView();
		YGridLayout yLayout = factory.createGridLayout();
		yView.setContent(yLayout);
		YList yList1 = factory.createList();
		yList1.setSelectionType(YSelectionType.MULTI);
		yList1.setType(String.class);
		yLayout.getElements().add(yList1);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		IListEditpart list1Editpart = DelegatingEditPartManager.getInstance()
				.getEditpart(yList1);
		IWidgetPresentation<Component> list1Presentation = list1Editpart
				.getPresentation();
		ComponentContainer list1BaseComponentContainer = (ComponentContainer) list1Presentation
				.getWidget();
		ListSelect list1 = (ListSelect) unwrapList(list1BaseComponentContainer);

		// start tests
		//
		Container.Indexed container = (Indexed) list1.getContainerDataSource();
		assertEquals(0, container.size());

		assertTrue(asList(list1.getValue()).isEmpty());
		assertTrue(yList1.getMultiSelection().isEmpty());

		// add
		yList1.getCollection().add("Huhu");
		yList1.getCollection().add("Haha");
		assertEquals(2, container.size());

		assertTrue(yList1.getMultiSelection().isEmpty());
		assertTrue(asList(list1.getValue()).isEmpty());

		// test set selection by model
		yList1.getMultiSelection().add("Huhu");
		assertEquals("Huhu", yList1.getMultiSelection().get(0));
		assertEquals("Huhu", asList(list1.getValue()).get(0));
		assertEquals(1, yList1.getMultiSelection().size());
		assertEquals(1, asList(list1.getValue()).size());

		yList1.getMultiSelection().add("Haha");
		assertEquals("Huhu", yList1.getMultiSelection().get(0));
		assertEquals("Haha", yList1.getMultiSelection().get(1));
		assertEquals("Huhu", asList(list1.getValue()).get(0));
		assertEquals("Haha", asList(list1.getValue()).get(1));
		assertEquals(2, yList1.getMultiSelection().size());
		assertEquals(2, asList(list1.getValue()).size());

		// remove selection
		yList1.getMultiSelection().remove("Huhu");
		assertEquals("Haha", yList1.getMultiSelection().get(0));
		assertEquals("Haha", asList(list1.getValue()).get(0));
		assertEquals(1, yList1.getMultiSelection().size());
		assertEquals(1, asList(list1.getValue()).size());

		yList1.getMultiSelection().clear();
		assertEquals(0, yList1.getMultiSelection().size());
		assertEquals(0, asList(list1.getValue()).size());

		// test set selection by widget
		List<String> selection = new ArrayList<String>();
		selection.add("Huhu");
		list1.setValue(selection);
		assertEquals("Huhu", yList1.getMultiSelection().get(0));
		assertEquals("Huhu", asList(list1.getValue()).get(0));
		assertEquals(1, yList1.getMultiSelection().size());
		assertEquals(1, asList(list1.getValue()).size());

		selection = new ArrayList<String>();
		selection.add("Huhu");
		selection.add("Haha");
		list1.setValue(selection);
		assertEquals("Huhu", yList1.getMultiSelection().get(0));
		assertEquals("Haha", yList1.getMultiSelection().get(1));
		assertEquals("Huhu", asList(list1.getValue()).get(0));
		assertEquals("Haha", asList(list1.getValue()).get(1));
		assertEquals(2, yList1.getMultiSelection().size());
		assertEquals(2, asList(list1.getValue()).size());

		selection = new ArrayList<String>();
		selection.add("Haha");
		list1.setValue(selection);
		assertEquals("Haha", yList1.getMultiSelection().get(0));
		assertEquals("Haha", asList(list1.getValue()).get(0));
		assertEquals(1, yList1.getMultiSelection().size());
		assertEquals(1, asList(list1.getValue()).size());

		selection = new ArrayList<String>();
		list1.setValue(selection);
		assertEquals(0, yList1.getMultiSelection().size());
		assertEquals(0, asList(list1.getValue()).size());

	}

	private Collection<?> castCollection(Object value) {
		return (Collection<?>) value;
	}

	private List<?> asList(Object value) {
		return new ArrayList<Object>(castCollection(value));
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
		YList yList1 = factory.createList();
		yList1.setType(String.class);
		yLayout.getElements().add(yList1);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		IListEditpart list1Editpart = DelegatingEditPartManager.getInstance()
				.getEditpart(yList1);
		IWidgetPresentation<Component> list1Presentation = list1Editpart
				.getPresentation();
		ComponentContainer list1BaseComponentContainer = (ComponentContainer) list1Presentation
				.getWidget();
		ListSelect list1 = (ListSelect) unwrapList(list1BaseComponentContainer);

		// start tests
		//
		Container.Indexed container = (Indexed) list1.getContainerDataSource();
		assertEquals(0, container.size());

		assertNull(list1.getValue());
		assertNull(yList1.getSelection());

		// add
		assertNull(yList1.getSelection());
		assertNull(list1.getValue());

		// test set selection
		yList1.setSelection("Huhu");
		assertNull(yList1.getSelection());
		assertNull(list1.getValue());
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
		YList yList1 = factory.createList();
		yList1.setType(String.class);
		yList1.setSelectionType(YSelectionType.MULTI);
		yLayout.getElements().add(yList1);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		IListEditpart list1Editpart = DelegatingEditPartManager.getInstance()
				.getEditpart(yList1);
		IWidgetPresentation<Component> list1Presentation = list1Editpart
				.getPresentation();
		ComponentContainer list1BaseComponentContainer = (ComponentContainer) list1Presentation
				.getWidget();
		ListSelect list1 = (ListSelect) unwrapList(list1BaseComponentContainer);

		// start tests
		//
		Container.Indexed container = (Indexed) list1.getContainerDataSource();
		assertEquals(0, container.size());

		assertTrue(asList(list1.getValue()).isEmpty());
		assertTrue(yList1.getMultiSelection().isEmpty());

		// test set selection by model
		yList1.getMultiSelection().add("Huhu");
		assertEquals("Huhu", yList1.getMultiSelection().get(0));
		assertEquals("Huhu", asList(list1.getValue()).get(0));
		assertEquals(1, yList1.getMultiSelection().size());
		assertEquals(1, asList(list1.getValue()).size());

		yList1.getMultiSelection().add("Haha");
		assertEquals("Huhu", yList1.getMultiSelection().get(0));
		assertEquals("Haha", yList1.getMultiSelection().get(1));
		assertEquals("Huhu", asList(list1.getValue()).get(0));
		assertEquals("Haha", asList(list1.getValue()).get(1));
		assertEquals(2, yList1.getMultiSelection().size());
		assertEquals(2, asList(list1.getValue()).size());

		// remove selection
		yList1.getMultiSelection().remove("Huhu");
		assertEquals("Haha", yList1.getMultiSelection().get(0));
		assertEquals("Haha", asList(list1.getValue()).get(0));
		assertEquals(1, yList1.getMultiSelection().size());
		assertEquals(1, asList(list1.getValue()).size());

		yList1.getMultiSelection().clear();
		assertEquals(0, yList1.getMultiSelection().size());
		assertEquals(0, asList(list1.getValue()).size());

		// test set selection by widget
		List<String> selection = new ArrayList<String>();
		selection.add("Huhu");
		list1.setValue(selection);
		assertEquals("Huhu", yList1.getMultiSelection().get(0));
		assertEquals("Huhu", asList(list1.getValue()).get(0));
		assertEquals(1, yList1.getMultiSelection().size());
		assertEquals(1, asList(list1.getValue()).size());

		selection = new ArrayList<String>();
		selection.add("Huhu");
		selection.add("Haha");
		list1.setValue(selection);
		assertEquals("Huhu", yList1.getMultiSelection().get(0));
		assertEquals("Haha", yList1.getMultiSelection().get(1));
		assertEquals("Huhu", asList(list1.getValue()).get(0));
		assertEquals("Haha", asList(list1.getValue()).get(1));
		assertEquals(2, yList1.getMultiSelection().size());
		assertEquals(2, asList(list1.getValue()).size());

		selection = new ArrayList<String>();
		selection.add("Haha");
		list1.setValue(selection);
		assertEquals("Haha", yList1.getMultiSelection().get(0));
		assertEquals("Haha", asList(list1.getValue()).get(0));
		assertEquals(1, yList1.getMultiSelection().size());
		assertEquals(1, asList(list1.getValue()).size());

		selection = new ArrayList<String>();
		list1.setValue(selection);
		assertEquals(0, yList1.getMultiSelection().size());
		assertEquals(0, asList(list1.getValue()).size());
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
		YList yList1 = factory.createList();
		yList1.setType(Bar.class);
		yLayout.getElements().add(yList1);
		YTextField yText = factory.createTextField();
		yLayout.getElements().add(yText);

		YBindingSet yBindingSet = yView.getOrCreateBindingSet();
		YEmbeddableSelectionEndpoint selectionBindingEndpoint = yList1
				.createSelectionEndpoint();
		selectionBindingEndpoint.setAttributePath("myfoo.name");
		yBindingSet.addBinding(yText.createValueEndpoint(),
				selectionBindingEndpoint);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		IListEditpart list1Editpart = DelegatingEditPartManager.getInstance()
				.getEditpart(yList1);
		IWidgetPresentation<Component> list1Presentation = list1Editpart
				.getPresentation();
		ComponentContainer list1BaseComponentContainer = (ComponentContainer) list1Presentation
				.getWidget();
		ListSelect list1 = (ListSelect) unwrapList(list1BaseComponentContainer);

		ITextFieldEditpart textEditpart = DelegatingEditPartManager
				.getInstance().getEditpart(yText);
		IWidgetPresentation<Component> textPresentation = textEditpart
				.getPresentation();
		ComponentContainer textBaseComponentContainer = (ComponentContainer) textPresentation
				.getWidget();
		TextField text = (TextField) unwrapList(textBaseComponentContainer);

		// start tests
		//
		Container.Indexed container = (Indexed) list1.getContainerDataSource();
		assertEquals(0, container.size());

		assertNull(list1.getValue());
		assertNull(yList1.getSelection());
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

		yList1.getCollection().add(bar1);
		yList1.getCollection().add(bar2);
		assertEquals(2, container.size());

		assertNull(yList1.getSelection());
		assertNull(list1.getValue());
		assertNull(yText.getValue());
		assertNull(text.getValue());

		// test set selection
		yList1.setSelection(bar1);
		assertEquals("Foo1", yText.getValue());
		assertEquals("Foo1", text.getValue());

		yList1.setSelection(bar2);
		assertEquals("Foo2", yText.getValue());
		assertEquals("Foo2", text.getValue());

		// test set selection null
		yList1.setSelection(null);
		assertNull(yText.getValue());
		assertNull(text.getValue());

		list1.setValue(bar1);
		assertEquals("Foo1", yText.getValue());
		assertEquals("Foo1", text.getValue());

		list1.setValue(bar2);
		assertEquals("Foo2", yText.getValue());
		assertEquals("Foo2", text.getValue());

		// test set selection null
		list1.setValue(null);
		assertNull(yText.getValue());
		assertNull(text.getValue());

		// test remove element that is selected
		// add
		list1.setValue(bar2);
		assertEquals("Foo2", yText.getValue());
		assertEquals("Foo2", text.getValue());

		yList1.getCollection().clear();

		assertNull(yList1.getSelection());
		assertNull(list1.getValue());

		// test setValue to textfield
		yList1.getCollection().add(bar1);
		yList1.getCollection().add(bar2);
		assertEquals(2, container.size());

		yList1.setSelection(bar2);
		assertEquals("Foo2", yText.getValue());
		assertEquals("Foo2", text.getValue());

		yText.setValue("Foo2_1");
		assertEquals("Foo2_1", foo2.getName());
		assertEquals("Foo2_1", text.getValue());

		text.setValue("Foo2_2");
		assertEquals("Foo2_2", foo2.getName());
		assertEquals("Foo2_2", yText.getValue());

		yList1.setSelection(bar1);
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
	public void test_type_String() throws ContextException {

		// END SUPRESS CATCH EXCEPTION
		// build the view model
		// ...> yView
		// ......> yText
		YView yView = factory.createView();
		YGridLayout yLayout = factory.createGridLayout();
		yView.setContent(yLayout);
		YList yList = factory.createList();
		yList.setType(String.class);
		yLayout.getElements().add(yList);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		IListEditpart listEditpart = DelegatingEditPartManager.getInstance()
				.getEditpart(yList);
		IWidgetPresentation<Component> listPresentation = listEditpart
				.getPresentation();
		ComponentContainer tableBaseComponentContainer = (ComponentContainer) listPresentation
				.getWidget();
		ListSelect list = (ListSelect) unwrapList(tableBaseComponentContainer);

		// start tests
		//
		yList.getCollection().add("Blabla");
		yList.getCollection().add("Huhu");

		// For String values NO BeanItemContainer is prepared.
		Container.Indexed container = (Indexed) list.getContainerDataSource();
		assertEquals(0, container.getContainerPropertyIds().size());

		String itemCaption = list.getItemCaption(container.getItemIds()
				.iterator().next());
		assertEquals("Blabla", itemCaption);
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
		YList yList1 = factory.createList();
		yList1.setSelectionType(YSelectionType.MULTI);
		yList1.setType(String.class);
		yLayout.getElements().add(yList1);
		YList yList2 = factory.createList();
		yList2.setSelectionType(YSelectionType.MULTI);
		yList2.setType(String.class);
		yLayout.getElements().add(yList2);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		IListEditpart list1Editpart = DelegatingEditPartManager.getInstance()
				.getEditpart(yList1);
		IListEditpart list2Editpart = DelegatingEditPartManager.getInstance()
				.getEditpart(yList2);
		IWidgetPresentation<Component> list1Presentation = list1Editpart
				.getPresentation();
		IWidgetPresentation<Component> list2Presentation = list2Editpart
				.getPresentation();
		ComponentContainer list1BaseComponentContainer = (ComponentContainer) list1Presentation
				.getWidget();
		ComponentContainer list2BaseComponentContainer = (ComponentContainer) list2Presentation
				.getWidget();
		ListSelect list1 = (ListSelect) unwrapList(list1BaseComponentContainer);
		ListSelect list2 = (ListSelect) unwrapList(list2BaseComponentContainer);

		// Container.Indexed indexedDs1 = (Indexed)
		// list1.getContainerDataSource();
		// Container.Indexed indexedDs2 = (Indexed)
		// list2.getContainerDataSource();

		YBindingSet yBindingSet = yView.getOrCreateBindingSet();

		// start tests
		//

		YEmbeddableCollectionEndpoint endp1 = yList1.createCollectionEndpoint();
		YEmbeddableCollectionEndpoint endp2 = yList2.createCollectionEndpoint();
		yBindingSet.addBinding(endp1, endp2);

		YEmbeddableMultiSelectionEndpoint endpSel1 = yList1
				.createMultiSelectionEndpoint();
		YEmbeddableMultiSelectionEndpoint endpSel2 = yList2
				.createMultiSelectionEndpoint();
		yBindingSet.addBinding(endpSel1, endpSel2);

		Container.Indexed container1 = (Indexed) list1.getContainerDataSource();
		Container.Indexed container2 = (Indexed) list2.getContainerDataSource();
		assertEquals(0, container1.size());
		assertEquals(0, container2.size());

		assertTrue(asList(list1.getValue()).isEmpty());
		assertTrue(yList1.getMultiSelection().isEmpty());
		assertTrue(asList(list2.getValue()).isEmpty());
		assertTrue(yList2.getMultiSelection().isEmpty());

		// add
		yList1.getCollection().add("Huhu");
		yList2.getCollection().add("Haha");
		assertEquals(2, container1.size());
		assertEquals(2, container2.size());

		assertTrue(asList(list1.getValue()).isEmpty());
		assertTrue(yList1.getMultiSelection().isEmpty());
		assertTrue(asList(list2.getValue()).isEmpty());
		assertTrue(yList2.getMultiSelection().isEmpty());

		// test set selection
		yList1.getMultiSelection().add("Huhu");
		assertEquals("Huhu", yList1.getMultiSelection().get(0));
		assertEquals("Huhu", asList(list1.getValue()).get(0));
		assertEquals(1, yList1.getMultiSelection().size());
		assertEquals(1, asList(list1.getValue()).size());
		assertEquals("Huhu", yList2.getMultiSelection().get(0));
		assertEquals("Huhu", asList(list2.getValue()).get(0));
		assertEquals(1, yList2.getMultiSelection().size());
		assertEquals(1, asList(list2.getValue()).size());

		yList2.getMultiSelection().add("Haha");
		assertEquals("Huhu", yList1.getMultiSelection().get(0));
		assertEquals("Huhu", asList(list1.getValue()).get(0));
		assertEquals("Haha", yList1.getMultiSelection().get(1));
		assertEquals("Haha", asList(list1.getValue()).get(1));
		assertEquals(2, yList1.getMultiSelection().size());
		assertEquals(2, asList(list1.getValue()).size());
		assertEquals("Huhu", yList2.getMultiSelection().get(0));
		assertEquals("Huhu", asList(list2.getValue()).get(0));
		assertEquals("Haha", yList2.getMultiSelection().get(1));
		assertEquals("Haha", asList(list2.getValue()).get(1));
		assertEquals(2, yList2.getMultiSelection().size());
		assertEquals(2, asList(list2.getValue()).size());

		yList1.getMultiSelection().remove("Huhu");
		assertEquals("Haha", yList1.getMultiSelection().get(0));
		assertEquals("Haha", asList(list1.getValue()).get(0));
		assertEquals(1, yList1.getMultiSelection().size());
		assertEquals(1, asList(list1.getValue()).size());
		assertEquals("Haha", yList2.getMultiSelection().get(0));
		assertEquals("Haha", asList(list2.getValue()).get(0));
		assertEquals(1, yList2.getMultiSelection().size());
		assertEquals(1, asList(list2.getValue()).size());

		// clear
		yList2.getMultiSelection().clear();
		assertTrue(asList(list1.getValue()).isEmpty());
		assertTrue(yList1.getMultiSelection().isEmpty());
		assertTrue(asList(list2.getValue()).isEmpty());
		assertTrue(yList2.getMultiSelection().isEmpty());

		// test set selection null
		List<String> selection = new ArrayList<String>();
		selection.add("Huhu");
		list1.setValue(selection);
		assertEquals("Huhu", yList1.getMultiSelection().get(0));
		assertEquals("Huhu", asList(list1.getValue()).get(0));
		assertEquals(1, yList1.getMultiSelection().size());
		assertEquals(1, asList(list1.getValue()).size());
		assertEquals("Huhu", yList2.getMultiSelection().get(0));
		assertEquals("Huhu", asList(list2.getValue()).get(0));
		assertEquals(1, yList2.getMultiSelection().size());
		assertEquals(1, asList(list2.getValue()).size());

		selection.add("Haha");
		list2.setValue(selection);
		assertEquals("Huhu", yList1.getMultiSelection().get(0));
		assertEquals("Huhu", asList(list1.getValue()).get(0));
		assertEquals("Haha", yList1.getMultiSelection().get(1));
		assertEquals("Haha", asList(list1.getValue()).get(1));
		assertEquals(2, yList1.getMultiSelection().size());
		assertEquals(2, asList(list1.getValue()).size());
		assertEquals("Huhu", yList2.getMultiSelection().get(0));
		assertEquals("Huhu", asList(list2.getValue()).get(0));
		assertEquals("Haha", yList2.getMultiSelection().get(1));
		assertEquals("Haha", asList(list2.getValue()).get(1));
		assertEquals(2, yList2.getMultiSelection().size());
		assertEquals(2, asList(list2.getValue()).size());

		list2.setValue(new ArrayList<String>());
		assertTrue(asList(list2.getValue()).isEmpty());
		assertTrue(yList2.getMultiSelection().isEmpty());
		assertTrue(asList(list1.getValue()).isEmpty());
		assertTrue(yList1.getMultiSelection().isEmpty());
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
		YList yList1 = factory.createList();
		yList1.setType(String.class);
		yLayout.getElements().add(yList1);
		YList yList2 = factory.createList();
		yList2.setType(String.class);
		yLayout.getElements().add(yList2);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		IListEditpart list1Editpart = DelegatingEditPartManager.getInstance()
				.getEditpart(yList1);
		IListEditpart list2Editpart = DelegatingEditPartManager.getInstance()
				.getEditpart(yList2);
		IWidgetPresentation<Component> list1Presentation = list1Editpart
				.getPresentation();
		IWidgetPresentation<Component> list2Presentation = list2Editpart
				.getPresentation();
		ComponentContainer list1BaseComponentContainer = (ComponentContainer) list1Presentation
				.getWidget();
		ComponentContainer list2BaseComponentContainer = (ComponentContainer) list2Presentation
				.getWidget();
		ListSelect list1 = (ListSelect) unwrapList(list1BaseComponentContainer);
		ListSelect list2 = (ListSelect) unwrapList(list2BaseComponentContainer);

		Container.Indexed indexedDs1 = (Indexed) list1.getContainerDataSource();
		Container.Indexed indexedDs2 = (Indexed) list2.getContainerDataSource();

		YBindingSet yBindingSet = yView.getOrCreateBindingSet();

		// start tests
		//

		YEmbeddableCollectionEndpoint endp1 = yList1.createCollectionEndpoint();
		YEmbeddableCollectionEndpoint endp2 = yList2.createCollectionEndpoint();
		yBindingSet.addBinding(endp1, endp2);

		YEmbeddableSelectionEndpoint endpSel1 = yList1
				.createSelectionEndpoint();
		YEmbeddableSelectionEndpoint endpSel2 = yList2
				.createSelectionEndpoint();
		yBindingSet.addBinding(endpSel1, endpSel2);

		Container.Indexed container1 = (Indexed) list1.getContainerDataSource();
		Container.Indexed container2 = (Indexed) list2.getContainerDataSource();
		assertEquals(0, container1.size());
		assertEquals(0, container2.size());

		assertNull(list1.getValue());
		assertNull(yList1.getSelection());

		// add
		yList1.getCollection().add("Huhu");
		yList2.getCollection().add("Haha");
		assertEquals(2, container1.size());
		assertEquals(2, container2.size());

		assertNull(yList1.getSelection());
		assertNull(list1.getValue());
		assertNull(yList2.getSelection());
		assertNull(list2.getValue());

		// test set selection
		yList1.setSelection("Huhu");
		assertEquals("Huhu", yList1.getSelection());
		assertEquals("Huhu", list1.getValue());
		assertEquals("Huhu", yList2.getSelection());
		assertEquals("Huhu", list2.getValue());

		list1.setValue("Haha");
		assertEquals("Haha", yList1.getSelection());
		assertEquals("Haha", list1.getValue());
		assertEquals("Haha", yList2.getSelection());
		assertEquals("Haha", list2.getValue());

		// test set selection null
		list1.setValue(null);
		assertNull(yList1.getSelection());
		assertNull(list1.getValue());
		assertNull(yList2.getSelection());
		assertNull(list2.getValue());

		list1.setValue("Haha");
		assertEquals("Haha", yList1.getSelection());
		assertEquals("Haha", list1.getValue());
		assertEquals("Haha", yList2.getSelection());
		assertEquals("Haha", list2.getValue());

		list2.setValue(null);
		assertNull(yList1.getSelection());
		assertNull(list1.getValue());
		assertNull(yList2.getSelection());
		assertNull(list2.getValue());

		list2.setValue("Haha");
		assertEquals("Haha", yList1.getSelection());
		assertEquals("Haha", list1.getValue());
		assertEquals("Haha", yList2.getSelection());
		assertEquals("Haha", list2.getValue());

		yList1.setSelection(null);
		assertNull(yList1.getSelection());
		assertNull(list1.getValue());
		assertNull(yList2.getSelection());
		assertNull(list2.getValue());

		list2.setValue("Haha");
		assertEquals("Haha", yList1.getSelection());
		assertEquals("Haha", list1.getValue());
		assertEquals("Haha", yList2.getSelection());
		assertEquals("Haha", list2.getValue());

		yList2.setSelection(null);
		assertNull(yList1.getSelection());
		assertNull(list1.getValue());
		assertNull(yList2.getSelection());
		assertNull(list2.getValue());

		// test remove element that is selected
		// add
		assertEquals(2, container1.size());
		assertEquals(2, container2.size());

		yList1.setSelection("Huhu");
		assertEquals("Huhu", yList1.getSelection());
		assertEquals("Huhu", yList2.getSelection());
		assertEquals("Huhu", list1.getValue());
		assertEquals("Huhu", list2.getValue());

		yList1.getCollection().remove("Huhu");
		assertNull(list1.getValue());
		assertNull(list2.getValue());
		assertNull(yList1.getSelection());
		assertNull(yList2.getSelection());

		// test remove element that is selected
		// add
		yList2.getCollection().add("Huhu");
		assertEquals(2, container1.size());
		assertEquals(2, container2.size());

		yList1.setSelection("Huhu");
		assertEquals("Huhu", yList1.getSelection());
		assertEquals("Huhu", list1.getValue());
		assertEquals("Huhu", yList2.getSelection());
		assertEquals("Huhu", list2.getValue());

		list2.setValue(null);
		assertNull(yList1.getSelection());
		assertNull(list1.getValue());
		assertNull(yList2.getSelection());
		assertNull(list2.getValue());
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
		YList yList = factory.createList();
		yList.setType(String.class);
		yGridlayout.getElements().add(yList);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		IListEditpart listEditpart = DelegatingEditPartManager.getInstance()
				.getEditpart(yList);
		IWidgetPresentation<Component> presentation = listEditpart
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
	public void test_i18n() throws ContextException {

		// switch the global locale to german
		Locale.setDefault(Locale.GERMAN);

		YView yView = factory.createView();
		YGridLayout yGridlayout = factory.createGridLayout();
		yView.setContent(yGridlayout);
		YList yList = factory.createList();
		yList.setType(String.class);
		yGridlayout.getElements().add(yList);

		// set the i18n key
		yList.setLabelI18nKey(TestI18nService.KEY__AGE);

		// prepare the I18nService and pass it to the renderer
		Map<String, Object> parameter = new HashMap<String, Object>();
		Map<String, Object> services = new HashMap<String, Object>();
		parameter.put(IViewContext.PARAM_SERVICES, services);
		services.put(II18nService.ID, new TestI18nService());

		VaadinRenderer renderer = new VaadinRenderer();
		IViewContext context = renderer.render(rootLayout, yView, parameter);
		IListEditpart editpart = DelegatingEditPartManager.getInstance()
				.getEditpart(yList);
		ListPresentation presentation = editpart.getPresentation();

		ListSelect list = (ListSelect) unwrapList(presentation.getWidget());
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
		YList yList = factory.createList();
		yList.setType(String.class);
		yLayout.getElements().add(yList);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		IListEditpart editpart = DelegatingEditPartManager.getInstance()
				.getEditpart(yList);
		IWidgetPresentation<Component> presentation = editpart
				.getPresentation();
		ComponentContainer baseComponentContainer = (ComponentContainer) presentation
				.getWidget();
		ListSelect list = (ListSelect) unwrapList(presentation.getWidget());

		ValueBean bean = new ValueBean(false);
		YBeanValueBindingEndpoint yBeanBinding = factory
				.createBeanBindingEndpoint();
		yBeanBinding.setBean(bean);
		yBeanBinding.setPropertyPath("boolValue");
		YBindingSet yBindingSet = yView.getOrCreateBindingSet();
		yBindingSet.addBinding(yList.createEditableEndpoint(), yBeanBinding);

		// test binding
		assertFalse(yList.isEditable());
		assertFalse(!list.isReadOnly());
		assertFalse(bean.isBoolValue());

		bean.setBoolValue(true);
		assertTrue(yList.isEditable());
		assertTrue(!list.isReadOnly());
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
		YList yList = factory.createList();
		yList.setType(String.class);
		yLayout.getElements().add(yList);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		IListEditpart editpart = DelegatingEditPartManager.getInstance()
				.getEditpart(yList);
		IWidgetPresentation<Component> presentation = editpart
				.getPresentation();
		ComponentContainer textBaseComponentContainer = (ComponentContainer) presentation
				.getWidget();
		ListSelect list = (ListSelect) unwrapList(presentation.getWidget());

		ValueBean bean = new ValueBean(false);
		YBeanValueBindingEndpoint yBeanBinding = factory
				.createBeanBindingEndpoint();
		yBeanBinding.setBean(bean);
		yBeanBinding.setPropertyPath("boolValue");
		YBindingSet yBindingSet = yView.getOrCreateBindingSet();
		yBindingSet.addBinding(yList.createVisibleEndpoint(), yBeanBinding);

		// test binding
		assertFalse(yList.isVisible());
		assertFalse(list.isVisible());
		assertFalse(bean.isBoolValue());

		bean.setBoolValue(true);
		assertTrue(yList.isVisible());
		assertTrue(list.isVisible());
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
		YList yList = factory.createList();
		yList.setType(String.class);
		yLayout.getElements().add(yList);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		IListEditpart editpart = DelegatingEditPartManager.getInstance()
				.getEditpart(yList);
		IWidgetPresentation<Component> presentation = editpart
				.getPresentation();
		ComponentContainer textBaseComponentContainer = (ComponentContainer) presentation
				.getWidget();
		ListSelect list = (ListSelect) unwrapList(presentation.getWidget());

		ValueBean bean = new ValueBean(false);
		YBeanValueBindingEndpoint yBeanBinding = factory
				.createBeanBindingEndpoint();
		yBeanBinding.setBean(bean);
		yBeanBinding.setPropertyPath("boolValue");
		YBindingSet yBindingSet = yView.getOrCreateBindingSet();
		yBindingSet.addBinding(yList.createEnabledEndpoint(), yBeanBinding);

		// test binding
		assertFalse(yList.isEnabled());
		assertFalse(list.isEnabled());
		assertFalse(bean.isBoolValue());

		bean.setBoolValue(true);
		assertTrue(yList.isEnabled());
		assertTrue(list.isEnabled());
		assertTrue(bean.isBoolValue());
	}

	/**
	 * Unwraps the component from its parent composite.
	 * 
	 * @param component
	 * @return
	 */
	private Component unwrapList(Component component) {
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
