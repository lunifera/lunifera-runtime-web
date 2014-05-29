package org.lunifera.runtime.web.ecview.services.vaadin.impl;

import java.util.Map;

import org.eclipse.emf.ecp.ecview.common.model.core.YView;
import org.lunifera.dsl.semantic.dto.LDto;
import org.lunifera.runtime.web.ecview.services.vaadin.ILDtoRenderStrategy;
import org.osgi.service.component.annotations.Component;

@Component(properties = { "service.ranking=100" })
public class LDtoRenderStrategy implements ILDtoRenderStrategy {

	@Override
	public YView render(LDto lDto, Class<?> dtoClass, Object dtoInstance,
			Map<String, Object> beanSlots) {
		return null;
	}

}
