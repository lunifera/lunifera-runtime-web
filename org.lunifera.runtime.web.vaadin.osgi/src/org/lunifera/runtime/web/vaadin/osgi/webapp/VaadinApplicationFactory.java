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
package org.lunifera.runtime.web.vaadin.osgi.webapp;

import java.util.Collections;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.lunifera.runtime.web.http.HttpConstants;
import org.lunifera.runtime.web.vaadin.osgi.common.IVaadinApplication;
import org.lunifera.runtime.web.vaadin.osgi.common.VaadinConstants;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedServiceFactory;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ManagedServiceFactories that creates new instances of VaadinApplication.
 */
public class VaadinApplicationFactory implements ManagedServiceFactory {

	private static final Logger logger = LoggerFactory
			.getLogger(VaadinApplicationFactory.class);

	private Map<String, IVaadinApplication> applications = Collections
			.synchronizedMap(new HashMap<String, IVaadinApplication>());
	private Map<String, ServiceRegistration<IVaadinApplication>> registrations = Collections
			.synchronizedMap(new HashMap<String, ServiceRegistration<IVaadinApplication>>());

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
		return "IVaadinApplication Factory";
	}

	@Override
	public void updated(String pid, Dictionary<String, ?> properties)
			throws ConfigurationException {

		VaadinApplication application = (VaadinApplication) applications
				.get(pid);
		if (application == null) {
			application = new VaadinApplication(
					Integer.toString(++lastServiceId),
					context.getBundleContext());
		} else {
			application.stop();
		}

		String name = VaadinConstants.DEFAULT_APPLICATION_NAME;
		if (properties.get(VaadinConstants.APPLICATION_NAME) != null) {
			name = (String) properties.get(VaadinConstants.APPLICATION_NAME);
		}

		String httpApplicationName = null;
		if (properties.get(VaadinConstants.HTTP_APPLICATION_NAME) != null) {
			httpApplicationName = (String) properties
					.get(VaadinConstants.HTTP_APPLICATION_NAME);
		}

		String widgetsetName = null;
		if (properties.get(VaadinConstants.WIDGETSET) != null) {
			widgetsetName = (String) properties.get(VaadinConstants.WIDGETSET);
		}

		String uialias = null;
		if (properties.get(VaadinConstants.UI_ALIAS) != null) {
			uialias = (String) properties.get(VaadinConstants.UI_ALIAS);
		}

		application.setName(name);
		application.setWidgetSetName(widgetsetName);
		application.setHttpApplication(httpApplicationName);
		application.setUIAlias(uialias);

		Dictionary<String, Object> copyProps = copy(properties);
		copyProps.put(VaadinConstants.APPLICATION_ID, application.getId());

		// Register application as service and add to cache
		//
		if (!applications.containsKey(pid)) {
			applications.put(pid, application);

			// register http application as a service
			ServiceRegistration<IVaadinApplication> registration = context
					.getBundleContext().registerService(
							IVaadinApplication.class, application, copyProps);
			registrations.put(pid, registration);
		} else {
			ServiceRegistration<IVaadinApplication> registration = registrations
					.get(pid);
			registration.setProperties(copyProps);
		}

		// start the application again
		//
		application.start();
		logger.debug(
				"New IVaadinApplication {} deployed on 'http application' {}",
				application.getName(), application.getHttpApplication());
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
		IVaadinApplication application = applications.remove(pid);
		if (application != null) {
			application.destroy();

			logger.debug(
					"IVaadinApplication {} deployed on http application {}",
					application.getName(), application.getHttpApplication());
		}

		// unregister http application as a service
		ServiceRegistration<IVaadinApplication> registration = registrations
				.remove(pid);
		if (registration != null) {
			registration.unregister();
			logger.debug("IVaadinApplication {} removed as a service!",
					application.getName());
		}

	}
}
