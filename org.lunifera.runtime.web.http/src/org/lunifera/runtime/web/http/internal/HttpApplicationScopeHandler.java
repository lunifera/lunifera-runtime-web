/**
 * Copyright (c) 2012 Committers of lunifera.org.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Based on org.eclipse.gyrex.http.jetty.internal.app.ApplicationDelegateHandler (Gunnar Wagenknecht)
 * 
 * Contributors:
 *    Florian Pirchner - initial API and implementation
 */
package org.lunifera.runtime.web.http.internal;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.ScopedHandler;
import org.eclipse.jetty.util.URIUtil;
import org.lunifera.runtime.web.http.IHttpApplication;
import org.omg.CORBA.portable.ApplicationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This scope handler will delegate calls to
 * {@link #doScope(String, Request, HttpServletRequest, HttpServletResponse)
 * doScope} to the {@link IHttpApplication http application} this handler is
 * assigned to. So the http application can itself manipulate the request, do
 * security stuff,...<br>
 * The {@link IHttpApplication http application} will invoke
 * {@link #handleHttpApplicationCallback(HttpServletRequest, HttpServletResponse)
 * handleHttpApplicationCallback} at this handler instance. And this handler
 * will forward the request to the
 * {@link #nextScope(String, Request, HttpServletRequest, HttpServletResponse)
 * nextScope}.
 */
public class HttpApplicationScopeHandler extends ScopedHandler {

	private static final Logger logger = LoggerFactory
			.getLogger(HttpApplicationScopeHandler.class);

	private final HttpApplication httpApplication;

	public HttpApplicationScopeHandler(HttpApplication httpApplication) {
		super();
		this.httpApplication = httpApplication;
	}

	@Override
	public void doScope(String target, Request baseRequest,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		if (!httpApplication.isStarted()) {
			throw new UnavailableException(
					String.format(
							"HttpApplication %s on context path %s is not started!",
							httpApplication.getName(),
							httpApplication.getContextPath()));
		}

		// forward the all to the http application.
		httpApplication.handleRequest(request, response);

		// mark the request handled (if this point is reached)
		baseRequest.setHandled(true);
	}

	@Override
	public void doHandle(String target, Request baseRequest,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		nextHandle(target, baseRequest, request, response);
	}

	/**
	 * Is called by the HttpApplication after
	 * {@link #doScope(String, Request, HttpServletRequest, HttpServletResponse)
	 * doScope} delegated to it.
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ApplicationException
	 */
	public boolean handleHttpApplicationCallback(
			final HttpServletRequest request, final HttpServletResponse response)
			throws IOException, ServletException {
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
								StringUtils.trimToEmpty(target)));
			nextScope(target, baseRequest, baseRequest, response);
		} catch (final ServletException e) {
			logger.error("{}", e);
			throw e;
		}
		return baseRequest.isHandled();
	}
}
