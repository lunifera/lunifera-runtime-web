package org.lunifera.runtime.web.ecview.services.vaadin.impl;

import java.util.Map;

import org.eclipse.emf.ecp.ecview.common.context.IViewContext;
import org.eclipse.emf.ecp.ecview.common.model.core.YView;
import org.eclipse.emf.ecp.ecview.extension.model.extension.YGridLayout;
import org.eclipse.emf.ecp.ecview.extension.model.extension.util.SimpleExtensionModelFactory;
import org.lunifera.dsl.semantic.entity.LEntity;
import org.lunifera.runtime.web.ecview.services.vaadin.ILEntityRenderStrategy;
import org.osgi.service.component.annotations.Component;

@Component(property = { "service.ranking=100" })
public class LEntityRenderStrategy implements ILEntityRenderStrategy {

	private SimpleExtensionModelFactory factory = new SimpleExtensionModelFactory();

	@Override
	public YView render(LEntity lEntity, Class<?> entityClass,
			Object entityInstance, Map<String, Object> beanSlots) {

		YView yView = factory.createView();
		// create a bean slot for the bindings
		yView.addBeanSlot(IViewContext.ROOTBEAN_SELECTOR, entityClass);

		// create a main layout
		YGridLayout yLayout = factory.createGridLayout();
		yLayout.setColumns(2);
		yLayout.setFillHorizontal(true);
		yView.setContent(yLayout);

		// Use the entity renderer to build the UI model
		EntityModelRenderer renderer = new EntityModelRenderer();
		renderer.render(lEntity, yLayout, entityClass);

		return yView;
	}
}
