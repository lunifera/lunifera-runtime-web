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
package org.lunifera.runtime.web.vaadin.osgi;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.lunifera.runtime.web.vaadin.osgi.common.IVaadinApplication;
import org.lunifera.runtime.web.vaadin.osgi.common.VaadinConstants;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.service.log.LogService;

/**
 * Activator for this bundle.
 * 
 * <p/>
 * In order to turn production mode on a configuration must be provided for the
 * application. The PID to use is
 * <code>lunifera.web.vaadin.config.<em>alias</em></code>. See also
 * {@link VaadinConstants#PROP_MANAGED_SERVICE_PREFIX}
 * 
 * <p/>
 * An easy way to provide this configuration is to use FileInstall and create a
 * file of the same name as the PID but with the extension .cfg. e.g.
 * <code>lunifera.web.vaadin.config/guessit</code> would require a file called
 * <code>lunifera.web.vaadin.config.guessit.cfg</code>. The contents of this
 * file would contain the property <code>productionMode=true</code> and any
 * other parameters that would normally passed to the Vaadin servlet as init
 * parameters.
 * 
 * brindy (with help from Neil Bartlett) <br>
 * cvgaviao - Integration with Shiro Security Framework.<br>
 * florian pirchner - migration to vaadin 7
 */
public class Activator implements BundleActivator {

	public static final String BUNDLE_NAME = "org.lunifera.runtime.web.vaadin.osgi";

	private LogService logService;

	private static BundleContext bundleContext;

	public static BundleContext getBundleContext() {
		return bundleContext;
	}

	protected void bindLogService(BundleContext context) {
		ServiceReference<LogService> ref = context
				.getServiceReference(LogService.class);
		logService = context.getService(ref);

		logService.log(LogService.LOG_DEBUG, "Binded LogService.");
	}

	/**
	 * Returns the vaadin web application with the given name.
	 * 
	 * @param name
	 * @return
	 */
	protected static Collection<IVaadinApplication> getVaadinWebApplications() {
		List<IVaadinApplication> services = new ArrayList<IVaadinApplication>();
		try {
			for (ServiceReference<IVaadinApplication> reference : bundleContext
					.getServiceReferences(IVaadinApplication.class, null)) {
				services.add(bundleContext.getService(reference));
			}
		} catch (InvalidSyntaxException e) {
			throw new RuntimeException(e);
		}
		return services;
	}

	/**
	 * Returns the vaadin web application with the given name.
	 * 
	 * @param name
	 * @return
	 */
	public static IVaadinApplication getVaadinWebApplication(String name) {
		try {
			Collection<ServiceReference<IVaadinApplication>> refs = bundleContext
					.getServiceReferences(IVaadinApplication.class,
							"(component.name=" + name + ")");
			if (refs.size() > 0) {
				ServiceReference<IVaadinApplication> ref = refs.iterator()
						.next();
				return bundleContext.getService(ref);
			}
		} catch (InvalidSyntaxException e) {
			throw new RuntimeException(e);
		}

		return null;
	}

	protected LogService getLogService() {
		return logService;
	}

	@Override
	public void start(BundleContext context) throws Exception {

		bundleContext = context;

		// bind the log service
		bindLogService(context);

	}

	@Override
	public void stop(BundleContext context) throws Exception {

		logService = null;
	}

}
