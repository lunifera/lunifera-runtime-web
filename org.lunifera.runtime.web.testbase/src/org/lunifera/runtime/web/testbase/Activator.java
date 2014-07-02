/**
 * Copyright (c) 2011 - 2014, Lunifera GmbH (Gross Enzersdorf), Loetz KG (Heidelberg)
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: 
 * 		Florian Pirchner - Initial implementation
 */
package org.lunifera.runtime.web.testbase;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.ServletException;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleListener;
import org.osgi.framework.ServiceReference;
import org.osgi.service.http.HttpService;
import org.osgi.service.http.NamespaceException;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

/**
 * Activator is used by OSGi framework to notify about the start and stop of the
 * bundle. The activator will look for the HttpService and registers the vaadin
 * servlet at it.
 */
public class Activator implements BundleActivator,
		ServiceTrackerCustomizer<HttpService, HttpService>, BundleListener {

	private static final String LUNIFERA_TESTBASE = "Lunifera-Testbase";
	public static final String URI_ALIAS = "/";
	private static BundleContext context;
	private static Activator instance;

	static BundleContext getContext() {
		return context;
	}

	/**
	 * Returns all registered test base bundles.
	 * 
	 * @return
	 */
	public static List<Bundle> getTestBaseBundles() {
		return new ArrayList<Bundle>(instance.testBases);
	}

	// used to track the HttpService
	private ServiceTracker<HttpService, HttpService> tracker;
	// used to register servlets
	private HttpService httpService;
	private ResourceProvider resourceProvider;
	private List<Bundle> testBases = new ArrayList<Bundle>();

	//
	// Helper methods to get an instance of the http service
	//
	@Override
	public HttpService addingService(ServiceReference<HttpService> reference) {
		httpService = context.getService(reference);

		try {
			// register the servlet at the http service
			httpService.registerServlet(URI_ALIAS, new TestbaseVaadinServlet(),
					null, resourceProvider);
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (NamespaceException e) {
			e.printStackTrace();
		}

		return httpService;
	}

	@Override
	public void removedService(ServiceReference<HttpService> reference,
			HttpService service) {
		// unregister the servlet from the http service
		httpService.unregister("URI_ALIAS");
	}

	@Override
	public void modifiedService(ServiceReference<HttpService> reference,
			HttpService service) {

	}

	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		instance = this;

		resourceProvider = new ResourceProvider();

		handleStartedBundles(context);

		// register this instance as a bundle listener to an reference to all
		// vaadin bundles. Used to find the static resources.
		bundleContext.addBundleListener(this);

		// Start a HttpService-Tracker to get an instance of HttpService
		tracker = new ServiceTracker<HttpService, HttpService>(bundleContext,
				HttpService.class, this);
		tracker.open();
	}

	public void stop(BundleContext bundleContext) throws Exception {
		// close the HttpService-tracker
		tracker.close();
		tracker = null;

		resourceProvider = null;

		bundleContext.removeBundleListener(this);

		instance = null;
		Activator.context = null;
	}

	/**
	 * Tries to find proper started bundles and adds them to resource provider.
	 * Since bundle changed listener will not find them.
	 * 
	 * @param context
	 */
	protected void handleStartedBundles(BundleContext context) {
		for (Bundle bundle : context.getBundles()) {
			String name = bundle.getSymbolicName();
			if (bundle.getState() == Bundle.ACTIVE
					&& name.startsWith("com.vaadin")) {
				resourceProvider.add(bundle);
			} else {
				Enumeration<String> keys = bundle.getHeaders().keys();
				while (keys.hasMoreElements()) {
					String key = keys.nextElement();
					if (key.equals(LUNIFERA_TESTBASE)) {
						testBases.add(bundle);
					}
				}
			}
		}
	}

	@Override
	public void bundleChanged(BundleEvent event) {
		Bundle bundle = event.getBundle();
		// tracks the starting and stopping of vaadin bundles. If a bundle is a
		// vaadin bundle it will be added to the resource provider for lookups.
		String name = event.getBundle().getSymbolicName();
		if (name.startsWith("com.vaadin")) {
			if (event.getType() == BundleEvent.STARTED) {
				resourceProvider.add(event.getBundle());
			} else if (event.getType() == BundleEvent.STOPPED) {
				resourceProvider.remove(event.getBundle());
			}
		} else {
			Enumeration<String> keys = bundle.getHeaders().keys();
			while (keys.hasMoreElements()) {
				String key = keys.nextElement();
				if (key.equals(LUNIFERA_TESTBASE)) {
					testBases.add(bundle);
				}
			}
		}
	}

}
