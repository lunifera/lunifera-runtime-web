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
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.lunifera.runtime.web.vaadin.osgi.common.IOSGiUiProviderFactory;
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

	private Map<String, VaadinApplication> applications = Collections
			.synchronizedMap(new HashMap<String, VaadinApplication>());
	private Map<String, ServiceRegistration<IVaadinApplication>> registrations = Collections
			.synchronizedMap(new HashMap<String, ServiceRegistration<IVaadinApplication>>());

	private int lastServiceId = 0;
	private ComponentContext context;

	private List<IOSGiUiProviderFactory> providerFactories = new CopyOnWriteArrayList<IOSGiUiProviderFactory>();
	private List<UiFactoryWrapper> uiFactories = new CopyOnWriteArrayList<UiFactoryWrapper>();

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

	/**
	 * Returns true, if the factory is active.
	 * 
	 * @return
	 */
	protected boolean isActive() {
		return context != null;
	}

	@Override
	public String getName() {
		return "IVaadinApplication Factory";
	}

	@Override
	public void updated(String pid, Dictionary<String, ?> properties)
			throws ConfigurationException {

		System.out.println("Update");
		logger.debug("Calling update");

		try {
			accessLock.lock();
			VaadinApplication application = applications.get(pid);
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

			boolean productionMode = false;
			if (properties.get(VaadinConstants.PRODUCTIONMODE) != null) {
				productionMode = Boolean.valueOf((String) properties
						.get(VaadinConstants.PRODUCTIONMODE));
			}

			application.setName(name);
			application.setWidgetSetName(widgetsetName);
			application.setHttpApplication(httpApplicationName);
			application.setUIAlias(uialias);
			application.setProductionMode(productionMode);

			// create and set the UI provider to the application
			OSGiUIProvider provider = createUIProvider(application.getName());
			if (provider != null) {
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
	 * Creates the UI provider for the given application name.
	 * 
	 * @param applicationName
	 * @return
	 */
	protected OSGiUIProvider createUIProvider(String applicationName) {

		// iterate all the factories with match application name
		OSGiUIProvider provider = null;
		for (UiFactoryWrapper factory : uiFactories) {
			if (applicationName.equals(factory.vaadinApplication)) {
				provider = factory.createProvider();
				break;
			}
		}

		// if no UI provider was found, try to find a factory with empty
		// application name
		if (provider == null) {
			for (UiFactoryWrapper factory : uiFactories) {
				if (factory.vaadinApplication == null) {
					provider = factory.createProvider();
					break;
				}
			}
		}
		return provider;
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
	 * Adds the OSGi UI provider factory.
	 * 
	 * @param providerFactory
	 */
	protected void addUIProviderFactory(IOSGiUiProviderFactory providerFactory) {
		providerFactories.add(providerFactory);
	}

	/**
	 * Removes the OSGi UI provider factory.
	 * 
	 * @param providerFactory
	 */
	protected void removeUIProviderFactory(
			IOSGiUiProviderFactory providerFactory) {
		providerFactories.remove(providerFactory);
	}

	/**
	 * Called by OSGi-DS and adds a component factory that is used to create
	 * instances of UIs.
	 * 
	 * @param reference
	 */
	protected void addUIFactory(ServiceReference<ComponentFactory> reference) {
		try {
			accessLock.lock();

			logger.debug("Adding ui factory");

			// parse the target of service
			//
			String name = (String) reference.getProperty("component.factory");
			String[] tokens = name.split("/");
			if (tokens.length != 2) {
				logger.error(
						"UiFactory {} does not meet syntax. (org.lunifera.web.vaadin.UI/[uiclass]@[vaadinApp]) Eg: org.lunifera.web.vaadin.UI/org.lunifera.examples.runtime.web.vaadin.standalone.Vaadin7StandaloneDemoUI@StandaloneDemo",
						name);
				return;
			}

			String uiClassName;
			String vaadinApplication;
			String[] innerTokens = tokens[1].split("@");
			if (innerTokens.length == 1) {
				uiClassName = innerTokens[0];
				vaadinApplication = null;
			} else {
				uiClassName = innerTokens[0];
				vaadinApplication = innerTokens[1];
			}

			// register factory internally
			//
			uiFactories.add(new UiFactoryWrapper(vaadinApplication,
					uiClassName, reference));

		} finally {
			accessLock.unlock();
		}
	}

	/**
	 * Called by OSGi-DS and removes a component factory that is used to create
	 * instances of UIs.
	 * 
	 * @param factory
	 */
	protected void removeUIFactory(ComponentFactory factory,
			Map<String, Object> properties) {
		try {
			accessLock.lock();
			for (VaadinApplication application : applications.values()) {
				application.removeUIProviderForFactory(factory);
			}
		} finally {
			accessLock.unlock();
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
	 * A wrapper class to keep information about pending factory. Pending means
	 * that the service was not activated yet.
	 */
	private class UiFactoryWrapper {

		private final String vaadinApplication;
		private final ServiceReference<ComponentFactory> reference;
		private final String uiClassName;

		public UiFactoryWrapper(String vaadinApplication, String uiClassName,
				ServiceReference<ComponentFactory> reference) {
			super();
			this.vaadinApplication = vaadinApplication;
			this.uiClassName = uiClassName;
			this.reference = reference;
		}

		@SuppressWarnings("unchecked")
		public OSGiUIProvider createProvider() {
			ComponentFactory uiFactory = reference.getBundle()
					.getBundleContext().getService(reference);

			Class<? extends UI> uiClass = null;
			try {
				uiClass = (Class<? extends UI>) reference.getBundle()
						.loadClass(uiClassName);
			} catch (ClassNotFoundException e) {
				throw new IllegalStateException(e);
			}

			// iterates all UI provider factories to find a proper UI provider
			OSGiUIProvider uiProvider = null;
			for (IOSGiUiProviderFactory providerFactory : providerFactories) {
				uiProvider = providerFactory.createUiProvider(
						vaadinApplication, uiClass);
				if (uiProvider == null) {
					continue;
				}
				uiProvider.setUIFactory(uiFactory);
				break;
			}

			// If no provider was found for the given vaadin application, then
			// add a default one
			if (uiProvider == null) {
				uiProvider = new OSGiUIProvider(vaadinApplication, uiClass);
				uiProvider.setUIFactory(uiFactory);
			}

			// handle added UI provider
			uiProviderAdded(uiProvider);

			return uiProvider;
		}
	}
}
