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
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JettyService implements IJettyService, ManagedService {

	private static final int DEFAULT_PORT = 8080;

	private static final Logger logger = LoggerFactory
			.getLogger(JettyService.class);

	private BundleContext bundleContext;
	private ServiceRegistration<?> httpServiceRegistration;
	private ServiceRegistration<?> managedServiceRegistration;
	private boolean started;

	private Set<String> registeredAlias = new HashSet<String>();

	private String id;
	private String name;
	private int port = DEFAULT_PORT;

	@Override
	public String getId() {
		return id;
	}

	@Override
	public int getPort() {
		return port;
	}

	/**
	 * Returns the prot for the given properties.
	 * 
	 * @param properties
	 * @return
	 */
	private int getPort(Map<String, Object> properties) {
		int port = DEFAULT_PORT;
		String portString = (String) properties.get(OSGI__PORT);
		if (portString == null) {
			logger.warn("Using default port {} for {}", DEFAULT_PORT, getId());
		} else {
			try {
				port = Integer.valueOf(portString);
			} catch (NumberFormatException e) {
				logger.warn("Using default port {} for {}", DEFAULT_PORT,
						getId());
			}
		}
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
			ensureId();

			internalStart();

			// register managed service
			//
			if (managedServiceRegistration == null) {
				Hashtable<String, Object> properties = new Hashtable<String, Object>();
				properties.put(Constants.SERVICE_PID, OSGI__PID);
				properties.put(OSGI__ID, getId());
				if (getName() != null && !getName().equals("")) {
					properties.put(OSGI__NAME, getName());
				}
				properties.put(OSGI__PORT, String.valueOf(getPort()));
				managedServiceRegistration = bundleContext.registerService(
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
		this.bundleContext = context.getBundleContext();
		initialize(properties);
	}

	/**
	 * Initializes the http application. Can be used to if not instantiated by
	 * OSGi-DS.
	 * 
	 * @param properties
	 */
	public void initialize(Dictionary<?, ?> properties) {
		initialize(toMap(properties));
	}

	/**
	 * Initializes the jetty service. Can be used to if not instantiated by
	 * OSGi-DS.
	 * 
	 * @param properties
	 */
	public void initialize(Map<String, Object> properties) {
		updateFromProperties(true, properties);
	}

	/**
	 * Updates the internal values from the properties
	 * 
	 * @param force
	 *            if true, then all values will be updated. Otherwise only
	 *            values that are contained in the properties. Also the ID will
	 *            be updated.
	 */
	private void updateFromProperties(boolean force,
			Map<String, Object> properties) {
		if (properties != null) {
			if (force) {
				this.id = (String) properties.get(OSGI__ID);
				this.name = (String) properties.get(OSGI__NAME);
				this.port = getPort(properties);
			} else {
				if (properties.containsKey(OSGI__ID)) {
					this.name = (String) properties.get(OSGI__NAME);
				}
				if (properties.containsKey(OSGI__PORT)) {
					this.port = getPort(properties);
				}
			}
		}

		ensureId();
	}

	/**
	 * Ensures that an id is specified.
	 */
	private void ensureId() {
		if (this.id == null || this.id.equals("")) {
			// may happen if service was instantiated manually
			this.id = UUID.randomUUID().toString();
		}
	}

	/**
	 * Called by OSGi-DS.
	 * 
	 * @param context
	 * @param properties
	 */
	protected void deactivate(ComponentContext context,
			Map<String, Object> properties) {
		try {
			if (started) {
				stop();
			}
		} finally {
			context = null;
			bundleContext = null;
		}
	}

	/**
	 * Only for testing issues!!!
	 * 
	 * @param context
	 */
	protected void setBundleContext(BundleContext context) {
		this.bundleContext = context;
	}

	@Override
	public void updated(Dictionary<String, ?> properties)
			throws ConfigurationException {
		boolean oldStarted = started;

		stop();

		// update the value from the properties
		updateFromProperties(false, toMap(properties));

		if (oldStarted) {
			start();
		}
	}

	/**
	 * Maps the params to a map.
	 * 
	 * @param input
	 * @return
	 */
	private Map<String, Object> toMap(final Dictionary<?, ?> input) {
		if (input == null) {
			return null;
		}

		final HashMap<String, Object> result = new HashMap<String, Object>(
				input.size());
		final Enumeration<?> keys = input.keys();
		while (keys.hasMoreElements()) {
			final Object key = keys.nextElement();
			try {
				result.put((String) key, (String) input.get(key));
			} catch (final ClassCastException e) {
				throw new IllegalArgumentException("Only strings are allowed",
						e);
			}
		}
		return result;
	}

}
