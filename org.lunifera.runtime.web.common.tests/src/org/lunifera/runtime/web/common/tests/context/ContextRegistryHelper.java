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

package org.lunifera.runtime.web.common.tests.context;

import org.lunifera.runtime.web.common.IWebContextRegistry;

public class ContextRegistryHelper {

	static IWebContextRegistry registry;

	/**
	 * Called by OSGi-DS
	 * 
	 * @param registry
	 */
	public void setRegistry(IWebContextRegistry registry) {
		ContextRegistryHelper.registry = registry;
	}

	/**
	 * Called by OSGi-DS
	 * 
	 * @param registry
	 */
	public void unsetRegistry(IWebContextRegistry registry) {
		ContextRegistryHelper.registry = null;
	}

}
