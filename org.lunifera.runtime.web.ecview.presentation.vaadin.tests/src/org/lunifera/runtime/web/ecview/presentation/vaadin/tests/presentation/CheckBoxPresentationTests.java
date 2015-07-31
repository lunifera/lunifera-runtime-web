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
import org.lunifera.ecview.core.extension.model.extension.YCheckBox;
import org.lunifera.ecview.core.extension.model.extension.YGridLayout;
import org.lunifera.ecview.core.extension.model.extension.util.SimpleExtensionModelFactory;
import org.lunifera.ecview.core.ui.core.editparts.extension.ICheckboxEditpart;
import org.lunifera.runtime.web.ecview.presentation.vaadin.VaadinRenderer;
import org.lunifera.runtime.web.ecview.presentation.vaadin.common.AbstractVaadinWidgetPresenter;
import org.lunifera.runtime.web.ecview.presentation.vaadin.internal.CheckBoxPresentation;
import org.lunifera.runtime.web.ecview.presentation.vaadin.tests.model.ValueBean;
import org.osgi.framework.BundleException;
import org.osgi.service.cm.ConfigurationException;

import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
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

		CheckBox checkBox = (CheckBox) presentation.getWidget();
		assertNotNull(checkBox);
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

		CheckBox checkBox1 = (CheckBox) checkBox1Presentation.getWidget();
		CheckBox checkBox2 = (CheckBox) checkBox2Presentation.getWidget();

		// assert css class

		assertTrue(checkBox1.getStyleName().contains("anyOtherClass"));
		assertTrue(checkBox2.getStyleName().contains(
				AbstractVaadinWidgetPresenter.CSS_CLASS_CONTROL));

		// assert css id
		assertEquals("ID_0815", checkBox1.getId());
		assertEquals(checkBox2Editpart.getId(), checkBox2.getId());
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
		CheckBox checkBox1 = (CheckBox) checkBox1Presentation.getWidget();
		CheckBox checkBox2 = (CheckBox) checkBox2Presentation.getWidget();

		// start tests
		//
		assertFalse(checkBox1.getValue());
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

		yCheckBox1.setValue(false);
		assertFalse(checkBox1.getValue());

		yCheckBox1.setValue(true);
		assertTrue(checkBox1.getValue());
	}

	@Test
	// BEGIN SUPRESS CATCH EXCEPTION
	public void test_ValueBinding() throws Exception {
		// END SUPRESS CATCH EXCEPTION
		// build the view model
		// ...> yView
		// ......> yText
		YView yView = factory.createView();
		YGridLayout yLayout = factory.createGridLayout();
		yView.setContent(yLayout);
		YCheckBox yCheckBox1 = factory.createCheckBox();
		yLayout.getElements().add(yCheckBox1);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		ICheckboxEditpart checkBox1Editpart = DelegatingEditPartManager
				.getInstance().getEditpart(yCheckBox1);
		IWidgetPresentation<Component> checkBox1Presentation = checkBox1Editpart
				.getPresentation();
		CheckBox checkBox1 = (CheckBox) checkBox1Presentation.getWidget();

		// start tests
		//
		YBindingSet yBindingSet = yView.getOrCreateBindingSet();

		checkBox1.setValue(false);
		YBeanValueBindingEndpoint beanBinding = factory
				.createBeanBindingEndpoint();
		ValueBean bean = new ValueBean(true);
		beanBinding.setPropertyPath("boolValue");
		beanBinding.setBean(bean);
		yBindingSet.addBinding(yCheckBox1.createValueEndpoint(), beanBinding);
		assertTrue(yCheckBox1.isValue());
		assertTrue(checkBox1.getValue());

		bean.setBoolValue(false);
		assertFalse(checkBox1.getValue());
		assertFalse(yCheckBox1.isValue());

		checkBox1.setValue(true);
		assertTrue(bean.isBoolValue());
		assertTrue(yCheckBox1.isValue());

		yCheckBox1.setValue(false);
		assertFalse(bean.isBoolValue());
		assertFalse(checkBox1.getValue());
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
		assertEquals(4, presentation.getUIBindings().size());

		presentation.dispose();
		assertFalse(presentation.isRendered());
		assertTrue(presentation.isDisposed());
		assertEquals(0, presentation.getUIBindings().size());
	}

	@Test
	public void test_i18n() throws ContextException {

		// switch the global locale to german
		Locale.setDefault(Locale.GERMAN);

		YView yView = factory.createView();
		YGridLayout yGridlayout = factory.createGridLayout();
		yView.setContent(yGridlayout);
		YCheckBox yCheckBox = factory.createCheckBox();
		yGridlayout.getElements().add(yCheckBox);

		// set the i18n key
		yCheckBox.setLabelI18nKey(I18nServiceForTests.KEY__AGE);

		// prepare the I18nService and pass it to the renderer
		Map<String, Object> parameter = new HashMap<String, Object>();
		Map<String, Object> services = new HashMap<String, Object>();
		parameter.put(IViewContext.PARAM_SERVICES, services);
		services.put(II18nService.ID, new I18nServiceForTests());

		VaadinRenderer renderer = new VaadinRenderer();
		IViewContext context = renderer.render(rootLayout, yView, parameter);
		ICheckboxEditpart editpart = DelegatingEditPartManager.getInstance()
				.getEditpart(yCheckBox);
		CheckBoxPresentation presentation = editpart.getPresentation();

		assertEquals("Alter", presentation.getWidget().getCaption());

		context.setLocale(Locale.ENGLISH);
		assertEquals("Age", presentation.getWidget().getCaption());
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
	// BEGIN SUPRESS CATCH EXCEPTION
	public void test_Readonly_Binding() throws Exception {
		// END SUPRESS CATCH EXCEPTION
		// build the view model
		YView yView = factory.createView();
		YGridLayout yLayout = factory.createGridLayout();
		yView.setContent(yLayout);
		YCheckBox yCheckBox = factory.createCheckBox();
		yLayout.getElements().add(yCheckBox);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		ICheckboxEditpart editpart = DelegatingEditPartManager.getInstance()
				.getEditpart(yCheckBox);
		IWidgetPresentation<Component> presentation = editpart
				.getPresentation();
		CheckBox box = (CheckBox) presentation.getWidget();

		ValueBean bean = new ValueBean(false);
		YBeanValueBindingEndpoint yBeanBinding = factory
				.createBeanBindingEndpoint();
		yBeanBinding.setBean(bean);
		yBeanBinding.setPropertyPath("boolValue");
		YBindingSet yBindingSet = yView.getOrCreateBindingSet();
		yBindingSet
				.addBinding(yCheckBox.createEditableEndpoint(), yBeanBinding);

		// test binding
		assertFalse(yCheckBox.isEditable());
		assertFalse(!box.isReadOnly());
		assertFalse(bean.isBoolValue());

		bean.setBoolValue(true);
		assertTrue(yCheckBox.isEditable());
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
		YCheckBox yCheckBox = factory.createCheckBox();
		yLayout.getElements().add(yCheckBox);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		ICheckboxEditpart editpart = DelegatingEditPartManager.getInstance()
				.getEditpart(yCheckBox);
		IWidgetPresentation<Component> presentation = editpart
				.getPresentation();
		CheckBox box = (CheckBox) presentation.getWidget();

		ValueBean bean = new ValueBean(false);
		YBeanValueBindingEndpoint yBeanBinding = factory
				.createBeanBindingEndpoint();
		yBeanBinding.setBean(bean);
		yBeanBinding.setPropertyPath("boolValue");
		YBindingSet yBindingSet = yView.getOrCreateBindingSet();
		yBindingSet.addBinding(yCheckBox.createVisibleEndpoint(), yBeanBinding);

		// test binding
		assertFalse(yCheckBox.isVisible());
		assertFalse(box.isVisible());
		assertFalse(bean.isBoolValue());

		bean.setBoolValue(true);
		assertTrue(yCheckBox.isVisible());
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
		YCheckBox yCheckBox = factory.createCheckBox();
		yLayout.getElements().add(yCheckBox);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		ICheckboxEditpart editpart = DelegatingEditPartManager.getInstance()
				.getEditpart(yCheckBox);
		IWidgetPresentation<Component> presentation = editpart
				.getPresentation();
		CheckBox box = (CheckBox) presentation.getWidget();

		ValueBean bean = new ValueBean(false);
		YBeanValueBindingEndpoint yBeanBinding = factory
				.createBeanBindingEndpoint();
		yBeanBinding.setBean(bean);
		yBeanBinding.setPropertyPath("boolValue");
		YBindingSet yBindingSet = yView.getOrCreateBindingSet();
		yBindingSet.addBinding(yCheckBox.createEnabledEndpoint(), yBeanBinding);

		// test binding
		assertFalse(yCheckBox.isEnabled());
		assertFalse(box.isEnabled());
		assertFalse(bean.isBoolValue());

		bean.setBoolValue(true);
		assertTrue(yCheckBox.isEnabled());
		assertTrue(box.isEnabled());
		assertTrue(bean.isBoolValue());
	}

}
