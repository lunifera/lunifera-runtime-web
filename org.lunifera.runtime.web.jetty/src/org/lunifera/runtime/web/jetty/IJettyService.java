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
package org.lunifera.runtime.web.jetty;

import java.util.Dictionary;

import org.lunifera.runtime.web.jetty.internal.JettyService;
import org.osgi.service.cm.ConfigurationException;

/**
 * <s abstraction of a jetty server service that handles a jetty server
 * internally.
 * <p>
 * 
 * @noimplement Should not be implemented by sub classes! Use
 *              {@link JettyService}
 */
public interface IJettyService {

	public static final String OSGI__PID = "org.lunifera.runtime.web.jetty.service";

	public static final String OSGI__ID = "lunifera.jetty.id";
	public static final String OSGI__NAME = "lunifera.jetty.name";
	public static final String OSGI__PORT = "lunifera.jetty.port";

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
	 * Returns the port.
	 * 
	 * @return
	 */
	int getPort();

	/**
	 * Returns true, if the server was started.
	 * 
	 * @return
	 */
	boolean isStarted();

	/**
	 * Is called to start the jetty server. Will register a managed service to
	 * configure the jetty instance. And will also register a {@link IJetty}.
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
	 * Is called to stop the jetty server. Will deregister a managed service to
	 * configure the jetty instance. And will also deregister the {@link IJetty}
	 * .
	 */
	void stop();
}
