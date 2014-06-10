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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Iterator;
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
import org.eclipse.emf.ecp.ecview.common.model.core.YElement;
import org.eclipse.emf.ecp.ecview.common.model.core.YView;
import org.eclipse.emf.ecp.ecview.common.presentation.IWidgetPresentation;
import org.eclipse.emf.ecp.ecview.extension.model.extension.YBrowser;
import org.eclipse.emf.ecp.ecview.extension.model.extension.YButton;
import org.eclipse.emf.ecp.ecview.extension.model.extension.YCheckBox;
import org.eclipse.emf.ecp.ecview.extension.model.extension.YGridLayout;
import org.eclipse.emf.ecp.ecview.extension.model.extension.util.SimpleExtensionModelFactory;
import org.eclipse.emf.ecp.ecview.ui.core.editparts.extension.IBrowserEditpart;
import org.eclipse.emf.ecp.ecview.ui.core.editparts.extension.IButtonEditpart;
import org.eclipse.emf.ecp.ecview.ui.core.editparts.extension.ICheckboxEditpart;
import org.junit.Before;
import org.junit.Test;
import org.lunifera.runtime.web.ecview.presentation.vaadin.VaadinRenderer;
import org.lunifera.runtime.web.ecview.presentation.vaadin.internal.AbstractVaadinWidgetPresenter;
import org.lunifera.runtime.web.ecview.presentation.vaadin.internal.ButtonPresentation;
import org.lunifera.runtime.web.ecview.presentation.vaadin.internal.CheckBoxPresentation;
import org.lunifera.runtime.web.ecview.presentation.vaadin.tests.model.ValueBean;
import org.osgi.framework.BundleException;
import org.osgi.service.cm.ConfigurationException;

import com.vaadin.ui.BrowserFrame;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
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
		ComponentContainer checkBox1BaseComponentContainer = (ComponentContainer) checkBox1Presentation
				.getWidget();
		CheckBox checkBox1 = (CheckBox) unwrapText(checkBox1BaseComponentContainer);

		// start tests
		//
		YBindingSet yBindingSet = yView.getOrCreateBindingSet();

		checkBox1.setValue(false);
		YBeanValueBindingEndpoint beanBinding = factory.createBeanBindingEndpoint();
		ValueBean bean = new ValueBean(true);
		beanBinding.setPropertyPath("boolValue");
		beanBinding.setBean(bean);
		yBindingSet.addBinding(yCheckBox1.createValueEndpoint(), beanBinding);
		assertTrue(yCheckBox1.isValue());
		assertTrue(checkBox1.getValue());

		// bean = new ValueBean("Huhu11");
		// beanBinding.setPropertyPath("value");
		// TODO Setting a bean later does not cause any sideeffects. See
		// BeanBindingEndpointEditpart. The binding for the bean is not
		// refreshed.
		// beanBinding.setBean(bean);
		// assertEquals("Huhu11", text1.getValue());
		// assertEquals("Huhu11", yText1.getValue());

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
		yCheckBox.setLabelI18nKey(TestI18nService.KEY__AGE);

		// prepare the I18nService and pass it to the renderer
		Map<String, Object> parameter = new HashMap<String, Object>();
		Map<String, Object> services = new HashMap<String, Object>();
		parameter.put(IViewContext.PARAM_SERVICES, services);
		services.put(II18nService.ID, new TestI18nService());

		VaadinRenderer renderer = new VaadinRenderer();
		IViewContext context = renderer.render(rootLayout, yView, parameter);
		ICheckboxEditpart editpart = DelegatingEditPartManager
				.getInstance().getEditpart(yCheckBox);
		CheckBoxPresentation presentation = editpart
				.getPresentation();

		CheckBox box = (CheckBox) unwrapText(presentation.getWidget());
		assertEquals("Alter", box.getCaption());

		context.setLocale(Locale.ENGLISH);
		assertEquals("Age", box.getCaption());
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

		ICheckboxEditpart editpart = DelegatingEditPartManager
				.getInstance().getEditpart(yCheckBox);
		IWidgetPresentation<Component> presentation = editpart
				.getPresentation();
		ComponentContainer baseComponentContainer = (ComponentContainer) presentation
				.getWidget();
		CheckBox box = (CheckBox) unwrapText(presentation.getWidget());		
		
		ValueBean bean = new ValueBean(false);
		YBeanValueBindingEndpoint yBeanBinding = factory.createBeanBindingEndpoint();
		yBeanBinding.setBean(bean);
		yBeanBinding.setPropertyPath("boolValue");
		YBindingSet yBindingSet = yView.getOrCreateBindingSet();
		yBindingSet.addBinding(yCheckBox.createEditableEndpoint(),
				yBeanBinding);

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

		ICheckboxEditpart editpart = DelegatingEditPartManager
				.getInstance().getEditpart(yCheckBox);
		IWidgetPresentation<Component> presentation = editpart
				.getPresentation();
		ComponentContainer textBaseComponentContainer = (ComponentContainer) presentation
				.getWidget();
		CheckBox box = (CheckBox) unwrapText(presentation.getWidget());		
		
		ValueBean bean = new ValueBean(false);
		YBeanValueBindingEndpoint yBeanBinding = factory.createBeanBindingEndpoint();
		yBeanBinding.setBean(bean);
		yBeanBinding.setPropertyPath("boolValue");
		YBindingSet yBindingSet = yView.getOrCreateBindingSet();
		yBindingSet.addBinding(yCheckBox.createVisibleEndpoint(),
				yBeanBinding);

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

		ICheckboxEditpart editpart = DelegatingEditPartManager
				.getInstance().getEditpart(yCheckBox);
		IWidgetPresentation<Component> presentation = editpart
				.getPresentation();
		ComponentContainer textBaseComponentContainer = (ComponentContainer) presentation
				.getWidget();
		CheckBox box = (CheckBox) unwrapText(presentation.getWidget());		
		
		ValueBean bean = new ValueBean(false);
		YBeanValueBindingEndpoint yBeanBinding = factory.createBeanBindingEndpoint();
		yBeanBinding.setBean(bean);
		yBeanBinding.setPropertyPath("boolValue");
		YBindingSet yBindingSet = yView.getOrCreateBindingSet();
		yBindingSet.addBinding(yCheckBox.createEnabledEndpoint(),
				yBeanBinding);

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
