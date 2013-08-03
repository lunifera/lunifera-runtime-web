/*******************************************************************************
 * Copyright (c) 2012 by committers of lunifera.org
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *    Florian Pirchner - initial API and implementation
 *    
 *******************************************************************************/
package org.lunifera.runtime.web.vaadin.osgi.common;

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.lunifera.runtime.web.vaadin.osgi.webapp.OSGiUIProvider;

/**
 * A wrapper that handles the registration of servlets, resources and filters at
 * the HttpService.
 */
public interface IVaadinApplication {

	/**
	 * Returns the current status of the vaadin application.
	 * 
	 * @return
	 */
	IStatus getStatus();

	/**
	 * Returns the id of that application.
	 * 
	 * @return
	 */
	String getId();

	/**
	 * Returns the name of the application.
	 * 
	 * @return
	 */
	String getName();

	/**
	 * Returns the alias to access the vaadin web UI.
	 * 
	 * @return
	 */
	String getUIAlias();

	/**
	 * Returns the name of the HTTP application that vaadin application should
	 * be deployed at.
	 * 
	 * @return
	 */
	String getHttpApplication();

	/**
	 * Returns the name of the widget set.
	 * 
	 * @return
	 */
	String getWidgetSetName();

	/**
	 * Returns true, if the application is running in production mode.
	 * 
	 * @return
	 */
	boolean isProductionMode();

	/**
	 * Returns a list of ui providers available for the vaadin web application.
	 * 
	 * @return
	 */
	List<OSGiUIProvider> getUiProviders();

	/**
	 * Is called to add an UI provider. Can be called twice with the same
	 * instance. Handle that issue internal.
	 * 
	 * @param uiProvider
	 */
	void addUIProvider(OSGiUIProvider uiProvider);

	/**
	 * Is called to remove an ui provider.
	 * 
	 * @param uiProvider
	 */
	void removeUIProvider(OSGiUIProvider remove);

	/**
	 * Returns true, if the application is started.
	 * 
	 * @return
	 */
	boolean isStarted();

	/**
	 * Starts the application. All required servlets, resources and filters will
	 * be registered at the http service.
	 */
	void start();

	/**
	 * Stops the application. All servlets, resources and filters will be
	 * unregistered at the http service.
	 */
	void stop();

	/**
	 * Destroys the application and prepares it for garbage collection.
	 */
	void destroy();

}
