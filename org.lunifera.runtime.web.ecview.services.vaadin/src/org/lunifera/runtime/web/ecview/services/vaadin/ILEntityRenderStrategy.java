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

import org.lunifera.ecview.core.common.model.core.YView;
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
