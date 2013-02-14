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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.math.NumberUtils;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.session.HashSessionManager;
import org.eclipse.jetty.server.session.SessionHandler;
import org.eclipse.jetty.util.URIUtil;

public class HttpApplicationServletContextHandler extends
		org.eclipse.jetty.servlet.ServletContextHandler {

	private final HttpApplication application;
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

	public boolean handleApplicationRequest(final HttpServletRequest request,
			final HttpServletResponse response) throws IOException {
		if (!(request instanceof Request))
			throw new IllegalArgumentException(
					"Please ensure that this method is called within the request thread with the original Jetty request and response objects!");
		final Request baseRequest = (Request) request;

		try {
			// calculate target based on current path info
			final String target = baseRequest.getPathInfo();
			// also make sure the path absolute is absolute (required by
			// ServletHandler down the road)
			if ((null == target) || !target.startsWith(URIUtil.SLASH))
				// if not it might indicate a problem higher up the stack, thus,
				// make sure to fail
				// otherwise we might unveil unwanted resources (eg. display
				// directory for wrong folder)
				throw new IllegalArgumentException(
						String.format(
								"Unable to handle request. It seems the specified request is invalid (path info '%s'). At least an absolute path info is necessary in order to determine the request target within the registered application servlets and resources.",
								target != null ? target : ""));
			nextScope(target, baseRequest, baseRequest, response);
		} catch (final ServletException e) {
			throw new RuntimeException(e);
		}
		return baseRequest.isHandled();
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

}
