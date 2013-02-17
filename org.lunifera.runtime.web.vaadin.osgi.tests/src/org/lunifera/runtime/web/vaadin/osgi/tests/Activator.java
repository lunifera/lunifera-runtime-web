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
package org.lunifera.runtime.web.vaadin.osgi.tests;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.equinox.http.servlet.ExtendedHttpService;
import org.knowhowlab.osgi.testing.assertions.BundleAssert;
import org.knowhowlab.osgi.testing.assertions.ServiceAssert;
import org.lunifera.runtime.web.vaadin.osgi.common.IVaadinApplication;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.service.cm.ConfigurationAdmin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Activator implements BundleActivator {

	private static final Logger logger = LoggerFactory
			.getLogger(Activator.class);

	private static Activator instance;
	public static BundleContext context;

	private ConfigurationAdmin cmAdmin;
	private List<IVaadinApplication> httpApplications = new ArrayList<IVaadinApplication>();
	private List<ExtendedHttpService> httpServices = new ArrayList<ExtendedHttpService>();

	/**
	 * @return the instance
	 */
	public static Activator getInstance() {
		return instance;
	}

	/**
	 * @return the applications
	 */
	public List<IVaadinApplication> getVaadinApplications() {
		return httpApplications;
	}

	/**
	 * @return the httpServices
	 */
	public List<ExtendedHttpService> getHttpServices() {
		return httpServices;
	}

	/**
	 * Returns the configuration admin service.
	 * 
	 * @return
	 */
	public ConfigurationAdmin getConfigurationAdmin() {
		return cmAdmin;
	}

	@Override
	public void start(BundleContext context) throws Exception {
		Activator.context = context;
		instance = this;

		BundleAssert.setDefaultBundleContext(context);
		ServiceAssert.setDefaultBundleContext(context);
	}

	/**
	 * Returns the bundle with the given id.
	 * 
	 * @param id
	 * @return
	 */
	public static Bundle findBundle(String id) {
		for (Bundle bundle : context.getBundles()) {
			if (bundle.getSymbolicName().equals(id)) {
				return bundle;
			}
		}
		return null;
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		Activator.context = null;
		instance = null;
	}

	/**
	 * OSGi-DS component
	 */
	public static final class Component {

		/**
		 * Called by OSGi-DS
		 * 
		 * @param httpService
		 */
		public void addHttpService(ExtendedHttpService httpService) {
			Activator.getInstance().httpServices.add(httpService);
		}

		/**
		 * Called by OSGi-DS
		 * 
		 * @param httpService
		 */
		public void removeHttpService(ExtendedHttpService httpService) {
			Activator.getInstance().httpServices.remove(httpService);
		}

		/**
		 * Called by OSGi-DS
		 * 
		 * @param vaadinApplication
		 */
		public void addVaadinApplication(IVaadinApplication vaadinApplication) {
			Activator.getInstance().httpApplications.add(vaadinApplication);
		}

		/**
		 * Called by OSGi-DS
		 * 
		 * @param vaadinApplication
		 */
		public void removeVaadinApplication(IVaadinApplication vaadinApplication) {
			Activator.getInstance().httpApplications.remove(vaadinApplication);
		}

		/**
		 * Called by OSGi-DS
		 * 
		 * @param cmAdmin
		 */
		public void setCMAdmin(ConfigurationAdmin cmAdmin) {
			Activator.getInstance().cmAdmin = cmAdmin;
		}

		/**
		 * Called by OSGi-DS
		 * 
		 * @param httpApplication
		 */
		public void unsetCMAdmin(ConfigurationAdmin cmAdmin) {
			Activator.getInstance().cmAdmin = null;
		}

	}
}
