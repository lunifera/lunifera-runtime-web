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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import org.eclipse.emf.ecp.ecview.common.context.ContextException;
import org.eclipse.emf.ecp.ecview.common.context.II18nService;
import org.eclipse.emf.ecp.ecview.common.context.IViewContext;
import org.eclipse.emf.ecp.ecview.common.context.ViewSetContext;
import org.eclipse.emf.ecp.ecview.common.editpart.DelegatingEditPartManager;
import org.eclipse.emf.ecp.ecview.common.editpart.IElementEditpart;
import org.eclipse.emf.ecp.ecview.common.editpart.IEmbeddableEditpart;
import org.eclipse.emf.ecp.ecview.common.editpart.IViewEditpart;
import org.eclipse.emf.ecp.ecview.common.editpart.IViewSetEditpart;
import org.eclipse.emf.ecp.ecview.common.model.binding.YBeanValueBindingEndpoint;
import org.eclipse.emf.ecp.ecview.common.model.binding.YBindingSet;
import org.eclipse.emf.ecp.ecview.common.model.core.YElement;
import org.eclipse.emf.ecp.ecview.common.model.core.YView;
import org.eclipse.emf.ecp.ecview.common.model.core.YViewSet;
import org.eclipse.emf.ecp.ecview.common.presentation.IWidgetPresentation;
import org.eclipse.emf.ecp.ecview.extension.model.datatypes.YDecimalDatatype;
import org.eclipse.emf.ecp.ecview.extension.model.extension.YDecimalField;
import org.eclipse.emf.ecp.ecview.extension.model.extension.YGridLayout;
import org.eclipse.emf.ecp.ecview.extension.model.extension.YLabel;
import org.eclipse.emf.ecp.ecview.extension.model.extension.YNumericField;
import org.eclipse.emf.ecp.ecview.extension.model.extension.YTextField;
import org.eclipse.emf.ecp.ecview.extension.model.extension.util.SimpleExtensionModelFactory;
import org.eclipse.emf.ecp.ecview.ui.core.editparts.extension.IDecimalFieldEditpart;
import org.eclipse.emf.ecp.ecview.ui.core.editparts.extension.ILabelEditpart;
import org.eclipse.emf.ecp.ecview.ui.core.editparts.extension.INumericFieldEditpart;
import org.eclipse.emf.ecp.ecview.ui.core.editparts.extension.ITextFieldEditpart;
import org.eclipse.emf.ecp.ecview.util.emf.ModelUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.lunifera.runtime.web.ecview.presentation.vaadin.VaadinRenderer;
import org.lunifera.runtime.web.ecview.presentation.vaadin.internal.AbstractFieldWidgetPresenter;
import org.lunifera.runtime.web.ecview.presentation.vaadin.internal.AbstractVaadinWidgetPresenter;
import org.lunifera.runtime.web.ecview.presentation.vaadin.internal.TextFieldPresentation;
import org.lunifera.runtime.web.ecview.presentation.vaadin.tests.model.ValueBean;
import org.lunifera.runtime.web.vaadin.components.fields.DecimalField;
import org.lunifera.runtime.web.vaadin.components.fields.NumberField;
import org.osgi.framework.BundleException;
import org.osgi.service.cm.ConfigurationException;

import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;

/**
 * Tests the {@link TextFieldPresentation}.
 */
@SuppressWarnings("restriction")
public class NumericFieldPresentationTests {

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
		Locale.setDefault(Locale.GERMANY);
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
		YNumericField yText = factory.createNumericField();
		yGridlayout.getElements().add(yText);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		INumericFieldEditpart textEditpart = DelegatingEditPartManager
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
		YNumericField yText = factory.createNumericField();
		yView.setContent(yText);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		INumericFieldEditpart textEditpart = DelegatingEditPartManager
				.getInstance().getEditpart(yText);
		IWidgetPresentation<Component> presentation = textEditpart
				.getPresentation();
		ComponentContainer baseComponentContainer = (ComponentContainer) presentation
				.getWidget();

		NumberField text = (NumberField) unwrapText(baseComponentContainer);
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
		YNumericField yText1 = factory.createNumericField();
		yText1.setCssID("ID_0815");
		yText1.setCssClass("anyOtherClass");
		yLayout.getElements().add(yText1);
		YNumericField yText2 = factory.createNumericField();
		yLayout.getElements().add(yText2);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		INumericFieldEditpart text1Editpart = DelegatingEditPartManager
				.getInstance().getEditpart(yText1);
		INumericFieldEditpart text2Editpart = DelegatingEditPartManager
				.getInstance().getEditpart(yText2);
		IWidgetPresentation<Component> text1Presentation = text1Editpart
				.getPresentation();
		IWidgetPresentation<Component> text2Presentation = text2Editpart
				.getPresentation();
		ComponentContainer text1BaseComponentContainer = (ComponentContainer) text1Presentation
				.getWidget();
		ComponentContainer text2BaseComponentContainer = (ComponentContainer) text2Presentation
				.getWidget();

		NumberField text1 = (NumberField) unwrapText(text1BaseComponentContainer);
		NumberField text2 = (NumberField) unwrapText(text2BaseComponentContainer);

		// assert css class
		assertTrue(text1BaseComponentContainer.getStyleName().contains(
				AbstractVaadinWidgetPresenter.CSS_CLASS_CONTROL_BASE));
		assertTrue(text2BaseComponentContainer.getStyleName().contains(
				AbstractVaadinWidgetPresenter.CSS_CLASS_CONTROL_BASE));

		assertTrue(text1.getStyleName().contains("anyOtherClass"));
		assertTrue(text2.getStyleName().contains(
				AbstractVaadinWidgetPresenter.CSS_CLASS_CONTROL));

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
		YNumericField yText1 = factory.createNumericField();
		yLayout.getElements().add(yText1);
		YNumericField yText2 = factory.createNumericField();
		yLayout.getElements().add(yText2);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		INumericFieldEditpart text1Editpart = DelegatingEditPartManager
				.getInstance().getEditpart(yText1);
		INumericFieldEditpart text2Editpart = DelegatingEditPartManager
				.getInstance().getEditpart(yText2);
		IWidgetPresentation<Component> text1Presentation = text1Editpart
				.getPresentation();
		IWidgetPresentation<Component> text2Presentation = text2Editpart
				.getPresentation();
		ComponentContainer text1BaseComponentContainer = (ComponentContainer) text1Presentation
				.getWidget();
		ComponentContainer text2BaseComponentContainer = (ComponentContainer) text2Presentation
				.getWidget();
		NumberField text1 = (NumberField) unwrapText(text1BaseComponentContainer);
		NumberField text2 = (NumberField) unwrapText(text2BaseComponentContainer);

		// start tests
		//
		assertTrue(text1.isVisible());
		assertTrue(text1.isEnabled());
		assertFalse(text1.isReadOnly());

		assertTrue(text2.isVisible());
		assertTrue(text2.isEnabled());
		assertFalse(text2.isReadOnly());

		yText1.setVisible(false);
		assertFalse(text1.getParent().isVisible());

		yText1.setEnabled(false);
		assertFalse(text1.isEnabled());

		yText1.setEditable(false);
		assertTrue(text1.isReadOnly());

		// target to model
		text1.setReadOnly(false);
		assertTrue(yText1.isEditable());

		yText1.setValue(112233);
		assertEquals("112.233", text1.getValue());

		yText1.setValue(332211);
		assertEquals("332.211", text1.getValue());

		text1.setValue("998.877");
		assertEquals(998877, yText1.getValue());

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
		YNumericField yField1 = factory.createNumericField();
		yLayout.getElements().add(yField1);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		INumericFieldEditpart text1Editpart = DelegatingEditPartManager
				.getInstance().getEditpart(yField1);
		IWidgetPresentation<Component> text1Presentation = text1Editpart
				.getPresentation();
		ComponentContainer text1BaseComponentContainer = (ComponentContainer) text1Presentation
				.getWidget();
		NumberField field1 = (NumberField) unwrapText(text1BaseComponentContainer);

		// start tests
		//
		YBindingSet yBindingSet = yView.getOrCreateBindingSet();

		YBeanValueBindingEndpoint beanBinding = factory
				.createBeanBindingEndpoint();
		ValueBean bean = new ValueBean(998877);
		beanBinding.setPropertyPath("intValue");
		beanBinding.setBean(bean);
		yBindingSet.addBinding(yField1.createValueEndpoint(), beanBinding);
		assertEquals("998.877", field1.getValue());
		assertEquals(998877, yField1.getValue());

		// bean = new ValueBean("Huhu11");
		// beanBinding.setPropertyPath("value");
		// TODO Setting a bean later does not cause any sideeffects. See
		// BeanBindingEndpointEditpart. The binding for the bean is not
		// refreshed.
		// beanBinding.setBean(bean);
		// assertEquals("Huhu11", text1.getValue());
		// assertEquals("Huhu11", yText1.getValue());

		bean.setIntValue(223344);
		assertEquals("223.344", field1.getValue());
		assertEquals(223344, yField1.getValue());

		field1.setValue("445.566");
		assertEquals(445566, bean.getIntValue());
		assertEquals(445566, yField1.getValue());

		yField1.setValue(778899);
		assertEquals(778899, bean.getIntValue());
		assertEquals("778.899", field1.getValue());
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
		YNumericField yText = factory.createNumericField();
		yGridlayout.getElements().add(yText);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		INumericFieldEditpart textEditpart = DelegatingEditPartManager
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
	public void testMarkNegative() throws ContextException {
		YView yView = factory.createView();
		YGridLayout yGridlayout = factory.createGridLayout();
		yView.setContent(yGridlayout);
		YNumericField yText = factory.createNumericField();

		yGridlayout.getElements().add(yText);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		INumericFieldEditpart textEditpart = DelegatingEditPartManager
				.getInstance().getEditpart(yText);
		IWidgetPresentation<Component> presentation = textEditpart
				.getPresentation();
		ComponentContainer baseComponentContainer = (ComponentContainer) presentation
				.getWidget();
		NumberField field = (NumberField) unwrapText(baseComponentContainer);

		yText.setValue(99);
		assertEquals("99", field.getValue());
		assertFalse(field.getStyleName().contains("lun-negative-value"));
		yText.setValue(-99);
		assertEquals("-99", field.getValue());
		assertTrue(field.getStyleName().contains("lun-negative-value"));

	}

	/**
	 * Test grouping of decimals
	 * 
	 * @throws ContextException
	 */
	@Test
	public void testGrouping() throws ContextException {
		YView yView = factory.createView();
		YGridLayout yLayout = factory.createGridLayout();
		yView.setContent(yLayout);
		YNumericField yText1 = factory.createNumericField();
		yLayout.getElements().add(yText1);
		YDecimalDatatype dt1 = factory.createDecimalDatatype();
		dt1.setGrouping(true);
		yText1.setDatatype(dt1);
		YNumericField yText2 = factory.createNumericField();
		yLayout.getElements().add(yText2);
		YDecimalDatatype dt2 = factory.createDecimalDatatype();
		dt2.setGrouping(false);
		yText2.setDatatype(dt2);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		INumericFieldEditpart text1Editpart = DelegatingEditPartManager
				.getInstance().getEditpart(yText1);
		INumericFieldEditpart text2Editpart = DelegatingEditPartManager
				.getInstance().getEditpart(yText2);
		IWidgetPresentation<Component> text1Presentation = text1Editpart
				.getPresentation();
		IWidgetPresentation<Component> text2Presentation = text2Editpart
				.getPresentation();
		ComponentContainer text1BaseComponentContainer = (ComponentContainer) text1Presentation
				.getWidget();
		ComponentContainer text2BaseComponentContainer = (ComponentContainer) text2Presentation
				.getWidget();
		NumberField text1 = (NumberField) unwrapText(text1BaseComponentContainer);
		NumberField text2 = (NumberField) unwrapText(text2BaseComponentContainer);

		yText1.setValue(112233);
		assertEquals("112.233", text1.getValue());

		yText2.setValue(332211);
		assertEquals("332211", text2.getValue());

	}

	@Test
	public void testGrouping_ByChangingDatatype() throws ContextException {
		YView yView = factory.createView();
		YGridLayout yLayout = factory.createGridLayout();
		yView.setContent(yLayout);
		YNumericField yField = factory.createNumericField();
		yLayout.getElements().add(yField);
		YDecimalDatatype dt1 = factory.createDecimalDatatype();
		dt1.setGrouping(true);
		YDecimalDatatype dt2 = factory.createDecimalDatatype();
		dt2.setPrecision(1);
		dt2.setGrouping(false);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		INumericFieldEditpart text1Editpart = DelegatingEditPartManager
				.getInstance().getEditpart(yField);
		IWidgetPresentation<Component> text1Presentation = text1Editpart
				.getPresentation();
		ComponentContainer text1BaseComponentContainer = (ComponentContainer) text1Presentation
				.getWidget();
		NumberField field = (NumberField) unwrapText(text1BaseComponentContainer);

		// start tests
		yField.setDatatype(dt1);
		yField.setValue(112233);
		assertEquals("112.233", field.getValue());
		assertEquals(112233, yField.getValue(), 0);

		yField.setDatatype(dt2);
		assertEquals("112233", field.getValue());
		assertEquals(112233, yField.getValue(), 0);

		yField.setValue(4567);
		assertEquals("4567", field.getValue());
		assertEquals(4567, yField.getValue(), 0);
	}

	@Test
	public void testMarkNegative_ByChangingDatatype() throws ContextException {
		YView yView = factory.createView();
		YGridLayout yLayout = factory.createGridLayout();
		yView.setContent(yLayout);
		YNumericField yField = factory.createNumericField();
		yLayout.getElements().add(yField);
		YDecimalDatatype dt1 = factory.createDecimalDatatype();
		dt1.setMarkNegative(true);
		YDecimalDatatype dt2 = factory.createDecimalDatatype();
		dt2.setPrecision(1);
		dt2.setMarkNegative(false);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		INumericFieldEditpart text1Editpart = DelegatingEditPartManager
				.getInstance().getEditpart(yField);
		IWidgetPresentation<Component> text1Presentation = text1Editpart
				.getPresentation();
		ComponentContainer text1BaseComponentContainer = (ComponentContainer) text1Presentation
				.getWidget();
		NumberField field = (NumberField) unwrapText(text1BaseComponentContainer);

		// start tests
		yField.setDatatype(dt1);
		yField.setValue(112233);
		assertEquals("112.233", field.getValue());
		assertEquals(112233, yField.getValue(), 0);
		assertFalse(field.getStyleName().contains("lun-negative-value"));

		yField.setValue(-112233);
		assertEquals("-112.233", field.getValue());
		assertEquals(-112233, yField.getValue(), 0);
		assertTrue(field.getStyleName().contains("lun-negative-value"));

		yField.setDatatype(dt2);
		assertEquals("-112.233", field.getValue());
		assertEquals(-112233, yField.getValue(), 0);
		assertFalse(field.getStyleName().contains("lun-negative-value"));

	}

	@Test
	public void test_i18n() throws ContextException {

		// switch the global locale to german
		Locale.setDefault(Locale.GERMAN);

		YView yView = factory.createView();
		YGridLayout yGridlayout = factory.createGridLayout();
		yView.setContent(yGridlayout);
		YNumericField yText = factory.createNumericField();
		yText.setValue(123456789);
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
		INumericFieldEditpart textEditpart = DelegatingEditPartManager
				.getInstance().getEditpart(yText);
		AbstractFieldWidgetPresenter<Component> presentation = textEditpart
				.getPresentation();

		NumberField textField = (NumberField) unwrapText(presentation
				.getWidget());
		assertEquals("Alter", presentation.getWidget().getCaption());
		assertEquals("123.456.789", textField.getValue());

		context.setLocale(Locale.ENGLISH);
		assertEquals("Age", presentation.getWidget().getCaption());
		assertEquals("123,456,789", textField.getValue());
	}

	@Test
	// BEGIN SUPRESS CATCH EXCEPTION
	public void test_Readonly_Binding() throws Exception {
		// END SUPRESS CATCH EXCEPTION
		// build the view model
		YView yView = factory.createView();
		YGridLayout yLayout = factory.createGridLayout();
		yView.setContent(yLayout);
		YNumericField yText = factory.createNumericField();
		yLayout.getElements().add(yText);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		INumericFieldEditpart editpart = DelegatingEditPartManager
				.getInstance().getEditpart(yText);
		IWidgetPresentation<Component> presentation = editpart
				.getPresentation();
		ComponentContainer baseComponentContainer = (ComponentContainer) presentation
				.getWidget();
		NumberField textField = (NumberField) unwrapText(presentation
				.getWidget());

		ValueBean bean = new ValueBean(false);
		YBeanValueBindingEndpoint yBeanBinding = factory
				.createBeanBindingEndpoint();
		yBeanBinding.setBean(bean);
		yBeanBinding.setPropertyPath("boolValue");
		YBindingSet yBindingSet = yView.getOrCreateBindingSet();
		yBindingSet.addBinding(yText.createEditableEndpoint(), yBeanBinding);

		// test binding
		assertFalse(yText.isEditable());
		assertFalse(!textField.isReadOnly());
		assertFalse(bean.isBoolValue());

		bean.setBoolValue(true);
		assertTrue(yText.isEditable());
		assertTrue(!textField.isReadOnly());
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
		YNumericField yText = factory.createNumericField();
		yLayout.getElements().add(yText);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		INumericFieldEditpart editpart = DelegatingEditPartManager
				.getInstance().getEditpart(yText);
		IWidgetPresentation<Component> presentation = editpart
				.getPresentation();
		ComponentContainer textBaseComponentContainer = (ComponentContainer) presentation
				.getWidget();
		NumberField textField = (NumberField) unwrapText(presentation
				.getWidget());

		ValueBean bean = new ValueBean(false);
		YBeanValueBindingEndpoint yBeanBinding = factory
				.createBeanBindingEndpoint();
		yBeanBinding.setBean(bean);
		yBeanBinding.setPropertyPath("boolValue");
		YBindingSet yBindingSet = yView.getOrCreateBindingSet();
		yBindingSet.addBinding(yText.createVisibleEndpoint(), yBeanBinding);

		// test binding
		assertFalse(yText.isVisible());
		assertFalse(textField.getParent().isVisible());
		assertFalse(bean.isBoolValue());

		bean.setBoolValue(true);
		assertTrue(yText.isVisible());
		assertTrue(textField.isVisible());
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
		YNumericField yText = factory.createNumericField();
		yLayout.getElements().add(yText);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		INumericFieldEditpart editpart = DelegatingEditPartManager
				.getInstance().getEditpart(yText);
		IWidgetPresentation<Component> presentation = editpart
				.getPresentation();
		ComponentContainer textBaseComponentContainer = (ComponentContainer) presentation
				.getWidget();
		NumberField textField = (NumberField) unwrapText(presentation
				.getWidget());

		ValueBean bean = new ValueBean(false);
		YBeanValueBindingEndpoint yBeanBinding = factory
				.createBeanBindingEndpoint();
		yBeanBinding.setBean(bean);
		yBeanBinding.setPropertyPath("boolValue");
		YBindingSet yBindingSet = yView.getOrCreateBindingSet();
		yBindingSet.addBinding(yText.createEnabledEndpoint(), yBeanBinding);

		// test binding
		assertFalse(yText.isEnabled());
		assertFalse(textField.isEnabled());
		assertFalse(bean.isBoolValue());

		bean.setBoolValue(true);
		assertTrue(yText.isEnabled());
		assertTrue(textField.isEnabled());
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
