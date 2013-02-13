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

import org.lunifera.runtime.web.http.internal.HttpApplication;
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
