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
import org.eclipse.emf.ecp.ecview.extension.model.extension.YSelectionType;
import org.eclipse.emf.ecp.ecview.extension.model.extension.YTextField;
import org.eclipse.emf.ecp.ecview.extension.model.extension.YTree;
import org.eclipse.emf.ecp.ecview.extension.model.extension.util.SimpleExtensionModelFactory;
import org.eclipse.emf.ecp.ecview.ui.core.editparts.extension.ITextFieldEditpart;
import org.eclipse.emf.ecp.ecview.ui.core.editparts.extension.ITreeEditpart;
import org.junit.Before;
import org.junit.Test;
import org.lunifera.runtime.web.ecview.presentation.vaadin.VaadinRenderer;
import org.lunifera.runtime.web.ecview.presentation.vaadin.internal.AbstractVaadinWidgetPresenter;
import org.lunifera.runtime.web.ecview.presentation.vaadin.internal.TreePresentation;
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
import com.vaadin.ui.TextField;
import com.vaadin.ui.Tree;
import com.vaadin.ui.UI;

/**
 * Tests the {@link TreePresentation}.
 */
@SuppressWarnings("restriction")
public class TreePresentationTests {

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
		// .........> yTree
		YView yView = factory.createView();
		YGridLayout yGridlayout = factory.createGridLayout();
		yView.setContent(yGridlayout);
		YTree yTree = factory.createTree();
		yGridlayout.getElements().add(yTree);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		ITreeEditpart treeEditpart = DelegatingEditPartManager.getInstance()
				.getEditpart(yTree);
		IWidgetPresentation<Component> presentation = treeEditpart
				.getPresentation();
		assertTrue(presentation.isRendered());
		assertFalse(presentation.isDisposed());

		yGridlayout.getElements().remove(yTree);
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
		// ......> yTree
		YView yView = factory.createView();
		YTree yTree = factory.createTree();
		yView.setContent(yTree);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		ITreeEditpart treeEditpart = DelegatingEditPartManager.getInstance()
				.getEditpart(yTree);
		IWidgetPresentation<Component> presentation = treeEditpart
				.getPresentation();
		ComponentContainer baseComponentContainer = (ComponentContainer) presentation
				.getWidget();

		Tree label = (Tree) unwrapTree(baseComponentContainer);
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
		// ......> yTree
		YView yView = factory.createView();
		YGridLayout yLayout = factory.createGridLayout();
		yView.setContent(yLayout);
		YTree yTree1 = factory.createTree();
		yTree1.setCssID("ID_0815");
		yTree1.setCssClass("anyOtherClass");
		yLayout.getElements().add(yTree1);
		YTree yTree2 = factory.createTree();
		yLayout.getElements().add(yTree2);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		ITreeEditpart tree1Editpart = DelegatingEditPartManager.getInstance()
				.getEditpart(yTree1);
		ITreeEditpart tree2Editpart = DelegatingEditPartManager.getInstance()
				.getEditpart(yTree2);
		IWidgetPresentation<Component> tree1Presentation = tree1Editpart
				.getPresentation();
		IWidgetPresentation<Component> tree2Presentation = tree2Editpart
				.getPresentation();
		ComponentContainer tree1BaseComponentContainer = (ComponentContainer) tree1Presentation
				.getWidget();
		ComponentContainer tree2BaseComponentContainer = (ComponentContainer) tree2Presentation
				.getWidget();

		Tree label1 = (Tree) unwrapTree(tree1BaseComponentContainer);
		Tree label2 = (Tree) unwrapTree(tree2BaseComponentContainer);

		// assert css class
		assertTrue(tree1BaseComponentContainer.getStyleName().contains(
				AbstractVaadinWidgetPresenter.CSS_CLASS__CONTROL_BASE));
		assertTrue(tree2BaseComponentContainer.getStyleName().contains(
				AbstractVaadinWidgetPresenter.CSS_CLASS__CONTROL_BASE));

		assertTrue(label1.getStyleName().contains("anyOtherClass"));
		assertTrue(label2.getStyleName().contains(
				AbstractVaadinWidgetPresenter.CSS_CLASS__CONTROL));

		// assert css id
		assertEquals("ID_0815", tree1BaseComponentContainer.getId());
		assertNull(label1.getId());
		assertEquals(tree2Editpart.getId(), tree2BaseComponentContainer.getId());
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
		// ......> yTree
		YView yView = factory.createView();
		YGridLayout yLayout = factory.createGridLayout();
		yView.setContent(yLayout);
		YTree yTree1 = factory.createTree();
		yLayout.getElements().add(yTree1);
		YTree yTree2 = factory.createTree();
		yLayout.getElements().add(yTree2);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		ITreeEditpart label1Editpart = DelegatingEditPartManager.getInstance()
				.getEditpart(yTree1);
		ITreeEditpart label2Editpart = DelegatingEditPartManager.getInstance()
				.getEditpart(yTree2);
		IWidgetPresentation<Component> tree1Presentation = label1Editpart
				.getPresentation();
		IWidgetPresentation<Component> tree2Presentation = label2Editpart
				.getPresentation();
		ComponentContainer tree1BaseComponentContainer = (ComponentContainer) tree1Presentation
				.getWidget();
		ComponentContainer tree2BaseComponentContainer = (ComponentContainer) tree2Presentation
				.getWidget();
		Tree label1 = (Tree) unwrapTree(tree1BaseComponentContainer);
		Tree label2 = (Tree) unwrapTree(tree2BaseComponentContainer);

		// start tests
		//
		assertTrue(label1.isVisible());
		assertTrue(label1.isEnabled());
		assertFalse(label1.isReadOnly());

		assertTrue(label2.isVisible());
		assertTrue(label2.isEnabled());
		assertFalse(label2.isReadOnly());

		yTree1.setVisible(false);
		assertFalse(label1.isVisible());

		yTree1.setEnabled(false);
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
		YTree yTree1 = factory.createTree();
		yLayout.getElements().add(yTree1);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		ITreeEditpart tree1Editpart = DelegatingEditPartManager.getInstance()
				.getEditpart(yTree1);
		IWidgetPresentation<Component> tree1Presentation = tree1Editpart
				.getPresentation();
		ComponentContainer tree1BaseComponentContainer = (ComponentContainer) tree1Presentation
				.getWidget();
		Tree tree1 = (Tree) unwrapTree(tree1BaseComponentContainer);

		// start tests
		//

		Container.Indexed container = (Indexed) tree1.getContainerDataSource();
		assertEquals(0, container.size());

		// add
		container.addItem("Blabla");
		assertEquals(1, yTree1.getCollection().size());
		assertEquals(1, container.size());

		yTree1.getCollection().add("Huhu");
		assertEquals(2, yTree1.getCollection().size());
		assertEquals(2, container.size());

		// add at index
		yTree1.getCollection().add(0, "First");
		assertEquals("First", yTree1.getCollection().get(0));
		assertEquals("First", container.getItemIds(0, 1).get(0));

		container.addItemAt(0, "Another First");
		assertEquals("Another First", yTree1.getCollection().get(0));
		assertEquals("Another First", container.getItemIds(0, 1).get(0));

		// move
		yTree1.getCollection().move(1, 0);
		assertEquals("First", yTree1.getCollection().get(0));
		assertEquals("First", container.getItemIds(0, 1).get(0));

		// remove all
		container.removeAllItems();
		assertEquals(0, yTree1.getCollection().size());
		assertEquals(0, container.size());

	}

	/**
	 * Test the internal structure based on CSS.
	 * 
	 * @throws Exception
	 */
	@Test
	// BEGIN SUPRESS CATCH EXCEPTION
	public void test_CollectionBinding_TreeToTree() throws Exception {
		// END SUPRESS CATCH EXCEPTION
		// build the view model
		// ...> yView
		// ......> yText
		YView yView = factory.createView();
		YGridLayout yLayout = factory.createGridLayout();
		yView.setContent(yLayout);
		YTree yTree1 = factory.createTree();
		yLayout.getElements().add(yTree1);
		YTree yTree2 = factory.createTree();
		yLayout.getElements().add(yTree2);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		ITreeEditpart tree1Editpart = DelegatingEditPartManager.getInstance()
				.getEditpart(yTree1);
		ITreeEditpart tree2Editpart = DelegatingEditPartManager.getInstance()
				.getEditpart(yTree2);
		IWidgetPresentation<Component> tree1Presentation = tree1Editpart
				.getPresentation();
		IWidgetPresentation<Component> tree2Presentation = tree2Editpart
				.getPresentation();
		ComponentContainer tree1BaseComponentContainer = (ComponentContainer) tree1Presentation
				.getWidget();
		ComponentContainer tree2BaseComponentContainer = (ComponentContainer) tree2Presentation
				.getWidget();
		Tree tree1 = (Tree) unwrapTree(tree1BaseComponentContainer);
		Tree tree2 = (Tree) unwrapTree(tree2BaseComponentContainer);

		Container.Indexed indexedDs1 = (Indexed) tree1.getContainerDataSource();
		Container.Indexed indexedDs2 = (Indexed) tree2.getContainerDataSource();

		YBindingSet yBindingSet = yView.getOrCreateBindingSet();

		// start tests
		//

		YEmbeddableCollectionEndpoint endp1 = yTree1.createCollectionEndpoint();
		YEmbeddableCollectionEndpoint endp2 = yTree2.createCollectionEndpoint();
		yBindingSet.addBinding(endp1, endp2);

		assertEquals(0, yTree1.getCollection().size());
		assertEquals(0, yTree2.getCollection().size());
		assertEquals(0, indexedDs1.size());
		assertEquals(0, indexedDs2.size());

		// add to yTree1
		yTree1.getCollection().add("Huhu");
		assertEquals(1, yTree2.getCollection().size());
		assertEquals("Huhu", yTree2.getCollection().get(0));
		assertEquals("Huhu", indexedDs1.getItemIds(0, 1).get(0));
		assertEquals("Huhu", indexedDs2.getItemIds(0, 1).get(0));
		assertEquals(1, indexedDs1.size());
		assertEquals(1, indexedDs2.size());

		// add to yTree1
		yTree1.getCollection().add("Huhu2");
		assertEquals(2, yTree2.getCollection().size());
		assertEquals("Huhu", yTree2.getCollection().get(0));
		assertEquals("Huhu2", yTree2.getCollection().get(1));
		assertEquals("Huhu", yTree1.getCollection().get(0));
		assertEquals("Huhu2", yTree1.getCollection().get(1));
		assertEquals("Huhu", indexedDs1.getItemIds(0, 1).get(0));
		assertEquals("Huhu2", indexedDs1.getItemIds(1, 1).get(0));
		assertEquals("Huhu", indexedDs2.getItemIds(0, 1).get(0));
		assertEquals("Huhu2", indexedDs2.getItemIds(1, 1).get(0));
		assertEquals(2, indexedDs1.size());
		assertEquals(2, indexedDs2.size());

		// remove from yTree2
		yTree2.getCollection().remove("Huhu");
		assertEquals(1, yTree1.getCollection().size());
		assertEquals(1, yTree2.getCollection().size());
		assertEquals(1, indexedDs1.size());
		assertEquals(1, indexedDs2.size());

		// add another to yTree2
		yTree2.getCollection().add("Blabla");
		assertEquals(2, yTree1.getCollection().size());
		assertEquals(2, yTree2.getCollection().size());
		assertEquals("Huhu2", yTree1.getCollection().get(0));
		assertEquals("Blabla", yTree1.getCollection().get(1));
		assertEquals("Huhu2", yTree2.getCollection().get(0));
		assertEquals("Blabla", yTree2.getCollection().get(1));
		assertEquals("Huhu2", indexedDs1.getItemIds(0, 1).get(0));
		assertEquals("Blabla", indexedDs1.getItemIds(1, 1).get(0));
		assertEquals("Huhu2", indexedDs2.getItemIds(0, 1).get(0));
		assertEquals("Blabla", indexedDs2.getItemIds(1, 1).get(0));
		assertEquals(2, indexedDs1.size());
		assertEquals(2, indexedDs2.size());

		yTree2.getCollection().move(0, 1);
		assertEquals(2, yTree1.getCollection().size());
		assertEquals(2, yTree2.getCollection().size());
		assertEquals("Blabla", yTree1.getCollection().get(0));
		assertEquals("Huhu2", yTree1.getCollection().get(1));
		assertEquals("Blabla", yTree2.getCollection().get(0));
		assertEquals("Huhu2", yTree2.getCollection().get(1));
		assertEquals("Blabla", indexedDs1.getItemIds(0, 1).get(0));
		assertEquals("Huhu2", indexedDs1.getItemIds(1, 1).get(0));
		assertEquals("Blabla", indexedDs2.getItemIds(0, 1).get(0));
		assertEquals("Huhu2", indexedDs2.getItemIds(1, 1).get(0));
		assertEquals(2, indexedDs1.size());
		assertEquals(2, indexedDs2.size());

		yTree2.getCollection().clear();
		assertEquals(0, yTree1.getCollection().size());
		assertEquals(0, yTree2.getCollection().size());
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
		YTree yTree1 = factory.createTree();
		yLayout.getElements().add(yTree1);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		ITreeEditpart tree1Editpart = DelegatingEditPartManager.getInstance()
				.getEditpart(yTree1);
		IWidgetPresentation<Component> tree1Presentation = tree1Editpart
				.getPresentation();
		ComponentContainer tree1BaseComponentContainer = (ComponentContainer) tree1Presentation
				.getWidget();
		Tree tree1 = (Tree) unwrapTree(tree1BaseComponentContainer);

		// start tests
		//
		Container.Indexed container = (Indexed) tree1.getContainerDataSource();
		assertEquals(0, container.size());

		assertNull(tree1.getValue());
		assertNull(yTree1.getSelection());

		// add
		yTree1.getCollection().add("Huhu");
		yTree1.getCollection().add("Haha");
		assertEquals(2, container.size());

		assertNull(yTree1.getSelection());
		assertNull(tree1.getValue());

		// test set selection
		yTree1.setSelection("Huhu");
		assertEquals("Huhu", yTree1.getSelection());
		assertEquals("Huhu", tree1.getValue());

		tree1.setValue("Haha");
		assertEquals("Haha", yTree1.getSelection());
		assertEquals("Haha", tree1.getValue());

		// test set selection null
		tree1.setValue(null);
		assertNull(yTree1.getSelection());
		assertNull(tree1.getValue());

		tree1.setValue("Haha");
		assertEquals("Haha", yTree1.getSelection());
		assertEquals("Haha", tree1.getValue());

		yTree1.setSelection(null);
		assertNull(yTree1.getSelection());
		assertNull(tree1.getValue());

		// test remove element that is selected
		// add
		yTree1.getCollection().add("Huhu");
		yTree1.getCollection().add("Haha");
		assertEquals(2, container.size());

		yTree1.setSelection("Huhu");
		assertEquals("Huhu", yTree1.getSelection());
		assertEquals("Huhu", tree1.getValue());

		yTree1.getCollection().remove("Huhu");
		assertNull(tree1.getValue());
		assertNull(yTree1.getSelection());

		// test remove element that is selected
		// add
		yTree1.getCollection().add("Huhu");
		assertEquals(2, container.size());

		yTree1.setSelection("Huhu");
		assertEquals("Huhu", yTree1.getSelection());
		assertEquals("Huhu", tree1.getValue());

		tree1.setValue(null);
		assertNull(yTree1.getSelection());
		assertNull(tree1.getValue());

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
		YTree yTree1 = factory.createTree();
		yLayout.getElements().add(yTree1);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		ITreeEditpart tree1Editpart = DelegatingEditPartManager.getInstance()
				.getEditpart(yTree1);
		IWidgetPresentation<Component> tree1Presentation = tree1Editpart
				.getPresentation();
		ComponentContainer tree1BaseComponentContainer = (ComponentContainer) tree1Presentation
				.getWidget();
		Tree tree1 = (Tree) unwrapTree(tree1BaseComponentContainer);

		// start tests
		//
		Container.Indexed container = (Indexed) tree1.getContainerDataSource();
		assertEquals(0, container.size());

		assertNull(tree1.getValue());
		assertNull(yTree1.getSelection());

		// add
		assertNull(yTree1.getSelection());
		assertNull(tree1.getValue());

		// test set selection
		yTree1.setSelection("Huhu");
		assertNull(yTree1.getSelection());
		assertNull(tree1.getValue());

	}

	/**
	 * Test the internal structure based on CSS.
	 * 
	 * @throws Exception
	 */
	@Test
	// BEGIN SUPRESS CATCH EXCEPTION
	public void test_SelectionBinding_TreeToTree() throws Exception {
		// END SUPRESS CATCH EXCEPTION
		// build the view model
		// ...> yView
		// ......> yText
		YView yView = factory.createView();
		YGridLayout yLayout = factory.createGridLayout();
		yView.setContent(yLayout);
		YTree yTree1 = factory.createTree();
		yLayout.getElements().add(yTree1);
		YTree yTree2 = factory.createTree();
		yLayout.getElements().add(yTree2);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		ITreeEditpart tree1Editpart = DelegatingEditPartManager.getInstance()
				.getEditpart(yTree1);
		ITreeEditpart tree2Editpart = DelegatingEditPartManager.getInstance()
				.getEditpart(yTree2);
		IWidgetPresentation<Component> tree1Presentation = tree1Editpart
				.getPresentation();
		IWidgetPresentation<Component> tree2Presentation = tree2Editpart
				.getPresentation();
		ComponentContainer tree1BaseComponentContainer = (ComponentContainer) tree1Presentation
				.getWidget();
		ComponentContainer tree2BaseComponentContainer = (ComponentContainer) tree2Presentation
				.getWidget();
		Tree tree1 = (Tree) unwrapTree(tree1BaseComponentContainer);
		Tree tree2 = (Tree) unwrapTree(tree2BaseComponentContainer);

		Container.Indexed indexedDs1 = (Indexed) tree1.getContainerDataSource();
		Container.Indexed indexedDs2 = (Indexed) tree2.getContainerDataSource();

		YBindingSet yBindingSet = yView.getOrCreateBindingSet();

		// start tests
		//

		YEmbeddableCollectionEndpoint endp1 = yTree1.createCollectionEndpoint();
		YEmbeddableCollectionEndpoint endp2 = yTree2.createCollectionEndpoint();
		yBindingSet.addBinding(endp1, endp2);

		YEmbeddableSelectionEndpoint endpSel1 = yTree1
				.createSelectionEndpoint();
		YEmbeddableSelectionEndpoint endpSel2 = yTree2
				.createSelectionEndpoint();
		yBindingSet.addBinding(endpSel1, endpSel2);

		Container.Indexed container1 = (Indexed) tree1.getContainerDataSource();
		Container.Indexed container2 = (Indexed) tree2.getContainerDataSource();
		assertEquals(0, container1.size());
		assertEquals(0, container2.size());

		assertNull(tree1.getValue());
		assertNull(yTree1.getSelection());

		// add
		yTree1.getCollection().add("Huhu");
		yTree2.getCollection().add("Haha");
		assertEquals(2, container1.size());
		assertEquals(2, container2.size());

		assertNull(yTree1.getSelection());
		assertNull(tree1.getValue());
		assertNull(yTree2.getSelection());
		assertNull(tree2.getValue());

		// test set selection
		yTree1.setSelection("Huhu");
		assertEquals("Huhu", yTree1.getSelection());
		assertEquals("Huhu", tree1.getValue());
		assertEquals("Huhu", yTree2.getSelection());
		assertEquals("Huhu", tree2.getValue());

		tree1.setValue("Haha");
		assertEquals("Haha", yTree1.getSelection());
		assertEquals("Haha", tree1.getValue());
		assertEquals("Haha", yTree2.getSelection());
		assertEquals("Haha", tree2.getValue());

		// test set selection null
		tree1.setValue(null);
		assertNull(yTree1.getSelection());
		assertNull(tree1.getValue());
		assertNull(yTree2.getSelection());
		assertNull(tree2.getValue());

		tree1.setValue("Haha");
		assertEquals("Haha", yTree1.getSelection());
		assertEquals("Haha", tree1.getValue());
		assertEquals("Haha", yTree2.getSelection());
		assertEquals("Haha", tree2.getValue());

		tree2.setValue(null);
		assertNull(yTree1.getSelection());
		assertNull(tree1.getValue());
		assertNull(yTree2.getSelection());
		assertNull(tree2.getValue());

		tree2.setValue("Haha");
		assertEquals("Haha", yTree1.getSelection());
		assertEquals("Haha", tree1.getValue());
		assertEquals("Haha", yTree2.getSelection());
		assertEquals("Haha", tree2.getValue());

		yTree1.setSelection(null);
		assertNull(yTree1.getSelection());
		assertNull(tree1.getValue());
		assertNull(yTree2.getSelection());
		assertNull(tree2.getValue());

		tree2.setValue("Haha");
		assertEquals("Haha", yTree1.getSelection());
		assertEquals("Haha", tree1.getValue());
		assertEquals("Haha", yTree2.getSelection());
		assertEquals("Haha", tree2.getValue());

		yTree2.setSelection(null);
		assertNull(yTree1.getSelection());
		assertNull(tree1.getValue());
		assertNull(yTree2.getSelection());
		assertNull(tree2.getValue());

		// test remove element that is selected
		// add
		assertEquals(2, container1.size());
		assertEquals(2, container2.size());

		yTree1.setSelection("Huhu");
		assertEquals("Huhu", yTree1.getSelection());
		assertEquals("Huhu", yTree2.getSelection());
		assertEquals("Huhu", tree1.getValue());
		assertEquals("Huhu", tree2.getValue());

		yTree1.getCollection().remove("Huhu");
		assertNull(tree1.getValue());
		assertNull(tree2.getValue());
		assertNull(yTree1.getSelection());
		assertNull(yTree2.getSelection());

		// test remove element that is selected
		// add
		yTree2.getCollection().add("Huhu");
		assertEquals(2, container1.size());
		assertEquals(2, container2.size());

		yTree1.setSelection("Huhu");
		assertEquals("Huhu", yTree1.getSelection());
		assertEquals("Huhu", tree1.getValue());
		assertEquals("Huhu", yTree2.getSelection());
		assertEquals("Huhu", tree2.getValue());

		tree2.setValue(null);
		assertNull(yTree1.getSelection());
		assertNull(tree1.getValue());
		assertNull(yTree2.getSelection());
		assertNull(tree2.getValue());
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
		YTree yTree = factory.createTree();
		yGridlayout.getElements().add(yTree);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		ITreeEditpart treeEditpart = DelegatingEditPartManager.getInstance()
				.getEditpart(yTree);
		IWidgetPresentation<Component> presentation = treeEditpart
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
		YTree yTree1 = factory.createTree();
		yTree1.setSelectionType(YSelectionType.MULTI);
		yLayout.getElements().add(yTree1);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		ITreeEditpart tree1Editpart = DelegatingEditPartManager.getInstance()
				.getEditpart(yTree1);
		IWidgetPresentation<Component> tree1Presentation = tree1Editpart
				.getPresentation();
		ComponentContainer tree1BaseComponentContainer = (ComponentContainer) tree1Presentation
				.getWidget();
		Tree tree1 = (Tree) unwrapTree(tree1BaseComponentContainer);

		// start tests
		//
		Container.Indexed container = (Indexed) tree1.getContainerDataSource();
		assertEquals(0, container.size());

		assertTrue(asList(tree1.getValue()).isEmpty());
		assertTrue(yTree1.getMultiSelection().isEmpty());

		// test set selection by model
		yTree1.getMultiSelection().add("Huhu");
		assertEquals("Huhu", yTree1.getMultiSelection().get(0));
		assertEquals("Huhu", asList(tree1.getValue()).get(0));
		assertEquals(1, yTree1.getMultiSelection().size());
		assertEquals(1, asList(tree1.getValue()).size());

		yTree1.getMultiSelection().add("Haha");
		assertEquals("Huhu", yTree1.getMultiSelection().get(0));
		assertEquals("Haha", yTree1.getMultiSelection().get(1));
		assertEquals("Huhu", asList(tree1.getValue()).get(0));
		assertEquals("Haha", asList(tree1.getValue()).get(1));
		assertEquals(2, yTree1.getMultiSelection().size());
		assertEquals(2, asList(tree1.getValue()).size());

		// remove selection
		yTree1.getMultiSelection().remove("Huhu");
		assertEquals("Haha", yTree1.getMultiSelection().get(0));
		assertEquals("Haha", asList(tree1.getValue()).get(0));
		assertEquals(1, yTree1.getMultiSelection().size());
		assertEquals(1, asList(tree1.getValue()).size());

		yTree1.getMultiSelection().clear();
		assertEquals(0, yTree1.getMultiSelection().size());
		assertEquals(0, asList(tree1.getValue()).size());

		// test set selection by widget
		List<String> selection = new ArrayList<String>();
		selection.add("Huhu");
		tree1.setValue(selection);
		assertEquals("Huhu", yTree1.getMultiSelection().get(0));
		assertEquals("Huhu", asList(tree1.getValue()).get(0));
		assertEquals(1, yTree1.getMultiSelection().size());
		assertEquals(1, asList(tree1.getValue()).size());

		selection = new ArrayList<String>();
		selection.add("Huhu");
		selection.add("Haha");
		tree1.setValue(selection);
		assertEquals("Huhu", yTree1.getMultiSelection().get(0));
		assertEquals("Haha", yTree1.getMultiSelection().get(1));
		assertEquals("Huhu", asList(tree1.getValue()).get(0));
		assertEquals("Haha", asList(tree1.getValue()).get(1));
		assertEquals(2, yTree1.getMultiSelection().size());
		assertEquals(2, asList(tree1.getValue()).size());

		selection = new ArrayList<String>();
		selection.add("Haha");
		tree1.setValue(selection);
		assertEquals("Haha", yTree1.getMultiSelection().get(0));
		assertEquals("Haha", asList(tree1.getValue()).get(0));
		assertEquals(1, yTree1.getMultiSelection().size());
		assertEquals(1, asList(tree1.getValue()).size());

		selection = new ArrayList<String>();
		tree1.setValue(selection);
		assertEquals(0, yTree1.getMultiSelection().size());
		assertEquals(0, asList(tree1.getValue()).size());
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
		YTree yTree1 = factory.createTree();
		yTree1.setType(Bar.class);
		yLayout.getElements().add(yTree1);
		YTextField yText = factory.createTextField();
		yLayout.getElements().add(yText);

		YBindingSet yBindingSet = yView.getOrCreateBindingSet();
		YEmbeddableSelectionEndpoint selectionBindingEndpoint = yTree1
				.createSelectionEndpoint();
		selectionBindingEndpoint.setAttributePath("myfoo.name");
		yBindingSet.addBinding(yText.createValueEndpoint(),
				selectionBindingEndpoint);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		ITreeEditpart tree1Editpart = DelegatingEditPartManager.getInstance()
				.getEditpart(yTree1);
		IWidgetPresentation<Component> tree1Presentation = tree1Editpart
				.getPresentation();
		ComponentContainer tree1BaseComponentContainer = (ComponentContainer) tree1Presentation
				.getWidget();
		Tree tree1 = (Tree) unwrapTree(tree1BaseComponentContainer);

		ITextFieldEditpart textEditpart = DelegatingEditPartManager
				.getInstance().getEditpart(yText);
		IWidgetPresentation<Component> textPresentation = textEditpart
				.getPresentation();
		ComponentContainer textBaseComponentContainer = (ComponentContainer) textPresentation
				.getWidget();
		TextField text = (TextField) unwrapTree(textBaseComponentContainer);

		// start tests
		//
		Container.Indexed container = (Indexed) tree1.getContainerDataSource();
		assertEquals(0, container.size());

		assertNull(tree1.getValue());
		assertNull(yTree1.getSelection());
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

		yTree1.getCollection().add(bar1);
		yTree1.getCollection().add(bar2);
		assertEquals(2, container.size());

		assertNull(yTree1.getSelection());
		assertNull(tree1.getValue());
		assertNull(yText.getValue());
		assertNull(text.getValue());

		// test set selection
		yTree1.setSelection(bar1);
		assertEquals("Foo1", yText.getValue());
		assertEquals("Foo1", text.getValue());

		yTree1.setSelection(bar2);
		assertEquals("Foo2", yText.getValue());
		assertEquals("Foo2", text.getValue());

		// test set selection null
		yTree1.setSelection(null);
		assertNull(yText.getValue());
		assertNull(text.getValue());

		tree1.setValue(bar1);
		assertEquals("Foo1", yText.getValue());
		assertEquals("Foo1", text.getValue());

		tree1.setValue(bar2);
		assertEquals("Foo2", yText.getValue());
		assertEquals("Foo2", text.getValue());

		// test set selection null
		tree1.setValue(null);
		assertNull(yText.getValue());
		assertNull(text.getValue());

		// test remove element that is selected
		// add
		tree1.setValue(bar2);
		assertEquals("Foo2", yText.getValue());
		assertEquals("Foo2", text.getValue());

		yTree1.getCollection().clear();

		assertNull(yTree1.getSelection());
		assertNull(tree1.getValue());

		// test setValue to textfield
		yTree1.getCollection().add(bar1);
		yTree1.getCollection().add(bar2);
		assertEquals(2, container.size());

		yTree1.setSelection(bar2);
		assertEquals("Foo2", yText.getValue());
		assertEquals("Foo2", text.getValue());

		yText.setValue("Foo2_1");
		assertEquals("Foo2_1", foo2.getName());
		assertEquals("Foo2_1", text.getValue());

		text.setValue("Foo2_2");
		assertEquals("Foo2_2", foo2.getName());
		assertEquals("Foo2_2", yText.getValue());

		yTree1.setSelection(bar1);
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
	public void test_SelectionBinding_Multi_ListToList() throws Exception {
		// END SUPRESS CATCH EXCEPTION
		// build the view model
		// ...> yView
		// ......> yText
		YView yView = factory.createView();
		YGridLayout yLayout = factory.createGridLayout();
		yView.setContent(yLayout);
		YTree yTree1 = factory.createTree();
		yTree1.setSelectionType(YSelectionType.MULTI);
		yLayout.getElements().add(yTree1);
		YTree yTree2 = factory.createTree();
		yTree2.setSelectionType(YSelectionType.MULTI);
		yLayout.getElements().add(yTree2);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		ITreeEditpart tree1Editpart = DelegatingEditPartManager.getInstance()
				.getEditpart(yTree1);
		ITreeEditpart tree2Editpart = DelegatingEditPartManager.getInstance()
				.getEditpart(yTree2);
		IWidgetPresentation<Component> tree1Presentation = tree1Editpart
				.getPresentation();
		IWidgetPresentation<Component> tree2Presentation = tree2Editpart
				.getPresentation();
		ComponentContainer tree1BaseComponentContainer = (ComponentContainer) tree1Presentation
				.getWidget();
		ComponentContainer tree2BaseComponentContainer = (ComponentContainer) tree2Presentation
				.getWidget();
		Tree tree1 = (Tree) unwrapTree(tree1BaseComponentContainer);
		Tree tree2 = (Tree) unwrapTree(tree2BaseComponentContainer);

		Container.Indexed indexedDs1 = (Indexed) tree1.getContainerDataSource();
		Container.Indexed indexedDs2 = (Indexed) tree2.getContainerDataSource();

		YBindingSet yBindingSet = yView.getOrCreateBindingSet();

		// start tests
		//

		YEmbeddableCollectionEndpoint endp1 = yTree1.createCollectionEndpoint();
		YEmbeddableCollectionEndpoint endp2 = yTree2.createCollectionEndpoint();
		yBindingSet.addBinding(endp1, endp2);

		YEmbeddableMultiSelectionEndpoint endpSel1 = yTree1
				.createMultiSelectionEndpoint();
		YEmbeddableMultiSelectionEndpoint endpSel2 = yTree2
				.createMultiSelectionEndpoint();
		yBindingSet.addBinding(endpSel1, endpSel2);

		Container.Indexed container1 = (Indexed) tree1.getContainerDataSource();
		Container.Indexed container2 = (Indexed) tree2.getContainerDataSource();
		assertEquals(0, container1.size());
		assertEquals(0, container2.size());

		assertTrue(asList(tree1.getValue()).isEmpty());
		assertTrue(yTree1.getMultiSelection().isEmpty());
		assertTrue(asList(tree2.getValue()).isEmpty());
		assertTrue(yTree2.getMultiSelection().isEmpty());

		// add
		yTree1.getCollection().add("Huhu");
		yTree2.getCollection().add("Haha");
		assertEquals(2, container1.size());
		assertEquals(2, container2.size());

		assertTrue(asList(tree1.getValue()).isEmpty());
		assertTrue(yTree1.getMultiSelection().isEmpty());
		assertTrue(asList(tree2.getValue()).isEmpty());
		assertTrue(yTree2.getMultiSelection().isEmpty());

		// test set selection
		yTree1.getMultiSelection().add("Huhu");
		assertEquals("Huhu", yTree1.getMultiSelection().get(0));
		assertEquals("Huhu", asList(tree1.getValue()).get(0));
		assertEquals(1, yTree1.getMultiSelection().size());
		assertEquals(1, asList(tree1.getValue()).size());
		assertEquals("Huhu", yTree2.getMultiSelection().get(0));
		assertEquals("Huhu", asList(tree2.getValue()).get(0));
		assertEquals(1, yTree2.getMultiSelection().size());
		assertEquals(1, asList(tree2.getValue()).size());

		yTree2.getMultiSelection().add("Haha");
		assertEquals("Huhu", yTree1.getMultiSelection().get(0));
		assertEquals("Huhu", asList(tree1.getValue()).get(0));
		assertEquals("Haha", yTree1.getMultiSelection().get(1));
		assertEquals("Haha", asList(tree1.getValue()).get(1));
		assertEquals(2, yTree1.getMultiSelection().size());
		assertEquals(2, asList(tree1.getValue()).size());
		assertEquals("Huhu", yTree2.getMultiSelection().get(0));
		assertEquals("Huhu", asList(tree2.getValue()).get(0));
		assertEquals("Haha", yTree2.getMultiSelection().get(1));
		assertEquals("Haha", asList(tree2.getValue()).get(1));
		assertEquals(2, yTree2.getMultiSelection().size());
		assertEquals(2, asList(tree2.getValue()).size());

		yTree1.getMultiSelection().remove("Huhu");
		assertEquals("Haha", yTree1.getMultiSelection().get(0));
		assertEquals("Haha", asList(tree1.getValue()).get(0));
		assertEquals(1, yTree1.getMultiSelection().size());
		assertEquals(1, asList(tree1.getValue()).size());
		assertEquals("Haha", yTree2.getMultiSelection().get(0));
		assertEquals("Haha", asList(tree2.getValue()).get(0));
		assertEquals(1, yTree2.getMultiSelection().size());
		assertEquals(1, asList(tree2.getValue()).size());

		// clear
		yTree2.getMultiSelection().clear();
		assertTrue(asList(tree1.getValue()).isEmpty());
		assertTrue(yTree1.getMultiSelection().isEmpty());
		assertTrue(asList(tree2.getValue()).isEmpty());
		assertTrue(yTree2.getMultiSelection().isEmpty());

		// test set selection null
		List<String> selection = new ArrayList<String>();
		selection.add("Huhu");
		tree1.setValue(selection);
		assertEquals("Huhu", yTree1.getMultiSelection().get(0));
		assertEquals("Huhu", asList(tree1.getValue()).get(0));
		assertEquals(1, yTree1.getMultiSelection().size());
		assertEquals(1, asList(tree1.getValue()).size());
		assertEquals("Huhu", yTree2.getMultiSelection().get(0));
		assertEquals("Huhu", asList(tree2.getValue()).get(0));
		assertEquals(1, yTree2.getMultiSelection().size());
		assertEquals(1, asList(tree2.getValue()).size());

		selection.add("Haha");
		tree2.setValue(selection);
		assertEquals("Huhu", yTree1.getMultiSelection().get(0));
		assertEquals("Huhu", asList(tree1.getValue()).get(0));
		assertEquals("Haha", yTree1.getMultiSelection().get(1));
		assertEquals("Haha", asList(tree1.getValue()).get(1));
		assertEquals(2, yTree1.getMultiSelection().size());
		assertEquals(2, asList(tree1.getValue()).size());
		assertEquals("Huhu", yTree2.getMultiSelection().get(0));
		assertEquals("Huhu", asList(tree2.getValue()).get(0));
		assertEquals("Haha", yTree2.getMultiSelection().get(1));
		assertEquals("Haha", asList(tree2.getValue()).get(1));
		assertEquals(2, yTree2.getMultiSelection().size());
		assertEquals(2, asList(tree2.getValue()).size());

		tree2.setValue(new ArrayList<String>());
		assertTrue(asList(tree2.getValue()).isEmpty());
		assertTrue(yTree2.getMultiSelection().isEmpty());
		assertTrue(asList(tree1.getValue()).isEmpty());
		assertTrue(yTree1.getMultiSelection().isEmpty());
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
		YTree yTree1 = factory.createTree();
		yLayout.getElements().add(yTree1);
		YTree yTree2 = factory.createTree();
		yLayout.getElements().add(yTree2);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		ITreeEditpart tree1Editpart = DelegatingEditPartManager.getInstance()
				.getEditpart(yTree1);
		ITreeEditpart tree2Editpart = DelegatingEditPartManager.getInstance()
				.getEditpart(yTree2);
		IWidgetPresentation<Component> tree1Presentation = tree1Editpart
				.getPresentation();
		IWidgetPresentation<Component> tree2Presentation = tree2Editpart
				.getPresentation();
		ComponentContainer tree1BaseComponentContainer = (ComponentContainer) tree1Presentation
				.getWidget();
		ComponentContainer tree2BaseComponentContainer = (ComponentContainer) tree2Presentation
				.getWidget();
		Tree tree1 = (Tree) unwrapTree(tree1BaseComponentContainer);
		Tree tree2 = (Tree) unwrapTree(tree2BaseComponentContainer);

		Container.Indexed indexedDs1 = (Indexed) tree1.getContainerDataSource();
		Container.Indexed indexedDs2 = (Indexed) tree2.getContainerDataSource();

		YBindingSet yBindingSet = yView.getOrCreateBindingSet();

		// start tests
		//

		YEmbeddableCollectionEndpoint endp1 = yTree1.createCollectionEndpoint();
		YEmbeddableCollectionEndpoint endp2 = yTree2.createCollectionEndpoint();
		yBindingSet.addBinding(endp1, endp2);

		YEmbeddableSelectionEndpoint endpSel1 = yTree1
				.createSelectionEndpoint();
		YEmbeddableSelectionEndpoint endpSel2 = yTree2
				.createSelectionEndpoint();
		yBindingSet.addBinding(endpSel1, endpSel2);

		Container.Indexed container1 = (Indexed) tree1.getContainerDataSource();
		Container.Indexed container2 = (Indexed) tree2.getContainerDataSource();
		assertEquals(0, container1.size());
		assertEquals(0, container2.size());

		assertNull(tree1.getValue());
		assertNull(yTree1.getSelection());

		// add
		yTree1.getCollection().add("Huhu");
		yTree2.getCollection().add("Haha");
		assertEquals(2, container1.size());
		assertEquals(2, container2.size());

		assertNull(yTree1.getSelection());
		assertNull(tree1.getValue());
		assertNull(yTree2.getSelection());
		assertNull(tree2.getValue());

		// test set selection
		yTree1.setSelection("Huhu");
		assertEquals("Huhu", yTree1.getSelection());
		assertEquals("Huhu", tree1.getValue());
		assertEquals("Huhu", yTree2.getSelection());
		assertEquals("Huhu", tree2.getValue());

		tree1.setValue("Haha");
		assertEquals("Haha", yTree1.getSelection());
		assertEquals("Haha", tree1.getValue());
		assertEquals("Haha", yTree2.getSelection());
		assertEquals("Haha", tree2.getValue());

		// test set selection null
		tree1.setValue(null);
		assertNull(yTree1.getSelection());
		assertNull(tree1.getValue());
		assertNull(yTree2.getSelection());
		assertNull(tree2.getValue());

		tree1.setValue("Haha");
		assertEquals("Haha", yTree1.getSelection());
		assertEquals("Haha", tree1.getValue());
		assertEquals("Haha", yTree2.getSelection());
		assertEquals("Haha", tree2.getValue());

		tree2.setValue(null);
		assertNull(yTree1.getSelection());
		assertNull(tree1.getValue());
		assertNull(yTree2.getSelection());
		assertNull(tree2.getValue());

		tree2.setValue("Haha");
		assertEquals("Haha", yTree1.getSelection());
		assertEquals("Haha", tree1.getValue());
		assertEquals("Haha", yTree2.getSelection());
		assertEquals("Haha", tree2.getValue());

		yTree1.setSelection(null);
		assertNull(yTree1.getSelection());
		assertNull(tree1.getValue());
		assertNull(yTree2.getSelection());
		assertNull(tree2.getValue());

		tree2.setValue("Haha");
		assertEquals("Haha", yTree1.getSelection());
		assertEquals("Haha", tree1.getValue());
		assertEquals("Haha", yTree2.getSelection());
		assertEquals("Haha", tree2.getValue());

		yTree2.setSelection(null);
		assertNull(yTree1.getSelection());
		assertNull(tree1.getValue());
		assertNull(yTree2.getSelection());
		assertNull(tree2.getValue());

		// test remove element that is selected
		// add
		assertEquals(2, container1.size());
		assertEquals(2, container2.size());

		yTree1.setSelection("Huhu");
		assertEquals("Huhu", yTree1.getSelection());
		assertEquals("Huhu", yTree2.getSelection());
		assertEquals("Huhu", tree1.getValue());
		assertEquals("Huhu", tree2.getValue());

		yTree1.getCollection().remove("Huhu");
		assertNull(tree1.getValue());
		assertNull(tree2.getValue());
		assertNull(yTree1.getSelection());
		assertNull(yTree2.getSelection());

		// test remove element that is selected
		// add
		yTree2.getCollection().add("Huhu");
		assertEquals(2, container1.size());
		assertEquals(2, container2.size());

		yTree1.setSelection("Huhu");
		assertEquals("Huhu", yTree1.getSelection());
		assertEquals("Huhu", tree1.getValue());
		assertEquals("Huhu", yTree2.getSelection());
		assertEquals("Huhu", tree2.getValue());

		tree2.setValue(null);
		assertNull(yTree1.getSelection());
		assertNull(tree1.getValue());
		assertNull(yTree2.getSelection());
		assertNull(tree2.getValue());
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
		YTree yTree1 = factory.createTree();
		yTree1.setType(Bar.class);
		yLayout.getElements().add(yTree1);
		YTextField yText = factory.createTextField();
		yLayout.getElements().add(yText);

		YBindingSet yBindingSet = yView.getOrCreateBindingSet();
		YDetailValueBindingEndpoint yDetailEndpoint = yTree1
				.createSelectionEndpoint().createDetailValueEndpoint();
		yDetailEndpoint.setPropertyPath("myfoo.name");
		yDetailEndpoint.setType(Bar.class);
		yBindingSet.addBinding(yText.createValueEndpoint(), yDetailEndpoint);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		ITreeEditpart tree1Editpart = DelegatingEditPartManager.getInstance()
				.getEditpart(yTree1);
		IWidgetPresentation<Component> tree1Presentation = tree1Editpart
				.getPresentation();
		ComponentContainer tree1BaseComponentContainer = (ComponentContainer) tree1Presentation
				.getWidget();
		Tree tree1 = (Tree) unwrapTree(tree1BaseComponentContainer);

		ITextFieldEditpart textEditpart = DelegatingEditPartManager
				.getInstance().getEditpart(yText);
		IWidgetPresentation<Component> textPresentation = textEditpart
				.getPresentation();
		ComponentContainer textBaseComponentContainer = (ComponentContainer) textPresentation
				.getWidget();
		TextField text = (TextField) unwrapTree(textBaseComponentContainer);

		// start tests
		//
		Container.Indexed container = (Indexed) tree1.getContainerDataSource();
		assertEquals(0, container.size());

		assertNull(tree1.getValue());
		assertNull(yTree1.getSelection());
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

		yTree1.getCollection().add(bar1);
		yTree1.getCollection().add(bar2);
		assertEquals(2, container.size());

		assertNull(yTree1.getSelection());
		assertNull(tree1.getValue());
		assertNull(yText.getValue());
		assertNull(text.getValue());

		// test set selection
		yTree1.setSelection(bar1);
		assertEquals("Foo1", yText.getValue());
		assertEquals("Foo1", text.getValue());

		yTree1.setSelection(bar2);
		assertEquals("Foo2", yText.getValue());
		assertEquals("Foo2", text.getValue());

		// test set selection null
		yTree1.setSelection(null);
		assertNull(yText.getValue());
		assertNull(text.getValue());

		tree1.setValue(bar1);
		assertEquals("Foo1", yText.getValue());
		assertEquals("Foo1", text.getValue());

		tree1.setValue(bar2);
		assertEquals("Foo2", yText.getValue());
		assertEquals("Foo2", text.getValue());

		// test set selection null
		tree1.setValue(null);
		assertNull(yText.getValue());
		assertNull(text.getValue());

		// test remove element that is selected
		// add
		tree1.setValue(bar2);
		assertEquals("Foo2", yText.getValue());
		assertEquals("Foo2", text.getValue());

		yTree1.getCollection().clear();

		assertNull(yTree1.getSelection());
		assertNull(tree1.getValue());

		// test setValue to textfield
		yTree1.getCollection().add(bar1);
		yTree1.getCollection().add(bar2);
		assertEquals(2, container.size());

		yTree1.setSelection(bar2);
		assertEquals("Foo2", yText.getValue());
		assertEquals("Foo2", text.getValue());

		yText.setValue("Foo2_1");
		assertEquals("Foo2_1", foo2.getName());
		assertEquals("Foo2_1", text.getValue());

		text.setValue("Foo2_2");
		assertEquals("Foo2_2", foo2.getName());
		assertEquals("Foo2_2", yText.getValue());

		yTree1.setSelection(bar1);
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
		YTree yTree1 = factory.createTree();
		yTree1.setType(EmfBar.class);
		yTree1.setEmfNsURI(ModelPackage.eINSTANCE.getNsURI());
		yLayout.getElements().add(yTree1);
		YTextField yText = factory.createTextField();
		yLayout.getElements().add(yText);

		YBindingSet yBindingSet = yView.getOrCreateBindingSet();
		YDetailValueBindingEndpoint yDetailEndpoint = yTree1
				.createSelectionEndpoint().createDetailValueEndpoint();
		yDetailEndpoint.setType(EmfBar.class);
		yDetailEndpoint.getFeatures().add(
				ModelPackage.eINSTANCE.getEmfBar_Myfoo());
		yDetailEndpoint.getFeatures().add(
				ModelPackage.eINSTANCE.getEmfFoo_Name());
		yBindingSet.addBinding(yText.createValueEndpoint(), yDetailEndpoint);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		ITreeEditpart tree1Editpart = DelegatingEditPartManager.getInstance()
				.getEditpart(yTree1);
		IWidgetPresentation<Component> tree1Presentation = tree1Editpart
				.getPresentation();
		ComponentContainer tree1BaseComponentContainer = (ComponentContainer) tree1Presentation
				.getWidget();
		Tree tree1 = (Tree) unwrapTree(tree1BaseComponentContainer);

		ITextFieldEditpart textEditpart = DelegatingEditPartManager
				.getInstance().getEditpart(yText);
		IWidgetPresentation<Component> textPresentation = textEditpart
				.getPresentation();
		ComponentContainer textBaseComponentContainer = (ComponentContainer) textPresentation
				.getWidget();
		TextField text = (TextField) unwrapTree(textBaseComponentContainer);

		// start tests
		//
		Container.Indexed container = (Indexed) tree1.getContainerDataSource();
		assertEquals(0, container.size());

		assertNull(tree1.getValue());
		assertNull(yTree1.getSelection());
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

		yTree1.getCollection().add(bar1);
		yTree1.getCollection().add(bar2);
		assertEquals(2, container.size());

		assertNull(yTree1.getSelection());
		assertNull(tree1.getValue());
		assertNull(yText.getValue());
		assertNull(text.getValue());

		// test set selection
		yTree1.setSelection(bar1);
		assertEquals("Foo1", yText.getValue());
		assertEquals("Foo1", text.getValue());

		yTree1.setSelection(bar2);
		assertEquals("Foo2", yText.getValue());
		assertEquals("Foo2", text.getValue());

		// test set selection null
		yTree1.setSelection(null);
		assertNull(yText.getValue());
		assertNull(text.getValue());

		tree1.setValue(bar1);
		assertEquals("Foo1", yText.getValue());
		assertEquals("Foo1", text.getValue());

		tree1.setValue(bar2);
		assertEquals("Foo2", yText.getValue());
		assertEquals("Foo2", text.getValue());

		// test set selection null
		tree1.setValue(null);
		assertNull(yText.getValue());
		assertNull(text.getValue());

		// test remove element that is selected
		// add
		tree1.setValue(bar2);
		assertEquals("Foo2", yText.getValue());
		assertEquals("Foo2", text.getValue());

		yTree1.getCollection().clear();

		assertNull(yTree1.getSelection());
		assertNull(tree1.getValue());

		// test setValue to textfield
		yTree1.getCollection().add(bar1);
		yTree1.getCollection().add(bar2);
		assertEquals(2, container.size());

		yTree1.setSelection(bar2);
		assertEquals("Foo2", yText.getValue());
		assertEquals("Foo2", text.getValue());

		yText.setValue("Foo2_1");
		assertEquals("Foo2_1", foo2.getName());
		assertEquals("Foo2_1", text.getValue());

		text.setValue("Foo2_2");
		assertEquals("Foo2_2", foo2.getName());
		assertEquals("Foo2_2", yText.getValue());

		yTree1.setSelection(bar1);
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
		YTree yTree1 = factory.createTree();
		yGridlayout.getElements().add(yTree1);

		// set the i18n key
		yTree1.setLabelI18nKey(TestI18nService.KEY__AGE);

		// prepare the I18nService and pass it to the renderer
		Map<String, Object> parameter = new HashMap<String, Object>();
		Map<String, Object> services = new HashMap<String, Object>();
		parameter.put(IViewContext.PARAM_SERVICES, services);
		services.put(II18nService.ID, new TestI18nService());

		VaadinRenderer renderer = new VaadinRenderer();
		IViewContext context = renderer.render(rootLayout, yView, parameter);
		ITreeEditpart editpart = DelegatingEditPartManager.getInstance()
				.getEditpart(yTree1);
		TreePresentation presentation = editpart.getPresentation();

		Tree tree = (Tree) unwrapTree(presentation.getWidget());
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
		YTree yTree1 = factory.createTree();
		yLayout.getElements().add(yTree1);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		ITreeEditpart editpart = DelegatingEditPartManager.getInstance()
				.getEditpart(yTree1);
		IWidgetPresentation<Component> presentation = editpart
				.getPresentation();
		ComponentContainer baseComponentContainer = (ComponentContainer) presentation
				.getWidget();
		Tree tree1 = (Tree) unwrapTree(baseComponentContainer);

		ValueBean bean = new ValueBean(false);
		YBeanValueBindingEndpoint yBeanBinding = factory
				.createBeanBindingEndpoint();
		yBeanBinding.setBean(bean);
		yBeanBinding.setPropertyPath("boolValue");
		YBindingSet yBindingSet = yView.getOrCreateBindingSet();
		yBindingSet.addBinding(yTree1.createEditableEndpoint(), yBeanBinding);

		// test binding
		assertFalse(yTree1.isEditable());
		assertFalse(!tree1.isReadOnly());
		assertFalse(bean.isBoolValue());

		bean.setBoolValue(true);
		assertTrue(yTree1.isEditable());
		assertTrue(!tree1.isReadOnly());
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
		YTree yTree1 = factory.createTree();
		yLayout.getElements().add(yTree1);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		ITreeEditpart editpart = DelegatingEditPartManager.getInstance()
				.getEditpart(yTree1);
		IWidgetPresentation<Component> presentation = editpart
				.getPresentation();
		ComponentContainer textBaseComponentContainer = (ComponentContainer) presentation
				.getWidget();
		Tree tree1 = (Tree) unwrapTree(textBaseComponentContainer);

		ValueBean bean = new ValueBean(false);
		YBeanValueBindingEndpoint yBeanBinding = factory
				.createBeanBindingEndpoint();
		yBeanBinding.setBean(bean);
		yBeanBinding.setPropertyPath("boolValue");
		YBindingSet yBindingSet = yView.getOrCreateBindingSet();
		yBindingSet.addBinding(yTree1.createVisibleEndpoint(), yBeanBinding);

		// test binding
		assertFalse(yTree1.isVisible());
		assertFalse(tree1.isVisible());
		assertFalse(bean.isBoolValue());

		bean.setBoolValue(true);
		assertTrue(yTree1.isVisible());
		assertTrue(tree1.isVisible());
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
		YTree yTree1 = factory.createTree();
		yLayout.getElements().add(yTree1);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		ITreeEditpart editpart = DelegatingEditPartManager.getInstance()
				.getEditpart(yTree1);
		IWidgetPresentation<Component> presentation = editpart
				.getPresentation();
		ComponentContainer textBaseComponentContainer = (ComponentContainer) presentation
				.getWidget();
		Tree tree1 = (Tree) unwrapTree(textBaseComponentContainer);

		ValueBean bean = new ValueBean(false);
		YBeanValueBindingEndpoint yBeanBinding = factory
				.createBeanBindingEndpoint();
		yBeanBinding.setBean(bean);
		yBeanBinding.setPropertyPath("boolValue");
		YBindingSet yBindingSet = yView.getOrCreateBindingSet();
		yBindingSet.addBinding(yTree1.createEnabledEndpoint(), yBeanBinding);

		// test binding
		assertFalse(yTree1.isEnabled());
		assertFalse(tree1.isEnabled());
		assertFalse(bean.isBoolValue());

		bean.setBoolValue(true);
		assertTrue(yTree1.isEnabled());
		assertTrue(tree1.isEnabled());
		assertTrue(bean.isBoolValue());
	}

	/**
	 * Unwraps the component from its parent composite.
	 * 
	 * @param component
	 * @return
	 */
	private Component unwrapTree(Component component) {
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
