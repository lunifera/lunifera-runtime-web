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
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.lunifera.ecview.core.common.binding.IECViewBindingManager;
import org.lunifera.ecview.core.common.context.ContextException;
import org.lunifera.ecview.core.common.context.II18nService;
import org.lunifera.ecview.core.common.context.IViewContext;
import org.lunifera.ecview.core.common.context.ViewSetContext;
import org.lunifera.ecview.core.common.editpart.DelegatingEditPartManager;
import org.lunifera.ecview.core.common.editpart.IElementEditpart;
import org.lunifera.ecview.core.common.editpart.IEmbeddableEditpart;
import org.lunifera.ecview.core.common.editpart.IViewEditpart;
import org.lunifera.ecview.core.common.editpart.IViewSetEditpart;
import org.lunifera.ecview.core.common.model.binding.YBeanValueBindingEndpoint;
import org.lunifera.ecview.core.common.model.binding.YBindingSet;
import org.lunifera.ecview.core.common.model.binding.YBindingUpdateStrategy;
import org.lunifera.ecview.core.common.model.core.YBeanSlot;
import org.lunifera.ecview.core.common.model.core.YBeanSlotValueBindingEndpoint;
import org.lunifera.ecview.core.common.model.core.YElement;
import org.lunifera.ecview.core.common.model.core.YEmbeddableValueEndpoint;
import org.lunifera.ecview.core.common.model.core.YView;
import org.lunifera.ecview.core.common.model.core.YViewSet;
import org.lunifera.ecview.core.common.model.validation.ValidationFactory;
import org.lunifera.ecview.core.common.model.validation.YMaxLengthValidator;
import org.lunifera.ecview.core.common.model.validation.YMinLengthValidator;
import org.lunifera.ecview.core.common.presentation.IWidgetPresentation;
import org.lunifera.ecview.core.extension.model.datatypes.YTextDatatype;
import org.lunifera.ecview.core.extension.model.extension.YGridLayout;
import org.lunifera.ecview.core.extension.model.extension.YTextField;
import org.lunifera.ecview.core.extension.model.extension.util.SimpleExtensionModelFactory;
import org.lunifera.ecview.core.ui.core.editparts.extension.ITextFieldEditpart;
import org.lunifera.ecview.core.util.emf.ModelUtil;
import org.lunifera.runtime.web.ecview.presentation.vaadin.VaadinRenderer;
import org.lunifera.runtime.web.ecview.presentation.vaadin.common.AbstractFieldWidgetPresenter;
import org.lunifera.runtime.web.ecview.presentation.vaadin.common.AbstractVaadinWidgetPresenter;
import org.lunifera.runtime.web.ecview.presentation.vaadin.internal.TextFieldPresentation;
import org.lunifera.runtime.web.ecview.presentation.vaadin.tests.model.BarHashById;
import org.lunifera.runtime.web.ecview.presentation.vaadin.tests.model.ValueBean;
import org.osgi.framework.BundleException;
import org.osgi.service.cm.ConfigurationException;

import com.vaadin.ui.Component;
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
		YTextField yText = factory.createTextField();
		yView.setContent(yText);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		ITextFieldEditpart textEditpart = DelegatingEditPartManager
				.getInstance().getEditpart(yText);
		IWidgetPresentation<Component> presentation = textEditpart
				.getPresentation();

		TextField text = (TextField) presentation.getWidget();
		;
		assertNotNull(text);
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

		TextField text1 = (TextField) text1Presentation.getWidget();
		TextField text2 = (TextField) text2Presentation.getWidget();

		// assert css class

		assertTrue(text1.getStyleName().contains("anyOtherClass"));
		assertTrue(text2.getStyleName().contains(
				AbstractVaadinWidgetPresenter.CSS_CLASS_CONTROL));

		// assert css id
		assertEquals("ID_0815", text1.getId());
		assertEquals(text2Editpart.getId(), text2.getId());
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
		TextField text1 = (TextField) text1Presentation.getWidget();
		;
		TextField text2 = (TextField) text2Presentation.getWidget();
		;

		// start tests
		//
		yText1.setValue("");
		yText2.setValue("");

		assertTrue(text1.isVisible());
		assertTrue(text1.isEnabled());
		assertFalse(text1.isReadOnly());
		assertEquals("", text1.getValue());

		assertTrue(text2.isVisible());
		assertTrue(text2.isEnabled());
		assertFalse(text2.isReadOnly());
		assertEquals("", text2.getValue());

		yText1.setVisible(false);
		assertFalse(text1.isVisible());

		yText1.setEnabled(false);
		assertFalse(text1.isEnabled());

		yText1.setEditable(false);
		assertTrue(text1.isReadOnly());

		// target to model
		text1.setReadOnly(false);
		assertTrue(yText1.isEditable());

		yText1.setValue("huhu");
		yText2.setValue("haha");
		assertEquals("huhu", text1.getValue());
		assertEquals("haha", text2.getValue());

	}

	@Test
	// BEGIN SUPRESS CATCH EXCEPTION
	public void test_Readonly_Binding() throws Exception {
		// END SUPRESS CATCH EXCEPTION
		// build the view model
		YView yView = factory.createView();
		YGridLayout yLayout = factory.createGridLayout();
		yView.setContent(yLayout);
		YTextField yText = factory.createTextField();
		yLayout.getElements().add(yText);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		ITextFieldEditpart textEditpart = DelegatingEditPartManager
				.getInstance().getEditpart(yText);
		IWidgetPresentation<Component> textPresentation = textEditpart
				.getPresentation();
		TextField text = (TextField) textPresentation.getWidget();

		ValueBean bean = new ValueBean(false);
		YBeanValueBindingEndpoint yBeanBinding = factory
				.createBeanBindingEndpoint();
		yBeanBinding.setBean(bean);
		yBeanBinding.setPropertyPath("boolValue");
		YBindingSet yBindingSet = yView.getOrCreateBindingSet();
		yBindingSet.addBinding(yText.createEditableEndpoint(), yBeanBinding);

		// test binding
		assertFalse(yText.isEditable());
		assertFalse(!text.isReadOnly());
		assertFalse(bean.isBoolValue());

		bean.setBoolValue(true);
		assertTrue(yText.isEditable());
		assertTrue(!text.isReadOnly());
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
		YTextField yText = factory.createTextField();
		yLayout.getElements().add(yText);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		ITextFieldEditpart textEditpart = DelegatingEditPartManager
				.getInstance().getEditpart(yText);
		IWidgetPresentation<Component> textPresentation = textEditpart
				.getPresentation();
		TextField text = (TextField) textPresentation.getWidget();

		ValueBean bean = new ValueBean(false);
		YBeanValueBindingEndpoint yBeanBinding = factory
				.createBeanBindingEndpoint();
		yBeanBinding.setBean(bean);
		yBeanBinding.setPropertyPath("boolValue");
		YBindingSet yBindingSet = yView.getOrCreateBindingSet();
		yBindingSet.addBinding(yText.createVisibleEndpoint(), yBeanBinding);

		// test binding
		assertFalse(yText.isVisible());
		assertFalse(text.isVisible());
		assertFalse(bean.isBoolValue());

		bean.setBoolValue(true);
		assertTrue(yText.isVisible());
		assertTrue(text.isVisible());
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
		YTextField yText = factory.createTextField();
		yLayout.getElements().add(yText);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		ITextFieldEditpart textEditpart = DelegatingEditPartManager
				.getInstance().getEditpart(yText);
		IWidgetPresentation<Component> textPresentation = textEditpart
				.getPresentation();
		TextField text = (TextField) textPresentation.getWidget();

		ValueBean bean = new ValueBean(false);
		YBeanValueBindingEndpoint yBeanBinding = factory
				.createBeanBindingEndpoint();
		yBeanBinding.setBean(bean);
		yBeanBinding.setPropertyPath("boolValue");
		YBindingSet yBindingSet = yView.getOrCreateBindingSet();
		yBindingSet.addBinding(yText.createEnabledEndpoint(), yBeanBinding);

		// test binding
		assertFalse(yText.isEnabled());
		assertFalse(text.isEnabled());
		assertFalse(bean.isBoolValue());

		bean.setBoolValue(true);
		assertTrue(yText.isEnabled());
		assertTrue(text.isEnabled());
		assertTrue(bean.isBoolValue());
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
		YTextField yText1 = factory.createTextField();
		yLayout.getElements().add(yText1);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		ITextFieldEditpart text1Editpart = DelegatingEditPartManager
				.getInstance().getEditpart(yText1);
		IWidgetPresentation<Component> text1Presentation = text1Editpart
				.getPresentation();
		TextField text1 = (TextField) text1Presentation.getWidget();

		// start tests
		//
		YBindingSet yBindingSet = yView.getOrCreateBindingSet();

		yText1.setValue("");
		YBeanValueBindingEndpoint beanBinding = factory
				.createBeanBindingEndpoint();
		ValueBean bean = new ValueBean("Huhu");
		beanBinding.setPropertyPath("value");
		beanBinding.setBean(bean);
		yBindingSet.addBinding(yText1.createValueEndpoint(), beanBinding);
		assertEquals("Huhu", text1.getValue());
		assertEquals("Huhu", yText1.getValue());

		bean.setValue("Haha");
		assertEquals("Haha", text1.getValue());
		assertEquals("Haha", yText1.getValue());

		text1.setValue("Haha1");
		assertEquals("Haha1", bean.getValue());
		assertEquals("Haha1", yText1.getValue());

		yText1.setValue("Haha2");
		assertEquals("Haha2", bean.getValue());
		assertEquals("Haha2", text1.getValue());
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
		YTextField yText = factory.createTextField();
		yGridlayout.getElements().add(yText);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		ITextFieldEditpart textEditpart = DelegatingEditPartManager
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
	public void test_addRemoveValidatorByModel() throws ContextException {
		YView yView = factory.createView();
		YGridLayout yGridlayout = factory.createGridLayout();
		yView.setContent(yGridlayout);
		YTextField yText = factory.createTextField();
		yGridlayout.getElements().add(yText);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);
		ITextFieldEditpart textEditpart = DelegatingEditPartManager
				.getInstance().getEditpart(yText);
		AbstractFieldWidgetPresenter<Component> presentation = textEditpart
				.getPresentation();

		assertEquals(0, yText.getValidators().size());
		assertEquals(0, presentation.getValidators().size());

		ValidationFactory vf = ValidationFactory.eINSTANCE;
		YMinLengthValidator yValidator = vf.createYMinLengthValidator();
		yText.getValidators().add(yValidator);

		assertEquals(1, presentation.getValidators().size());
		assertEquals(1, yText.getValidators().size());

		yText.getValidators().remove(yValidator);
		assertEquals(0, yText.getValidators().size());
		assertEquals(0, presentation.getValidators().size());

	}

	@Test
	public void test_addRemoveValidatorByModel_Twice() throws ContextException {
		YView yView = factory.createView();
		YGridLayout yGridlayout = factory.createGridLayout();
		yView.setContent(yGridlayout);
		YTextField yText = factory.createTextField();
		yGridlayout.getElements().add(yText);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);
		ITextFieldEditpart textEditpart = DelegatingEditPartManager
				.getInstance().getEditpart(yText);
		AbstractFieldWidgetPresenter<Component> presentation = textEditpart
				.getPresentation();

		assertEquals(0, yText.getValidators().size());
		assertEquals(0, presentation.getValidators().size());

		ValidationFactory vf = ValidationFactory.eINSTANCE;
		YMinLengthValidator yValidator = vf.createYMinLengthValidator();
		yText.getValidators().add(yValidator);
		yText.getValidators().add(yValidator);

		assertEquals(1, presentation.getValidators().size());
		assertEquals(1, yText.getValidators().size());

		yText.getValidators().remove(yValidator);
		yText.getValidators().remove(yValidator);
		assertEquals(0, yText.getValidators().size());
		assertEquals(0, presentation.getValidators().size());

	}

	@Test
	public void test_i18n() throws ContextException {

		// switch the global locale to german
		Locale.setDefault(Locale.GERMAN);

		YView yView = factory.createView();
		YGridLayout yGridlayout = factory.createGridLayout();
		yView.setContent(yGridlayout);
		YTextField yText = factory.createTextField();
		yGridlayout.getElements().add(yText);

		// set the i18n key
		yText.setLabelI18nKey(I18nServiceForTests.KEY__AGE);

		// prepare the I18nService and pass it to the renderer
		Map<String, Object> parameter = new HashMap<String, Object>();
		Map<String, Object> services = new HashMap<String, Object>();
		parameter.put(IViewContext.PARAM_SERVICES, services);
		services.put(II18nService.ID, new I18nServiceForTests());

		VaadinRenderer renderer = new VaadinRenderer();
		IViewContext context = renderer.render(rootLayout, yView, parameter);
		ITextFieldEditpart textEditpart = DelegatingEditPartManager
				.getInstance().getEditpart(yText);
		AbstractFieldWidgetPresenter<Component> presentation = textEditpart
				.getPresentation();

		assertEquals("Alter", presentation.getWidget().getCaption());

		context.setLocale(Locale.ENGLISH);
		assertEquals("Age", presentation.getWidget().getCaption());
	}

	@Test
	public void test_i18nByViewSet() throws ContextException {

		// switch the global locale to german
		Locale.setDefault(Locale.GERMAN);

		YViewSet yViewSet = factory.createViewSet();
		IViewSetEditpart viewSetEditpart = ModelUtil.getEditpart(yViewSet);
		ViewSetContext viewSetContext = new ViewSetContext(viewSetEditpart);

		YView yView = factory.createView();
		yViewSet.getViews().add(yView);
		YGridLayout yGridlayout = factory.createGridLayout();
		yView.setContent(yGridlayout);
		YTextField yText = factory.createTextField();
		yGridlayout.getElements().add(yText);

		// set the i18n key
		yText.setLabelI18nKey(I18nServiceForTests.KEY__AGE);

		// prepare the I18nService and pass it to the renderer
		Map<String, Object> parameter = new HashMap<String, Object>();
		Map<String, Object> services = new HashMap<String, Object>();
		parameter.put(IViewContext.PARAM_SERVICES, services);
		services.put(II18nService.ID, new I18nServiceForTests());

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, parameter);
		ITextFieldEditpart textEditpart = DelegatingEditPartManager
				.getInstance().getEditpart(yText);
		AbstractFieldWidgetPresenter<Component> presentation = textEditpart
				.getPresentation();

		assertEquals("Alter", presentation.getWidget().getCaption());

		viewSetContext.setLocale(Locale.ENGLISH);
		assertEquals("Age", presentation.getWidget().getCaption());
	}

	@Test
	public void test_addRemoveInternalValidatorByDatatype()
			throws ContextException {
		YView yView = factory.createView();
		YGridLayout yGridlayout = factory.createGridLayout();
		yView.setContent(yGridlayout);
		YTextField yText = factory.createTextField();
		YTextDatatype yTextDt = factory.createTextDatatype();
		yText.setDatatype(yTextDt);
		yGridlayout.getElements().add(yText);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);
		ITextFieldEditpart textEditpart = DelegatingEditPartManager
				.getInstance().getEditpart(yText);
		AbstractFieldWidgetPresenter<Component> presentation = textEditpart
				.getPresentation();

		assertEquals(0, yText.getInternalValidators().size());
		assertEquals(0, presentation.getValidators().size());

		yTextDt.setMaxLength(10);
		assertEquals(1, presentation.getValidators().size());
		assertEquals(1, yText.getInternalValidators().size());

		yTextDt.setMaxLength(-1);
		assertEquals(0, presentation.getValidators().size());
		assertEquals(0, yText.getInternalValidators().size());

		yTextDt.setMinLength(10);
		assertEquals(1, presentation.getValidators().size());
		assertEquals(1, yText.getInternalValidators().size());

		yTextDt.setMinLength(-1);
		assertEquals(0, presentation.getValidators().size());
		assertEquals(0, yText.getInternalValidators().size());

		yTextDt.setRegExpression("\\.pdf$");
		assertEquals(1, presentation.getValidators().size());
		assertEquals(1, yText.getInternalValidators().size());

		yTextDt.setRegExpression("");
		assertEquals(0, presentation.getValidators().size());
		assertEquals(0, yText.getInternalValidators().size());
	}

	@Test
	public void test_changePropertyOfDatatype_ValidatorNeedsToUpdate()
			throws ContextException {
		YView yView = factory.createView();
		YGridLayout yGridlayout = factory.createGridLayout();
		yView.setContent(yGridlayout);
		YTextField yText = factory.createTextField();
		YTextDatatype yTextDt = factory.createTextDatatype();
		yText.setDatatype(yTextDt);
		yGridlayout.getElements().add(yText);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);
		ITextFieldEditpart textEditpart = DelegatingEditPartManager
				.getInstance().getEditpart(yText);
		AbstractFieldWidgetPresenter<Component> presentation = textEditpart
				.getPresentation();

		assertEquals(0, yText.getInternalValidators().size());
		assertEquals(0, presentation.getValidators().size());

		yTextDt.setMaxLength(10);
		YMaxLengthValidator internalValidator = (YMaxLengthValidator) yText
				.getInternalValidators().get(0);
		assertEquals(1, presentation.getValidators().size());
		assertEquals(1, yText.getInternalValidators().size());
		assertEquals(10, internalValidator.getMaxLength());

		yTextDt.setMaxLength(5);

		assertEquals(1, presentation.getValidators().size());
		assertEquals(1, yText.getInternalValidators().size());
		assertEquals(5, internalValidator.getMaxLength());

	}

	@Test
	public void test_setNewDatatype() throws ContextException {
		// ensure that the old validators are removed and disposed properly
		YView yView = factory.createView();
		YGridLayout yGridlayout = factory.createGridLayout();
		yView.setContent(yGridlayout);
		YTextField yText = factory.createTextField();
		YTextDatatype yTextDt = factory.createTextDatatype();
		yText.setDatatype(yTextDt);
		yGridlayout.getElements().add(yText);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);
		ITextFieldEditpart textEditpart = DelegatingEditPartManager
				.getInstance().getEditpart(yText);
		AbstractFieldWidgetPresenter<Component> presentation = textEditpart
				.getPresentation();

		assertEquals(0, yText.getInternalValidators().size());
		assertEquals(0, presentation.getValidators().size());

		yTextDt.setMaxLength(10);

		assertEquals(1, presentation.getValidators().size());
		assertEquals(1, yText.getInternalValidators().size());

		yText.setDatatype(null);

		assertEquals(0, yText.getInternalValidators().size());
		assertEquals(0, presentation.getValidators().size());
	}

	/**
	 * If a textfield is bound to a beanslot and a new bean with the same
	 * hashcode is set to the slot, then the databinding is not refreshed and
	 * the old bean instance is still bound.
	 * 
	 * @throws ContextException
	 */
	@Test
	public void fix_MP81() throws ContextException {
		YView yView = factory.createView();
		YBeanSlot yBeanSlot = factory.createBeanSlot();
		yBeanSlot.setName("main");
		yBeanSlot.setValueType(BarHashById.class);
		yView.getBeanSlots().add(yBeanSlot);
		YGridLayout yLayout = factory.createGridLayout();
		yView.setContent(yLayout);
		YTextField yText = factory.createTextField();
		yLayout.getElements().add(yText);

		YBeanSlotValueBindingEndpoint slotEP = yBeanSlot
				.createBindingEndpoint("name");
		YEmbeddableValueEndpoint textEP = yText.createValueEndpoint();

		YBindingSet yBindingSet = yView.getOrCreateBindingSet();
		yBindingSet.addBinding(textEP, slotEP, YBindingUpdateStrategy.UPDATE,
				YBindingUpdateStrategy.UPDATE);

		VaadinRenderer renderer = new VaadinRenderer();
		IViewContext context = renderer.render(rootLayout, yView, null);

		ITextFieldEditpart textEditpart = DelegatingEditPartManager
				.getInstance().getEditpart(yText);
		IWidgetPresentation<Component> textPresentation = textEditpart
				.getPresentation();
		TextField text = (TextField) textPresentation.getWidget();

		BarHashById bean = new BarHashById("112233");
		bean.setName("Lunifera");
		context.setBean("main", bean);

		assertEquals("Lunifera", text.getValue());
		assertEquals("Lunifera", bean.getName());

		BarHashById bean2 = new BarHashById("112233");
		bean2.setName("AnotherName");
		context.setBean("main", bean2);

//		IECViewBindingManager bindingManager = context
//				.getService(IECViewBindingManager.class.getName());
//		bindingManager.updateTarget();

		assertEquals("AnotherName", bean2.getName());
		assertEquals("AnotherName", text.getValue());

	}
}
