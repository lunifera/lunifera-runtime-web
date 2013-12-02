/**
 * Copyright (c) 2013 COMPEX Systemhaus GmbH Heidelberg. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Jose C. Dominguez - initial API and implementation
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
import org.eclipse.emf.ecp.ecview.extension.model.extension.YCheckBox;
import org.eclipse.emf.ecp.ecview.extension.model.extension.util.SimpleExtensionModelFactory;
import org.eclipse.emf.ecp.ecview.ui.core.editparts.extension.ICheckboxEditpart;
import org.junit.Before;
import org.junit.Test;
import org.lunifera.runtime.web.ecview.presentation.vaadin.VaadinRenderer;
import org.lunifera.runtime.web.ecview.presentation.vaadin.internal.AbstractVaadinWidgetPresenter;
import org.lunifera.runtime.web.ecview.presentation.vaadin.internal.CheckBoxPresentation;
import org.osgi.framework.BundleException;
import org.osgi.service.cm.ConfigurationException;

import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.UI;

/**
 * Tests the {@link CheckBoxPresentation}.
 */
@SuppressWarnings("restriction")
public class CheckBoxPresentationTests {

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
		YCheckBox yCheckBox = factory.createCheckBox();
		yGridlayout.getElements().add(yCheckBox);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		ICheckboxEditpart checkBoxEditpart = DelegatingEditPartManager
				.getInstance().getEditpart(yCheckBox);
		IWidgetPresentation<Component> presentation = checkBoxEditpart
				.getPresentation();
		assertTrue(presentation.isRendered());
		assertFalse(presentation.isDisposed());

		yGridlayout.getElements().remove(yCheckBox);
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
		// ......> yText
		YView yView = factory.createView();
		YCheckBox yCheckBox = factory.createCheckBox();
		yView.setContent(yCheckBox);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		ICheckboxEditpart checkBoxEditpart = DelegatingEditPartManager
				.getInstance().getEditpart(yCheckBox);
		IWidgetPresentation<Component> presentation = checkBoxEditpart
				.getPresentation();
		ComponentContainer baseComponentContainer = (ComponentContainer) presentation
				.getWidget();

		CheckBox checkBox = (CheckBox) unwrapText(baseComponentContainer);
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
		// ......> yText
		YView yView = factory.createView();
		YGridLayout yLayout = factory.createGridLayout();
		yView.setContent(yLayout);
		YCheckBox yCheckBox1 = factory.createCheckBox();
		yCheckBox1.setCssID("ID_0815");
		yCheckBox1.setCssClass("anyOtherClass");
		yLayout.getElements().add(yCheckBox1);
		YCheckBox yCheckBox2 = factory.createCheckBox();
		yLayout.getElements().add(yCheckBox2);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		ICheckboxEditpart checkBox1Editpart = DelegatingEditPartManager
				.getInstance().getEditpart(yCheckBox1);
		ICheckboxEditpart checkBox2Editpart = DelegatingEditPartManager
				.getInstance().getEditpart(yCheckBox2);
		IWidgetPresentation<Component> checkBox1Presentation = checkBox1Editpart
				.getPresentation();
		IWidgetPresentation<Component> checkBox2Presentation = checkBox2Editpart
				.getPresentation();
		ComponentContainer checkBox1BaseComponentContainer = (ComponentContainer) checkBox1Presentation
				.getWidget();
		ComponentContainer checkBox2BaseComponentContainer = (ComponentContainer) checkBox2Presentation
				.getWidget();

		CheckBox checkBox1 = (CheckBox) unwrapText(checkBox1BaseComponentContainer);
		CheckBox checkBox2 = (CheckBox) unwrapText(checkBox2BaseComponentContainer);

		// assert css class
		assertTrue(checkBox1BaseComponentContainer.getStyleName().contains(
				AbstractVaadinWidgetPresenter.CSS_CLASS__CONTROL_BASE));
		assertTrue(checkBox2BaseComponentContainer.getStyleName().contains(
				AbstractVaadinWidgetPresenter.CSS_CLASS__CONTROL_BASE));

		assertTrue(checkBox1.getStyleName().contains("anyOtherClass"));
		assertTrue(checkBox2.getStyleName().contains(
				AbstractVaadinWidgetPresenter.CSS_CLASS__CONTROL));

		// assert css id
		assertEquals("ID_0815", checkBox1BaseComponentContainer.getId());
		assertNull(checkBox1.getId());
		assertEquals(checkBox2Editpart.getId(),
				checkBox2BaseComponentContainer.getId());
		assertNull(checkBox2.getId());
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
		YCheckBox yCheckBox1 = factory.createCheckBox();
		yLayout.getElements().add(yCheckBox1);
		YCheckBox yCheckBox2 = factory.createCheckBox();
		yLayout.getElements().add(yCheckBox2);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		ICheckboxEditpart checkBox1Editpart = DelegatingEditPartManager
				.getInstance().getEditpart(yCheckBox1);
		ICheckboxEditpart checkBox2Editpart = DelegatingEditPartManager
				.getInstance().getEditpart(yCheckBox2);
		IWidgetPresentation<Component> checkBox1Presentation = checkBox1Editpart
				.getPresentation();
		IWidgetPresentation<Component> checkBox2Presentation = checkBox2Editpart
				.getPresentation();
		ComponentContainer checkBox1BaseComponentContainer = (ComponentContainer) checkBox1Presentation
				.getWidget();
		ComponentContainer checkBox2BaseComponentContainer = (ComponentContainer) checkBox2Presentation
				.getWidget();
		CheckBox checkBox1 = (CheckBox) unwrapText(checkBox1BaseComponentContainer);
		CheckBox checkBox2 = (CheckBox) unwrapText(checkBox2BaseComponentContainer);

		// start tests
		//
		assertTrue(checkBox1.isVisible());
		assertTrue(checkBox1.isEnabled());
		assertFalse(checkBox1.isReadOnly());
		
		assertTrue(checkBox2.isVisible());
		assertTrue(checkBox2.isEnabled());
		assertFalse(checkBox2.isReadOnly());
		
		yCheckBox1.setVisible(false);
		assertFalse(checkBox1.isVisible());
		
		yCheckBox1.setEnabled(false);
		assertFalse(checkBox1.isEnabled());
		
		yCheckBox1.setEditable(false);
		assertTrue(checkBox1.isReadOnly());
		
		// target to model
		checkBox1.setReadOnly(false);
		assertTrue(yCheckBox1.isEditable());
		
	}

	/**
	 * Unwraps the component from its parent composite.
	 * 
	 * @param component
	 * @return
	 */
	private Component unwrapText(Component component) {
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
