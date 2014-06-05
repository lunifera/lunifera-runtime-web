/**
 * Copyright 2013 Lunifera GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.lunifera.runtime.web.ecview.presentation.vaadin.tests.ui;

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

public class Activator implements BundleActivator,
ServiceTrackerCustomizer<HttpService, HttpService>, BundleListener {

	private static BundleContext context;
	private static Activator plugin;

	static BundleContext getContext() {
		return context;
	}
	
	private ServiceTracker<HttpService, HttpService> tracker;

	// used to register servlets
	private HttpService httpService;
	private ResourceProvider resourceProvider;

	/**
	 * @return the plugin
	 */
	public static Activator getPlugin() {
		return plugin;
	}
	
	//
	// Helper methods to get an instance of the http service
	//
	@Override
	public HttpService addingService(ServiceReference<HttpService> reference) {
		httpService = context.getService(reference);

		try {
			// register the servlet at the http service
			httpService.registerServlet("/", new ECViewTestsServlet(), null,
					resourceProvider);
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
		httpService.unregister("/");
	}

	@Override
	public void modifiedService(ServiceReference<HttpService> reference,
			HttpService service) {

	}

	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		Activator.plugin = this;

		resourceProvider = new ResourceProvider();

		handleStartedBundles(context);

		// register this instance as a bundle listener to an reference to all
		// vaadin bundles. Used to find the static resources.
		bundleContext.addBundleListener(this);

		// Start a HttpService-Tracker to get an instance of HttpService
				tracker = new ServiceTracker<>(bundleContext, HttpService.class, this);
				tracker.open();
	}

	public void stop(BundleContext bundleContext) throws Exception {
		// close the HttpService-tracker
				tracker.close();
				tracker = null;

		
		resourceProvider = null;

		bundleContext.removeBundleListener(this);

		Activator.context = null;
		Activator.plugin = null;
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
					&& (name.startsWith("com.vaadin") || name
							.equals("org.lunifera.runtime.web.ecview.presentation.vaadin.tests.ui"))) {
				resourceProvider.add(bundle);
			}
		}
	}

	@Override
	public void bundleChanged(BundleEvent event) {
		// tracks the starting and stopping of vaadin bundles. If a bundle is a
		// vaadin bundle it will be added to the resource provider for lookups.
		String name = event.getBundle().getSymbolicName();
		if (name.startsWith("com.vaadin")
				|| name.equals("org.lunifera.runtime.web.ecview.presentation.vaadin.tests.ui")) {
			if (event.getType() == BundleEvent.STARTED) {
				resourceProvider.add(event.getBundle());
			} else if (event.getType() == BundleEvent.STOPPED) {
				resourceProvider.remove(event.getBundle());
			}
		}
	}

}
