/*******************************************************************************
 * Copyright (c) 2012 by committers of lunifera.org
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Information:
 * 		Based on original sources of 
 * 				- org.vaadin.osgi.Activator from Chris Brind
 *				- com.c4biz.osgiutils.vaadin.equinox.shiro.HttpServiceTracker from Cristiano Gaviao
 *
 * Contributors:
 *    Florian Pirchner - migrated to vaadin 7 and copied into org.lunifera namespace
 *    
 *******************************************************************************/
package org.lunifera.runtime.web.vaadin.standalone.webapp;

import java.util.Hashtable;
import java.util.IdentityHashMap;
import java.util.Map;

import org.eclipse.equinox.http.servlet.ExtendedHttpService;
import org.lunifera.runtime.web.vaadin.standalone.common.Constants;
import org.lunifera.runtime.web.vaadin.standalone.common.IVaadinWebApplication;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.osgi.service.component.ComponentFactory;
import org.osgi.service.log.LogService;
import org.osgi.util.tracker.ServiceTracker;

/**
 * This tracker takes a {@link ComponentFactory} and then creates a
 * {@link VaadinWebApplicationRegister} class which is then registered as a
 * {@link ManagedService} to receive configuration for that specific
 * application.
 * 
 * @author brindy - initial contribution cvgaviao - using ExtendedHttpService
 *         that added support to filters
 */
@SuppressWarnings(value = { "rawtypes", "unchecked" })
public class HttpServiceTracker extends ServiceTracker {

	private IVaadinWebApplication webApplication;

	public String getAlias() {
		return webApplication.getAlias();
	}

	private final LogService logService;
	private Map<ExtendedHttpService, VaadinWebApplicationRegister> configs = new IdentityHashMap<ExtendedHttpService, VaadinWebApplicationRegister>();

	public HttpServiceTracker(BundleContext ctx,
			IVaadinWebApplication webApplication, LogService logService) {
		super(ctx, ExtendedHttpService.class.getName(), null);
		this.webApplication = webApplication;
		this.logService = logService;
	}

	@Override
	public Object addingService(ServiceReference reference) {
		ExtendedHttpService http = (ExtendedHttpService) super
				.addingService(reference);

		// register the application
		VaadinWebApplicationRegister config = new VaadinWebApplicationRegister(
				http, webApplication);

		logService.log(LogService.LOG_DEBUG, "Application for alias \""
				+ getAlias() + "\" was created.");

		// save it for later
		configs.put(http, config);

		// register as a managed service so that alternative properties can
		// be provided
		Hashtable<String, String> properties = new Hashtable<String, String>();
		properties.put(org.osgi.framework.Constants.SERVICE_PID,
				Constants.PROP_MANAGED_SERVICE_PREFIX + "." + getAlias());
		context.registerService(ManagedService.class.getName(), config,
				properties);

		try {
			config.updated(null);
		} catch (ConfigurationException e) {
			logService.log(LogService.LOG_WARNING,
					"Initial setup caused exception: !" + e);
		}

		return http;
	}

	@Override
	public void removedService(ServiceReference reference, Object service) {
		configs.remove(service).kill();
		logService.log(LogService.LOG_DEBUG, "Application for alias \""
				+ getAlias() + "\" was removed.");

		super.removedService(reference, service);
	}

}
