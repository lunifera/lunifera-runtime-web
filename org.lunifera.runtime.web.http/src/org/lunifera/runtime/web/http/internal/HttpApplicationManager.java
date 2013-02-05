package org.lunifera.runtime.web.http.internal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Dictionary;
import java.util.List;
import java.util.Map;

import org.lunifera.runtime.web.http.IHttpApplication;
import org.lunifera.runtime.web.http.IHttpApplicationManager;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpApplicationManager implements IHttpApplicationManager,
		ManagedService {

	private static final Logger logger = LoggerFactory
			.getLogger(HttpApplicationManager.class);

	private List<IHttpApplication> applications = Collections
			.synchronizedList(new ArrayList<IHttpApplication>());

	/**
	 * Called by OSGi-DS
	 * 
	 * @param context
	 */
	protected void activate(ComponentContext context) {
		logger.debug("HttpApplicationManager activated");
	}

	/**
	 * Called by OSGi-DS
	 * 
	 * @param context
	 */
	protected void deactivate(ComponentContext context) {
		logger.debug("HttpApplicationManager activated");
	}

	@Override
	public void updated(Dictionary<String, ?> properties)
			throws ConfigurationException {
		
	}

	@Override
	public IHttpApplication getApplication(String id) {
		for (IHttpApplication application : applications
				.toArray(new IHttpApplication[applications.size()])) {
			if (application.getId().intern() == id.intern()) {
				return application;
			}
		}
		return null;
	}

	/**
	 * Called by OSGi-DS to add an application to this manager.
	 * 
	 * @param application
	 * @param properties
	 */
	protected void addApplication(IHttpApplication application,
			Map<String, Object> properties) {

		if (!applications.contains(application)) {
			applications.add(application);
		}

		application.start();
	}

	/**
	 * Called by OSGi-DS to remove an application from this manager.
	 * 
	 * @param application
	 * @param properties
	 */
	protected void removeApplication(IHttpApplication application,
			Map<String, Object> properties) {

		applications.remove(application);
		
		application.stop();
	}

}
