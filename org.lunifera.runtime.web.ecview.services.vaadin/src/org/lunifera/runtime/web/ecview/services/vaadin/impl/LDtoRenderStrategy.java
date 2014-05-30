package org.lunifera.runtime.web.ecview.services.vaadin.impl;

import java.util.Map;

import org.eclipse.emf.ecp.ecview.common.context.IViewContext;
import org.eclipse.emf.ecp.ecview.common.model.core.YView;
import org.eclipse.emf.ecp.ecview.extension.model.extension.YGridLayout;
import org.eclipse.emf.ecp.ecview.extension.model.extension.util.SimpleExtensionModelFactory;
import org.lunifera.dsl.semantic.dto.LDto;
import org.lunifera.runtime.web.ecview.services.vaadin.ILDtoRenderStrategy;
import org.osgi.service.component.annotations.Component;

@Component(property = { "service.ranking=100" })
public class LDtoRenderStrategy implements ILDtoRenderStrategy {

	private SimpleExtensionModelFactory factory = new SimpleExtensionModelFactory();

	@Override
	public YView render(LDto lDto, Class<?> dtoClass, Object dtoInstance,
			Map<String, Object> beanSlots) {

		YView yView = factory.createView();
		// create a bean slot for the bindings
		yView.addBeanSlot(IViewContext.ROOTBEAN_SELECTOR, dtoClass);

		// create a main layout
		YGridLayout yLayout = factory.createGridLayout();
		yLayout.setColumns(4);
		yView.setContent(yLayout);

		// Use the dto visitor to build the UI
		LDtoVisitor visitor = new LDtoVisitor(yLayout);
		visitor.doSwitch(lDto);

		return yView;
	}
}
