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
package org.lunifera.runtime.web.http.internal;

import java.util.Collections;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.lunifera.runtime.web.http.Constants;
import org.lunifera.runtime.web.http.HttpApplication;
import org.lunifera.runtime.web.http.IHttpApplication;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedServiceFactory;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ManagedServiceFactories that creates new instances of HttpApplication.
 */
public class HttpApplicationFactory implements ManagedServiceFactory {

	private static final Logger logger = LoggerFactory
			.getLogger(HttpApplicationFactory.class);

	private Map<String, HttpApplication> applications = Collections
			.synchronizedMap(new HashMap<String, HttpApplication>());
	private Map<String, ServiceRegistration<IHttpApplication>> registrations = Collections
			.synchronizedMap(new HashMap<String, ServiceRegistration<IHttpApplication>>());

	private int lastServiceId = 0;
	private ComponentContext context;

	/**
	 * Called by OSGi-DS.
	 * 
	 * @param context
	 * @param properties
	 */
	protected void activate(ComponentContext context,
			Map<String, Object> properties) {
		logger.debug("{} started", getName());
		this.context = context;
	}

	/**
	 * Called by OSGi-DS.
	 * 
	 * @param context
	 * @param properties
	 */
	protected void deactivate(ComponentContext context,
			Map<String, Object> properties) {

		this.context = null;

		for (String pid : applications.keySet().toArray(
				new String[applications.size()])) {
			deleted(pid);
		}

		logger.debug("{} stopped", getName());
	}

	@Override
	public String getName() {
		return "HttpApplication Factory";
	}

	@Override
	public void updated(String pid, Dictionary<String, ?> properties)
			throws ConfigurationException {

		HttpApplication application = (HttpApplication) applications.get(pid);
		if (application == null) {
			application = new HttpApplication(
					Integer.toString(++lastServiceId),
					context.getBundleContext());
		} else {
			application.stop();
		}

		String name = Constants.DEFAULT_APPLICATION_NAME;
		if (properties.get(Constants.OSGI__APPLICATION_NAME) != null) {
			name = (String) properties.get(Constants.OSGI__APPLICATION_NAME);
		}
		application.setName(name);
		application.setContextPath(getContextPath(properties));

		Dictionary<String, Object> copyProps = copy(properties);
		copyProps.put(Constants.OSGI__APPLICATION_ID, application.getId());

		// Register application as service and add to cache
		//
		if (!applications.containsKey(pid)) {
			applications.put(pid, application);

			// register http application as a service
			ServiceRegistration<IHttpApplication> registration = context
					.getBundleContext().registerService(IHttpApplication.class,
							application, copyProps);
			registrations.put(pid, registration);
		} else {
			ServiceRegistration<IHttpApplication> registration = registrations
					.get(pid);
			registration.setProperties(copyProps);
		}

		// start the application again
		//
		application.start();
		logger.debug("New HttpApplication {} started on context path {}",
				application.getName(), application.getContextPath());
	}

	/**
	 * Maps the params to a map.
	 * 
	 * @param input
	 * @return
	 */
	private Dictionary<String, Object> copy(final Dictionary<?, ?> input) {
		if (input == null) {
			return null;
		}

		final Hashtable<String, Object> result = new Hashtable<String, Object>(
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

	@Override
	public void deleted(String pid) {
		HttpApplication application = applications.remove(pid);
		if (application != null) {
			application.stop();

			logger.debug("HttpApplication {} stopped on context path {}",
					application.getName(), application.getContextPath());
		}

		// unregister http application as a service
		ServiceRegistration<IHttpApplication> registration = registrations
				.remove(pid);
		if (registration != null) {
			registration.unregister();
			logger.debug("HttpApplication {} removed as a service!",
					application.getName());
		}

	}

	/**
	 * Returns the context path for the given properties.
	 * 
	 * @param properties
	 * @return
	 */
	private String getContextPath(Dictionary<String, ?> properties) {
		String contextPath = (String) properties
				.get(Constants.OSGI__APPLICATION_CONTEXT_PATH);
		if (contextPath == null) {
			contextPath = "/";
		} else {
			if (!contextPath.startsWith("/")) {
				contextPath = "/" + contextPath;
			}
		}
		return contextPath;
	}
}
