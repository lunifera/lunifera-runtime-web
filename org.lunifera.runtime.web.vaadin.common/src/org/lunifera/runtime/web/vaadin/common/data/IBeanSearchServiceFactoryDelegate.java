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
 * Implementations are factories used by the {@link IBeanSearchServiceFactory}.
 * Delegates are hooked up by {@link IBeanSearchServiceFactory} and requests for
 * proper services are delegated to the delegates.
 */
public interface IBeanSearchServiceFactoryDelegate {

	/**
	 * Returns the {@link IBeanSearchService} for the given type. If no proper
	 * implementation could be found, <code>null</code> must be returned.
	 * 
	 * @param bean
	 * @return
	 */
	<BEAN> IBeanSearchService<BEAN> createService(Class<BEAN> bean);

}
