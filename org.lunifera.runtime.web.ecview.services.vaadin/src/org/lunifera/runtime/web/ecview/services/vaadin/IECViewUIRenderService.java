package org.lunifera.runtime.web.ecview.services.vaadin;

import java.util.Map;

import org.eclipse.emf.ecp.ecview.common.context.IViewContext;

import com.vaadin.ui.ComponentContainer;

/**
 * Is used to render a UI for a given dto or entity.
 */
public interface IECViewUIRenderService {

	/**
	 * Renders an UI for the given dto class.
	 * 
	 * @param clazz
	 *            - the dto class
	 * @param component
	 *            - the component container. Must be attached to an UI instance.
	 * @param beanSlots
	 *            - The map entry is registered as a bean slot
	 * @param renderingOptions
	 *            - The rendering options
	 * @return
	 */
	IViewContext renderUIForDTO(Class<?> clazz, ComponentContainer component,
			Map<String, Object> beanSlots, Map<String, Object> renderingOptions);

	/**
	 * Renders an UI for the given dto instance and also create the required
	 * bindings.
	 * 
	 * @param dto
	 *            - the dto instance
	 * @param component
	 *            - the component container. Must be attached to an UI instance.
	 * @param beanSlots
	 *            - The map entry is registered as a bean slot
	 * @param renderingOptions
	 *            - The rendering options
	 * @return
	 */
	IViewContext renderUIForDTO(Object dto, ComponentContainer component,
			Map<String, Object> beanSlots, Map<String, Object> renderingOptions);

	/**
	 * Renders an UI for the given entity class.
	 * 
	 * @param clazz
	 *            - the dto class
	 * @param component
	 *            - the component container. Must be attached to an UI instance.
	 * @param beanSlots
	 *            - The map entry is registered as a bean slot
	 * @param renderingOptions
	 *            - The rendering options
	 * @return
	 */
	IViewContext renderUIForEntity(Class<?> clazz,
			ComponentContainer component, Map<String, Object> beanSlots,
			Map<String, Object> renderingOptions);

	/**
	 * Renders an UI for the given entity instance and also create the required
	 * bindings.
	 * 
	 * @param entity
	 *            - the entity instance
	 * @param component
	 *            - the component container. Must be attached to an UI instance.
	 * @param beanSlots
	 *            - The map entry is registered as a bean slot
	 * @param renderingOptions
	 *            - The rendering options
	 * @return
	 */
	IViewContext renderUIForEntity(Object entity, ComponentContainer component,
			Map<String, Object> beanSlots, Map<String, Object> renderingOptions);

}
