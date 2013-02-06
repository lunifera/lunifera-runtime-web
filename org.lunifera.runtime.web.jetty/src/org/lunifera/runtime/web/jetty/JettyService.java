/**
 * Copyright (c) 2012 Committers of lunifera.org.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Based on org.eclipse.gyrex.http.jetty.internal.app.ApplicationContext (Gunnar Wagenknecht)
 * 
 * Contributors:
 *    Florian Pirchner - initial API and implementation
 */
package org.lunifera.runtime.web.jetty;

import java.util.Dictionary;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JettyService implements IJettyService, ManagedService {

	private static final Logger logger = LoggerFactory
			.getLogger(JettyService.class);

	private String id;
	private String name;
	private int port = 8080;
	private BundleContext context;
	private ServiceRegistration<?> httpServiceRegistration;
	private ServiceRegistration<?> managedServiceRegistration;
	private boolean started;

	private Set<String> registeredAlias = new HashSet<String>();

	@Override
	public String getId() {
		return id;
	}

	@Override
	public int getPort() {
		return port;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public boolean isStarted() {
		return started;
	}

	@Override
	public void start() {
		if (started) {
			logger.debug("JettyServer {} is already started", getId());
			return;
		}
		try {
			internalStart();

			// register managed service
			//
			if (managedServiceRegistration == null) {
				Hashtable<String, Object> properties = new Hashtable<String, Object>();
				properties.put(Constants.SERVICE_PID, OSGI__PID);
				properties.put(OSGI__ID, getId());
				properties.put(OSGI__NAME, getName());
				properties.put(OSGI__PORT, String.valueOf(getPort()));
				managedServiceRegistration = context.registerService(
						ManagedService.class.getName(), this, properties);
			}

		} finally {
			started = true;
		}
	}

	protected void internalStart() {

	}

	@Override
	public void stop() {
		if (!started) {
			logger.debug("JettyServer {} not started", getId());
			return;
		}
		try {

			// clear aliases
			//
			registeredAlias.clear();

			// unregister the managed service
			//
			if (managedServiceRegistration != null) {
				managedServiceRegistration.unregister();
				managedServiceRegistration = null;
			}

			// unregister http service
			//
			if (httpServiceRegistration != null) {
				httpServiceRegistration.unregister();
				httpServiceRegistration = null;
			}

			internalStop();
		} finally {
			started = false;
		}
	}

	protected void internalStop() {

	}

	/**
	 * Called by OSGi-DS.
	 * 
	 * @param context
	 * @param properties
	 */
	protected void activate(ComponentContext context,
			Map<String, Object> properties) {
		this.context = context.getBundleContext();
		id = (String) properties.get(Constants.SERVICE_ID);
	}

	/**
	 * Called by OSGi-DS.
	 * 
	 * @param context
	 * @param properties
	 */
	protected void deactivate(ComponentContext context,
			Map<String, Object> properties) {
		if (started) {
			stop();
		}
	}

	/**
	 * Only for testing issues!!!
	 * 
	 * @param context
	 */
	protected void setBundleContext(BundleContext context) {
		this.context = context;
	}

	@Override
	public void updated(Dictionary<String, ?> properties)
			throws ConfigurationException {
		boolean oldStarted = started;

		stop();

		name = (String) properties.get(OSGI__NAME);
		String portString = (String) properties.get(OSGI__PORT);
		if (portString == null) {
			logger.warn("Using default port 8080 for {}", getName());
			this.port = 8080;
		} else {
			try {
				this.port = Integer.valueOf(portString);
			} catch (NumberFormatException e) {
				logger.warn("Using default port 8080 for {}", getName());
				this.port = 8080;
			}
		}

		if (oldStarted) {
			start();
		}
	}
}
