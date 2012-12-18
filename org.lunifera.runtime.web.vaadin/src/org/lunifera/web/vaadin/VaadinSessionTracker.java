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
 *				- com.c4biz.osgiutils.vaadin.equinox.shiro.VaadinApplicationTracker from Cristiano Gaviao
 *
 * Contributors:
 *    Florian Pirchner - migrated to vaadin 7 and copied into org.lunifera namespace
 *    
 *******************************************************************************/
package org.lunifera.web.vaadin;

import java.util.IdentityHashMap;
import java.util.Map;

import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.ComponentFactory;
import org.osgi.service.http.HttpService;
import org.osgi.service.log.LogService;
import org.osgi.util.tracker.ServiceTracker;

/**
 * This is the tracker which looks for the {@link ComponentFactory}
 * registrations.
 * <p>
 * For each {@link ComponentFactory} found it creates a tracker for
 * {@link HttpService}.
 */
@SuppressWarnings(value = { "rawtypes", "unchecked" })
public class VaadinSessionTracker extends ServiceTracker {

	private final LogService logService;

	private Map<ServiceReference, HttpServiceTracker> trackers = new IdentityHashMap<ServiceReference, HttpServiceTracker>();

	public VaadinSessionTracker(BundleContext ctx, LogService logService)
			throws InvalidSyntaxException {
		super(ctx, ctx.createFilter("(component.factory="
				+ Constants.OSGI_COMP_FACTORY__VAADIN_SESSION + "/*)"), null);
		this.logService = logService;
	}

	@Override
	public Object addingService(ServiceReference reference) {
		Object o = super.addingService(reference);

		if (o instanceof ComponentFactory) {
			ComponentFactory factory = (ComponentFactory) o;
			String name = (String) reference.getProperty("component.factory");
			String alias = name
					.substring(org.lunifera.web.vaadin.Constants.OSGI_COMP_FACTORY__VAADIN_SESSION
							.length());

			HttpServiceTracker tracker = new HttpServiceTracker(this.context,
					factory, alias, logService);
			logService.log(LogService.LOG_DEBUG,
					"The alias that will be tracked is:\"" + alias);

			HttpServiceTracker oldTracker = trackers.put(reference, tracker);

			if (oldTracker != null) {
				oldTracker.close();
				oldTracker = null;
			}

			tracker.open();
		}

		return o;
	}

	@Override
	public void removedService(ServiceReference reference, Object service) {

		HttpServiceTracker tracker = trackers.remove(reference);
		logService.log(LogService.LOG_DEBUG,
				"Tracker for alias" + tracker.getAlias() + " was removed.");
		if (tracker != null) {
			tracker.close();
			tracker = null;
		}
	}
}
