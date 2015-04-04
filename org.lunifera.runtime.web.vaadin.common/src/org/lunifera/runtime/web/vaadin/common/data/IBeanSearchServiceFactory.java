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

/**
 * This factory is exposed as an OSGi service and will return new bean search
 * service instances for the given bean type.
 */
public interface IBeanSearchServiceFactory {

	/**
	 * Returns the {@link IBeanSearchService} for the given type. If no proper
	 * implementation could be found, an in memory service will be returned.
	 * 
	 * @param bean
	 * @return
	 */
	<BEAN> IBeanSearchService<BEAN> createService(Class<BEAN> bean);

}
