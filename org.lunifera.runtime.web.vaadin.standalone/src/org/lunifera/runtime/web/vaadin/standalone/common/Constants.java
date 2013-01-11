/*******************************************************************************
 * Copyright (c) 2012 by committers of lunifera.org

 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 *******************************************************************************/
package org.lunifera.runtime.web.vaadin.standalone.common;

public class Constants extends org.lunifera.web.vaadin.common.Constants {

	/**
	 * This prefix is used as PID prefix to register the application register as
	 * a managed service. Concating the alias will result in the PID.
	 */
	public static final String PROP_MANAGED_SERVICE_PREFIX = "lunifera.web.vaadin.config";

	/**
	 * The name of the web application
	 */
	public static final String PROP_COMP_NAME__WEBAPPLICATION = "lunifera.web.vaadin.webapplication";

}
