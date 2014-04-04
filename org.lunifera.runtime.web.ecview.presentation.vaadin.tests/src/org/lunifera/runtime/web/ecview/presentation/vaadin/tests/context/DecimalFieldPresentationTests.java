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
package org.lunifera.runtime.web.ecview.presentation.vaadin.tests.context;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;
import java.util.Locale;

import org.eclipse.core.databinding.Binding;
import org.eclipse.emf.ecp.ecview.common.context.ContextException;
import org.eclipse.emf.ecp.ecview.common.editpart.DelegatingEditPartManager;
import org.eclipse.emf.ecp.ecview.common.editpart.IElementEditpart;
import org.eclipse.emf.ecp.ecview.common.editpart.IEmbeddableEditpart;
import org.eclipse.emf.ecp.ecview.common.editpart.IViewEditpart;
import org.eclipse.emf.ecp.ecview.common.model.binding.YBeanBindingEndpoint;
import org.eclipse.emf.ecp.ecview.common.model.binding.YBindingSet;
import org.eclipse.emf.ecp.ecview.common.model.core.YElement;
import org.eclipse.emf.ecp.ecview.common.model.core.YEmbeddableValueEndpoint;
import org.eclipse.emf.ecp.ecview.common.model.core.YView;
import org.eclipse.emf.ecp.ecview.common.presentation.IWidgetPresentation;
import org.eclipse.emf.ecp.ecview.extension.model.datatypes.YDecimalDatatype;
import org.eclipse.emf.ecp.ecview.extension.model.extension.YDecimalField;
import org.eclipse.emf.ecp.ecview.extension.model.extension.YGridLayout;
import org.eclipse.emf.ecp.ecview.extension.model.extension.util.SimpleExtensionModelFactory;
import org.eclipse.emf.ecp.ecview.ui.core.editparts.extension.IDecimalFieldEditpart;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.lunifera.runtime.web.ecview.presentation.vaadin.VaadinRenderer;
import org.lunifera.runtime.web.ecview.presentation.vaadin.internal.AbstractVaadinWidgetPresenter;
import org.lunifera.runtime.web.ecview.presentation.vaadin.internal.TextFieldPresentation;
import org.lunifera.runtime.web.ecview.presentation.vaadin.internal.binding.BindingUtil;
import org.lunifera.runtime.web.ecview.presentation.vaadin.tests.model.ValueBean;
import org.lunifera.runtime.web.vaadin.components.fields.DecimalField;
import org.osgi.framework.BundleException;
import org.osgi.service.cm.ConfigurationException;

import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.UI;

/**
 * Tests the {@link TextFieldPresentation}.
 */
@SuppressWarnings("restriction")
public class DecimalFieldPresentationTests {

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
		YDecimalField yText = factory.createDecimalField();
		yGridlayout.getElements().add(yText);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		IDecimalFieldEditpart textEditpart = DelegatingEditPartManager
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
		YDecimalField yText = factory.createDecimalField();
		yView.setContent(yText);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		IDecimalFieldEditpart textEditpart = DelegatingEditPartManager
				.getInstance().getEditpart(yText);
		IWidgetPresentation<Component> presentation = textEditpart
				.getPresentation();
		ComponentContainer baseComponentContainer = (ComponentContainer) presentation
				.getWidget();

		DecimalField text = (DecimalField) unwrapText(baseComponentContainer);
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
		YDecimalField yText1 = factory.createDecimalField();
		yText1.setCssID("ID_0815");
		yText1.setCssClass("anyOtherClass");
		yLayout.getElements().add(yText1);
		YDecimalField yText2 = factory.createDecimalField();
		yLayout.getElements().add(yText2);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		IDecimalFieldEditpart text1Editpart = DelegatingEditPartManager
				.getInstance().getEditpart(yText1);
		IDecimalFieldEditpart text2Editpart = DelegatingEditPartManager
				.getInstance().getEditpart(yText2);
		IWidgetPresentation<Component> text1Presentation = text1Editpart
				.getPresentation();
		IWidgetPresentation<Component> text2Presentation = text2Editpart
				.getPresentation();
		ComponentContainer text1BaseComponentContainer = (ComponentContainer) text1Presentation
				.getWidget();
		ComponentContainer text2BaseComponentContainer = (ComponentContainer) text2Presentation
				.getWidget();

		DecimalField text1 = (DecimalField) unwrapText(text1BaseComponentContainer);
		DecimalField text2 = (DecimalField) unwrapText(text2BaseComponentContainer);

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
		YDecimalField yText1 = factory.createDecimalField();
		yLayout.getElements().add(yText1);
		YDecimalField yText2 = factory.createDecimalField();
		yLayout.getElements().add(yText2);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		IDecimalFieldEditpart text1Editpart = DelegatingEditPartManager
				.getInstance().getEditpart(yText1);
		IDecimalFieldEditpart text2Editpart = DelegatingEditPartManager
				.getInstance().getEditpart(yText2);
		IWidgetPresentation<Component> text1Presentation = text1Editpart
				.getPresentation();
		IWidgetPresentation<Component> text2Presentation = text2Editpart
				.getPresentation();
		ComponentContainer text1BaseComponentContainer = (ComponentContainer) text1Presentation
				.getWidget();
		ComponentContainer text2BaseComponentContainer = (ComponentContainer) text2Presentation
				.getWidget();
		DecimalField text1 = (DecimalField) unwrapText(text1BaseComponentContainer);
		DecimalField text2 = (DecimalField) unwrapText(text2BaseComponentContainer);

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

		yText1.setValue(1122.33);
		assertEquals("1.122,33", text1.getValue());

		yText1.setValue(3322.11);
		assertEquals("3.322,11", text1.getValue());

		text1.setValue("9.988,77");
		assertEquals(9988.77, yText1.getValue(), 0);
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
		YDecimalField yField1 = factory.createDecimalField();
		yLayout.getElements().add(yField1);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		IDecimalFieldEditpart text1Editpart = DelegatingEditPartManager
				.getInstance().getEditpart(yField1);
		IWidgetPresentation<Component> text1Presentation = text1Editpart
				.getPresentation();
		ComponentContainer text1BaseComponentContainer = (ComponentContainer) text1Presentation
				.getWidget();
		DecimalField field1 = (DecimalField) unwrapText(text1BaseComponentContainer);

		// start tests
		//
		YBindingSet yBindingSet = yView.getOrCreateBindingSet();

		YBeanBindingEndpoint beanBinding = factory.createBeanBindingEndpoint();
		ValueBean bean = new ValueBean(9988.77);
		beanBinding.setPropertyPath("doubleValue");
		beanBinding.setBean(bean);
		yBindingSet.addBinding(yField1.createValueEndpoint(), beanBinding);
		assertEquals("9.988,77", field1.getValue());
		assertEquals(9988.77, yField1.getValue(), 0);

		// bean = new ValueBean("Huhu11");
		// beanBinding.setPropertyPath("value");
		// TODO Setting a bean later does not cause any sideeffects. See
		// BeanBindingEndpointEditpart. The binding for the bean is not
		// refreshed.
		// beanBinding.setBean(bean);
		// assertEquals("Huhu11", text1.getValue());
		// assertEquals("Huhu11", yText1.getValue());

		bean.setDoubleValue(2233.44);
		assertEquals("2.233,44", field1.getValue());
		assertEquals(2233.44, yField1.getValue(), 0);

		field1.setValue("4.455,66");
		assertEquals(4455.66, bean.getDoubleValue(), 0);
		assertEquals(4455.66, yField1.getValue(), 0);

		yField1.setValue(7788.99);
		assertEquals(7788.99, bean.getDoubleValue(), 0);
		assertEquals("7.788,99", field1.getValue());
	}

	@Test
	public void test_MarkNegative() {
		// Assert.fail("Implement");
	}

	@Test
	public void test_Grouping() throws ContextException {

		// END SUPRESS CATCH EXCEPTION
		// build the view model
		// ...> yView
		// ......> yText
		YView yView = factory.createView();
		YGridLayout yLayout = factory.createGridLayout();
		yView.setContent(yLayout);
		YDecimalField yField1 = factory.createDecimalField();
		yLayout.getElements().add(yField1);
		YDecimalDatatype dt1 = factory.createDecimalDatatype();
		dt1.setGrouping(true);
		yField1.setDatatype(dt1);

		YDecimalField yField2 = factory.createDecimalField();
		yLayout.getElements().add(yField2);
		YDecimalDatatype dt2 = factory.createDecimalDatatype();
		dt2.setGrouping(false);
		yField2.setDatatype(dt2);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		IDecimalFieldEditpart text1Editpart = DelegatingEditPartManager
				.getInstance().getEditpart(yField1);
		IWidgetPresentation<Component> text1Presentation = text1Editpart
				.getPresentation();
		ComponentContainer text1BaseComponentContainer = (ComponentContainer) text1Presentation
				.getWidget();
		DecimalField field1 = (DecimalField) unwrapText(text1BaseComponentContainer);

		IDecimalFieldEditpart text2Editpart = DelegatingEditPartManager
				.getInstance().getEditpart(yField2);
		IWidgetPresentation<Component> text2Presentation = text2Editpart
				.getPresentation();
		ComponentContainer text2BaseComponentContainer = (ComponentContainer) text2Presentation
				.getWidget();
		DecimalField field2 = (DecimalField) unwrapText(text2BaseComponentContainer);

		// start tests
		//
		YBindingSet yBindingSet = yView.getOrCreateBindingSet();

		YBeanBindingEndpoint beanBinding = factory.createBeanBindingEndpoint();
		ValueBean bean = new ValueBean(9988.77);
		beanBinding.setPropertyPath("doubleValue");
		beanBinding.setBean(bean);
		yBindingSet.addBinding(yField1.createValueEndpoint(), beanBinding);
		assertEquals("9.988,77", field1.getValue());
		assertEquals(9988.77, yField1.getValue(), 0);

		YBeanBindingEndpoint beanBinding2 = factory.createBeanBindingEndpoint();
		ValueBean bean2 = new ValueBean(9988.77);
		beanBinding2.setPropertyPath("doubleValue");
		beanBinding2.setBean(bean2);
		yBindingSet.addBinding(yField2.createValueEndpoint(), beanBinding2);
		assertEquals("9988,77", field2.getValue());
		assertEquals(9988.77, yField2.getValue(), 0);

		bean.setDoubleValue(2233.44);
		assertEquals("2.233,44", field1.getValue());
		assertEquals(2233.44, yField1.getValue(), 0);

		bean2.setDoubleValue(2233.44);
		assertEquals("2233,44", field2.getValue());
		assertEquals(2233.44, yField2.getValue(), 0);

		field1.setValue("4.455,66");
		assertEquals(4455.66, bean.getDoubleValue(), 0);
		assertEquals(4455.66, yField1.getValue(), 0);

		// grouped value in ungrouped field is not converted
		field2.setValue("4.455,66");
		// assertEquals("4455,66", field2.getValue());
		// assertEquals(4455.66, bean2.getDoubleValue(), 0);
		// assertEquals(4455.66, yField2.getValue(), 0);

		field2.setValue("4455,66");
		assertEquals("4455,66", field2.getValue());
		assertEquals(4455.66, bean2.getDoubleValue(), 0);
		assertEquals(4455.66, yField2.getValue(), 0);

		yField1.setValue(7788.99);
		assertEquals(7788.99, bean.getDoubleValue(), 0);
		assertEquals("7.788,99", field1.getValue());

		yField2.setValue(7788.99);
		assertEquals(7788.99, bean2.getDoubleValue(), 0);
		assertEquals("7788,99", field2.getValue());

	}

	@Test
	public void test_Precision() throws ContextException {
		YView yView = factory.createView();
		YGridLayout yLayout = factory.createGridLayout();
		yView.setContent(yLayout);
		YDecimalField yField1 = factory.createDecimalField();
		yLayout.getElements().add(yField1);
		YDecimalDatatype dt1 = factory.createDecimalDatatype();
		dt1.setPrecision(3);
		yField1.setDatatype(dt1);

		YDecimalField yField2 = factory.createDecimalField();
		yLayout.getElements().add(yField2);
		YDecimalDatatype dt2 = factory.createDecimalDatatype();
		dt2.setPrecision(0);
		yField2.setDatatype(dt2);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		IDecimalFieldEditpart text1Editpart = DelegatingEditPartManager
				.getInstance().getEditpart(yField1);
		IWidgetPresentation<Component> text1Presentation = text1Editpart
				.getPresentation();
		ComponentContainer text1BaseComponentContainer = (ComponentContainer) text1Presentation
				.getWidget();
		DecimalField field1 = (DecimalField) unwrapText(text1BaseComponentContainer);

		IDecimalFieldEditpart text2Editpart = DelegatingEditPartManager
				.getInstance().getEditpart(yField2);
		IWidgetPresentation<Component> text2Presentation = text2Editpart
				.getPresentation();
		ComponentContainer text2BaseComponentContainer = (ComponentContainer) text2Presentation
				.getWidget();
		DecimalField field2 = (DecimalField) unwrapText(text2BaseComponentContainer);

		// start tests
		//
		YBindingSet yBindingSet = yView.getOrCreateBindingSet();

		YBeanBindingEndpoint beanBinding = factory.createBeanBindingEndpoint();
		ValueBean bean = new ValueBean(9988.77);
		beanBinding.setPropertyPath("doubleValue");
		beanBinding.setBean(bean);
		yBindingSet.addBinding(yField1.createValueEndpoint(), beanBinding);
		assertEquals("9.988,770", field1.getValue());
		assertEquals(9988.77, yField1.getValue(), 0);

		YBeanBindingEndpoint beanBinding2 = factory.createBeanBindingEndpoint();
		ValueBean bean2 = new ValueBean(9988.77);
		beanBinding2.setPropertyPath("doubleValue");
		beanBinding2.setBean(bean2);
		yBindingSet.addBinding(yField2.createValueEndpoint(), beanBinding2);
		assertEquals("9.989", field2.getValue());
		assertEquals(9989, yField2.getValue(), 0);
		// rounded value should be written back to bean itself
		assertEquals(9989, bean2.getDoubleValue(), 0);

		bean.setDoubleValue(2233.44);
		assertEquals("2.233,440", field1.getValue());
		assertEquals(2233.44, yField1.getValue(), 0);

		bean2.setDoubleValue(2233.44);
		assertEquals("2.233", field2.getValue());
		assertEquals(2233, yField2.getValue(), 0);
		// rounded value should be written back to bean itself
		assertEquals(2233, bean2.getDoubleValue(), 0);

		field1.setValue("4.455,66");
		assertEquals(4455.660, bean.getDoubleValue(), 0);
		assertEquals(4455.66, yField1.getValue(), 0);

		field2.setValue("4.455,66");
		assertEquals("4.456", field2.getValue());
		assertEquals(4456, yField2.getValue(), 0);
		assertEquals(4456, bean2.getDoubleValue(), 0);

		yField1.setValue(7788.99);
		assertEquals(7788.99, bean.getDoubleValue(), 0);
		assertEquals("7.788,990", field1.getValue());

		yField2.setValue(7788.99);
		assertEquals(7789, bean2.getDoubleValue(), 0);
		assertEquals("7.789", field2.getValue());
		assertEquals(7789, yField2.getValue(), 0);

	}

	@Test
	public void test_BindingIsDisposed() throws ContextException {
		YView yView = factory.createView();
		YDecimalField yText = factory.createDecimalField();
		yView.setContent(yText);
		YDecimalDatatype dt = factory.createDecimalDatatype();
		dt.setGrouping(true);
		yText.setDatatype(dt);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		IDecimalFieldEditpart textEditpart = DelegatingEditPartManager
				.getInstance().getEditpart(yText);
		IWidgetPresentation<Component> presentation = textEditpart
				.getPresentation();
		ComponentContainer baseComponentContainer = (ComponentContainer) presentation
				.getWidget();

		DecimalField text = (DecimalField) unwrapText(baseComponentContainer);
		assertEquals(1, baseComponentContainer.getComponentCount());

		YBindingSet yBindingSet = yView.getOrCreateBindingSet();

		YBeanBindingEndpoint beanBinding = factory.createBeanBindingEndpoint();
		ValueBean bean = new ValueBean(123.0);
		beanBinding.setPropertyPath("doubleValue");
		beanBinding.setBean(bean);

		YEmbeddableValueEndpoint valueEndpoint = yText.createValueEndpoint();
		yBindingSet.addBinding(valueEndpoint, beanBinding);
		assertEquals("123,00", text.getValue());
		assertEquals(123, bean.getDoubleValue(), 0);

		IElementEditpart beanBindingEditPart = DelegatingEditPartManager
				.getInstance().getEditpart(beanBinding);
		IElementEditpart valueEndpointEditPart = DelegatingEditPartManager
				.getInstance().getEditpart(valueEndpoint);

		Binding binding = BindingUtil.getValueBinding(yText);
		assertFalse(textEditpart.isDisposed());
		assertFalse(presentation.isDisposed());
		assertFalse(binding.isDisposed());
		assertFalse(beanBindingEditPart.isDisposed());
		assertFalse(valueEndpointEditPart.isDisposed());

		textEditpart.dispose();
		assertTrue(textEditpart.isDisposed());
		assertTrue(presentation.isDisposed());
		assertTrue(binding.isDisposed());
		assertTrue(beanBindingEditPart.isDisposed());
		assertTrue(valueEndpointEditPart.isDisposed());

	}

	@Test
	public void test_BindingWithUnrender() throws ContextException {
		YView yView = factory.createView();
		YDecimalField yText = factory.createDecimalField();
		yView.setContent(yText);
		YDecimalDatatype dt = factory.createDecimalDatatype();
		dt.setGrouping(true);
		yText.setDatatype(dt);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		IDecimalFieldEditpart textEditpart = DelegatingEditPartManager
				.getInstance().getEditpart(yText);
		IWidgetPresentation<Component> presentation = textEditpart
				.getPresentation();
		ComponentContainer baseComponentContainer = (ComponentContainer) presentation
				.getWidget();

		DecimalField text = (DecimalField) unwrapText(baseComponentContainer);
		assertEquals(1, baseComponentContainer.getComponentCount());

		YBindingSet yBindingSet = yView.getOrCreateBindingSet();

		YBeanBindingEndpoint beanBinding = factory.createBeanBindingEndpoint();
		ValueBean bean = new ValueBean(123.0);
		beanBinding.setPropertyPath("doubleValue");
		beanBinding.setBean(bean);

		YEmbeddableValueEndpoint valueEndpoint = yText.createValueEndpoint();
		yBindingSet.addBinding(valueEndpoint, beanBinding);
		assertEquals("123,00", text.getValue());
		assertEquals(123, bean.getDoubleValue(), 0);

		presentation.unrender();
		Assert.fail();

		IElementEditpart beanBindingEditPart = DelegatingEditPartManager
				.getInstance().getEditpart(beanBinding);
		IElementEditpart valueEndpointEditPart = DelegatingEditPartManager
				.getInstance().getEditpart(valueEndpoint);

		Binding binding = BindingUtil.getValueBinding(yText);
		assertFalse(textEditpart.isDisposed());
		assertFalse(presentation.isDisposed());
		assertFalse(binding.isDisposed());
		assertFalse(beanBindingEditPart.isDisposed());
		assertFalse(valueEndpointEditPart.isDisposed());

		textEditpart.dispose();
		assertTrue(textEditpart.isDisposed());
		assertTrue(presentation.isDisposed());
		assertTrue(binding.isDisposed());
		assertTrue(beanBindingEditPart.isDisposed());
		assertTrue(valueEndpointEditPart.isDisposed());

	}

	@Test
	public void test_Binding_RemoveAndAddElement() throws ContextException {
		YView yView = factory.createView();
		YDecimalField yText = factory.createDecimalField();
		yView.setContent(yText);
		YDecimalDatatype dt = factory.createDecimalDatatype();
		dt.setGrouping(true);
		yText.setDatatype(dt);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		IDecimalFieldEditpart textEditpart = DelegatingEditPartManager
				.getInstance().getEditpart(yText);
		IWidgetPresentation<Component> presentation = textEditpart
				.getPresentation();
		ComponentContainer baseComponentContainer = (ComponentContainer) presentation
				.getWidget();

		DecimalField text = (DecimalField) unwrapText(baseComponentContainer);
		assertEquals(1, baseComponentContainer.getComponentCount());

		YBindingSet yBindingSet = yView.getOrCreateBindingSet();

		YBeanBindingEndpoint beanBinding = factory.createBeanBindingEndpoint();
		ValueBean bean = new ValueBean(123.0);
		beanBinding.setPropertyPath("doubleValue");
		beanBinding.setBean(bean);

		YEmbeddableValueEndpoint valueEndpoint = yText.createValueEndpoint();
		yBindingSet.addBinding(valueEndpoint, beanBinding);
		assertEquals("123,00", text.getValue());
		assertEquals(123, bean.getDoubleValue(), 0);

		// force unrender
		yView.setContent(null);

		// force re-render
		yView.setContent(yText);

		Assert.fail();

		IElementEditpart beanBindingEditPart = DelegatingEditPartManager
				.getInstance().getEditpart(beanBinding);
		IElementEditpart valueEndpointEditPart = DelegatingEditPartManager
				.getInstance().getEditpart(valueEndpoint);

		Binding binding = BindingUtil.getValueBinding(yText);
		assertFalse(textEditpart.isDisposed());
		assertFalse(presentation.isDisposed());
		assertFalse(binding.isDisposed());
		assertFalse(beanBindingEditPart.isDisposed());
		assertFalse(valueEndpointEditPart.isDisposed());

		textEditpart.dispose();
		assertTrue(textEditpart.isDisposed());
		assertTrue(presentation.isDisposed());
		assertTrue(binding.isDisposed());
		assertTrue(beanBindingEditPart.isDisposed());
		assertTrue(valueEndpointEditPart.isDisposed());

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
