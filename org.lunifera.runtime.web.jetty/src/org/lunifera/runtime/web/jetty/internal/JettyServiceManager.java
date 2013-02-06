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
package org.lunifera.runtime.web.jetty.internal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Dictionary;
import java.util.List;
import java.util.Map;

import org.lunifera.runtime.web.jetty.IJettyService;
import org.lunifera.runtime.web.jetty.IJettyServiceManager;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JettyServiceManager implements IJettyServiceManager,
		ManagedService {

	private static final Logger logger = LoggerFactory
			.getLogger(JettyServiceManager.class);

	private List<IJettyService> jettys = Collections
			.synchronizedList(new ArrayList<IJettyService>());

	/**
	 * Called by OSGi-DS
	 * 
	 * @param context
	 */
	protected void activate(ComponentContext context) {
		logger.debug("JettyServiceManager activated");
	}

	/**
	 * Called by OSGi-DS
	 * 
	 * @param context
	 */
	protected void deactivate(ComponentContext context) {
		logger.debug("JettyServiceManager activated");
	}

	@Override
	public void updated(Dictionary<String, ?> properties)
			throws ConfigurationException {

	}

	@Override
	public IJettyService getJetty(String id) {
		for (IJettyService jetty : jettys.toArray(new IJettyService[jettys
				.size()])) {
			if (jetty.getId().intern() == id.intern()) {
				return jetty;
			}
		}
		return null;
	}

	/**
	 * Called by OSGi-DS to add an jetty to this manager.
	 * 
	 * @param jetty
	 * @param properties
	 */
	protected void addJetty(IJettyService jetty, Map<String, Object> properties) {

		if (!jettys.contains(jetty)) {
			jettys.add(jetty);
		}

		jetty.start();
	}

	/**
	 * Called by OSGi-DS to remove an jetty from this manager.
	 * 
	 * @param jetty
	 * @param properties
	 */
	protected void removeJetty(IJettyService jetty,
			Map<String, Object> properties) {

		jettys.remove(jetty);

		jetty.stop();
	}

}
