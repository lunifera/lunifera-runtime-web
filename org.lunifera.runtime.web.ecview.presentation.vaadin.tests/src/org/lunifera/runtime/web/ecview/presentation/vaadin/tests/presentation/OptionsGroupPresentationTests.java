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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.lunifera.ecview.core.common.context.ContextException;
import org.lunifera.ecview.core.common.context.II18nService;
import org.lunifera.ecview.core.common.context.IViewContext;
import org.lunifera.ecview.core.common.editpart.DelegatingEditPartManager;
import org.lunifera.ecview.core.common.editpart.IElementEditpart;
import org.lunifera.ecview.core.common.editpart.IEmbeddableEditpart;
import org.lunifera.ecview.core.common.editpart.IViewEditpart;
import org.lunifera.ecview.core.common.model.binding.YBeanValueBindingEndpoint;
import org.lunifera.ecview.core.common.model.binding.YBindingSet;
import org.lunifera.ecview.core.common.model.binding.YDetailValueBindingEndpoint;
import org.lunifera.ecview.core.common.model.core.YElement;
import org.lunifera.ecview.core.common.model.core.YEmbeddableCollectionEndpoint;
import org.lunifera.ecview.core.common.model.core.YEmbeddableMultiSelectionEndpoint;
import org.lunifera.ecview.core.common.model.core.YEmbeddableSelectionEndpoint;
import org.lunifera.ecview.core.common.model.core.YView;
import org.lunifera.ecview.core.common.presentation.IWidgetPresentation;
import org.lunifera.ecview.core.extension.model.extension.YGridLayout;
import org.lunifera.ecview.core.extension.model.extension.YOptionsGroup;
import org.lunifera.ecview.core.extension.model.extension.YSelectionType;
import org.lunifera.ecview.core.extension.model.extension.YTextField;
import org.lunifera.ecview.core.extension.model.extension.util.SimpleExtensionModelFactory;
import org.lunifera.ecview.core.ui.core.editparts.extension.IOptionsGroupEditpart;
import org.lunifera.ecview.core.ui.core.editparts.extension.ITextFieldEditpart;
import org.lunifera.runtime.web.ecview.presentation.vaadin.VaadinRenderer;
import org.lunifera.runtime.web.ecview.presentation.vaadin.common.AbstractVaadinWidgetPresenter;
import org.lunifera.runtime.web.ecview.presentation.vaadin.internal.ListPresentation;
import org.lunifera.runtime.web.ecview.presentation.vaadin.internal.OptionsGroupPresentation;
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
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;

/**
 * Tests the {@link ListPresentation}.
 */
@SuppressWarnings("restriction")
public class OptionsGroupPresentationTests {

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
		// .........> yOptionsGroup
		YView yView = factory.createView();
		YGridLayout yGridlayout = factory.createGridLayout();
		yView.setContent(yGridlayout);
		YOptionsGroup yOptionsGroup = factory.createOptionsGroup();
		yOptionsGroup.setType(String.class);
		yGridlayout.getElements().add(yOptionsGroup);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		IOptionsGroupEditpart optionsGroupEditpart = DelegatingEditPartManager
				.getInstance().getEditpart(yOptionsGroup);
		IWidgetPresentation<Component> presentation = optionsGroupEditpart
				.getPresentation();
		assertTrue(presentation.isRendered());
		assertFalse(presentation.isDisposed());

		yGridlayout.getElements().remove(yOptionsGroup);
		assertFalse(presentation.isRendered());
		assertFalse(presentation.isDisposed());
	}

	/**
	 * Tests the internal structure.
	 * 
	 * @throws Exception
	 */
	@Test
	// BEGIN SUPRESS CATCH EXCEPTION
	public void test_InternalStructure() throws Exception {
		// END SUPRESS CATCH EXCEPTION
		// build the view model
		// ...> yView
		// ......> yOptionsGroup
		YView yView = factory.createView();
		YOptionsGroup yOptionsGroup = factory.createOptionsGroup();
		yOptionsGroup.setType(String.class);
		yView.setContent(yOptionsGroup);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		IOptionsGroupEditpart optionsGroupEditpart = DelegatingEditPartManager
				.getInstance().getEditpart(yOptionsGroup);
		IWidgetPresentation<Component> presentation = optionsGroupEditpart
				.getPresentation();

		OptionGroup group = (OptionGroup) presentation.getWidget();
		assertNotNull(group);

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
		// ......> yOptionsGroup
		YView yView = factory.createView();
		YGridLayout yLayout = factory.createGridLayout();
		yView.setContent(yLayout);
		YOptionsGroup yOptionsGroup1 = factory.createOptionsGroup();
		yOptionsGroup1.setType(String.class);
		yOptionsGroup1.setCssID("ID_0815");
		yOptionsGroup1.setCssClass("anyOtherClass");
		yLayout.getElements().add(yOptionsGroup1);
		YOptionsGroup yOptionsGroup2 = factory.createOptionsGroup();
		yOptionsGroup2.setType(String.class);
		yLayout.getElements().add(yOptionsGroup2);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		IOptionsGroupEditpart optionsGroup1Editpart = DelegatingEditPartManager
				.getInstance().getEditpart(yOptionsGroup1);
		IOptionsGroupEditpart optionsGroup2Editpart = DelegatingEditPartManager
				.getInstance().getEditpart(yOptionsGroup2);
		IWidgetPresentation<Component> optionsGroup1Presentation = optionsGroup1Editpart
				.getPresentation();
		IWidgetPresentation<Component> optionsGroup2Presentation = optionsGroup2Editpart
				.getPresentation();

		OptionGroup label1 = (OptionGroup) optionsGroup1Presentation
				.getWidget();
		OptionGroup label2 = (OptionGroup) optionsGroup2Presentation
				.getWidget();

		// assert css class

		assertTrue(label1.getStyleName().contains("anyOtherClass"));
		assertTrue(label2.getStyleName().contains(
				AbstractVaadinWidgetPresenter.CSS_CLASS_CONTROL));

		// assert css id
		assertEquals("ID_0815", label1.getId());
		assertEquals(optionsGroup2Editpart.getId(), label2.getId());
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
		// ......> yOptionsGroup
		YView yView = factory.createView();
		YGridLayout yLayout = factory.createGridLayout();
		yView.setContent(yLayout);
		YOptionsGroup yOptionsGroup1 = factory.createOptionsGroup();
		yOptionsGroup1.setType(String.class);
		yLayout.getElements().add(yOptionsGroup1);
		YOptionsGroup yOptionsGroup2 = factory.createOptionsGroup();
		yOptionsGroup2.setType(String.class);
		yLayout.getElements().add(yOptionsGroup2);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		IOptionsGroupEditpart label1Editpart = DelegatingEditPartManager
				.getInstance().getEditpart(yOptionsGroup1);
		IOptionsGroupEditpart label2Editpart = DelegatingEditPartManager
				.getInstance().getEditpart(yOptionsGroup2);
		IWidgetPresentation<Component> optionsGroup1Presentation = label1Editpart
				.getPresentation();
		IWidgetPresentation<Component> optionsGroup2Presentation = label2Editpart
				.getPresentation();
		OptionGroup label1 = (OptionGroup) optionsGroup1Presentation
				.getWidget();
		OptionGroup label2 = (OptionGroup) optionsGroup2Presentation
				.getWidget();

		// start tests
		//
		assertTrue(label1.isVisible());
		assertTrue(label1.isEnabled());
		assertFalse(label1.isReadOnly());

		assertTrue(label2.isVisible());
		assertTrue(label2.isEnabled());
		assertFalse(label2.isReadOnly());

		yOptionsGroup1.setVisible(false);
		assertFalse(label1.isVisible());

		yOptionsGroup1.setEnabled(false);
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
		YOptionsGroup yOptionsGroup1 = factory.createOptionsGroup();
		yOptionsGroup1.setType(String.class);
		yLayout.getElements().add(yOptionsGroup1);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		IOptionsGroupEditpart optionsGroup1Editpart = DelegatingEditPartManager
				.getInstance().getEditpart(yOptionsGroup1);
		IWidgetPresentation<Component> optionsGroup1Presentation = optionsGroup1Editpart
				.getPresentation();
		OptionGroup optionsGroup1 = (OptionGroup) optionsGroup1Presentation
				.getWidget();

		// start tests
		//

		Container.Indexed container = (Indexed) optionsGroup1
				.getContainerDataSource();
		assertEquals(0, container.size());

		// add
		container.addItem("Blabla");
		assertEquals(1, yOptionsGroup1.getCollection().size());
		assertEquals(1, container.size());

		yOptionsGroup1.getCollection().add("Huhu");
		assertEquals(2, yOptionsGroup1.getCollection().size());
		assertEquals(2, container.size());

		// add at index
		yOptionsGroup1.getCollection().add(0, "First");
		assertEquals("First", yOptionsGroup1.getCollection().get(0));
		assertEquals("First", container.getItemIds(0, 1).get(0));

		container.addItemAt(0, "Another First");
		assertEquals("Another First", yOptionsGroup1.getCollection().get(0));
		assertEquals("Another First", container.getItemIds(0, 1).get(0));

		// move
		yOptionsGroup1.getCollection().move(1, 0);
		assertEquals("First", yOptionsGroup1.getCollection().get(0));
		assertEquals("First", container.getItemIds(0, 1).get(0));

		// remove all
		container.removeAllItems();
		assertEquals(0, yOptionsGroup1.getCollection().size());
		assertEquals(0, container.size());

	}

	/**
	 * Test the internal structure based on CSS.
	 * 
	 * @throws Exception
	 */
	@Test
	// BEGIN SUPRESS CATCH EXCEPTION
	public void test_CollectionBinding_OptionGroupToOptionGroup()
			throws Exception {
		// END SUPRESS CATCH EXCEPTION
		// build the view model
		// ...> yView
		// ......> yText
		YView yView = factory.createView();
		YGridLayout yLayout = factory.createGridLayout();
		yView.setContent(yLayout);
		YOptionsGroup yOptionsGroup1 = factory.createOptionsGroup();
		yOptionsGroup1.setType(String.class);
		yLayout.getElements().add(yOptionsGroup1);
		YOptionsGroup yOptionsGroup2 = factory.createOptionsGroup();
		yOptionsGroup2.setType(String.class);
		yLayout.getElements().add(yOptionsGroup2);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		IOptionsGroupEditpart optionsGroup1Editpart = DelegatingEditPartManager
				.getInstance().getEditpart(yOptionsGroup1);
		IOptionsGroupEditpart optionsGroup2Editpart = DelegatingEditPartManager
				.getInstance().getEditpart(yOptionsGroup2);
		IWidgetPresentation<Component> optionsGroup1Presentation = optionsGroup1Editpart
				.getPresentation();
		IWidgetPresentation<Component> optionsGroup2Presentation = optionsGroup2Editpart
				.getPresentation();
		OptionGroup optionsGroup1 = (OptionGroup) optionsGroup1Presentation
				.getWidget();
		OptionGroup optionsGroup2 = (OptionGroup) optionsGroup2Presentation
				.getWidget();

		Container.Indexed indexedDs1 = (Indexed) optionsGroup1
				.getContainerDataSource();
		Container.Indexed indexedDs2 = (Indexed) optionsGroup2
				.getContainerDataSource();

		YBindingSet yBindingSet = yView.getOrCreateBindingSet();

		// start tests
		//

		YEmbeddableCollectionEndpoint endp1 = yOptionsGroup1
				.createCollectionEndpoint();
		YEmbeddableCollectionEndpoint endp2 = yOptionsGroup2
				.createCollectionEndpoint();
		yBindingSet.addBinding(endp1, endp2);

		assertEquals(0, yOptionsGroup1.getCollection().size());
		assertEquals(0, yOptionsGroup2.getCollection().size());
		assertEquals(0, indexedDs1.size());
		assertEquals(0, indexedDs2.size());

		// add to yOptionsGroup1
		yOptionsGroup1.getCollection().add("Huhu");
		assertEquals(1, yOptionsGroup2.getCollection().size());
		assertEquals("Huhu", yOptionsGroup2.getCollection().get(0));
		assertEquals("Huhu", indexedDs1.getItemIds(0, 1).get(0));
		assertEquals("Huhu", indexedDs2.getItemIds(0, 1).get(0));
		assertEquals(1, indexedDs1.size());
		assertEquals(1, indexedDs2.size());

		// add to yOptionsGroup1
		yOptionsGroup1.getCollection().add("Huhu2");
		assertEquals(2, yOptionsGroup2.getCollection().size());
		assertEquals("Huhu", yOptionsGroup2.getCollection().get(0));
		assertEquals("Huhu2", yOptionsGroup2.getCollection().get(1));
		assertEquals("Huhu", yOptionsGroup1.getCollection().get(0));
		assertEquals("Huhu2", yOptionsGroup1.getCollection().get(1));
		assertEquals("Huhu", indexedDs1.getItemIds(0, 1).get(0));
		assertEquals("Huhu2", indexedDs1.getItemIds(1, 1).get(0));
		assertEquals("Huhu", indexedDs2.getItemIds(0, 1).get(0));
		assertEquals("Huhu2", indexedDs2.getItemIds(1, 1).get(0));
		assertEquals(2, indexedDs1.size());
		assertEquals(2, indexedDs2.size());

		// remove from yOptionsGroup2
		yOptionsGroup2.getCollection().remove("Huhu");
		assertEquals(1, yOptionsGroup1.getCollection().size());
		assertEquals(1, yOptionsGroup2.getCollection().size());
		assertEquals(1, indexedDs1.size());
		assertEquals(1, indexedDs2.size());

		// add another to yOptionsGroup2
		yOptionsGroup2.getCollection().add("Blabla");
		assertEquals(2, yOptionsGroup1.getCollection().size());
		assertEquals(2, yOptionsGroup2.getCollection().size());
		assertEquals("Huhu2", yOptionsGroup1.getCollection().get(0));
		assertEquals("Blabla", yOptionsGroup1.getCollection().get(1));
		assertEquals("Huhu2", yOptionsGroup2.getCollection().get(0));
		assertEquals("Blabla", yOptionsGroup2.getCollection().get(1));
		assertEquals("Huhu2", indexedDs1.getItemIds(0, 1).get(0));
		assertEquals("Blabla", indexedDs1.getItemIds(1, 1).get(0));
		assertEquals("Huhu2", indexedDs2.getItemIds(0, 1).get(0));
		assertEquals("Blabla", indexedDs2.getItemIds(1, 1).get(0));
		assertEquals(2, indexedDs1.size());
		assertEquals(2, indexedDs2.size());

		yOptionsGroup2.getCollection().move(0, 1);
		assertEquals(2, yOptionsGroup1.getCollection().size());
		assertEquals(2, yOptionsGroup2.getCollection().size());
		assertEquals("Blabla", yOptionsGroup1.getCollection().get(0));
		assertEquals("Huhu2", yOptionsGroup1.getCollection().get(1));
		assertEquals("Blabla", yOptionsGroup2.getCollection().get(0));
		assertEquals("Huhu2", yOptionsGroup2.getCollection().get(1));
		assertEquals("Blabla", indexedDs1.getItemIds(0, 1).get(0));
		assertEquals("Huhu2", indexedDs1.getItemIds(1, 1).get(0));
		assertEquals("Blabla", indexedDs2.getItemIds(0, 1).get(0));
		assertEquals("Huhu2", indexedDs2.getItemIds(1, 1).get(0));
		assertEquals(2, indexedDs1.size());
		assertEquals(2, indexedDs2.size());

		yOptionsGroup2.getCollection().clear();
		assertEquals(0, yOptionsGroup1.getCollection().size());
		assertEquals(0, yOptionsGroup2.getCollection().size());
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
		YOptionsGroup yOptionsGroup1 = factory.createOptionsGroup();
		yOptionsGroup1.setType(String.class);
		yLayout.getElements().add(yOptionsGroup1);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		IOptionsGroupEditpart optionsGroup1Editpart = DelegatingEditPartManager
				.getInstance().getEditpart(yOptionsGroup1);
		IWidgetPresentation<Component> optionsGroup1Presentation = optionsGroup1Editpart
				.getPresentation();
		OptionGroup optionsGroup1 = (OptionGroup) optionsGroup1Presentation
				.getWidget();

		// start tests
		//
		Container.Indexed container = (Indexed) optionsGroup1
				.getContainerDataSource();
		assertEquals(0, container.size());

		assertNull(optionsGroup1.getValue());
		assertNull(yOptionsGroup1.getSelection());

		// add
		yOptionsGroup1.getCollection().add("Huhu");
		yOptionsGroup1.getCollection().add("Haha");
		assertEquals(2, container.size());

		assertNull(yOptionsGroup1.getSelection());
		assertNull(optionsGroup1.getValue());

		// test set selection
		yOptionsGroup1.setSelection("Huhu");
		assertEquals("Huhu", yOptionsGroup1.getSelection());
		assertEquals("Huhu", optionsGroup1.getValue());

		optionsGroup1.setValue("Haha");
		assertEquals("Haha", yOptionsGroup1.getSelection());
		assertEquals("Haha", optionsGroup1.getValue());

		// test set selection null
		optionsGroup1.setValue(null);
		assertNull(yOptionsGroup1.getSelection());
		assertNull(optionsGroup1.getValue());

		optionsGroup1.setValue("Haha");
		assertEquals("Haha", yOptionsGroup1.getSelection());
		assertEquals("Haha", optionsGroup1.getValue());

		yOptionsGroup1.setSelection(null);
		assertNull(yOptionsGroup1.getSelection());
		assertNull(optionsGroup1.getValue());

		// test remove element that is selected
		// add
		yOptionsGroup1.getCollection().add("Huhu");
		yOptionsGroup1.getCollection().add("Haha");
		assertEquals(2, container.size());

		yOptionsGroup1.setSelection("Huhu");
		assertEquals("Huhu", yOptionsGroup1.getSelection());
		assertEquals("Huhu", optionsGroup1.getValue());

		yOptionsGroup1.getCollection().remove("Huhu");
		assertNull(optionsGroup1.getValue());
		assertNull(yOptionsGroup1.getSelection());

		// test remove element that is selected
		// add
		yOptionsGroup1.getCollection().add("Huhu");
		assertEquals(2, container.size());

		yOptionsGroup1.setSelection("Huhu");
		assertEquals("Huhu", yOptionsGroup1.getSelection());
		assertEquals("Huhu", optionsGroup1.getValue());

		optionsGroup1.setValue(null);
		assertNull(yOptionsGroup1.getSelection());
		assertNull(optionsGroup1.getValue());

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
		YOptionsGroup yOptionsGroup1 = factory.createOptionsGroup();
		yOptionsGroup1.setType(Bar.class);
		yLayout.getElements().add(yOptionsGroup1);
		YTextField yText = factory.createTextField();
		yLayout.getElements().add(yText);

		YBindingSet yBindingSet = yView.getOrCreateBindingSet();
		YDetailValueBindingEndpoint yDetailEndpoint = yOptionsGroup1
				.createSelectionEndpoint().createDetailValueEndpoint();
		yDetailEndpoint.setPropertyPath("myfoo.name");
		yDetailEndpoint.setType(Bar.class);
		yBindingSet.addBinding(yText.createValueEndpoint(), yDetailEndpoint);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		IOptionsGroupEditpart optionsGroup1Editpart = DelegatingEditPartManager
				.getInstance().getEditpart(yOptionsGroup1);
		IWidgetPresentation<Component> optionsGroup1Presentation = optionsGroup1Editpart
				.getPresentation();
		OptionGroup optionsGroup1 = (OptionGroup) optionsGroup1Presentation
				.getWidget();

		ITextFieldEditpart textEditpart = DelegatingEditPartManager
				.getInstance().getEditpart(yText);
		IWidgetPresentation<Component> textPresentation = textEditpart
				.getPresentation();
		TextField text = (TextField) textPresentation.getWidget();

		// start tests
		//
		Container.Indexed container = (Indexed) optionsGroup1
				.getContainerDataSource();
		assertEquals(0, container.size());

		assertNull(optionsGroup1.getValue());
		assertNull(yOptionsGroup1.getSelection());
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

		yOptionsGroup1.getCollection().add(bar1);
		yOptionsGroup1.getCollection().add(bar2);
		assertEquals(2, container.size());

		assertNull(yOptionsGroup1.getSelection());
		assertNull(optionsGroup1.getValue());
		assertNull(yText.getValue());
		assertNull(text.getValue());

		// test set selection
		yOptionsGroup1.setSelection(bar1);
		assertEquals("Foo1", yText.getValue());
		assertEquals("Foo1", text.getValue());

		yOptionsGroup1.setSelection(bar2);
		assertEquals("Foo2", yText.getValue());
		assertEquals("Foo2", text.getValue());

		// test set selection null
		yOptionsGroup1.setSelection(null);
		assertNull(yText.getValue());
		assertNull(text.getValue());

		optionsGroup1.setValue(bar1);
		assertEquals("Foo1", yText.getValue());
		assertEquals("Foo1", text.getValue());

		optionsGroup1.setValue(bar2);
		assertEquals("Foo2", yText.getValue());
		assertEquals("Foo2", text.getValue());

		// test set selection null
		optionsGroup1.setValue(null);
		assertNull(yText.getValue());
		assertNull(text.getValue());

		// test remove element that is selected
		// add
		optionsGroup1.setValue(bar2);
		assertEquals("Foo2", yText.getValue());
		assertEquals("Foo2", text.getValue());

		yOptionsGroup1.getCollection().clear();

		assertNull(yOptionsGroup1.getSelection());
		assertNull(optionsGroup1.getValue());

		// test setValue to textfield
		yOptionsGroup1.getCollection().add(bar1);
		yOptionsGroup1.getCollection().add(bar2);
		assertEquals(2, container.size());

		yOptionsGroup1.setSelection(bar2);
		assertEquals("Foo2", yText.getValue());
		assertEquals("Foo2", text.getValue());

		yText.setValue("Foo2_1");
		assertEquals("Foo2_1", foo2.getName());
		assertEquals("Foo2_1", text.getValue());

		text.setValue("Foo2_2");
		assertEquals("Foo2_2", foo2.getName());
		assertEquals("Foo2_2", yText.getValue());

		yOptionsGroup1.setSelection(bar1);
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
		YOptionsGroup yOptionsGroup1 = factory.createOptionsGroup();
		yOptionsGroup1.setType(EmfBar.class);
		yOptionsGroup1.setEmfNsURI(ModelPackage.eINSTANCE.getNsURI());
		yLayout.getElements().add(yOptionsGroup1);
		YTextField yText = factory.createTextField();
		yLayout.getElements().add(yText);

		YBindingSet yBindingSet = yView.getOrCreateBindingSet();
		YDetailValueBindingEndpoint yDetailEndpoint = yOptionsGroup1
				.createSelectionEndpoint().createDetailValueEndpoint();
		yDetailEndpoint.setType(EmfBar.class);
		yDetailEndpoint.getFeatures().add(
				ModelPackage.eINSTANCE.getEmfBar_Myfoo());
		yDetailEndpoint.getFeatures().add(
				ModelPackage.eINSTANCE.getEmfFoo_Name());
		yBindingSet.addBinding(yText.createValueEndpoint(), yDetailEndpoint);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		IOptionsGroupEditpart optionsGroup1Editpart = DelegatingEditPartManager
				.getInstance().getEditpart(yOptionsGroup1);
		IWidgetPresentation<Component> optionsGroup1Presentation = optionsGroup1Editpart
				.getPresentation();
		OptionGroup optionsGroup1 = (OptionGroup) optionsGroup1Presentation
				.getWidget();

		ITextFieldEditpart textEditpart = DelegatingEditPartManager
				.getInstance().getEditpart(yText);
		IWidgetPresentation<Component> textPresentation = textEditpart
				.getPresentation();
		TextField text = (TextField) textPresentation.getWidget();

		// start tests
		//
		Container.Indexed container = (Indexed) optionsGroup1
				.getContainerDataSource();
		assertEquals(0, container.size());

		assertNull(optionsGroup1.getValue());
		assertNull(yOptionsGroup1.getSelection());
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

		yOptionsGroup1.getCollection().add(bar1);
		yOptionsGroup1.getCollection().add(bar2);
		assertEquals(2, container.size());

		assertNull(yOptionsGroup1.getSelection());
		assertNull(optionsGroup1.getValue());
		assertNull(yText.getValue());
		assertNull(text.getValue());

		// test set selection
		yOptionsGroup1.setSelection(bar1);
		assertEquals("Foo1", yText.getValue());
		assertEquals("Foo1", text.getValue());

		yOptionsGroup1.setSelection(bar2);
		assertEquals("Foo2", yText.getValue());
		assertEquals("Foo2", text.getValue());

		// test set selection null
		yOptionsGroup1.setSelection(null);
		assertNull(yText.getValue());
		assertNull(text.getValue());

		optionsGroup1.setValue(bar1);
		assertEquals("Foo1", yText.getValue());
		assertEquals("Foo1", text.getValue());

		optionsGroup1.setValue(bar2);
		assertEquals("Foo2", yText.getValue());
		assertEquals("Foo2", text.getValue());

		// test set selection null
		optionsGroup1.setValue(null);
		assertNull(yText.getValue());
		assertNull(text.getValue());

		// test remove element that is selected
		// add
		optionsGroup1.setValue(bar2);
		assertEquals("Foo2", yText.getValue());
		assertEquals("Foo2", text.getValue());

		yOptionsGroup1.getCollection().clear();

		assertNull(yOptionsGroup1.getSelection());
		assertNull(optionsGroup1.getValue());

		// test setValue to textfield
		yOptionsGroup1.getCollection().add(bar1);
		yOptionsGroup1.getCollection().add(bar2);
		assertEquals(2, container.size());

		yOptionsGroup1.setSelection(bar2);
		assertEquals("Foo2", yText.getValue());
		assertEquals("Foo2", text.getValue());

		yText.setValue("Foo2_1");
		assertEquals("Foo2_1", foo2.getName());
		assertEquals("Foo2_1", text.getValue());

		text.setValue("Foo2_2");
		assertEquals("Foo2_2", foo2.getName());
		assertEquals("Foo2_2", yText.getValue());

		yOptionsGroup1.setSelection(bar1);
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
		YOptionsGroup yOptionsGroup1 = factory.createOptionsGroup();
		yOptionsGroup1.setSelectionType(YSelectionType.MULTI);
		yOptionsGroup1.setType(String.class);
		yLayout.getElements().add(yOptionsGroup1);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		IOptionsGroupEditpart optionsGroup1Editpart = DelegatingEditPartManager
				.getInstance().getEditpart(yOptionsGroup1);
		IWidgetPresentation<Component> optionsGroup1Presentation = optionsGroup1Editpart
				.getPresentation();
		OptionGroup optionsGroup1 = (OptionGroup) optionsGroup1Presentation
				.getWidget();

		// start tests
		//
		Container.Indexed container = (Indexed) optionsGroup1
				.getContainerDataSource();
		assertEquals(0, container.size());

		assertTrue(asList(optionsGroup1.getValue()).isEmpty());
		assertTrue(yOptionsGroup1.getMultiSelection().isEmpty());

		// add
		yOptionsGroup1.getCollection().add("Huhu");
		yOptionsGroup1.getCollection().add("Haha");
		assertEquals(2, container.size());

		assertTrue(yOptionsGroup1.getMultiSelection().isEmpty());
		assertTrue(asList(optionsGroup1.getValue()).isEmpty());

		// test set selection by model
		yOptionsGroup1.getMultiSelection().add("Huhu");
		assertEquals("Huhu", yOptionsGroup1.getMultiSelection().get(0));
		assertEquals("Huhu", asList(optionsGroup1.getValue()).get(0));
		assertEquals(1, yOptionsGroup1.getMultiSelection().size());
		assertEquals(1, asList(optionsGroup1.getValue()).size());

		yOptionsGroup1.getMultiSelection().add("Haha");
		assertEquals("Huhu", yOptionsGroup1.getMultiSelection().get(0));
		assertEquals("Haha", yOptionsGroup1.getMultiSelection().get(1));
		assertTrue(asList(optionsGroup1.getValue()).contains("Huhu"));
		assertTrue(asList(optionsGroup1.getValue()).contains("Haha"));
		assertEquals(2, yOptionsGroup1.getMultiSelection().size());
		assertEquals(2, asList(optionsGroup1.getValue()).size());

		// remove selection
		yOptionsGroup1.getMultiSelection().remove("Huhu");
		assertEquals("Haha", yOptionsGroup1.getMultiSelection().get(0));
		assertEquals("Haha", asList(optionsGroup1.getValue()).get(0));
		assertEquals(1, yOptionsGroup1.getMultiSelection().size());
		assertEquals(1, asList(optionsGroup1.getValue()).size());

		yOptionsGroup1.getMultiSelection().clear();
		assertEquals(0, yOptionsGroup1.getMultiSelection().size());
		assertEquals(0, asList(optionsGroup1.getValue()).size());

		// test set selection by widget
		List<String> selection = new ArrayList<String>();
		selection.add("Huhu");
		optionsGroup1.setValue(selection);
		assertEquals("Huhu", yOptionsGroup1.getMultiSelection().get(0));
		assertEquals("Huhu", asList(optionsGroup1.getValue()).get(0));
		assertEquals(1, yOptionsGroup1.getMultiSelection().size());
		assertEquals(1, asList(optionsGroup1.getValue()).size());

		selection = new ArrayList<String>();
		selection.add("Huhu");
		selection.add("Haha");
		optionsGroup1.setValue(selection);
		assertEquals("Huhu", yOptionsGroup1.getMultiSelection().get(0));
		assertEquals("Haha", yOptionsGroup1.getMultiSelection().get(1));
		// -> Sort order not defined by vaadin -> Set
		assertTrue(asList(optionsGroup1.getValue()).contains("Huhu"));
		assertTrue(asList(optionsGroup1.getValue()).contains("Haha"));
		assertEquals(2, yOptionsGroup1.getMultiSelection().size());
		assertEquals(2, asList(optionsGroup1.getValue()).size());

		selection = new ArrayList<String>();
		selection.add("Haha");
		optionsGroup1.setValue(selection);
		assertEquals("Haha", yOptionsGroup1.getMultiSelection().get(0));
		assertEquals("Haha", asList(optionsGroup1.getValue()).get(0));
		assertEquals(1, yOptionsGroup1.getMultiSelection().size());
		assertEquals(1, asList(optionsGroup1.getValue()).size());

		selection = new ArrayList<String>();
		optionsGroup1.setValue(selection);
		assertEquals(0, yOptionsGroup1.getMultiSelection().size());
		assertEquals(0, asList(optionsGroup1.getValue()).size());

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
		YOptionsGroup yOptionsGroup1 = factory.createOptionsGroup();
		yOptionsGroup1.setType(String.class);
		yLayout.getElements().add(yOptionsGroup1);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		IOptionsGroupEditpart optionsGroup1Editpart = DelegatingEditPartManager
				.getInstance().getEditpart(yOptionsGroup1);
		IWidgetPresentation<Component> optionsGroup1Presentation = optionsGroup1Editpart
				.getPresentation();
		OptionGroup optionsGroup1 = (OptionGroup) optionsGroup1Presentation
				.getWidget();

		// start tests
		//
		Container.Indexed container = (Indexed) optionsGroup1
				.getContainerDataSource();
		assertEquals(0, container.size());

		assertNull(optionsGroup1.getValue());
		assertNull(yOptionsGroup1.getSelection());

		// add
		assertNull(yOptionsGroup1.getSelection());
		assertNull(optionsGroup1.getValue());

		// test set selection
		yOptionsGroup1.setSelection("Huhu");
		assertNull(yOptionsGroup1.getSelection());
		assertNull(optionsGroup1.getValue());
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
		YOptionsGroup yOptionsGroup1 = factory.createOptionsGroup();
		yOptionsGroup1.setSelectionType(YSelectionType.MULTI);
		yOptionsGroup1.setType(List.class);
		yLayout.getElements().add(yOptionsGroup1);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		IOptionsGroupEditpart optionsGroup1Editpart = DelegatingEditPartManager
				.getInstance().getEditpart(yOptionsGroup1);
		IWidgetPresentation<Component> optionsGroup1Presentation = optionsGroup1Editpart
				.getPresentation();
		OptionGroup optionsGroup1 = (OptionGroup) optionsGroup1Presentation
				.getWidget();

		// start tests
		//
		Container.Indexed container = (Indexed) optionsGroup1
				.getContainerDataSource();
		assertEquals(0, container.size());

		assertTrue(asList(optionsGroup1.getValue()).isEmpty());
		assertTrue(yOptionsGroup1.getMultiSelection().isEmpty());

		// test set selection by model
		yOptionsGroup1.getMultiSelection().add("Huhu");
		assertEquals("Huhu", yOptionsGroup1.getMultiSelection().get(0));
		assertEquals("Huhu", asList(optionsGroup1.getValue()).get(0));
		assertEquals(1, yOptionsGroup1.getMultiSelection().size());
		assertEquals(1, asList(optionsGroup1.getValue()).size());

		yOptionsGroup1.getMultiSelection().add("Haha");
		assertEquals("Huhu", yOptionsGroup1.getMultiSelection().get(0));
		assertEquals("Haha", yOptionsGroup1.getMultiSelection().get(1));
		// -> Sort order not defined by vaadin -> Set
		assertTrue(asList(optionsGroup1.getValue()).contains("Huhu"));
		assertTrue(asList(optionsGroup1.getValue()).contains("Haha"));
		assertEquals(2, yOptionsGroup1.getMultiSelection().size());
		assertEquals(2, asList(optionsGroup1.getValue()).size());

		// remove selection
		yOptionsGroup1.getMultiSelection().remove("Huhu");
		assertEquals("Haha", yOptionsGroup1.getMultiSelection().get(0));
		assertEquals("Haha", asList(optionsGroup1.getValue()).get(0));
		assertEquals(1, yOptionsGroup1.getMultiSelection().size());
		assertEquals(1, asList(optionsGroup1.getValue()).size());

		yOptionsGroup1.getMultiSelection().clear();
		assertEquals(0, yOptionsGroup1.getMultiSelection().size());
		assertEquals(0, asList(optionsGroup1.getValue()).size());

		// test set selection by widget
		List<String> selection = new ArrayList<String>();
		selection.add("Huhu");
		optionsGroup1.setValue(selection);
		assertEquals("Huhu", yOptionsGroup1.getMultiSelection().get(0));
		assertEquals("Huhu", asList(optionsGroup1.getValue()).get(0));
		assertEquals(1, yOptionsGroup1.getMultiSelection().size());
		assertEquals(1, asList(optionsGroup1.getValue()).size());

		selection = new ArrayList<String>();
		selection.add("Huhu");
		selection.add("Haha");
		optionsGroup1.setValue(selection);
		assertEquals("Huhu", yOptionsGroup1.getMultiSelection().get(0));
		assertEquals("Haha", yOptionsGroup1.getMultiSelection().get(1));
		// -> Sort order not defined by vaadin -> Set
		assertTrue(asList(optionsGroup1.getValue()).contains("Huhu"));
		assertTrue(asList(optionsGroup1.getValue()).contains("Haha"));
		assertEquals(2, yOptionsGroup1.getMultiSelection().size());
		assertEquals(2, asList(optionsGroup1.getValue()).size());

		selection = new ArrayList<String>();
		selection.add("Haha");
		optionsGroup1.setValue(selection);
		assertEquals("Haha", yOptionsGroup1.getMultiSelection().get(0));
		assertEquals("Haha", asList(optionsGroup1.getValue()).get(0));
		assertEquals(1, yOptionsGroup1.getMultiSelection().size());
		assertEquals(1, asList(optionsGroup1.getValue()).size());

		selection = new ArrayList<String>();
		optionsGroup1.setValue(selection);
		assertEquals(0, yOptionsGroup1.getMultiSelection().size());
		assertEquals(0, asList(optionsGroup1.getValue()).size());
	}

	@Test
	public void test_SelectionBinding_Single_WithAttributePath()
			throws ContextException {
		// END SUPRESS CATCH EXCEPTION
		// build the view model
		// ...> yView
		// ......> yText
		YView yView = factory.createView();
		YGridLayout yLayout = factory.createGridLayout();
		yView.setContent(yLayout);
		YOptionsGroup yOptionsGroup1 = factory.createOptionsGroup();
		yOptionsGroup1.setType(Bar.class);
		yLayout.getElements().add(yOptionsGroup1);
		YTextField yText = factory.createTextField();
		yLayout.getElements().add(yText);

		YBindingSet yBindingSet = yView.getOrCreateBindingSet();
		YEmbeddableSelectionEndpoint selectionBindingEndpoint = yOptionsGroup1
				.createSelectionEndpoint();
		selectionBindingEndpoint.setAttributePath("myfoo.name");
		yBindingSet.addBinding(yText.createValueEndpoint(),
				selectionBindingEndpoint);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		IOptionsGroupEditpart optionsGroup1Editpart = DelegatingEditPartManager
				.getInstance().getEditpart(yOptionsGroup1);
		IWidgetPresentation<Component> optionsGroup1Presentation = optionsGroup1Editpart
				.getPresentation();
		OptionGroup optionsGroup1 = (OptionGroup) optionsGroup1Presentation
				.getWidget();

		ITextFieldEditpart textEditpart = DelegatingEditPartManager
				.getInstance().getEditpart(yText);
		IWidgetPresentation<Component> textPresentation = textEditpart
				.getPresentation();
		TextField text = (TextField) textPresentation.getWidget();

		// start tests
		//
		Container.Indexed container = (Indexed) optionsGroup1
				.getContainerDataSource();
		assertEquals(0, container.size());

		assertNull(optionsGroup1.getValue());
		assertNull(yOptionsGroup1.getSelection());
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

		yOptionsGroup1.getCollection().add(bar1);
		yOptionsGroup1.getCollection().add(bar2);
		assertEquals(2, container.size());

		assertNull(yOptionsGroup1.getSelection());
		assertNull(optionsGroup1.getValue());
		assertNull(yText.getValue());
		assertNull(text.getValue());

		// test set selection
		yOptionsGroup1.setSelection(bar1);
		assertEquals("Foo1", yText.getValue());
		assertEquals("Foo1", text.getValue());

		yOptionsGroup1.setSelection(bar2);
		assertEquals("Foo2", yText.getValue());
		assertEquals("Foo2", text.getValue());

		// test set selection null
		yOptionsGroup1.setSelection(null);
		assertNull(yText.getValue());
		assertNull(text.getValue());

		optionsGroup1.setValue(bar1);
		assertEquals("Foo1", yText.getValue());
		assertEquals("Foo1", text.getValue());

		optionsGroup1.setValue(bar2);
		assertEquals("Foo2", yText.getValue());
		assertEquals("Foo2", text.getValue());

		// test set selection null
		optionsGroup1.setValue(null);
		assertNull(yText.getValue());
		assertNull(text.getValue());

		// test remove element that is selected
		// add
		optionsGroup1.setValue(bar2);
		assertEquals("Foo2", yText.getValue());
		assertEquals("Foo2", text.getValue());

		yOptionsGroup1.getCollection().clear();

		assertNull(yOptionsGroup1.getSelection());
		assertNull(optionsGroup1.getValue());

		// test setValue to textfield
		yOptionsGroup1.getCollection().add(bar1);
		yOptionsGroup1.getCollection().add(bar2);
		assertEquals(2, container.size());

		yOptionsGroup1.setSelection(bar2);
		assertEquals("Foo2", yText.getValue());
		assertEquals("Foo2", text.getValue());

		yText.setValue("Foo2_1");
		assertEquals("Foo2_1", foo2.getName());
		assertEquals("Foo2_1", text.getValue());

		text.setValue("Foo2_2");
		assertEquals("Foo2_2", foo2.getName());
		assertEquals("Foo2_2", yText.getValue());

		yOptionsGroup1.setSelection(bar1);
		assertEquals("Foo1", yText.getValue());
		assertEquals("Foo1", text.getValue());

		yText.setValue("Foo1_1");
		assertEquals("Foo1_1", foo1.getName());
		assertEquals("Foo1_1", text.getValue());

		text.setValue("Foo1_2");
		assertEquals("Foo1_2", foo1.getName());
		assertEquals("Foo1_2", yText.getValue());

	}

	/**
	 * Test the internal structure based on CSS.
	 * 
	 * @throws Exception
	 */
	@Test
	// BEGIN SUPRESS CATCH EXCEPTION
	public void test_SelectionBinding_Multi_OptionGroupToOptionGroup()
			throws Exception {
		// END SUPRESS CATCH EXCEPTION
		// build the view model
		// ...> yView
		// ......> yText
		YView yView = factory.createView();
		YGridLayout yLayout = factory.createGridLayout();
		yView.setContent(yLayout);
		YOptionsGroup yOptionsGroup1 = factory.createOptionsGroup();
		yOptionsGroup1.setSelectionType(YSelectionType.MULTI);
		yOptionsGroup1.setType(String.class);
		yLayout.getElements().add(yOptionsGroup1);
		YOptionsGroup yOptionsGroup2 = factory.createOptionsGroup();
		yOptionsGroup2.setSelectionType(YSelectionType.MULTI);
		yOptionsGroup2.setType(String.class);
		yLayout.getElements().add(yOptionsGroup2);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		IOptionsGroupEditpart optionsGroup1Editpart = DelegatingEditPartManager
				.getInstance().getEditpart(yOptionsGroup1);
		IOptionsGroupEditpart optionsGroup2Editpart = DelegatingEditPartManager
				.getInstance().getEditpart(yOptionsGroup2);
		IWidgetPresentation<Component> optionsGroup1Presentation = optionsGroup1Editpart
				.getPresentation();
		IWidgetPresentation<Component> optionsGroup2Presentation = optionsGroup2Editpart
				.getPresentation();
		OptionGroup optionsGroup1 = (OptionGroup) optionsGroup1Presentation
				.getWidget();
		OptionGroup optionsGroup2 = (OptionGroup) optionsGroup2Presentation
				.getWidget();

		optionsGroup1.getContainerDataSource();
		optionsGroup2.getContainerDataSource();

		YBindingSet yBindingSet = yView.getOrCreateBindingSet();

		// start tests
		//

		YEmbeddableCollectionEndpoint endp1 = yOptionsGroup1
				.createCollectionEndpoint();
		YEmbeddableCollectionEndpoint endp2 = yOptionsGroup2
				.createCollectionEndpoint();
		yBindingSet.addBinding(endp1, endp2);

		YEmbeddableMultiSelectionEndpoint endpSel1 = yOptionsGroup1
				.createMultiSelectionEndpoint();
		YEmbeddableMultiSelectionEndpoint endpSel2 = yOptionsGroup2
				.createMultiSelectionEndpoint();
		yBindingSet.addBinding(endpSel1, endpSel2);

		Container.Indexed container1 = (Indexed) optionsGroup1
				.getContainerDataSource();
		Container.Indexed container2 = (Indexed) optionsGroup2
				.getContainerDataSource();
		assertEquals(0, container1.size());
		assertEquals(0, container2.size());

		assertTrue(asList(optionsGroup1.getValue()).isEmpty());
		assertTrue(yOptionsGroup1.getMultiSelection().isEmpty());
		assertTrue(asList(optionsGroup2.getValue()).isEmpty());
		assertTrue(yOptionsGroup2.getMultiSelection().isEmpty());

		// add
		yOptionsGroup1.getCollection().add("Huhu");
		yOptionsGroup2.getCollection().add("Haha");
		assertEquals(2, container1.size());
		assertEquals(2, container2.size());

		assertTrue(asList(optionsGroup1.getValue()).isEmpty());
		assertTrue(yOptionsGroup1.getMultiSelection().isEmpty());
		assertTrue(asList(optionsGroup2.getValue()).isEmpty());
		assertTrue(yOptionsGroup2.getMultiSelection().isEmpty());

		// test set selection
		yOptionsGroup1.getMultiSelection().add("Huhu");
		assertEquals("Huhu", yOptionsGroup1.getMultiSelection().get(0));
		assertEquals("Huhu", asList(optionsGroup1.getValue()).get(0));
		assertEquals(1, yOptionsGroup1.getMultiSelection().size());
		assertEquals(1, asList(optionsGroup1.getValue()).size());
		assertEquals("Huhu", yOptionsGroup2.getMultiSelection().get(0));
		assertEquals("Huhu", asList(optionsGroup2.getValue()).get(0));
		assertEquals(1, yOptionsGroup2.getMultiSelection().size());
		assertEquals(1, asList(optionsGroup2.getValue()).size());

		yOptionsGroup2.getMultiSelection().add("Haha");
		assertEquals("Huhu", yOptionsGroup1.getMultiSelection().get(0));
		assertEquals("Haha", yOptionsGroup1.getMultiSelection().get(1));
		// -> Sort order not defined by vaadin -> Set
		assertTrue(asList(optionsGroup1.getValue()).contains("Huhu"));
		assertTrue(asList(optionsGroup1.getValue()).contains("Haha"));
		assertEquals(2, yOptionsGroup1.getMultiSelection().size());
		assertEquals(2, asList(optionsGroup1.getValue()).size());
		assertEquals("Huhu", yOptionsGroup2.getMultiSelection().get(0));
		assertEquals("Haha", yOptionsGroup2.getMultiSelection().get(1));
		// -> Sort order not defined by vaadin -> Set
		assertTrue(asList(optionsGroup1.getValue()).contains("Huhu"));
		assertTrue(asList(optionsGroup1.getValue()).contains("Haha"));
		assertEquals(2, yOptionsGroup2.getMultiSelection().size());
		assertEquals(2, asList(optionsGroup2.getValue()).size());

		yOptionsGroup1.getMultiSelection().remove("Huhu");
		assertEquals("Haha", yOptionsGroup1.getMultiSelection().get(0));
		assertEquals("Haha", asList(optionsGroup1.getValue()).get(0));
		assertEquals(1, yOptionsGroup1.getMultiSelection().size());
		assertEquals(1, asList(optionsGroup1.getValue()).size());
		assertEquals("Haha", yOptionsGroup2.getMultiSelection().get(0));
		assertEquals("Haha", asList(optionsGroup2.getValue()).get(0));
		assertEquals(1, yOptionsGroup2.getMultiSelection().size());
		assertEquals(1, asList(optionsGroup2.getValue()).size());

		// clear
		yOptionsGroup2.getMultiSelection().clear();
		assertTrue(asList(optionsGroup1.getValue()).isEmpty());
		assertTrue(yOptionsGroup1.getMultiSelection().isEmpty());
		assertTrue(asList(optionsGroup2.getValue()).isEmpty());
		assertTrue(yOptionsGroup2.getMultiSelection().isEmpty());

		// test set selection null
		List<String> selection = new ArrayList<String>();
		selection.add("Huhu");
		optionsGroup1.setValue(selection);
		assertEquals("Huhu", yOptionsGroup1.getMultiSelection().get(0));
		assertEquals("Huhu", asList(optionsGroup1.getValue()).get(0));
		assertEquals(1, yOptionsGroup1.getMultiSelection().size());
		assertEquals(1, asList(optionsGroup1.getValue()).size());
		assertEquals("Huhu", yOptionsGroup2.getMultiSelection().get(0));
		assertEquals("Huhu", asList(optionsGroup2.getValue()).get(0));
		assertEquals(1, yOptionsGroup2.getMultiSelection().size());
		assertEquals(1, asList(optionsGroup2.getValue()).size());

		selection.add("Haha");
		optionsGroup2.setValue(selection);
		assertEquals("Huhu", yOptionsGroup1.getMultiSelection().get(0));
		assertEquals("Haha", yOptionsGroup1.getMultiSelection().get(1));
		// -> Sort order not defined by vaadin -> Set
		assertTrue(asList(optionsGroup1.getValue()).contains("Huhu"));
		assertTrue(asList(optionsGroup1.getValue()).contains("Haha"));
		assertEquals(2, yOptionsGroup1.getMultiSelection().size());
		assertEquals(2, asList(optionsGroup1.getValue()).size());
		assertEquals("Huhu", yOptionsGroup2.getMultiSelection().get(0));
		assertEquals("Haha", yOptionsGroup2.getMultiSelection().get(1));
		// -> Sort order not defined by vaadin -> Set
		assertTrue(asList(optionsGroup1.getValue()).contains("Huhu"));
		assertTrue(asList(optionsGroup1.getValue()).contains("Haha"));
		assertEquals(2, yOptionsGroup2.getMultiSelection().size());
		assertEquals(2, asList(optionsGroup2.getValue()).size());

		optionsGroup2.setValue(new ArrayList<String>());
		assertTrue(asList(optionsGroup2.getValue()).isEmpty());
		assertTrue(yOptionsGroup2.getMultiSelection().isEmpty());
		assertTrue(asList(optionsGroup1.getValue()).isEmpty());
		assertTrue(yOptionsGroup1.getMultiSelection().isEmpty());
	}

	/**
	 * Test the internal structure based on CSS.
	 * 
	 * @throws Exception
	 */
	@Test
	// BEGIN SUPRESS CATCH EXCEPTION
	public void test_SelectionBinding_Single_OptionGroupToOptionGroup()
			throws Exception {
		// END SUPRESS CATCH EXCEPTION
		// build the view model
		// ...> yView
		// ......> yText
		YView yView = factory.createView();
		YGridLayout yLayout = factory.createGridLayout();
		yView.setContent(yLayout);
		YOptionsGroup yOptionsGroup1 = factory.createOptionsGroup();
		yOptionsGroup1.setType(String.class);
		yLayout.getElements().add(yOptionsGroup1);
		YOptionsGroup yOptionsGroup2 = factory.createOptionsGroup();
		yOptionsGroup2.setType(String.class);
		yLayout.getElements().add(yOptionsGroup2);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		IOptionsGroupEditpart optionsGroup1Editpart = DelegatingEditPartManager
				.getInstance().getEditpart(yOptionsGroup1);
		IOptionsGroupEditpart optionsGroup2Editpart = DelegatingEditPartManager
				.getInstance().getEditpart(yOptionsGroup2);
		IWidgetPresentation<Component> optionsGroup1Presentation = optionsGroup1Editpart
				.getPresentation();
		IWidgetPresentation<Component> optionsGroup2Presentation = optionsGroup2Editpart
				.getPresentation();
		OptionGroup optionsGroup1 = (OptionGroup) optionsGroup1Presentation
				.getWidget();
		OptionGroup optionsGroup2 = (OptionGroup) optionsGroup2Presentation
				.getWidget();

		optionsGroup1.getContainerDataSource();
		optionsGroup2.getContainerDataSource();

		YBindingSet yBindingSet = yView.getOrCreateBindingSet();

		// start tests
		//

		YEmbeddableCollectionEndpoint endp1 = yOptionsGroup1
				.createCollectionEndpoint();
		YEmbeddableCollectionEndpoint endp2 = yOptionsGroup2
				.createCollectionEndpoint();
		yBindingSet.addBinding(endp1, endp2);

		YEmbeddableSelectionEndpoint endpSel1 = yOptionsGroup1
				.createSelectionEndpoint();
		YEmbeddableSelectionEndpoint endpSel2 = yOptionsGroup2
				.createSelectionEndpoint();
		yBindingSet.addBinding(endpSel1, endpSel2);

		Container.Indexed container1 = (Indexed) optionsGroup1
				.getContainerDataSource();
		Container.Indexed container2 = (Indexed) optionsGroup2
				.getContainerDataSource();
		assertEquals(0, container1.size());
		assertEquals(0, container2.size());

		assertNull(optionsGroup1.getValue());
		assertNull(yOptionsGroup1.getSelection());

		// add
		yOptionsGroup1.getCollection().add("Huhu");
		yOptionsGroup2.getCollection().add("Haha");
		assertEquals(2, container1.size());
		assertEquals(2, container2.size());

		assertNull(yOptionsGroup1.getSelection());
		assertNull(optionsGroup1.getValue());
		assertNull(yOptionsGroup2.getSelection());
		assertNull(optionsGroup2.getValue());

		// test set selection
		yOptionsGroup1.setSelection("Huhu");
		assertEquals("Huhu", yOptionsGroup1.getSelection());
		assertEquals("Huhu", optionsGroup1.getValue());
		assertEquals("Huhu", yOptionsGroup2.getSelection());
		assertEquals("Huhu", optionsGroup2.getValue());

		optionsGroup1.setValue("Haha");
		assertEquals("Haha", yOptionsGroup1.getSelection());
		assertEquals("Haha", optionsGroup1.getValue());
		assertEquals("Haha", yOptionsGroup2.getSelection());
		assertEquals("Haha", optionsGroup2.getValue());

		// test set selection null
		optionsGroup1.setValue(null);
		assertNull(yOptionsGroup1.getSelection());
		assertNull(optionsGroup1.getValue());
		assertNull(yOptionsGroup2.getSelection());
		assertNull(optionsGroup2.getValue());

		optionsGroup1.setValue("Haha");
		assertEquals("Haha", yOptionsGroup1.getSelection());
		assertEquals("Haha", optionsGroup1.getValue());
		assertEquals("Haha", yOptionsGroup2.getSelection());
		assertEquals("Haha", optionsGroup2.getValue());

		optionsGroup2.setValue(null);
		assertNull(yOptionsGroup1.getSelection());
		assertNull(optionsGroup1.getValue());
		assertNull(yOptionsGroup2.getSelection());
		assertNull(optionsGroup2.getValue());

		optionsGroup2.setValue("Haha");
		assertEquals("Haha", yOptionsGroup1.getSelection());
		assertEquals("Haha", optionsGroup1.getValue());
		assertEquals("Haha", yOptionsGroup2.getSelection());
		assertEquals("Haha", optionsGroup2.getValue());

		yOptionsGroup1.setSelection(null);
		assertNull(yOptionsGroup1.getSelection());
		assertNull(optionsGroup1.getValue());
		assertNull(yOptionsGroup2.getSelection());
		assertNull(optionsGroup2.getValue());

		optionsGroup2.setValue("Haha");
		assertEquals("Haha", yOptionsGroup1.getSelection());
		assertEquals("Haha", optionsGroup1.getValue());
		assertEquals("Haha", yOptionsGroup2.getSelection());
		assertEquals("Haha", optionsGroup2.getValue());

		yOptionsGroup2.setSelection(null);
		assertNull(yOptionsGroup1.getSelection());
		assertNull(optionsGroup1.getValue());
		assertNull(yOptionsGroup2.getSelection());
		assertNull(optionsGroup2.getValue());

		// test remove element that is selected
		// add
		assertEquals(2, container1.size());
		assertEquals(2, container2.size());

		yOptionsGroup1.setSelection("Huhu");
		assertEquals("Huhu", yOptionsGroup1.getSelection());
		assertEquals("Huhu", yOptionsGroup2.getSelection());
		assertEquals("Huhu", optionsGroup1.getValue());
		assertEquals("Huhu", optionsGroup2.getValue());

		yOptionsGroup1.getCollection().remove("Huhu");
		assertNull(optionsGroup1.getValue());
		assertNull(optionsGroup2.getValue());
		assertNull(yOptionsGroup1.getSelection());
		assertNull(yOptionsGroup2.getSelection());

		// test remove element that is selected
		// add
		yOptionsGroup2.getCollection().add("Huhu");
		assertEquals(2, container1.size());
		assertEquals(2, container2.size());

		yOptionsGroup1.setSelection("Huhu");
		assertEquals("Huhu", yOptionsGroup1.getSelection());
		assertEquals("Huhu", optionsGroup1.getValue());
		assertEquals("Huhu", yOptionsGroup2.getSelection());
		assertEquals("Huhu", optionsGroup2.getValue());

		optionsGroup2.setValue(null);
		assertNull(yOptionsGroup1.getSelection());
		assertNull(optionsGroup1.getValue());
		assertNull(yOptionsGroup2.getSelection());
		assertNull(optionsGroup2.getValue());
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
		YOptionsGroup yOptionsGroup = factory.createOptionsGroup();
		yOptionsGroup.setType(String.class);
		yGridlayout.getElements().add(yOptionsGroup);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		IOptionsGroupEditpart optionsGroupEditpart = DelegatingEditPartManager
				.getInstance().getEditpart(yOptionsGroup);
		IWidgetPresentation<Component> presentation = optionsGroupEditpart
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
		YOptionsGroup yOptionsGroup = factory.createOptionsGroup();
		yOptionsGroup.setType(String.class);
		yGridlayout.getElements().add(yOptionsGroup);

		// set the i18n key
		yOptionsGroup.setLabelI18nKey(I18nServiceForTests.KEY__AGE);

		// prepare the I18nService and pass it to the renderer
		Map<String, Object> parameter = new HashMap<String, Object>();
		Map<String, Object> services = new HashMap<String, Object>();
		parameter.put(IViewContext.PARAM_SERVICES, services);
		services.put(II18nService.ID, new I18nServiceForTests());

		VaadinRenderer renderer = new VaadinRenderer();
		IViewContext context = renderer.render(rootLayout, yView, parameter);
		IOptionsGroupEditpart editpart = DelegatingEditPartManager
				.getInstance().getEditpart(yOptionsGroup);
		OptionsGroupPresentation presentation = editpart.getPresentation();

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
		YOptionsGroup yOptionsGroup = factory.createOptionsGroup();
		yOptionsGroup.setType(ValueBean.class);
		yLayout.getElements().add(yOptionsGroup);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		IOptionsGroupEditpart editpart = DelegatingEditPartManager
				.getInstance().getEditpart(yOptionsGroup);
		IWidgetPresentation<Component> presentation = editpart
				.getPresentation();
		OptionGroup grp = (OptionGroup) presentation.getWidget();

		ValueBean bean = new ValueBean(false);
		YBeanValueBindingEndpoint yBeanBinding = factory
				.createBeanBindingEndpoint();
		yBeanBinding.setBean(bean);
		yBeanBinding.setPropertyPath("boolValue");
		YBindingSet yBindingSet = yView.getOrCreateBindingSet();
		yBindingSet.addBinding(yOptionsGroup.createEditableEndpoint(),
				yBeanBinding);

		// test binding
		assertFalse(yOptionsGroup.isEditable());
		assertFalse(!grp.isReadOnly());
		assertFalse(bean.isBoolValue());

		bean.setBoolValue(true);
		assertTrue(yOptionsGroup.isEditable());
		assertTrue(!grp.isReadOnly());
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
		YOptionsGroup yOptionsGroup = factory.createOptionsGroup();
		yOptionsGroup.setType(ValueBean.class);
		yLayout.getElements().add(yOptionsGroup);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		IOptionsGroupEditpart editpart = DelegatingEditPartManager
				.getInstance().getEditpart(yOptionsGroup);
		IWidgetPresentation<Component> presentation = editpart
				.getPresentation();
		presentation.getWidget();
		OptionGroup grp = (OptionGroup) presentation.getWidget();

		ValueBean bean = new ValueBean(false);
		YBeanValueBindingEndpoint yBeanBinding = factory
				.createBeanBindingEndpoint();
		yBeanBinding.setBean(bean);
		yBeanBinding.setPropertyPath("boolValue");
		YBindingSet yBindingSet = yView.getOrCreateBindingSet();
		yBindingSet.addBinding(yOptionsGroup.createVisibleEndpoint(),
				yBeanBinding);

		// test binding
		assertFalse(yOptionsGroup.isVisible());
		assertFalse(grp.isVisible());
		assertFalse(bean.isBoolValue());

		bean.setBoolValue(true);
		assertTrue(yOptionsGroup.isVisible());
		assertTrue(grp.isVisible());
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
		YOptionsGroup yOptionsGroup = factory.createOptionsGroup();
		yOptionsGroup.setType(ValueBean.class);
		yLayout.getElements().add(yOptionsGroup);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		IOptionsGroupEditpart editpart = DelegatingEditPartManager
				.getInstance().getEditpart(yOptionsGroup);
		IWidgetPresentation<Component> presentation = editpart
				.getPresentation();
		presentation.getWidget();
		OptionGroup grp = (OptionGroup) presentation.getWidget();

		ValueBean bean = new ValueBean(false);
		YBeanValueBindingEndpoint yBeanBinding = factory
				.createBeanBindingEndpoint();
		yBeanBinding.setBean(bean);
		yBeanBinding.setPropertyPath("boolValue");
		YBindingSet yBindingSet = yView.getOrCreateBindingSet();
		yBindingSet.addBinding(yOptionsGroup.createEnabledEndpoint(),
				yBeanBinding);

		// test binding
		assertFalse(yOptionsGroup.isEnabled());
		assertFalse(grp.isEnabled());
		assertFalse(bean.isBoolValue());

		bean.setBoolValue(true);
		assertTrue(yOptionsGroup.isEnabled());
		assertTrue(grp.isEnabled());
		assertTrue(bean.isBoolValue());
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
