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

import java.util.Date;
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
import org.eclipse.emf.ecp.ecview.extension.model.extension.YButton;
import org.eclipse.emf.ecp.ecview.extension.model.extension.YComboBox;
import org.eclipse.emf.ecp.ecview.extension.model.extension.YDateTime;
import org.eclipse.emf.ecp.ecview.extension.model.extension.YGridLayout;
import org.eclipse.emf.ecp.ecview.extension.model.extension.util.SimpleExtensionModelFactory;
import org.eclipse.emf.ecp.ecview.ui.core.editparts.extension.IButtonEditpart;
import org.eclipse.emf.ecp.ecview.ui.core.editparts.extension.IComboBoxEditpart;
import org.eclipse.emf.ecp.ecview.ui.core.editparts.extension.IDateTimeEditpart;
import org.junit.Before;
import org.junit.Test;
import org.lunifera.runtime.web.ecview.presentation.vaadin.VaadinRenderer;
import org.lunifera.runtime.web.ecview.presentation.vaadin.internal.AbstractVaadinWidgetPresenter;
import org.lunifera.runtime.web.ecview.presentation.vaadin.internal.ButtonPresentation;
import org.lunifera.runtime.web.ecview.presentation.vaadin.internal.DateTimePresentation;
import org.lunifera.runtime.web.ecview.presentation.vaadin.internal.TextFieldPresentation;
import org.lunifera.runtime.web.ecview.presentation.vaadin.tests.model.ValueBean;
import org.osgi.framework.BundleException;
import org.osgi.service.cm.ConfigurationException;

import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.DateField;
import com.vaadin.ui.UI;

/**
 * Tests the {@link TextFieldPresentation}.
 */
@SuppressWarnings("restriction")
public class DateTimePresentationTests {

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
		YDateTime yText = factory.createDateTime();
		yGridlayout.getElements().add(yText);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		IDateTimeEditpart textEditpart = DelegatingEditPartManager
				.getInstance().getEditpart(yText);
		IWidgetPresentation<Component> presentation = textEditpart
				.getPresentation();
		assertTrue(presentation.isRendered());
		assertFalse(presentation.isDisposed());

		yGridlayout.getElements().remove(yText);
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
		YDateTime yText = factory.createDateTime();
		yView.setContent(yText);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		IDateTimeEditpart textEditpart = DelegatingEditPartManager
				.getInstance().getEditpart(yText);
		IWidgetPresentation<Component> presentation = textEditpart
				.getPresentation();
		ComponentContainer baseComponentContainer = (ComponentContainer) presentation
				.getWidget();

		DateField text = (DateField) unwrapText(baseComponentContainer);
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
		YDateTime yText1 = factory.createDateTime();
		yText1.setCssID("ID_0815");
		yText1.setCssClass("anyOtherClass");
		yLayout.getElements().add(yText1);
		YDateTime yText2 = factory.createDateTime();
		yLayout.getElements().add(yText2);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		IDateTimeEditpart text1Editpart = DelegatingEditPartManager
				.getInstance().getEditpart(yText1);
		IDateTimeEditpart text2Editpart = DelegatingEditPartManager
				.getInstance().getEditpart(yText2);
		IWidgetPresentation<Component> text1Presentation = text1Editpart
				.getPresentation();
		IWidgetPresentation<Component> text2Presentation = text2Editpart
				.getPresentation();
		ComponentContainer text1BaseComponentContainer = (ComponentContainer) text1Presentation
				.getWidget();
		ComponentContainer text2BaseComponentContainer = (ComponentContainer) text2Presentation
				.getWidget();

		DateField text1 = (DateField) unwrapText(text1BaseComponentContainer);
		DateField text2 = (DateField) unwrapText(text2BaseComponentContainer);

		// assert css class
		assertTrue(text1BaseComponentContainer.getStyleName().contains(
				AbstractVaadinWidgetPresenter.CSS_CLASS__CONTROL_BASE));
		assertTrue(text2BaseComponentContainer.getStyleName().contains(
				AbstractVaadinWidgetPresenter.CSS_CLASS__CONTROL_BASE));

		assertTrue(text1.getStyleName().contains("anyOtherClass"));
		assertTrue(text2.getStyleName().contains(
				AbstractVaadinWidgetPresenter.CSS_CLASS__CONTROL));

		// assert css id
		assertEquals("ID_0815", text1BaseComponentContainer.getId());
		assertNull(text1.getId());
		assertEquals(text2Editpart.getId(), text2BaseComponentContainer.getId());
		assertNull(text2.getId());
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
		YDateTime yText1 = factory.createDateTime();
		yLayout.getElements().add(yText1);
		YDateTime yText2 = factory.createDateTime();
		yLayout.getElements().add(yText2);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		IDateTimeEditpart text1Editpart = DelegatingEditPartManager
				.getInstance().getEditpart(yText1);
		IDateTimeEditpart text2Editpart = DelegatingEditPartManager
				.getInstance().getEditpart(yText2);
		IWidgetPresentation<Component> text1Presentation = text1Editpart
				.getPresentation();
		IWidgetPresentation<Component> text2Presentation = text2Editpart
				.getPresentation();
		ComponentContainer text1BaseComponentContainer = (ComponentContainer) text1Presentation
				.getWidget();
		ComponentContainer text2BaseComponentContainer = (ComponentContainer) text2Presentation
				.getWidget();
		DateField text1 = (DateField) unwrapText(text1BaseComponentContainer);
		DateField text2 = (DateField) unwrapText(text2BaseComponentContainer);

		// start tests
		//
		assertTrue(text1.isVisible());
		assertTrue(text1.isEnabled());
		assertFalse(text1.isReadOnly());

		assertTrue(text2.isVisible());
		assertTrue(text2.isEnabled());
		assertFalse(text2.isReadOnly());

		yText1.setVisible(false);
		assertFalse(text1.isVisible());

		yText1.setEnabled(false);
		assertFalse(text1.isEnabled());

		yText1.setEditable(false);
		assertTrue(text1.isReadOnly());

		// target to model
		text1.setReadOnly(false);
		assertTrue(yText1.isEditable());

		Date date = new Date();
		yText1.setValue(date);
		assertEquals(date, text1.getValue());

		date = new Date();
		text1.setValue(date);
		assertEquals(date, yText1.getValue());
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
		YDateTime yText1 = factory.createDateTime();
		yLayout.getElements().add(yText1);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		IDateTimeEditpart text1Editpart = DelegatingEditPartManager
				.getInstance().getEditpart(yText1);
		IWidgetPresentation<Component> text1Presentation = text1Editpart
				.getPresentation();
		ComponentContainer text1BaseComponentContainer = (ComponentContainer) text1Presentation
				.getWidget();
		DateField text1 = (DateField) unwrapText(text1BaseComponentContainer);

		// start tests
		//
		YBindingSet yBindingSet = yView.getOrCreateBindingSet();

		YBeanValueBindingEndpoint beanBinding = factory.createBeanBindingEndpoint();
		ValueBean bean = new ValueBean(new Date());
		beanBinding.setPropertyPath("dateValue");
		beanBinding.setBean(bean);

		yBindingSet.addBinding(yText1.createValueEndpoint(), beanBinding);
		bean.setDateValue(new Date(100000));
		assertEquals(bean.getDateValue(), text1.getValue());
		assertEquals(bean.getDateValue(), yText1.getValue());

		// bean = new ValueBean("Huhu11");
		// beanBinding.setPropertyPath("value");
		// TODO Setting a bean later does not cause any sideeffects. See
		// BeanBindingEndpointEditpart. The binding for the bean is not
		// refreshed.
		// beanBinding.setBean(bean);
		// assertEquals("Huhu11", text1.getValue());
		// assertEquals("Huhu11", yText1.getValue());

		bean.setDateValue(new Date(100001));
		assertEquals(bean.getDateValue(), text1.getValue());
		assertEquals(bean.getDateValue().getTime(), yText1.getValue().getTime());

		Date date = new Date(100002);
		text1.setValue(date);
		assertEquals(date, bean.getDateValue());
		assertEquals(date, yText1.getValue());

		date = new Date(100003);
		yText1.setValue(date);
		assertEquals(date, bean.getDateValue());
		assertEquals(date, text1.getValue());
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
		YDateTime yText = factory.createDateTime();
		yGridlayout.getElements().add(yText);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		IDateTimeEditpart textEditpart = DelegatingEditPartManager
				.getInstance().getEditpart(yText);
		IWidgetPresentation<Component> presentation = textEditpart
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
		YDateTime yText = factory.createDateTime();
		yGridlayout.getElements().add(yText);

		// set the i18n key
		yText.setLabelI18nKey(TestI18nService.KEY__AGE);

		// prepare the I18nService and pass it to the renderer
		Map<String, Object> parameter = new HashMap<String, Object>();
		Map<String, Object> services = new HashMap<String, Object>();
		parameter.put(IViewContext.PARAM_SERVICES, services);
		services.put(II18nService.ID, new TestI18nService());

		VaadinRenderer renderer = new VaadinRenderer();
		IViewContext context = renderer.render(rootLayout, yView, parameter);
		IDateTimeEditpart editpart = DelegatingEditPartManager
				.getInstance().getEditpart(yText);
		DateTimePresentation presentation = editpart
				.getPresentation();

		DateField datetime = (DateField) unwrapText(presentation.getWidget());
		assertEquals("Alter", datetime.getCaption());

		context.setLocale(Locale.ENGLISH);
		assertEquals("Age", datetime.getCaption());
	}
	
	@Test
	// BEGIN SUPRESS CATCH EXCEPTION
	public void test_Readonly_Binding() throws Exception {
		// END SUPRESS CATCH EXCEPTION
		// build the view model
		YView yView = factory.createView();
		YGridLayout yLayout = factory.createGridLayout();
		yView.setContent(yLayout);
		YDateTime yText = factory.createDateTime();
		yLayout.getElements().add(yText);
		
		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		IDateTimeEditpart editpart = DelegatingEditPartManager
				.getInstance().getEditpart(yText);
		IWidgetPresentation<Component> presentation = editpart
				.getPresentation();
		ComponentContainer baseComponentContainer = (ComponentContainer) presentation
				.getWidget();
		DateField datetime = (DateField) unwrapText(presentation.getWidget());		
		
		ValueBean bean = new ValueBean(false);
		YBeanValueBindingEndpoint yBeanBinding = factory.createBeanBindingEndpoint();
		yBeanBinding.setBean(bean);
		yBeanBinding.setPropertyPath("boolValue");
		YBindingSet yBindingSet = yView.getOrCreateBindingSet();
		yBindingSet.addBinding(yText.createEditableEndpoint(),
				yBeanBinding);

		// test binding
		assertFalse(yText.isEditable());
		assertFalse(!datetime.isReadOnly());
		assertFalse(bean.isBoolValue());
		
		bean.setBoolValue(true);
		assertTrue(yText.isEditable());
		assertTrue(!datetime.isReadOnly());
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
		YDateTime yText = factory.createDateTime();
		yLayout.getElements().add(yText);
		
		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		IDateTimeEditpart editpart = DelegatingEditPartManager
				.getInstance().getEditpart(yText);
		IWidgetPresentation<Component> presentation = editpart
				.getPresentation();
		ComponentContainer textBaseComponentContainer = (ComponentContainer) presentation
				.getWidget();
		DateField datetime = (DateField) unwrapText(presentation.getWidget());		
		
		ValueBean bean = new ValueBean(false);
		YBeanValueBindingEndpoint yBeanBinding = factory.createBeanBindingEndpoint();
		yBeanBinding.setBean(bean);
		yBeanBinding.setPropertyPath("boolValue");
		YBindingSet yBindingSet = yView.getOrCreateBindingSet();
		yBindingSet.addBinding(yText.createVisibleEndpoint(),
				yBeanBinding);

		// test binding
		assertFalse(yText.isVisible());
		assertFalse(datetime.isVisible());
		assertFalse(bean.isBoolValue());
		
		bean.setBoolValue(true);
		assertTrue(yText.isVisible());
		assertTrue(datetime.isVisible());
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
		YDateTime yText = factory.createDateTime();
		yLayout.getElements().add(yText);
		
		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		IDateTimeEditpart editpart = DelegatingEditPartManager
				.getInstance().getEditpart(yText);
		IWidgetPresentation<Component> presentation = editpart
				.getPresentation();
		ComponentContainer textBaseComponentContainer = (ComponentContainer) presentation
				.getWidget();
		DateField datetime = (DateField) unwrapText(presentation.getWidget());		
		
		ValueBean bean = new ValueBean(false);
		YBeanValueBindingEndpoint yBeanBinding = factory.createBeanBindingEndpoint();
		yBeanBinding.setBean(bean);
		yBeanBinding.setPropertyPath("boolValue");
		YBindingSet yBindingSet = yView.getOrCreateBindingSet();
		yBindingSet.addBinding(yText.createEnabledEndpoint(),
				yBeanBinding);

		// test binding
		assertFalse(yText.isEnabled());
		assertFalse(datetime.isEnabled());
		assertFalse(bean.isBoolValue());
		
		bean.setBoolValue(true);
		assertTrue(yText.isEnabled());
		assertTrue(datetime.isEnabled());
		assertTrue(bean.isBoolValue());
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
