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
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.lunifera.runtime.web.vaadin.osgi.common.IVaadinApplication;
import org.lunifera.runtime.web.vaadin.osgi.common.VaadinConstants;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedServiceFactory;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.ComponentFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.ui.UI;

/**
 * ManagedServiceFactories that creates new instances of VaadinApplication.
 */
public class VaadinApplicationFactory implements ManagedServiceFactory {

	private static final Logger logger = LoggerFactory
			.getLogger(VaadinApplicationFactory.class);

	private final Lock accessLock = new ReentrantLock();

	private Map<String, IVaadinApplication> applications = Collections
			.synchronizedMap(new HashMap<String, IVaadinApplication>());
	private Map<String, ServiceRegistration<IVaadinApplication>> registrations = Collections
			.synchronizedMap(new HashMap<String, ServiceRegistration<IVaadinApplication>>());

	private int lastServiceId = 0;
	private ComponentContext context;

	private final Map<ComponentFactory, OSGiUIProvider> providerMapping = new HashMap<ComponentFactory, OSGiUIProvider>();

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

		try {
			accessLock.lock();
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
				name = (String) properties
						.get(VaadinConstants.APPLICATION_NAME);
			}

			String httpApplicationName = null;
			if (properties.get(VaadinConstants.HTTP_APPLICATION_NAME) != null) {
				httpApplicationName = (String) properties
						.get(VaadinConstants.HTTP_APPLICATION_NAME);
			}

			String widgetsetName = null;
			if (properties.get(VaadinConstants.WIDGETSET) != null) {
				widgetsetName = (String) properties
						.get(VaadinConstants.WIDGETSET);
			}

			String uialias = null;
			if (properties.get(VaadinConstants.UI_ALIAS) != null) {
				uialias = (String) properties.get(VaadinConstants.UI_ALIAS);
			}

			application.setName(name);
			application.setWidgetSetName(widgetsetName);
			application.setHttpApplication(httpApplicationName);
			application.setUIAlias(uialias);

			// add the UI provider
			for (OSGiUIProvider provider : providerMapping.values()) {
				application.addUIProvider(provider);
			}

			Dictionary<String, Object> copyProps = copy(properties);
			copyProps.put(VaadinConstants.APPLICATION_ID, application.getId());

			// Register application as service and add to cache
			//
			if (!applications.containsKey(pid)) {
				applications.put(pid, application);

				// register http application as a service
				ServiceRegistration<IVaadinApplication> registration = context
						.getBundleContext().registerService(
								IVaadinApplication.class, application,
								copyProps);
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

		} finally {
			accessLock.unlock();
		}
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

	/**
	 * Called by OSGi-DS and adds a component factory that is used to create
	 * instances of UIs.
	 * 
	 * @param reference
	 */
	@SuppressWarnings("unchecked")
	protected void addUIFactory(ServiceReference<ComponentFactory> reference) {
		ComponentFactory factory = reference.getBundle().getBundleContext()
				.getService(reference);
		String name = (String) reference.getProperty("component.factory");
		String[] tokens = name.split("/");
		if (tokens.length != 2) {
			logger.error(
					"UiFactory {} does not meet syntax. (org.lunifera.web.vaadin.UI/[uiclass]@[vaadinApp]) Eg: org.lunifera.web.vaadin.UI/org.lunifera.examples.runtime.web.vaadin.standalone.Vaadin7StandaloneDemoUI@StandaloneDemo",
					name);
			return;
		}

		String className;
		String vaadinApplication;
		String[] innerTokens = tokens[1].split("@");
		if (innerTokens.length == 1) {
			className = innerTokens[0];
			vaadinApplication = null;
		} else {
			className = innerTokens[0];
			vaadinApplication = innerTokens[1];
		}

		OSGiUIProvider uiProvider = null;
		try {
			uiProvider = new OSGiUIProvider(factory,
					(Class<? extends UI>) reference.getBundle().loadClass(
							className), vaadinApplication);
		} catch (ClassNotFoundException e) {
			throw new IllegalStateException(e);
		}
		if (uiProvider != null) {
			providerMapping.put(factory, uiProvider);
		}

		// handle added ui provider
		uiProviderAdded(uiProvider);
	}

	/**
	 * Called by OSGi-DS and removes a component factory that is used to create
	 * instances of UIs.
	 * 
	 * @param factory
	 */
	protected void removeUIFactory(ComponentFactory factory,
			Map<String, Object> properties) {
		OSGiUIProvider uiProvider = providerMapping.remove(factory);
		if (uiProvider != null) {
			uiProviderRemoved(uiProvider);
		}
	}

	/**
	 * Update the web application with the new UI provider.
	 * 
	 * @param uiProvider
	 */
	private void uiProviderAdded(OSGiUIProvider uiProvider) {
		try {
			accessLock.lock();
			for (IVaadinApplication application : applications.values()) {
				if (uiProvider.getVaadinApplication() == null) {
					application.addUIProvider(uiProvider);
				} else {
					if (uiProvider.getVaadinApplication().equals(
							application.getName())) {
						application.addUIProvider(uiProvider);
					}
				}
			}
		} finally {
			accessLock.unlock();
		}
	}

	/**
	 * Remove UI provider from the web application.
	 * 
	 * @param uiProvider
	 */
	private void uiProviderRemoved(OSGiUIProvider uiProvider) {
		try {
			accessLock.lock();
			for (IVaadinApplication application : applications.values()) {
				application.removeUIProvider(uiProvider);
			}
		} finally {
			accessLock.unlock();
		}
	}
}
