package org.lunifera.runtime.web.ecview.services.vaadin;

import java.util.Map;

import org.eclipse.emf.ecp.ecview.common.model.core.YView;
import org.lunifera.dsl.semantic.entity.LEntity;

public interface ILEntityRenderStrategy {

	/**
	 * Renders an YView for the given parameters.
	 * 
	 * @param lEntity
	 *            - the entity model
	 * @param entityClass
	 *            - the class of the entity
	 * @param entityInstance
	 *            - the entity instance. May be <code>null</code>.
	 * @param beanSlots
	 *            - the bean slot that should be initialzed.
	 * @return
	 */
	YView render(LEntity lEntity, Class<?> entityClass, Object entityInstance,
			Map<String, Object> beanSlots);

}
