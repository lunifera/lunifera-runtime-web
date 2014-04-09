package org.lunifera.runtime.web.ecview.presentation.vaadin.tests;

import static org.junit.Assert.assertEquals;

import java.util.Iterator;

import org.eclipse.emf.ecp.ecview.common.context.ContextException;
import org.eclipse.emf.ecp.ecview.common.context.IViewContext;
import org.eclipse.emf.ecp.ecview.common.context.IViewSetContext;
import org.eclipse.emf.ecp.ecview.common.editpart.DelegatingEditPartManager;
import org.eclipse.emf.ecp.ecview.common.editpart.IElementEditpart;
import org.eclipse.emf.ecp.ecview.common.editpart.IEmbeddableEditpart;
import org.eclipse.emf.ecp.ecview.common.editpart.IViewEditpart;
import org.eclipse.emf.ecp.ecview.common.editpart.IViewSetEditpart;
import org.eclipse.emf.ecp.ecview.common.model.binding.YBeanBindingEndpoint;
import org.eclipse.emf.ecp.ecview.common.model.binding.YBindingSet;
import org.eclipse.emf.ecp.ecview.common.model.core.YElement;
import org.eclipse.emf.ecp.ecview.common.model.core.YView;
import org.eclipse.emf.ecp.ecview.common.model.core.YViewSet;
import org.eclipse.emf.ecp.ecview.common.presentation.IWidgetPresentation;
import org.eclipse.emf.ecp.ecview.extension.model.extension.YDecimalField;
import org.eclipse.emf.ecp.ecview.extension.model.extension.YGridLayout;
import org.eclipse.emf.ecp.ecview.extension.model.extension.util.SimpleExtensionModelFactory;
import org.eclipse.emf.ecp.ecview.util.emf.ModelUtil;
import org.junit.Before;
import org.junit.Test;
import org.lunifera.runtime.web.ecview.presentation.vaadin.VaadinRenderer;
import org.lunifera.runtime.web.ecview.presentation.vaadin.tests.model.ValueBean;
import org.lunifera.runtime.web.vaadin.components.fields.DecimalField;

import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;

public class ModelUtilTest {

	private SimpleExtensionModelFactory factory = new SimpleExtensionModelFactory();
	private CssLayout rootLayout = new CssLayout();
	private DecimalField text1;
	private DecimalField text2;
	private IEmbeddableEditpart text1Editpart;
	private IEmbeddableEditpart text2Editpart;
	private YDecimalField yText1;
	private YDecimalField yText2;
	private YBindingSet yBindingSet;
	private ValueBean bean1;
	private ValueBean bean2;
	private YViewSet yViewSet;
	private YView yView;
	private IViewEditpart viewEditpart;
	private IViewSetEditpart viewSetEditpart;
	private YGridLayout yLayout;
	private IViewSetContext viewSetContext;
	private IViewContext viewContext;

	@Before
	public void initialize() throws ContextException {

		yViewSet = factory.createViewSet();
		yView = factory.createView();
		yViewSet.getViews().add(yView);
		yLayout = factory.createGridLayout();
		yView.setContent(yLayout);
		yText1 = factory.createDecimalField();
		yText1.setCssID("ID_0815");
		yText1.setCssClass("anyOtherClass");
		yLayout.getElements().add(yText1);
		yText2 = factory.createDecimalField();
		yLayout.getElements().add(yText2);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		viewSetEditpart = ModelUtil.getEditpart(yViewSet);
		viewEditpart = ModelUtil.getEditpart(yView);
		text1Editpart = ModelUtil.getEditpart(yText1);
		text2Editpart = ModelUtil.getEditpart(yText2);
		viewSetContext = ModelUtil.getViewSetContext(yLayout);
		viewContext = ModelUtil.getViewContext(yLayout);
		IWidgetPresentation<Component> text1Presentation = text1Editpart
				.getPresentation();
		IWidgetPresentation<Component> text2Presentation = text2Editpart
				.getPresentation();
		ComponentContainer text1BaseComponentContainer = (ComponentContainer) text1Presentation
				.getWidget();
		ComponentContainer text2BaseComponentContainer = (ComponentContainer) text2Presentation
				.getWidget();
		text1 = (DecimalField) unwrapText(text1BaseComponentContainer);
		text2 = (DecimalField) unwrapText(text2BaseComponentContainer);

		yBindingSet = yView.getOrCreateBindingSet();
		yView.setBindingSet(yBindingSet);
		YBeanBindingEndpoint beanBinding1 = factory.createBeanBindingEndpoint();
		bean1 = new ValueBean(9988.77);
		beanBinding1.setPropertyPath("doubleValue");
		beanBinding1.setBean(bean1);
		yBindingSet.addBinding(yText1.createValueEndpoint(), beanBinding1);

		YBeanBindingEndpoint beanBinding2 = factory.createBeanBindingEndpoint();
		bean2 = new ValueBean(9988.77);
		beanBinding2.setPropertyPath("doubleValue");
		beanBinding2.setBean(bean2);
		yBindingSet.addBinding(yText2.createValueEndpoint(), beanBinding2);
	}

	@Test
	public void test_testenvironment() {

		// model to ui & bean
		yText1.setValue(111.11);
		yText2.setValue(222.22);
		assertEquals("111.11", text1.getValue());
		assertEquals("222.22", text2.getValue());
		assertEquals(111.11, bean1.getDoubleValue(), 0);
		assertEquals(222.22, bean2.getDoubleValue(), 0);

		// bean to ui & model
		bean1.setDoubleValue(333.33);
		bean2.setDoubleValue(444.44);
		assertEquals("333.33", text1.getValue());
		assertEquals("444.44", text2.getValue());
		assertEquals(333.33, yText1.getValue(), 0);
		assertEquals(444.44, yText2.getValue(), 0);

		// ui to bean & model
		text1.setValue("555.55");
		text2.setValue("666.66");
		assertEquals(555.55, yText1.getValue(), 0);
		assertEquals(666.66, yText2.getValue(), 0);
		assertEquals(555.55, bean1.getDoubleValue(), 0);
		assertEquals(666.66, bean2.getDoubleValue(), 0);

	}

	@Test
	public void test_getViewEditPart() {
		IViewEditpart testPart = ModelUtil.getViewEditpart(yText1);
		assertEquals(viewEditpart, testPart);
		testPart = ModelUtil.getViewEditpart(yText2);
		assertEquals(viewEditpart, testPart);
		testPart = ModelUtil.getViewEditpart(yLayout);
		assertEquals(viewEditpart, testPart);
	}
	
	@Test
	public void test_getViewSetEditPart() {
		IViewSetEditpart testPart = ModelUtil.getViewSetEditpart(yText1);
		assertEquals(viewSetEditpart, testPart);
		testPart = ModelUtil.getViewSetEditpart(yText2);
		assertEquals(viewSetEditpart, testPart);
		testPart = ModelUtil.getViewSetEditpart(yLayout);
		assertEquals(viewSetEditpart, testPart);
	}
	
	@Test
	public void test_getViewSetContext() {
		IViewSetContext testContext = ModelUtil.getViewSetContext(yText1);
		assertEquals(viewSetContext, testContext);
		testContext = ModelUtil.getViewSetContext(yText2);
		assertEquals(viewSetContext, testContext);
		testContext = ModelUtil.getViewSetContext(yLayout);
		assertEquals(viewSetContext, testContext);
	}
	
	@Test
	public void test_getViewContext() {
		IViewContext testContext = ModelUtil.getViewContext(yText1);
		assertEquals(viewContext, testContext);
		testContext = ModelUtil.getViewContext(yText2);
		assertEquals(viewContext, testContext);
		testContext = ModelUtil.getViewContext(yLayout);
		assertEquals(viewContext, testContext);
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
