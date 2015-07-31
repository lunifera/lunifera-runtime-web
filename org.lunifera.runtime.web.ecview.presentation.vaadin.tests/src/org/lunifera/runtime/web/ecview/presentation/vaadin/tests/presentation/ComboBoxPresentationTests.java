/**
 * Copyright (c) 2013 COMPEX Systemhaus GmbH Heidelberg. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Jose C. Dominguez - initial API and implementation
 */
package org.lunifera.runtime.web.ecview.presentation.vaadin.tests.presentation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
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
import org.lunifera.ecview.core.common.model.core.YEmbeddableSelectionEndpoint;
import org.lunifera.ecview.core.common.model.core.YView;
import org.lunifera.ecview.core.common.presentation.IWidgetPresentation;
import org.lunifera.ecview.core.extension.model.extension.YComboBox;
import org.lunifera.ecview.core.extension.model.extension.YGridLayout;
import org.lunifera.ecview.core.extension.model.extension.YTextField;
import org.lunifera.ecview.core.extension.model.extension.util.SimpleExtensionModelFactory;
import org.lunifera.ecview.core.ui.core.editparts.extension.IComboBoxEditpart;
import org.lunifera.ecview.core.ui.core.editparts.extension.ITextFieldEditpart;
import org.lunifera.runtime.web.ecview.presentation.vaadin.VaadinRenderer;
import org.lunifera.runtime.web.ecview.presentation.vaadin.common.AbstractVaadinWidgetPresenter;
import org.lunifera.runtime.web.ecview.presentation.vaadin.internal.ComboBoxPresentation;
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
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;

/**
 * Tests the {@link ComboBoxPresentation}.
 */
@SuppressWarnings("restriction")
public class ComboBoxPresentationTests {

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
		// .........> yText
		YView yView = factory.createView();
		YGridLayout yGridlayout = factory.createGridLayout();
		yView.setContent(yGridlayout);
		YComboBox yComboBox = factory.createComboBox();
		yGridlayout.getElements().add(yComboBox);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		IComboBoxEditpart comboBoxEditpart = DelegatingEditPartManager
				.getInstance().getEditpart(yComboBox);
		IWidgetPresentation<Component> presentation = comboBoxEditpart
				.getPresentation();
		assertTrue(presentation.isRendered());
		assertFalse(presentation.isDisposed());

		yGridlayout.getElements().remove(yComboBox);
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
		// ......> yText
		YView yView = factory.createView();
		YComboBox yComboBox = factory.createComboBox();
		yView.setContent(yComboBox);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		IComboBoxEditpart comboBoxEditpart = DelegatingEditPartManager
				.getInstance().getEditpart(yComboBox);
		IWidgetPresentation<Component> presentation = comboBoxEditpart
				.getPresentation();

		ComboBox comboBox = (ComboBox) presentation.getWidget();
		assertNotNull(comboBox);
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
		// ......> yText
		YView yView = factory.createView();
		YGridLayout yLayout = factory.createGridLayout();
		yView.setContent(yLayout);
		YComboBox yComboBox1 = factory.createComboBox();
		yComboBox1.setCssID("ID_0815");
		yComboBox1.setCssClass("anyOtherClass");
		yLayout.getElements().add(yComboBox1);
		YComboBox yComboBox2 = factory.createComboBox();
		yLayout.getElements().add(yComboBox2);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		IComboBoxEditpart comboBox1Editpart = DelegatingEditPartManager
				.getInstance().getEditpart(yComboBox1);
		IComboBoxEditpart comboBox2Editpart = DelegatingEditPartManager
				.getInstance().getEditpart(yComboBox2);
		IWidgetPresentation<Component> comboBox1Presentation = comboBox1Editpart
				.getPresentation();
		IWidgetPresentation<Component> comboBox2Presentation = comboBox2Editpart
				.getPresentation();

		ComboBox comboBox1 = (ComboBox) comboBox1Presentation.getWidget();
		ComboBox comboBox2 = (ComboBox) comboBox2Presentation.getWidget();

		// assert css class

		assertTrue(comboBox1.getStyleName().contains("anyOtherClass"));
		assertTrue(comboBox2.getStyleName().contains(
				AbstractVaadinWidgetPresenter.CSS_CLASS_CONTROL));

		// assert css id
		assertEquals("ID_0815", comboBox1.getId());
		assertEquals(comboBox2Editpart.getId(), comboBox2.getId());
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
		// ......> yText
		YView yView = factory.createView();
		YGridLayout yLayout = factory.createGridLayout();
		yView.setContent(yLayout);
		YComboBox yComboBox1 = factory.createComboBox();
		yComboBox1.setType(String.class);
		yLayout.getElements().add(yComboBox1);
		YComboBox yComboBox2 = factory.createComboBox();
		yComboBox2.setType(String.class);
		yLayout.getElements().add(yComboBox2);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		IComboBoxEditpart comboBox1Editpart = DelegatingEditPartManager
				.getInstance().getEditpart(yComboBox1);
		IComboBoxEditpart comboBox2Editpart = DelegatingEditPartManager
				.getInstance().getEditpart(yComboBox2);
		IWidgetPresentation<Component> comboBox1Presentation = comboBox1Editpart
				.getPresentation();
		IWidgetPresentation<Component> comboBox2Presentation = comboBox2Editpart
				.getPresentation();
		ComboBox comboBox1 = (ComboBox) comboBox1Presentation.getWidget();
		ComboBox comboBox2 = (ComboBox) comboBox2Presentation.getWidget();

		// start tests
		//
		assertTrue(comboBox1.isVisible());
		assertTrue(comboBox1.isEnabled());
		assertFalse(comboBox1.isReadOnly());

		assertTrue(comboBox2.isVisible());
		assertTrue(comboBox2.isEnabled());
		assertFalse(comboBox2.isReadOnly());

		yComboBox1.setVisible(false);
		assertFalse(comboBox1.isVisible());

		yComboBox1.setEnabled(false);
		assertFalse(comboBox1.isEnabled());

		yComboBox1.setEditable(false);
		assertTrue(comboBox1.isReadOnly());

		// target to model
		comboBox1.setReadOnly(false);
		assertTrue(yComboBox1.isEditable());

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
		YComboBox yComboBox1 = factory.createComboBox();
		yComboBox1.setType(String.class);
		yLayout.getElements().add(yComboBox1);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		IComboBoxEditpart comboBox1Editpart = DelegatingEditPartManager
				.getInstance().getEditpart(yComboBox1);
		IWidgetPresentation<Component> comboBox1Presentation = comboBox1Editpart
				.getPresentation();
		ComboBox comboBox1 = (ComboBox) comboBox1Presentation.getWidget();

		// start tests
		//

		Container.Indexed container = (Indexed) comboBox1
				.getContainerDataSource();
		assertEquals(0, container.size());

		// add
		container.addItem("Blabla");
		assertEquals(1, yComboBox1.getCollection().size());
		assertEquals(1, container.size());

		yComboBox1.getCollection().add("Huhu");
		assertEquals(2, yComboBox1.getCollection().size());
		assertEquals(2, container.size());

		// add at index
		yComboBox1.getCollection().add(0, "First");
		assertEquals("First", yComboBox1.getCollection().get(0));
		assertEquals("First", container.getItemIds(0, 1).get(0));

		container.addItemAt(0, "Another First");
		assertEquals("Another First", yComboBox1.getCollection().get(0));
		assertEquals("Another First", container.getItemIds(0, 1).get(0));

		// move
		yComboBox1.getCollection().move(1, 0);
		assertEquals("First", yComboBox1.getCollection().get(0));
		assertEquals("First", container.getItemIds(0, 1).get(0));

		// remove all
		container.removeAllItems();
		assertEquals(0, yComboBox1.getCollection().size());
		assertEquals(0, container.size());

	}

	/**
	 * Test the internal structure based on CSS.
	 * 
	 * @throws Exception
	 */
	@Test
	// BEGIN SUPRESS CATCH EXCEPTION
	public void test_CollectionBinding_ComboToCombo() throws Exception {
		// END SUPRESS CATCH EXCEPTION
		// build the view model
		// ...> yView
		// ......> yText
		YView yView = factory.createView();
		YGridLayout yLayout = factory.createGridLayout();
		yView.setContent(yLayout);
		YComboBox yComboBox1 = factory.createComboBox();
		yComboBox1.setType(String.class);
		yLayout.getElements().add(yComboBox1);
		YComboBox yComboBox2 = factory.createComboBox();
		yComboBox2.setType(String.class);
		yLayout.getElements().add(yComboBox2);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		IComboBoxEditpart comboBox1Editpart = DelegatingEditPartManager
				.getInstance().getEditpart(yComboBox1);
		IComboBoxEditpart comboBox2Editpart = DelegatingEditPartManager
				.getInstance().getEditpart(yComboBox2);
		IWidgetPresentation<Component> comboBox1Presentation = comboBox1Editpart
				.getPresentation();
		IWidgetPresentation<Component> comboBox2Presentation = comboBox2Editpart
				.getPresentation();
		ComboBox comboBox1 = (ComboBox) comboBox1Presentation.getWidget();
		ComboBox comboBox2 = (ComboBox) comboBox2Presentation.getWidget();

		Container.Indexed indexedDs1 = (Indexed) comboBox1
				.getContainerDataSource();
		Container.Indexed indexedDs2 = (Indexed) comboBox2
				.getContainerDataSource();

		YBindingSet yBindingSet = yView.getOrCreateBindingSet();

		// start tests
		//

		YEmbeddableCollectionEndpoint endp1 = yComboBox1
				.createCollectionEndpoint();
		YEmbeddableCollectionEndpoint endp2 = yComboBox2
				.createCollectionEndpoint();
		yBindingSet.addBinding(endp1, endp2);

		assertEquals(0, yComboBox1.getCollection().size());
		assertEquals(0, yComboBox2.getCollection().size());
		assertEquals(0, indexedDs1.size());
		assertEquals(0, indexedDs2.size());

		// add to yCombo1
		yComboBox1.getCollection().add("Huhu");
		assertEquals(1, yComboBox2.getCollection().size());
		assertEquals("Huhu", yComboBox2.getCollection().get(0));
		assertEquals("Huhu", indexedDs1.getItemIds(0, 1).get(0));
		assertEquals("Huhu", indexedDs2.getItemIds(0, 1).get(0));
		assertEquals(1, indexedDs1.size());
		assertEquals(1, indexedDs2.size());

		// add to yCombo1
		yComboBox1.getCollection().add("Huhu2");
		assertEquals(2, yComboBox2.getCollection().size());
		assertEquals("Huhu", yComboBox2.getCollection().get(0));
		assertEquals("Huhu2", yComboBox2.getCollection().get(1));
		assertEquals("Huhu", yComboBox1.getCollection().get(0));
		assertEquals("Huhu2", yComboBox1.getCollection().get(1));
		assertEquals("Huhu", indexedDs1.getItemIds(0, 1).get(0));
		assertEquals("Huhu2", indexedDs1.getItemIds(1, 1).get(0));
		assertEquals("Huhu", indexedDs2.getItemIds(0, 1).get(0));
		assertEquals("Huhu2", indexedDs2.getItemIds(1, 1).get(0));
		assertEquals(2, indexedDs1.size());
		assertEquals(2, indexedDs2.size());

		// remove from yCombo2
		yComboBox2.getCollection().remove("Huhu");
		assertEquals(1, yComboBox1.getCollection().size());
		assertEquals(1, yComboBox2.getCollection().size());
		assertEquals(1, indexedDs1.size());
		assertEquals(1, indexedDs2.size());

		// add another to yCombo2
		yComboBox2.getCollection().add("Blabla");
		assertEquals(2, yComboBox1.getCollection().size());
		assertEquals(2, yComboBox2.getCollection().size());
		assertEquals("Huhu2", yComboBox1.getCollection().get(0));
		assertEquals("Blabla", yComboBox1.getCollection().get(1));
		assertEquals("Huhu2", yComboBox2.getCollection().get(0));
		assertEquals("Blabla", yComboBox2.getCollection().get(1));
		assertEquals("Huhu2", indexedDs1.getItemIds(0, 1).get(0));
		assertEquals("Blabla", indexedDs1.getItemIds(1, 1).get(0));
		assertEquals("Huhu2", indexedDs2.getItemIds(0, 1).get(0));
		assertEquals("Blabla", indexedDs2.getItemIds(1, 1).get(0));
		assertEquals(2, indexedDs1.size());
		assertEquals(2, indexedDs2.size());

		yComboBox2.getCollection().move(0, 1);
		assertEquals(2, yComboBox1.getCollection().size());
		assertEquals(2, yComboBox2.getCollection().size());
		assertEquals("Blabla", yComboBox1.getCollection().get(0));
		assertEquals("Huhu2", yComboBox1.getCollection().get(1));
		assertEquals("Blabla", yComboBox2.getCollection().get(0));
		assertEquals("Huhu2", yComboBox2.getCollection().get(1));
		assertEquals("Blabla", indexedDs1.getItemIds(0, 1).get(0));
		assertEquals("Huhu2", indexedDs1.getItemIds(1, 1).get(0));
		assertEquals("Blabla", indexedDs2.getItemIds(0, 1).get(0));
		assertEquals("Huhu2", indexedDs2.getItemIds(1, 1).get(0));
		assertEquals(2, indexedDs1.size());
		assertEquals(2, indexedDs2.size());

		yComboBox2.getCollection().clear();
		assertEquals(0, yComboBox1.getCollection().size());
		assertEquals(0, yComboBox2.getCollection().size());
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
		YComboBox yComboBox1 = factory.createComboBox();
		yComboBox1.setType(String.class);
		yLayout.getElements().add(yComboBox1);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		IComboBoxEditpart comboBox1Editpart = DelegatingEditPartManager
				.getInstance().getEditpart(yComboBox1);
		IWidgetPresentation<Component> comboBox1Presentation = comboBox1Editpart
				.getPresentation();
		ComboBox comboBox1 = (ComboBox) comboBox1Presentation.getWidget();

		// start tests
		//
		Container.Indexed container = (Indexed) comboBox1
				.getContainerDataSource();
		assertEquals(0, container.size());

		assertNull(comboBox1.getValue());
		assertNull(yComboBox1.getSelection());

		// add
		yComboBox1.getCollection().add("Huhu");
		yComboBox1.getCollection().add("Haha");
		assertEquals(2, container.size());

		assertNull(yComboBox1.getSelection());
		assertNull(comboBox1.getValue());

		// test set selection
		yComboBox1.setSelection("Huhu");
		assertEquals("Huhu", yComboBox1.getSelection());
		assertEquals("Huhu", comboBox1.getValue());

		comboBox1.setValue("Haha");
		assertEquals("Haha", yComboBox1.getSelection());
		assertEquals("Haha", comboBox1.getValue());

		// test set selection null
		comboBox1.setValue(null);
		assertNull(yComboBox1.getSelection());
		assertNull(comboBox1.getValue());

		comboBox1.setValue("Haha");
		assertEquals("Haha", yComboBox1.getSelection());
		assertEquals("Haha", comboBox1.getValue());

		yComboBox1.setSelection(null);
		assertNull(yComboBox1.getSelection());
		assertNull(comboBox1.getValue());

		// test remove element that is selected
		// add
		yComboBox1.getCollection().add("Huhu");
		yComboBox1.getCollection().add("Haha");
		assertEquals(2, container.size());

		yComboBox1.setSelection("Huhu");
		assertEquals("Huhu", yComboBox1.getSelection());
		assertEquals("Huhu", comboBox1.getValue());

		yComboBox1.getCollection().remove("Huhu");
		assertNull(comboBox1.getValue());
		assertNull(yComboBox1.getSelection());

		// test remove element that is selected
		// add
		yComboBox1.getCollection().add("Huhu");
		assertEquals(2, container.size());

		yComboBox1.setSelection("Huhu");
		assertEquals("Huhu", yComboBox1.getSelection());
		assertEquals("Huhu", comboBox1.getValue());

		comboBox1.setValue(null);
		assertNull(yComboBox1.getSelection());
		assertNull(comboBox1.getValue());

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
		YComboBox yComboBox1 = factory.createComboBox();
		yLayout.getElements().add(yComboBox1);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		IComboBoxEditpart comboBox1Editpart = DelegatingEditPartManager
				.getInstance().getEditpart(yComboBox1);
		IWidgetPresentation<Component> comboBox1Presentation = comboBox1Editpart
				.getPresentation();
		ComboBox comboBox1 = (ComboBox) comboBox1Presentation.getWidget();

		// start tests
		//
		Container.Indexed container = (Indexed) comboBox1
				.getContainerDataSource();
		assertEquals(0, container.size());

		assertNull(comboBox1.getValue());
		assertNull(yComboBox1.getSelection());

		// add
		assertNull(yComboBox1.getSelection());
		assertNull(comboBox1.getValue());

		// test set selection -> Not a valid selection
		yComboBox1.setSelection("Huhu");
		assertEquals("Huhu", yComboBox1.getSelection());
		assertNull("Huhu", comboBox1.getValue());

	}

	/**
	 * Test the internal structure based on CSS.
	 * 
	 * @throws Exception
	 */
	@Test
	// BEGIN SUPRESS CATCH EXCEPTION
	public void test_SelectionBinding_ComboToCombo() throws Exception {
		// END SUPRESS CATCH EXCEPTION
		// build the view model
		// ...> yView
		// ......> yText
		YView yView = factory.createView();
		YGridLayout yLayout = factory.createGridLayout();
		yView.setContent(yLayout);
		YComboBox yComboBox1 = factory.createComboBox();
		yComboBox1.setType(String.class);
		yLayout.getElements().add(yComboBox1);
		YComboBox yComboBox2 = factory.createComboBox();
		yComboBox2.setType(String.class);
		yLayout.getElements().add(yComboBox2);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		IComboBoxEditpart comboBox1Editpart = DelegatingEditPartManager
				.getInstance().getEditpart(yComboBox1);
		IComboBoxEditpart comboBox2Editpart = DelegatingEditPartManager
				.getInstance().getEditpart(yComboBox2);
		IWidgetPresentation<Component> comboBox1Presentation = comboBox1Editpart
				.getPresentation();
		IWidgetPresentation<Component> comboBox2Presentation = comboBox2Editpart
				.getPresentation();
		ComboBox comboBox1 = (ComboBox) comboBox1Presentation.getWidget();
		ComboBox comboBox2 = (ComboBox) comboBox2Presentation.getWidget();

		YBindingSet yBindingSet = yView.getOrCreateBindingSet();

		// start tests
		//

		YEmbeddableCollectionEndpoint endp1 = yComboBox1
				.createCollectionEndpoint();
		YEmbeddableCollectionEndpoint endp2 = yComboBox2
				.createCollectionEndpoint();
		yBindingSet.addBinding(endp1, endp2);

		YEmbeddableSelectionEndpoint endpSel1 = yComboBox1
				.createSelectionEndpoint();
		YEmbeddableSelectionEndpoint endpSel2 = yComboBox2
				.createSelectionEndpoint();
		yBindingSet.addBinding(endpSel1, endpSel2);

		Container.Indexed container1 = (Indexed) comboBox1
				.getContainerDataSource();
		Container.Indexed container2 = (Indexed) comboBox2
				.getContainerDataSource();
		assertEquals(0, container1.size());
		assertEquals(0, container2.size());

		assertNull(comboBox1.getValue());
		assertNull(yComboBox1.getSelection());

		// add
		yComboBox1.getCollection().add("Huhu");
		yComboBox2.getCollection().add("Haha");
		assertEquals(2, container1.size());
		assertEquals(2, container2.size());

		assertNull(yComboBox1.getSelection());
		assertNull(comboBox1.getValue());
		assertNull(yComboBox2.getSelection());
		assertNull(comboBox2.getValue());

		// test set selection
		yComboBox1.setSelection("Huhu");
		assertEquals("Huhu", yComboBox1.getSelection());
		assertEquals("Huhu", comboBox1.getValue());
		assertEquals("Huhu", yComboBox2.getSelection());
		assertEquals("Huhu", comboBox2.getValue());

		comboBox1.setValue("Haha");
		assertEquals("Haha", yComboBox1.getSelection());
		assertEquals("Haha", comboBox1.getValue());
		assertEquals("Haha", yComboBox2.getSelection());
		assertEquals("Haha", comboBox2.getValue());

		// test set selection null
		comboBox1.setValue(null);
		assertNull(yComboBox1.getSelection());
		assertNull(comboBox1.getValue());
		assertNull(yComboBox2.getSelection());
		assertNull(comboBox2.getValue());

		comboBox1.setValue("Haha");
		assertEquals("Haha", yComboBox1.getSelection());
		assertEquals("Haha", comboBox1.getValue());
		assertEquals("Haha", yComboBox2.getSelection());
		assertEquals("Haha", comboBox2.getValue());

		comboBox2.setValue(null);
		assertNull(yComboBox1.getSelection());
		assertNull(comboBox1.getValue());
		assertNull(yComboBox2.getSelection());
		assertNull(comboBox2.getValue());

		comboBox2.setValue("Haha");
		assertEquals("Haha", yComboBox1.getSelection());
		assertEquals("Haha", comboBox1.getValue());
		assertEquals("Haha", yComboBox2.getSelection());
		assertEquals("Haha", comboBox2.getValue());

		yComboBox1.setSelection(null);
		assertNull(yComboBox1.getSelection());
		assertNull(comboBox1.getValue());
		assertNull(yComboBox2.getSelection());
		assertNull(comboBox2.getValue());

		comboBox2.setValue("Haha");
		assertEquals("Haha", yComboBox1.getSelection());
		assertEquals("Haha", comboBox1.getValue());
		assertEquals("Haha", yComboBox2.getSelection());
		assertEquals("Haha", comboBox2.getValue());

		yComboBox2.setSelection(null);
		assertNull(yComboBox1.getSelection());
		assertNull(comboBox1.getValue());
		assertNull(yComboBox2.getSelection());
		assertNull(comboBox2.getValue());

		// test remove element that is selected
		// add
		assertEquals(2, container1.size());
		assertEquals(2, container2.size());

		yComboBox1.setSelection("Huhu");
		assertEquals("Huhu", yComboBox1.getSelection());
		assertEquals("Huhu", yComboBox2.getSelection());
		assertEquals("Huhu", comboBox1.getValue());
		assertEquals("Huhu", comboBox2.getValue());

		yComboBox1.getCollection().remove("Huhu");
		assertNull(comboBox1.getValue());
		assertNull(comboBox2.getValue());
		assertNull(yComboBox1.getSelection());
		assertNull(yComboBox2.getSelection());

		// test remove element that is selected
		// add
		yComboBox2.getCollection().add("Huhu");
		assertEquals(2, container1.size());
		assertEquals(2, container2.size());

		yComboBox1.setSelection("Huhu");
		assertEquals("Huhu", yComboBox1.getSelection());
		assertEquals("Huhu", comboBox1.getValue());
		assertEquals("Huhu", yComboBox2.getSelection());
		assertEquals("Huhu", comboBox2.getValue());

		comboBox2.setValue(null);
		assertNull(yComboBox1.getSelection());
		assertNull(comboBox1.getValue());
		assertNull(yComboBox2.getSelection());
		assertNull(comboBox2.getValue());
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
		YComboBox yComboBox1 = factory.createComboBox();
		yComboBox1.setType(Bar.class);
		yLayout.getElements().add(yComboBox1);
		YTextField yText = factory.createTextField();
		yLayout.getElements().add(yText);

		YBindingSet yBindingSet = yView.getOrCreateBindingSet();
		YEmbeddableSelectionEndpoint selectionBindingEndpoint = yComboBox1
				.createSelectionEndpoint();
		selectionBindingEndpoint.setAttributePath("myfoo.name");
		yBindingSet.addBinding(yText.createValueEndpoint(),
				selectionBindingEndpoint);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		IComboBoxEditpart combobox1Editpart = DelegatingEditPartManager
				.getInstance().getEditpart(yComboBox1);
		IWidgetPresentation<Component> combobox1Presentation = combobox1Editpart
				.getPresentation();
		ComboBox combobox1 = (ComboBox) combobox1Presentation.getWidget();

		ITextFieldEditpart textEditpart = DelegatingEditPartManager
				.getInstance().getEditpart(yText);
		IWidgetPresentation<Component> textPresentation = textEditpart
				.getPresentation();
		TextField text = (TextField) textPresentation.getWidget();

		// start tests
		//
		Container.Indexed container = (Indexed) combobox1
				.getContainerDataSource();
		assertEquals(0, container.size());

		assertNull(combobox1.getValue());
		assertNull(yComboBox1.getSelection());
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

		yComboBox1.getCollection().add(bar1);
		yComboBox1.getCollection().add(bar2);
		assertEquals(2, container.size());

		assertNull(yComboBox1.getSelection());
		assertNull(combobox1.getValue());
		assertNull(yText.getValue());
		assertNull(text.getValue());

		// test set selection
		yComboBox1.setSelection(bar1);
		assertEquals("Foo1", yText.getValue());
		assertEquals("Foo1", text.getValue());

		yComboBox1.setSelection(bar2);
		assertEquals("Foo2", yText.getValue());
		assertEquals("Foo2", text.getValue());

		// test set selection null
		yComboBox1.setSelection(null);
		assertNull(yText.getValue());
		assertNull(text.getValue());

		combobox1.setValue(bar1);
		assertEquals("Foo1", yText.getValue());
		assertEquals("Foo1", text.getValue());

		combobox1.setValue(bar2);
		assertEquals("Foo2", yText.getValue());
		assertEquals("Foo2", text.getValue());

		// test set selection null
		combobox1.setValue(null);
		assertNull(yText.getValue());
		assertNull(text.getValue());

		// test remove element that is selected
		// add
		combobox1.setValue(bar2);
		assertEquals("Foo2", yText.getValue());
		assertEquals("Foo2", text.getValue());

		yComboBox1.getCollection().clear();

		assertNull(yComboBox1.getSelection());
		assertNull(combobox1.getValue());

		// test setValue to textfield
		yComboBox1.getCollection().add(bar1);
		yComboBox1.getCollection().add(bar2);
		assertEquals(2, container.size());

		yComboBox1.setSelection(bar2);
		assertEquals("Foo2", yText.getValue());
		assertEquals("Foo2", text.getValue());

		yText.setValue("Foo2_1");
		assertEquals("Foo2_1", foo2.getName());
		assertEquals("Foo2_1", text.getValue());

		text.setValue("Foo2_2");
		assertEquals("Foo2_2", foo2.getName());
		assertEquals("Foo2_2", yText.getValue());

		yComboBox1.setSelection(bar1);
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
	 * Test the automatic disposal of bindings
	 * 
	 * @throws ContextException
	 */
	@Test
	public void testBindingIsDisposed() throws ContextException {
		YView yView = factory.createView();
		YGridLayout yGridlayout = factory.createGridLayout();
		yView.setContent(yGridlayout);
		YComboBox yComboBox = factory.createComboBox();
		yComboBox.setType(String.class);
		yGridlayout.getElements().add(yComboBox);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		IComboBoxEditpart comboBoxEditpart = DelegatingEditPartManager
				.getInstance().getEditpart(yComboBox);
		IWidgetPresentation<Component> presentation = comboBoxEditpart
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
	public void test_SelectionBinding_Single_DetailBinding_ToBean()
			throws Exception {
		// END SUPRESS CATCH EXCEPTION
		// build the view model
		// ...> yView
		// ......> yText
		YView yView = factory.createView();
		YGridLayout yLayout = factory.createGridLayout();
		yView.setContent(yLayout);
		YComboBox yComboBox1 = factory.createComboBox();
		yComboBox1.setType(Bar.class);
		yLayout.getElements().add(yComboBox1);
		YTextField yText = factory.createTextField();
		yLayout.getElements().add(yText);

		YBindingSet yBindingSet = yView.getOrCreateBindingSet();
		YDetailValueBindingEndpoint yDetailEndpoint = yComboBox1
				.createSelectionEndpoint().createDetailValueEndpoint();
		yDetailEndpoint.setPropertyPath("myfoo.name");
		yDetailEndpoint.setType(Bar.class);
		yBindingSet.addBinding(yText.createValueEndpoint(), yDetailEndpoint);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		IComboBoxEditpart combobox1Editpart = DelegatingEditPartManager
				.getInstance().getEditpart(yComboBox1);
		IWidgetPresentation<Component> combobox1Presentation = combobox1Editpart
				.getPresentation();
		ComboBox combobox1 = (ComboBox) combobox1Presentation.getWidget();

		ITextFieldEditpart textEditpart = DelegatingEditPartManager
				.getInstance().getEditpart(yText);
		IWidgetPresentation<Component> textPresentation = textEditpart
				.getPresentation();
		TextField text = (TextField) textPresentation.getWidget();

		// start tests
		//
		Container.Indexed container = (Indexed) combobox1
				.getContainerDataSource();
		assertEquals(0, container.size());

		assertNull(combobox1.getValue());
		assertNull(yComboBox1.getSelection());
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

		yComboBox1.getCollection().add(bar1);
		yComboBox1.getCollection().add(bar2);
		assertEquals(2, container.size());

		assertNull(yComboBox1.getSelection());
		assertNull(combobox1.getValue());
		assertNull(yText.getValue());
		assertNull(text.getValue());

		// test set selection
		yComboBox1.setSelection(bar1);
		assertEquals("Foo1", yText.getValue());
		assertEquals("Foo1", text.getValue());

		yComboBox1.setSelection(bar2);
		assertEquals("Foo2", yText.getValue());
		assertEquals("Foo2", text.getValue());

		// test set selection null
		yComboBox1.setSelection(null);
		assertNull(yText.getValue());
		assertNull(text.getValue());

		combobox1.setValue(bar1);
		assertEquals("Foo1", yText.getValue());
		assertEquals("Foo1", text.getValue());

		combobox1.setValue(bar2);
		assertEquals("Foo2", yText.getValue());
		assertEquals("Foo2", text.getValue());

		// test set selection null
		combobox1.setValue(null);
		assertNull(yText.getValue());
		assertNull(text.getValue());

		// test remove element that is selected
		// add
		combobox1.setValue(bar2);
		assertEquals("Foo2", yText.getValue());
		assertEquals("Foo2", text.getValue());

		yComboBox1.getCollection().clear();

		assertNull(yComboBox1.getSelection());
		assertNull(combobox1.getValue());

		// test setValue to textfield
		yComboBox1.getCollection().add(bar1);
		yComboBox1.getCollection().add(bar2);
		assertEquals(2, container.size());

		yComboBox1.setSelection(bar2);
		assertEquals("Foo2", yText.getValue());
		assertEquals("Foo2", text.getValue());

		yText.setValue("Foo2_1");
		assertEquals("Foo2_1", foo2.getName());
		assertEquals("Foo2_1", text.getValue());

		text.setValue("Foo2_2");
		assertEquals("Foo2_2", foo2.getName());
		assertEquals("Foo2_2", yText.getValue());

		yComboBox1.setSelection(bar1);
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
		YComboBox yComboBox1 = factory.createComboBox();
		yComboBox1.setType(EmfBar.class);
		yComboBox1.setEmfNsURI(ModelPackage.eINSTANCE.getNsURI());
		yLayout.getElements().add(yComboBox1);
		YTextField yText = factory.createTextField();
		yLayout.getElements().add(yText);

		YBindingSet yBindingSet = yView.getOrCreateBindingSet();
		YDetailValueBindingEndpoint yDetailEndpoint = yComboBox1
				.createSelectionEndpoint().createDetailValueEndpoint();
		yDetailEndpoint.setType(EmfBar.class);
		yDetailEndpoint.getFeatures().add(
				ModelPackage.eINSTANCE.getEmfBar_Myfoo());
		yDetailEndpoint.getFeatures().add(
				ModelPackage.eINSTANCE.getEmfFoo_Name());
		yBindingSet.addBinding(yText.createValueEndpoint(), yDetailEndpoint);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		IComboBoxEditpart combobox1Editpart = DelegatingEditPartManager
				.getInstance().getEditpart(yComboBox1);
		IWidgetPresentation<Component> combobox1Presentation = combobox1Editpart
				.getPresentation();
		ComboBox combobox1 = (ComboBox) combobox1Presentation.getWidget();

		ITextFieldEditpart textEditpart = DelegatingEditPartManager
				.getInstance().getEditpart(yText);
		IWidgetPresentation<Component> textPresentation = textEditpart
				.getPresentation();
		TextField text = (TextField) textPresentation.getWidget();

		// start tests
		//
		Container.Indexed container = (Indexed) combobox1
				.getContainerDataSource();
		assertEquals(0, container.size());

		assertNull(combobox1.getValue());
		assertNull(yComboBox1.getSelection());
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

		yComboBox1.getCollection().add(bar1);
		yComboBox1.getCollection().add(bar2);
		assertEquals(2, container.size());

		assertNull(yComboBox1.getSelection());
		assertNull(combobox1.getValue());
		assertNull(yText.getValue());
		assertNull(text.getValue());

		// test set selection
		yComboBox1.setSelection(bar1);
		assertEquals("Foo1", yText.getValue());
		assertEquals("Foo1", text.getValue());

		yComboBox1.setSelection(bar2);
		assertEquals("Foo2", yText.getValue());
		assertEquals("Foo2", text.getValue());

		// test set selection null
		yComboBox1.setSelection(null);
		assertNull(yText.getValue());
		assertNull(text.getValue());

		combobox1.setValue(bar1);
		assertEquals("Foo1", yText.getValue());
		assertEquals("Foo1", text.getValue());

		combobox1.setValue(bar2);
		assertEquals("Foo2", yText.getValue());
		assertEquals("Foo2", text.getValue());

		// test set selection null
		combobox1.setValue(null);
		assertNull(yText.getValue());
		assertNull(text.getValue());

		// test remove element that is selected
		// add
		combobox1.setValue(bar2);
		assertEquals("Foo2", yText.getValue());
		assertEquals("Foo2", text.getValue());

		yComboBox1.getCollection().clear();

		assertNull(yComboBox1.getSelection());
		assertNull(combobox1.getValue());

		// test setValue to textfield
		yComboBox1.getCollection().add(bar1);
		yComboBox1.getCollection().add(bar2);
		assertEquals(2, container.size());

		yComboBox1.setSelection(bar2);
		assertEquals("Foo2", yText.getValue());
		assertEquals("Foo2", text.getValue());

		yText.setValue("Foo2_1");
		assertEquals("Foo2_1", foo2.getName());
		assertEquals("Foo2_1", text.getValue());

		text.setValue("Foo2_2");
		assertEquals("Foo2_2", foo2.getName());
		assertEquals("Foo2_2", yText.getValue());

		yComboBox1.setSelection(bar1);
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
		YGridLayout yGridlayout = factory.createGridLayout();
		yView.setContent(yGridlayout);
		YComboBox yComboBox = factory.createComboBox();
		yComboBox.setType(String.class);
		yView.setContent(yComboBox);

		// set the i18n key
		yComboBox.setLabelI18nKey(I18nServiceForTests.KEY__AGE);

		// prepare the I18nService and pass it to the renderer
		Map<String, Object> parameter = new HashMap<String, Object>();
		Map<String, Object> services = new HashMap<String, Object>();
		parameter.put(IViewContext.PARAM_SERVICES, services);
		services.put(II18nService.ID, new I18nServiceForTests());

		VaadinRenderer renderer = new VaadinRenderer();
		IViewContext context = renderer.render(rootLayout, yView, parameter);
		IComboBoxEditpart editpart = DelegatingEditPartManager.getInstance()
				.getEditpart(yComboBox);
		ComboBoxPresentation presentation = editpart.getPresentation();

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
		YComboBox yComboBox = factory.createComboBox();
		yComboBox.setType(String.class);
		yLayout.getElements().add(yComboBox);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		IComboBoxEditpart editpart = DelegatingEditPartManager.getInstance()
				.getEditpart(yComboBox);
		IWidgetPresentation<Component> presentation = editpart
				.getPresentation();
		ComboBox box = (ComboBox) presentation.getWidget();

		ValueBean bean = new ValueBean(false);
		YBeanValueBindingEndpoint yBeanBinding = factory
				.createBeanBindingEndpoint();
		yBeanBinding.setBean(bean);
		yBeanBinding.setPropertyPath("boolValue");
		YBindingSet yBindingSet = yView.getOrCreateBindingSet();
		yBindingSet
				.addBinding(yComboBox.createEditableEndpoint(), yBeanBinding);

		// test binding
		assertFalse(yComboBox.isEditable());
		assertFalse(!box.isReadOnly());
		assertFalse(bean.isBoolValue());

		bean.setBoolValue(true);
		assertTrue(yComboBox.isEditable());
		assertTrue(!box.isReadOnly());
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
		YComboBox yComboBox = factory.createComboBox();
		yComboBox.setType(String.class);
		yLayout.getElements().add(yComboBox);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		IComboBoxEditpart editpart = DelegatingEditPartManager.getInstance()
				.getEditpart(yComboBox);
		IWidgetPresentation<Component> presentation = editpart
				.getPresentation();
		ComboBox box = (ComboBox) presentation.getWidget();

		ValueBean bean = new ValueBean(false);
		YBeanValueBindingEndpoint yBeanBinding = factory
				.createBeanBindingEndpoint();
		yBeanBinding.setBean(bean);
		yBeanBinding.setPropertyPath("boolValue");
		YBindingSet yBindingSet = yView.getOrCreateBindingSet();
		yBindingSet.addBinding(yComboBox.createVisibleEndpoint(), yBeanBinding);

		// test binding
		assertFalse(yComboBox.isVisible());
		assertFalse(box.isVisible());
		assertFalse(bean.isBoolValue());

		bean.setBoolValue(true);
		assertTrue(yComboBox.isVisible());
		assertTrue(box.isVisible());
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
		YComboBox yComboBox = factory.createComboBox();
		yComboBox.setType(String.class);
		yLayout.getElements().add(yComboBox);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		IComboBoxEditpart editpart = DelegatingEditPartManager.getInstance()
				.getEditpart(yComboBox);
		IWidgetPresentation<Component> presentation = editpart
				.getPresentation();
		ComboBox box = (ComboBox) presentation.getWidget();

		ValueBean bean = new ValueBean(false);
		YBeanValueBindingEndpoint yBeanBinding = factory
				.createBeanBindingEndpoint();
		yBeanBinding.setBean(bean);
		yBeanBinding.setPropertyPath("boolValue");
		YBindingSet yBindingSet = yView.getOrCreateBindingSet();
		yBindingSet.addBinding(yComboBox.createEnabledEndpoint(), yBeanBinding);

		// test binding
		assertFalse(yComboBox.isEnabled());
		assertFalse(box.isEnabled());
		assertFalse(bean.isBoolValue());

		bean.setBoolValue(true);
		assertTrue(yComboBox.isEnabled());
		assertTrue(box.isEnabled());
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
