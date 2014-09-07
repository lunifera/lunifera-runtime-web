/**
 * Copyright (c) 2011 - 2014, Lunifera GmbH (Gross Enzersdorf), Loetz KG (Heidelberg)
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: 
 * 		Florian Pirchner - Initial implementation
 */

package org.lunifera.runtime.web.ecview.services.vaadin;

import java.util.Map;

import org.lunifera.ecview.core.common.model.core.YView;
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
