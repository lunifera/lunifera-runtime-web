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
import org.lunifera.ecview.core.common.model.core.YElement;
import org.lunifera.ecview.core.common.model.core.YView;
import org.lunifera.ecview.core.common.presentation.IWidgetPresentation;
import org.lunifera.ecview.core.extension.model.extension.YButton;
import org.lunifera.ecview.core.extension.model.extension.YGridLayout;
import org.lunifera.ecview.core.extension.model.extension.util.SimpleExtensionModelFactory;
import org.lunifera.ecview.core.ui.core.editparts.extension.IButtonEditpart;
import org.lunifera.runtime.web.ecview.presentation.vaadin.VaadinRenderer;
import org.lunifera.runtime.web.ecview.presentation.vaadin.common.AbstractVaadinWidgetPresenter;
import org.lunifera.runtime.web.ecview.presentation.vaadin.internal.ButtonPresentation;
import org.lunifera.runtime.web.ecview.presentation.vaadin.tests.model.ValueBean;
import org.osgi.framework.BundleException;
import org.osgi.service.cm.ConfigurationException;

import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
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
		assertTrue(presentation.isRendered());
		assertFalse(presentation.isDisposed());

		yGridlayout.getElements().remove(yButton);
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
		Button button = (Button) presentation.getWidget();
		assertNotNull(button);
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

		Button button1 = (Button) button1Presentation.getWidget();
		Button button2 = (Button) button2Presentation.getWidget();

		// assert css class
		assertTrue(button1.getStyleName().contains("anyOtherClass"));
		assertTrue(button2.getStyleName().contains(
				AbstractVaadinWidgetPresenter.CSS_CLASS_CONTROL));

		// assert css id
		assertEquals("ID_0815", button1.getId());
		assertEquals(button2Editpart.getId(),
				button2.getId());
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
		Button button1 = (Button) button1Presentation
				.getWidget();
		Button button2 = (Button) button2Presentation
				.getWidget();

		// start tests
		//
		assertTrue(button1.isVisible());
		assertTrue(button1.isEnabled());
		assertFalse(button1.isReadOnly());

		assertTrue(button2.isVisible());
		assertTrue(button2.isEnabled());
		assertFalse(button2.isReadOnly());

		yButton1.setVisible(false);
		assertFalse(button1.isVisible());

		yButton1.setEnabled(false);
		assertFalse(button1.isEnabled());

	}

	/**
	 * Test the automatic disposal of bindings
	 * 
	 * @throws ContextException
	 */
	@Test
	public void testBindingIsDisposed() throws ContextException {
		// test that the binding is disposed if field is disposed
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
		assertTrue(presentation.isRendered());
		assertFalse(presentation.isDisposed());
		assertEquals(4, presentation.getUIBindings().size());

		presentation.dispose();
		assertFalse(presentation.isRendered());
		assertTrue(presentation.isDisposed());
		assertEquals(0, presentation.getUIBindings().size());

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

	@Test
	public void test_i18n() throws ContextException {

		// switch the global locale to german
		Locale.setDefault(Locale.GERMAN);

		YView yView = factory.createView();
		YGridLayout yGridlayout = factory.createGridLayout();
		yView.setContent(yGridlayout);
		YButton yButton = factory.createButton();
		yGridlayout.getElements().add(yButton);

		// set the i18n key
		yButton.setLabelI18nKey(I18nServiceForTests.KEY__AGE);

		// prepare the I18nService and pass it to the renderer
		Map<String, Object> parameter = new HashMap<String, Object>();
		Map<String, Object> services = new HashMap<String, Object>();
		parameter.put(IViewContext.PARAM_SERVICES, services);
		services.put(II18nService.ID, new I18nServiceForTests());

		VaadinRenderer renderer = new VaadinRenderer();
		IViewContext context = renderer.render(rootLayout, yView, parameter);
		IButtonEditpart editpart = DelegatingEditPartManager.getInstance()
				.getEditpart(yButton);
		ButtonPresentation presentation = editpart.getPresentation();

		Button button = (Button) presentation.getWidget();
		assertEquals("Alter", button.getCaption());

		context.setLocale(Locale.ENGLISH);
		assertEquals("Age", button.getCaption());
	}

	@Test
	// BEGIN SUPRESS CATCH EXCEPTION
	public void test_Readonly_Binding() throws Exception {
		// END SUPRESS CATCH EXCEPTION
		// build the view model
		YView yView = factory.createView();
		YGridLayout yLayout = factory.createGridLayout();
		yView.setContent(yLayout);
		YButton yButton = factory.createButton();
		yLayout.getElements().add(yButton);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		IButtonEditpart editpart = DelegatingEditPartManager.getInstance()
				.getEditpart(yButton);
		IWidgetPresentation<Component> presentation = editpart
				.getPresentation();
		Button button = (Button) presentation.getWidget();

		ValueBean bean = new ValueBean(false);
		YBeanValueBindingEndpoint yBeanBinding = factory
				.createBeanBindingEndpoint();
		yBeanBinding.setBean(bean);
		yBeanBinding.setPropertyPath("boolValue");
		YBindingSet yBindingSet = yView.getOrCreateBindingSet();
		yBindingSet.addBinding(yButton.createEditableEndpoint(), yBeanBinding);

		// test binding
		assertFalse(yButton.isEditable());
		assertFalse(!button.isReadOnly());
		assertFalse(bean.isBoolValue());

		bean.setBoolValue(true);
		assertTrue(yButton.isEditable());
		assertTrue(!button.isReadOnly());
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
		YButton yButton = factory.createButton();
		yLayout.getElements().add(yButton);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		IButtonEditpart editpart = DelegatingEditPartManager.getInstance()
				.getEditpart(yButton);
		IWidgetPresentation<Component> presentation = editpart
				.getPresentation();
		Button button = (Button) presentation.getWidget();

		ValueBean bean = new ValueBean(false);
		YBeanValueBindingEndpoint yBeanBinding = factory
				.createBeanBindingEndpoint();
		yBeanBinding.setBean(bean);
		yBeanBinding.setPropertyPath("boolValue");
		YBindingSet yBindingSet = yView.getOrCreateBindingSet();
		yBindingSet.addBinding(yButton.createVisibleEndpoint(), yBeanBinding);

		// test binding
		assertFalse(yButton.isVisible());
		assertFalse(button.isVisible());
		assertFalse(bean.isBoolValue());

		bean.setBoolValue(true);
		assertTrue(yButton.isVisible());
		assertTrue(button.isVisible());
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
		YButton yButton = factory.createButton();
		yLayout.getElements().add(yButton);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		IButtonEditpart editpart = DelegatingEditPartManager.getInstance()
				.getEditpart(yButton);
		IWidgetPresentation<Component> presentation = editpart
				.getPresentation();
		Button button = (Button) presentation.getWidget();

		ValueBean bean = new ValueBean(false);
		YBeanValueBindingEndpoint yBeanBinding = factory
				.createBeanBindingEndpoint();
		yBeanBinding.setBean(bean);
		yBeanBinding.setPropertyPath("boolValue");
		YBindingSet yBindingSet = yView.getOrCreateBindingSet();
		yBindingSet.addBinding(yButton.createEnabledEndpoint(), yBeanBinding);

		// test binding
		assertFalse(yButton.isEnabled());
		assertFalse(button.isEnabled());
		assertFalse(bean.isBoolValue());

		bean.setBoolValue(true);
		assertTrue(yButton.isEnabled());
		assertTrue(button.isEnabled());
		assertTrue(bean.isBoolValue());
	}
}
