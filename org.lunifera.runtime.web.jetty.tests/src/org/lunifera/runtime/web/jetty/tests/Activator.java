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
package org.lunifera.runtime.web.jetty.tests;

import java.util.ArrayList;
import java.util.List;

import org.lunifera.runtime.web.jetty.IJettyService;
import org.lunifera.runtime.web.jetty.IJettyServiceManager;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.service.cm.ManagedService;

public class Activator implements BundleActivator {

	private static Activator instance;
	public static BundleContext context;

	private IJettyServiceManager manager;
	private List<ManagedService> managedServices = new ArrayList<ManagedService>();
	private List<IJettyService> jettyServices = new ArrayList<IJettyService>();

	/**
	 * @return the instance
	 */
	public static Activator getInstance() {
		return instance;
	}

	/**
	 * Returns the jetty manager.
	 * 
	 * @return the manager
	 */
	public IJettyServiceManager getJettyManager() {
		return manager;
	}

	/**
	 * @return the managedServices
	 */
	public List<ManagedService> getManagedServices() {
		return managedServices;
	}

	/**
	 * @return the JettyServices
	 */
	public List<IJettyService> getJettyServices() {
		return jettyServices;
	}

	@Override
	public void start(BundleContext context) throws Exception {
		Activator.context = context;
		instance = this;
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
		 * @param manager
		 */
		public void setManager(IJettyServiceManager manager) {
			Activator.getInstance().manager = manager;
		}

		/**
		 * Called by OSGi-DS
		 * 
		 * @param JettyService
		 */
		public void addJettyService(IJettyService JettyService) {
			Activator.getInstance().jettyServices.add(JettyService);
		}

		/**
		 * Called by OSGi-DS
		 * 
		 * @param JettyService
		 */
		public void removeJettyService(IJettyService JettyService) {
			Activator.getInstance().jettyServices.remove(JettyService);
		}

		/**
		 * Called by OSGi-DS
		 * 
		 * @param service
		 */
		public void addManagedService(ManagedService service) {
			Activator.getInstance().managedServices.add(service);
		}

		/**
		 * Called by OSGi-DS
		 * 
		 * @param service
		 */
		public void removeManagedService(ManagedService service) {
			Activator.getInstance().managedServices.remove(service);
		}

	}
}
