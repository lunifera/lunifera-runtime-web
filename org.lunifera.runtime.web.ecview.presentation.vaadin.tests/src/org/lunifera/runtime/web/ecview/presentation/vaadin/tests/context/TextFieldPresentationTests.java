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
import org.eclipse.emf.ecp.ecview.extension.model.extension.YTextField;
import org.eclipse.emf.ecp.ecview.extension.model.extension.util.SimpleExtensionModelFactory;
import org.eclipse.emf.ecp.ecview.ui.core.editparts.extension.ITextFieldEditpart;
import org.junit.Before;
import org.junit.Test;
import org.lunifera.runtime.web.ecview.presentation.vaadin.VaadinRenderer;
import org.lunifera.runtime.web.ecview.presentation.vaadin.internal.AbstractVaadinWidgetPresenter;
import org.lunifera.runtime.web.ecview.presentation.vaadin.internal.TextFieldPresentation;
import org.osgi.framework.BundleException;
import org.osgi.service.cm.ConfigurationException;

import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;

/**
 * Tests the {@link TextFieldPresentation}.
 */
@SuppressWarnings("restriction")
public class TextFieldPresentationTests {

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
		YTextField yText = factory.createTextField();
		yGridlayout.getElements().add(yText);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		ITextFieldEditpart textEditpart = DelegatingEditPartManager
				.getInstance().getEditpart(yText);
		IWidgetPresentation<Component> presentation = textEditpart
				.getPresentation();
		Assert.assertTrue(presentation.isRendered());
		Assert.assertFalse(presentation.isDisposed());

		yGridlayout.getElements().remove(yText);
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
		YTextField yText = factory.createTextField();
		yView.setContent(yText);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		ITextFieldEditpart textEditpart = DelegatingEditPartManager
				.getInstance().getEditpart(yText);
		IWidgetPresentation<Component> presentation = textEditpart
				.getPresentation();
		ComponentContainer baseComponentContainer = (ComponentContainer) presentation
				.getWidget();

		TextField text = (TextField) unwrapText(baseComponentContainer);
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
		YTextField yText1 = factory.createTextField();
		yText1.setCssID("ID_0815");
		yText1.setCssClass("anyOtherClass");
		yLayout.getElements().add(yText1);
		YTextField yText2 = factory.createTextField();
		yLayout.getElements().add(yText2);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		ITextFieldEditpart text1Editpart = DelegatingEditPartManager
				.getInstance().getEditpart(yText1);
		ITextFieldEditpart text2Editpart = DelegatingEditPartManager
				.getInstance().getEditpart(yText2);
		IWidgetPresentation<Component> text1Presentation = text1Editpart
				.getPresentation();
		IWidgetPresentation<Component> text2Presentation = text2Editpart
				.getPresentation();
		ComponentContainer text1BaseComponentContainer = (ComponentContainer) text1Presentation
				.getWidget();
		ComponentContainer text2BaseComponentContainer = (ComponentContainer) text2Presentation
				.getWidget();

		TextField text1 = (TextField) unwrapText(text1BaseComponentContainer);
		TextField text2 = (TextField) unwrapText(text2BaseComponentContainer);

		// assert css class
		Assert.assertTrue(text1BaseComponentContainer.getStyleName().contains(
				AbstractVaadinWidgetPresenter.CSS_CLASS__CONTROL_BASE));
		Assert.assertTrue(text2BaseComponentContainer.getStyleName().contains(
				AbstractVaadinWidgetPresenter.CSS_CLASS__CONTROL_BASE));

		Assert.assertTrue(text1.getStyleName().contains("anyOtherClass"));
		Assert.assertTrue(text2.getStyleName().contains(
				AbstractVaadinWidgetPresenter.CSS_CLASS__CONTROL));

		// assert css id
		Assert.assertEquals("ID_0815", text1BaseComponentContainer.getId());
		Assert.assertNull(text1.getId());
		Assert.assertEquals(text2Editpart.getId(),
				text2BaseComponentContainer.getId());
		Assert.assertNull(text2.getId());
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
		YTextField yText1 = factory.createTextField();
		yLayout.getElements().add(yText1);
		YTextField yText2 = factory.createTextField();
		yLayout.getElements().add(yText2);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		ITextFieldEditpart text1Editpart = DelegatingEditPartManager
				.getInstance().getEditpart(yText1);
		ITextFieldEditpart text2Editpart = DelegatingEditPartManager
				.getInstance().getEditpart(yText2);
		IWidgetPresentation<Component> text1Presentation = text1Editpart
				.getPresentation();
		IWidgetPresentation<Component> text2Presentation = text2Editpart
				.getPresentation();
		ComponentContainer text1BaseComponentContainer = (ComponentContainer) text1Presentation
				.getWidget();
		ComponentContainer text2BaseComponentContainer = (ComponentContainer) text2Presentation
				.getWidget();
		TextField text1 = (TextField) unwrapText(text1BaseComponentContainer);
		TextField text2 = (TextField) unwrapText(text2BaseComponentContainer);

		// start tests
		//
		Assert.assertTrue(text1.isVisible());
		Assert.assertTrue(text1.isEnabled());
		Assert.assertFalse(text1.isReadOnly());
		
		Assert.assertTrue(text2.isVisible());
		Assert.assertTrue(text2.isEnabled());
		Assert.assertFalse(text2.isReadOnly());
		
		yText1.setVisible(false);
		Assert.assertFalse(text1.isVisible());
		
		yText1.setEnabled(false);
		Assert.assertFalse(text1.isEnabled());
		
		yText1.setEditable(false);
		Assert.assertTrue(text1.isReadOnly());
		
		// target to model
		text1.setReadOnly(false);
		Assert.assertTrue(yText1.isEditable());
		
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