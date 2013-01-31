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
package org.lunifera.runtime.web.vaadin.standalone.common;

import java.util.Dictionary;
import java.util.List;

import org.lunifera.web.vaadin.common.OSGiUIProvider;

public interface IVaadinWebApplication {

	/**
	 * Returns the alias path, the application is registered at.
	 * 
	 * @return
	 */
	String getAlias();

	/**
	 * Returns the name of the web app.
	 * 
	 * @return
	 */
	String getName();

	/**
	 * Returns the name of the widget set.
	 * 
	 * @return
	 */
	String getWidgetSetName();

	/**
	 * Returns a list of ui providers available for the vaadin web application.
	 * 
	 * @return
	 */
	List<OSGiUIProvider> getUiProviders();

	/**
	 * Updates the web application with the new values.
	 * 
	 * @param properties
	 */
	@SuppressWarnings("rawtypes")
	public void updated(Dictionary properties);

	/**
	 * Is called to add an ui provider.
	 * 
	 * @param uiProvider
	 */
	void addOSGiUIProvider(OSGiUIProvider uiProvider);

	/**
	 * Is called to remove an ui provider.
	 * 
	 * @param uiProvider
	 */
	void removeOSGiUIProvider(OSGiUIProvider remove);

}
