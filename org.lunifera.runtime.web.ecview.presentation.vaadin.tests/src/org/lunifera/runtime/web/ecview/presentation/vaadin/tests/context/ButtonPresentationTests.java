/**
 * Copyright (c) 2012 Florian Pirchner (Vienna, Austria) and others.
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

import junit.framework.Assert;

import org.eclipse.emf.ecp.ecview.common.editpart.DelegatingEditPartManager;
import org.eclipse.emf.ecp.ecview.common.editpart.IElementEditpart;
import org.eclipse.emf.ecp.ecview.common.editpart.IEmbeddableEditpart;
import org.eclipse.emf.ecp.ecview.common.editpart.IViewEditpart;
import org.eclipse.emf.ecp.ecview.common.model.core.YElement;
import org.eclipse.emf.ecp.ecview.common.model.core.YView;
import org.eclipse.emf.ecp.ecview.common.presentation.IWidgetPresentation;
import org.eclipse.emf.ecp.ecview.extension.model.extension.YGridLayout;
import org.eclipse.emf.ecp.ecview.extension.model.extension.YButton;
import org.eclipse.emf.ecp.ecview.extension.model.extension.util.SimpleExtensionModelFactory;
import org.eclipse.emf.ecp.ecview.ui.core.editparts.extension.IButtonEditpart;
import org.junit.Before;
import org.junit.Test;
import org.lunifera.runtime.web.ecview.presentation.vaadin.VaadinRenderer;
import org.lunifera.runtime.web.ecview.presentation.vaadin.internal.AbstractVaadinWidgetPresenter;
import org.lunifera.runtime.web.ecview.presentation.vaadin.internal.ButtonPresentation;
import org.osgi.framework.BundleException;
import org.osgi.service.cm.ConfigurationException;

import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Button;
import com.vaadin.ui.UI;

/**
 * Tests the {@link ButtonPresentation}.
 */
@SuppressWarnings("restriction")
public class ButtonPresentationTests {

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
		// .........> yButton
		YView yView = factory.createView();
		YGridLayout yGridlayout = factory.createGridLayout();
		yView.setContent(yGridlayout);
		YButton yButton = factory.createButton();
		yGridlayout.getElements().add(yButton);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		IButtonEditpart buttonEditpart = DelegatingEditPartManager
				.getInstance().getEditpart(yButton);
		IWidgetPresentation<Component> presentation = buttonEditpart
				.getPresentation();
		Assert.assertTrue(presentation.isRendered());
		Assert.assertFalse(presentation.isDisposed());

		yGridlayout.getElements().remove(yButton);
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
		// ......> yButton
		YView yView = factory.createView();
		YButton yButton = factory.createButton();
		yView.setContent(yButton);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		IButtonEditpart buttonEditpart = DelegatingEditPartManager
				.getInstance().getEditpart(yButton);
		IWidgetPresentation<Component> presentation = buttonEditpart
				.getPresentation();
		ComponentContainer baseComponentContainer = (ComponentContainer) presentation
				.getWidget();

		Button button = (Button) unwrapButton(baseComponentContainer);
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
		// ......> yButton
		YView yView = factory.createView();
		YGridLayout yLayout = factory.createGridLayout();
		yView.setContent(yLayout);
		YButton yButton1 = factory.createButton();
		yButton1.setCssID("ID_0815");
		yButton1.setCssClass("anyOtherClass");
		yLayout.getElements().add(yButton1);
		YButton yButton2 = factory.createButton();
		yLayout.getElements().add(yButton2);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		IButtonEditpart button1Editpart = DelegatingEditPartManager
				.getInstance().getEditpart(yButton1);
		IButtonEditpart button2Editpart = DelegatingEditPartManager
				.getInstance().getEditpart(yButton2);
		IWidgetPresentation<Component> button1Presentation = button1Editpart
				.getPresentation();
		IWidgetPresentation<Component> button2Presentation = button2Editpart
				.getPresentation();
		ComponentContainer button1BaseComponentContainer = (ComponentContainer) button1Presentation
				.getWidget();
		ComponentContainer button2BaseComponentContainer = (ComponentContainer) button2Presentation
				.getWidget();

		Button button1 = (Button) unwrapButton(button1BaseComponentContainer);
		Button button2 = (Button) unwrapButton(button2BaseComponentContainer);

		// assert css class
		Assert.assertTrue(button1BaseComponentContainer.getStyleName().contains(
				AbstractVaadinWidgetPresenter.CSS_CLASS__CONTROL_BASE));
		Assert.assertTrue(button2BaseComponentContainer.getStyleName().contains(
				AbstractVaadinWidgetPresenter.CSS_CLASS__CONTROL_BASE));

		Assert.assertTrue(button1.getStyleName().contains("anyOtherClass"));
		Assert.assertTrue(button2.getStyleName().contains(
				AbstractVaadinWidgetPresenter.CSS_CLASS__CONTROL));

		// assert css id
		Assert.assertEquals("ID_0815", button1BaseComponentContainer.getId());
		Assert.assertNull(button1.getId());
		Assert.assertEquals(button2Editpart.getId(),
				button2BaseComponentContainer.getId());
		Assert.assertNull(button2.getId());
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
		// ......> yButton
		YView yView = factory.createView();
		YGridLayout yLayout = factory.createGridLayout();
		yView.setContent(yLayout);
		YButton yButton1 = factory.createButton();
		yLayout.getElements().add(yButton1);
		YButton yButton2 = factory.createButton();
		yLayout.getElements().add(yButton2);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		IButtonEditpart button1Editpart = DelegatingEditPartManager
				.getInstance().getEditpart(yButton1);
		IButtonEditpart button2Editpart = DelegatingEditPartManager
				.getInstance().getEditpart(yButton2);
		IWidgetPresentation<Component> button1Presentation = button1Editpart
				.getPresentation();
		IWidgetPresentation<Component> button2Presentation = button2Editpart
				.getPresentation();
		ComponentContainer button1BaseComponentContainer = (ComponentContainer) button1Presentation
				.getWidget();
		ComponentContainer button2BaseComponentContainer = (ComponentContainer) button2Presentation
				.getWidget();
		Button button1 = (Button) unwrapButton(button1BaseComponentContainer);
		Button button2 = (Button) unwrapButton(button2BaseComponentContainer);

		// start tests
		//
		Assert.assertTrue(button1.isVisible());
		Assert.assertTrue(button1.isEnabled());
		Assert.assertFalse(button1.isReadOnly());
		
		Assert.assertTrue(button2.isVisible());
		Assert.assertTrue(button2.isEnabled());
		Assert.assertFalse(button2.isReadOnly());
		
		yButton1.setVisible(false);
		Assert.assertFalse(button1.isVisible());
		
		yButton1.setEnabled(false);
		Assert.assertFalse(button1.isEnabled());
		
	}

	/**
	 * Unwraps the component from its parent composite.
	 * 
	 * @param component
	 * @return
	 */
	private Component unwrapButton(Component component) {
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
