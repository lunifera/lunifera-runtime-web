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

import org.lunifera.runtime.web.jetty.JettyConstants;

public interface HttpConstants extends org.osgi.framework.Constants {

	public static final String OSGI__FACTORY_PID = "org.lunifera.runtime.web.http.application.factory";

	/**
	 * The id of the http application.
	 */
	public static final String APPLICATION_ID = "lunifera.http.id";

	/**
	 * The name of the http application.
	 */
	public static final String APPLICATION_NAME = "lunifera.http.name";

	/**
	 * The context path that is used for the http application.
	 */
	public static final String CONTEXT_PATH = "lunifera.http.contextPath";

	/**
	 * Is used to define the jetty server name the http application should
	 * become installed at.
	 */
	public static final String JETTY_SERVER_NAME = JettyConstants.JETTY_SERVER_NAME;

	public static final String DEFAULT_APPLICATION_NAME = "defaultapplication";
	public static final String DEFAULT_APPLICATION_CONTEXT_PATH = "/";
}
