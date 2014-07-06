/**
 * Copyright (c) 2011 - 2014, Lunifera GmbH (Gross Enzersdorf), Loetz KG (Heidelberg)
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: 
 * 		Florian Pirchner - Initial implementation
 */
package org.lunifera.runtime.web.common;

public interface IConstants {

	/**
	 * OSGi property used to define unique id of web context.
	 */
	public static final String OSGI_PROPERTY__WEB_CONTEXT__ID = "lunifera.web.common.context.id";

	/**
	 * OSGi property to define user id of web context.
	 */
	public static final String OSGI_PROPERTY__WEB_CONTEXT__USER = "lunifera.web.common.context.user";

	/**
	 * OSGi property to define the vendor of the ui kit.
	 */
	public static final String OSGI_PROPERTY__WEB_CONTEXT__VENDOR = "lunifera.web.common.context.vendor";
}
