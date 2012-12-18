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
 *				- com.c4biz.osgiutils.vaadin.equinox.shiro.ApplicationRegister from Cristiano Gaviao
 *
 * Contributors:
 *    Florian Pirchner - migrated to vaadin 7 and copied into org.lunifera namespace
 *    
 *******************************************************************************/
package org.lunifera.web.vaadin;

import java.util.Dictionary;

import javax.servlet.Filter;

import org.apache.shiro.web.servlet.IniShiroFilter;
import org.eclipse.equinox.http.servlet.ExtendedHttpService;
import org.lunifera.web.vaadin.servlet.VaadinOSGiServlet;
import org.lunifera.web.vaadin.servlet.WebResourcesHttpContext;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.osgi.service.component.ComponentFactory;
import org.osgi.service.http.HttpContext;

/**
 * This class is responsible for registering the {@link ComponentFactory} as a
 * vaadin {@link VaadinSession}. It is a {@link ManagedService} so that it can
 * receive properties which are then passed in to the {@link VaadinOSGiServlet}
 * as init parameters, e.g. to enable production mode.
 */
@SuppressWarnings("deprecation")
public class SessionRegister implements ManagedService {

	private final ExtendedHttpService http;

	private final ComponentFactory factory;

	private final String alias;

	private VaadinOSGiServlet servlet;

	private Filter filter;

	private final String RESOURCE_BASE = "/VAADIN";

	public SessionRegister(ExtendedHttpService http,
			ComponentFactory factory, String alias) {
		super();
		this.http = http;
		this.factory = factory;
		this.alias = alias;
	}

	public void kill() {
		if (filter != null) {
			http.unregisterFilter(filter);
			filter = null;
		}
		if (alias != null) {
			try {
				http.unregister(alias);
				http.unregister(RESOURCE_BASE);
			} catch (java.lang.IllegalArgumentException e) {
				// ignore in case alias was not found exception
			}
		}
		if (servlet != null) {
			servlet.destroy();
			servlet = null;
		}
	}

	@Override
	public void updated(@SuppressWarnings("rawtypes") Dictionary properties)
			throws ConfigurationException {
		kill();

		try {
			servlet = new VaadinOSGiServlet(factory, properties);

			HttpContext defaultContext = new WebResourcesHttpContext(
					Activator.bundleContext.getBundle());
			http.registerFilter("/", getSecurityFilter(), properties,
					defaultContext);
			http.registerResources(RESOURCE_BASE, RESOURCE_BASE, defaultContext);
			http.registerServlet(alias, servlet, properties, defaultContext);

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private Filter getSecurityFilter() {
		if (filter == null) {
			filter = new IniShiroFilter();
		}
		return filter;
	}
}
