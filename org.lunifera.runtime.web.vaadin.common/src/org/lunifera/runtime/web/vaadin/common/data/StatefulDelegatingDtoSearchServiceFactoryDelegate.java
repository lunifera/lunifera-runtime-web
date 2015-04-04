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
package org.lunifera.runtime.web.vaadin.common.data;

import org.lunifera.dsl.dto.lib.impl.DtoServiceAccess;
import org.lunifera.dsl.dto.lib.services.IDTOService;
import org.osgi.service.component.annotations.Component;

/**
 * Tries to create a bean search service backed by a DTO-Service.
 */
@SuppressWarnings("restriction")
@Component
public class StatefulDelegatingDtoSearchServiceFactoryDelegate implements
		IBeanSearchServiceFactoryDelegate {

	@Override
	public <BEAN> IBeanSearchService<BEAN> createService(Class<BEAN> bean) {
		// if a service that delegates to the database could be found, use it.
		IDTOService<BEAN> service = DtoServiceAccess.getService(bean);
		if (service != null) {
			return new StatefulDelegatingDtoSearchService<BEAN>(service, bean);
		}
		return null;
	}

}
