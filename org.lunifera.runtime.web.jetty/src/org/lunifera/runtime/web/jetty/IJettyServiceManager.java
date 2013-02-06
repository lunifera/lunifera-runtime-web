/**
 * Copyright (c) 2012 Committers of lunifera.org.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *    Florian Pirchner - initial API and implementation
 */
package org.lunifera.runtime.web.jetty;

import java.util.Dictionary;

import org.osgi.service.cm.ConfigurationException;

/**
 * A manager that manages all jetty services.
 */
public interface IJettyServiceManager {

	public static final String OSGI__PID = "org.lunifera.runtime.web.jetty.service.manager";

	/**
	 * Updates the manager with the given properties.
	 * 
	 * @param properties
	 */
	void updated(Dictionary<String, ?> properties)
			throws ConfigurationException;

	/**
	 * Returns the jetty service with the given id.
	 * 
	 * @param id
	 * @return
	 */
	IJettyService getJetty(String id);
}
