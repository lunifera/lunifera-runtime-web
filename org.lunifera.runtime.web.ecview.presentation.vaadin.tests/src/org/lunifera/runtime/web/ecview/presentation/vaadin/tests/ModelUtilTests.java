package org.lunifera.runtime.web.ecview.presentation.vaadin.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.eclipse.core.databinding.Binding;
import org.junit.Before;
import org.junit.Test;
import org.lunifera.ecview.core.common.binding.IECViewBindingManager;
import org.lunifera.ecview.core.common.context.ContextException;
import org.lunifera.ecview.core.common.context.IViewContext;
import org.lunifera.ecview.core.common.editpart.DelegatingEditPartManager;
import org.lunifera.ecview.core.common.editpart.IElementEditpart;
import org.lunifera.ecview.core.common.editpart.IEmbeddableEditpart;
import org.lunifera.ecview.core.common.editpart.IEmbeddableValueBindingEndpointEditpart;
import org.lunifera.ecview.core.common.editpart.IViewEditpart;
import org.lunifera.ecview.core.common.editpart.binding.IBindingEditpart;
import org.lunifera.ecview.core.common.editpart.binding.IBindingSetEditpart;
import org.lunifera.ecview.core.common.editpart.binding.IValueBindingEditpart;
import org.lunifera.ecview.core.common.model.binding.YBeanValueBindingEndpoint;
import org.lunifera.ecview.core.common.model.binding.YBinding;
import org.lunifera.ecview.core.common.model.binding.YBindingSet;
import org.lunifera.ecview.core.common.model.core.CoreModelFactory;
import org.lunifera.ecview.core.common.model.core.CoreModelPackage;
import org.lunifera.ecview.core.common.model.core.YBeanSlot;
import org.lunifera.ecview.core.common.model.core.YBeanSlotValueBindingEndpoint;
import org.lunifera.ecview.core.common.model.core.YElement;
import org.lunifera.ecview.core.common.model.core.YEmbeddableValueEndpoint;
import org.lunifera.ecview.core.common.model.core.YView;
import org.lunifera.ecview.core.common.notification.ILifecycleService;
import org.lunifera.ecview.core.common.presentation.IWidgetPresentation;
import org.lunifera.ecview.core.extension.model.extension.YDecimalField;
import org.lunifera.ecview.core.extension.model.extension.YGridLayout;
import org.lunifera.ecview.core.extension.model.extension.util.SimpleExtensionModelFactory;
import org.lunifera.ecview.core.util.emf.ModelUtil;
import org.lunifera.runtime.web.ecview.presentation.vaadin.VaadinRenderer;
import org.lunifera.runtime.web.ecview.presentation.vaadin.tests.model.ValueBean;
import org.lunifera.runtime.web.ecview.presentation.vaadin.tests.presentation.DefaultUI;
import org.lunifera.runtime.web.vaadin.components.fields.DecimalField;

import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.UI;

public class ModelUtilTests {

	private SimpleExtensionModelFactory factory = new SimpleExtensionModelFactory();
	private CssLayout rootLayout = new CssLayout();
	private DecimalField text1;
	private DecimalField text2;
	private IEmbeddableEditpart layoutEditpart;
	private IEmbeddableEditpart text1Editpart;
	private IEmbeddableEditpart text2Editpart;
	private YDecimalField yText1;
	private YDecimalField yText2;
	private YBindingSet yBindingSet;
	private ValueBean bean1;
	private ValueBean bean2;
	private YView yView;
	private IViewEditpart viewEditpart;
	private YGridLayout yLayout;
	private IViewContext viewContext;
	private YEmbeddableValueEndpoint yText1ValueEndpoint;
	private YBinding yText1ValueBinding;

	@Before
	public void initialize() throws ContextException {

		Locale.setDefault(Locale.US);

		UI.setCurrent(new DefaultUI());
		UI.getCurrent().setContent(rootLayout);

		yView = factory.createView();
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

		viewEditpart = ModelUtil.getEditpart(yView);
		layoutEditpart = ModelUtil.getEditpart(yLayout);
		text1Editpart = ModelUtil.getEditpart(yText1);
		text2Editpart = ModelUtil.getEditpart(yText2);
		viewContext = ModelUtil.getViewContext(yLayout);
		IWidgetPresentation<Component> text1Presentation = text1Editpart
				.getPresentation();
		IWidgetPresentation<Component> text2Presentation = text2Editpart
				.getPresentation();
		text1 = (DecimalField) text1Presentation.getWidget();
		text2 = (DecimalField) text2Presentation.getWidget();

		yBindingSet = yView.getOrCreateBindingSet();
		yView.setBindingSet(yBindingSet);
		YBeanValueBindingEndpoint beanBinding1 = factory
				.createBeanBindingEndpoint();
		bean1 = new ValueBean(9988.77);
		beanBinding1.setPropertyPath("doubleValue");
		beanBinding1.setBean(bean1);

		yText1ValueEndpoint = yText1.createValueEndpoint();
		yText1ValueBinding = yBindingSet.addBinding(yText1ValueEndpoint,
				beanBinding1);

		YBeanValueBindingEndpoint beanBinding2 = factory
				.createBeanBindingEndpoint();
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

	// @Test
	// public void test_getViewSetEditPart() {
	// IViewSetEditpart testPart = ModelUtil.getViewSetEditpart(yText1);
	// assertEquals(viewSetEditpart, testPart);
	// testPart = ModelUtil.getViewSetEditpart(yText2);
	// assertEquals(viewSetEditpart, testPart);
	// testPart = ModelUtil.getViewSetEditpart(yLayout);
	// assertEquals(viewSetEditpart, testPart);
	// }
	//
	// @Test
	// public void test_getViewSetContext() {
	// IViewSetContext testContext = ModelUtil.getViewSetContext(yText1);
	// assertEquals(viewSetContext, testContext);
	// testContext = ModelUtil.getViewSetContext(yText2);
	// assertEquals(viewSetContext, testContext);
	// testContext = ModelUtil.getViewSetContext(yLayout);
	// assertEquals(viewSetContext, testContext);
	// }

	@Test
	public void test_getViewContext() {
		IViewContext testContext = ModelUtil.getViewContext(yText1);
		assertEquals(viewContext, testContext);
		testContext = ModelUtil.getViewContext(yText2);
		assertEquals(viewContext, testContext);
		testContext = ModelUtil.getViewContext(yLayout);
		assertEquals(viewContext, testContext);
	}

	// @Test
	// public void test_addNewViewByEditpart() {
	// viewSetEditpart.removeView(viewEditpart);
	// assertEquals(0, viewSetEditpart.getViews().size());
	// IViewEditpart part = ModelUtil.addNewViewByEditpart(viewSetEditpart);
	// assertEquals(1, viewSetEditpart.getViews().size());
	// assertNotNull(part);
	// }

	@Test
	public void test_getWidget() {
		Object layout_w = ModelUtil.getWidget(yLayout);
		Object text1_w = ModelUtil.getWidget(yText1);
		Object text2_w = ModelUtil.getWidget(yText2);
		assertNotNull(layout_w);
		assertNotNull(text1_w);
		assertNotNull(text2_w);
		assertEquals(layoutEditpart.getPresentation().getWidget(), layout_w);

	}

	@Test
	public void test_getUIBindings() {
		Set<Binding> uiBindings = text1Editpart.getPresentation()
				.getUIBindings();
		Set<Binding> uiBindings2 = ModelUtil.getUIBindings(yText1);
		assertEquals(uiBindings2.size(), uiBindings.size());
		for (Binding binding : uiBindings2) {
			assertTrue(uiBindings.contains(binding));
		}
		for (Binding binding : uiBindings) {
			assertTrue(uiBindings2.contains(binding));
		}
	}

	@Test
	public void test_getValueBinding() {
		IValueBindingEditpart editpart = ModelUtil
				.getEditpart(yText1ValueBinding);
		Binding valueBinding2 = ModelUtil.getValueBinding(yText1);
		assertEquals(editpart.getBinding(), valueBinding2);
	}

	@Test
	public void test_getValueBindingEditpart() {
		IValueBindingEditpart editpart = ModelUtil
				.getEditpart(yText1ValueBinding);
		YBinding yBinding = yText1.getValueBindingEndpoint().getBinding();
		IValueBindingEditpart bindingEditpart = ModelUtil.getEditpart(yBinding);
		assertEquals(editpart, bindingEditpart);
	}

	@Test
	public void test_getValueEndpointEditpart() {
		YEmbeddableValueEndpoint yBinding = yText1.getValueBindingEndpoint();
		IEmbeddableValueBindingEndpointEditpart editpart1 = ModelUtil
				.getValueEndpointEditpart(yBinding);
		IEmbeddableValueBindingEndpointEditpart editpart2 = DelegatingEditPartManager
				.getInstance().getEditpart(yBinding);

		assertEquals(editpart1, editpart2);
	}

	@Test
	public void test_getPresentation() {
		IEmbeddableEditpart editpart = DelegatingEditPartManager.getInstance()
				.getEditpart(yText1);
		IWidgetPresentation<?> presentation = editpart.getPresentation();
		assertEquals(presentation, ModelUtil.getPresentation(yText1));
	}

	@Test
	public void test_getModelBindingEditparts() {
		YBindingSet bindingset = yText1.getView().getBindingSet();
		IBindingSetEditpart bindingSetEditpart = ModelUtil
				.getEditpart(bindingset);
		List<IBindingEditpart<?>> bindings = bindingSetEditpart
				.findBindings(yText1);
		assertEquals(bindings, ModelUtil.getModelBindingEditparts(yText1));
	}

	@Test
	public void test_getLifecacleService() {
		ILifecycleService service = viewContext
				.getService(ILifecycleService.class.getName());
		assertEquals(service, ModelUtil.getLifecylceService(viewContext));
	}

	@Test
	public void test_getEditpart() {
		IElementEditpart editpart = DelegatingEditPartManager.getInstance()
				.getEditpart(yLayout);
		assertEquals(editpart, ModelUtil.getEditpart(yLayout));
	}

	@Test
	public void test_getBindingManager() {
		IECViewBindingManager manager = viewContext
				.getService(IECViewBindingManager.class.getName());
		assertEquals(manager, ModelUtil.getBindingManager(viewContext));
	}

	// @Test
	// public void test_createViewSetByEditpart() {
	// IViewSetEditpart editpart = DelegatingEditPartManager.getInstance()
	// .createEditpart(CoreModelPackage.eNS_URI,
	// IViewSetEditpart.class);
	// YViewSet set = (YViewSet) editpart.getModel();
	// assertNotNull(set);
	// }

	@Test
	public void test_createViewByEditpart() {
		IViewEditpart editpart = DelegatingEditPartManager.getInstance()
				.createEditpart(CoreModelPackage.eNS_URI, IViewEditpart.class);
		YView view = (YView) editpart.getModel();
		assertNotNull(view);
	}

	@Test
	public void test_URI_ForBeanSlot_ViewContext() {
		IViewEditpart editpart = DelegatingEditPartManager.getInstance()
				.createEditpart(CoreModelPackage.eNS_URI, IViewEditpart.class);
		YView yView = (YView) editpart.getModel();

		YBeanSlot ySlot = factory.createBeanSlot();
		ySlot.setName("slotNo1");
		ySlot.setValueType(String.class);
		yView.getBeanSlots().add(ySlot);

		assertEquals("view://bean/slotNo1", ModelUtil.getURI(ySlot).toString());
	}

	// @Test
	// public void test_URI_ForBeanSlot_ViewSetContext() {
	// IViewSetEditpart editpart = DelegatingEditPartManager.getInstance()
	// .createEditpart(CoreModelPackage.eNS_URI,
	// IViewSetEditpart.class);
	// YViewSet yViewSet = (YViewSet) editpart.getModel();
	//
	// YBeanSlot ySlot = factory.createBeanSlot();
	// ySlot.setName("slotNo1");
	// ySlot.setValueType(String.class);
	// yViewSet.getBeanSlots().add(ySlot);
	//
	// assertEquals("viewset://bean/slotNo1", ModelUtil.getURI(ySlot)
	// .toString());
	// }

	@Test
	public void test_URI_ForBeanSlotBinding_ViewContext() {
		IViewEditpart editpart = DelegatingEditPartManager.getInstance()
				.createEditpart(CoreModelPackage.eNS_URI, IViewEditpart.class);
		YView yView = (YView) editpart.getModel();

		YBeanSlot ySlot = factory.createBeanSlot();
		ySlot.setName("slotNo1");
		ySlot.setValueType(String.class);
		yView.getBeanSlots().add(ySlot);

		YBeanSlotValueBindingEndpoint yEndpoint = CoreModelFactory.eINSTANCE
				.createYBeanSlotValueBindingEndpoint();
		yEndpoint.setAttributePath("item.group.name");
		yEndpoint.setBeanSlot(ySlot);

		assertEquals("view://bean/slotNo1#item.group.name",
				ModelUtil.getURI(yEndpoint).toString());
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
