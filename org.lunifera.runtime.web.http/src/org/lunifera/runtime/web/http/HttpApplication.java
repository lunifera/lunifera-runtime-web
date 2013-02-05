package org.lunifera.runtime.web.http;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Map;

import org.lunifera.runtime.web.http.internal.HttpServiceImpl;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceFactory;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.http.HttpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpApplication implements IHttpApplication, ManagedService {

	private static final Logger logger = LoggerFactory
			.getLogger(HttpApplication.class);

	private final HttpServiceServiceFactory httpServiceFactory = new HttpServiceServiceFactory();

	private String id;
	private String name;
	private String contextPath;
	private BundleContext context;
	private ServiceRegistration<?> httpServiceRegistration;
	private boolean started;
	private ServiceRegistration<?> managedServiceRegistration;

	@Override
	public String getId() {
		return id;
	}

	@Override
	public String getContextPath() {
		return contextPath;
	}

	@Override
	public String getName() {
		return name;
	}

	/**
	 * Is called to start the application. All resources should be registered
	 * and the http service is registered.
	 */
	@Override
	public void start() {
		if (started) {
			logger.debug("HttpApplication {} is already started", getId());
			return;
		}
		try {
			internalStart();

			// register http service
			//
			if (httpServiceRegistration == null) {
				Hashtable<String, Object> properties = new Hashtable<String, Object>();
				properties.put(OSGI__ID, getId());
				properties.put(OSGI__NAME, getName());
				properties.put(OSGI__CONTEXT_PATH, getContextPath());
				httpServiceRegistration = context.registerService(
						HttpService.class.getName(), httpServiceFactory,
						properties);
			}

			// register managed service
			//
			if (managedServiceRegistration == null) {
				Hashtable<String, Object> properties = new Hashtable<String, Object>();
				properties.put(Constants.SERVICE_PID, OSGI__PID);
				properties.put(OSGI__ID, getId());
				properties.put(OSGI__NAME, getName());
				properties.put(OSGI__CONTEXT_PATH, getContextPath());
				managedServiceRegistration = context.registerService(
						ManagedService.class.getName(), this, properties);
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
	@Override
	public void stop() {
		if (!started) {
			logger.debug("HttpApplication {} not started", getId());
			return;
		}
		try {

			// unregister the managed service
			//
			if (managedServiceRegistration != null) {
				managedServiceRegistration.unregister();
				managedServiceRegistration = null;
			}

			// unregister http service
			//
			if (httpServiceRegistration != null) {
				httpServiceRegistration.unregister();
				httpServiceRegistration = null;
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
	 * Called by OSGi-DS.
	 * 
	 * @param context
	 * @param properties
	 */
	protected void activate(ComponentContext context,
			Map<String, Object> properties) {
		this.context = context.getBundleContext();
		id = (String) properties.get(Constants.SERVICE_ID);
	}

	/**
	 * Called by OSGi-DS.
	 * 
	 * @param context
	 * @param properties
	 */
	protected void deactivate(ComponentContext context,
			Map<String, Object> properties) {
		if (started) {
			stop();
		}
	}

	/**
	 * Only for testing issues!!!
	 * 
	 * @param context
	 */
	protected void setBundleContext(BundleContext context) {
		this.context = context;
	}

	@Override
	public void updated(Dictionary<String, ?> properties)
			throws ConfigurationException {
		boolean oldStarted = started;

		stop();

		name = (String) properties.get(OSGI__NAME);
		String contextPath = (String) properties.get(OSGI__CONTEXT_PATH);
		if (contextPath == null) {
			this.contextPath = "/";
		} else {
			if (contextPath.startsWith("/")) {
				this.contextPath = contextPath;
			} else {
				this.contextPath = "/" + contextPath;
			}
		}

		if (oldStarted) {
			start();
		}
	}

	/**
	 * An internal factory class to create and destroy http services.
	 * 
	 */
	private static class HttpServiceServiceFactory implements
			ServiceFactory<HttpService> {
		@Override
		public HttpService getService(Bundle bundle,
				ServiceRegistration<HttpService> registration) {
			return new HttpServiceImpl();
		}

		@Override
		public void ungetService(Bundle bundle,
				ServiceRegistration<HttpService> registration,
				HttpService service) {
			((HttpServiceImpl) service).destroy();
		}
	}

}
