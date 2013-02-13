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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.eclipse.equinox.http.servlet.ExtendedHttpService;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.FilterMapping;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.servlet.ServletMapping;
import org.lunifera.runtime.web.http.HttpConstants;
import org.lunifera.runtime.web.http.IHttpApplication;
import org.lunifera.runtime.web.jetty.IHandlerProvider;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleListener;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceFactory;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.http.HttpService;
import org.osgi.service.http.NamespaceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpApplication implements IHttpApplication {

	private static final Logger logger = LoggerFactory
			.getLogger(HttpApplication.class);

	private final HttpServiceServiceFactory httpServiceFactory = new HttpServiceServiceFactory();

	private BundleContext context;
	private ServiceRegistration<?> httpServiceRegistration;
	private boolean started;

	private ServletContextHandler servletContext;
	private Set<String> registeredAlias = new HashSet<String>();
	private final Map<Object, BundleResourceMonitor> bundleMonitors = new HashMap<Object, BundleResourceMonitor>();

	private final String id;
	private String name;
	private String contextPath = "/";

	private ServiceRegistration<IHandlerProvider> jettyHandlerProviderRegistration;

	private String jettyServer;

	public HttpApplication(String id, BundleContext context) {
		if (id == null) {
			throw new NullPointerException("Id must not be null!");
		}
		this.id = id;
		this.context = context;
	}

	/**
	 * @return the servletContext
	 */
	public ServletContextHandler getServletContext() {
		return servletContext;
	}

	@Override
	public String getId() {
		return id;
	}

	/**
	 * Returns the name of that http application.
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the context path this application will register a
	 * {@link HttpService} at.
	 * 
	 * @return
	 */
	public String getContextPath() {
		return contextPath;
	}

	@Override
	public String getJettyServer() {
		return jettyServer;
	}

	/**
	 * Sets the name of that application.
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Sets the context path of that application.
	 * 
	 * @param contextPath
	 */
	public void setContextPath(String contextPath) {
		this.contextPath = contextPath;
	}

	/**
	 * Sets the name of the jetty server this web application should be
	 * installed at.
	 * 
	 * @param jettyServer
	 *            the jettyServer to set
	 */
	public void setJettyServer(String jettyServer) {
		this.jettyServer = jettyServer;
	}

	public boolean isStarted() {
		return started;
	}

	/**
	 * Is called to start the application. All resources should be registered
	 * and the http service is registered.
	 */
	public void start() {
		if (started) {
			logger.debug("HttpApplication {} is already started", getName());
			return;
		}
		try {
			servletContext = new ServletContextHandler(this);

			internalStart();

			// register jetty handler
			//
			if (jettyHandlerProviderRegistration == null) {
				Hashtable<String, Object> properties = new Hashtable<String, Object>();
				properties.put(HttpConstants.APPLICATION_ID, getId());
				properties.put(HttpConstants.APPLICATION_NAME, getName());
				properties.put(HttpConstants.CONTEXT_PATH, getContextPath());
				if (getJettyServer() != null) {
					properties.put(HttpConstants.JETTY_SERVER_NAME,
							getJettyServer());
				}
				jettyHandlerProviderRegistration = context.registerService(
						IHandlerProvider.class, new JettyHandlerProvider(
								servletContext), properties);
			}

			// register http service
			//
			if (httpServiceRegistration == null) {
				Hashtable<String, Object> properties = new Hashtable<String, Object>();
				properties.put(HttpConstants.APPLICATION_ID, getId());
				properties.put(HttpConstants.APPLICATION_NAME, getName());
				properties.put(HttpConstants.CONTEXT_PATH, getContextPath());
				if (getJettyServer() != null) {
					properties.put(HttpConstants.JETTY_SERVER_NAME,
							getJettyServer());
				}
				httpServiceRegistration = context.registerService(
						new String[] { HttpService.class.getName(),
								ExtendedHttpService.class.getName() },
						httpServiceFactory, properties);
			}
		} finally {
			started = true;
		}
	}

	/**
	 * Should be overridden by sub classes to start the http application.
	 */
	protected void internalStart() {

	}

	/**
	 * Is called to stop the application. All resources should be unregistered
	 * and the http service will become disposed.
	 */
	public void stop() {
		if (!started) {
			logger.debug("HttpApplication {} not started", getName());
			return;
		}
		try {

			// reset bundle listener
			//
			for (BundleResourceMonitor monitor : bundleMonitors.values()) {
				monitor.remove();
			}
			bundleMonitors.clear();

			// reset servlet context
			//
			if (servletContext != null) {
				try {
					servletContext.stop();
				} catch (Exception e) {
					logger.error("{}", e);
				}
				servletContext = null;
			}

			// clear aliases
			//
			registeredAlias.clear();

			// unregister http service
			//
			if (httpServiceRegistration != null) {
				httpServiceRegistration.unregister();
				httpServiceRegistration = null;
			}

			// unregister jetty handler
			//
			if (jettyHandlerProviderRegistration != null) {
				jettyHandlerProviderRegistration.unregister();
				jettyHandlerProviderRegistration = null;
			}

			internalStop();
		} finally {
			started = false;
		}
	}

	/**
	 * Should be overridden by sub classes to stop the http application.
	 */
	protected void internalStop() {

	}

	/**
	 * Only for testing issues!!!
	 * 
	 * @param context
	 */
	protected void setBundleContext(BundleContext context) {
		this.context = context;
	}

	/**
	 * Registers the given servlet by the alias.
	 * 
	 * @param alias
	 * @param servlet
	 * @param initparams
	 * @throws ServletException
	 * @throws NamespaceException
	 */
	public void registerServlet(String alias, Servlet servlet,
			Map<String, String> initparams) throws ServletException,
			NamespaceException {
		registerAlias(alias);

		// track bundle de-activation
		addBundleResourceMonitor(alias, servlet.getClass());

		ServletHolder holder = new ServletHolder(servlet);
		if (initparams != null) {
			holder.setInitParameters(initparams);
		}
		servletContext.getServletHandler().addServletWithMapping(holder, alias);

		// start the servlet if it is necessary
		initializeServletIfNecessary(alias, holder);
	}

	/**
	 * Unregisters the resource registered under the given alias.
	 * 
	 * @param alias
	 */
	public void unregister(String alias) {
		unregisterAlias(alias);

		// remove bundle monitor
		removeBundleResourceMonitor(alias);

		// collect list of new mappings and remaining servlets
		final ServletHandler servletHandler = servletContext
				.getServletHandler();
		boolean removedSomething = false;
		final ServletMapping[] mappings = servletHandler.getServletMappings();
		final List<ServletMapping> newMappings = new ArrayList<ServletMapping>(
				mappings.length);
		final Set<String> mappedServlets = new HashSet<String>(mappings.length);
		for (final ServletMapping mapping : mappings) {
			final String[] pathSpecs = mapping.getPathSpecs();
			for (final String spec : pathSpecs) {
				if (alias.equals(spec)) {
					mapping.setPathSpecs(ArrayUtil.removeFromArray(
							mapping.getPathSpecs(), spec));
					removedSomething = true;
				}
			}
			if (mapping.getPathSpecs().length > 0) {
				newMappings.add(mapping);
				mappedServlets.add(mapping.getServletName());
			}
		}

		// sanity check
		if (!removedSomething)
			throw new IllegalStateException("alias '" + alias
					+ "' registered but nothing removed");

		// find servlets to remove
		final ServletHolder[] servlets = servletHandler.getServlets();
		final List<ServletHolder> servletsToRemove = new ArrayList<ServletHolder>(
				servlets.length);
		final List<ServletHolder> newServlets = new ArrayList<ServletHolder>(
				servlets.length);
		for (final ServletHolder servlet : servlets) {
			if (!mappedServlets.contains(servlet.getName())) {
				servletsToRemove.add(servlet);
			} else {
				newServlets.add(servlet);
			}
		}

		// update mappings and servlets
		servletHandler.setServlets(newServlets
				.toArray(new ServletHolder[newServlets.size()]));
		servletHandler.setServletMappings(newMappings
				.toArray(new ServletMapping[newMappings.size()]));

		// stop removed servlets
		for (final ServletHolder servlet : servletsToRemove) {
			try {
				servlet.doStop();
			} catch (final Exception e) {
				// ignore
			}
		}
	}

	/**
	 * Registers the given filter under the alias.
	 * 
	 * @param alias
	 * @param filter
	 * @param initparams
	 * @throws NamespaceException
	 */
	public void registerFilter(String alias, Filter filter,
			Map<String, String> initparams) throws NamespaceException {

		addBundleResourceMonitor(filter);

		// create holder
		final FilterHolder holder = new FilterHolder(filter);
		if (initparams != null) {
			holder.setInitParameters(initparams);
		}

		// register filter
		servletContext.getServletHandler().addFilterWithMapping(holder, alias,
				null);

	}

	/**
	 * Unregisters the given filter
	 * 
	 * @param filter
	 */
	public void unregisterFilter(Filter filter) {
		// collect list of remaining filters and filters to remove
		final ServletHandler servletHandler = servletContext
				.getServletHandler();
		final FilterHolder[] filters = servletHandler.getFilters();
		final List<FilterHolder> newfilters = new ArrayList<FilterHolder>(
				filters.length);
		final Set<FilterHolder> toRemove = new HashSet<FilterHolder>(
				filters.length);
		final Set<String> toRemoveNames = new HashSet<String>(filters.length);
		for (final FilterHolder filterHolder : filters) {
			if (filterHolder.getFilter() == filter) {
				toRemove.add(filterHolder);
				toRemoveNames.add(filterHolder.getName());
			} else {
				newfilters.add(filterHolder);
			}
		}

		// sanity check
		if (toRemove.isEmpty()) {
			throw new IllegalStateException("filter '" + filter + "' not found");
		}

		// collect remaining mappings
		final FilterMapping[] mappings = servletHandler.getFilterMappings();
		final List<FilterMapping> newMappings = new ArrayList<FilterMapping>(
				mappings.length);
		for (final FilterMapping mapping : mappings) {
			final String filterName = mapping.getFilterName();
			if (!toRemove.contains(filterName)) {
				newMappings.add(mapping);
			}
		}

		// update mappings and servlets
		servletHandler.setFilters(newfilters
				.toArray(new FilterHolder[newfilters.size()]));
		servletHandler.setFilterMappings(newMappings
				.toArray(new FilterMapping[newMappings.size()]));

		// stop removed filters
		for (final FilterHolder filterHolder : toRemove) {
			try {
				filterHolder.doStop();
			} catch (final Exception e) {
				// ignore
			}
		}
	}

	private void registerAlias(final String alias) throws NamespaceException {
		if (registeredAlias.contains(alias)) {
			logger.error("Alias {} was already registered!", alias);
			throw new NamespaceException(alias);
		}
		registeredAlias.add(alias);
	}

	private void unregisterAlias(final String alias) {
		if (!registeredAlias.contains(alias)) {
			logger.error("Alias {} was not registered!", alias);
			throw new IllegalArgumentException(String.format(
					"Alias %s was not registered", alias));
		}
		registeredAlias.remove(alias);
	}

	/**
	 * Initializes the servlet if it is necessary.
	 * 
	 * @param alias
	 * @param holder
	 * @throws ServletException
	 */
	private void initializeServletIfNecessary(final String alias,
			final ServletHolder holder) throws ServletException {
		if (servletContext.getServletHandler().isStarted()
				|| servletContext.getServletHandler().isStarting()) {
			try {
				holder.start();
			} catch (final Exception e) {
				// attempt a clean unregister
				try {
					unregister(alias);
				} catch (final Exception e2) {
					logger.error("{}", e);
				}
				// fail
				throw new ServletException(String.format(
						"Error starting servlet. %s", e.getMessage()), e);
			}
		}
	}

	/**
	 * Register a bundle listener that will unregister the alias if the bundle
	 * of the given class will stop.
	 * 
	 * @param alias
	 * @param bundleClass
	 */
	private void addBundleResourceMonitor(final String alias,
			final Class<?> bundleClass) {
		final Bundle bundle = FrameworkUtil.getBundle(bundleClass);
		if (bundle != null) {
			final BundleContext bundleContext = bundle.getBundleContext();
			if (null != bundleContext) {
				final BundleResourceMonitor monitor = new BundleResourceMonitor(
						alias, bundleContext);
				bundleMonitors.put(alias, monitor);
				monitor.activate();
			}
		}
	}

	/**
	 * Register a bundle listener that will unregister the alias if the bundle
	 * of the given class will stop.
	 * 
	 * @param alias
	 * @param bundleClass
	 */
	private void addBundleResourceMonitor(final Filter filter) {
		final Bundle bundle = FrameworkUtil.getBundle(filter.getClass());
		if (bundle != null) {
			final BundleContext bundleContext = bundle.getBundleContext();
			if (null != bundleContext) {
				final BundleResourceMonitor monitor = new BundleResourceMonitor(
						filter, bundleContext);
				bundleMonitors.put(filter, monitor);
				monitor.activate();
			}
		}
	}

	/**
	 * Removes the bundle listener that will unregister the alias if the bundle
	 * of the given class was unregistered.
	 * 
	 * @param alias
	 */
	private void removeBundleResourceMonitor(final String alias) {
		final BundleResourceMonitor monitor = bundleMonitors.remove(alias);
		if (monitor != null) {
			monitor.remove();
		}
	}

	/**
	 * An internal factory class to create and destroy http services.
	 */
	private class HttpServiceServiceFactory implements
			ServiceFactory<ExtendedHttpService> {
		@Override
		public ExtendedHttpService getService(Bundle bundle,
				ServiceRegistration<ExtendedHttpService> registration) {
			return new HttpServiceImpl(HttpApplication.this);
		}

		@Override
		public void ungetService(Bundle bundle,
				ServiceRegistration<ExtendedHttpService> registration,
				ExtendedHttpService service) {
			((HttpServiceImpl) service).destroy();
		}
	}

	private final class BundleResourceMonitor implements BundleListener {
		private final String alias;
		private final long bundleId;
		private final Filter filter;
		private BundleContext bundleContext;

		BundleResourceMonitor(final String alias,
				final BundleContext bundleContext) {
			this.alias = alias;
			this.filter = null;
			this.bundleContext = bundleContext;
			bundleId = bundleContext.getBundle().getBundleId();
		}

		BundleResourceMonitor(final Filter filter,
				final BundleContext bundleContext) {
			this.alias = null;
			this.filter = filter;
			this.bundleContext = bundleContext;
			bundleId = bundleContext.getBundle().getBundleId();
		}

		void activate() {
			try {
				bundleContext.addBundleListener(this);
			} catch (final Exception e) {
				// ignore
			}
		}

		@Override
		public void bundleChanged(final BundleEvent event) {
			if (bundleId != event.getBundle().getBundleId())
				// ignore events for different bundles
				// (clarify if we should ever get those here; I got a stacktrace
				// once that indicates this)
				return;
			if (event.getType() == Bundle.STOPPING) {
				try {
					if (filter != null) {
						unregisterFilter(filter);
					} else {
						unregister(alias);
					}
				} catch (final Exception e) {
					// ignore
				} finally {
					remove();
				}
			}
		}

		void remove() {
			try {
				bundleContext.removeBundleListener(this);
			} catch (final Exception e) {
				// ignore
			}
			bundleContext = null;
		}
	}
}
