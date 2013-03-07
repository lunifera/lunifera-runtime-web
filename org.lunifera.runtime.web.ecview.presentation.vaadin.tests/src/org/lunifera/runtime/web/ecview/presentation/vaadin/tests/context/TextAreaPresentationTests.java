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
import org.eclipse.emf.ecp.ecview.extension.model.extension.YGridLayout;
import org.eclipse.emf.ecp.ecview.extension.model.extension.YTextArea;
import org.eclipse.emf.ecp.ecview.extension.model.extension.util.SimpleExtensionModelFactory;
import org.eclipse.emf.ecp.ecview.ui.core.editparts.extension.ITextAreaEditpart;
import org.junit.Before;
import org.junit.Test;
import org.lunifera.runtime.web.ecview.presentation.vaadin.VaadinRenderer;
import org.lunifera.runtime.web.ecview.presentation.vaadin.internal.AbstractVaadinWidgetPresenter;
import org.lunifera.runtime.web.ecview.presentation.vaadin.internal.TextAreaPresentation;
import org.osgi.framework.BundleException;
import org.osgi.service.cm.ConfigurationException;

import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.UI;

/**
 * Tests the {@link TextAreaPresentation}.
 */
@SuppressWarnings("restriction")
public class TextAreaPresentationTests {

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
		YTextArea yTextArea = factory.createTextArea();
		yGridlayout.getElements().add(yTextArea);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		ITextAreaEditpart textAreaEditpart = DelegatingEditPartManager
				.getInstance().getEditpart(yTextArea);
		IWidgetPresentation<Component> presentation = textAreaEditpart
				.getPresentation();
		Assert.assertTrue(presentation.isRendered());
		Assert.assertFalse(presentation.isDisposed());

		yGridlayout.getElements().remove(yTextArea);
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
		YTextArea yTextArea = factory.createTextArea();
		yView.setContent(yTextArea);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		ITextAreaEditpart textAreaEditpart = DelegatingEditPartManager
				.getInstance().getEditpart(yTextArea);
		IWidgetPresentation<Component> presentation = textAreaEditpart
				.getPresentation();
		ComponentContainer baseComponentContainer = (ComponentContainer) presentation
				.getWidget();

		TextArea textArea = (TextArea) unwrapText(baseComponentContainer);
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
		YTextArea yTextArea1 = factory.createTextArea();
		yTextArea1.setCssID("ID_0815");
		yTextArea1.setCssClass("anyOtherClass");
		yLayout.getElements().add(yTextArea1);
		YTextArea yTextArea2 = factory.createTextArea();
		yLayout.getElements().add(yTextArea2);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		ITextAreaEditpart textArea1Editpart = DelegatingEditPartManager
				.getInstance().getEditpart(yTextArea1);
		ITextAreaEditpart textArea2Editpart = DelegatingEditPartManager
				.getInstance().getEditpart(yTextArea2);
		IWidgetPresentation<Component> textArea1Presentation = textArea1Editpart
				.getPresentation();
		IWidgetPresentation<Component> textArea2Presentation = textArea2Editpart
				.getPresentation();
		ComponentContainer textArea1BaseComponentContainer = (ComponentContainer) textArea1Presentation
				.getWidget();
		ComponentContainer textArea2BaseComponentContainer = (ComponentContainer) textArea2Presentation
				.getWidget();

		TextArea textArea1 = (TextArea) unwrapText(textArea1BaseComponentContainer);
		TextArea textArea2 = (TextArea) unwrapText(textArea2BaseComponentContainer);

		// assert css class
		Assert.assertTrue(textArea1BaseComponentContainer.getStyleName().contains(
				AbstractVaadinWidgetPresenter.CSS_CLASS__CONTROL_BASE));
		Assert.assertTrue(textArea2BaseComponentContainer.getStyleName().contains(
				AbstractVaadinWidgetPresenter.CSS_CLASS__CONTROL_BASE));

		Assert.assertTrue(textArea1.getStyleName().contains("anyOtherClass"));
		Assert.assertTrue(textArea2.getStyleName().contains(
				AbstractVaadinWidgetPresenter.CSS_CLASS__CONTROL));

		// assert css id
		Assert.assertEquals("ID_0815", textArea1BaseComponentContainer.getId());
		Assert.assertNull(textArea1.getId());
		Assert.assertEquals(textArea2Editpart.getId(),
				textArea2BaseComponentContainer.getId());
		Assert.assertNull(textArea2.getId());
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
		YTextArea yTextArea1 = factory.createTextArea();
		yLayout.getElements().add(yTextArea1);
		YTextArea yTextArea2 = factory.createTextArea();
		yLayout.getElements().add(yTextArea2);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		ITextAreaEditpart textArea1Editpart = DelegatingEditPartManager
				.getInstance().getEditpart(yTextArea1);
		ITextAreaEditpart textArea2Editpart = DelegatingEditPartManager
				.getInstance().getEditpart(yTextArea2);
		IWidgetPresentation<Component> textArea1Presentation = textArea1Editpart
				.getPresentation();
		IWidgetPresentation<Component> textArea2Presentation = textArea2Editpart
				.getPresentation();
		ComponentContainer textArea1BaseComponentContainer = (ComponentContainer) textArea1Presentation
				.getWidget();
		ComponentContainer textArea2BaseComponentContainer = (ComponentContainer) textArea2Presentation
				.getWidget();
		TextArea textArea1 = (TextArea) unwrapText(textArea1BaseComponentContainer);
		TextArea textArea2 = (TextArea) unwrapText(textArea2BaseComponentContainer);

		// start tests
		//
		Assert.assertTrue(textArea1.isVisible());
		Assert.assertTrue(textArea1.isEnabled());
		Assert.assertFalse(textArea1.isReadOnly());
		
		Assert.assertTrue(textArea2.isVisible());
		Assert.assertTrue(textArea2.isEnabled());
		Assert.assertFalse(textArea2.isReadOnly());
		
		yTextArea1.setVisible(false);
		Assert.assertFalse(textArea1.isVisible());
		
		yTextArea1.setEnabled(false);
		Assert.assertFalse(textArea1.isEnabled());
		
		yTextArea1.setEditable(false);
		Assert.assertTrue(textArea1.isReadOnly());
		
		// target to model
		textArea1.setReadOnly(false);
		Assert.assertTrue(yTextArea1.isEditable());
		
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
