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

import java.util.Iterator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.eclipse.emf.ecp.ecview.common.editpart.DelegatingEditPartManager;
import org.eclipse.emf.ecp.ecview.common.editpart.IElementEditpart;
import org.eclipse.emf.ecp.ecview.common.editpart.IEmbeddableEditpart;
import org.eclipse.emf.ecp.ecview.common.editpart.IViewEditpart;
import org.eclipse.emf.ecp.ecview.common.model.core.YElement;
import org.eclipse.emf.ecp.ecview.common.model.core.YView;
import org.eclipse.emf.ecp.ecview.common.presentation.IWidgetPresentation;
import org.eclipse.emf.ecp.ecview.extension.model.extension.YGridLayout;
import org.eclipse.emf.ecp.ecview.extension.model.extension.YList;
import org.eclipse.emf.ecp.ecview.extension.model.extension.util.SimpleExtensionModelFactory;
import org.eclipse.emf.ecp.ecview.ui.core.editparts.extension.IListEditpart;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.lunifera.runtime.web.ecview.presentation.vaadin.VaadinRenderer;
import org.lunifera.runtime.web.ecview.presentation.vaadin.internal.AbstractVaadinWidgetPresenter;
import org.lunifera.runtime.web.ecview.presentation.vaadin.internal.ListPresentation;
import org.osgi.framework.BundleException;
import org.osgi.service.cm.ConfigurationException;

import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.UI;

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
		yGridlayout.getElements().add(yList);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		IListEditpart listEditpart = DelegatingEditPartManager
				.getInstance().getEditpart(yList);
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
		yView.setContent(yList);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		IListEditpart listEditpart = DelegatingEditPartManager
				.getInstance().getEditpart(yList);
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
		yList1.setCssID("ID_0815");
		yList1.setCssClass("anyOtherClass");
		yLayout.getElements().add(yList1);
		YList yList2 = factory.createList();
		yLayout.getElements().add(yList2);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		IListEditpart list1Editpart = DelegatingEditPartManager
				.getInstance().getEditpart(yList1);
		IListEditpart list2Editpart = DelegatingEditPartManager
				.getInstance().getEditpart(yList2);
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
				AbstractVaadinWidgetPresenter.CSS_CLASS__CONTROL_BASE));
		assertTrue(list2BaseComponentContainer.getStyleName().contains(
				AbstractVaadinWidgetPresenter.CSS_CLASS__CONTROL_BASE));

		assertTrue(label1.getStyleName().contains("anyOtherClass"));
		assertTrue(label2.getStyleName().contains(
				AbstractVaadinWidgetPresenter.CSS_CLASS__CONTROL));

		// assert css id
		assertEquals("ID_0815", list1BaseComponentContainer.getId());
		assertNull(label1.getId());
		assertEquals(list2Editpart.getId(),
				list2BaseComponentContainer.getId());
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
		yLayout.getElements().add(yList1);
		YList yList2 = factory.createList();
		yLayout.getElements().add(yList2);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		IListEditpart label1Editpart = DelegatingEditPartManager
				.getInstance().getEditpart(yList1);
		IListEditpart label2Editpart = DelegatingEditPartManager
				.getInstance().getEditpart(yList2);
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
	
	@Test
	public void testBindingIsDisposed(){
		// test that the binding is disposed if field is disposed
		Assert.fail();
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
			presentation = (IWidgetPresentation<Component>) ((IViewEditpart) editpart).getPresentation();
		} else {
			presentation = ((IEmbeddableEditpart) editpart).getPresentation();
		}
		Component widget = presentation.getWidget();
		return widget;
	}
}
