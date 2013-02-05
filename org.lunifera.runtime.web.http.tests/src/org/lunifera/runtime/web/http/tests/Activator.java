package org.lunifera.runtime.web.http.tests;

import java.util.ArrayList;
import java.util.List;

import org.lunifera.runtime.web.http.IHttpApplicationManager;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.service.cm.ManagedService;
import org.osgi.service.http.HttpService;

public class Activator implements BundleActivator {

	private static Activator instance;
	public static BundleContext context;

	private IHttpApplicationManager manager;
	private List<ManagedService> managedServices = new ArrayList<ManagedService>();
	private List<HttpService> httpServices = new ArrayList<HttpService>();

	/**
	 * @return the instance
	 */
	public static Activator getInstance() {
		return instance;
	}

	protected void setHttpManager(IHttpApplicationManager manager) {
		this.manager = manager;
	}

	/**
	 * Returns the http manager.
	 * 
	 * @return the manager
	 */
	public IHttpApplicationManager getHttpManager() {
		return manager;
	}

	/**
	 * @return the managedServices
	 */
	public List<ManagedService> getManagedServices() {
		return managedServices;
	}

	/**
	 * @return the httpServices
	 */
	public List<HttpService> getHttpServices() {
		return httpServices;
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
		public void setManager(IHttpApplicationManager manager) {
			Activator.getInstance().setHttpManager(manager);
		}

		/**
		 * Called by OSGi-DS
		 * 
		 * @param httpService
		 */
		public void addHttpService(HttpService httpService) {
			Activator.getInstance().httpServices.add(httpService);
		}

		/**
		 * Called by OSGi-DS
		 * 
		 * @param httpService
		 */
		public void removeHttpService(HttpService httpService) {
			Activator.getInstance().httpServices.remove(httpService);
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
