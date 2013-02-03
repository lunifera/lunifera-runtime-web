package org.lunifera.runtime.web.common.tests;

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
