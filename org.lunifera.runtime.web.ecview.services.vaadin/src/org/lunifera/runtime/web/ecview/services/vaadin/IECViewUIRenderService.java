/**
 * Copyright (c) 2011 - 2015, Lunifera GmbH (Gross Enzersdorf), Loetz KG (Heidelberg)
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *         Florian Pirchner - Initial implementation
 */

package org.lunifera.runtime.web.ecview.services.vaadin;

import java.util.Map;

import org.lunifera.ecview.core.common.context.IViewContext;

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
