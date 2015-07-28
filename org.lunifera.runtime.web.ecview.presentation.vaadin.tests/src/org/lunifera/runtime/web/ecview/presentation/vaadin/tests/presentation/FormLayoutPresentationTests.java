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

import java.util.Locale;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.lunifera.ecview.core.common.context.ContextException;
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
import org.lunifera.ecview.core.extension.model.extension.YFormLayout;
import org.lunifera.ecview.core.extension.model.extension.YTextField;
import org.lunifera.ecview.core.extension.model.extension.util.SimpleExtensionModelFactory;
import org.lunifera.ecview.core.ui.core.editparts.extension.ICheckboxEditpart;
import org.lunifera.ecview.core.ui.core.editparts.extension.IFormLayoutEditpart;
import org.lunifera.ecview.core.ui.core.editparts.extension.ITextFieldEditpart;
import org.lunifera.runtime.web.ecview.presentation.vaadin.VaadinRenderer;
import org.lunifera.runtime.web.ecview.presentation.vaadin.common.AbstractVaadinWidgetPresenter;
import org.lunifera.runtime.web.ecview.presentation.vaadin.internal.CheckBoxPresentation;
import org.lunifera.runtime.web.ecview.presentation.vaadin.internal.FormLayoutPresentation;
import org.lunifera.runtime.web.ecview.presentation.vaadin.internal.TextFieldPresentation;
import org.lunifera.runtime.web.ecview.presentation.vaadin.internal.ViewPresentation;
import org.lunifera.runtime.web.ecview.presentation.vaadin.tests.model.ValueBean;
import org.osgi.framework.BundleException;
import org.osgi.service.cm.ConfigurationException;

import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.UI;

/**
 * Tests the {@link FormLayoutPresentation}.
 */
@SuppressWarnings("restriction")
public class FormLayoutPresentationTests {

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

		// switch the global locale to german
		Locale.setDefault(Locale.GERMAN);

		YView yView = factory.createView();
		YFormLayout yLayout = factory.createFormLayout();
		yView.setContent(yLayout);
		YCheckBox yCheckBox = factory.createCheckBox();
		yLayout.getElements().add(yCheckBox);
		YTextField yTextField = factory.createTextField();
		yLayout.getElements().add(yTextField);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);
		IFormLayoutEditpart layoutEP = DelegatingEditPartManager.getInstance()
				.getEditpart(yLayout);
		ICheckboxEditpart checkBoxEP = DelegatingEditPartManager.getInstance()
				.getEditpart(yCheckBox);
		ITextFieldEditpart textEP = DelegatingEditPartManager.getInstance()
				.getEditpart(yTextField);
		FormLayoutPresentation layoutPres = layoutEP.getPresentation();
		CheckBoxPresentation checkBoxPres = checkBoxEP.getPresentation();
		TextFieldPresentation textPres = textEP.getPresentation();

		assertTrue(layoutPres.isRendered());
		assertTrue(textPres.isRendered());
		assertTrue(checkBoxPres.isRendered());

		yView.setContent(null);
		assertTrue(layoutEP.isDisposed());
		assertTrue(checkBoxEP.isDisposed());
		assertTrue(textEP.isDisposed());

		assertFalse(layoutPres.isRendered());
		assertFalse(textPres.isRendered());
		assertFalse(checkBoxPres.isRendered());

		yView.setContent(yLayout);

		layoutEP = DelegatingEditPartManager.getInstance().getEditpart(yLayout);
		checkBoxEP = DelegatingEditPartManager.getInstance().getEditpart(
				yCheckBox);
		textEP = DelegatingEditPartManager.getInstance()
				.getEditpart(yTextField);
		assertTrue(layoutEP.isRendered());
		assertTrue(textEP.isRendered());
		assertTrue(checkBoxEP.isRendered());

	}

	@Test
	// BEGIN SUPRESS CATCH EXCEPTION
	public void test_removeField_AddAndInsert_byModel() throws Exception {

		// switch the global locale to german
		Locale.setDefault(Locale.GERMAN);

		YView yView = factory.createView();
		YFormLayout yLayout = factory.createFormLayout();
		yView.setContent(yLayout);
		YCheckBox yCheckBox = factory.createCheckBox();
		yLayout.getElements().add(yCheckBox);
		YTextField yTextField = factory.createTextField();
		yLayout.getElements().add(yTextField);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);
		IFormLayoutEditpart layoutEP = DelegatingEditPartManager.getInstance()
				.getEditpart(yLayout);
		ICheckboxEditpart checkBoxEP = DelegatingEditPartManager.getInstance()
				.getEditpart(yCheckBox);
		ITextFieldEditpart textEP = DelegatingEditPartManager.getInstance()
				.getEditpart(yTextField);
		FormLayoutPresentation layoutPres = layoutEP.getPresentation();
		CheckBoxPresentation checkBoxPres = checkBoxEP.getPresentation();
		TextFieldPresentation textPres = textEP.getPresentation();

		FormLayout layout = (FormLayout) layoutPres.getWidget();

		assertTrue(layoutPres.isRendered());
		assertTrue(checkBoxPres.isRendered());
		assertTrue(textPres.isRendered());

		Assert.assertSame(checkBoxPres.getWidget(), layout.getComponent(0));
		Assert.assertSame(textPres.getWidget(), layout.getComponent(1));
		assertEquals(2, layout.getComponentCount());

		yLayout.removeElement(yCheckBox);

		assertTrue(layoutPres.isRendered());
		assertTrue(textPres.isRendered());
		assertFalse(checkBoxPres.isRendered());

		Assert.assertSame(textPres.getWidget(), layout.getComponent(0));
		assertEquals(1, layout.getComponentCount());

		yLayout.addElement(yCheckBox);

		assertTrue(layoutPres.isRendered());
		assertTrue(textPres.isRendered());
		assertTrue(checkBoxPres.isRendered());

		Assert.assertSame(textPres.getWidget(), layout.getComponent(0));
		Assert.assertSame(checkBoxPres.getWidget(), layout.getComponent(1));
		assertEquals(2, layout.getComponentCount());

		yLayout.moveElement(0, yCheckBox);
		assertTrue(layoutPres.isRendered());
		assertTrue(textPres.isRendered());
		assertTrue(checkBoxPres.isRendered());

		Assert.assertSame(checkBoxPres.getWidget(), layout.getComponent(0));
		Assert.assertSame(textPres.getWidget(), layout.getComponent(1));
		assertEquals(2, layout.getComponentCount());

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
		YFormLayout yLayout = factory.createFormLayout();
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
		YFormLayout yLayout = factory.createFormLayout();
		yView.setContent(yLayout);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		IFormLayoutEditpart layoutEP = DelegatingEditPartManager.getInstance()
				.getEditpart(yLayout);
		IWidgetPresentation<Component> layoutPres = layoutEP.getPresentation();
		FormLayout layout = (FormLayout) layoutPres.getWidget();

		// start tests
		//
		assertTrue(layout.isVisible());
		assertTrue(layout.isEnabled());
		assertFalse(layout.isReadOnly());

		yLayout.setVisible(false);
		assertFalse(layout.isVisible());

	}

	@Test
	public void test_Dispose() throws ContextException {

		// switch the global locale to german
		Locale.setDefault(Locale.GERMAN);

		YView yView = factory.createView();
		YFormLayout yLayout = factory.createFormLayout();
		yView.setContent(yLayout);
		YCheckBox yCheckBox = factory.createCheckBox();
		yLayout.getElements().add(yCheckBox);
		YTextField yTextField = factory.createTextField();
		yLayout.getElements().add(yTextField);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);
		IFormLayoutEditpart layoutEP = DelegatingEditPartManager.getInstance()
				.getEditpart(yLayout);
		ICheckboxEditpart checkBoxEP = DelegatingEditPartManager.getInstance()
				.getEditpart(yCheckBox);
		ITextFieldEditpart textEP = DelegatingEditPartManager.getInstance()
				.getEditpart(yTextField);
		FormLayoutPresentation layoutPres = layoutEP.getPresentation();
		CheckBoxPresentation checkBoxPres = checkBoxEP.getPresentation();
		TextFieldPresentation textPres = textEP.getPresentation();

		assertTrue(layoutPres.isRendered());
		assertTrue(textPres.isRendered());
		assertTrue(checkBoxPres.isRendered());
		assertFalse(layoutPres.isDisposed());
		assertFalse(textPres.isDisposed());
		assertFalse(checkBoxPres.isDisposed());

		layoutEP.requestDispose();
		assertFalse(layoutPres.isRendered());
		assertFalse(textPres.isRendered());
		assertFalse(checkBoxPres.isRendered());

		assertTrue(layoutPres.isDisposed());
		assertTrue(textPres.isDisposed());
		assertTrue(checkBoxPres.isDisposed());

		assertEquals(2, yLayout.getElements().size());
	}

	@Test
	public void test_ContextDispose() throws ContextException {

		// switch the global locale to german
		Locale.setDefault(Locale.GERMAN);

		YView yView = factory.createView();
		YFormLayout yLayout = factory.createFormLayout();
		yView.setContent(yLayout);
		YCheckBox yCheckBox = factory.createCheckBox();
		yLayout.getElements().add(yCheckBox);
		YTextField yTextField = factory.createTextField();
		yLayout.getElements().add(yTextField);

		VaadinRenderer renderer = new VaadinRenderer();
		IViewContext context = renderer.render(rootLayout, yView, null);
		IViewEditpart viewEP = DelegatingEditPartManager.getInstance()
				.getEditpart(yView);
		IFormLayoutEditpart layoutEP = DelegatingEditPartManager.getInstance()
				.getEditpart(yLayout);
		ICheckboxEditpart checkBoxEP = DelegatingEditPartManager.getInstance()
				.getEditpart(yCheckBox);
		ITextFieldEditpart textEP = DelegatingEditPartManager.getInstance()
				.getEditpart(yTextField);
		ViewPresentation viewPres = viewEP.getPresentation();
		FormLayoutPresentation layoutPres = layoutEP.getPresentation();
		CheckBoxPresentation checkBoxPres = checkBoxEP.getPresentation();
		TextFieldPresentation textPres = textEP.getPresentation();

		assertTrue(layoutPres.isRendered());
		assertTrue(textPres.isRendered());
		assertTrue(checkBoxPres.isRendered());
		assertFalse(layoutPres.isDisposed());
		assertFalse(textPres.isDisposed());
		assertFalse(checkBoxPres.isDisposed());

		context.dispose();
		assertFalse(viewPres.isRendered());
		assertFalse(textPres.isRendered());
		assertFalse(checkBoxPres.isRendered());
		assertTrue(viewEP.isDisposed());
		assertTrue(layoutPres.isDisposed());
		assertTrue(textPres.isDisposed());
		assertTrue(checkBoxPres.isDisposed());

		Assert.assertNotNull(yView.getContent());
		assertEquals(2, yLayout.getElements().size());
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
		YFormLayout yLayout = factory.createFormLayout();
		yView.setContent(yLayout);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		IFormLayoutEditpart layoutEP = DelegatingEditPartManager.getInstance()
				.getEditpart(yLayout);
		IWidgetPresentation<Component> presentation = layoutEP
				.getPresentation();
		assertTrue(presentation.isRendered());
		assertFalse(presentation.isDisposed());
		assertEquals(3, presentation.getUIBindings().size());

		presentation.dispose();
		assertFalse(presentation.isRendered());
		assertTrue(presentation.isDisposed());
		assertEquals(0, presentation.getUIBindings().size());
	}

	@Test
	public void test_isRendered_unrender_byEditpart() throws ContextException {

		// switch the global locale to german
		Locale.setDefault(Locale.GERMAN);

		YView yView = factory.createView();
		YFormLayout yLayout = factory.createFormLayout();
		yView.setContent(yLayout);
		YCheckBox yCheckBox = factory.createCheckBox();
		yLayout.getElements().add(yCheckBox);
		YTextField yTextField = factory.createTextField();
		yLayout.getElements().add(yTextField);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);
		IFormLayoutEditpart layoutEP = DelegatingEditPartManager.getInstance()
				.getEditpart(yLayout);
		ICheckboxEditpart checkBoxEP = DelegatingEditPartManager.getInstance()
				.getEditpart(yCheckBox);
		ITextFieldEditpart textEP = DelegatingEditPartManager.getInstance()
				.getEditpart(yTextField);
		FormLayoutPresentation layoutPres = layoutEP.getPresentation();
		CheckBoxPresentation checkBoxPres = checkBoxEP.getPresentation();
		TextFieldPresentation textPres = textEP.getPresentation();

		assertTrue(layoutPres.isRendered());
		assertTrue(textPres.isRendered());
		assertTrue(checkBoxPres.isRendered());

		ComponentContainer widget = layoutPres.getWidget();
		Assert.assertNotNull(widget.getParent());
		layoutEP.requestUnrender();
		Assert.assertNull(widget.getParent());

		assertFalse(layoutPres.isRendered());
		assertFalse(textPres.isRendered());
		assertFalse(checkBoxPres.isRendered());
		assertFalse(layoutPres.isDisposed());
		assertFalse(textPres.isDisposed());
		assertFalse(checkBoxPres.isDisposed());

		layoutEP.requestRender();
		assertTrue(layoutPres.isRendered());
		assertTrue(textPres.isRendered());
		assertTrue(checkBoxPres.isRendered());
		assertFalse(layoutPres.isDisposed());
		assertFalse(textPres.isDisposed());
		assertFalse(checkBoxPres.isDisposed());

		layoutEP.requestDispose();
		assertFalse(layoutPres.isRendered());
		assertFalse(textPres.isRendered());
		assertFalse(checkBoxPres.isRendered());
		assertTrue(layoutPres.isDisposed());
		assertTrue(textPres.isDisposed());
		assertTrue(checkBoxPres.isDisposed());
	}

	@Test
	public void test_isRendered_unrender_Child_byEditpart()
			throws ContextException {

		// switch the global locale to german
		Locale.setDefault(Locale.GERMAN);

		YView yView = factory.createView();
		YFormLayout yLayout = factory.createFormLayout();
		yView.setContent(yLayout);
		YCheckBox yCheckBox = factory.createCheckBox();
		yLayout.getElements().add(yCheckBox);
		YTextField yTextField = factory.createTextField();
		yLayout.getElements().add(yTextField);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);
		IFormLayoutEditpart layoutEP = DelegatingEditPartManager.getInstance()
				.getEditpart(yLayout);
		ICheckboxEditpart checkBoxEP = DelegatingEditPartManager.getInstance()
				.getEditpart(yCheckBox);
		ITextFieldEditpart textEP = DelegatingEditPartManager.getInstance()
				.getEditpart(yTextField);
		FormLayoutPresentation layoutPres = layoutEP.getPresentation();
		CheckBoxPresentation checkBoxPres = checkBoxEP.getPresentation();
		TextFieldPresentation textPres = textEP.getPresentation();

		assertTrue(layoutPres.isRendered());
		assertTrue(textPres.isRendered());
		assertTrue(checkBoxPres.isRendered());

		checkBoxEP.requestUnrender();

		assertTrue(layoutPres.isRendered());
		assertTrue(textPres.isRendered());
		assertFalse(checkBoxPres.isRendered());

		FormLayout layout = (FormLayout) layoutPres.getWidget();
		Assert.assertSame(textPres.getWidget(), layout.getComponent(0));
		assertEquals(1, layout.getComponentCount());

		checkBoxEP.requestRender();
		assertTrue(layoutPres.isRendered());
		assertTrue(textPres.isRendered());
		assertTrue(checkBoxPres.isRendered());
		assertEquals(2, layout.getComponentCount());

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
		YFormLayout yLayout = factory.createFormLayout();
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

}
