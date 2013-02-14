/**
 * Copyright (c) 2012 Committers of lunifera.org.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Based on org.eclipse.gyrex.http.jetty.internal.app.ApplicationHandler (Gunnar Wagenknecht)
 * 
 * Contributors:
 *    Florian Pirchner - initial API and implementation
 */
package org.lunifera.runtime.web.http.internal;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.lang.math.NumberUtils;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.jetty.http.PathMap;
import org.eclipse.jetty.server.session.HashSessionManager;
import org.eclipse.jetty.server.session.SessionHandler;
import org.eclipse.jetty.util.URIUtil;
import org.eclipse.jetty.util.resource.Resource;
import org.lunifera.runtime.web.http.IResourceProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpApplicationServletContextHandler extends
		org.eclipse.jetty.servlet.ServletContextHandler {

	private static final Logger logger = LoggerFactory
			.getLogger(HttpApplicationServletContextHandler.class);

	private final HttpApplication application;
	private final PathMap resourcesMap = new PathMap();
	private HttpApplicationScopeHandler applicationScopeHandler;
	private SessionHandler sessionHandler;

	public HttpApplicationServletContextHandler(HttpApplication application) {
		this.application = application;
		setContextPath(application.getContextPath());
		setDisplayName(application.getName() + " - "
				+ application.getContextPath());
	}

	/**
	 * @return the applicationScopeHandler
	 */
	public HttpApplicationScopeHandler getApplicationScopeHandler() {
		return applicationScopeHandler;
	}

	@Override
	protected void startContext() throws Exception {
		// create remaining the handlers
		applicationScopeHandler = new HttpApplicationScopeHandler(application);
		// setup the handler chain before super initialization
		// session -> application -> registered servlets/resources
		sessionHandler = createSessionHandler();
		setHandler(sessionHandler);
		sessionHandler.setHandler(applicationScopeHandler);
		applicationScopeHandler.setHandler(_servletHandler);

		super.startContext();
	}

	private SessionHandler createSessionHandler() {
		// make sure the set a proper session inactive interval
		// otherwise Jetty will keep sessions open forever
		final HashSessionManager sessionManager = new HashSessionManager();
		sessionManager.setMaxInactiveInterval(NumberUtils.toInt(
				getInitParameter("session.maxInactiveInterval"), 1800));
		return new SessionHandler(sessionManager);
	}

	/**
	 * Creates a new session handler.
	 * 
	 * @return the new session handler instance
	 */
	protected SessionHandler newSessionHandler() {
		final HashSessionManager sessionManager = new HashSessionManager();
		sessionManager.setMaxInactiveInterval(NumberUtils.toInt(
				getInitParameter("session.maxInactiveInterval"), 1800));
		return new SessionHandler(sessionManager);
	}

	/**
	 * Adds a new resource to the servlet context.
	 * 
	 * @param alias
	 * @param provider
	 */
	public void addResource(String alias, IResourceProvider provider) {
		resourcesMap.put(alias, provider);
	}

	/**
	 * Removes the resource with the given alias from the servlet context.
	 * 
	 * @param alias
	 */
	public void removeResource(String alias) {
		resourcesMap.remove(alias);
	}

	/**
	 * Returns the count of the resources contained.
	 * 
	 * @return
	 */
	public int getResourceCount() {
		return resourcesMap.size();
	}

	@Override
	public Resource getResource(String path) throws MalformedURLException {
		// data structure which maps a request to a resource provider;
		// first-best match wins
		// { path => resource provider }
		if (resourcesMap.isEmpty() || (null == path)
				|| !path.startsWith(URIUtil.SLASH)) {
			return null;
		}

		path = URIUtil.canonicalPath(path);
		final PathMap.Entry entry = resourcesMap.getMatch(path);
		if (null == entry)
			return null;
		final IResourceProvider provider = (IResourceProvider) entry.getValue();
		if (null == provider)
			return null;

		final String pathSpec = (String) entry.getKey();
		final String pathInfo = PathMap.pathInfo(pathSpec, path);
		final URL resourceUrl = provider.getResource(pathInfo);
		if (null == resourceUrl)
			return null;
		try {
			final URL fileURL = FileLocator.toFileURL(resourceUrl);
			return Resource.newResource(fileURL);
		} catch (final IOException e) {
			logger.warn("Error resolving url {} to file based resource. {}",
					resourceUrl.toExternalForm(), e.getMessage());
			return null;
		}
	}

}
