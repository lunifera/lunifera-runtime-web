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
package org.lunifera.runtime.web.vaadin.osgi.common;

import org.osgi.service.cm.ManagedServiceFactory;

/**
 * Constants for that bundle.
 */
public class VaadinConstants {

	/**
	 * The persistence ID used for {@link ManagedServiceFactory}
	 */
	public static final String OSGI_FACTORY_PID = "lunifera.vaadin.application.factory";

	/**
	 * Property for the external PID of vaadin application.
	 */
	public static final String EXTERNAL_PID = "lunifera.externalPid";

	/**
	 * Property for the name of a {@link IHttpApplication}.
	 */
	public static final String APPLICATION_NAME = "lunifera.web.vaadin.name";

	/**
	 * Property for the url alias that is used to access the vaadin UI.
	 * <p>
	 * For instance:<br>
	 * 
	 * <code>host:</code> localhost:8080<br>
	 * <code>context path:</code> app1/test<br>
	 * <code>uialias:</code> vaadinmain<br>
	 * <code>http://localhost:8080/app1/test/vaadinmain</code> will open the
	 * vaadin UI.
	 */
	public static final String UI_ALIAS = "lunifera.web.vaadin.uialias";

	/**
	 * The OSGi property for specifying the widgetset.
	 */
	public static final String WIDGETSET = "lunifera.web.vaadin.widgetset";

	/**
	 * The OSGi property for specifying the productionmode.
	 */
	public static final String PRODUCTIONMODE = "lunifera.web.vaadin.productionmode";

	/**
	 * Property for the name of the IHttpApplication the
	 * {@link IVaadinApplication} should be deployed at.
	 * <p>
	 * See <code>org.lunifera.runtime.web.http.HttpConstants</code>.
	 */
	public static final String HTTP_APPLICATION_NAME = "lunifera.http.name";

	/**
	 * The default name of the vaadin application.
	 */
	public static final String DEFAULT_APPLICATION_NAME = "vaadindefault";

	/**
	 * The default name of the UIAlias.
	 */
	public static final String DEFAULT_UI_ALIAS = "lunifera";

	/**
	 * OSGi property component.factory for the vaadin UI (tab sheet). The
	 * vaadin.ui.class name is part of the factory name and putted after the /.
	 * The class name is required for lazy loading issues.
	 * <p>
	 * Example:
	 * 
	 * factory=
	 * "org.lunifera.web.vaadin.UI/org.lunifera.web.vaadin.example.Vaadin7DemoUI"
	 */
	public static final String OSGI_COMP_FACTORY_VAADIN_UI = "org.lunifera.web.vaadin.UI";

	/**
	 * The prefix of the factory component name before the UI class name starts. <br>
	 * UI-Class name: org.lunifera.web.vaadin.example.Vaadin7DemoUI<br>
	 * Factory name: org.lunifera.web.vaadin.UI/org.lunifera.web.vaadin.example.
	 * Vaadin7DemoUI
	 */
	public static final String PREFIX_UI_CLASS = OSGI_COMP_FACTORY_VAADIN_UI
			+ "/";

}
