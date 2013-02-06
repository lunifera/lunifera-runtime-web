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

import java.util.Dictionary;

import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.http.HttpService;

/**
 * A http application contains servlets, resources and filters that are
 * registered by a common context path. Additionally it provides a
 * {@link HttpService} for the context path.
 * <p>
 * 
 * @noimplement Should not be implemented by sub classes! Use
 *              {@link HttpApplication}
 */
public interface IHttpApplication {

	public static final String OSGI__PID = "org.lunifera.runtime.web.http.application";

	public static final String OSGI__ID = "lunifera.http.id";
	public static final String OSGI__NAME = "lunifera.http.name";
	public static final String OSGI__CONTEXT_PATH = "lunifera.http.contextPath";

	/**
	 * Returns the id for the application.
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
	 * Is called to start the application. All resources should be registered
	 * and the http service is registered.
	 * <p>
	 * Will register a managed service to configure the application instance.
	 * And also will provide a {@link HttpService}.
	 */
	void start();

	/**
	 * Updates the http application with the given properties.
	 * 
	 * @param properties
	 */
	void updated(Dictionary<String, ?> properties)
			throws ConfigurationException;

	/**
	 * Is called to stop the application. All resources should be unregistered
	 * and the http service will become disposed.
	 * <p>
	 * Will unregister the managed service and the {@link HttpService}.
	 */
	void stop();
}
