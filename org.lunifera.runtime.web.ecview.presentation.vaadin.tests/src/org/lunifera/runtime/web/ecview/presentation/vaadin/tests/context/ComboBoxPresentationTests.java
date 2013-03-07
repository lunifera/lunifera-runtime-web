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

import junit.framework.Assert;

import org.eclipse.emf.ecp.ecview.common.editpart.DelegatingEditPartManager;
import org.eclipse.emf.ecp.ecview.common.editpart.IElementEditpart;
import org.eclipse.emf.ecp.ecview.common.editpart.IEmbeddableEditpart;
import org.eclipse.emf.ecp.ecview.common.editpart.IViewEditpart;
import org.eclipse.emf.ecp.ecview.common.model.core.YElement;
import org.eclipse.emf.ecp.ecview.common.model.core.YView;
import org.eclipse.emf.ecp.ecview.common.presentation.IWidgetPresentation;
import org.eclipse.emf.ecp.ecview.extension.model.extension.YComboBox;
import org.eclipse.emf.ecp.ecview.extension.model.extension.YGridLayout;
import org.eclipse.emf.ecp.ecview.extension.model.extension.util.SimpleExtensionModelFactory;
import org.eclipse.emf.ecp.ecview.ui.core.editparts.extension.IComboBoxEditpart;
import org.junit.Before;
import org.junit.Test;
import org.lunifera.runtime.web.ecview.presentation.vaadin.VaadinRenderer;
import org.lunifera.runtime.web.ecview.presentation.vaadin.internal.AbstractVaadinWidgetPresenter;
import org.lunifera.runtime.web.ecview.presentation.vaadin.internal.ComboBoxPresentation;
import org.osgi.framework.BundleException;
import org.osgi.service.cm.ConfigurationException;

import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
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
		Assert.assertTrue(presentation.isRendered());
		Assert.assertFalse(presentation.isDisposed());

		yGridlayout.getElements().remove(yComboBox);
		Assert.assertFalse(presentation.isRendered());
		Assert.assertFalse(presentation.isDisposed());
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
		YComboBox yComboBox = factory.createComboBox();
		yView.setContent(yComboBox);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		IComboBoxEditpart comboBoxEditpart = DelegatingEditPartManager
				.getInstance().getEditpart(yComboBox);
		IWidgetPresentation<Component> presentation = comboBoxEditpart
				.getPresentation();
		ComponentContainer baseComponentContainer = (ComponentContainer) presentation
				.getWidget();

		ComboBox comboBox = (ComboBox) unwrapText(baseComponentContainer);
		Assert.assertEquals(1, baseComponentContainer.getComponentCount());

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
		ComponentContainer comboBox1BaseComponentContainer = (ComponentContainer) comboBox1Presentation
				.getWidget();
		ComponentContainer comboBox2BaseComponentContainer = (ComponentContainer) comboBox2Presentation
				.getWidget();

		ComboBox comboBox1 = (ComboBox) unwrapText(comboBox1BaseComponentContainer);
		ComboBox comboBox2 = (ComboBox) unwrapText(comboBox2BaseComponentContainer);

		// assert css class
		Assert.assertTrue(comboBox1BaseComponentContainer.getStyleName().contains(
				AbstractVaadinWidgetPresenter.CSS_CLASS__CONTROL_BASE));
		Assert.assertTrue(comboBox2BaseComponentContainer.getStyleName().contains(
				AbstractVaadinWidgetPresenter.CSS_CLASS__CONTROL_BASE));

		Assert.assertTrue(comboBox1.getStyleName().contains("anyOtherClass"));
		Assert.assertTrue(comboBox2.getStyleName().contains(
				AbstractVaadinWidgetPresenter.CSS_CLASS__CONTROL));

		// assert css id
		Assert.assertEquals("ID_0815", comboBox1BaseComponentContainer.getId());
		Assert.assertNull(comboBox1.getId());
		Assert.assertEquals(comboBox2Editpart.getId(),
				comboBox2BaseComponentContainer.getId());
		Assert.assertNull(comboBox2.getId());
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
		ComponentContainer comboBox1BaseComponentContainer = (ComponentContainer) comboBox1Presentation
				.getWidget();
		ComponentContainer comboBox2BaseComponentContainer = (ComponentContainer) comboBox2Presentation
				.getWidget();
		ComboBox comboBox1 = (ComboBox) unwrapText(comboBox1BaseComponentContainer);
		ComboBox comboBox2 = (ComboBox) unwrapText(comboBox2BaseComponentContainer);

		// start tests
		//
		Assert.assertTrue(comboBox1.isVisible());
		Assert.assertTrue(comboBox1.isEnabled());
		Assert.assertFalse(comboBox1.isReadOnly());
		
		Assert.assertTrue(comboBox2.isVisible());
		Assert.assertTrue(comboBox2.isEnabled());
		Assert.assertFalse(comboBox2.isReadOnly());
		
		yComboBox1.setVisible(false);
		Assert.assertFalse(comboBox1.isVisible());
		
		yComboBox1.setEnabled(false);
		Assert.assertFalse(comboBox1.isEnabled());
		
		yComboBox1.setEditable(false);
		Assert.assertTrue(comboBox1.isReadOnly());
		
		// target to model
		comboBox1.setReadOnly(false);
		Assert.assertTrue(yComboBox1.isEditable());
		
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
	protected Component getComponent(YElement yView) {
		IElementEditpart editpart = DelegatingEditPartManager.getInstance()
				.getEditpart(yView);

		IWidgetPresentation<Component> presentation = null;
		if (editpart instanceof IViewEditpart) {
			presentation = ((IViewEditpart) editpart).getPresentation();
		} else {
			presentation = ((IEmbeddableEditpart) editpart).getPresentation();
		}
		Component widget = presentation.getWidget();
		return widget;
	}
}
