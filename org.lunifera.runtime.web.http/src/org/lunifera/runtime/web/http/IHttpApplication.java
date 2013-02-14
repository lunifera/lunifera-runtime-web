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
package org.lunifera.runtime.web.http;

import javax.servlet.ServletContext;

import org.lunifera.runtime.web.jetty.IHandlerProvider;
import org.lunifera.runtime.web.jetty.IJetty;
import org.osgi.service.http.HttpService;

/**
 * A http application contains servlets, resources and filters for a common
 * context path. These artifacts can be registered using a {@link HttpService}.
 * A http application also provides an {@link IHandlerProvider} that is used by
 * the jetty service bundle to add the http application as a
 * {@link ServletContext}.
 * 
 * <h2>HttpService</h2><br>
 * The {@link HttpService} is provided as an OSGi service and can be accessed
 * using OSGi service filters.<br>
 * Filters:
 * <ul>
 * <li>{@link HttpConstants#APPLICATION_ID lunifera.http.id} - filter by
 * application id</li>
 * <li>{@link HttpConstants#APPLICATION_NAME lunifera.http.name} - filter by
 * application name</li>
 * <li>{@link HttpConstants#CONTEXT_PATH lunifera.http.contextPath} - filter by
 * context path</li>
 * <li>{@link HttpConstants#JETTY_SERVER_NAME lunifera.jetty.name} - filter by
 * the target jetty server</li>
 * </ul>
 * For instance:<br>
 * <code>(lunifera.http.contextPath=/app1/test)</code>
 * <p>
 * <h2>IHandlerProvider</h2><br>
 * The {@link IHandlerProvider} is provided as an OSGi service and can be
 * accessed using OSGi service filters.<br>
 * Filters:
 * <ul>
 * <li>{@link HttpConstants#APPLICATION_ID lunifera.http.id} - filter by
 * application id</li>
 * <li>{@link HttpConstants#APPLICATION_NAME lunifera.http.name} - filter by
 * application name</li>
 * <li>{@link HttpConstants#CONTEXT_PATH lunifera.http.contextPath} - filter by
 * context path</li>
 * <li>{@link HttpConstants#JETTY_SERVER_NAME lunifera.jetty.name} - filter by
 * the target jetty server</li>
 * </ul>
 * For instance a {@link IJetty jetty server} with name 'Server1' will use a
 * filter like the following to look for assigned {@link IHandlerProvider}:<br>
 * <code>(lunifera.jetty.name=Server1)</code>
 * <p>
 * 
 * @noimplement Should not be implemented by clients!
 */
public interface IHttpApplication {

	/**
	 * Returns the id of that http application. Can not be changed.
	 * 
	 * @return
	 */
	String getId();

	/**
	 * Returns the name for the application.
	 * 
	 * @return
	 */
	String getName();

	/**
	 * Returns the context path.
	 * 
	 * @return
	 */
	String getContextPath();

	/**
	 * Returns the name of the jetty server this http application should be
	 * installed at.
	 * 
	 * @return
	 */
	String getJettyServer();

	/**
	 * Returns true, if the application is started.
	 * 
	 * @return
	 */
	boolean isStarted();

}
