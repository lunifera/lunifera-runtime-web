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

import java.util.Iterator;

import org.eclipse.emf.ecp.ecview.common.context.ContextException;
import org.eclipse.emf.ecp.ecview.common.editpart.DelegatingEditPartManager;
import org.eclipse.emf.ecp.ecview.common.editpart.IElementEditpart;
import org.eclipse.emf.ecp.ecview.common.editpart.IEmbeddableEditpart;
import org.eclipse.emf.ecp.ecview.common.editpart.IViewEditpart;
import org.eclipse.emf.ecp.ecview.common.model.binding.YBeanBindingEndpoint;
import org.eclipse.emf.ecp.ecview.common.model.binding.YBindingSet;
import org.eclipse.emf.ecp.ecview.common.model.core.YElement;
import org.eclipse.emf.ecp.ecview.common.model.core.YView;
import org.eclipse.emf.ecp.ecview.common.presentation.IWidgetPresentation;
import org.eclipse.emf.ecp.ecview.extension.model.extension.YGridLayout;
import org.eclipse.emf.ecp.ecview.extension.model.extension.YProgressBar;
import org.eclipse.emf.ecp.ecview.extension.model.extension.util.SimpleExtensionModelFactory;
import org.eclipse.emf.ecp.ecview.ui.core.editparts.extension.IProgressBarEditpart;
import org.junit.Before;
import org.junit.Test;
import org.lunifera.runtime.web.ecview.presentation.vaadin.VaadinRenderer;
import org.lunifera.runtime.web.ecview.presentation.vaadin.internal.AbstractVaadinWidgetPresenter;
import org.lunifera.runtime.web.ecview.presentation.vaadin.internal.ProgressBarPresentation;
import org.lunifera.runtime.web.ecview.presentation.vaadin.tests.model.ValueBean;
import org.osgi.framework.BundleException;
import org.osgi.service.cm.ConfigurationException;

import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.ProgressBar;
import com.vaadin.ui.UI;

/**
 * Tests the {@link ProgressBarPresentation}.
 */
@SuppressWarnings("restriction")
public class ProgressBarPresentationTests {

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
		// .........> yProgressBar
		YView yView = factory.createView();
		YGridLayout yGridlayout = factory.createGridLayout();
		yView.setContent(yGridlayout);
		YProgressBar yProgressBar = factory.createProgressBar();
		yGridlayout.getElements().add(yProgressBar);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		IProgressBarEditpart progressBarEditpart = DelegatingEditPartManager
				.getInstance().getEditpart(yProgressBar);
		IWidgetPresentation<Component> presentation = progressBarEditpart
				.getPresentation();
		assertTrue(presentation.isRendered());
		assertFalse(presentation.isDisposed());

		yGridlayout.getElements().remove(yProgressBar);
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
		// ......> yProgressBar
		YView yView = factory.createView();
		YProgressBar yProgressBar = factory.createProgressBar();
		yView.setContent(yProgressBar);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		IProgressBarEditpart progressBarEditpart = DelegatingEditPartManager
				.getInstance().getEditpart(yProgressBar);
		IWidgetPresentation<Component> presentation = progressBarEditpart
				.getPresentation();
		ComponentContainer baseComponentContainer = (ComponentContainer) presentation
				.getWidget();

		ProgressBar text = (ProgressBar) unwrapText(baseComponentContainer);
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
		// ......> yProgressBar
		YView yView = factory.createView();
		YGridLayout yLayout = factory.createGridLayout();
		yView.setContent(yLayout);
		YProgressBar yProgressBar1 = factory.createProgressBar();
		yProgressBar1.setCssID("ID_0815");
		yProgressBar1.setCssClass("anyOtherClass");
		yLayout.getElements().add(yProgressBar1);
		YProgressBar yProgressBar2 = factory.createProgressBar();
		yLayout.getElements().add(yProgressBar2);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		IProgressBarEditpart progressBar1Editpart = DelegatingEditPartManager
				.getInstance().getEditpart(yProgressBar1);
		IProgressBarEditpart progressBar2Editpart = DelegatingEditPartManager
				.getInstance().getEditpart(yProgressBar2);
		IWidgetPresentation<Component> progressBar1Presentation = progressBar1Editpart
				.getPresentation();
		IWidgetPresentation<Component> progressBar2Presentation = progressBar2Editpart
				.getPresentation();
		ComponentContainer progressBar1BaseComponentContainer = (ComponentContainer) progressBar1Presentation
				.getWidget();
		ComponentContainer progressBar2BaseComponentContainer = (ComponentContainer) progressBar2Presentation
				.getWidget();

		ProgressBar progressBar1 = (ProgressBar) unwrapText(progressBar1BaseComponentContainer);
		ProgressBar progressBar2 = (ProgressBar) unwrapText(progressBar2BaseComponentContainer);

		// assert css class
		assertTrue(progressBar1BaseComponentContainer.getStyleName().contains(
				AbstractVaadinWidgetPresenter.CSS_CLASS__CONTROL_BASE));
		assertTrue(progressBar2BaseComponentContainer.getStyleName().contains(
				AbstractVaadinWidgetPresenter.CSS_CLASS__CONTROL_BASE));

		assertTrue(progressBar1.getStyleName().contains("anyOtherClass"));
		assertTrue(progressBar2.getStyleName().contains(
				AbstractVaadinWidgetPresenter.CSS_CLASS__CONTROL));

		// assert css id
		assertEquals("ID_0815", progressBar1BaseComponentContainer.getId());
		assertNull(progressBar1.getId());
		assertEquals(progressBar2Editpart.getId(),
				progressBar2BaseComponentContainer.getId());
		assertNull(progressBar2.getId());
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
		// ......> yProgressBar
		YView yView = factory.createView();
		YGridLayout yLayout = factory.createGridLayout();
		yView.setContent(yLayout);
		YProgressBar yProgressBar1 = factory.createProgressBar();
		yLayout.getElements().add(yProgressBar1);
		YProgressBar yProgressBar2 = factory.createProgressBar();
		yLayout.getElements().add(yProgressBar2);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		IProgressBarEditpart progressBar1Editpart = DelegatingEditPartManager
				.getInstance().getEditpart(yProgressBar1);
		IProgressBarEditpart progressBar2Editpart = DelegatingEditPartManager
				.getInstance().getEditpart(yProgressBar2);
		IWidgetPresentation<Component> progressBar1Presentation = progressBar1Editpart
				.getPresentation();
		IWidgetPresentation<Component> progressBar2Presentation = progressBar2Editpart
				.getPresentation();
		ComponentContainer progressBar1BaseComponentContainer = (ComponentContainer) progressBar1Presentation
				.getWidget();
		ComponentContainer progressBar2BaseComponentContainer = (ComponentContainer) progressBar2Presentation
				.getWidget();
		ProgressBar progressBar1 = (ProgressBar) unwrapText(progressBar1BaseComponentContainer);
		ProgressBar progressBar2 = (ProgressBar) unwrapText(progressBar2BaseComponentContainer);

		// start tests
		//
		yProgressBar1.setValue(0.0f);
		yProgressBar2.setValue(0.0f);

		assertTrue(progressBar1.isVisible());
		assertTrue(progressBar1.isEnabled());
		assertFalse(progressBar1.isReadOnly());
		assertEquals(0.0f, progressBar1.getValue(), 0);

		assertTrue(progressBar2.isVisible());
		assertTrue(progressBar2.isEnabled());
		assertFalse(progressBar2.isReadOnly());
		assertEquals(0.0f, progressBar2.getValue(), 0);

		yProgressBar1.setVisible(false);
		assertFalse(progressBar1.isVisible());

		yProgressBar1.setEnabled(false);
		assertFalse(progressBar1.isEnabled());

		yProgressBar1.setEditable(false);
		assertTrue(progressBar1.isReadOnly());

		// target to model
		progressBar1.setReadOnly(false);
		assertTrue(yProgressBar1.isEditable());

		yProgressBar1.setValue(0.30f);
		yProgressBar2.setValue(0.60f);
		assertEquals(0.30f, progressBar1.getValue(), 0);
		assertEquals(0.60f, progressBar2.getValue(), 0);

	}

	@Test
	// BEGIN SUPRESS CATCH EXCEPTION
	public void test_ValueBinding() throws Exception {
		// END SUPRESS CATCH EXCEPTION
		// build the view model
		// ...> yView
		// ......> yProgressBar
		YView yView = factory.createView();
		YGridLayout yLayout = factory.createGridLayout();
		yView.setContent(yLayout);
		YProgressBar yProgressBar1 = factory.createProgressBar();
		yLayout.getElements().add(yProgressBar1);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		IProgressBarEditpart progressBar1Editpart = DelegatingEditPartManager
				.getInstance().getEditpart(yProgressBar1);
		IWidgetPresentation<Component> progressBar1Presentation = progressBar1Editpart
				.getPresentation();
		ComponentContainer progressBar1BaseComponentContainer = (ComponentContainer) progressBar1Presentation
				.getWidget();
		ProgressBar progressBar1 = (ProgressBar) unwrapText(progressBar1BaseComponentContainer);

		// start tests
		//
		YBindingSet yBindingSet = yView.getOrCreateBindingSet();

		yProgressBar1.setValue(0.0f);
		YBeanBindingEndpoint beanBinding = factory.createBeanBindingEndpoint();
		ValueBean bean = new ValueBean(0.30f);
		beanBinding.setPropertyPath("floatValue");
		beanBinding.setBean(bean);
		yBindingSet
				.addBinding(yProgressBar1.createValueEndpoint(), beanBinding);
		assertEquals(0.30f, progressBar1.getValue(), 0);
		assertEquals(0.30f, yProgressBar1.getValue(), 0);

		bean.setFloatValue(0.60f);
		assertEquals(0.60f, progressBar1.getValue(), 0);
		assertEquals(0.60f, yProgressBar1.getValue(), 0);

		progressBar1.setValue(0.70f);
		assertEquals(0.70f, bean.getFloatValue(), 0);
		assertEquals(0.70f, yProgressBar1.getValue(), 0);

		yProgressBar1.setValue(0.80f);
		assertEquals(0.80f, bean.getFloatValue(), 0);
		assertEquals(0.80f, progressBar1.getValue(), 0);
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
		YProgressBar yProgressBar = factory.createProgressBar();
		yGridlayout.getElements().add(yProgressBar);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		IProgressBarEditpart progressBarEditpart = DelegatingEditPartManager
				.getInstance().getEditpart(yProgressBar);
		IWidgetPresentation<Component> presentation = progressBarEditpart
				.getPresentation();
		assertTrue(presentation.isRendered());
		assertFalse(presentation.isDisposed());
		assertEquals(4, presentation.getUIBindings().size());

		presentation.dispose();
		assertFalse(presentation.isRendered());
		assertTrue(presentation.isDisposed());
		assertEquals(0, presentation.getUIBindings().size());
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
			presentation = (IWidgetPresentation<Component>) ((IViewEditpart) editpart)
					.getPresentation();
		} else {
			presentation = ((IEmbeddableEditpart) editpart).getPresentation();
		}
		Component widget = presentation.getWidget();
		return widget;
	}
}
