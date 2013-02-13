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
package org.lunifera.runtime.web.http.internal;

import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.eclipse.equinox.http.servlet.ExtendedHttpService;
import org.osgi.service.http.HttpContext;
import org.osgi.service.http.NamespaceException;

public class HttpServiceImpl implements ExtendedHttpService {

	private HttpApplication httpApplication;

	public HttpServiceImpl(HttpApplication httpApplication) {
		this.httpApplication = httpApplication;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void registerServlet(String alias, Servlet servlet,
			Dictionary initparams, HttpContext context)
			throws ServletException, NamespaceException {
		httpApplication.registerServlet(alias, servlet, toMap(initparams));
	}

	@Override
	public void registerResources(String alias, String name, HttpContext context)
			throws NamespaceException {

	}

	@Override
	public void unregister(String alias) {
		httpApplication.unregister(alias);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void registerFilter(String alias, Filter filter,
			Dictionary initparams, HttpContext context)
			throws ServletException, NamespaceException {
		httpApplication.registerFilter(alias, filter, toMap(initparams));
	}

	@Override
	public void unregisterFilter(Filter filter) {
		httpApplication.unregisterFilter(filter);
	}

	@Override
	public HttpContext createDefaultHttpContext() {
		return null;
	}

	public void destroy() {

	}

	/**
	 * Maps the params to a map.
	 * 
	 * @param input
	 * @return
	 */
	private Map<String, String> toMap(final Dictionary<?, ?> input) {
		if (input == null) {
			return null;
		}

		final HashMap<String, String> result = new HashMap<String, String>(
				input.size());
		final Enumeration<?> keys = input.keys();
		while (keys.hasMoreElements()) {
			final Object key = keys.nextElement();
			try {
				result.put((String) key, (String) input.get(key));
			} catch (final ClassCastException e) {
				throw new IllegalArgumentException("Only strings are allowed",
						e);
			}
		}
		return result;
	}

}
