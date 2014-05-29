package org.lunifera.runtime.web.ecview.services.vaadin;

import java.util.Map;

import org.eclipse.emf.ecp.ecview.common.model.core.YView;
import org.lunifera.dsl.semantic.dto.LDto;

public interface ILDtoRenderStrategy {

	/**
	 * Renders an YView for the given parameters.
	 * 
	 * @param lDto
	 *            - the dto model
	 * @param dtoClass
	 *            - the class of the dto
	 * @param dtoInstance
	 *            - the dto instance. May be <code>null</code>.
	 * @param beanSlots
	 *            - the bean slot that should be initialzed.
	 * @return
	 */
	YView render(LDto lDto, Class<?> dtoClass, Object dtoInstance,
			Map<String, Object> beanSlots);

}
