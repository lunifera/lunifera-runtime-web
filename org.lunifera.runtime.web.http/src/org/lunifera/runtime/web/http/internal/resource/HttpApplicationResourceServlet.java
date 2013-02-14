/**
 * Copyright (c) 2012 Committers of lunifera.org.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Based on org.eclipse.gyrex.http.jetty.internal.app.ApplicationResourceServlet (Gunnar Wagenknecht)
 * 
 * Contributors:
 *    Florian Pirchner - initial API and implementation
 */
package org.lunifera.runtime.web.http.internal.resource;

import java.net.MalformedURLException;

import javax.servlet.ServletContext;

import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.util.resource.Resource;
import org.lunifera.runtime.web.http.internal.HttpApplicationServletContextHandler;

/**
 * A servlet which serves
 * {@link HttpApplicationServletContextHandler#getResource(String) registered
 * application resources}.
 */
@SuppressWarnings("serial")
public class HttpApplicationResourceServlet extends DefaultServlet {

	final HttpApplicationServletContextHandler servletContextHandler;

	/**
	 * Creates a new instance.
	 */
	public HttpApplicationResourceServlet(
			HttpApplicationServletContextHandler servletContextHandler) {
		this.servletContextHandler = servletContextHandler;
	}

	@Override
	public Resource getResource(final String pathInContext) {
		try {
			return servletContextHandler.getResource(pathInContext);
		} catch (final MalformedURLException e) {
			return null;
		}
	}

	@Override
	protected ContextHandler initContextHandler(
			final ServletContext servletContext) {
		return servletContextHandler;
	}

}
